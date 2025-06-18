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

    @Override
    public Mono<Orders> save(Orders order) {
        return Mono.just(order)
                .map(this::toData)
                .flatMap(repository::save)
                .map(this::toEntity);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }

    @Override
    public reactor.core.publisher.Flux<Orders> findByFilters(String status, java.time.LocalDateTime createdAt, String customerName) {
        return repository.findByFilters(status, createdAt, customerName)
                .map(this::toEntity);
    }

    @Override
    public reactor.core.publisher.Flux<Orders> findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId)
                .map(this::toEntity);
    }

    private Orders toEntity(OrdersData data) {
        return Orders.builder()
                .id(data.getId())
                .customer_id(data.getCustomer_id())
                .customer_name(data.getCustomer_name())
                .customer_email(data.getCustomer_email())
                .status(data.getStatus())
                .total(data.getTotal())
                .created_at(data.getCreated_at())
                .build();
    }

    private OrdersData toData(Orders entity) {
        return OrdersData.builder()
                .id(entity.getId())
                .customer_id(entity.getCustomer_id())
                .customer_name(entity.getCustomer_name())
                .customer_email(entity.getCustomer_email())
                .status(entity.getStatus())
                .total(entity.getTotal())
                .created_at(entity.getCreated_at())
                .build();
    }
}
