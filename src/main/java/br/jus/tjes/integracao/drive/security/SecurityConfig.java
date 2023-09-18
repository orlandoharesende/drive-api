package br.jus.tjes.integracao.drive.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private ApplicationContext applicationContext;
	private List<AntPathRequestMatcher> matchersUrlsPublicas = Arrays.asList( //
			antMatcher("/api/download/**"), //
			antMatcher("/swagger-ui/**"), //
			antMatcher("/javainuse-openapi/**"), //
			antMatcher("/v3/api-docs/**") //
	);
	private List<AntPathRequestMatcher> matchersUrlsAutenticadas = Arrays.asList(antMatcher("/api/gerar-link/**"));

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		TokenAuthenticationFilter tokenAuthenticationFilter = applicationContext
				.getBean(TokenAuthenticationFilter.class);
		tokenAuthenticationFilter.getMatchersUrlsPublicas().addAll(matchersUrlsPublicas);
		tokenAuthenticationFilter.getMatchersUrlsAutenticadas().addAll(matchersUrlsAutenticadas);

		http.authorizeHttpRequests((requests) -> requests //
				.requestMatchers(toArray(matchersUrlsPublicas)).permitAll() //
				.requestMatchers(toArray(matchersUrlsAutenticadas)).authenticated() //
				.anyRequest().authenticated()).csrf(AbstractHttpConfigurer::disable)
				.addFilterAfter(tokenAuthenticationFilter, BasicAuthenticationFilter.class);
		return http.build();
	}

	protected AntPathRequestMatcher[] antMatchers(String... antMatchers) {
		AntPathRequestMatcher[] array = new AntPathRequestMatcher[antMatchers.length];
		Arrays.stream(antMatchers).map(p -> new AntPathRequestMatcher(p)).toList().toArray(array);
		return array;
	}

	private AntPathRequestMatcher antMatcher(String antMatcher) {
		return new AntPathRequestMatcher(antMatcher);
	}

	private AntPathRequestMatcher[] toArray(List<AntPathRequestMatcher> matchersUrlsPublicas) {
		AntPathRequestMatcher[] array = new AntPathRequestMatcher[matchersUrlsPublicas.size()];
		matchersUrlsPublicas.stream().map(p -> p).toList().toArray(array);
		return array;
	}
}