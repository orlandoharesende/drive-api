package br.jus.tjes.integracao.drive.util;

public class Log {
	public static void info(Object texto, Object... parametros) {
		String textoFormatado = String.format(texto.toString(), parametros);
		String textoSaida = String.format("\n# [LOG][%s] %s", Util.getDataAtualFormatada(), textoFormatado);
		System.out.print(textoSaida);
	}

	public static void error(String texto, Object... parametros) {
		String textoFormatado = String.format(texto, parametros);
		String textoSaida = String.format("\n# [LOG][%s] %s", Util.getDataAtualFormatada(), textoFormatado);
		System.err.print(textoSaida);
	}
}
