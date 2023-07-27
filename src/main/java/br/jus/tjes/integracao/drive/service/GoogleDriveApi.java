package br.jus.tjes.integracao.drive.service;

import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.jus.tjes.integracao.drive.dto.ArquivoDTO;
import br.jus.tjes.integracao.drive.google.beans.File;
import br.jus.tjes.integracao.drive.google.beans.ListGoogleFiles;
import br.jus.tjes.integracao.drive.security.GoogleAuthentication;

/**
 * https://www.googleapis.com/discovery/v1/apis/drive/v3/rest
 * 
 */
@Service
public class GoogleDriveApi {
	private static final String PATH_ARQUIVO = "/%s/arquivos/%s";
	private static final String PATH_DOWNLOAD = PATH_ARQUIVO + "/download";
	private static final Logger LOG = LoggerFactory.getLogger(GoogleDriveApi.class);
	public static final String BASE_URL = "https://www.googleapis.com/drive/v3/";
	public static final String URL_LIST_FILES = BASE_URL + "files/";

	public List<ArquivoDTO> listarArquivos(String numeroProcesso) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet request = new HttpGet(URL_LIST_FILES);
		addBasicHeaders(request);

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			logarRequisicao(response, URL_LIST_FILES, numeroProcesso);
			if (!isOK(response)) {
				throw new RuntimeException(
						"Erro na comunicação com o Google Drive:" + response.getStatusLine().getStatusCode());
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String result = EntityUtils.toString(entity);
				ListGoogleFiles listGooleFiles = new Gson().fromJson(result, ListGoogleFiles.class);
				List<ArquivoDTO> listArquivos = convertToListArquivoDTO(listGooleFiles, numeroProcesso);
				return listArquivos;
			}
			return Collections.emptyList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void addBasicHeaders(HttpGet request) {
		request.addHeader(HttpHeaders.AUTHORIZATION, getToken());
	}

	private List<ArquivoDTO> convertToListArquivoDTO(ListGoogleFiles listGooleFiles, String numeroProcesso) {
		return listGooleFiles.getFiles().stream().map(f -> convertToArquivoDTO(f, numeroProcesso)).toList();
	}

	private ArquivoDTO convertToArquivoDTO(File file, String numeroProcesso) {
		if (file == null) {
			return null;
		}
		ArquivoDTO dto = new ArquivoDTO();
		dto.setDiretorio(isDiretorio(file));
		dto.setId(file.getId());
		dto.setMimeType(file.getMimeType());
		dto.setNome(file.getName());
		dto.setUri(criarPathArquivo(file, numeroProcesso));
		dto.setDownload(criarPathDownload(file, numeroProcesso));
		return dto;
	}

	private String criarPathDownload(File file, String numeroProcesso) {
		if (!isDiretorio(file)) {
			return String.format(PATH_DOWNLOAD, numeroProcesso, file.getId());
		}
		return null;

	}

	private String criarPathArquivo(File file, String numeroProcesso) {
		return String.format(PATH_ARQUIVO, numeroProcesso, file.getId());
	}

	private boolean isDiretorio(File file) {
		if (file.getMimeType() != null) {
			return file.getMimeType().toUpperCase().contains("FOLDER");
		}
		return false;
	}

	private boolean isOK(CloseableHttpResponse response) {
		return response.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
	}

	private void logarRequisicao(CloseableHttpResponse response, String url, String numeroProcesso) {
		if (isOK(response)) {
			LOG.info(String.format("StatusCode: 200 | URL: [%s] | numeroProcesso: [%s] ", url, numeroProcesso));
		} else {
			LOG.error(String.format("StatusCode: %s | ReasonPhrase: %s | URL: [%s] | numeroProcesso: [%s] | Info: %s  ",
					response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase(), url,
					numeroProcesso, response.getStatusLine().toString()));
		}

	}

	private String getToken() {
		return "Bearer " + GoogleAuthentication.getAccessToken().getTokenValue();
	}

	public ArquivoDTO getArquivo(String numeroProcesso, String idArquivo) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String uri = URL_LIST_FILES + idArquivo;
		HttpGet request = new HttpGet(uri);
		addBasicHeaders(request);

		try (CloseableHttpResponse response = httpClient.execute(request)) {
			logarRequisicao(response, uri, numeroProcesso);
			if (!isOK(response)) {
				throw new RuntimeException(
						"Erro na comunicação com o Google Drive:" + response.getStatusLine().getStatusCode());
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String result = EntityUtils.toString(entity);
				File file = new Gson().fromJson(result, File.class);
				ArquivoDTO listArquivos = convertToArquivoDTO(file, numeroProcesso);
				return listArquivos;
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
