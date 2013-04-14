package project.model;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public class LoanTester {
BigDecimal minPay = new BigDecimal(50); 
BigDecimal premiumInt = new BigDecimal(.05);
BigDecimal loanAmount = new BigDecimal(600); 
Loan account; 
	
@BeforeClass
public static void setUpClass() throws InvalidInputException {
	Bank.getInstance().setLoanCap(new BigDecimal("4000.00"));
}

	@Before
	public void setUp() throws InvalidInputException, LoanCapException{
		account = new Loan(loanAmount, minPay, loanAmount); 
		TestUtil.assertEquals(new BigDecimal(-600), account.getBalance());
	    TestUtil.assertEquals(minPay, account.getMinimumPayment());
	}
	
	@After
	public void tearDown() throws InvalidInputException{
		if (account != null && !account.isClosed()) {
    		account.deposit(account.getBalance().negate());
	    	account.close();
    	}
    	account = null;
	}
	
	@Test
	public void basicDeposit() throws InvalidInputException {
		account.deposit(new BigDecimal(300)); 
		TestUtil.assertEquals(300, account.getBalance().negate()); 
	}
	
	@Test
	public void testMinNotPayed() throws InvalidInputException {
		//Note that the minimum payment for this test is 50$
		account.deposit(new BigDecimal(40)); 
		TestUtil.assertEquals(account.getMonthlyCharge(), account.getPenalty()); 
	}
	
	@Test
	public void testMinPayed() throws InvalidInputException {
		account.deposit(new BigDecimal(100)); 
		TestUtil.assertEquals(BigDecimal.ZERO, account.getMonthlyCharge()); 
	}
	
	@Test
	public void testClosed() throws InsufficientFundsException, InvalidInputException {
		account.deposit(new BigDecimal(600));
		account.doPayments(); 
		assertTrue(account.isClosed()); 
	}
	
	@Test (expected = InvalidInputException.class)
	public void testNegConstructor() throws InvalidInputException, LoanCapException {
		account = new Loan(new BigDecimal(500), new BigDecimal(-50), new BigDecimal(.07)); 
	}
	
	@Test (expected = LoanCapException.class)
	public void testExceedLoanCap() throws InvalidInputException, LoanCapException {
		account = new Loan(new BigDecimal(5000), minPay, premiumInt); 
	}
}
