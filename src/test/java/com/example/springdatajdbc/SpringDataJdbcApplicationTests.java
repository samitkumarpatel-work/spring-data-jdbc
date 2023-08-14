package com.example.springdatajdbc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
class SpringDataJdbcApplicationTests {

//	@Container
//	static PostgreSQLContainer PGSQL = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"));
//
//	@DynamicPropertySource
//	static void postgresqlProperties(DynamicPropertyRegistry registry) {
//		registry.add("spring.datasource.url", PGSQL::getJdbcUrl);
//		registry.add("spring.datasource.driver-class-name", PGSQL::getDriverClassName);
//		registry.add("spring.datasource.username", PGSQL::getUsername);
//		registry.add("spring.datasource.password", PGSQL::getPassword);
//	}

	@Test
	void contextLoads() {
	}

}
