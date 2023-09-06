package br.jus.tjes.integracao.drive.controller;

import java.util.ArrayList;

public class Root {
	public String pageToken;
	public ArrayList<File> files;

	public String getPageToken() {
		return pageToken;
	}

	public void setPageToken(String pageToken) {
		this.pageToken = pageToken;
	}

	public ArrayList<File> getFiles() {
		return files;
	}

	public void setFiles(ArrayList<File> files) {
		this.files = files;
	}

}
