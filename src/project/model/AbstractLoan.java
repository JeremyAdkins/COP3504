package project.model;

import java.math.BigDecimal;

public abstract class AbstractLoan extends Account {
	private BigDecimal interestPremium;

	private BigDecimal depositsToDate;
	
	protected AbstractLoan(BigDecimal interestPremium) throws InvalidInputException {
        if (interestPremium.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(interestPremium, "interestPremium must be non-negative");
        }
		this.interestPremium = interestPremium;
		this.depositsToDate = BigDecimal.ZERO;
	}

    @Override
    public Transaction deposit(BigDecimal amount) throws InvalidInputException {
        BigDecimal newBalance = getBalance().add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) > 0) {
            throw new InvalidInputException(amount, String.format("cannot repay more than the current balance of $%.2f", getBalance()));
        }
        depositsToDate = depositsToDate.add(amount);
        return super.deposit(amount);
    }

    @Override
	protected void doPayments() throws InvalidInputException, InsufficientFundsException {
		super.doPayments();
		depositsToDate = BigDecimal.ZERO;
	}
	
	@Override
	public final BigDecimal getInterestRate() {
		return getBaseInterest().add(interestPremium);
	}
	
	public final BigDecimal getInterestPremium() {
		return this.interestPremium;
	}
	
	public final void setInterestPremium(BigDecimal interestPremium) {
		this.interestPremium = interestPremium;
	}
	
	@Override
	protected final BigDecimal getMonthlyCharge() {
		BigDecimal minimumPayment = getMinimumPayment();
		if (minimumPayment.compareTo(depositsToDate) > 0) {
			return getPenalty();
		} else {
			return BigDecimal.ZERO;
		}
	}

	@Override
	protected final BigDecimal getThreshold() {
		return BigDecimal.ZERO;
	}
	
	protected abstract BigDecimal getBaseInterest();

	protected abstract BigDecimal getCreditLimit();

	protected abstract BigDecimal getMinimumPayment();

	protected abstract BigDecimal getPenalty();
}
