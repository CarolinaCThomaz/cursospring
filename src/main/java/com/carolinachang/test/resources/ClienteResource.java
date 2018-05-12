package com.carolinachang.test.resources;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.carolinachang.test.domain.Cliente;
import com.carolinachang.test.dto.ClienteDTO;
import com.carolinachang.test.dto.ClienteNewDTO;
import com.carolinachang.test.services.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService service;
	
	@RequestMapping(value="/{id}" ,method=RequestMethod.GET)
	public ResponseEntity<?> find(@PathVariable Integer id) {
		Cliente obj = service.find(id);
		return ResponseEntity.ok(obj);
	}
	
	@RequestMapping(value="/email" ,method=RequestMethod.GET)
	public ResponseEntity<?> find(@RequestParam(value="email") String email) {
		Cliente obj = service.findByEmail(email);
		return ResponseEntity.ok().body(obj);
	}
	
	
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO clienteNewDto){
		Cliente cliente = service.fromDTO(clienteNewDto);
		cliente = service.insert(cliente);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value="/{id}" ,method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO categoriaDto, @PathVariable Integer id){
		Cliente categoria = service.fromDTO(categoriaDto);
		categoria.setId(id);
		categoria = service.update(categoria);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/{id}" ,method=RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> findAll(){
		List<Cliente> list = service.findAll();
		List<ClienteDTO> listDto = list.stream().map(cat -> new ClienteDTO(cat)).collect(Collectors.toList());
		return ResponseEntity.ok(listDto);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value="/page" ,method=RequestMethod.GET)
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(value="page",defaultValue="0") Integer page,
			@RequestParam(value="linesPerPage",defaultValue="24") Integer linesPerPage,
			@RequestParam(value="orderBy",defaultValue="nome") String orderBy,
			@RequestParam(value="direction",defaultValue="ASC") String direction
			) {
		
		Page<Cliente> list = service.findPage(page, linesPerPage, orderBy, direction);
		Page<ClienteDTO> listDto = list.map(cat -> new ClienteDTO(cat));
		return ResponseEntity.ok().body(listDto);
	}
	
	@RequestMapping(value="/picture" ,method=RequestMethod.POST)
	public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name="file") MultipartFile multipartFile) throws URISyntaxException{
		
		URI uri = service.uploadProfilePicture(multipartFile);
		return ResponseEntity.created(uri).build();
	}
}


