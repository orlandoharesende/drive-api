package br.jus.tjes.integracao.drive.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

import br.jus.tjes.integracao.drive.exception.UrlJaUtilizadaException;
import br.jus.tjes.integracao.drive.util.ExpiringCache;
import jakarta.annotation.PostConstruct;

@Component
@ApplicationScope
@Configuration
public class ControleUtilizacaoUrlDownload {
	@Value("${environments.urlTemporaria.tempoExpiracaoEmSegundos}")
	private Integer tempoExpiracaoEmSegundos;
	@Value("${environments.urlTemporaria.qtdUtilizacaoPermitida}")
	private Integer qtdUtilizacaoPermitida;
	private ExpiringCache<String, Integer> cache;
	private Integer tempoExpiracaoCache;

	@PostConstruct
	public void init() {
		// Colocamos como padrão o tempo de expiriação do cache como 2 vezes o tempo de
		// expiração, para colocar uma gordura.
		this.tempoExpiracaoCache = tempoExpiracaoEmSegundos * 2;
		this.cache = new ExpiringCache<>(tempoExpiracaoCache);
	}

	public void registrarUtilizacao(String url) {
		Integer qtdUtilizacao = cache.get(url);
		if (qtdUtilizacao == null) {
			cache.put(url, 1);
			return;
		}
		if (qtdUtilizacao >= qtdUtilizacaoPermitida) {
			throw new UrlJaUtilizadaException();
		} else {
			cache.put(url, ++qtdUtilizacao);
		}
	}

}
