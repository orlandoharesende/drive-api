package br.jus.tjes.integracao.drive.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.jus.tjes.integracao.drive.util.Log;
import br.jus.tjes.integracao.drive.util.Util;

public class DownloadCamila {
	private static final int UM_MINUTO_EM_MILLIS = 60 * 1000;
	public static final Integer DEFAULT_HTTP_CLIENT_TIMEOUT_IN_MILLIS = UM_MINUTO_EM_MILLIS * 25;
	static int qtdRequisicoes = 0;

	private static final ConfigDownload config = Configs.DIR_05;

	public static void main(String[] args) {

		DownloadCamila service = new DownloadCamila();
		try {
			String idDirPrincipal = config.getMainFolder();
			List<File> files = service.consultarPastaRecursiva(idDirPrincipal, null, 0);
			Log.info("Imprimindo estrutura: \n\n");
			files.forEach(f -> System.out.println(f));
			Log.info("Aguardando 30 segundos para iniciar o download dos documentos.");
			Thread.sleep(30000);
			service.criarPastasAndDownloadArquivos(files, config.getLocalFolder());
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			Log.info("Fim do programa!");
		}

	}

	private void criarPastasAndDownloadArquivos(List<File> files, String rootDir) throws IOException {
		for (File file : files) {
			String name = rootDir + "/" + file.getName();
			if (file.isDir()) {
				Path path = Paths.get(name);
				if (!Files.exists(path)) {
					Files.createDirectories(path);
					Log.info("Diretório [%s]: [%s]", "Novo", path);
				} else {
					Log.info("Diretório [%s]: [%s]", "Já existente", path);
				}
				criarPastasAndDownloadArquivos(file.getFilhos(), name);
			} else {
				Path arquivo = Paths.get(name);
				if (!Files.exists(arquivo)) {
					Log.info("Fazer download do arquivo: " + name);
					Log.info("Tamanho do arquivo: " + (file.getSize() / 1024 / 1024) + "MB");
					try {
						// Thread.sleep(5000);
						byte[] arquivoEmBytes = download(file.getId());
						if (arquivoEmBytes != null) {
							Log.info("Arquivo baixado.");
							Log.info("Gravando arquivo.");
							Files.write(Paths.get(name), arquivoEmBytes);
							Log.info("Arquivo gravado.");
						} else {
							Log.error("Erro ao baixar arquivo.");
						}
					} catch (Exception e) {
						Log.error("Erro ao baixar arquivo: " + e.getMessage());
						Log.info("Passando para o próximo");
					}
				} else {
					Log.info("Arquivo [%s]: [%s]", "Já existente", arquivo);
				}
			}
		}
	}

	public List<File> consultarPastaRecursiva(String id, File pai, int nivel) throws IOException {
		nivel++;
		Root root = consultarPasta(id);
		if (root == null || root.getFiles() == null || root.getFiles().isEmpty()) {
			return Collections.emptyList();
		}

		for (File file : root.getFiles()) {
			file.setNivel(nivel);
			if (file.isDir()) {
				file.getFilhos().addAll(consultarPastaRecursiva(file.getId(), file, nivel));
				file.getFilhos().stream().forEach(f -> f.setPai(pai));
			}
		}
		return root.getFiles();
	}

	public Root consultarPasta(String id) throws IOException {
		Log.info("[%s][QTD=%s] Requisição: id = %s", new Date(), ++qtdRequisicoes, id);
		String fields = "pageToken,id,cTime,mTime,mimeType,name,parentID,extension,size,publiclyShared,privatelyShared,video,audio,image,family,tags,description,autoTags,trashed,document,storageType,storageID";
		String url = UrlBuilder.builder(config.getBaseUrl()) //
				.path("/${device-id}/sdk/v2/filesSearch/parents") //
				.addPathVariable("device-id", config.getDeviceId()) //
				.addQueryParameter("pretty", "false") //
				.addQueryParameter("ids", id) //
				.addQueryParameter("fields", fields) //
				.addQueryParameter("limit", 50) //
				.addQueryParameter("orderBy", "name") //
				.addQueryParameter("order", "asc") //
				.build().toUrl();

		HttpGet request = new HttpGet(url);
		addHeadersToFileSearch(request);
		try (CloseableHttpResponse response = executeHttpClient(request)) {
			HttpEntity entity = response.getEntity();
			if (isSucess(response) && entity != null) {
				return convertToObjectEntity(entity, Root.class);
			} else {
				Log.error("Erro: Status code: %s", response.getStatusLine().getStatusCode());
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isSucess(CloseableHttpResponse response) {
		int statusCode = response.getStatusLine().getStatusCode();
		return (statusCode >= 200 && statusCode < 300);
	}

	private <T> T convertToObjectEntity(HttpEntity entity, Class<T> classe) {
		try {
			return createJsonMapper().readValue(EntityUtils.toString(entity), classe);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void addHeadersToFileSearch(HttpGet request) {
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + config.getAccessToken());
		request.addHeader("authority", config.getAuthority());
		request.addHeader("accept", "*/*");
		request.addHeader("accept-language", "pt-BR,pt;q=0.8");
		request.addHeader("cache-control", "no-cache");
		request.addHeader("origin", "https://os5.mycloud.com");
		request.addHeader("pragma", "no-cache");
		request.addHeader("referer", "https://os5.mycloud.com/");
		request.addHeader("sec-ch-ua", "\"Chromium\";v=\"106\", \"Google Chrome\";v=\"106\", \"Not;A=Brand\";v=\"99\"");
		request.addHeader("sec-ch-ua-mobile", "?0");
		request.addHeader("sec-ch-ua-platform", "\"Linux\"");
		request.addHeader("sec-fetch-dest", "empty");
		request.addHeader("sec-fetch-mode", "cors");
		request.addHeader("sec-fetch-site", "cross-site");
		request.addHeader("user-agent",
				"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36");
		request.addHeader("x-correlation-id", config.getCorrelationId());
	}

	private CloseableHttpResponse executeHttpClient(HttpUriRequest request)
			throws ClientProtocolException, IOException, Exception {
		return createHttpClient(request).execute(request);
	}

	private ObjectMapper createJsonMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return objectMapper;
	}

	public byte[] download(String fileId) {
		String url = UrlBuilder.builder(config.getBaseUrl()).path("${device-id}/sdk/v2/files/${fileId}/content") //
				.addPathVariable("device-id", config.getDeviceId()) //
				.addPathVariable("fileId", fileId) //
				.addQueryParameter("access_token", config.getAccessToken()) //
				.addQueryParameter("download", true) //
				.build().toUrl();

		// Supplier<CloseableHttpResponse> httpRequest = createSupplierHttp(url);
		HttpGet request = new HttpGet(url);

		try (CloseableHttpResponse response = createHttpClient(request).execute(request)) {
			Log.info("Resposta: Status code [%s] | Message: [%s] ", response.getStatusLine().getStatusCode(),
					response.getStatusLine().getReasonPhrase());
			HttpEntity entity = response.getEntity();
			if (isSucess(response) && entity != null) {
				return Util.toByteArray(entity.getContent());
			}
			return null;
		} catch (Exception e) {
			Log.error("Erro ao realizar download. [] %s", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private Supplier<CloseableHttpResponse> createSupplierHttp(String url) {
		Supplier<CloseableHttpResponse> httpRequest = () -> {
			try {
				HttpGet request = new HttpGet(url);
				return createHttpClient(request).execute(request);
			} catch (Exception e) {
				Log.error("Erro ao realizar download. [] %s", e.getMessage());
				throw new RuntimeException(e);
			}
		};
		return httpRequest;
	}

	private void setHardTimeout(HttpUriRequest request) {
		int hardTimeout = DEFAULT_HTTP_CLIENT_TIMEOUT_IN_MILLIS;
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Log.error("Conexão abortada!!");
				request.abort();
			}
		};
		new Timer(true).schedule(task, hardTimeout);
	}

	private CloseableHttpClient createHttpClient(HttpUriRequest request) throws Exception {
		setHardTimeout(request);
		RequestConfig config = RequestConfig.custom() //
				.setConnectTimeout(DEFAULT_HTTP_CLIENT_TIMEOUT_IN_MILLIS) //
				.setConnectionRequestTimeout(DEFAULT_HTTP_CLIENT_TIMEOUT_IN_MILLIS)//
				.setSocketTimeout(DEFAULT_HTTP_CLIENT_TIMEOUT_IN_MILLIS) //
				.build();
		return HttpClients.custom().setDefaultRequestConfig(config).build();
	}

}
