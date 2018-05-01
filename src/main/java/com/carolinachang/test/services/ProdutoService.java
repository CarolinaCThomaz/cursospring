package com.carolinachang.test.services;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carolinachang.test.domain.Produto;
import com.carolinachang.test.repositories.ProdutoRepository;
import com.carolinachang.test.services.exception.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repositorioProduto;
	
	public Produto buscar(Integer id) {
		Optional<Produto> obj = repositorioProduto.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName(), null));
		
	}
}
