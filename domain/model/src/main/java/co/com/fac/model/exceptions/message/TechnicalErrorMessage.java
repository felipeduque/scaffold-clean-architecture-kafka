package co.com.fac.model.exceptions.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum TechnicalErrorMessage {
    TECHNICAL_RESTCLIENT_ERROR("SFT010", "Technical error rest client"),
    EXTERNAL_ERROR("SFT011", "External error");

    private final  String code;
    private final String message;
}
