package br.jus.tjes.integracao.drive.controller;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.jus.tjes.integracao.drive.dto.ArquivoDTO;
import br.jus.tjes.integracao.drive.service.DriveApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class DriveApiController {
	@Autowired
	private DriveApiService service;

	@Operation(summary = "Consulta Lista de Arquivos")
	@GetMapping(path = "/{numero-processo}/arquivos", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ArquivoDTO>> consultarListaArquivos(
			@PathVariable("numero-processo") String numeroProcesso) {
		List<ArquivoDTO> lista = service.listarArquivosPorDiretorioProcesso(numeroProcesso);
		return ResponseEntity.ok(lista);
	}

	@Operation(summary = "Consulta Arquivo Específico")
	@GetMapping(path = "/{numero-processo}/arquivos/{id-arquivo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArquivoDTO> consultarListaArquivos(@PathVariable("numero-processo") String numeroProcesso,
			@PathVariable("id-arquivo") String idArquivo) {
		ArquivoDTO arquivo = service.getArquivo(numeroProcesso, idArquivo);
		return ResponseEntity.ok(arquivo);
	}

	@Operation(summary = "Consulta Arquivo Específico", responses = {
			@ApiResponse(content = @Content(schema = @Schema(implementation = Byte[].class)), responseCode = "200") })
	@GetMapping(path = "/{numero-processo}/arquivos/{id-arquivo}/download")
	public ResponseEntity<byte[]> consultarConteudoArquivo(@PathVariable("numero-processo") String numeroProcesso,
			@PathVariable("id-arquivo") String idArquivo) {
		ArquivoDTO arquivoDTO = service.getArquivo(numeroProcesso, idArquivo);
		byte[] arquivo = service.getArquivoEmBytes(numeroProcesso, idArquivo);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, arquivoDTO.getMimeType());
		headers.setContentDispositionFormData("attachment", arquivoDTO.getNome());
		headers.setContentLength(arquivo.length);
		return new ResponseEntity<byte[]>(arquivo, headers, HttpStatus.SC_OK);
	}

}
