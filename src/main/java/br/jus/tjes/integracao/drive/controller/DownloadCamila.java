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
	private static final String ACCESS_TOKEN = "eyJraWQiOiJNOHNrM1VSdnZHMnhKZ3FkZDdYdGhWZnhyQVptdmVvMyIsImFsZyI6IlJTMjU2In0.eyJqdGkiOiJKV1QiLCJhdWQiOiJrZXlzdG9uZSIsImlzcyI6Im0ybS10b2tlbi1zZXJ2aWNlIiwic3ViIjoicHVibGljX3NoYXJlIiwic2NvcGVzIjoibmFzX3JlYWRfb25seSBuYXNfcmVhZF93cml0ZSIsImN1c3RvbUNsYWltcyI6eyJkZXZpY2VJZCI6ImRhYWE3ZmY4LTlhMjctNDE5Mi05Nzk2LTM0NTA3YTQzMGRkYyIsImF1dGhfaWQiOiI3MmU3MDNmMy00NGY0LTQzNGUtYjM3Yi0zMWE5OWQwOGExMGUifSwiZXhwIjoxNjk0NTA3NjQxfQ.arP8xdLaaFONMp3NG_0pvH6m8DtINpeErcZHoJEqmZayxJ6RyXdcpvH7YsExILKG8CHk-ozgSL-k29Km7xJOMjUUVmuEVtytLT7HkijXMGrgQYf07-Up97ZcCfSexZbp5AQ4c0IeNQVAbgyf1aH7KcmaR1Zb2P5hHE16EblUv3VmTLMVDtHUrQIaAoRCqZYZHGdd3keoVrURxSKgRBLIKDNa1cZiAt-mJtc5MsdBMvaoNJIX-5U7aSkmJS2KohB2R05VVEo2IUQUsseaohnl9OHuqog0r6jDRtz3_4WNuUo9vCc1c_BeOJQoHvSaideZWASOl1ac-Y7KlGwX_dL9eg";
	private static final int UM_MINUTO_EM_MILLIS = 60 * 1000;
	public static final String BASE_URL = "https://prod-7c74fffc718ed01.wdckeystone.com";
	public static final String KEY = "daaa7ff8-9a27-4192-9796-34507a430ddc";
	public static final String ID_DIRETORIO_PRINCIPAL = "kfimc4yxsv7sadaghrmtyzrv";
	public static final String ROOT_DIR = "/home/orlando/camila/pasta-05";
	static int qtdRequisicoes = 0;
	public static final Integer DEFAULT_HTTP_CLIENT_TIMEOUT_IN_MILLIS = UM_MINUTO_EM_MILLIS * 10;

	public static void main(String[] args) {
		DownloadCamila service = new DownloadCamila();
		try {
			String idDirPrincipal = ID_DIRETORIO_PRINCIPAL;
			List<File> files = service.consultarPastaRecursiva(idDirPrincipal, null);
			Log.info("Imprimindo estrutura: ");
			for (File file : files) {
				System.out.println(file);
			}
			Log.info("Aguardando 30 segungos para iniciar o download dos documentos.");
			Thread.sleep(30000);
			service.criarPastasAndDownloadArquivos(files, ROOT_DIR);
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
						//Thread.sleep(5000);
						byte[] arquivoEmBytes = download(file.getId());
						if(arquivoEmBytes != null) {
							Log.info("Arquivo baixado.");
							Log.info("Gravando arquivo.");
							Files.write(Paths.get(name), arquivoEmBytes);
							Log.info("Arquivo gravado.");
						}else {
							Log.error("Erro ao baixar arquivo.");
						}
					} catch (Exception e) {
						Log.error("Erro ao baixar arquivo: " + e.getMessage());
						Log.info("Passando para o próximo");
					}
				}
			}
		}
	}

	public List<File> consultarPastaRecursiva(String id, File pai) throws IOException {
		Root root = consultarPasta(id);
		if (root == null || root.getFiles() == null || root.getFiles().isEmpty()) {
			return Collections.emptyList();
		}

		for (File file : root.getFiles()) {
			if (file.isDir()) {
				file.getFilhos().addAll(consultarPastaRecursiva(file.getId(), pai));
				file.getFilhos().stream().forEach(f -> f.setPai(pai));
			}
		}
		return root.getFiles();
	}

	public Root consultarPasta(String id) throws IOException {
		Log.info("[%s][QTD=%s] Requisição: id = %s", new Date(), ++qtdRequisicoes, id);
		String fields = "pageToken,id,cTime,mTime,mimeType,name,parentID,extension,size,publiclyShared,privatelyShared,video,audio,image,family,tags,description,autoTags,trashed,document,storageType,storageID";
		String url = UrlBuilder.builder(BASE_URL) //
				.path("/${key}/sdk/v2/filesSearch/parents") //
				.addPathVariable("key", KEY) //
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
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN);
		request.addHeader("authority", "prod-7c74fffc718ed01.wdckeystone.com");
		request.addHeader("authority", "prod-7c74fffc718ed01.wdckeystone.com");
		request.addHeader("accept", "*/*");
		request.addHeader("accept-language", "pt-BR,pt;q=0.8");
		request.addHeader("authorization", "Bearer " + ACCESS_TOKEN);
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
		request.addHeader("x-correlation-id", "w_g:9333a6c6-fc0b-4e9f-847e-7471965c1ba5");
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

	public byte[] download(String id) {
		String url = UrlBuilder.builder(BASE_URL).path("${key}/sdk/v2/files/${id}/content") //
				.addPathVariable("key", KEY) //
				.addPathVariable("id", id) //
				.addQueryParameter("access_token", ACCESS_TOKEN) //
				.addQueryParameter("download", true) //
				.build().toUrl();
		
		Supplier<CloseableHttpResponse> httpRequest = () -> {
			try {
				HttpGet request = new HttpGet(url);
				return createHttpClient(request).execute(request);
			} catch (Exception e) {
				Log.error("Erro ao realizar download. [] %s", e.getMessage());
				throw new RuntimeException(e);
			}
		};
		
		try (CloseableHttpResponse response = RetryHttpClient.executeWithRetry(3, httpRequest)) {
			Log.info("Resposta: Status code [%s] | Message: [%s] ", response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
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


	private void setHardTimeout(HttpUriRequest request) {
		int hardTimeout = DEFAULT_HTTP_CLIENT_TIMEOUT_IN_MILLIS;
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				request.abort();
				Log.error("Conexão abortada!!");
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
