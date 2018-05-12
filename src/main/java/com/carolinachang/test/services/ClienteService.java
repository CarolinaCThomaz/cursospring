package com.carolinachang.test.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.carolinachang.test.domain.Cidade;
import com.carolinachang.test.domain.Cliente;
import com.carolinachang.test.domain.Endereco;
import com.carolinachang.test.domain.enums.Perfil;
import com.carolinachang.test.domain.enums.TipoCliente;
import com.carolinachang.test.dto.ClienteDTO;
import com.carolinachang.test.dto.ClienteNewDTO;
import com.carolinachang.test.repositories.ClienteRepository;
import com.carolinachang.test.repositories.EnderecoRepository;
import com.carolinachang.test.security.UserSS;
import com.carolinachang.test.services.exception.AuthorizationException;
import com.carolinachang.test.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ClienteRepository repositorioCliente;
	
	@Autowired
	private EnderecoRepository repositorioEndereco;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = repositorioCliente.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName(), null));
		
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repositorioCliente.save(obj);
	    repositorioEndereco.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente cliente = find(obj.getId());
		updateData(cliente,obj);
		return repositorioCliente.save(cliente);
	}

	private void updateData(Cliente cliente, Cliente obj) {
		cliente.setNome(obj.getNome());
		cliente.setEmail(obj.getEmail());
		
	}

	public void delete(Integer id) {
		find(id);
		try {
			repositorioCliente.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("Não é possível excluir porque há pedidos relacionados");
		}
		
		
	}

	public List<Cliente> findAll() {
		return repositorioCliente.findAll();
		
	}
	
	public Page<Cliente> findPage(Integer page,Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest =  PageRequest.of(page, linesPerPage,Direction.valueOf(direction), orderBy);
		return repositorioCliente.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(),null,null,null);
	}

	public Cliente fromDTO(ClienteNewDTO clienteNewDto) {
		Cliente cli = new Cliente(null, clienteNewDto.getNome(), clienteNewDto.getEmail(), clienteNewDto.getCpfOuCnpj(), TipoCliente.toEnum(clienteNewDto.getTipo()), bCryptPasswordEncoder.encode(clienteNewDto.getSenha()));
		Cidade cid = new Cidade(clienteNewDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, clienteNewDto.getLogradouro(), clienteNewDto.getNumero(), clienteNewDto.getComplemento(), clienteNewDto.getBairro(), clienteNewDto.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(clienteNewDto.getTelefone1());
		if(clienteNewDto.getTelefone2() != null) {
			cli.getTelefones().add(clienteNewDto.getTelefone2());
		}
		if(clienteNewDto.getTelefone3() != null) {
			cli.getTelefones().add(clienteNewDto.getTelefone3());
		}
		return cli;
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) throws URISyntaxException {
		UserSS user = UserService.authenticated();
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		URI uri = s3Service.uploadFile(multipartFile);
		
		Cliente cli = find(user.getId());
		cli.setImageUrl(uri.toString());
		repositorioCliente.save(cli);
		
		return uri;
	}
}
