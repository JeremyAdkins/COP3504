import java.math.BigDecimal;
import java.math.RoundingMode;

public class CertificateOfDeposit extends Account {
    private CdTerm term;

    private int monthsElapsed;

	public CdTerm getTerm() {
		return term;
	}
	
	public int getMonthsElapsed() {
		return monthsElapsed;
	}

	public Transaction withdraw(BigDecimal amount) throws OverdraftException
	{
		BigDecimal fee = getInterestRate().divide(new BigDecimal(2),4,RoundingMode.HALF_EVEN).multiply(getBalance());
		super.applyFee(fee);

        BigDecimal newBalance = getBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new OverdraftException();//TODO Do we want to use an OverdraftException, or something tailored for the CoD class?
        } else if (newBalance.compareTo(BigDecimal.ZERO) == 0) {
            close();
        }
		return super.withdraw(amount);
	}

    @Override
    protected void doPayments() throws OverdraftException {
        super.doPayments();
        monthsElapsed += 1;
    }
	
	public BigDecimal getInterestRate()
	{	
		if (monthsElapsed < term.getLength()) {
            return Bank.getInstance().getPaymentSchedule().getCdInterest(term);
        } else {
            return BigDecimal.ZERO;
        }
	}
	
	public BigDecimal getMonthlyCharge()
	{
		return BigDecimal.ZERO; 
	}
	
	public BigDecimal getThreshold()
	{
		return BigDecimal.ZERO; 
	}
}
