package org.hazelcast.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class PersonRoutes {

    public static class PersonHandler {

        private final PersonRepository repository;

        public PersonHandler(PersonRepository repository) {
            this.repository = repository;
        }

        public Mono<ServerResponse> getAll(ServerRequest req) {
            return ok().bodyValue(repository.findAll(Sort.by("lastName", "firstName")));
        }

        public Mono<ServerResponse> getOne(ServerRequest req) {
            return ok().bodyValue(repository.findById(Long.valueOf(req.pathVariable("id"))));
        }
    }

    @Bean
    public PersonHandler handler(PersonRepository repository) {
        return new PersonHandler(repository);
    }

    @Bean
    public RouterFunction<ServerResponse> routes(PersonHandler handler) {
        return route().path(
            "/person", builder -> builder
                .GET("/", handler::getAll)
                .GET("/{id}", handler::getOne)
        ).build();
    }
}