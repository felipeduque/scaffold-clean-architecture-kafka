package co.com.fac.r2dbc.repository.orders;

import co.com.fac.r2dbc.repository.orders.data.OrdersData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// TODO: This file is just an example, you should delete or modify it
public interface OrdersDataDAO extends ReactiveCrudRepository<OrdersData, Long>, ReactiveQueryByExampleExecutor<OrdersData> {

}
