package br.jus.tjes.integracao.drive.controller;

import java.util.HashMap;
import java.util.Map;

public class UrlBuilder {

	private String baseUrl;
	private String path;
	private Map<String, String> pathVariable = new HashMap<String, String>();
	private Map<String, String> queryParams = new HashMap<String, String>();
	private String url;

	private UrlBuilder() {
	}

	private UrlBuilder(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public static UrlBuilder builder() {
		return new UrlBuilder();
	}

	public static UrlBuilder builder(String baseUrl) {
		return new UrlBuilder(baseUrl);
	}

	public UrlBuilder baseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}

	public UrlBuilder path(String path) {
		this.path = path;
		return this;
	}

	public UrlBuilder addPathVariable(String chave, String valor) {
		this.pathVariable.put(String.format("${%s}", chave), valor);
		return this;
	}

	public UrlBuilder addQueryParameter(String chave, Object valor) {
		this.queryParams.put(chave, valor.toString());
		return this;
	}

	public UrlBuilder build() {
		StringBuilder url = new StringBuilder();
		url.append(this.baseUrl);
		if (!this.baseUrl.endsWith("/")) {
			url.append("/");
		}

		if (this.path != null) {
			String pathBuilded = processPathVariable();
			url.append(pathBuilded);
		}

		if (!queryParams.isEmpty()) {
			url.append("?");
			int count = 0;
			for (String key : this.queryParams.keySet()) {
				if ((++count) > 0) {
					url.append("&");
				}
				url.append(String.format("%s=%s", key, this.queryParams.get(key)));
			}
		}
		this.url = url.toString();
		return this;
	}

	private String processPathVariable() {
		String pathBuilded = this.path;
		for (String key : this.pathVariable.keySet()) {
			pathBuilded = pathBuilded.replace(key, this.pathVariable.get(key));
		}
		if (this.path.startsWith("/")) {
			pathBuilded = pathBuilded.substring(1);
		}
		return pathBuilded;
	}

	public String toUrl() {
		return url;
	}
}
