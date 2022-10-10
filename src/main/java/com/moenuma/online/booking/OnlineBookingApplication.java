package com.moenuma.online.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class OnlineBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineBookingApplication.class, args);
	}

}
