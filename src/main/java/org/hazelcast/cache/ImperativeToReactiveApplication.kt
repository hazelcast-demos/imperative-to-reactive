package org.hazelcast.cache

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories


@SpringBootApplication
@EnableR2dbcRepositories
class ImperativeToReactiveApplication

fun main() {
    SpringApplication.run(ImperativeToReactiveApplication::class.java)
}
