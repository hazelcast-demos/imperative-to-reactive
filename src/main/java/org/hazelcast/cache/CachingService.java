package org.hazelcast.cache;

import com.hazelcast.map.IMap;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

public class CachingService {

    private static final Logger LOGGER = Loggers.getLogger(CachingService.class);

    private final IMap<Long, Person> cache;
    private final PersonRepository repository;

    public CachingService(IMap<Long, Person> cache, PersonRepository repository) {
        this.repository = repository;
        this.cache = cache;
    }

    Mono<Person> findById(Long id) {
        return Mono.fromCompletionStage(() -> cache.getAsync(id))
                .doOnNext(p -> LOGGER.info("Person with id " + p.getId() + " found in cache"))
                .switchIfEmpty(repository
                        .findById(id)
                        .doOnNext(p -> {
                            cache.putAsync(p.getId(), p);
                            LOGGER.info("Person with id " + p.getId() + " set in cache");
                        })
                );
    }

    Flux<Person> findAll(Sort sort) {
        return repository
                .findAll(sort)
                .doOnNext(p -> cache.putAsync(p.getId(), p));
    }
}