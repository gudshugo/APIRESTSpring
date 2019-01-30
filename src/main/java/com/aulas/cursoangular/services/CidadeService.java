package com.aulas.cursoangular.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aulas.cursoangular.domain.Cidade;
import com.aulas.cursoangular.repositories.CidadeRepository;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	public List<Cidade> cidadePorEstado(Integer estadoId){
		return cidadeRepository.findCidades(estadoId);
	}
	
	
}
