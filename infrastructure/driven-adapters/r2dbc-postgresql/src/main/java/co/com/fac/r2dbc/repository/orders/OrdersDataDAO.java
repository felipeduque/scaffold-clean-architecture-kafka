package co.com.fac.r2dbc.repository.orders;

import co.com.fac.r2dbc.repository.orders.data.OrdersData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import java.time.LocalDateTime;

public interface OrdersDataDAO extends ReactiveCrudRepository<OrdersData, Long>, ReactiveQueryByExampleExecutor<OrdersData> {
    @Query("SELECT * FROM orders " +
    "WHERE LOWER(status) LIKE LOWER(CONCAT('%', :status, '%')) " +
    "AND (:created_at IS NULL OR created_at::date = :created_at::date) " +
    "AND LOWER(customer_name) LIKE LOWER(CONCAT('%', :customerName, '%'))")
Flux<OrdersData> findByFilters(@Param("status") String status,
                              @Param("created_at") LocalDateTime created_at,
                              @Param("customerName") String customerName);

    @Query("SELECT * FROM orders WHERE customer_id = :customerId")
    Flux<OrdersData> findByCustomerId(@Param("customerId") Long customerId);
}
