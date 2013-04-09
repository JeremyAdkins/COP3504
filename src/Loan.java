import java.math.BigDecimal;


public class Loan extends AbstractLoan{
	BigDecimal minimumPayment;//Total money needed to be payed by end of month
	
	public Loan(BigDecimal amount, BigDecimal minimumPayment, BigDecimal interestPremium) {
		// TODO possibly calculate minimumPayment ourselves
		super(interestPremium);
		applyTransaction(amount, Transaction.Type.WITHDRAWAL);
		this.minimumPayment = minimumPayment;
	}
	
	@Override
	public Transaction withdraw(BigDecimal amount) throws OverdraftException{
		throw new UnsupportedOperationException();
	}
	
	protected BigDecimal getMinimumPayment(){
		return minimumPayment;
	}
	
	@Override
	protected BigDecimal getBaseInterest() {
		return Bank.getInstance().getPaymentSchedule().getLoanInterest();
	}

	@Override
	protected BigDecimal getCreditLimit() {//Used for calculating loanCap
		return getBalance();
	}
	
	@Override
	protected BigDecimal getPenalty() {
		return Bank.getInstance().getPaymentSchedule().getLoanPenalty();
	}
}
