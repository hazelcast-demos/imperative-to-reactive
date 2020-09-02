package org.hazelcast.cache;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class PersonController {

    private final PersonRepository repository;

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/person")
    public List<Person> getAll() {
        return repository.findAll(Sort.by("lastName", "firstName"));
    }

    @GetMapping("/person/{id}")
    public Optional<Person> getOne(@PathVariable Long id) {
        return repository.findById(id);
    }
}