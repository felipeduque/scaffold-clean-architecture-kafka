package co.com.fac.usecase.orders;

import co.com.fac.model.orders.Orders;
import co.com.fac.model.orders.gateways.OrdersRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class FindOrdersByCustomerIdUseCase {
    private final OrdersRepository ordersRepository;

    public Flux<Orders> find(Long customerId) {
        return ordersRepository.findByCustomerId(customerId);
    }
}