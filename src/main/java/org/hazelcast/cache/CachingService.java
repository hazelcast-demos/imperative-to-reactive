package org.hazelcast.cache;

import com.hazelcast.map.IMap;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CachingService {

    private final IMap<Long, Person> cache;
    private final PersonRepository repository;

    public CachingService(IMap<Long, Person> cache, PersonRepository repository) {
        this.repository = repository;
        this.cache = cache;
    }

    Mono<Person> findById(Long id) {
        var person = cache.get(id);
        if (person == null) {
            System.out.println("Person with id " + id + " not found in cache");
            var mono = repository.findById(id);
            return mono.doOnSuccess(p -> {
                cache.put(id, p);
                System.out.println("Person with id " + id + " put in cache");
            });
        } else {
            System.out.println("Person with id " + id + " found in cache");
            return Mono.just(person);
        }
    }

    Flux<Person> findAll(Sort sort) {
        return repository
                .findAll(sort)
                .doOnNext(p -> cache.put(p.getId(), p));
    }
}