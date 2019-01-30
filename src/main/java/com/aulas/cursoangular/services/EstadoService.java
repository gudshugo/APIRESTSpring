package com.aulas.cursoangular.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aulas.cursoangular.domain.Estado;
import com.aulas.cursoangular.repositories.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	public List<Estado> buscaTodos(){
		return estadoRepository.findAllByOrderByNome();
	}
	
}
