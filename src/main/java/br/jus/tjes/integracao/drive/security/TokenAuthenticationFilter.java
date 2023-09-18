package br.jus.tjes.integracao.drive.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
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
	private static final Logger LOG = LoggerFactory.getLogger(TokenAuthenticationFilter.class);
	protected static List<String> emissoresAutorizados = Arrays.asList("pje-tjes-1g", "pje-tjes-2g");
	private Set<AntPathRequestMatcher> matchersUrlsPublicas = new HashSet<>();
	private Set<AntPathRequestMatcher> matchersUrlsAutenticadas = new HashSet<>();

	@Value("${environments.auth.secret}")
	private String secret;
	@Autowired
	private TokenService tokenService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			if (isAuthenticatedUrl(httpServletRequest)) {
				processarAutenticacao(httpServletRequest);
				filterChain.doFilter(request, response);
			} else {
				filterChain.doFilter(request, response);
			}
		} catch (Exception e) {
			LOG.error("Erro ao realizar autenticação: " + e.getMessage());
			configurarRespostaNaoAutorizado(response);
		}
	}

	private void processarAutenticacao(HttpServletRequest httpServletRequest) throws IOException, ServletException {
		String token = extractToken(httpServletRequest);
		if (isValidToken(token)) {
			configurarAutenticacaoComSucesso(token);
		} else {
			throw new RuntimeException("Token inválido.");
		}
	}

	private void configurarAutenticacaoComSucesso(String token) {
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
		return emissoresAutorizados.contains(emissor.toLowerCase());
	}

	private String tokenSemPrefixo(String token) {
		return token.replace("Bearer", "").trim();
	}

	private List<GrantedAuthority> getAuthoritiesFromToken(String token) {
		return Collections.emptyList();
	}

	private boolean isAuthenticatedUrl(HttpServletRequest request) {
		return !getMatchersUrlsPublicas().stream().filter(m -> m.matches(request)).findAny().isPresent();
	}

	public Set<AntPathRequestMatcher> getMatchersUrlsPublicas() {
		return matchersUrlsPublicas;
	}

	public void setMatchersUrlsPublicas(Set<AntPathRequestMatcher> matchersUrlsPublicas) {
		this.matchersUrlsPublicas = matchersUrlsPublicas;
	}

	public Set<AntPathRequestMatcher> getMatchersUrlsAutenticadas() {
		return matchersUrlsAutenticadas;
	}

	public void setMatchersUrlsAutenticadas(Set<AntPathRequestMatcher> matchersUrlsAutenticadas) {
		this.matchersUrlsAutenticadas = matchersUrlsAutenticadas;
	}

}