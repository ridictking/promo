package com.ng.emts.christmasOffer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChristmasOfferApplication {


	public static void main(String[] args) {
		SpringApplication.run(ChristmasOfferApplication.class, args);
	}

}
