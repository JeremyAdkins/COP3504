package project.model;

import java.math.BigDecimal;

public final class InsufficientFundsException extends Exception {
    private final BigDecimal available;

    private final BigDecimal requested;

    InsufficientFundsException(BigDecimal available, BigDecimal requested) {
        super(String.format("available funds of $%.2f are insufficient to cover request for $%.2f", available, requested));
        if (available.compareTo(BigDecimal.ZERO) < 0 || available.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amounts in InsufficientFundsException must be non-negative");
        }
        this.available = available;
        this.requested = requested;
    }
}
