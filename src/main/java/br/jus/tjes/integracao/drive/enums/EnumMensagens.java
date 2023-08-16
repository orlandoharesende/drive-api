package br.jus.tjes.integracao.drive.enums;

public enum EnumMensagens {
	
	URL_EXPIRADA(1, "A url está expirada"),
	URL_INVALIDA(2,"Url inválida"),
	ERRO_INTERNO(3,"Erro interno");
	
	private final int codigo;
	private final String mensagem;
	
	
	
	private EnumMensagens(int codigo, String mensagem) {
		this.codigo = codigo;
		this.mensagem = mensagem;
	}
	public int getCodigo() {
		return codigo;
	}
	public String getMensagem() {
		return mensagem;
	}
	
	

}
