package com.example.springdatajdbc;

import com.example.springdatajdbc.jdbc.JdbcRepository;
import com.example.springdatajdbc.model.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
class SpringDataJdbcApplicationTests {

	@Autowired
	JdbcRepository jdbcRepository;

	@Container
	@ServiceConnection
	static PostgreSQLContainer PGSQL = new PostgreSQLContainer<>("postgres:latest")
			// mount this file so that we have the table before we do any crud operation.
			.withCopyFileToContainer(MountableFile.forClasspathResource("db/schema.sql"), "/docker-entrypoint-initdb.d/schema.sql")
			.waitingFor(Wait.forListeningPort());
	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("Create/Read/Update/Delete")
	void crudTest() {

		assertAll("Create/Read/Update/Delete",
			() -> {
				// Create
				var noOfInsert = Arrays
						.asList("John Doe", "Billu Barber", "Karg Dominic", "Happy More", "Killy Paul")
						.stream()
						.map(name -> name.split(" "))
						.map(strings -> new Customer(0l, strings[0], strings[1]))
						.map(jdbcRepository::insert)
						.collect(Collectors.toList());
				assertEquals(5, noOfInsert.size());
			},
			() -> {
				//pgsql get_records_by_last_name_length(4) function test
				var listOfCustomers = jdbcRepository.findCustomerByLastNameLength(4);
				assertEquals(2, listOfCustomers.size());
				assertEquals(new Customer(4, "Happy","More"), listOfCustomers.get(0));
				assertEquals(new Customer(5, "Killy","Paul"), listOfCustomers.get(1));
			},
			() -> {
				//find
				assertAll("find",
						() -> {
							var customer = jdbcRepository.findByFirstName("John");
							assertEquals(1, customer.size());
							assertEquals("John", customer.get(0).firstName());
							assertEquals("Doe", customer.get(0).lastName());
						},
						() -> {
							var customer = jdbcRepository.findByLastName("Doe");
							assertEquals(1, customer.size());
							assertEquals("John", customer.get(0).firstName());
							assertEquals("Doe", customer.get(0).lastName());
						},
						() -> {
							var customer = jdbcRepository.findById(1l);
							assertEquals("John", customer.firstName());
							assertEquals("Doe", customer.lastName());
						}
				);
			},
			() -> {
				//Update
				var updateSignal = jdbcRepository
						.update(1l,new Customer(0l, "JohnX", "Doe"));

				assertEquals(1, updateSignal);
				assertAll("find",
						() -> {
							var customer = jdbcRepository.findByFirstName("JohnX");
							assertEquals(1, customer.size());
							assertEquals("JohnX", customer.get(0).firstName());
							assertEquals("Doe", customer.get(0).lastName());
						},
						() -> {
							var customer = jdbcRepository.findByLastName("Doe");
							assertEquals(1, customer.size());
							assertEquals("JohnX", customer.get(0).firstName());
							assertEquals("Doe", customer.get(0).lastName());
						}
				);
			},
			() -> {
				//delete
				var deleteSignal = jdbcRepository.delete(1l);
				assertEquals(1, deleteSignal);
				assertEquals(4, jdbcRepository.findAll().size());
			}
		);
	}

	@Test
	@DisplayName("pgsql add(1,1) function test")
	void pgsqlFunctionTest() {
		assertEquals(2, jdbcRepository.addWithPgsqlFunction(1,1));
	}

}
