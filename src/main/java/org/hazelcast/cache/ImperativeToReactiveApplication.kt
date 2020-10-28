package org.hazelcast.cache

import com.hazelcast.core.HazelcastInstance
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import reactor.blockhound.BlockHound


@SpringBootApplication
@EnableR2dbcRepositories
class ImperativeToReactiveApplication {

    @Bean
    fun service(instance: HazelcastInstance, repository: PersonRepository) =
        CachingService(instance.getMap<Long, Person>("persons"), repository)
}

fun main() {
    BlockHound.builder()
        .with {
            it.allowBlockingCallsInside("RandomAccessFile", "readBytes")
        }.install()
    SpringApplication.run(ImperativeToReactiveApplication::class.java)
}
