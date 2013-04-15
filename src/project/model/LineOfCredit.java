package project.model;

import java.math.BigDecimal;

public final class LineOfCredit extends AbstractLoan {
    /**
     * The maximum magnitude of the balance of this account. Any attempts to withdraw funds over this limit will result
     * in an exception. Note that while this amount is positive, the balance itself is negative as it represents funds
     * owed to the bank.
     */
	private BigDecimal creditLimit;
	
	public LineOfCredit(BigDecimal creditLimit, BigDecimal interestPremium) throws InvalidInputException, LoanCapException {
		super(interestPremium);
        if (creditLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(creditLimit, "credit limit must be positive");
        }
        Bank.getInstance().authorizeLoan(creditLimit);
        this.creditLimit = creditLimit;
	}

    @Override
    public Type getType() {
        return Type.LINE_OF_CREDIT;
    }
	
	@Override
	public void close() {
		super.close();
        Bank.getInstance().returnLoan(creditLimit);
	}
	
	@Override
	public Transaction withdraw(BigDecimal amount) throws InvalidInputException, InsufficientFundsException {
		if (getBalance().subtract(amount).negate().compareTo(getCreditLimit()) <= 0) {
			return super.withdraw(amount);
		} else {
			throw new InsufficientFundsException(getCreditLimit().subtract(getBalance().negate()), amount);
		}
	}
	
	@Override
	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

    public void setCreditLimit(BigDecimal creditLimit) throws InvalidInputException, LoanCapException {
        if (getBalance().negate().compareTo(creditLimit) > 0) {
            throw new InvalidInputException(creditLimit, String.format("the current balance of $%.2f exceeds the new credit limit", getBalance().negate()));
        } else if (this.creditLimit.compareTo(creditLimit) < 0) {
            // we are increasing the credit limit, so check for loan authorization
            Bank.getInstance().authorizeLoan(creditLimit.subtract(this.creditLimit));
        } else {
           // we are decreasing the credit limit, so return some loan authorization
            Bank.getInstance().returnLoan(this.creditLimit.subtract(creditLimit));
        }
        this.creditLimit = creditLimit;
    }
	
	@Override
	protected BigDecimal getBaseInterest() {
		return Bank.getInstance().getPaymentSchedule().getLoanInterest().add(Bank.getInstance().getPaymentSchedule().getLocPremium());
	}

	@Override
	protected BigDecimal getMinimumPayment() {
        // note that both the percentPayment and the fixedPayment end up being positive
		// keep in mind that balance is negative, but minimumPayment is positive!
		BigDecimal percentPayment = Bank.getInstance().getPaymentSchedule().getLocPercentPayment();
		BigDecimal fixedPayment = Bank.getInstance().getPaymentSchedule().getLocFixedPayment();
		BigDecimal percentPaymentValue = percentPayment.multiply(getBalance()).negate();

        BigDecimal minimumPayment = percentPaymentValue.max(fixedPayment);
        if (getBalance().negate().compareTo(minimumPayment) < 0) {
            minimumPayment = getBalance().negate();
        }
        return minimumPayment;
	}
	
	@Override
	protected BigDecimal getPenalty() {
		return Bank.getInstance().getPaymentSchedule().getLocPenalty();
	}
}
