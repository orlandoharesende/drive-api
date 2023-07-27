package br.jus.tjes.integracao.drive.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.jus.tjes.integracao.drive.dto.ArquivoDTO;
import br.jus.tjes.integracao.drive.service.DriveApiService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
public class DriveApiController {
	@Autowired
	private DriveApiService service;

	@Operation(summary = "Consulta Lista de Arquivos")
	@GetMapping(path = "/{numero-processo}/arquivos", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ArquivoDTO> consultarListaArquivos(@PathVariable("numero-processo") String numeroProcesso) {
		List<ArquivoDTO> lista = service.listarArquivos(numeroProcesso);
		return lista;
	}

	@Operation(summary = "Consulta Arquivo Específico")
	@GetMapping(path = "/{numero-processo}/arquivos/{id-arquivo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ArquivoDTO consultarListaArquivos(@PathVariable("numero-processo") String numeroProcesso,
			@PathVariable("id-arquivo") String idArquivo) {
		ArquivoDTO lista = service.getArquivo(numeroProcesso, idArquivo);
		return lista;
	}

	@Operation(summary = "Consulta Arquivo Específico")
	@GetMapping(path = "/{numero-processo}/arquivos/{id-arquivo}/download", produces = MediaType.TEXT_PLAIN_VALUE)
	public String consultarConteudoArquivo(@PathVariable("numero-processo") String numeroProcesso,
			@PathVariable("id-arquivo") String idArquivo) {
		return "Método ainda não implementado";
	}
}
