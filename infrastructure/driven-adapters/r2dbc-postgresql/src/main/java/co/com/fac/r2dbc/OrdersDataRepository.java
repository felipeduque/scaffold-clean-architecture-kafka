package co.com.fac.r2dbc;

import co.com.fac.model.orders.Orders;
import co.com.fac.model.orders.gateways.OrdersRepository;
import co.com.fac.r2dbc.repository.orders.OrdersDataDAO;
import co.com.fac.r2dbc.repository.orders.data.OrdersData;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class OrdersDataRepository implements OrdersRepository {
    private final OrdersDataDAO repository;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Orders> findById(long id) {
        return repository.findById(id)
                .map(this::toEntity);
    }

    private Orders toEntity(OrdersData data) {
        return objectMapper.mapBuilder(data, Orders.OrdersBuilder.class).build();
    }

    private OrdersData toData(Orders entity) {
        return objectMapper.map(entity, OrdersData.class);
    }
}
