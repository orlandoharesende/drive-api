package br.jus.tjes.integracao.drive.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import br.jus.tjes.integracao.drive.service.TokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenAuthenticationFilter extends GenericFilterBean {
	@Value("${environments.auth.secret}")
	private String secret;
	@Autowired
	private TokenService tokenService;
	protected static List<String> emissoresAutorizados = Arrays.asList("pje-tjes-1g", "pje-tjes-2g");

	private static final Logger LOG = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			String token = extractToken((HttpServletRequest) request);
			if (isValidToken(token)) {
				processarAutenticacao(token);
				filterChain.doFilter(request, response);
			} else {
				LOG.error("Erro ao realizar autenticação.");
				configurarRespostaNaoAutorizado(response);
			}
		} catch (Exception e) {
			LOG.error("Erro ao realizar autenticação: " + e.getMessage());
			configurarRespostaNaoAutorizado(response);
		}
	}

	private void processarAutenticacao(String token) {
		CustomAuthToken authToken = new CustomAuthToken(token);
		List<GrantedAuthority> authorities = getAuthoritiesFromToken(token);
		Authentication authentication = new UsernamePasswordAuthenticationToken(authToken, null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private void configurarRespostaNaoAutorizado(ServletResponse response) {
		((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
	}

	private String extractToken(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}

	private boolean isValidToken(String token) {
		if (token == null) {
			return false;
		}
		Claims claims = tokenService.validarToken(tokenSemPrefixo(token), this.secret);
		return isEmissorAutorizado(claims);
	}

	private boolean isEmissorAutorizado(Claims claims) {
		String emissor = claims.getIssuer();
		if (emissor == null) {
			return false;
		}
		return emissoresAutorizados.contains(emissor.toUpperCase());
	}

	private String tokenSemPrefixo(String token) {
		return token.replace("Bearer", "").trim();
	}

	private List<GrantedAuthority> getAuthoritiesFromToken(String token) {
		return Collections.emptyList();
	}
}