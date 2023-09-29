package br.jus.tjes.integracao.drive.util;

public class StringUtil {

	public static String fullTrim(String source) {
		return trimInside(trimBorders(source));
	}

	public static String trimInside(String source) {
		return source.replaceAll("\\b\\s{2,}\\b", " ");
	}

	public static String trimBorders(String source) {
		return trimLeft(trimRight(source));
	}

	public static String trimRight(String source) {
		return source.replaceAll("\\s+$", "");
	}

	public static String trimLeft(String source) {
		return source.replaceAll("^\\s+", "");
	}

	public static String limparCaracteresEntreStrings(String valor) {
		return valor.replaceAll("\\s+", " ");
	}

	public static boolean isEmpty(StringBuilder sb) {
		return sb == null || sb.toString().trim().isEmpty();
	}

	public static boolean isEmpty(String valor) {
		return valor == null || valor.trim().isEmpty();
	}

	public static String removerNaoNumeros(String texto) {
		if (texto == null) {
			return null;
		}
		return texto.replaceAll("[^0-9]", "").trim();
	}
}
