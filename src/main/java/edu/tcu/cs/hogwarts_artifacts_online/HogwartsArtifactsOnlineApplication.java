package edu.tcu.cs.hogwarts_artifacts_online;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.tcu.cs.hogwarts_artifacts_online.artifact.utils.IdWorker;

@SpringBootApplication
public class HogwartsArtifactsOnlineApplication {

	public static void main(String[] args) {
		SpringApplication.run(HogwartsArtifactsOnlineApplication.class, args);
	}
	/*
	 * Dans la classe principale de l'application Spring Boot, une méthode est
	 * définie pour créer une instance de IdWorker.
	 * Cette méthode est annotée avec @Bean. Cette annotation indique à Spring que
	 * l'objet retourné par cette méthode
	 * (une instance de IdWorker) doit être enregistré et géré comme un bean dans le
	 * contexte de l'application Spring.
	 * Lors du démarrage de l'application Spring Boot, cette méthode annotée
	 * avec @Bean est exécutée.
	 * La méthode crée une nouvelle instance de IdWorker en passant un workerID et
	 * un dataCenterID à son constructeur.
	 * L'instance de IdWorker ainsi créée est ensuite gérée par Spring
	 */

	@Bean
	public IdWorker idWorker() {
		return new IdWorker(1, 1);
	}

}
