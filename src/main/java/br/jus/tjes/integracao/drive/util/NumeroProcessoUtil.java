package br.jus.tjes.integracao.drive.util;

import java.math.BigInteger;

public final class NumeroProcessoUtil {

	public static final int QTD_CARACTERES_NUMERO_UNICO_COM_MASCARA = 25;
	public static final int QTD_CARACTERES_NUMERO_UNICO_SEM_MASCARA = 20;

	public static String completaZeros(long l, int tamanho) {
		StringBuilder sb = new StringBuilder();
		String lSrt = Long.toString(l);
		for (int i = 0; i < tamanho - lSrt.length(); i++) {
			sb.append('0');
		}
		sb.append(lSrt);
		return sb.toString();
	}

	public static int calcDigitoVerificador(long numeroSequencia, long ano, long numeroVara,
			long numeroOrigemProcesso) {
		String numeroCalc = completaZeros(numeroSequencia, 7) + completaZeros(ano, 4) + completaZeros(numeroVara, 3)
				+ completaZeros(numeroOrigemProcesso, 4) + "00";
		BigInteger nro = new BigInteger(numeroCalc);
		long digito = 98 - (nro.mod(new BigInteger("97")).longValue());
		return (int) digito;
	}

	public static String formatNumeroProcesso(Integer numeroSequencia, Integer numeroDigitoVerificador, Integer ano,
			Long numeroOrgaoJustica, Integer numeroOrigem) {
		StringBuilder sb = new StringBuilder();
		sb.append(completaZeros(numeroSequencia, 7));
		sb.append('-').append(completaZeros(numeroDigitoVerificador, 2));
		sb.append('.').append(completaZeros(ano, 4));

		String nOrgJus = completaZeros(numeroOrgaoJustica.intValue(), 3);
		sb.append('.').append(nOrgJus.substring(0, 1));
		sb.append('.').append(nOrgJus.substring(1));

		sb.append('.').append(completaZeros(numeroOrigem, 4));
		return sb.toString();
	}

	public static String formatNumeroProcesso(Integer numeroSequencia, Integer numeroDigitoVerificador, Integer ano,
			Integer numeroOrgaoJustica, Integer numeroOrigem) {
		return formatNumeroProcesso(numeroSequencia, numeroDigitoVerificador, ano, numeroOrgaoJustica.longValue(),
				numeroOrigem);
	}

	public static String mascaraNumeroProcesso(String numeroProcesso) {
		String numeroRetorno = numeroProcesso;
		if (!StringUtil.isEmpty(numeroProcesso)) {
			numeroProcesso = StringUtil.limparCaracteresEntreStrings(numeroProcesso);
			if (numeroProcesso.length() == QTD_CARACTERES_NUMERO_UNICO_SEM_MASCARA) {
				StringBuilder sb = new StringBuilder();
				sb.append(numeroProcesso.substring(0, 7));
				sb.append('-');
				sb.append(numeroProcesso.substring(7, 9));
				sb.append('.');
				sb.append(numeroProcesso.substring(9, 13));
				sb.append('.');
				sb.append(numeroProcesso.substring(13, 14));
				sb.append('.');
				sb.append(numeroProcesso.substring(14, 16));
				sb.append('.');
				sb.append(numeroProcesso.substring(16, 20));
				numeroRetorno = sb.toString();
			}
		}
		return numeroRetorno;
	}

	public static String retiraMascaraNumeroProcesso(String numeroProcesso) {
		return StringUtil.fullTrim(numeroProcesso.replaceAll("[_\\.\\-/]", ""));
	}

	public static boolean numeroProcessoValido(String numeroProcesso) {
		try {
			if (StringUtil.isEmpty(numeroProcesso)) {
				return false;
			}

			if (numeroProcesso.length() != QTD_CARACTERES_NUMERO_UNICO_SEM_MASCARA
					&& numeroProcesso.length() != QTD_CARACTERES_NUMERO_UNICO_COM_MASCARA) {
				return false;
			}

			if (numeroProcesso.length() == QTD_CARACTERES_NUMERO_UNICO_SEM_MASCARA) {
				numeroProcesso = mascaraNumeroProcesso(numeroProcesso);
			}

			long numeroSequencia = Integer.parseInt(numeroProcesso.substring(0, 7));
			long numeroDigitoVerificador = Integer.parseInt(numeroProcesso.substring(8, 10));
			long ano = Integer.parseInt(numeroProcesso.substring(11, 15));
			long numeroVara = Integer.parseInt(numeroProcesso.substring(16, 17) + numeroProcesso.substring(18, 20));
			long numeroOrigemProcesso = Integer.parseInt(numeroProcesso.substring(21, 25));

			if (NumeroProcessoUtil.calcDigitoVerificador(numeroSequencia, ano, numeroVara,
					numeroOrigemProcesso) == numeroDigitoVerificador) {
				return true;
			}

		} catch (Exception e) {
			return false;
		}
		return false;
	}

}