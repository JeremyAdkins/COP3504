package project.model;

import java.math.BigDecimal;

public final class Loan extends AbstractLoan{
	private final BigDecimal minimumPayment; // required to be paid by the end of the month
	
	public Loan(BigDecimal amount, BigDecimal minimumPayment, BigDecimal interestPremium) throws InvalidInputException, LoanCapException {
		super(interestPremium);
        Bank.getInstance().authorizeLoan(amount);
		applyTransaction(amount, Transaction.Type.WITHDRAWAL);
		this.minimumPayment = minimumPayment;
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
		return Bank.getInstance().getPaymentSchedule().getLoanInterest();
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
