package co.com.fac.usecase.orders;

import co.com.fac.model.orders.gateways.OrdersRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DeleteOrderUseCase {
    private final OrdersRepository ordersRepository;

    public Mono<Void> deleteOrder(Long id) {
        return ordersRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Order not found")))
                .flatMap(order -> ordersRepository.deleteById(id));
    }
} 