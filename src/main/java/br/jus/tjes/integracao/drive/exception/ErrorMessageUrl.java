package br.jus.tjes.integracao.drive.exception;

import java.util.Date;

public class ErrorMessageUrl {

	private String mensagem;
	private Date data;
	private String descricao;

	private Integer status;
	
	

	public ErrorMessageUrl(String mensagem, Date data, String descricao, Integer status) {
		super();
		this.mensagem = mensagem;
		this.data = data;
		this.descricao = descricao;
		this.status = status;
	}

	public ErrorMessageUrl() {
		
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Date getData() {
		return data;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
