package org.hazelcast.cache

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import reactor.blockhound.BlockHound


@SpringBootApplication
@EnableR2dbcRepositories
class ImperativeToReactiveApplication

fun main() {
    BlockHound.builder()
        .with {
            it.allowBlockingCallsInside("RandomAccessFile", "readBytes")
        }.install()
    SpringApplication.run(ImperativeToReactiveApplication::class.java)
}
