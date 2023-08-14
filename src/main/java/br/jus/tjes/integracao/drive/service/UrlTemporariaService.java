package br.jus.tjes.integracao.drive.service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.jus.tjes.integracao.drive.dto.DocumentoDTO;
import br.jus.tjes.integracao.drive.enums.EnumClaimsAdicionais;
import br.jus.tjes.integracao.drive.models.TokenDocumento;
import br.jus.tjes.integracao.drive.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;

@Service
public class UrlTemporariaService {
	
	/*
	 * public static final String NR_PROCESSO = "nrprocesso";
	 * 
	 * public static final String ID_DOC_GOOGLE = "iddoc";
	 */
	@Autowired
	private TokenService tokenService;
	
	@Value("${environments.urlTemporaria.tempoExpiracaoEmSegundos}")
	private Integer expiracao;
	
	public TokenDocumento validarUrl(String token,String emissor) {

		TokenDocumento tokenDoc = tokenService.recuperarInformacoesDoToken(token);
		long now = System.currentTimeMillis();
		Date data = new Date(now);
		 if(!tokenDoc.getDataExpiracao().after(data) ||  !emissor.equals(tokenDoc.getEmissor()))
		 {
			 //Lançar excecao
			 
			 return null;
		 }
		 return tokenDoc;
	}
	
	
	
	public String getUrlTemporaria(DocumentoDTO documento, String baseUrl) {
		long now = System.currentTimeMillis();
		Date dataAtual = new Date(now);
		Date dataExpiracao = new Date(now + expiracao * 1000);
		Map<String, Object> claims = new HashMap<>();
		claims.put(Claims.SUBJECT, documento.getIdUser());
		claims.put(Claims.ISSUED_AT, dataAtual);
		claims.put(Claims.ISSUER, baseUrl);
		claims.put(Claims.EXPIRATION, dataExpiracao);
		claims.put(EnumClaimsAdicionais.ID_DOC_GOOGLE.getDescricao(), documento.getIdDoc());
		claims.put(EnumClaimsAdicionais.NR_PROCESSO.getDescricao(), documento.getNumeroProcesso());

		String token = tokenService.geradorDeTokenTemporario(claims);
		return baseUrl.concat("/download/tmp?").concat("id=").concat(token);
	}


	
	
	

}
