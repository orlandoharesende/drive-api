package br.jus.tjes.integracao.drive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.jus.tjes.integracao.drive.dto.ModeloDTO;
import br.jus.tjes.integracao.drive.service.TesteService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
public class TesteController {
	@Autowired
	private TesteService service;

	@Operation(summary = "Consulta básica de teste")
	@GetMapping(path = "/modelo/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ModeloDTO> consultarListaArquivos(@PathVariable("id") String id) {
		return ResponseEntity.ok(service.consultarModelo(id));
	}

}
