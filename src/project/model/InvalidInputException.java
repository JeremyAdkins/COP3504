package project.model;

import java.math.BigDecimal;

/*
 * TODO not sure if we need to worry about storing the value and message
 */
public final class InvalidInputException extends Exception {
    public InvalidInputException(BigDecimal value, String message) {
        super(String.format("received input %.2f; %s", value, message));
    }

    public InvalidInputException(String value, String message) {
        super(String.format("received input \"%s\"; %s", value, message));
    }
}
