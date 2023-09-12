package br.jus.tjes.integracao.drive.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

	protected AuthenticationFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		String text = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
		System.out.println(text);
		Authentication authentication = new TestingAuthenticationToken("user", "password", "ROLE_USER");
		return getAuthenticationManager().authenticate(authentication);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain, Authentication auth) throws IOException, ServletException {
		log.info("Autenticação com sucesso!!!");
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
	}

}
