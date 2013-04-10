package project.model;

import java.math.BigDecimal;

public class CheckingAccount extends Account {
	
	@Override
	public Transaction withdraw(BigDecimal amount) throws OverdraftException {
		BigDecimal overdraftLimit = Bank.getInstance().getPaymentSchedule()
				.getOverdraftLimit();
		if (amount.compareTo(this.getBalance().add(overdraftLimit)) < 0) {
			return super.withdraw(amount);
		} else {
			applyFee(Bank.getInstance().getPaymentSchedule().getOverdraftFee());
			throw new OverdraftException();
		}
	}

	@Override
	public BigDecimal getInterestRate() {
		return BigDecimal.ZERO;
	}

	@Override
	protected BigDecimal getMonthlyCharge() {
		return Bank.getInstance().getPaymentSchedule().getCheckingCharge();
	}

	@Override
	protected BigDecimal getThreshold() {
		return Bank.getInstance().getPaymentSchedule().getCheckingThreshold();
	}
}
