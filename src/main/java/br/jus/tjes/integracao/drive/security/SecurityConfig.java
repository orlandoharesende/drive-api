package br.jus.tjes.integracao.drive.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private TokenAuthenticationFilter tokenAuthenticationFilter;
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests) -> requests //
				.requestMatchers(antMatchers("api/download/tmp/**")).permitAll() //
				.requestMatchers(antMatchers("/swagger-ui/**", "/javainuse-openapi/**", "/v3/api-docs/**")).permitAll() //
				.requestMatchers(antMatchers("/health/**")).permitAll() //
				.requestMatchers(antMatchers("api/gerar-link/**")).authenticated()
				.anyRequest().authenticated())
		.csrf(AbstractHttpConfigurer::disable)
		.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	private AntPathRequestMatcher[] antMatchers(String... antMatchers) {
		AntPathRequestMatcher[] array = new AntPathRequestMatcher[antMatchers.length];
		Arrays.stream(antMatchers).map(p -> new AntPathRequestMatcher(p)).toList().toArray(array);
		return array;
	}
}