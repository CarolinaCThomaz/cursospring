package com.carolinachang.test.services;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carolinachang.test.domain.Categoria;
import com.carolinachang.test.repositories.CategoriaRepository;
import com.carolinachang.test.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repositorioCategoria;
	
	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repositorioCategoria.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName(), null));
		
	}
}
