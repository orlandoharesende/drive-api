package br.jus.tjes.integracao.drive.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.jus.tjes.integracao.drive.dto.ArquivoDTO;
import br.jus.tjes.integracao.drive.dto.DocumentoDTO;
import br.jus.tjes.integracao.drive.enums.EnumClaimsAdicionais;
import br.jus.tjes.integracao.drive.enums.EnumMensagemLog;
import br.jus.tjes.integracao.drive.exception.UrlInvalidaException;
import br.jus.tjes.integracao.drive.models.TokenDocumento;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

@Service
public class UrlTemporariaService {
	
	
	@Autowired
	private TokenService tokenService;
	@Autowired
	private DriveApiService driveService;
	
	@Value("${environments.urlTemporaria.tempoExpiracaoEmSegundos}")
	private Integer expiracao;
	
	private static final Logger LOG = LoggerFactory.getLogger(UrlTemporariaService.class);

	
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
		Object[] valoresLog = new Object[] {documento.getIdUser(),documento.getIdDoc(),documento.getNumeroProcesso(),dataAtual};
		String msgLog = String.format( EnumMensagemLog.LOG_SOLICITACAO_URL.getMensagem(), valoresLog);
		LOG.info( msgLog);
		String token = tokenService.geradorDeTokenTemporario(claims);
	
		return baseUrl.concat("/download/tmp?").concat("id=").concat(token);
	}
	
	public TokenDocumento getArquivoRemoto(String token, String emissor) throws UrlInvalidaException, ExpiredJwtException, MalformedJwtException, SignatureException, JwtException {
		
		Date data = Calendar.getInstance().getTime();
		Object[] valoresLog = new Object[] {token,data};
		String msgLog = String.format( EnumMensagemLog.LOG_TOKEN.getMensagem(),valoresLog);
		LOG.info(msgLog);
		TokenDocumento tokenDoc = tokenService.recuperarInformacoesDoToken(token);		
		valoresLog = new Object[] {tokenDoc.getIdUsuario(),tokenDoc.getIdDocumento(),tokenDoc.getNumeroProcesso(),data};
		msgLog = String.format( EnumMensagemLog.LOG_DOWNLOAD_ARQ_TMP.getMensagem(), valoresLog);
		LOG.info(msgLog);
		validar(tokenDoc, emissor);
		ArquivoDTO arquivoDTO = driveService.getArquivo(tokenDoc.getNumeroProcesso(), tokenDoc.getIdDocumento());
		byte[] arquivo = driveService.getArquivoEmBytes(tokenDoc.getNumeroProcesso(), tokenDoc.getIdDocumento());
		tokenDoc.setArquivo(arquivoDTO);
		tokenDoc.setArquivoPdf(arquivo);
		
		return tokenDoc;
		
	}
	
	
	private void validar(TokenDocumento tokenDoc, String emissor) throws UrlInvalidaException {

		long now = System.currentTimeMillis();
		Date data = new Date(now);
		if (!tokenDoc.getDataExpiracao().after(data) || !emissor.equals(tokenDoc.getEmissor())) {

			throw new UrlInvalidaException();
		}

		return;
	}



	
	
	

}
