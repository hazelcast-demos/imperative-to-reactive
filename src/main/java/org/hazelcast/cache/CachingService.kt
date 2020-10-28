package org.hazelcast.cache

import com.hazelcast.map.IMap
import org.springframework.data.domain.Sort
import reactor.core.publisher.Mono


class CachingService(private val cache: IMap<Long, Person>, private val repository: PersonRepository) {

    fun findById(id: Long): Mono<Person> {
        val person = cache[id]
        return if (person == null) {
            println("Person with id $id not found in cache")
            val mono = repository.findById(id)
            mono.doOnSuccess {
                cache[id] = it
                println("Person with id $id put in cache")
            }
        } else {
            println("Person with id $id found in cache")
            Mono.just(person)
        }
    }

    fun findAll(sort: Sort) = repository
        .findAll(sort)
        .doOnNext { cache[it.id] = it }
}
