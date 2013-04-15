package project.model;

import java.math.BigDecimal;

public final class SavingsAccount extends Account {

    @Override
    public Type getType() {
        return Type.SAVINGS;
    }

    @Override
    public Transaction withdraw(BigDecimal amount) throws InvalidInputException, InsufficientFundsException {
        if (getBalance().compareTo(amount) >= 0) {
            return super.withdraw(amount);
        } else {
            if (getBalance().compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientFundsException(getBalance(), amount);
            } else {
                throw new InsufficientFundsException(getBalance(), amount);
            }
        }
    }

    @Override
    public BigDecimal getInterestRate() {
        return Bank.getInstance().getPaymentSchedule().getSavingsInterest();
    }

    @Override
    protected BigDecimal getMonthlyCharge() {
        return Bank.getInstance().getPaymentSchedule().getSavingsCharge();
    }

    @Override
    protected BigDecimal getThreshold() {
        return Bank.getInstance().getPaymentSchedule().getSavingsThreshold();
    }
}
