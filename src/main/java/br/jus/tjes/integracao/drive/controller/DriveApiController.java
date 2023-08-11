package br.jus.tjes.integracao.drive.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.jus.tjes.integracao.drive.dto.ArquivoDTO;
import br.jus.tjes.integracao.drive.dto.DocumentoDTO;
import br.jus.tjes.integracao.drive.service.DriveApiService;
import br.jus.tjes.integracao.drive.service.UrlTemporarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
public class DriveApiController {
	@Autowired
	private DriveApiService service;
	
	@Autowired
	private UrlTemporarioService urlTemp;

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
	
	@Operation(summary = "Obter link temporário")
	@PostMapping(value = "/gerar-link",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> getLinkTemporario( @Valid @RequestBody DocumentoDTO documento) {
		//Validar a existência do documento		
		String baseUrl = ServletUriComponentsBuilder.fromCurrentServletMapping().build().toUriString();
		String urlTmp = urlTemp.getUrlTemporaria(documento,baseUrl);
		System.out.println(urlTmp);
		URI uri  = null;
		try {
			 uri = new URI(urlTmp);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ResponseEntity.created(uri).build();
	}
	
	
	@Operation(summary = "Consulta Arquivo Específico")
	@GetMapping(path = "/download/tmp", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> download(@RequestParam String id) {
		ResponseEntity<byte[]> resposta = null;
		String baseUrl = ServletUriComponentsBuilder.fromCurrentServletMapping().build().toUriString();
		Boolean isValido = urlTemp.validarToken(id,baseUrl);
		if(isValido) {
			String numeroProcesso = urlTemp.extrairNrProcesso(id);
			String idDocGoogle = urlTemp.extrairIdDocumentoGoogle(id);
			ArquivoDTO arquivoDTO = service.getArquivo(numeroProcesso, idDocGoogle);
			byte[] arquivo = service.getArquivoEmBytes(numeroProcesso, idDocGoogle);
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, arquivoDTO.getMimeType());
			headers.add(HttpHeaders.CONTENT_DISPOSITION,"inline;filename="+arquivoDTO.getNome());
			headers.setContentLength(arquivo.length);
			 resposta = new ResponseEntity<byte[]>(arquivo, headers, HttpStatus.SC_OK);
		}else
		{
			resposta = ResponseEntity.notFound().build();
		}
		return resposta;
	}
	

}
