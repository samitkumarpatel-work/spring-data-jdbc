package com.example.springdatajdbc.router;

import com.example.springdatajdbc.jdbc.JdbcRepository;
import com.example.springdatajdbc.model.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController(value = "/customer")
@RequiredArgsConstructor
public class Routers {
    final JdbcRepository jdbcRepository;

    @GetMapping
    public ResponseEntity<List<Customer>> all() {
        return
                ResponseEntity.ok(jdbcRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> byId(@PathVariable(value = "id") String id) {
        var customerId = Long.parseLong(id);
        return
                ResponseEntity.ok(jdbcRepository.findById(customerId));
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody Customer customer) {
        return ResponseEntity.ok(jdbcRepository
                .insert(customer));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Integer> update(@PathVariable("id") String id, @RequestBody Customer customer) {
        var idToBeUpdate = Long.parseLong(id);
        return ResponseEntity.ok(jdbcRepository.update(idToBeUpdate, customer));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Integer> delete(@PathVariable("id") String id) {
        var idToBeDelete = Long.parseLong(id);
        return ResponseEntity.ok(jdbcRepository.delete(idToBeDelete));
    }

}
