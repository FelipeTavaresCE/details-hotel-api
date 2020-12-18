package br.com.cvc.consulta.dados.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ConsultaDadosHotelApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsultaDadosHotelApiApplication.class, args);
	}

}
