package project.model;

import java.math.BigDecimal;

public final class Loan extends AbstractLoan {
    /**
     * The base interest rate. This value is copied from the {@link PaymentSchedule} when the {@code Loan} is created, so
     * that future interest rate changes do not affect the loan, and in particular, its minimum payment.
     */
    private final BigDecimal baseInterest;

    /**
     * The amount required to be paid towards the loan by the end of the month. If the minimum payment is not made, a
     * penalty is applied to the account balance. Minimum payments are calculated based on the loan term and the interest
     * rate.
     */
	private final BigDecimal minimumPayment;
	
	public Loan(BigDecimal amount, int term, BigDecimal interestPremium) throws InvalidInputException, LoanCapException {
		super(interestPremium);
        Bank.getInstance().authorizeLoan(amount);
		applyTransaction(amount, Transaction.Type.WITHDRAWAL);
        if (term <= 0) {
            throw new InvalidInputException(new BigDecimal(term), "loan term must be positive");
        }

        baseInterest = Bank.getInstance().getPaymentSchedule().getLoanInterest();
        BigDecimal monthlyInterest = baseInterest.add(interestPremium).divide(new BigDecimal("12"), Bank.MATH_CONTEXT);
        // see http://en.wikipedia.org/wiki/Amortization_calculator (the leftmost version of the equation)
        BigDecimal exponentialTerm = monthlyInterest.add(BigDecimal.ONE).pow(term);
        BigDecimal numerator = monthlyInterest.multiply(exponentialTerm);
        BigDecimal denominator = exponentialTerm.subtract(BigDecimal.ONE);
        minimumPayment = amount.multiply(numerator.divide(denominator, Bank.MATH_CONTEXT));
	}

    @Override
    public Type getType() {
        return Type.LOAN;
    }
	
	@Override
	public Transaction deposit(BigDecimal amount) throws InvalidInputException {
        // call super before altering loanCap, in case there's an exception
        Transaction trans = super.deposit(amount);
        Bank.getInstance().returnLoan(amount);
        return trans;
	}
	
	@Override
	public Transaction withdraw(BigDecimal amount) {
		throw new UnsupportedOperationException();
	}

    @Override
    protected void doPayments() throws InvalidInputException, InsufficientFundsException {
        if (getBalance().compareTo(BigDecimal.ZERO) == 0) {
            close();
        }
        super.doPayments();
    }
	
	@Override
	protected BigDecimal getBaseInterest() {
		return baseInterest;
	}

	@Override
	protected BigDecimal getCreditLimit() {
		return getBalance();
	}

    @Override
    protected BigDecimal getMinimumPayment() {
        return minimumPayment;
    }
	
	@Override
	protected BigDecimal getPenalty() {
		return Bank.getInstance().getPaymentSchedule().getLoanPenalty();
	}
}
