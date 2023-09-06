package br.jus.tjes.integracao.drive.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;

public class Util {

	public static byte[] toByteArray(InputStream content) {
		try {
			return IOUtils.toByteArray(content);
		} catch (IOException e) {
			throw new RuntimeException("Não foi possível ler o arquivo.");
		}
	}
	public static String getDataAtualFormatada() {
		return new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(new Date());
	}
	

}
