package com.aulas.cursoangular.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aulas.cursoangular.domain.Cliente;
import com.aulas.cursoangular.domain.ItemPedido;
import com.aulas.cursoangular.domain.PagamentoComBoleto;
import com.aulas.cursoangular.domain.Pedido;
import com.aulas.cursoangular.enums.EstadoPagamento;
import com.aulas.cursoangular.repositories.ItemPedidoRepository;
import com.aulas.cursoangular.repositories.PagamentoRepository;
import com.aulas.cursoangular.repositories.PedidoRepository;
import com.aulas.cursoangular.security.UserSS;
import com.aulas.cursoangular.services.exceptions.AuthorizationException;
import com.aulas.cursoangular.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	
	public Pedido buscar(Integer id) {
		Pedido obj = pedidoRepository.findOne(id);
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id:" + id + ", Tipo: " + Pedido.class.getName()); 
		}
		return obj;
	}

	@Transactional
	public Pedido inserir(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date()); 
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		obj.setCliente(clienteService.buscar(obj.getCliente().getId()));
		
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = pedidoRepository.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.buscar(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreço());
			ip.setPedido(obj);
		}
		
		itemPedidoRepository.save(obj.getItens());
		
		emailService.sendOrderConfirmationEmail(obj);

		return obj;
	}
	
	public Page<Pedido> buscarPagina(Integer page, Integer linesPerPage, String orderBy, String direction){
		
		UserSS user = UserService.authenticated();
		
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Cliente cliente = clienteService.buscar(user.getId());
		
		return pedidoRepository.findByCliente(cliente, pageRequest);
				
	}
	
	
}
