package br.jus.tjes.integracao.drive.exception;


import java.net.URISyntaxException;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.jus.tjes.integracao.drive.enums.EnumMensagens;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@ControllerAdvice
public class UrlExceptionHander extends ResponseEntityExceptionHandler {
	
	
	@ResponseBody
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorMessageUrl> tratarExpiredJwtException(ExpiredJwtException ex, WebRequest request) {

		ErrorMessageUrl mensagem = createMessageError(request,EnumMensagens.URL_EXPIRADA, HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<ErrorMessageUrl>(mensagem, HttpStatus.UNAUTHORIZED);

	}

	
	@ResponseBody
	@ExceptionHandler(MalformedJwtException.class)
	public ResponseEntity<ErrorMessageUrl> tratarMalformedJwtException(MalformedJwtException ex, WebRequest request) {

		ErrorMessageUrl mensagem = createMessageError(request,EnumMensagens.URL_EXPIRADA, HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<ErrorMessageUrl>(mensagem, HttpStatus.UNAUTHORIZED);

	}
	
	@ResponseBody
	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<ErrorMessageUrl> trataSignatureException(SignatureException ex, WebRequest request) {

		ErrorMessageUrl mensagem = createMessageError(request,EnumMensagens.URL_INVALIDA, HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<ErrorMessageUrl>(mensagem, HttpStatus.UNAUTHORIZED);

	}
	@ResponseBody
	@ExceptionHandler(UnsupportedJwtException.class)
	public ResponseEntity<ErrorMessageUrl> tratarUnsupportedJwtException(UnsupportedJwtException ex, WebRequest request) {

		ErrorMessageUrl mensagem = createMessageError(request,EnumMensagens.URL_INVALIDA, HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<ErrorMessageUrl>(mensagem, HttpStatus.UNAUTHORIZED);

	}
	
	@ResponseBody
	@ExceptionHandler(UrlInvalidaException.class)
	public ResponseEntity<ErrorMessageUrl> tratarUrlInvalidaExcepion(UrlInvalidaException ex, WebRequest request) {

		ErrorMessageUrl mensagem = createMessageError(request,EnumMensagens.URL_INVALIDA, HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<ErrorMessageUrl>(mensagem, HttpStatus.UNAUTHORIZED);

	}
	
	@ResponseBody
	@ExceptionHandler(URISyntaxException.class)
	public ResponseEntity<ErrorMessageUrl> tratarURISyntaxException(URISyntaxException ex, WebRequest request) {

		ErrorMessageUrl mensagem = createMessageError(request,EnumMensagens.URL_INVALIDA, HttpStatus.UNAUTHORIZED);
		return new ResponseEntity<ErrorMessageUrl>(mensagem, HttpStatus.UNAUTHORIZED);

	}
	
	@ResponseBody
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorMessageUrl> tratarRuntimeException(RuntimeException ex, WebRequest request) {

		ErrorMessageUrl mensagem = createMessageError(request,EnumMensagens.ERRO_INTERNO, HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<ErrorMessageUrl>(mensagem, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	

	private ErrorMessageUrl createMessageError(WebRequest request,EnumMensagens enumMensagem,HttpStatus status) {
		ErrorMessageUrl mensagem = new ErrorMessageUrl();
		mensagem.setStatus(status.value());
		mensagem.setData(new Date());
		mensagem.setMensagem(enumMensagem.getMensagem());
		mensagem.setDescricao(request.getDescription(false));
		return mensagem;
	}
	
}
