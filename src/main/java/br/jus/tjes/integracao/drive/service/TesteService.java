package br.jus.tjes.integracao.drive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.jus.tjes.integracao.drive.dto.ModeloDTO;
import br.jus.tjes.integracao.drive.jobs.MonitoramentoJob;

@Service
public class TesteService {
	private static final Logger logger = LoggerFactory.getLogger(MonitoramentoJob.class);

	public void processar() {
		logger.info("Início processamento.");
		logger.info("Fim processamento.");
	}

	public ModeloDTO consultarModelo(String id) {
		ModeloDTO modelo = new ModeloDTO();
		modelo.setId(id);
		modelo.setNome("Teste Funcionou");
		return modelo;
	}
}
