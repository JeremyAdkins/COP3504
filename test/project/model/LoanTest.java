package project.model;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public final class LoanTest {
    private final BigDecimal loanAmount = new BigDecimal(600);
    private final BigDecimal premiumInt = new BigDecimal(.05);
    private Loan account;

    @BeforeClass
    public static void setUpClass() throws InvalidInputException {
        Bank.getInstance().setLoanCap(new BigDecimal("4000.00"));
    }

	@Before
	public void setUp() throws InvalidInputException, LoanCapException{
		account = new Loan(loanAmount, 12, premiumInt);
		TestUtil.assertEquals(loanAmount.negate(), account.getBalance());
	    TestUtil.assertEquals(52.75, account.getMinimumPayment());
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
	public void testBasicDeposit() throws InvalidInputException {
		account.deposit(new BigDecimal(300)); 
		TestUtil.assertEquals(300, account.getBalance().negate()); 
	}
	
	@Test
	public void testMinNotPaid() throws InvalidInputException {
		//Note that the minimum payment for this test is 50$
		account.deposit(new BigDecimal(40)); 
		TestUtil.assertEquals(account.getMonthlyCharge(), account.getPenalty()); 
	}
	
	@Test
	public void testMinPaid() throws InvalidInputException {
		account.deposit(new BigDecimal(100)); 
		TestUtil.assertEquals(BigDecimal.ZERO, account.getMonthlyCharge()); 
	}
	
	@Test
	public void testAutomaticClosure() throws InsufficientFundsException, InvalidInputException {
		account.deposit(new BigDecimal(600));
		account.doPayments(); 
		assertTrue(account.isClosed()); 
	}
	
	@Test(expected = InvalidInputException.class)
	public void testNegativeTerm() throws InvalidInputException, LoanCapException {
		account = new Loan(new BigDecimal(500), -1, premiumInt);
	}
	
	@Test(expected = LoanCapException.class)
	public void testExceedLoanCap() throws InvalidInputException, LoanCapException {
		account = new Loan(new BigDecimal(5000), 12, premiumInt);
	}
}
