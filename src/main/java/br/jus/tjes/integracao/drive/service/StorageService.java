package br.jus.tjes.integracao.drive.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import br.jus.tjes.integracao.drive.dto.ArquivoDTO;

@Service
public class StorageService {

	public List<ArquivoDTO> listarArquivos(String numeroProcesso) {
		ArquivoDTO a1 = new ArquivoDTO();
		a1.setId("1");
		a1.setNome("Arquivo.pdf");
		a1.setMimeType(("application/pdf"));
		a1.setDiretorio(false);

		ArquivoDTO a2 = new ArquivoDTO();
		a2.setId("2");
		a2.setNome("Arquivo.pdf");
		a2.setMimeType(("application/pdf"));
		a2.setDiretorio(false);

		return Arrays.asList(a1, a2);
	}

}
