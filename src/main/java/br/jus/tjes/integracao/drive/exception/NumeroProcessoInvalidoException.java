package br.jus.tjes.integracao.drive.exception;

public class NumeroProcessoInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 4338700932267739113L;

	public NumeroProcessoInvalidoException() {
		super("Número do processo inválido");
	}
}
