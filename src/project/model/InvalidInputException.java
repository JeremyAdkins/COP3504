package project.model;

import java.math.BigDecimal;

public final class InvalidInputException extends Exception {
    private final BigDecimal value;

    private final String message;

    InvalidInputException(BigDecimal value, String message) {
        super(String.format("received input $%.2f; %s", value, message));
        this.value = value;
        this.message = message;
    }
}
