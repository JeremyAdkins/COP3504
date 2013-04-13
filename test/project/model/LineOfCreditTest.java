package project.model;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

//Test ideas
/*
 * >1 Test that you cannot create a LoC that goes over the bank's credit limit
 * >2 Test that interest rate premium cannot be less than zero
 * >3 Basic withdraw test
 * >4 Basic deposit test
 * >5 Withdraw an amount greater than the credit limit
 * >6 Withdraw/Deposit a negative value
 * >7 Deposit greater than the account's debt
 * >8 Set a new credit limit 
 * >9 Set a new credit limit below the current balance
 * >10 Set a new credit limit that is positive/negative
 * >11 Test that the account closes when close() is called 
 * >12 Test that the biggest (most negative) payment is returned by getMinimumPayment()
 * >13 Test that getMonthlyCharge() applies an appropriate fee
 * >14 Test that getMonthlyCharge() does not apply a fee when the minimum payment is met
 */

public class LineOfCreditTest {
	BigDecimal creditLim = new BigDecimal(3000); 
	BigDecimal premiumInt = new BigDecimal(.05); 
	LineOfCredit account;
	
	@BeforeClass
	public static void setUpClass() throws InvalidInputException {
		Bank.getInstance().setLoanCap(new BigDecimal("4000.00"));
	}
	
	 @Before
	    public void setUp() throws InvalidInputException, LoanCapException {
	        account = new LineOfCredit(creditLim, premiumInt);
	        TestUtil.assertEquals(BigDecimal.ZERO, account.getBalance());
	        TestUtil.assertEquals(creditLim, account.getCreditLimit());
	    }

	    @After
	    public void tearDown() throws InvalidInputException {
	    	if (account != null && !account.isClosed()) {
	    		account.deposit(account.getBalance().negate());
		    	account.close();
	    	}
	    	account = null;
	    }
	    
	  //>1
	    @Test(expected = LoanCapException.class)
	    public void pastBankLimit() throws InvalidInputException, InsufficientFundsException, LoanCapException {
	    	tearDown();
	    	account = new LineOfCredit(new BigDecimal(5000), premiumInt); 
	    }
	    
	  //>2
	    @Test(expected = InvalidInputException.class)
	    public void negPremiumInt() throws InvalidInputException, InsufficientFundsException, LoanCapException {
	    	tearDown();
	    	account = new LineOfCredit(creditLim, new BigDecimal(-.5)); 
	    }
	    
	  //>3
	    @Test
	    public void basicWithdraw() throws InvalidInputException, InsufficientFundsException {
	    	account.withdraw(new BigDecimal(123.45));
	    	TestUtil.assertEquals(-123.45, account.getBalance()); 
	    }
	    
	  //>4
	    @Test
	    public void basicDeposit() throws InvalidInputException, InsufficientFundsException {
	    	account.withdraw(new BigDecimal(123.45)); 
	    	account.deposit(new BigDecimal(123.45));
	    	TestUtil.assertEquals(BigDecimal.ZERO, account.getBalance()); 
	    }
	    
	  //>5
	    @Test (expected = InsufficientFundsException.class)
	    public void excessWithdraw() throws InvalidInputException, InsufficientFundsException {
	    	account.withdraw(new BigDecimal("5678.90")); 
	    }
	    
	  //>6
	    @Test(expected = InvalidInputException.class)
	    public void negDeposit() throws InvalidInputException, InsufficientFundsException, LoanCapException {
	    	account.withdraw(new BigDecimal("300.00"));
	    	account.deposit(new BigDecimal("-150.00"));  
	    }
	    
	    @Test(expected = InvalidInputException.class)
	    public void negWithdraw() throws InvalidInputException, InsufficientFundsException, LoanCapException {
	    	account.withdraw(new BigDecimal("-300.00")); 
	    }
	    
	  //>7
	    @Test (expected = InvalidInputException.class)
	    public void excessDeposit() throws InvalidInputException, InsufficientFundsException {
	    	account.deposit(new BigDecimal(1234.56)); 
	    }
	    
	  //>8
	    @Test
	    public void basicCreditSet() throws InvalidInputException, LoanCapException {
	    	account.setCreditLimit(new BigDecimal(1000)); 
	    	TestUtil.assertEquals(1000, account.getCreditLimit()); 
	    }
	    
	  //>9
	    @Test (expected = InvalidInputException.class)
	    public void lowerCreditSet() throws InvalidInputException, InsufficientFundsException, LoanCapException {
	    	account.withdraw(new BigDecimal(500)); 
	    	account.setCreditLimit(new BigDecimal(400)); 
	    }
	    
	  //>10
	    @Test(expected = InvalidInputException.class)
	    public void negativeCreditSet() throws InvalidInputException, LoanCapException {
	    	account.setCreditLimit(new BigDecimal(-500)); 
	    }
	    
	  //>11
	    @Test
	    public void closedTest()throws IllegalArgumentException {
	    	account.close(); 
	    	assertTrue(account.isClosed());
	    }
	    
	  //>12
	    @Test
	    public void testBalanceMinimumPayment() throws InvalidInputException, InsufficientFundsException{
	    	//Note that, for this test, locpercentpayment is 2% and locfixedpayment is 50$
	    	account.withdraw(new BigDecimal(10)); 
	    	BigDecimal min = account.getMinimumPayment(); 
	    	TestUtil.assertEquals(-10, min);
	    }
	    
	  //>13
	    @Test
	    public void monthlyChargeTest() {
	    	TestUtil.assertEquals(account.getPenalty(), account.getMonthlyCharge()); 
	    }
	    
	  //>14
	    @Test
	    public void minDepositPaidTest() throws InvalidInputException {
	    	account.deposit(account.getMinimumPayment().multiply(new BigDecimal(-2))); 
	    	TestUtil.assertEquals(BigDecimal.ZERO, account.getMonthlyCharge()); 
	    }
	    
	  //>15
	    @Test
	    public void testPercentMinimumPayment() throws InvalidInputException, InsufficientFundsException{
	    	//Note that, for this test, locpercentpayment is 2% and locfixedpayment is 50$
	    	account.withdraw(new BigDecimal(2800)); 
	    	BigDecimal min = account.getMinimumPayment(); 
	    	TestUtil.assertEquals(-56, min);
	    }
	    
	    @Test
	    public void testFixedMinimumPayment() throws InvalidInputException, InsufficientFundsException{
	    	//Note that, for this test, locpercentpayment is 2% and locfixedpayment is 50$
	    	account.withdraw(new BigDecimal(550)); 
	    	BigDecimal min = account.getMinimumPayment(); 
	    	TestUtil.assertEquals(-50, min);
	    }
}
