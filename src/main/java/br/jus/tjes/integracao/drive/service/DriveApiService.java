package br.jus.tjes.integracao.drive.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.jus.tjes.integracao.drive.dto.ArquivoDTO;
import br.jus.tjes.integracao.drive.exception.NumeroProcessoInvalidoException;
import br.jus.tjes.integracao.drive.util.NumeroProcessoUtil;
import br.jus.tjes.integracao.drive.util.StringUtil;

@Service
public class DriveApiService {

	@Autowired
	private GoogleDriveApi googleDriveApi;

	@Cacheable(value = "DriveApiServiceListarArquivosPorDiretorioProcesso")
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
				getQueryListaArquivosPorDiretorio(diretorio.getId()));
		ajustarInformacoesComplementares(numeroProcesso, diretorio, listaArquivos);
		return listaArquivos;
	}

	private void ajustarInformacoesComplementares(String numeroProcesso, ArquivoDTO diretorio,
			List<ArquivoDTO> listaArquivos) {
		diretorio.setNumeroProcesso(numeroProcesso);
		listaArquivos.forEach(a -> a.setDiretorioPai(diretorio));
		listaArquivos.forEach(a -> a.setNumeroProcesso(numeroProcesso));
	}

	@Cacheable(value = "DriveApiServiceListarArquivosPorDiretorioProcessoDiretorioFilho")
	public List<ArquivoDTO> listarArquivosPorDiretorioProcesso(String numeroProcesso, String idDiretorio) {
		validarNumeroProcesso(numeroProcesso);
		ArquivoDTO diretorioPai = criarRepresentacaoMinimaDiretorioPai(idDiretorio, numeroProcesso);
		List<ArquivoDTO> listaArquivos = googleDriveApi.listarArquivos(numeroProcesso,
				getQueryListaArquivosPorDiretorio(idDiretorio));
		ajustarInformacoesComplementares(numeroProcesso, diretorioPai, listaArquivos);
		return listaArquivos;
	}

	private ArquivoDTO criarRepresentacaoMinimaDiretorioPai(String idDiretorio, String numeroProcesso) {
		ArquivoDTO diretorioPai = new ArquivoDTO();
		diretorioPai.setDiretorio(true);
		diretorioPai.setMimeType("folder");
		diretorioPai.setId(idDiretorio);
		diretorioPai.setNumeroProcesso(numeroProcesso);
		return diretorioPai;
	}

	private void validarSeDiretorioValido(List<ArquivoDTO> listaDiretorios) {
		if (listaDiretorios.size() > 1) {
			throw new RuntimeException("Existe mais de um diretório com o mesmo nome.");
		}
	}

	private String getQueryListaArquivosPorDiretorio(String idDiretorio) {
		return q("'%s' in parents", idDiretorio);
	}

	private String getQueryFiltrarApenasDiretorioPorNome(String numeroProcesso) {
		return q("name = '%s' and mimeType = '%s'", StringUtil.removerNaoNumeros(numeroProcesso),
				GoogleDriveApi.MIME_TYPE_FOLDER);
	}

	@Cacheable(value = "DriveApiServiceGetArquivo")
	public ArquivoDTO getArquivo(String numeroProcesso, String idArquivo) {
		validarNumeroProcesso(numeroProcesso);
		return googleDriveApi.getArquivo(StringUtil.removerNaoNumeros(numeroProcesso), idArquivo);
	}

	public byte[] getArquivoEmBytes(String numeroProcesso, String idArquivo) {
		validarNumeroProcesso(numeroProcesso);
		return googleDriveApi.getArquivoEmBytes(StringUtil.removerNaoNumeros(numeroProcesso), idArquivo);
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
