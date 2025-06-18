package co.com.fac.model.orders.gateways;

import co.com.fac.model.orders.Orders;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

public interface OrdersRepository {
    Mono<Orders> findById(long id);
    Mono<Orders> save(Orders order);
    Mono<Void> deleteById(Long id);
    Flux<Orders> findByFilters(String status, java.time.LocalDateTime createdAt, String customerName);
    Flux<Orders> findByCustomerId(Long customerId);
}
