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

        @Override
        public String toString() {
            return name().toLowerCase().replace('_', ' ');
        }
    }

    private final BigDecimal interestRate;

    private final Term term;

    int monthsElapsed;

    public CertificateOfDeposit(BigDecimal amount, Term term) throws InvalidInputException {
        this.term = term;
        this.monthsElapsed = 0;

        BigDecimal minimumAmount = Bank.getInstance().getPaymentSchedule().getCdMinimum();
        if (minimumAmount.compareTo(amount) > 0) {
            throw new InvalidInputException(amount, String.format("CD amount must be at least the minimum, currently $%.2f", minimumAmount));
        } else {
            super.applyTransaction(amount, Transaction.Type.DEPOSIT);
        }

        BigDecimal interestRate = Bank.getInstance().getPaymentSchedule().getSavingsInterest();
        for (Term lowerTerm : Term.values()) {
            interestRate = interestRate.add(Bank.getInstance().getPaymentSchedule().getCdPremium(lowerTerm));
            if (lowerTerm == term) {
                break;
            }
        }
        this.interestRate = interestRate;
    }

    @Override
    public Type getType() {
        return Type.CD;
    }

	public Term getTerm() {
		return term;
	}

    public int getMonthsElapsed() {
        return monthsElapsed;
    }

    public boolean isMature() {
        return (monthsElapsed >= term.getLength());
    }

    @Override
	public Transaction withdraw(BigDecimal amount) throws InvalidInputException, InsufficientFundsException {
		BigDecimal fee = getInterestRate().divide(new BigDecimal(2), Bank.MATH_CONTEXT).multiply(getBalance());
		BigDecimal newBalance = getBalance().subtract(amount).subtract(fee);
        if (!isMature()) {
            // minimum balances apply, check for them
            BigDecimal minimumBalance = Bank.getInstance().getPaymentSchedule().getCdMinimum();
            if (newBalance.compareTo(minimumBalance) < 0) {
                throw new InsufficientFundsException(getBalance().subtract(minimumBalance), amount.add(fee));
            }
        }
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException(getBalance(), amount);
        }
        // if we get to this point, we have sufficient funds, so apply the fee and withdraw
        applyFee(fee);
		return super.withdraw(amount);
	}

    @Override
    protected void doPayments() throws InvalidInputException, InsufficientFundsException {
        if (getBalance().compareTo(BigDecimal.ZERO) == 0) {
            close();
        }
        super.doPayments();
        monthsElapsed += 1;
    }

    @Override
	public BigDecimal getInterestRate() {
        if (isMature()) {
            return BigDecimal.ZERO;
        } else {
            return interestRate;
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
