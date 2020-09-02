package org.hazelcast.cache;

import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface PersonRepository extends ReactiveSortingRepository<Person, Long> {
}