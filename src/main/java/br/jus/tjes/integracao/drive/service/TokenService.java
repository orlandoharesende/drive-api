package br.jus.tjes.integracao.drive.service;

import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.jus.tjes.integracao.drive.enums.EnumClaimsAdicionais;
import br.jus.tjes.integracao.drive.models.TokenDocumento;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class TokenService {

	@Value("${environments.urlTemporaria.secret}")
	private String secret;

	public String geradorDeTokenTemporario(Map<String, Object> claims) {

		return Jwts.builder().addClaims(claims).signWith(getKey(), SignatureAlgorithm.HS512).compact();
	}

	public TokenDocumento recuperarInformacoesDoToken(String token)
			throws ExpiredJwtException, MalformedJwtException, SignatureException, JwtException {

		TokenDocumento td = new TokenDocumento();
		Claims claim = validarToken(token);
		td.setDataCriacao(claim.getIssuedAt());
		td.setDataExpiracao(claim.getExpiration());
		td.setIdDocumento(String.class.cast(claim.get(EnumClaimsAdicionais.ID_DOC_GOOGLE.getDescricao())));
		td.setIdUsuario(claim.getSubject());
		td.setNumeroProcesso(String.class.cast(claim.get(EnumClaimsAdicionais.NR_PROCESSO.getDescricao())));
		td.setEmissor(claim.getIssuer());

		return td;
	}

	public Claims validarToken(String token, SecretKey key)
			throws ExpiredJwtException, MalformedJwtException, SignatureException, JwtException {
		Claims informacaoToken = null;
		informacaoToken = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		return informacaoToken;

	}

	private Claims validarToken(String token)
			throws ExpiredJwtException, MalformedJwtException, SignatureException, JwtException {
		return validarToken(token, getKey());
	}

	private SecretKey getKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	public SecretKey getKey(String secret) {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	public Claims validarToken(String token, String secret) {
		return validarToken(token, getKey(secret));
	}
}
