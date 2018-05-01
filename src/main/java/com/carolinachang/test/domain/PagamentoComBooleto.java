package com.carolinachang.test.domain;

import java.util.Date;

import javax.persistence.Entity;

import com.carolinachang.test.domain.enums.EstadoPagamento;

@Entity
public class PagamentoComBooleto extends Pagamento{
	private static final long serialVersionUID = 1L;
	
	private Date dataVencimento;
	private Date dataPagamento;
	
	public PagamentoComBooleto() {
	}

	public PagamentoComBooleto(Integer id, EstadoPagamento estado, Pedido pedido, Date dataVencimento,Date dataPagamento) {
		super(id, estado, pedido);
		this.dataVencimento = dataVencimento;
		this.dataPagamento = dataPagamento;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	
	
}
