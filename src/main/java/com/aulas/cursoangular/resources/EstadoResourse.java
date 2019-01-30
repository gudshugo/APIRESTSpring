package com.aulas.cursoangular.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aulas.cursoangular.domain.Cidade;
import com.aulas.cursoangular.domain.Estado;
import com.aulas.cursoangular.dto.CidadeDTO;
import com.aulas.cursoangular.dto.EstadoDTO;
import com.aulas.cursoangular.services.CidadeService;
import com.aulas.cursoangular.services.EstadoService;

@RestController
@RequestMapping(value="/estados")
public class EstadoResourse {
	
	@Autowired
	private EstadoService estadoService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> listaEstados(){
		 List<Estado> list = estadoService.buscaTodos();
		 List<EstadoDTO> listDto = list.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());
		 return ResponseEntity.ok().body(listDto);
	}
	
	@RequestMapping(value="/{estadoId}/cidades", method=RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> listaCidades(@PathVariable Integer estadoId){
		List<Cidade> list = cidadeService.cidadePorEstado(estadoId);
		List<CidadeDTO> listDto = list.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}
	
	
}
