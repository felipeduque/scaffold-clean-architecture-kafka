package co.com.fac.model.exceptions;

import co.com.fac.model.exceptions.message.BusinessErrorMessage;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final BusinessErrorMessage errorMessage;

    public BusinessException(BusinessErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }

}
