package co.com.fac.usecase.orders;

import co.com.fac.model.orders.Orders;
import co.com.fac.model.orders.gateways.OrdersRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class FindOrdersByFiltersUseCase {
    private final OrdersRepository ordersRepository;

    public Flux<Orders> find(String status, LocalDateTime createdAt, String customerName) {
        return ordersRepository.findByFilters(status, createdAt, customerName);
    }
} 