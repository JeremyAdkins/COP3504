public interface Account {
	boolean isClosed();
	
	int getBalance();
	
	void payInterest();
	
	Transaction deposit(int amount) throws NoDepositException, AccountClosedException;
	
	Transaction withdraw(int amount) throws NoWithdrawalException, OverdraftException, AccountClosedException;
	
	List<Transaction> getHistory();
}
