package co.com.fac.api;

import co.com.fac.model.orders.Orders;
import co.com.fac.model.orders.gateways.OrdersRepository;
import co.com.fac.usecase.orders.CreateOrderUseCase;
import co.com.fac.usecase.orders.DeleteOrderUseCase;
import co.com.fac.usecase.orders.FindOrdersByCustomerIdUseCase;
import co.com.fac.usecase.orders.FindOrdersByFiltersUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

public class RouterRestTest {

    private Handler handler;
    private RouterFunction<ServerResponse> routerFunction;
    private WebTestClient webTestClient;

    // Mocks
    private CreateOrderUseCase createOrderUseCase;
    private DeleteOrderUseCase deleteOrderUseCase;
    private OrdersRepository ordersRepository;
    private FindOrdersByFiltersUseCase findOrdersByFiltersUseCase;
    private FindOrdersByCustomerIdUseCase findOrdersByCustomerIdUseCase;

    @BeforeEach
    void setUp() {
        createOrderUseCase = Mockito.mock(CreateOrderUseCase.class);
        deleteOrderUseCase = Mockito.mock(DeleteOrderUseCase.class);
        ordersRepository = Mockito.mock(OrdersRepository.class);
        findOrdersByFiltersUseCase = Mockito.mock(FindOrdersByFiltersUseCase.class);
        findOrdersByCustomerIdUseCase = Mockito.mock(FindOrdersByCustomerIdUseCase.class);

        // Configura los mocks para evitar errores en los handlers
        Mockito.when(deleteOrderUseCase.deleteOrder(Mockito.anyLong()))
            .thenReturn(Mono.empty());
        Mockito.when(findOrdersByCustomerIdUseCase.find(Mockito.anyLong()))
            .thenReturn(Flux.empty());
         // Mock para evitar error 500 en GET /api/orders/filter
        Mockito.when(findOrdersByFiltersUseCase.find(anyString(), any(), anyString()))
            .thenReturn(Flux.empty());
        // Mock para GET by id
        Orders mockOrder = Mockito.mock(Orders.class);
        Mockito.when(ordersRepository.findById(Mockito.eq(1)))
            .thenReturn(Mono.just(mockOrder));
        Mockito.when(ordersRepository.findById(Mockito.eq(Integer.valueOf(1))))
            .thenReturn(Mono.just(mockOrder));

        handler = new Handler(
                createOrderUseCase,
                deleteOrderUseCase,
                ordersRepository,
                findOrdersByFiltersUseCase,
                findOrdersByCustomerIdUseCase
        );
        RouterRest routerRest = new RouterRest();
        routerFunction = routerRest.routerFunction(handler);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @Test
    void testListenGETUseCase() {
        webTestClient.get()
                .uri("/api/usecase/path")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testListenPOSTUseCase() {
        // Prepara un objeto Orders válido
        Orders validOrder = new Orders();
        validOrder.setCustomer_name("John Doe");
        validOrder.setCustomer_email("john@example.com");
        validOrder.setStatus("NEW");
        validOrder.setTotal(100.0);

        // Mock: el caso de uso retorna el mismo objeto
        Mockito.when(createOrderUseCase.createOrder(any(Orders.class)))
                .thenReturn(Mono.just(validOrder));

        webTestClient.post()
                .uri("/api/add/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                    {
                        "customer_name": "John Doe",
                        "customer_email": "john@example.com",
                        "status": "NEW",
                        "total": 100.0
                    }
                """)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.order.customer_name").isEqualTo("John Doe");
    }

    @Test
    void testListenGETOrdersByFilters() {
        webTestClient.get()
                .uri("/api/orders/filter")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody();
    }

    @Test
    void testListenGETOrdersByCustomerId() {
        webTestClient.get()
                .uri("/api/v1/orders/customer/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody();
    }


    @Test
    void testListenDeleteUseCase() {
        webTestClient.delete()
                .uri("/api/orders/delete/1")
                .exchange()
                .expectStatus().isNoContent() // 204 es el esperado por ServerResponse.noContent()
                .expectBody();

        // Opcional: verifica que el mock fue llamado
        Mockito.verify(deleteOrderUseCase).deleteOrder(1L);
    }

}