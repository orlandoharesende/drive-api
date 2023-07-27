package br.jus.tjes.integracao.drive.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class Health {
	@Operation(summary = "Consulta Lista de Arquivos")
	@GetMapping(path = "health/up", produces = MediaType.APPLICATION_JSON_VALUE)
	public String teste() {
		return "true";
	}
	
}
