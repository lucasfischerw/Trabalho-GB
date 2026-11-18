package br.com.dev.missao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MissaoServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MissaoServiceApplication.class, args);
	}
}