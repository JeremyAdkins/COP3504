package project.model;

import java.math.BigDecimal;

public final class LoanCapException extends Exception {
    private final BigDecimal loanCap;

    private final BigDecimal loanAmount;

    LoanCapException(BigDecimal loanCap, BigDecimal loanAmount) {
        super(String.format("the current loan cap of $%.2f prohibits a new loan of $%.2f", loanCap, loanAmount));
        if (loanAmount.compareTo(BigDecimal.ZERO) < 0 || loanCap.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amounts in LoanCapException must be non-negative");
        }
        this.loanCap = loanCap;
        this.loanAmount = loanAmount;
    }
}
