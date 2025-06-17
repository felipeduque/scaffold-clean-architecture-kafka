package co.com.fac.model.exceptions.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum BusinessErrorMessage {
    INVALID_REQUEST("SF800", "Invalid Request"),
    TOUR_VALIDATION("SF801", "Tour validation error"),
    TOUR_FIND_ERROR("SF802", "Tour find error"),
    CHANNEL_TX_NOT_FOUND("", "Channel transaction not found");

    private final  String code;
    private final String message;
}
