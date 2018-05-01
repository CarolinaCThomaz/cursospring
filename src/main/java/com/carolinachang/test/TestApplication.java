package com.carolinachang.test;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.carolinachang.test.domain.Categoria;
import com.carolinachang.test.domain.Cidade;
import com.carolinachang.test.domain.Cliente;
import com.carolinachang.test.domain.Endereco;
import com.carolinachang.test.domain.Estado;
import com.carolinachang.test.domain.ItemPedido;
import com.carolinachang.test.domain.Pagamento;
import com.carolinachang.test.domain.PagamentoComBooleto;
import com.carolinachang.test.domain.PagamentoComCartao;
import com.carolinachang.test.domain.Pedido;
import com.carolinachang.test.domain.Produto;
import com.carolinachang.test.domain.enums.EstadoPagamento;
import com.carolinachang.test.domain.enums.TipoCliente;
import com.carolinachang.test.repositories.CategoriaRepository;
import com.carolinachang.test.repositories.CidadeRepository;
import com.carolinachang.test.repositories.ClienteRepository;
import com.carolinachang.test.repositories.EnderecoRepository;
import com.carolinachang.test.repositories.EstadoRepository;
import com.carolinachang.test.repositories.ItemPedidoRepository;
import com.carolinachang.test.repositories.PagamentoRepository;
import com.carolinachang.test.repositories.PedidoRepository;
import com.carolinachang.test.repositories.ProdutoRepository;

@SpringBootApplication
public class TestApplication implements CommandLineRunner{

	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private ProdutoRepository produtoRepository;
	@Autowired
	private EstadoRepository estadoRepository;
	@Autowired
	private CidadeRepository cidadeRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private PagamentoRepository pagamentoRepository;
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(TestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Categoria cat1 = new Categoria(null,"Informática");
		Categoria cat2 = new Categoria(null,"Escritório");
		
		Produto prod1 = new Produto(null, "Computador", 2000.00);
		Produto prod2 = new Produto(null, "Impressora", 800.00);
		Produto prod3 = new Produto(null, "Mouse", 80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(prod1, prod2, prod3));
		cat2.getProdutos().addAll(Arrays.asList(prod2));
		
		prod1.getCategorias().addAll(Arrays.asList(cat1));
		prod2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		prod3.getCategorias().addAll(Arrays.asList(cat1));
		
		Estado est1 = new Estado(null,"Minas Gerais");
		Estado est2 = new Estado(null,"São Paulo");
		
		Cidade c1 = new Cidade(null,"Uberlândia",est1);
		Cidade c2 = new Cidade(null,"São Paulo",est2);
		Cidade c3 = new Cidade(null,"Campinas",est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2,c3));
		
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com","34974027883",TipoCliente.PESSOAFISICA);
		
		cli1.getTelefones().addAll(Arrays.asList("34546456","35464564"));
		
		Endereco e1 = new Endereco(null, "Rua JoãoFranco Oliveira", "91", "casa", "campininha", "04678100", cli1, c1);
		Endereco e2 = new Endereco(null, "Rua Mendes", "91", "ap", "sto Amaro", "45564565", cli1, c2);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
		
		Pedido ped1 = new Pedido(null, sdf.parse("28/04/2018 16:00"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("01/05/2018 19:00"), cli1, e2);
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pagto1);
		
		Pagamento pagto2 = new PagamentoComBooleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("01/05/2018 19:00"), null);
		ped2.setPagamento(pagto2);
		
		
		cli1.getPedidos().addAll(Arrays.asList(ped1,ped2));
		
		
		ItemPedido ip1 = new ItemPedido(ped1, prod1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, prod3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, prod2, 100.00, 1, 400.00);
		ped1.getItens().addAll(Arrays.asList(ip1,ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));
		
		prod1.getItens().addAll(Arrays.asList(ip1));
		prod2.getItens().addAll(Arrays.asList(ip3));
		prod3.getItens().addAll(Arrays.asList(ip2));
		
		categoriaRepository.saveAll(Arrays.asList(cat1,cat2));
		produtoRepository.saveAll(Arrays.asList(prod1,prod2,prod3));
		estadoRepository.saveAll(Arrays.asList(est1,est2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3));
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1,e2));
		pedidoRepository.saveAll(Arrays.asList(ped1,ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1,pagto2));
		itemPedidoRepository.saveAll(Arrays.asList(ip1,ip2,ip3));				
		
		
	}
}
