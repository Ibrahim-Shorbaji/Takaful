package com.takaful.foodshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TakafulApplication {

	public static void main(String[] args) {
		SpringApplication.run(TakafulApplication.class, args);
	}

}
