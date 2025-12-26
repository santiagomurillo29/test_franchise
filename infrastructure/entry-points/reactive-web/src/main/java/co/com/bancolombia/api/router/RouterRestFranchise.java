package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.HandlerFranchise;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRestFranchise {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(HandlerFranchise handlerFranchise) {
        return route(POST("/api/franchises"), handlerFranchise::createFranchise);
    }
}
