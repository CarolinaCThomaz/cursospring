package com.carolinachang.test.services;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.carolinachang.test.domain.Categoria;
import com.carolinachang.test.domain.Cliente;
import com.carolinachang.test.dto.CategoriaDTO;
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
		Categoria categoria = find(obj.getId());
		updateData(categoria,obj);
		return repositorioCategoria.save(categoria);
	}

	private void updateData(Categoria categoria, Categoria obj) {
		categoria.setNome(obj.getNome());
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
	
	public Page<Categoria> findPage(Integer page,Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest =  PageRequest.of(page, linesPerPage,Direction.valueOf(direction), orderBy);
		return repositorioCategoria.findAll(pageRequest);
	}
	
	public Categoria fromDTO(CategoriaDTO categoriaDTO) {
		return new Categoria(categoriaDTO.getId(),categoriaDTO.getNome());
	}
}
