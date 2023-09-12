package br.jus.tjes.integracao.drive.sso;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.jus.tjes.integracao.drive.controller.UrlBuilder;
import br.jus.tjes.integracao.drive.util.Log;

public class UpdateUserSSO {
	public static String BASE_URL = "https://sso.cloud.pje.jus.br/auth/admin/realms/pje/";
	public static final Integer DEFAULT_HTTP_CLIENT_TIMEOUT_IN_MILLIS = 60000;
	private static final String ACCESS_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI1dnJEZ1hCS21FLTdFb3J2a0U1TXU5VmxJZF9JU2dsMnY3QWYyM25EdkRVIn0.eyJleHAiOjE2OTQ0NzkzOTEsImlhdCI6MTY5NDQ1Nzc5MSwianRpIjoiZDgxMzQ3ODEtYzljNy00NWEzLTliM2EtYjdiZGQyZGFhZTEzIiwiaXNzIjoiaHR0cHM6Ly9zc28uY2xvdWQucGplLmp1cy5ici9hdXRoL3JlYWxtcy9wamUiLCJhdWQiOlsicmVhbG0tbWFuYWdlbWVudCIsImFjY291bnQiXSwic3ViIjoiYTIwYjJjMGYtNDgzMC00ZGQ5LWEyY2YtZTQzMTQyNTkxMDQ5IiwidHlwIjoiQmVhcmVyIiwiYXpwIjoicGplLXRqZGZ0LTFnIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3BqZS50amRmdC5qdXMuYnIiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbIm1hbmFnZS1yZWFsbSIsIm1hbmFnZS11c2VycyJdfSwicGplLXRqZGZ0LTFnIjp7InJvbGVzIjpbInVtYV9wcm90ZWN0aW9uIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImNsaWVudElkIjoicGplLXRqZGZ0LTFnIiwiY2xpZW50SG9zdCI6IjE4OS42LjE0LjE2MCIsIkpUUiI6IjgwNyIsImp0ciI6IjgwNyIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC1wamUtdGpkZnQtMWciLCJjbGllbnRBZGRyZXNzIjoiMTg5LjYuMTQuMTYwIn0.nT0qJ7f8anmOSXZPmO4WkjWkStcqYcV8dGqWkMQZtIdCUbglN1EkZLuG_zzPI4ZHGQRhPqKZAlhbWBHXz98G4DQEh2Y0ZZzqf57gs5pIfNyuv9uXmlRunyME2CfpDMev5CKUSaZV_syyUImkW5DePlPIznZfV0sZ0DOMpEYftgm2Ags1qi19RFJDGCrunKAYadwF4lOV7Nptp9HCD5NO4UT-OoBmS1Pcr2yYaSGvf6q4Kon1Xq4yoECcAc9q-0BfW7xMLHruOtss2KmSAe4rCIZC7AgjETHHqtkfL2N9WNQjQt07fjdSlX23U09Mf5TylN2ejOgVxoox_dHQWo4i0g";
	public static final String PATH = "/home/orlando/Downloads/tmp/update-sso/2i-email.txt";

	public static void main(String[] args) {
		UpdateUserSSO service = new UpdateUserSSO();
		AtomicInteger contador = new AtomicInteger();

		try {
			List<Pessoa> pessoas = service.geraListaPessoas();
			pessoas.parallelStream().forEach(p -> {
				contador.getAndIncrement();
				Log.info("Atualizando registro [%s/%s]: %s", contador.get(), pessoas.size(), p);
				service.updateUser(p);
			}

			);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error("Erro ao atualizar usuário." + e.getMessage());
		}
		Log.info("Fim do programa");
	}

	private void updateUser(Pessoa pessoa) {
		try {
			User user = consultarUsuario(pessoa.getCpf());
			if(user != null) {
				user.setEmail(pessoa.getEmail());
				update(user);
			}else {
				Log.error("Usuário não encontrado no SSO: " + pessoa.getCpf());
			}
		} catch (Exception e) {
			Log.error("Erro ao atualizar usuário: %s", pessoa.getCpf());
		}

	}

	public List<Pessoa> geraListaPessoas() throws Exception {

		CarregadorArquivo carregadorArquivo = new CarregadorArquivo(PATH);
		List<String> listaPessoas = carregadorArquivo.recuperarProcessosDoArquivo();

		System.out.println("Total de Registros: " + listaPessoas.size());

		int[] contador = { 0 };
		List<Pessoa> pessoas = new ArrayList<>();

		listaPessoas.forEach(id -> {
			contador[0]++;
			try {
				String linha[] = id.split(";");
				pessoas.add(new Pessoa(linha[0], linha[1]));

			} catch (Exception e) {
				e.printStackTrace();
			}

		});
		return pessoas;
	}

	public User consultarUsuario(String cpf) {
		String url = UrlBuilder.builder(BASE_URL).path("/users").addQueryParameter("username", cpf).build().toUrl();
		HttpGet request = new HttpGet(url);
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN);
		try (CloseableHttpResponse response = executeHttpClient(request)) {
			HttpEntity entity = response.getEntity();
			if (isSucess(response) && entity != null) {
				User[] users = createJsonMapper().readValue(EntityUtils.toString(entity), User[].class);
				return users[0];
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public User update(User user) throws Exception {
		String url = UrlBuilder.builder(BASE_URL).path("/users/${id}").addPathVariable("id", user.getId()).build()
				.toUrl();
		HttpPut request = new HttpPut(url);

		HttpEntity entity = new StringEntity(createJsonMapper().writeValueAsString(user), ContentType.APPLICATION_JSON);
		request.setEntity(entity);
		addBasicHeaders(request);
		try (CloseableHttpResponse response = executeHttpClient(request)) {
			if (isSucess(response)) {
				Log.info("Atualizado com sucesso");
			}
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void addBasicHeaders(HttpPut request) {
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + ACCESS_TOKEN);
	}

	private boolean isSucess(CloseableHttpResponse response) {
		int statusCode = response.getStatusLine().getStatusCode();
		return (statusCode >= 200 && statusCode < 300);
	}

	private CloseableHttpClient createHttpClient(HttpUriRequest request) throws Exception {
		RequestConfig config = RequestConfig.custom() //
				.setConnectTimeout(DEFAULT_HTTP_CLIENT_TIMEOUT_IN_MILLIS) //
				.setConnectionRequestTimeout(DEFAULT_HTTP_CLIENT_TIMEOUT_IN_MILLIS)//
				.setSocketTimeout(DEFAULT_HTTP_CLIENT_TIMEOUT_IN_MILLIS) //
				.build();
		return HttpClients.custom().setDefaultRequestConfig(config).build();
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

}

class Pessoa {

	private String cpf;
	private String email;

	public Pessoa(String cpf, String email) {
		super();
		this.cpf = cpf;
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Pessoa [cpf=" + cpf + ", email=" + email + "]";
	}
}

class CarregadorArquivo {
	private String caminhoArquivo;

	public CarregadorArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

	public List<String> recuperarProcessosDoArquivo() {
		try {
			List<String> linhas = Files.readAllLines(Paths.get(UpdateUserSSO.PATH));
			return linhas;
		} catch (Exception e) {
			throw new RuntimeException("Houve erro na leitura do arquivo. Endereço: " + caminhoArquivo, e);
		}
	}

}
