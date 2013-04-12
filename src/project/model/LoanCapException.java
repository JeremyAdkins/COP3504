package project.model;

import java.math.BigDecimal;

public final class LoanCapException extends Exception {
    private final BigDecimal loanAmount;

    private final BigDecimal loanCap;

    LoanCapException(BigDecimal loanAmount, BigDecimal loanCap) {
        super(String.format("cannot lend out $%.2f with the current loan cap of $%.2f", loanAmount, loanCap));
        if (loanAmount.compareTo(BigDecimal.ZERO) < 0 || loanCap.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amounts in LoanCapException must be non-negative");
        }
        this.loanAmount = loanAmount;
        this.loanCap = loanCap;
    }
}
