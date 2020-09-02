package org.hazelcast.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;

@Configuration
public class PersonRoutes {

    @Bean
    public RouterFunction<ServerResponse> getAll(PersonRepository repository) {
        return route().GET(
    "/person",
            req -> ok().body(repository.findAll(Sort.by("lastName", "firstName")))
        ).build();
    }

    @Bean
    public RouterFunction<ServerResponse> getOne(PersonRepository repository) {
        return route().GET(
    "/person/{id}",
            req -> ok().body(repository.findById(Long.valueOf(req.pathVariable("id"))))
        ).build();
    }
}