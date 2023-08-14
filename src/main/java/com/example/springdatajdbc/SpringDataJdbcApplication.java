package com.example.springdatajdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class SpringDataJdbcApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringDataJdbcApplication.class, args);
	}

	final JdbcRepository jdbcRepository;

	// This Bean invokes when the application is ready
	@Bean
	ApplicationListener<ApplicationStartedEvent> start() {
		return event -> {

			log.info("CRUD OPERATION");

			// Table
			jdbcRepository.dropTable();
			jdbcRepository.createTable();

			// Insert
			Arrays
					.asList("John Doe", "Billu Barber", "Karg Dominic", "Happy More")
					.stream()
					.map(name -> name.split(" "))
					.map(strings -> new Customer(0l, strings[0], strings[1]))
					.map(jdbcRepository::insert)
					.collect(Collectors.toList())
					.forEach(System.out::println);

			// Find
			jdbcRepository
					.findByFirstName("John")
					.forEach(System.out::println);

			// Find
			jdbcRepository
					.findByLastName("Doe")
					.forEach(System.out::println);

			// Find
			var customer = jdbcRepository
					.findById(1l);
			System.out.println(customer);

		};
	}
}

record Customer(long id, String firstName, String lastName) {}
@Component
@Slf4j
@RequiredArgsConstructor
class JdbcRepository {
	final JdbcTemplate jdbcTemplate;

	public void createTable() {
		log.info("createTable");
		jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS customers(id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");
	}
	public void dropTable() {
		log.info("dropTable");
		jdbcTemplate.execute("DROP TABLE IF EXISTS customers");
	}

	public void createFunction() {

	}

	public void createStoreProcedures() {

	}

	public void createViews() {

	}

	public Customer findById(long id) {
		log.info("findById {}", id);
		return
				jdbcTemplate
						.queryForStream(
								"SELECT * FROM customers WHERE id = ?",
								ps -> { ps.setLong(1,id);},
								(rs, rowNum) -> new Customer(
										rs.getLong("id"),
										rs.getString("first_name"),
										rs.getString("last_name"))
						)
						.findFirst()
						.orElseThrow(() -> new RuntimeException("Customer having id %s not found".formatted(id)));
	}

	public List<Customer> findByFirstName(String firstName) {
		log.info("findByFirstName {}", firstName);
		return jdbcTemplate
				.queryForStream(
						"SELECT id, first_name, last_name FROM customers WHERE first_name = ?",
						(ps) -> { ps.setString(1,firstName);},
						(rs, rowNum) -> new Customer(
								rs.getLong("id"),
								rs.getString("first_name"),
								rs.getString("last_name"))
				)
				.collect(Collectors.toList());
	}

	public List<Customer> findByLastName(String lastName) {
		log.info("findByLastName {}", lastName);
		return jdbcTemplate
				.queryForStream(
						"SELECT * FROM customers WHERE last_name = ?",
						(ps) -> { ps.setString(1, lastName);},
						(rs, rowNum) -> new Customer(
								rs.getLong("id"),
								rs.getString("first_name"),
								rs.getString("last_name"))
				)
				.collect(Collectors.toList());
	}

	public void update(long id, Customer customer) {

	}

	private void delete(long id) {

	}

	public int insert(Customer customer) {
		log.info("insert {}", customer);
		// In jdbcTemplate there is no insert method from spring data.
		return jdbcTemplate
				.update("INSERT INTO customers(first_name, last_name) VALUES (?,?)", ps -> {
					ps.setString(1, customer.firstName());
					ps.setString(2,customer.lastName());
				});
	}
}