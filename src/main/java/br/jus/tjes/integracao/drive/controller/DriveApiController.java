package br.jus.tjes.integracao.drive.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.ContentDisposition.Builder;
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
import br.jus.tjes.integracao.drive.exception.UrlInvalidaException;
import br.jus.tjes.integracao.drive.models.TokenDocumento;
import br.jus.tjes.integracao.drive.service.DriveApiService;
import br.jus.tjes.integracao.drive.service.UrlTemporariaService;
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
	private UrlTemporariaService urlTempService;

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
	@PostMapping(value = "/gerar-link", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> getLinkTemporario(@Valid @RequestBody DocumentoDTO documento)
			throws URISyntaxException {
		String baseUrl = ServletUriComponentsBuilder.fromCurrentServletMapping().build().toUriString();
		String urlTmp = urlTempService.getUrlTemporaria(documento, baseUrl);	
		URI uri = null;
		uri = new URI(urlTmp);
		return ResponseEntity.created(uri).build();
	}
	
	
	@Operation(summary = "Consulta Arquivo Específico")
	@GetMapping(path = "/download/tmp", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<byte[]> download(@RequestParam String id) throws UrlInvalidaException {
		ResponseEntity<byte[]> resposta = null;
		String baseUrl = ServletUriComponentsBuilder.fromCurrentServletMapping().build().toUriString();
		TokenDocumento td = urlTempService.getArquivoRemoto(id, baseUrl);
		Builder contentDisposition = ContentDisposition.inline().filename(td.getArquivo().getNome());
		resposta = getResponsePdf(td, contentDisposition);

		return resposta;
	}

	private ResponseEntity<byte[]> getResponsePdf(TokenDocumento td, Builder contentDisposition) {
		ResponseEntity<byte[]> resposta;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.valueOf(td.getArquivo().getMimeType()));
		headers.setContentDisposition(contentDisposition.build());		
		headers.setContentLength(td.getArquivoPdf().length);
		resposta = new ResponseEntity<byte[]>(td.getArquivoPdf(), headers, HttpStatus.SC_OK);
		return resposta;
	}
	
	
	

}
