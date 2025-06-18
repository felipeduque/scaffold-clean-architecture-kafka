package co.com.fac.usecase.orders;

import co.com.fac.model.orders.Orders;
import co.com.fac.model.orders.gateways.OrdersRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateOrderUseCase {
    private final OrdersRepository ordersRepository;

    public Mono<Orders> createOrder(Orders order) {
        return ordersRepository.save(order);
    }
} 