package com.aulas.cursoangular.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aulas.cursoangular.domain.Cidade;
import com.aulas.cursoangular.domain.Cliente;
import com.aulas.cursoangular.domain.Endereco;
import com.aulas.cursoangular.dto.ClienteDTO;
import com.aulas.cursoangular.dto.ClienteNewDTO;
import com.aulas.cursoangular.enums.Perfil;
import com.aulas.cursoangular.enums.TipoCliente;
import com.aulas.cursoangular.repositories.ClienteRepository;
import com.aulas.cursoangular.repositories.EnderecoRepository;
import com.aulas.cursoangular.security.UserSS;
import com.aulas.cursoangular.services.exceptions.AuthorizationException;
import com.aulas.cursoangular.services.exceptions.DataIntegrityException;
import com.aulas.cursoangular.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder bc;
	
	public Cliente buscar(Integer id) {
		
		UserSS user = UserService.authenticated();
		
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Cliente obj = clienteRepository.findOne(id);
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id:" + id + ", Tipo: " + Cliente.class.getName()); 
		}
		return obj;
	}
	
	@Transactional
	public Cliente inserir(Cliente obj) {
		obj.setId(null);
		obj = clienteRepository.save(obj);
		enderecoRepository.save(obj.getEnderecos());
		return obj;
	} 
	
	
	public Cliente atualizar(Cliente obj) {
		Cliente newObj = buscar(obj.getId());
		updateData(newObj, obj);
		return clienteRepository.save(newObj);
	}
	
	public void delete(Integer id) {
		buscar(id);
		try {
			clienteRepository.delete(id);		
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há entidades relacionadas");
		}
	
	}

	public List<Cliente> listarTodas() {
		return clienteRepository.findAll();
	}
	
	public Cliente findByEmail(String email) {
		UserSS user = UserService.authenticated();
		
		if(user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Cliente obj = clienteRepository.findByEmail(email);
		
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id:" + user.getId() + ", Tipo: " + Cliente.class.getName()); 
		}
		
		return obj;
	}
	
	
	public Page<Cliente> buscarPagina(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest); 
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}  
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipoCliente()), bc.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		
		if(objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		
		if(objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		
		return cli;
		
	}  
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	 
}
