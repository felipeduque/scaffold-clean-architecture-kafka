package co.com.fac.model.orders;
import co.com.fac.model.exceptions.BusinessException;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;

import java.util.Date;

import static co.com.fac.model.exceptions.message.BusinessErrorMessage.TOUR_VALIDATION;

@Getter
//@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Orders {
    private final Long id;
    private final String customer_name;
    private final String customer_email;
    private final String status;
    private final Number total;
    private final Date created_at;

    public static Orders newTour(long id, String customer_name, String customer_email, String status, Number total, Date created_at ) {
        if (customer_name.equals("error"))
            throw new BusinessException(TOUR_VALIDATION);
        return new Orders(id, customer_name, customer_email, status, total, created_at);
    }
}
