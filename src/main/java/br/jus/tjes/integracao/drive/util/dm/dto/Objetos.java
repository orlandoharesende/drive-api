package br.jus.tjes.integracao.drive.util.dm.dto;

import java.util.List;

public class Objetos {
	public Data data;
	public List<String> mensagens;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public List<String> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<String> mensagens) {
		this.mensagens = mensagens;
	}

}
