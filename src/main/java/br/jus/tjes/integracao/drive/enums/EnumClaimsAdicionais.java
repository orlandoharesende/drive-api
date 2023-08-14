package br.jus.tjes.integracao.drive.enums;

public enum EnumClaimsAdicionais {

	NR_PROCESSO("nrprocesso"), ID_DOC_GOOGLE("iddoc");

	private String descricao;

	EnumClaimsAdicionais(String descricao) {

		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
