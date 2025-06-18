package co.com.fac.model.orders;
import co.com.fac.model.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static co.com.fac.model.exceptions.message.BusinessErrorMessage.TOUR_VALIDATION;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    private Long id;
    private Long customer_id;
    private String customer_name;
    private String customer_email;
    private String status;
    private Number total;
    private LocalDateTime created_at;

    public static Orders newTour(long id, String customer_name, String customer_email, String status, Number total, LocalDateTime created_at) {
        if (customer_name.equals("error"))
            throw new BusinessException(TOUR_VALIDATION);
        return new Orders(id, null, customer_name, customer_email, status, total, created_at);
    }
}
