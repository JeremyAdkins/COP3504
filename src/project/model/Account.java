package project.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Account {
	private final int accountNumber; // TODO where is this used?

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
	public final void close() {
		if (getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException("cannot close account with nonzero balance");
        }
		closed = true;
	}

	public final BigDecimal getBalance() {
		return balance;
	}

	public Transaction deposit(BigDecimal amount) {//CHECK IF ACCOUNT CLOSED
		return applyTransaction(amount, Transaction.Type.DEPOSIT);
	}

	public Transaction withdraw(BigDecimal amount) throws OverdraftException {
		return applyTransaction(amount, Transaction.Type.WITHDRAWAL);
	}

	public final Transaction applyFee(BigDecimal amount) {
		return applyTransaction(amount, Transaction.Type.FEE);
	}

	protected final Transaction applyTransaction(BigDecimal amount, Transaction.Type type) {
        if (closed) {
            throw new IllegalStateException();
        } else if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return null;
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
		return new ArrayList<Transaction>(history);
	}

	public final Set<Transaction> getRepeatingPayments() {
		return new HashSet<Transaction>(repeatingPayments);
	}

	public final void addRepeatingPayment(Transaction payment) {
		switch (payment.getType()) {
			case DEPOSIT:
			case WITHDRAWAL:
				repeatingPayments.add(payment);
				break;
			default:
				throw new IllegalArgumentException("repeating payments can only be deposits or withdrawals");
		}
	}

	public final void removeRepeatingPayment(Transaction payment) {
		repeatingPayments.remove(payment);
	}

	protected void doPayments() throws OverdraftException {//TODO Check order
		// monthly existence fee, or minimum payment penalty, as appropriate
		if (balance.compareTo(getThreshold()) < 0) {
			applyFee(getMonthlyCharge());
		}

		// interest
		BigDecimal interest = balance.multiply(getInterestRate().divide(new BigDecimal(12), 4, RoundingMode.HALF_UP));
		applyTransaction(interest, Transaction.Type.INTEREST);

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

	protected abstract BigDecimal getInterestRate();

	protected abstract BigDecimal getMonthlyCharge();

	protected abstract BigDecimal getThreshold();
}
