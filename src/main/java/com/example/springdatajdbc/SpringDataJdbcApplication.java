package com.example.springdatajdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class SpringDataJdbcApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringDataJdbcApplication.class, args);
	}


	@Bean
	ApplicationListener<ApplicationStartedEvent> start() {
		return event -> {
			//do some CRUD operation
		};
	}
}

