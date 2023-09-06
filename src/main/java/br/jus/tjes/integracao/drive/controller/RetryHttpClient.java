package br.jus.tjes.integracao.drive.controller;

import java.util.function.Supplier;

import br.jus.tjes.integracao.drive.util.Log;

public class RetryHttpClient {
	private static final Integer SLEEP_IN_MIILLIS = 5000;

	public static <T> T executeWithRetry(int maxRetries, Supplier<T> action) {
		int retryCount = 0;
		Exception excecao = null;
		while (retryCount < maxRetries) {
			try {
				if(retryCount > 0) {
					Log.error("Realizando mais uma tentativa.");
				}
				return action.get();
			} catch (Exception e) {
				excecao = e;
				retryCount++;
				Log.error("Erro ao realizar requisiição.");
				try {
					Thread.sleep(SLEEP_IN_MIILLIS);
				} catch (InterruptedException e1) {
					Log.info("Erro ao aguardar thread.");
				}

			}
		}
		throw new RuntimeException("Máximo de tentativas realiziada sem conseguir terminar.", excecao);
	}
}