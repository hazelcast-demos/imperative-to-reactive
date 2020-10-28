package org.hazelcast.cache

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.servlet.function.RouterFunctions.route
import org.springframework.web.servlet.function.ServerResponse.ok
import java.time.LocalDate
import javax.persistence.*


@Configuration
class PersonRoutes {

    @Bean
    fun getAll(repository: PersonRepository) = route()
        .GET("/person") {
            ok().body(repository.findAll(Sort.by("lastName", "firstName")))
        }.build()

    @Bean
    fun getOne(repository: PersonRepository) = route()
        .GET("/person/{id}") {
            req -> ok().body(repository.findById(req.pathVariable("id").toLong()))
        }.build()
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