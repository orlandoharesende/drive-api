package br.jus.tjes.integracao.drive.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class File {
	public String id;
	public String parentID;
	public String mimeType;
	public String name;
	public Date mTime;
	public Date cTime;
	public String storageType;
	public String storageID;
	public boolean publiclyShared;
	public boolean privatelyShared;
	public boolean trashed;
	public long size;
	public String extension;
	public List<File> filhos = new ArrayList<File>();
	public File pai;
	public int nivel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getmTime() {
		return mTime;
	}

	public void setmTime(Date mTime) {
		this.mTime = mTime;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public String getStorageID() {
		return storageID;
	}

	public void setStorageID(String storageID) {
		this.storageID = storageID;
	}

	public boolean isPubliclyShared() {
		return publiclyShared;
	}

	public void setPubliclyShared(boolean publiclyShared) {
		this.publiclyShared = publiclyShared;
	}

	public boolean isPrivatelyShared() {
		return privatelyShared;
	}

	public void setPrivatelyShared(boolean privatelyShared) {
		this.privatelyShared = privatelyShared;
	}

	public boolean isTrashed() {
		return trashed;
	}

	public void setTrashed(boolean trashed) {
		this.trashed = trashed;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	@Override
	public String toString() {
		StringBuilder texto = new StringBuilder();
		String legenda = (isDir() ? "»█ DIR" : "¤ FILE");
		texto.append(String.format(" %s %s: %s (%s)", getIconTree(this.nivel), legenda, name, id));
		if (filhos != null) {
			for (File file : filhos) {
				texto.append("\n");
				texto.append(file.toString());
			}
		}
		return texto.toString();
	}

	private static String getIconTree(int nivel) {
		StringBuilder texto = new StringBuilder();
		nivel = nivel -1;
		if (nivel > 0) {
			texto.append(StringUtils.leftPad("", nivel * 4, " "));
			texto.append(StringUtils.rightPad("├", 4, "─"));
		}
		return texto.toString();
	}

	public boolean isDir() {
		return mimeType.equalsIgnoreCase("application/x.wd.dir");
	}

	public List<File> getFilhos() {
		return filhos;
	}

	public void setFilhos(List<File> filhos) {
		this.filhos = filhos;
	}

	public File getPai() {
		return pai;
	}

	public void setPai(File pai) {
		this.pai = pai;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

}
