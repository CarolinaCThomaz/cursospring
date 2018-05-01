package com.carolinachang.test.services;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carolinachang.test.domain.Cliente;
import com.carolinachang.test.repositories.ClienteRepository;
import com.carolinachang.test.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repositorioCliente;
	
	public Cliente buscar(Integer id) {
		Optional<Cliente> obj = repositorioCliente.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName(), null));
		
	}
}
