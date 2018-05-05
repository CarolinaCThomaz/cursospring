package com.carolinachang.test.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.carolinachang.test.domain.Categoria;
import com.carolinachang.test.domain.Produto;
import com.carolinachang.test.repositories.CategoriaRepository;
import com.carolinachang.test.repositories.ProdutoRepository;
import com.carolinachang.test.services.exception.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repositorioProduto;
	
	@Autowired
	private CategoriaRepository repositorioCategoria;
	
	public Produto find(Integer id) {
		Optional<Produto> obj = repositorioProduto.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName(), null));
		
	}
	
	public Page<Produto> search(String nome, List<Integer> ids,Integer page,Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest =  PageRequest.of(page, linesPerPage,Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = repositorioCategoria.findAllById(ids);
			return repositorioProduto.findDistinctByNomeContainingAndCategoriasIn(nome,categorias,pageRequest);
	}
}
