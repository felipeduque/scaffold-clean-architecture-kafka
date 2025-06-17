package co.com.fac.api;


import co.com.fac.model.orders.Orders;
import co.com.fac.model.orders.gateways.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Handler {
//private  final UseCase useCase;
private  final OrdersRepository tourRepository;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        return tourRepository.findById(3)
                .flatMap(tour -> {
                    Map<String, List<Orders>> response = Map.of("orders", List.of(tour));
                    return ServerResponse.ok().bodyValue(response);
                });
    }

    public Mono<ServerResponse> listenPOSTUseCase(ServerRequest serverRequest) {
        // useCase.logic();
        return ServerResponse.ok().bodyValue("");
    }
}
