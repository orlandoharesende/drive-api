package br.jus.tjes.integracao.drive.util.dm.dto;

public class ArquivoCSVDomicilio {
	private String nome;
	private String url;
	private byte[] conteudo;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public byte[] getConteudo() {
		return conteudo;
	}

	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}

}
