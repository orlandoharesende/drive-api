package br.jus.tjes.integracao.drive.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.jus.tjes.integracao.drive.dto.ArquivoDTO;
import br.jus.tjes.integracao.drive.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;


@RestController
public class StorageController {
	@Autowired
	private StorageService service;
	
	@Operation(summary = "Consulta Lista de Arquivos")
	@GetMapping(path = "/{numero-processo}/arquivos", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ArquivoDTO> consultarListaArquivos(@PathVariable("numero-processo") String numeroProcesso) {
		List<ArquivoDTO> lista = service.listarArquivos(numeroProcesso);
		return lista;
	}
	
	@Operation(summary = "Testes")
	@GetMapping(path = "/teste", produces = MediaType.APPLICATION_JSON_VALUE)
	public String teste() {
		return "Hello world!!";
	}
	
}
