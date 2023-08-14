package br.jus.tjes.integracao.drive.util;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.grpc.Context.Key;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@ApplicationScope
public class JwtTokenUtil implements Serializable {
	
	
	
	/* @Value("${url.jwt.secret}") */
	private static String secret="MTIzNDU2";
	
	//* @Value("${url.jwt.expiracao}") */
	private static final long  TEMPO_DURACAO_TOKEN = 3000000;
	
	
	public static String geradorDeTokenTemporario(String idUsuario, Map<String, String> parametrosAdicionais,
			String emissor) {
		/*
		 * long now = System.currentTimeMillis(); Date dataAtual = new Date(now); Date
		 * dataExpiracao = new Date(now + TEMPO_DURACAO_TOKEN); JwtBuilder jwt =
		 * Jwts.builder().setIssuer(emissor).setAudience(emissor).setSubject(subject)
		 * .setIssuedAt(dataAtual).setExpiration(dataExpiracao).signWith(
		 * SignatureAlgorithm.HS512, getChave());
		 * parametrosAdicionais.entrySet().stream().forEach(p -> jwt.claim(p.getKey(),
		 * p.getValue())); return jwt.compact();
		 */
		return null;
	}
	
	public static Claims extrairInformacoesToken(String token) {
		

		return Jwts.parser().setSigningKey(getChave()).parseClaimsJws(token).getBody();
	}
   
   
	public static String recuperarIdDocumento(String token) {
		Claims claims = extrairInformacoesToken(token);
		return String.class.cast(claims.get("id-doc"));

	}

	public static boolean isTokenExpirado(String token) {
		long now = System.currentTimeMillis();
		Date data = new Date(now);
		return extrairInformacoesToken(token).getExpiration().after(data);

	}

	private static byte[] getChave() {

		return Base64.decodeBase64(secret);
	}

	public static Boolean validarToken(String token, String emissor) {
		try {
			Jwts.parser().setSigningKey(getChave()).parseClaimsJws(token);
		} catch (Exception e) {
			return false;
		}

		return emissor.equals(extrairInformacoesToken(token).getIssuer()) && isTokenExpirado(token);
	}

	public static String recuperarNrProcesso(String token) {
		// TODO Auto-generated method stub
		return String.class.cast(extrairInformacoesToken(token).get("nr_processo"));
	}
}
