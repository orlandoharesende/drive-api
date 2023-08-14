package br.jus.tjes.integracao.drive.exception;


import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@ControllerAdvice
public class UrlExceptionHander extends ResponseEntityExceptionHandler {
	
	
	@ResponseBody
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorMessage> tratarExpiredJwtException(ExpiredJwtException ex, WebRequest request) {

		ErrorMessage mensagem = new ErrorMessage();
		mensagem.setStatus(HttpStatus.UNAUTHORIZED.value());
		mensagem.setData(new Date());
		mensagem.setMensagem("Token Expirado.");

		return new ResponseEntity<ErrorMessage>(mensagem, HttpStatus.UNAUTHORIZED);

	}
	
	@ResponseBody
	@ExceptionHandler(MalformedJwtException.class)
	public ResponseEntity<ErrorMessage> tratarMalformedJwtException(MalformedJwtException ex, WebRequest request) {

		ErrorMessage mensagem = new ErrorMessage();
		mensagem.setStatus(HttpStatus.UNAUTHORIZED.value());
		mensagem.setData(new Date());
		mensagem.setMensagem("Token malformado.");

		return new ResponseEntity<ErrorMessage>(mensagem, HttpStatus.UNAUTHORIZED);

	}
	
	@ResponseBody
	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<ErrorMessage> trataSignatureException(SignatureException ex, WebRequest request) {

		ErrorMessage mensagem = new ErrorMessage();
		mensagem.setStatus(HttpStatus.UNAUTHORIZED.value());
		mensagem.setData(new Date());
		mensagem.setMensagem("token inválido.");

		return new ResponseEntity<ErrorMessage>(mensagem, HttpStatus.UNAUTHORIZED);

	}
	@ResponseBody
	@ExceptionHandler(UnsupportedJwtException.class)
	public ResponseEntity<ErrorMessage> tratarUnsupportedJwtException(UnsupportedJwtException ex, WebRequest request) {

		ErrorMessage mensagem = new ErrorMessage();
		mensagem.setStatus(HttpStatus.UNAUTHORIZED.value());
		mensagem.setData(new Date());
		mensagem.setMensagem("Formado do token não suportado.");

		return new ResponseEntity<ErrorMessage>(mensagem, HttpStatus.UNAUTHORIZED);

	}
	

}
