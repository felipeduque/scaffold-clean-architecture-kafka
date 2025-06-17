package co.com.fac.model.exceptions;

import co.com.fac.model.exceptions.message.TechnicalErrorMessage;

public class TechnicalException extends RuntimeException{

    private final TechnicalErrorMessage errorMessage;

    public TechnicalException(Throwable cause, TechnicalErrorMessage errorMessage) {
        super(errorMessage.getMessage(), cause);
        this.errorMessage = errorMessage;
    }
}
