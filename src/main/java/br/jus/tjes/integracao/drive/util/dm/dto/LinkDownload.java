package br.jus.tjes.integracao.drive.util.dm.dto;

import java.util.List;

public class LinkDownload {
	private String data;
	private List<String> mensagens;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public List<String> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<String> mensagens) {
		this.mensagens = mensagens;
	}

}
