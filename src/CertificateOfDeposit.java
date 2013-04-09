import hw1.DateTime;
import hw1.Time;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CertificateOfDeposit extends Account {
private CdTerm term; 
private int monthsElapsed;

	public CdTerm getTerm() {
		return term;
	}
	
	//TODO implement this in getInterestRate, or get rid of it
	public int getMonthsElapsed() {
		return monthsElapsed;
	}

	public Transaction withdraw(BigDecimal amount) throws OverdraftException
	{
		//TODO Do we want the interest charge to occur before or after the withdrawal finishes?
		BigDecimal fee = getInterestRate().divide(new BigDecimal(2),4,RoundingMode.HALF_EVEN).multiply(getBalance());
		super.applyFee(fee); 
		if(getBalance().subtract(amount).compareTo(Bank.getInstance().getPaymentSchedule().getCdMinimum())<=0)
		{
			close(); 
			throw new OverdraftException();//TODO Do we want to use an OverdraftException, or something tailored for the CoD class? 
		}
		else{
		return super.withdraw(amount);
		}
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
