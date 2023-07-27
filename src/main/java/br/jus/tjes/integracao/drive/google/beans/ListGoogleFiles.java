package br.jus.tjes.integracao.drive.google.beans;

import java.util.Collections;
import java.util.List;

public class ListGoogleFiles {
	private String kind;
	private boolean incompleteSearch;
	private List<File> files;

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public boolean isIncompleteSearch() {
		return incompleteSearch;
	}

	public void setIncompleteSearch(boolean incompleteSearch) {
		this.incompleteSearch = incompleteSearch;
	}

	public List<File> getFiles() {
		if (files == null) {
			return Collections.emptyList();
		}
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

}