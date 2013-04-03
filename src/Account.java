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
	
	protected Account(int accountNumber){
		this.accountNumber = accountNumber;
		this.closed = false;
		this.balance = new BigDecimal(0);
		this.history = new ArrayList<Transaction>();
		this.repeatingPayments = new HashSet<Transaction>();
	}
	public final boolean isClosed(){
		return closed;
	}
	public final void close(){
		closed = true;
		//TODO PAYMENT SCHEDULE/FRAUDENT CLOSES?
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
	public Transaction withdraw(BigDecimal amount){
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
		repeatingPayments.add(payment);//TODO CHECK FOR INTEREST OR FEE
	}
	public final void removeRepeatingPayment(Transaction payment){
		repeatingPayments.remove(payment);
	}
	void doPayments(){//TODO Check order
		//MONTHLY EXIST FEE
		if(balance.compareTo(getThreshold())<0){
			applyFee(getMonthlyCharge());
		}
		//INTEREST
		BigDecimal interest = balance.multiply(getInterestRate().divide(new BigDecimal(12),RoundingMode.UP));//CHECK IF CORRECT
		balance = balance.add(interest);
		Transaction trans = new Transaction(Transaction.Type.INTEREST,interest);
		history.add(trans);
		//REPEATING PAYMENTS
		for(Transaction payment:repeatingPayments){
			switch(payment.getType()){
			case DEPOSIT: deposit(payment.getAmount()); break;
			case WITHDRAWAL: withdraw(payment.getAmount()); break;
			default: assert false;
			}
		}
	}
	protected abstract BigDecimal getInterestRate();
	protected abstract BigDecimal getMonthlyCharge();
	protected abstract BigDecimal getThreshold();
}
