package br.jus.tjes.google.drive.dto;

public class ArquivoDTO {
	private String id;
	private String nome;
	private String mimeType;
	private boolean diretorio;

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

}
