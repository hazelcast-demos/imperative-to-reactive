package org.hazelcast.cache;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import reactor.blockhound.BlockHound;

@SpringBootApplication
@EnableR2dbcRepositories
public class ImperativeToReactiveApplication {

    @Bean
    public ConnectionFactoryInitializer initialize(ConnectionFactory factory) {
        var initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(factory);
        var populator = new CompositeDatabasePopulator();
        populator.addPopulators(
                new ResourceDatabasePopulator(new ClassPathResource("/schema.sql")),
                new ResourceDatabasePopulator(new ClassPathResource("/data.sql"))
        );
        initializer.setDatabasePopulator(populator);
        return initializer;
    }

    public static void main(String[] args) {
        BlockHound.install();
        SpringApplication.run(ImperativeToReactiveApplication.class, args);
    }
}