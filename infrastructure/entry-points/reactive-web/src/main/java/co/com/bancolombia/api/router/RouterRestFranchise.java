package co.com.bancolombia.api.router;

import co.com.bancolombia.api.handler.HandlerFranchise;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRestFranchise {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(HandlerFranchise handlerFranchise) {
        return route(POST("/api/franchises"), handlerFranchise::createFranchise)
                .andRoute(POST("/api/franchises/{idFranchise}/branches"), handlerFranchise::addBranchToFranchise)
                .andRoute(POST("/api/branches/{idBranch}/products"), handlerFranchise::addProductToBranch)
                .andRoute(POST("/api/franchises/{idFranchise}/products"), handlerFranchise::createProduct)
                .andRoute(PATCH("/api/products/{idProduct}/stock"), handlerFranchise::updateStockProduct)
                .andRoute(DELETE("/api/branches/{idBranch}/products/{idProduct}"), handlerFranchise::deleteProductOfBranch);
    }
}
