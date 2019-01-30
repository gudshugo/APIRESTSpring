package com.aulas.cursoangular.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.aulas.cursoangular.domain.Categoria;
import com.aulas.cursoangular.dto.CategoriaDTO;
import com.aulas.cursoangular.repositories.CategoriaRepository;
import com.aulas.cursoangular.services.exceptions.DataIntegrityException;
import com.aulas.cursoangular.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Categoria buscar(Integer id) {
		Categoria obj = categoriaRepository.findOne(id);
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id:" + id + ", Tipo: " + Categoria.class.getName()); 
		}
		return obj;
	}
	
	public Categoria inserir(Categoria obj) {
		obj.setId(null);
		return categoriaRepository.save(obj);
	} 
	
	public Categoria atualizar(Categoria obj) {
		Categoria newObj = buscar(obj.getId());
		updateData(newObj, obj);
		return categoriaRepository.save(newObj);
	}
	
	public void delete(Integer id) {
		buscar(id);
		try {
			categoriaRepository.delete(id);		
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		}
	
	}

	public List<Categoria> listarTodas() {
		return categoriaRepository.findAll();
	}
	
	public Page<Categoria> buscarPagina(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return categoriaRepository.findAll(pageRequest); 
	}
	
	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}
	
	private void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}
	
}
