package com.example.springdatajdbc.jdbc;

import com.example.springdatajdbc.model.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JdbcRepository {
    final JdbcTemplate jdbcTemplate;

    public Customer findById(long id) {
        log.info("findById {}", id);
        return
                jdbcTemplate
                        .queryForStream(
                                "SELECT * FROM customers WHERE id = ?",
                                ps -> {
                                    ps.setLong(1, id);
                                },
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
                        (ps) -> {
                            ps.setString(1, firstName);
                        },
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
                        (ps) -> {
                            ps.setString(1, lastName);
                        },
                        (rs, rowNum) -> new Customer(
                                rs.getLong("id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"))
                )
                .collect(Collectors.toList());
    }

    public int update(long id, Customer customer) {
        log.info("update {}", id);
        return jdbcTemplate
                .update("UPDATE customers SET first_name=? , last_name=? WHERE id=?", ps -> {
                    ps.setString(1, customer.firstName());
                    ps.setString(2, customer.lastName());
                    ps.setLong(3, id);
                });
    }

    public int delete(long id) {
        log.info("delete {}", id);
        return jdbcTemplate
                .update("DELETE FROM customers WHERE id = ?", ps -> {
                    ps.setLong(1, id);
                });
    }

    public int insert(Customer customer) {
        log.info("insert {}", customer);
        return jdbcTemplate
                .update("INSERT INTO customers(first_name, last_name) VALUES (?,?)", ps -> {
                    ps.setString(1, customer.firstName());
                    ps.setString(2, customer.lastName());
                });
    }

    public List<Customer> findAll() {
        return jdbcTemplate
                .queryForStream(
                        "SELECT * FROM customers",
                        (rs, rowNo) -> new Customer(
                                rs.getLong("id"),
                                rs.getString("first_name"),
                                rs.getString("last_name"))
                )
                .collect(Collectors.toList());
    }

    public int addWithPgsqlFunction(int a, int b) {
        return
                jdbcTemplate.query(
                        "SELECT * FROM add(?,?)",
                        (ps) -> {
                            ps.setInt(1, a);
                            ps.setInt(2, b);
                        },
                        (rs) -> {
                            return rs.next() ? rs.getObject(1, Integer.class) : 0;
                        });
    }

    public List<Customer> findCustomerByLastNameLength(int length) {
        return jdbcTemplate
                .queryForStream(
                        "SELECT * FROM get_records_by_last_name_length(?)",
                        (ps) -> {
                            ps.setInt(1, length);
                        },
                        (rs, rowNum) -> new Customer(
                                rs.getLong("id"),
                                rs.getString("first_name"),
                                rs.getString("last_name")
                        )
                )
                .collect(Collectors.toList());
    }
}
