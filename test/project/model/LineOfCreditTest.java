package project.model;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
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
	BigDecimal creditLim = new BigDecimal(600); 
	BigDecimal premiumInt = new BigDecimal(.05); 
	LineOfCredit account; 
	 @Before
	    public void setUp() throws LoanCapException{
	        account = new LineOfCredit(creditLim, premiumInt);
	        TestUtil.assertEquals(BigDecimal.ZERO, account.getBalance());
	        TestUtil.assertEquals(creditLim, account.getCreditLimit());
	    }

	    @After
	    public void tearDown() {
	    	Bank.getInstance().returnLoan(account.getCreditLimit());
	        account = null;
	    }
	    
	  //>1
	    @Test
	    public void pastBankLimit() throws LoanCapException, InsufficientFundsException, OverdraftException{
	    	account = new LineOfCredit(new BigDecimal(2000), premiumInt); 
	    }
	    
	  //>2
	    @Test
	    public void negPremiumInt() throws LoanCapException, InsufficientFundsException, OverdraftException{
	    	account = new LineOfCredit(creditLim, new BigDecimal(-.5)); 
	    	TestUtil.assertEquals(0.5, account.getInterestPremium()); 
	    }
	    
	  //>3
	    @Test
	    public void basicWithdraw() throws InsufficientFundsException, OverdraftException{
	    	account.withdraw(new BigDecimal(123.45));
	    	TestUtil.assertEquals(creditLim.multiply(new BigDecimal(-1)).subtract(new BigDecimal(123.45)), account.getBalance()); 
	    }
	    
	  //>4
	    @Test
	    public void basicDeposit() throws InsufficientFundsException, OverdraftException{
	    	account.withdraw(new BigDecimal(123.45)); 
	    	account.deposit(new BigDecimal(123.45));
	    	TestUtil.assertEquals(BigDecimal.ZERO, account.getBalance()); 
	    }
	    
	  //>5
	    @Test (expected = OverdraftException.class)
	    public void excessWithdraw() throws InsufficientFundsException, OverdraftException{
	    	account.withdraw(new BigDecimal(1234.56)); 
	    }
	    
	  //>6
	    //Tests both withdraw() AND deposit() for negative input values
	    @Test
	    public void negWithdrawDeposit() throws LoanCapException, OverdraftException, InsufficientFundsException{
	    	BigDecimal expectedDP = account.getBalance().subtract(account.getCreditLimit().divide(new BigDecimal(4)));
	    	BigDecimal expectedWD = account.getBalance().add(account.getCreditLimit().divide(new BigDecimal(2)));
	    	
	    	//Dividing by negative 2 will make our withdrawal 'amount' negative, withdraw() should ignore the negative
	    	Transaction tran = account.withdraw(account.getCreditLimit().divide(new BigDecimal(-2))); 
	    	TestUtil.assertEquals(expectedWD, tran.getAmount()); 
	    	
	    	//The transaction value should also be positive
	    	Transaction trans = account.deposit(account.getCreditLimit().divide(new BigDecimal(-4))); 
	    	TestUtil.assertEquals(expectedDP, trans.getAmount()); 
	    }
	    
	  //>7
	    @Test (expected = IllegalArgumentException.class)
	    public void excessDeposit() throws InsufficientFundsException, OverdraftException{
	    	account.deposit(new BigDecimal(1234.56)); 
	    }
	    
	  //>8
	    @Test
	    public void basicCreditSet() throws LoanCapException, InsufficientFundsException, OverdraftException{
	    	account.setCreditLimit(new BigDecimal(1000)); 
	    	TestUtil.assertEquals(1000, account.getCreditLimit()); 
	    }
	    
	  //>9
	    @Test (expected = IllegalArgumentException.class)
	    public void lowerCreditSet() throws LoanCapException, InsufficientFundsException, OverdraftException{
	    	account.withdraw(new BigDecimal(500)); 
	    	account.setCreditLimit(new BigDecimal(400)); 
	    }
	    
	  //>10
	    @Test
	    public void negativeCreditSet() throws LoanCapException, InsufficientFundsException, OverdraftException{
	    	account.setCreditLimit(new BigDecimal(-500)); 
	    }
	    
	  //>11
	    @Test
	    public void closedTest()throws IllegalArgumentException {
	    	account.close(); 
	    	if(!account.isClosed())
	    	{
	    		throw new IllegalArgumentException(); 
	    	}
	    }
	    
	  //>12
	    @Test
	    public void minimumPaymentTest() {
	    	//Note that, for this test, locpercentpayment is 2% and locfixedpayment is 50$
	    	BigDecimal min = account.getMinimumPayment(); 
	    	TestUtil.assertEquals(-50, min); 
	    }
	    
	  //>13
	    @Test
	    public void monthlyChargeTest() {
	    	TestUtil.assertEquals(account.getPenalty(), account.getMonthlyCharge()); 
	    }
	    
	  //>14
	    @Test
	    public void minDepositPaidTest() {
	    	account.deposit(account.getMinimumPayment().multiply(new BigDecimal(-2))); 
	    	TestUtil.assertEquals(BigDecimal.ZERO, account.getMonthlyCharge()); 
	    }
}
