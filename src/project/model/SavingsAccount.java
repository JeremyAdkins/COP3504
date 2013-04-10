package project.model;

import java.math.BigDecimal;


public class SavingsAccount extends Account{

	@Override
	public Transaction withdraw(BigDecimal amount) throws OverdraftException {
		if(getBalance().compareTo(amount)>=0){
			return super.withdraw(amount);
		}
		else{return null;}//TODO FLAG EXCEPTION
	}
	
	@Override
	protected BigDecimal getInterestRate() {
		return Bank.getInstance().getPaymentSchedule().getSavingsInterest();
	}

	@Override
	protected BigDecimal getMonthlyCharge() {
		return Bank.getInstance().getPaymentSchedule().getSavingsCharge();
	}

	@Override
	protected BigDecimal getThreshold() {
		return Bank.getInstance().getPaymentSchedule().getSavingsThreshold();
	}

}
