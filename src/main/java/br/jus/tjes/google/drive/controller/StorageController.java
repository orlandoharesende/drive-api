package br.jus.tjes.google.drive.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.jus.tjes.google.drive.dto.ArquivoDTO;
import br.jus.tjes.google.drive.service.StorageService;
import io.swagger.annotations.ApiOperation;


@RestController
public class StorageController {
	@Autowired
	private StorageService service;
	
	@ApiOperation(value = "Relatório de expedientes enviados pelo tribunal aos Correios")
	@GetMapping(path = "/{numero-processo}/arquivos", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ArquivoDTO> consultarListaArquivos(@PathVariable("numero-processo") String numeroProcesso) {
		List<ArquivoDTO> lista = service.listarArquivos(numeroProcesso);
		return lista;
	}
	
	@ApiOperation(value = "Relatório de expedientes enviados pelo tribunal aos Correios")
	@GetMapping(path = "/teste", produces = MediaType.APPLICATION_JSON_VALUE)
	public String teste() {
		return "Hello world!!";
	}
	
}
