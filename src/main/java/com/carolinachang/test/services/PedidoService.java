package com.carolinachang.test.services;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.carolinachang.test.domain.ItemPedido;
import com.carolinachang.test.domain.PagamentoComBoleto;
import com.carolinachang.test.domain.Pedido;
import com.carolinachang.test.domain.enums.EstadoPagamento;
import com.carolinachang.test.repositories.ItemPedidoRepository;
import com.carolinachang.test.repositories.PagamentoRepository;
import com.carolinachang.test.repositories.PedidoRepository;
import com.carolinachang.test.repositories.ProdutoRepository;
import com.carolinachang.test.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repositorioPedido;
	
	@Autowired
	private PagamentoRepository repositorioPagamento;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	public Pedido buscar(Integer id) {
		Optional<Pedido> obj = repositorioPedido.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName(), null));
		
	}

	public Pedido insert(@Valid Pedido pedido) {
		pedido.setId(null);
		pedido.setInstance(new Date());
		pedido.setCliente(clienteService.find(pedido.getCliente().getId()));
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		if(pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, pedido.getInstance());
		}
		pedido = repositorioPedido.save(pedido);
		repositorioPagamento.save(pedido.getPagamento());
		for (ItemPedido ip : pedido.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco());
			ip.setPedido(pedido);
		}
		itemPedidoRepository.saveAll(pedido.getItens());
		//emailService.sendOrderConfirmationEmail(pedido);
		emailService.sendOrderConfirmationHtmlEmail(pedido);
		return pedido;
	}
}
