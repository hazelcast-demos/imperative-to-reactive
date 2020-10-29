package org.hazelcast.cache

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Sort
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.notFound
import org.springframework.web.reactive.function.server.ServerResponse.ok
import java.io.Serializable
import java.time.LocalDate


@Configuration
class PersonRoutes {

    @Bean
    fun router(service: CachingService) = coRouter {
        val handler = PersonHandler(service)
        GET("/person", handler::getAll)
        GET("/person/{id}", handler::getOne)
    }
}

class PersonHandler(private val service: CachingService) {

    suspend fun getAll(req: ServerRequest): ServerResponse {
        val flow = service.findAll(Sort.by("lastName", "firstName"))
        return ok().bodyAndAwait(flow)
    }

    suspend fun getOne(req: ServerRequest): ServerResponse {
        val person = service.findById(req.pathVariable("id").toLong())
        return if (person == null) notFound().buildAndAwait()
        else ok().bodyValueAndAwait(person)
    }
}

interface PersonRepository : CoroutineSortingRepository<Person, Long>

class Person(
    @Id
    val id: Long,
    var firstName: String,
    var lastName: String,
    var birthdate: LocalDate? = null
) : Serializable