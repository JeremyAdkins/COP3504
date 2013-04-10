package project.model;


import java.math.BigDecimal;


public abstract class AbstractLoan extends Account {
	BigDecimal interestPremium;
	BigDecimal depositsToDate;
	
	protected AbstractLoan(BigDecimal interestPremium) {
		this.interestPremium = interestPremium;
		this.depositsToDate = BigDecimal.ZERO;
	}
	
	@Override
	protected void doPayments() throws OverdraftException {
		super.doPayments();
		depositsToDate = BigDecimal.ZERO;
	}
	
	@Override
	protected BigDecimal getInterestRate() {
		return getBaseInterest().add(interestPremium);
	}
	
	public BigDecimal getInterestPremium(){
		return this.interestPremium;
	}
	
	public void setInterestPremium(BigDecimal interestPremium){
		this.interestPremium = interestPremium;
	}
	
	@Override
	protected BigDecimal getMonthlyCharge(){
		BigDecimal minimumPayment = getMinimumPayment();
		if (minimumPayment.compareTo(depositsToDate.negate()) < 0) {
			return getPenalty();
		} else {
			return BigDecimal.ZERO;
		}
	}

	@Override
	protected BigDecimal getThreshold() {
		return BigDecimal.ZERO;
	}
	
	protected abstract BigDecimal getBaseInterest();
	protected abstract BigDecimal getCreditLimit();
	protected abstract BigDecimal getMinimumPayment();
	protected abstract BigDecimal getPenalty();
	
}
