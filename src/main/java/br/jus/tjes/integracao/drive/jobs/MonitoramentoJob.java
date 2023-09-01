package br.jus.tjes.integracao.drive.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.jus.tjes.integracao.drive.service.TesteService;


@Component
public class MonitoramentoJob {
	private static final Logger logger = LoggerFactory.getLogger(MonitoramentoJob.class);

	@Autowired
	private TesteService service;
	
	@Scheduled(cron = "${environments.cronJobMonitoramento}")
	public void executar() {
		logger.info("Executando MonitoramentoJob.");
		service.processar();
		logger.info("Fim MonitoramentoJob.");
	}
}
