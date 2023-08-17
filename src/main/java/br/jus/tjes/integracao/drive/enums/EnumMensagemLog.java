package br.jus.tjes.integracao.drive.enums;

public enum EnumMensagemLog {

	LOG_SOLICITACAO_URL(1, "CPF:%s solicitou url temporária para acessar o documento %s do processo %s em %s"), LOG_DOWNLOAD_ARQ_TMP
	(2, "CPF: %s  utilizou a url temporária para acessar o documento %s do processo %s em %s"), LOG_TOKEN(3," token : %s em %s ");

	private final int codigo;
	private final String mensagem;

	private EnumMensagemLog(int codigo, String mensagem) {
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
