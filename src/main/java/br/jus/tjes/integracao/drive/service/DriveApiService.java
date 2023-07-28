package br.jus.tjes.integracao.drive.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.jus.tjes.integracao.drive.dto.ArquivoDTO;
import br.jus.tjes.integracao.drive.exception.NumeroProcessoInvalidoException;
import br.jus.tjes.integracao.drive.util.NumeroProcessoUtil;

@Service
public class DriveApiService {

	@Autowired
	private GoogleDriveApi googleDriveApi;

	public List<ArquivoDTO> listarArquivosPorDiretorioProcesso(String numeroProcesso) {
		validarNumeroProcesso(numeroProcesso);
		List<ArquivoDTO> listaDiretorios = googleDriveApi.listarArquivos(numeroProcesso,
				getQueryFiltrarApenasDiretorioPorNome(numeroProcesso));
		if (listaDiretorios.isEmpty()) {
			return Collections.emptyList();
		}
		validarSeDiretorioValido(listaDiretorios);
		ArquivoDTO diretorio = listaDiretorios.get(0);
		List<ArquivoDTO> listaArquivos = googleDriveApi.listarArquivos(numeroProcesso,
				getQueryListaArquivosPorDiretorio(diretorio));

		return listaArquivos;
	}

	private void validarSeDiretorioValido(List<ArquivoDTO> listaDiretorios) {
		if (listaDiretorios.size() > 1) {
			throw new RuntimeException("Existe mais de um diretório com o mesmo nome.");
		}
	}

	private String getQueryListaArquivosPorDiretorio(ArquivoDTO diretorio) {
		return q("'%s' in parents", diretorio.getId());
	}

	private String getQueryFiltrarApenasDiretorioPorNome(String numeroProcesso) {
		return q("name = '%s' and mimeType = '%s'", numeroProcesso, GoogleDriveApi.MIME_TYPE_FOLDER);
	}

	public ArquivoDTO getArquivo(String numeroProcesso, String idArquivo) {
		validarNumeroProcesso(numeroProcesso);
		return googleDriveApi.getArquivo(numeroProcesso, idArquivo);
	}

	public byte[] getArquivoEmBytes(String numeroProcesso, String idArquivo) {
		validarNumeroProcesso(numeroProcesso);
		return googleDriveApi.getArquivoEmBytes(numeroProcesso, idArquivo);
	}

	private void validarNumeroProcesso(String numeroProcesso) {
		if (!NumeroProcessoUtil.numeroProcessoValido(numeroProcesso)) {
			throw new NumeroProcessoInvalidoException();
		}
	}

	private String q(String query, Object... valores) {
		return String.format(query, valores);
	}

}
