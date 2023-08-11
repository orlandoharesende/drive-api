package br.jus.tjes.integracao.drive.service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.jus.tjes.integracao.drive.dto.DocumentoDTO;
import br.jus.tjes.integracao.drive.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;

@Service
public class UrlTemporarioService {
	
	
	
	
	public Boolean validarToken(String token,String emissor) {

		return JwtTokenUtil.validarToken(token, emissor);

	}
	
	public String getUrlTemporaria(DocumentoDTO documento,String baseUrl) {
		Map<String, String> mapa = new HashMap<>();
		mapa.put("id-doc", documento.getIdDoc());
		mapa.put("nr_processo", documento.getNumeroProcesso());		
		String token = JwtTokenUtil.geradorDeTokenTemporario(documento.getIdUSer(),mapa,baseUrl);   
		return  baseUrl.concat("/download/tmp?").concat("id=").concat(token);
	}

	public String extrairNrProcesso(String token) {
		// TODO Auto-generated method stub
		return  JwtTokenUtil.recuperarNrProcesso(token);
	}

	public String extrairIdDocumentoGoogle(String token) {
		// TODO Auto-generated method stub
		return JwtTokenUtil.recuperarIdDocumento(token);
	}
	
	

}
