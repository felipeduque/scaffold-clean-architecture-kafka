package co.com.fac.model.orders.gateways;

import co.com.fac.model.orders.Orders;
import reactor.core.publisher.Mono;

public interface OrdersRepository {
    Mono<Orders> findById(long id);
}
