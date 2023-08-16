package br.jus.tjes.integracao.drive.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import br.jus.tjes.integracao.drive.util.dm.dto.ArquivoCSVDomicilio;
import br.jus.tjes.integracao.drive.util.dm.dto.LinkDownload;
import br.jus.tjes.integracao.drive.util.dm.dto.Objetos;

public class MainTeste {
	private static final String BASE_URL = "https://gateway.cloud.pje.jus.br/domicilio-eletronico/";
	private static final String URL_LIST_FILES = "api/v1/objetos/";
	private static final String PATH_DIR = "/home/orlando/Downloads/tmp/";
	
	public static void main(String[] args) throws IOException {
		MainTeste service = new MainTeste();
		List<String> listaArquivos = service.obterListaArquivos();
		List<ArquivoCSVDomicilio> arquivos = new ArrayList<ArquivoCSVDomicilio>();
		for (String chave : listaArquivos) {
			ArquivoCSVDomicilio arquivo = new ArquivoCSVDomicilio();
			arquivo.setNome(chave);
			arquivos.add(arquivo);
		}

		System.out.println("Obtendo link de download.");
		for (ArquivoCSVDomicilio arquivo : arquivos) {
			System.out.println("Arquivo: " + arquivo.getNome());
			arquivo.setUrl(service.obterLinkDownload(arquivo.getNome()));
		}
		System.out.println("---------------");
		System.out.println("Obtendo conteudo do documento");
		for (ArquivoCSVDomicilio arquivo : arquivos) {
			System.out.println("Arquivo: " + arquivo.getNome());
			arquivo.setConteudo(service.getArquivoEmBytes(arquivo.getUrl()));
			
			Files.write(Paths.get(PATH_DIR + arquivo.getNome()), arquivo.getConteudo());
			arquivo.setConteudo(null);
		}
		System.out.println("FIM!!");

	}

	private List<String> obterListaArquivos() {
		HttpGet request = new HttpGet(BASE_URL + URL_LIST_FILES);
		addBasicHeaders(request);
		try (CloseableHttpResponse response = HttpClients.createDefault().execute(request)) {
			HttpEntity entity = response.getEntity();
			System.out.println(response.getStatusLine().getStatusCode());
			if (entity != null) {
				Objetos objetos = RestUtil.convertEntityToObject(entity, Objetos.class);
				return objetos.getData().getChaves();
			}
			return Collections.emptyList();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String obterLinkDownload(String chave) {
		HttpGet request = new HttpGet(BASE_URL + URL_LIST_FILES + chave);
		addBasicHeaders(request);
		try (CloseableHttpResponse response = HttpClients.createDefault().execute(request)) {
			HttpEntity entity = response.getEntity();
			System.out.println(response.getStatusLine().getStatusCode());
			if (entity != null) {
				LinkDownload linkDownload = RestUtil.convertEntityToObject(entity, LinkDownload.class);
				return linkDownload.getData();
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] getArquivoEmBytes(String url) {
		HttpGet request = new HttpGet(url);
		try (CloseableHttpResponse response = HttpClients.createDefault().execute(request)) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return Util.toByteArray(entity.getContent());
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void addBasicHeaders(HttpGet request) {
		String token = getToken();
		request.addHeader(HttpHeaders.AUTHORIZATION, token);
	}

	private String getToken() {
		return "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI1dnJEZ1hCS21FLTdFb3J2a0U1TXU5VmxJZF9JU2dsMnY3QWYyM25EdkRVIn0.eyJleHAiOjE2OTIyMTQ0NzAsImlhdCI6MTY5MjE5Mjg3MCwianRpIjoiZWFmZGFmN2MtNjc1Ny00YzFiLTlkOGQtZGNiN2M5ZDNmYzViIiwiaXNzIjoiaHR0cHM6Ly9zc28uY2xvdWQucGplLmp1cy5ici9hdXRoL3JlYWxtcy9wamUiLCJhdWQiOlsicmVhbG0tbWFuYWdlbWVudCIsImFjY291bnQiXSwic3ViIjoiYTIwYjJjMGYtNDgzMC00ZGQ5LWEyY2YtZTQzMTQyNTkxMDQ5IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoicGplLXRqZGZ0LTFnIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3BqZS50amRmdC5qdXMuYnIiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS11c2VycyJdfSwicGplLXRqZGZ0LTFnIjp7InJvbGVzIjpbInVtYV9wcm90ZWN0aW9uIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImNsaWVudElkIjoicGplLXRqZGZ0LTFnIiwiY2xpZW50SG9zdCI6IjE3Ny4xNy4xNzEuMTk0IiwiSlRSIjoiODA3IiwianRyIjoiODA3IiwicHJlZmVycmVkX3VzZXJuYW1lIjoic2VydmljZS1hY2NvdW50LXBqZS10amRmdC0xZyIsImNsaWVudEFkZHJlc3MiOiIxNzcuMTcuMTcxLjE5NCJ9.ry_rpjrHtUEJLDfzn_LrK-QGpBc86IFmgJwkMlVZSEc1UnxkViIQdYZbf_R-nXXseW19R44NzmcOUM8BITaLMg6QRd8XumtSp0KDBl_tX8itg-W1QOQCQOnCpuXmM-998Zz3Xj3qvtjLkwLigxRkiAYhDqS1mYTkbMe0EKT2MJbIDJdCItbHTLy_JUlaoCqq6Kx-Ql8rFLyoWSELc7NwaLz-jPDOcUo9ZXw6CQ6lL72q5kAdqjMpJ7bDxyoiALS8I6-iD6f-JIPB3mRVDWTsLOB5IuK8iSK9wOhtDgJ1sb3Z1aVpao4giajN-zUwjdov7rSDVGgpeAdPG55v9uwDkA";
	}
}
