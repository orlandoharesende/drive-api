package br.jus.tjes.integracao.drive.models;

import java.util.Date;

import br.jus.tjes.integracao.drive.dto.ArquivoDTO;

public class TokenDocumento {

	private String idUsuario;
	private String idDocumento;
	private Date dataExpiracao;
	private Date dataCriacao;
	private String numeroProcesso;
	private byte[] arquivoPdf;

	private ArquivoDTO arquivo;

	private String emissor;

	public String getEmissor() {
		return emissor;
	}

	public void setEmissor(String emissor) {
		this.emissor = emissor;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getIdDocumento() {
		return idDocumento;
	}

	public void setIdDocumento(String idDocumento) {
		this.idDocumento = idDocumento;
	}

	public Date getDataExpiracao() {
		return dataExpiracao;
	}

	public void setDataExpiracao(Date dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public byte[] getArquivoPdf() {
		return arquivoPdf;
	}

	public void setArquivoPdf(byte[] arquivoPdf) {
		this.arquivoPdf = arquivoPdf;
	}

	public ArquivoDTO getArquivo() {
		return arquivo;
	}

	public void setArquivo(ArquivoDTO arquivo) {
		this.arquivo = arquivo;
	}
	
	
	
	
	
	
	
	

}
