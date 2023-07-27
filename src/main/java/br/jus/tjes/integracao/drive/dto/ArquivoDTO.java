package br.jus.tjes.integracao.drive.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ArquivoDTO {
	private String id;
	private String nome;
	private String mimeType;
	private boolean diretorio;
	private String uri;
	@JsonInclude(Include.NON_NULL)
	private String download;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public boolean isDiretorio() {
		return diretorio;
	}

	public void setDiretorio(boolean diretorio) {
		this.diretorio = diretorio;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

}
