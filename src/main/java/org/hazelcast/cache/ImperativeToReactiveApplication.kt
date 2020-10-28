package org.hazelcast.cache

import com.hazelcast.core.HazelcastInstance
import com.hazelcast.map.IMap
import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.init.*
import reactor.blockhound.BlockHound


@SpringBootApplication
@EnableR2dbcRepositories
class ImperativeToReactiveApplication {

    @Bean
    fun initialize(factory: ConnectionFactory) = ConnectionFactoryInitializer()
        .apply {
            setConnectionFactory(factory)
            val populator = CompositeDatabasePopulator()
                .apply {
                    addPopulators(
                        ResourceDatabasePopulator(ClassPathResource("/schema.sql")),
                        ResourceDatabasePopulator(ClassPathResource("/data.sql"))
                    )
                }
            setDatabasePopulator(populator)
        }

    @Bean
    fun service(cache: IMap<Long, Person>, repository: PersonRepository) = CachingService(cache, repository)

    @Bean
    fun cache(instance: HazelcastInstance) = instance.getMap<Long, Person>("persons")
}

fun main() {
    BlockHound.builder()
        .with {
            it.allowBlockingCallsInside("RandomAccessFile", "readBytes")
        }.install()
    SpringApplication.run(ImperativeToReactiveApplication::class.java)
}
