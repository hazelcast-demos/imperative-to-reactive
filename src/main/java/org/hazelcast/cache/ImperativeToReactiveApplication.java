package org.hazelcast.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import reactor.blockhound.BlockHound;

@SpringBootApplication
@EnableR2dbcRepositories
public class ImperativeToReactiveApplication {

    public static void main(String[] args) {
        BlockHound.install();
        SpringApplication.run(ImperativeToReactiveApplication.class, args);
    }
}