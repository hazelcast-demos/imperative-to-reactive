package org.hazelcast.cache;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.util.StreamUtils;
import reactor.blockhound.BlockHound;

import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootApplication
@EnableR2dbcRepositories
public class ImperativeToReactiveApplication {

    @Bean
    public CommandLineRunner initialize(DatabaseClient client) {
        return (args) -> {
            var schemaIn = new ClassPathResource("/schema.sql").getInputStream();
            var schema = StreamUtils.copyToString(schemaIn, UTF_8);
            var dataIn = new ClassPathResource("/data.sql").getInputStream();
            var data = StreamUtils.copyToString(dataIn, UTF_8);
            client.sql(schema)
                    .then()
                    .and(client
                            .sql(data)
                            .then())
                    .block();
        };
    }

    public static void main(String[] args) {
        BlockHound.install();
        SpringApplication.run(ImperativeToReactiveApplication.class, args);
    }
}