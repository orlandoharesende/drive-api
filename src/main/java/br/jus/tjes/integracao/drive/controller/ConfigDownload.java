package br.jus.tjes.integracao.drive.controller;

public class ConfigDownload {
	private String authority;
	private String correlation;
	private String accessToken;
	private String baseUrl;
	private String deviceId;
	private String localFolder;
	private String mainFolder;

	private ConfigDownload() {

	}

	public static ConfigDownload builder() {
		return new ConfigDownload();
	}

	public String getAuthority() {
		return authority;
	}

	public ConfigDownload authority(String authority) {
		this.authority = authority;
		return this;
	}

	public String getCorrelationId() {
		return correlation;
	}

	public ConfigDownload correlationId(String correlation) {
		this.correlation = correlation;
		return this;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public ConfigDownload accessToken(String authorization) {
		this.accessToken = authorization;
		return this;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public ConfigDownload baseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public ConfigDownload deviceId(String sharedKey) {
		this.deviceId = sharedKey;
		return this;
	}

	public String getLocalFolder() {
		return localFolder;
	}

	public ConfigDownload localFolder(String localFolder) {
		this.localFolder = localFolder;
		return this;
	}

	public ConfigDownload build() {
		this.authority = this.baseUrl.replace("https://", "");
		return this;
	}

	public String getMainFolder() {
		return mainFolder;
	}

	public ConfigDownload mainFolder(String idMainFolder) {
		this.mainFolder = idMainFolder;
		return this;
	}

}
