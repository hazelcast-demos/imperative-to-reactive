package org.hazelcast.cache

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import javax.persistence.*

@RestController
class PersonController(private val repository: PersonRepository) {

    @GetMapping("/person")
    fun getAll() = repository.findAll(Sort.by("lastName", "firstName"))

    @GetMapping("/person/{id}")
    fun getOne(@PathVariable id: Long) = repository.findById(id)
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