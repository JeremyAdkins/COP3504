package cop3504.project;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class Account {
	private int accountNumber;//CHECK LATER
	private boolean closed;
	private BigDecimal balance;
	private List<Transaction> history;
	private Set<Transaction> repeatingPayments;
	
	protected Account(){
		this.accountNumber = Bank.getInstance().assignAccountNumber();
		this.closed = false;
		this.balance = new BigDecimal(0);
		this.history = new ArrayList<Transaction>();
		this.repeatingPayments = new HashSet<Transaction>();
	}
        
        enum Type {
		SAVINGS_ACCOUNT, CD_ACCOUNT, CHECKING_ACCOUNT, LOC_ACCOUNT, LOAN_ACCOUNT;
	}
	
	Type getAccountType(){
		for(Type x : Type.values()){
			if(x.toString().replace("_", "").compareToIgnoreCase(this.getClass().getSimpleName().replace(" ", ""))==0) return x;
		}
		return null;
	}
	public final boolean isClosed(){
		return closed;
	}
	public final void close(){
		closed = true;
		//TODO PAYMENT SCHEDULE/FRAUDENT CLOSES?
	}
        public final int getAccountNumber(){
            return accountNumber;
        }
	public final BigDecimal getBalance(){
		return balance;
	}
	public Transaction deposit(BigDecimal amount){//CHECK IF ACCOUNT CLOSED
		balance = balance.add(amount);
		Transaction trans = new Transaction(Transaction.Type.DEPOSIT,amount);
		history.add(trans);
		return trans;
	}
	public Transaction withdraw(BigDecimal amount) throws OverdraftException {
		balance = balance.subtract(amount);
		Transaction trans = new Transaction(Transaction.Type.WITHDRAWAL,amount);
		history.add(trans);
		return trans;
	}
	public final Transaction applyFee(BigDecimal amount){
		balance = balance.subtract(amount);
		Transaction trans = new Transaction(Transaction.Type.FEE,amount);
		history.add(trans);
		return trans;
	}
	public final List<Transaction> getHistory(){
		return new ArrayList<Transaction> (history);
	}
	public final Set<Transaction> getRepeatingPayments(){
		return new HashSet<Transaction> (repeatingPayments);
	}
	public final void addRepeatingPayment(Transaction payment){
		switch (payment.getType()) {
		case DEPOSIT:
		case WITHDRAWAL:
			repeatingPayments.add(payment);
			break;
		default:
			throw new IllegalArgumentException("repeating payments can only be deposits or withdrawals");
		}
	}
	public final void removeRepeatingPayment(Transaction payment){
		repeatingPayments.remove(payment);
	}
	void doPayments() throws OverdraftException{//TODO Check order
		//MONTHLY EXIST FEE
		if(balance.compareTo(getThreshold())<0){
			applyFee(getMonthlyCharge());
		}
		//INTEREST
		BigDecimal interest = balance.multiply(getInterestRate().divide(new BigDecimal(12),4,RoundingMode.HALF_UP));//CHECK IF CORRECT
		balance = balance.add(interest);
		Transaction trans = new Transaction(Transaction.Type.INTEREST,interest);
		history.add(trans);
		//REPEATING PAYMENTS
		for(Transaction payment:repeatingPayments){
			if(payment.getType()==Transaction.Type.DEPOSIT){
				deposit(payment.getAmount());
			}
		}
		for(Transaction payment:repeatingPayments){
			if(payment.getType()==Transaction.Type.WITHDRAWAL){
				withdraw(payment.getAmount());
			}
		}
	}
	protected abstract BigDecimal getInterestRate();
	protected abstract BigDecimal getMonthlyCharge();
	protected abstract BigDecimal getThreshold();
}
