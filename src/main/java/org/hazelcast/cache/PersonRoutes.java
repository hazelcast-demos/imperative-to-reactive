package org.hazelcast.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class PersonRoutes {

    public static class PersonHandler {

        private final CachingService service;

        public PersonHandler(CachingService service) {
            this.service = service;
        }

        public Mono<ServerResponse> getAll(ServerRequest req) {
            var all = service.findAll(Sort.by("lastName", "firstName"));
            return ok().body(fromPublisher(all, Person.class));
        }

        public Mono<ServerResponse> getOne(ServerRequest req) {
            var mono = service
                    .findById(Long.valueOf(req.pathVariable("id")))
                    .switchIfEmpty(Mono.error(() -> new ResponseStatusException(NOT_FOUND)));
            return ok().body(fromPublisher(mono, Person.class));
        }
    }

    @Bean
    public PersonHandler handler(CachingService service) {
        return new PersonHandler(service);
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