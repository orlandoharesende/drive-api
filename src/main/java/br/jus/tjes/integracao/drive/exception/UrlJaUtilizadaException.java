package br.jus.tjes.integracao.drive.exception;

public class UrlJaUtilizadaException extends RuntimeException {

	private static final long serialVersionUID = 6299774843097630898L;

	public UrlJaUtilizadaException() {
		super("URL já utilizada.");
	}
}
