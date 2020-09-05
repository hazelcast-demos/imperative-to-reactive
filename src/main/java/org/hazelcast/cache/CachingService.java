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
        return Mono.fromCompletionStage(() -> cache.getAsync(id))
            .switchIfEmpty(repository
                    .findById(id)
                    .doOnNext(p -> cache.putAsync(p.getId(), p))
            );
    }

    Flux<Person> findAll(Sort sort) {
        return repository
                .findAll(sort)
                .doOnNext(p -> cache.putAsync(p.getId(), p));
    }
}