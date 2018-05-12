package com.carolinachang.test.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.carolinachang.test.domain.Cidade;
import com.carolinachang.test.domain.Estado;
import com.carolinachang.test.dto.CidadeDTO;
import com.carolinachang.test.dto.EstadoDTO;
import com.carolinachang.test.repositories.CidadeRepository;
import com.carolinachang.test.repositories.EstadoRepository;
import com.carolinachang.test.services.CidadeService;

@RestController
@RequestMapping(value="/estados")
public class EstadoResource {

	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeService cidadeService;
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> findAll(){
		List<Estado> list = estadoRepository.findAll();
		List<EstadoDTO> listDto = list.stream().map(cat -> new EstadoDTO(cat)).collect(Collectors.toList());
		return ResponseEntity.ok(listDto);
	}
	
	@RequestMapping(value="/{estadoId}/cidades" ,method=RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer estadoId){
		List<Cidade> list = cidadeService.findByEstado(estadoId);
		List<CidadeDTO> listDto = list.stream().map(cat -> new CidadeDTO(cat)).collect(Collectors.toList());
		return ResponseEntity.ok(listDto);
	}
}
