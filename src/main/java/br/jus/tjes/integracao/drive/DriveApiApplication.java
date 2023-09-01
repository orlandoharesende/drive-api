package br.jus.tjes.integracao.drive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DriveApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriveApiApplication.class, args);
	}

}
