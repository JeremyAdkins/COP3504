package project.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class CertificateOfDeposit extends Account {
    private final CdTerm term;

    private int monthsElapsed;

    public CertificateOfDeposit(CdTerm term, BigDecimal amount) {
        this.term = term;
        this.monthsElapsed = 0;

        BigDecimal minimumAmount = Bank.getInstance().getPaymentSchedule().getCdMinimum();
        if (minimumAmount.compareTo(amount) > 0) {
            throw new IllegalArgumentException();
        } else {
            super.applyTransaction(amount, Transaction.Type.DEPOSIT);
        }
    }

	public CdTerm getTerm() {
		return term;
	}
	
	public int getMonthsElapsed() {
		return monthsElapsed;
	}

    @Override
    public Transaction deposit(BigDecimal amount) {
        throw new UnsupportedOperationException();
    }

	public Transaction withdraw(BigDecimal amount) throws OverdraftException {
		BigDecimal fee = getInterestRate().divide(new BigDecimal(2), 4, RoundingMode.HALF_EVEN).multiply(getBalance());
		BigDecimal newBalance = getBalance().subtract(amount).subtract(fee);
        if (monthsElapsed < term.getLength()) {
            // minimum balances apply, check for them
            BigDecimal minimumBalance = Bank.getInstance().getPaymentSchedule().getCdMinimum();
            if (newBalance.compareTo(minimumBalance) < 0) {
                throw new IllegalArgumentException("cannot withdraw to below minimum balance; note that the penalty is " + fee);
            }
        }
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            // TODO Do we want to use an OverdraftException, or something tailored for the CoD class?
            throw new OverdraftException();
        }
		return super.withdraw(amount);
	}

    @Override
    protected void doPayments() throws OverdraftException {
        if (getBalance().compareTo(BigDecimal.ZERO) == 0) {
            close();
        }
        super.doPayments();
        monthsElapsed += 1;
    }
	
	public BigDecimal getInterestRate() {
		if (monthsElapsed < term.getLength()) {
            return Bank.getInstance().getPaymentSchedule().getCdInterest(term);
        } else {
            return BigDecimal.ZERO;
        }
	}
	
	public BigDecimal getMonthlyCharge() {
		return BigDecimal.ZERO; 
	}
	
	public BigDecimal getThreshold() {
		return BigDecimal.ZERO; 
	}
}
