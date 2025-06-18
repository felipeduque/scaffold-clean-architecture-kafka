package co.com.fac.r2dbc.repository.orders.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("orders")
public class OrdersData {
    @Id
    private Long id;
    
    @Column("customer_id")
    private Long customer_id;
    
    @Column("customer_name")
    private String customer_name;
    
    @Column("customer_email")
    private String customer_email;
    
    private String status;
    private Number total;
    
    @Column("created_at")
    private LocalDateTime created_at;
}
