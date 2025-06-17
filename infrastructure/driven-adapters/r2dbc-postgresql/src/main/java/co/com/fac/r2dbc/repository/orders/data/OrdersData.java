package co.com.fac.r2dbc.repository.orders.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table("orders")
public class OrdersData {
    @Id
    private long id;
    private String customer_name;
    private String customer_email;
    private String status;
    private Number total;
    private Date created_at;
}
