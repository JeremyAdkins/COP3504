package project.model;

public class InsufficientFundsException extends Exception {
    InsufficientFundsException() {
        super();
    }

    InsufficientFundsException(String message) {
        super(message);
    }
}
