package project.model;

public final class RepeatingPaymentException extends Exception {
    private final String description;

    RepeatingPaymentException(String description) {
        super(String.format("duplicate repeating payment with description \"%s\"", description));
        this.description = description;
    }
}
