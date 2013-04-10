package cop3504.project;

import java.math.BigDecimal;


public class SavingsAccountTest {

	public static void main(String[] args) throws OverdraftException {
		Account acc = new SavingsAccount();
		System.out.println(acc.getBalance());
		acc.deposit(new BigDecimal(100.25));
		System.out.println(acc.getBalance());
		acc.withdraw(new BigDecimal(50));
		System.out.println(acc.getBalance());
		acc.doPayments();
		System.out.println(acc.getBalance());
                System.out.println(acc.getAccountType());
	}

}
