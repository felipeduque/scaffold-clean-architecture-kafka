package co.com.fac.api;


import co.com.fac.model.orders.Orders;
import co.com.fac.model.orders.gateways.OrdersRepository;
import co.com.fac.usecase.orders.CreateOrderUseCase;
import co.com.fac.usecase.orders.DeleteOrderUseCase;
import co.com.fac.usecase.orders.FindOrdersByCustomerIdUseCase;
import co.com.fac.usecase.orders.FindOrdersByFiltersUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Handler {
//private  final UseCase useCase;
private final CreateOrderUseCase createOrderUseCase;
private final DeleteOrderUseCase deleteOrderUseCase;
private final OrdersRepository tourRepository;
private final FindOrdersByFiltersUseCase findOrdersByFiltersUseCase;
private final FindOrdersByCustomerIdUseCase findOrdersByCustomerIdUseCase;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        return Mono.just(serverRequest)
                .map(request -> request.pathVariable("id"))
                .map(Integer::parseInt)
                .flatMap(tourRepository::findById)
                .flatMap(tour -> {
                    Map<String, List<Orders>> response = Map.of("orders", List.of(tour));
                    return ServerResponse.ok().bodyValue(response);
                })
                .onErrorResume(NumberFormatException.class, 
                    e -> ServerResponse.badRequest().bodyValue("Invalid ID format"))
                .onErrorResume(e -> ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Orders.class)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Request body is required")))
                .flatMap(order -> {
                    // Validate required fields
                    if (order.getCustomer_name()== null || order.getCustomer_name().trim().isEmpty()) {
                        return Mono.error(new IllegalArgumentException("Customer name is required"));
                    }
                    if (order.getCustomer_email() == null || order.getCustomer_email().trim().isEmpty()) {
                        return Mono.error(new IllegalArgumentException("Customer email is required"));
                    }
                    if (order.getStatus() == null || order.getStatus().trim().isEmpty()) {
                        return Mono.error(new IllegalArgumentException("Status is required"));
                    }
                    if (order.getTotal() == null) {
                        return Mono.error(new IllegalArgumentException("Total is required"));
                    }
                    
                    // Set creation date if not provided
                    if (order.getCreated_at() == null) {
                        order.setCreated_at(LocalDateTime.now());
                    }
                    return createOrderUseCase.createOrder(order);
                })
                .flatMap(order -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("order", order)))
                .onErrorResume(IllegalArgumentException.class, 
                    e -> ServerResponse.badRequest()
                        .bodyValue(Map.of("error", e.getMessage())))
                .onErrorResume(e -> ServerResponse.badRequest()
                        .bodyValue(Map.of("error", "Error creating order: " + e.getMessage())));
    }

    public Mono<ServerResponse> listenDELETEUseCase(ServerRequest serverRequest) {
        return Mono.just(serverRequest)
                .map(request -> request.pathVariable("id"))
                .map(Long::parseLong)
                .flatMap(deleteOrderUseCase::deleteOrder)
                .then(ServerResponse.noContent().build())
                .onErrorResume(NumberFormatException.class, 
                    e -> ServerResponse.badRequest()
                        .bodyValue(Map.of("error", "Invalid ID format")))
                .onErrorResume(IllegalArgumentException.class,
                    e -> ServerResponse.status(404)
                        .bodyValue(Map.of("error", e.getMessage())))
                .onErrorResume(e -> ServerResponse.badRequest()
                        .bodyValue(Map.of("error", "Error deleting order: " + e.getMessage())));
    }

    public Mono<ServerResponse> listenGETOrdersByFilters(ServerRequest serverRequest) {
        String status = serverRequest.queryParam("status").orElse("");
        String customerName = serverRequest.queryParam("customer_name").orElse("");
        String createdAtStr = serverRequest.queryParam("created_at").orElse("");
        java.time.LocalDateTime createdAt = null;
        if (!createdAtStr.isEmpty()) {
            try {
                createdAt = java.time.LocalDateTime.parse(createdAtStr);
            } catch (Exception e) {
                return ServerResponse.badRequest().bodyValue(Map.of("error", "Invalid created_at format. Use ISO-8601."));
            }
        }
        return findOrdersByFiltersUseCase.find(status, createdAt, customerName)
                .collectList()
                .flatMap(orders -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(Map.of("orders", orders)));
    }

    public Mono<ServerResponse> listenGETOrdersByCustomerId(ServerRequest serverRequest) {
        Long customerId;
        try {
            customerId = Long.parseLong(serverRequest.pathVariable("customerId"));
        } catch (Exception e) {
            return ServerResponse.badRequest().bodyValue(Map.of("error", "Invalid customerId"));
        }
        return findOrdersByCustomerIdUseCase.find(customerId)
                .collectList()
                .flatMap(orders -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(Map.of("orders", orders)));
    }
}
