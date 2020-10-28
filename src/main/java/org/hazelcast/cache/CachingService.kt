package org.hazelcast.cache

import com.hazelcast.map.IMap
import org.springframework.data.domain.Sort
import reactor.core.publisher.Mono
import reactor.util.Loggers


class CachingService(private val cache: IMap<Long, Person>, private val repository: PersonRepository) {

    private val logger = Loggers.getLogger(CachingService::class.java)

    fun findById(id: Long) = Mono.fromCompletionStage { cache.getAsync(id) }
        .doOnNext { logger.info("Person with id $id found in cache") }
        .switchIfEmpty(
            repository
                .findById(id)
                .doOnNext {
                    cache.putAsync(it.id, it)
                    logger.info("Person with id $id set in cache")
                }
        )

    fun findAll(sort: Sort) = repository
        .findAll(sort)
        .doOnNext { cache.putAsync(it.id, it) }
}
