package org.hazelcast.cache

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.router
import java.time.LocalDate
import javax.persistence.*


@Configuration
class PersonRoutes {

    @Bean
    fun router(repository: PersonRepository) = router {
        val handler = PersonHandler(repository)
        GET("/person", handler::getAll)
        GET("/person/{id}", handler::getOne)
    }
}

class PersonHandler(private val repository: PersonRepository) {
    fun getAll(req: ServerRequest) = ok().bodyValue(repository.findAll(Sort.by("lastName", "firstName")))
    fun getOne(req: ServerRequest) = ok().bodyValue(repository.findById(req.pathVariable("id").toLong()))
}

interface PersonRepository : JpaRepository<Person?, Long?>

@Entity
@Cache(region = "database", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class Person(
    @Id
    @GeneratedValue
    val id: Long,
    var firstName: String,
    var lastName: String,
    var birthdate: LocalDate? = null
)