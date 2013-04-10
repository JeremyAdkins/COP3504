package project.model;

import java.math.BigDecimal;

public abstract class AbstractLoan extends Account {
	private BigDecimal interestPremium;

	private BigDecimal depositsToDate;
	
	protected AbstractLoan(BigDecimal interestPremium) {
		this.interestPremium = interestPremium;
		this.depositsToDate = BigDecimal.ZERO;
	}
	
	@Override
	public final Transaction deposit(BigDecimal amount) {
        BigDecimal newBalance = getBalance().add(amount);
		if (newBalance.compareTo(BigDecimal.ZERO) > 0) {
			throw new IllegalArgumentException("Overpaying what you owe");
		}
		depositsToDate = depositsToDate.add(amount);
		return super.deposit(amount);
	}
	
	@Override
	protected void doPayments() throws InsufficientFundsException, OverdraftException {
		super.doPayments();
		depositsToDate = BigDecimal.ZERO;
	}
	
	@Override
	protected final BigDecimal getInterestRate() {
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
		if (minimumPayment.compareTo(depositsToDate.negate()) < 0) {
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
