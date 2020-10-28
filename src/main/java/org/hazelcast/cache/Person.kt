package org.hazelcast.cache

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Sort
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import java.io.Serializable
import java.time.LocalDate


@Configuration
class PersonRoutes {

    @Bean
    fun router(service: CachingService) = router {
        val handler = PersonHandler(service)
        GET("/person", handler::getAll)
        GET("/person/{id}", handler::getOne)
    }
}

class PersonHandler(private val service: CachingService) {

    fun getAll(req: ServerRequest): Mono<ServerResponse> {
        val flux = service.findAll(Sort.by("lastName", "firstName"))
        return ok().body(flux)
    }

    fun getOne(req: ServerRequest): Mono<ServerResponse> {
        val mono = service
            .findById(req.pathVariable("id").toLong())
            .switchIfEmpty(Mono.error { ResponseStatusException(NOT_FOUND) })
        return ok().body(mono)
    }
}

interface PersonRepository : ReactiveSortingRepository<Person, Long>

class Person (
    @Id
    val id: Long,
    var firstName: String,
    var lastName: String,
    var birthdate: LocalDate? = null
) : Serializable