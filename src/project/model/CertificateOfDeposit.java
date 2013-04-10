package project.model;

import java.math.BigDecimal;

public final class CertificateOfDeposit extends Account {
    public static enum Term {
        SIX_MONTHS(6), ONE_YEAR(12), TWO_YEARS(24), THREE_YEARS(36), FOUR_YEARS(48), FIVE_YEARS(60);

        private final int length;

        private Term(int length) {
            this.length = length;
        }

        public int getLength() {
            return length;
        }
    }

    private final Term term;

    private int monthsElapsed;

    public CertificateOfDeposit(Term term, BigDecimal amount) {
        this.term = term;
        this.monthsElapsed = 0;

        BigDecimal minimumAmount = Bank.getInstance().getPaymentSchedule().getCdMinimum();
        if (minimumAmount.compareTo(amount) > 0) {
            throw new IllegalArgumentException();
        } else {
            super.applyTransaction(amount, Transaction.Type.DEPOSIT);
        }
    }

	public Term getTerm() {
		return term;
	}
	
	public int getMonthsElapsed() {
		return monthsElapsed;
	}

    @Override
    public Transaction deposit(BigDecimal amount) {
        throw new UnsupportedOperationException();
    }

    @Override
	public Transaction withdraw(BigDecimal amount) throws OverdraftException {
		BigDecimal fee = getInterestRate().divide(new BigDecimal(2), Bank.MATH_CONTEXT).multiply(getBalance());
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

    @Override
	public BigDecimal getInterestRate() {
		if (monthsElapsed < term.getLength()) {
            return Bank.getInstance().getPaymentSchedule().getCdInterest(term);
        } else {
            return BigDecimal.ZERO;
        }
	}

    @Override
	public BigDecimal getMonthlyCharge() {
		return BigDecimal.ZERO; 
	}

    @Override
	public BigDecimal getThreshold() {
		return BigDecimal.ZERO; 
	}
}
