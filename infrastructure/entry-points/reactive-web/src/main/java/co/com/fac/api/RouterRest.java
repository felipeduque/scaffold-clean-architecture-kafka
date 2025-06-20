package co.com.fac.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/usecase/path"), handler::listenGETUseCase)
                .andRoute(POST("/api/add/orders"), handler::listenPOSTUseCase)
                .andRoute(GET("/api/path/orders/{id}"), handler::listenGETOrdersIdUseCase)
                .andRoute(GET("/api/orders/filter"), handler::listenGETOrdersByFilters)
                .andRoute(GET("/api/v1/orders/customer/{customerId}"), handler::listenGETOrdersByCustomerId)
                .andRoute(DELETE("/api/orders/delete/{id}"), handler::listenDeleteUseCase);
    }
}
