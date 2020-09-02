package org.hazelcast.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;

@Configuration
public class PersonRoutes {

    public static class PersonHandler {

        private final PersonRepository repository;

        public PersonHandler(PersonRepository repository) {
            this.repository = repository;
        }

        public ServerResponse getAll(ServerRequest req) {
            return ok().body(repository.findAll(Sort.by("lastName", "firstName")));
        }

        public ServerResponse getOne(ServerRequest req) {
            return ok().body(repository.findById(Long.valueOf(req.pathVariable("id"))));
        }
    }

    @Bean
    public PersonHandler handler(PersonRepository repository) {
        return new PersonHandler(repository);
    }

    @Bean
    public RouterFunction<ServerResponse> getAll(PersonHandler handler) {
        return route().GET("/person", handler::getAll).build();
    }


    @Bean
    public RouterFunction<ServerResponse> getOne(PersonHandler handler) {
        return route().GET("/person/{id}", handler::getOne).build();
    }
}