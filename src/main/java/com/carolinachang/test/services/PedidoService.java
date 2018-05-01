package com.carolinachang.test.services;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carolinachang.test.domain.Pedido;
import com.carolinachang.test.repositories.PedidoRepository;
import com.carolinachang.test.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repositorioPedido;
	
	public Pedido buscar(Integer id) {
		Optional<Pedido> obj = repositorioPedido.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName(), null));
		
	}
}
