import java.math.BigDecimal;


public class SavingsAccount extends Account{

	public SavingsAccount(int accountNumber) {
		super(accountNumber);
		
	}

	@Override
	public Transaction withdraw(BigDecimal amount){
		if(getBalance().compareTo(amount)>=0){
			return super.withdraw(amount);
		}
		else{return null;}//TODO FLAG EXCEPTION
	}
	
	@Override
	protected BigDecimal getInterestRate() {
		// TODO PAYMENT SCHEDULE
		return new BigDecimal(.25);//HAS TO BE TERMINATING DECIMAL EXPANSION? CHECK ROUNDINGMODE PARAMETER
	}

	@Override
	protected BigDecimal getMonthlyCharge() {
		// TODO PAYMENT SCHEDULE
		return new BigDecimal(8);
	}

	@Override
	protected BigDecimal getThreshold() {
		// TODO PAYMENT SCHEDULE
		return new BigDecimal(1000);
	}

}
