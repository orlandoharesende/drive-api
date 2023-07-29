package br.jus.tjes.integracao.drive.security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import com.google.api.services.drive.DriveScopes;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;

@Component
@ApplicationScope
public class GoogleAuthentication {
	private static final String ARQUIVO_CREDENCIAIS = "/home/orlando/tjes/integracao-google/user-google-drive-orlando.json";
	private static final Logger LOG = LoggerFactory.getLogger(GoogleAuthentication.class);
	private GoogleCredentials credentials;

	public AccessToken getAccessToken() {
		try {
			if (this.credentials == null) {
				synchronized (this) {
					this.credentials = createCredentials();
				}
			}
			this.credentials.refreshIfExpired();
			AccessToken accessToken = this.credentials.getAccessToken();
			if (accessToken == null) {
				throw new RuntimeException("Não foi possível obter o token de autenticação com a Google.");
			}
			return accessToken;
		} catch (Exception e) {
			e.printStackTrace();
			this.credentials = null;
			throw new RuntimeException("Não foi possível obter o token de autenticação com a Google.");
		}
	}

	private GoogleCredentials createCredentials() throws IOException, FileNotFoundException {
		LOG.info("Criando novo google token");
		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(ARQUIVO_CREDENCIAIS))
				.createScoped(Arrays.asList(DriveScopes.DRIVE));
		if (credentials.getAccessToken() != null) {
			LOG.error("Erro. Token não gerado!");
		} else {
			LOG.info("Token criado: " + credentials.getAccessToken());
		}
		return credentials;
	}
}
