package br.jus.tjes.integracao.drive.security;

import java.io.FileInputStream;
import java.util.Arrays;

import com.google.api.services.drive.DriveScopes;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

public class GoogleAuthentication {
	private static final String ARQUIVO_CREDENCIAIS = "/home/orlando/tjes/integracao-google/user-google-drive-orlando.json";

	public static AccessToken getAccessToken() {
		try {
			GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(ARQUIVO_CREDENCIAIS))
					.createScoped(Arrays.asList(DriveScopes.DRIVE));
			credentials.refreshIfExpired();
			return credentials.getAccessToken();
		} catch (Exception e) {
			throw new RuntimeException("Não foi possível obter o token de autenticação.");
		}
	}
}
