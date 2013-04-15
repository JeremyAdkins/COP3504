package project.model;

import java.math.BigDecimal;

public final class InsufficientFundsException extends Exception {
    private final BigDecimal available;

    private final BigDecimal requested;

    InsufficientFundsException(BigDecimal available, BigDecimal requested) {
        super(String.format("available funds of $%.2f are insufficient to cover request for $%.2f", available, requested));
        this.available = available;
        this.requested = requested;
    }
    /**
     * funds are negative
     * @param requested 
     */
    InsufficientFundsException(BigDecimal requested){
        super("You do not have any funds!");
        available = BigDecimal.ZERO;
        this.requested = requested;
    }
}
