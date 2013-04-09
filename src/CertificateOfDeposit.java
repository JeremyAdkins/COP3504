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
		BigDecimal fee = getInterestRate().divide(new BigDecimal(2),4,RoundingMode.HALF_UP).multiply(getBalance());
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
	
	public BigDecimal getInterestRate()
	{	
		//Compares the length that this account has been active (from it's first transaction to current time),
		//and if that time is greater that term, the interest returned must be zero. 
		DateTime current = new DateTime(); 
		Time passed = current.subtract(this.getHistory().get(0).getTimestamp()); 
		Time cdLength = term.getLength(); 
		if(passed.getAbsoluteLength() > cdLength.getAbsoluteLength()){
			return BigDecimal.ZERO; 
		}
		return Bank.getInstance().getPaymentSchedule().getCdInterest(term); 
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
