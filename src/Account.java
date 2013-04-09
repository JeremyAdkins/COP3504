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

	public final boolean isClosed() {
		return closed;
	}

	public final void close() {
		closed = true;
		//TODO PAYMENT SCHEDULE/FRAUDENT CLOSES?
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

	protected Transaction applyTransaction(BigDecimal amount, Transaction.Type type) {
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
