package project.model;

import java.math.BigDecimal;

public final class LineOfCredit extends AbstractLoan {
	private BigDecimal creditLimit;
	
	public LineOfCredit(BigDecimal creditLimit, BigDecimal interestPremium) {
		super(interestPremium);
		this.creditLimit = creditLimit;
	}
	
	@Override
	public Transaction withdraw(BigDecimal amount) throws InsufficientFundsException, OverdraftException {
		if (getBalance().subtract(amount).compareTo(getCreditLimit()) >= 0) {
			return super.withdraw(amount);
		} else {
			throw new OverdraftException();
		}
	}

	@Override
	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }
	
	@Override
	protected BigDecimal getBaseInterest() {
		return Bank.getInstance().getPaymentSchedule().getLocInterest();
	}

	@Override
	protected BigDecimal getMinimumPayment() {
		BigDecimal percentPayment = Bank.getInstance().getPaymentSchedule().getLocPercentPayment();
		BigDecimal fixedPayment = Bank.getInstance().getPaymentSchedule().getLocFixedPayment().negate();
		BigDecimal percentPaymentValue = percentPayment.multiply(getBalance());
		
		if (fixedPayment.compareTo(getBalance()) < 0) {
			return getBalance();
		} else if (percentPaymentValue.compareTo(fixedPayment) < 0) {
			return percentPaymentValue;
		} else {
			return fixedPayment;
		}
	}
	
	@Override
	protected BigDecimal getPenalty() {
		return Bank.getInstance().getPaymentSchedule().getLocPenalty();
	}
}
