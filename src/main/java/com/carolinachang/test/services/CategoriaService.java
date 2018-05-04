package com.carolinachang.test.services;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.carolinachang.test.domain.Categoria;
import com.carolinachang.test.repositories.CategoriaRepository;
import com.carolinachang.test.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repositorioCategoria;
	
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repositorioCategoria.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName(), null));
		
	}
	
	public Categoria insert(Categoria obj) {
		return repositorioCategoria.save(obj);
	}

	public Categoria update(Categoria obj) {
		find(obj.getId());
		return repositorioCategoria.save(obj);
	}

	public void delete(Integer id) {
		find(id);
		try {
			repositorioCategoria.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Não é possível excluir categoria");
		}
		
		
	}

	public List<Categoria> findAll() {
		return repositorioCategoria.findAll();
		
	}
}
