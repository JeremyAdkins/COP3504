package project.model;

import java.math.BigDecimal;
import java.util.*;

public abstract class Account {
    /*
     * Account types have logical meaning, because they are used for the accountant's summary statistics. There's no way
     * to accomplish what this enum does through polymorphism alone. We also have to account for the possibility of
     * negative interest, but only on some account types.
     */
    public static enum Type {
        SAVINGS("savings", false), CHECKING("checking", false), CD("CD", false), LOAN("loan", true), LINE_OF_CREDIT("line of credit", true);

        private final String displayName;

        private final boolean isLoan;

        private Type(String displayName, boolean isLoan) {
            this.displayName = displayName;
            this.isLoan = isLoan;
        }

        public boolean isLoan() {
            return isLoan;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

	private final int accountNumber;

	private boolean closed;

	private BigDecimal balance;

	private final List<Transaction> history;

	private final Set<Transaction> repeatingPayments;

	protected Account() {
		this.accountNumber = Bank.getInstance().assignAccountNumber();
		this.closed = false;
		this.balance = new BigDecimal(0);
		this.history = new ArrayList<Transaction>();
		this.repeatingPayments = new HashSet<Transaction>();
	}

    public abstract Type getType();

    public final int getAccountNumber() {
        return accountNumber;
    }

    /**
     * Returns {@code true} if this account is closed, {@code false} otherwise. A closed account always has a balance of
     * zero, and it is illegal to perform any transactions on it; in addition, no interest or fees apply to a closed
     * account. There are two ways for an open account to become closed:
     * <ul>
     *     <li>It can be manually closed by an employee, which will cause {@link #close()} to be invoked.</li>
     *     <li>For certain account types, namely, loans and CDs, the account is closed if the balance is zero at the end
     *     of the month. In this case, {@link #close()} is invoked during {@link #doPayments()}.</li>
     * </ul>
     *
     * @return whether or not this account is closed
     */
	public final boolean isClosed() {
		return closed;
	}

    /**
     * Closes this account, making it impossible for further transactions to apply to it. The account balance must be
     * zero for this method to be invoked.
     *
     * @throws IllegalStateException if the balance is nonzero
     * @see #isClosed()
     */
	public void close() {
        if (getBalance().round(Bank.MATH_CONTEXT).compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("cannot close account with nonzero balance");
        }
		closed = true;
	}

	public final BigDecimal getBalance() {
		return balance;
	}

	public Transaction deposit(BigDecimal amount) throws InvalidInputException {
		return applyTransaction(amount, Transaction.Type.DEPOSIT);
	}

	public Transaction withdraw(BigDecimal amount) throws InvalidInputException, InsufficientFundsException {
		return applyTransaction(amount, Transaction.Type.WITHDRAWAL);
	}

	public final Transaction applyFee(BigDecimal amount) throws InvalidInputException {
		return applyTransaction(amount, Transaction.Type.FEE);
	}

    /**
     * Constructs, applies, and returns a new {@link Transaction} of amount {@code amount} and type {@code type}. The
     * provided amount must be non-negative; whether it is added or subtracted to the account balance is determined by
     * the transaction type, specifically the {@link Transaction.Type#isPositive()} method.
     *
     * @param amount the monetary amount associated with the transaction
     * @param type the type of the transaction
     * @return a {@code Transaction} object, already added to history, reflecting the change requested; or {@code null},
     *         if {@code amount} was zero
     * @throws InvalidInputException if {@code amount} is negative
     * @throws IllegalStateException if the account is {@link #isClosed() closed}
     */
	protected final Transaction applyTransaction(BigDecimal amount, Transaction.Type type) throws InvalidInputException {
        if (closed) {
            throw new IllegalStateException("cannot apply a transaction to a closed account");
        } else if (amount.round(Bank.MATH_CONTEXT).compareTo(BigDecimal.ZERO) == 0) {
            // if the amount is zero, we don't want to add anything to the transaction history
            return null;
        } else if (amount.compareTo(BigDecimal.ZERO) < 0) {
            // amounts should never be negative, it's worth having the check
            throw new InvalidInputException(amount, "the amount for a transaction must be positive");
        }

		if (type.isPositive()) {
            balance = balance.add(amount);
        } else {
            balance = balance.subtract(amount);
        }
		Transaction trans = new Transaction(type, amount);
		history.add(trans);
		return trans;
	}

	public final List<Transaction> getHistory() {
		return Collections.unmodifiableList(history);
	}

	public final Set<Transaction> getRepeatingPayments() {
		return Collections.unmodifiableSet(repeatingPayments);
	}

	public final void addRepeatingPayment(Transaction payment) {
        if (payment.getType().canRepeat()) {
            repeatingPayments.add(payment);
        } else {
            throw new IllegalArgumentException("repeating payments can only be deposits or withdrawals");
        }
	}

	public final void removeRepeatingPayment(Transaction payment) {
		repeatingPayments.remove(payment);
	}

	protected void doPayments() throws InvalidInputException, InsufficientFundsException {
        // don't apply any payments to an account that's closed
        if (closed) {
            return;
        }

		// monthly existence fee, or minimum payment penalty, as appropriate
		if (balance.compareTo(getThreshold()) < 0) {
			applyFee(getMonthlyCharge());
		}

		// interest
        BigDecimal interest = balance.multiply(getInterestRate().divide(new BigDecimal(12), Bank.MATH_CONTEXT));
        if (getType().isLoan()) {
            if (getBalance().compareTo(BigDecimal.ZERO) > 0) {
                throw new IllegalStateException("loan balance should never be positive");
            }
            // negate the interest because all transaction amounts must be positive
            applyTransaction(interest.negate(), Transaction.Type.LOAN_INTEREST);
        } else if (getBalance().compareTo(BigDecimal.ZERO) >= 0) {
            // only apply interest on non-loans if the balance (and therefore, the interest) is positive
		    applyTransaction(interest, Transaction.Type.INTEREST);
        }

		// repeating payments
		for (Transaction payment : repeatingPayments) {
			if (payment.getType() == Transaction.Type.DEPOSIT) {
				deposit(payment.getAmount());
			}
		}
		for (Transaction payment : repeatingPayments) {
			if (payment.getType() == Transaction.Type.WITHDRAWAL) {
				withdraw(payment.getAmount());
			}
		}
	}

	public abstract BigDecimal getInterestRate();

	protected abstract BigDecimal getMonthlyCharge();

	protected abstract BigDecimal getThreshold();

    @Override
    public String toString() {
        return String.format("%s (x%04d)", getType(), accountNumber % 10000);
    }
}
