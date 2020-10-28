package org.hazelcast.cache

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.servlet.function.RouterFunctions.route
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse.ok
import java.time.LocalDate
import javax.persistence.*


@Configuration
class PersonRoutes {

    @Bean
    fun handler(repository: PersonRepository) = PersonHandler(repository)

    @Bean
    fun getAll(handler: PersonHandler) = route()
        .GET("/person", handler::getAll)
        .build()

    @Bean
    fun getOne(handler: PersonHandler) = route()
        .GET("/person/{id}", handler::getOne)
        .build()
}

class PersonHandler(private val repository: PersonRepository) {
    fun getAll(req: ServerRequest) = ok().body(repository.findAll(Sort.by("lastName", "firstName")))
    fun getOne(req: ServerRequest) = ok().body(repository.findById(req.pathVariable("id").toLong()))
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