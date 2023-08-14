package br.jus.tjes.integracao.drive.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class DocumentoDTO {
	
	@Valid
	@NotNull(message = "CPF do remetente não pode ser nulo")
	private String idUser;
	
	@Valid
	@NotNull(message = "Identificador do documento não pode ser nulo")
	private String idDoc;
	
	@Valid
	@NotNull(message = "Número do processo não pode ser nulo")
	private String numeroProcesso;

	

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getIdDoc() {
		return idDoc;
	}

	public void setIdDoc(String idDoc) {
		this.idDoc = idDoc;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}
	
	

}
