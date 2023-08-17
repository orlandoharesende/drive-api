package br.jus.tjes.integracao.drive.service;

import java.util.Map;

import javax.crypto.SecretKey;

import org.apache.tomcat.util.buf.UriUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.jus.tjes.integracao.drive.enums.EnumClaimsAdicionais;
import br.jus.tjes.integracao.drive.models.TokenDocumento;
import br.jus.tjes.integracao.drive.security.GoogleAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtHandler;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
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

	private Claims validarToken(String token)
			throws ExpiredJwtException, MalformedJwtException, SignatureException, JwtException {

		Claims informacaoToken = null;
		informacaoToken = Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();

		return informacaoToken;

	}

	private SecretKey getKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

	}

}
