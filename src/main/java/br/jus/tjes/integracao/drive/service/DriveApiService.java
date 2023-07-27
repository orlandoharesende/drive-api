package br.jus.tjes.integracao.drive.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.jus.tjes.integracao.drive.dto.ArquivoDTO;

@Service
public class DriveApiService {

	@Autowired
	private GoogleDriveApi googleDriveApi;

	public List<ArquivoDTO> listarArquivos(String numeroProcesso) {
		return googleDriveApi.listarArquivos(numeroProcesso);
	}

	public ArquivoDTO getArquivo(String numeroProcesso, String idArquivo) {
		return googleDriveApi.getArquivo(numeroProcesso, idArquivo);
	}

}
