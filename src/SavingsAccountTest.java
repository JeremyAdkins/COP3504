import java.math.BigDecimal;


public class SavingsAccountTest {

	public static void main(String[] args) {
		Account acc = new SavingsAccount(1);
		System.out.println(acc.getBalance());
		acc.deposit(new BigDecimal(100.25));
		System.out.println(acc.getBalance());
		acc.withdraw(new BigDecimal(50));
		System.out.println(acc.getBalance());
		acc.doPayments();
		System.out.println(acc.getBalance());
	}

}
