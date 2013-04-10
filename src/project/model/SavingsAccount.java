package project.model;

import java.math.BigDecimal;

public final class SavingsAccount extends Account{
	@Override
	public Transaction withdraw(BigDecimal amount) throws OverdraftException {
		if (getBalance().compareTo(amount)>=0) {
			return super.withdraw(amount);
		} else {
            // TODO should be an exception
            return null;
        }
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
