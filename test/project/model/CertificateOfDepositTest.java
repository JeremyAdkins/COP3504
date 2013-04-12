package project.model;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

//Test ideas 
/* Test withdraw(not matured, penalty) and deposit
 * Test a withdraw when matured 
 * Test a withdraw that goes to the min and charges a fee
 * Test that interest is not accumulated when matured
 * Test that interest IS accumulated when not matured
 * NOTE: CdMin for this test is 500 and interest is always 5% annual 
 */

public class CertificateOfDepositTest {
	 private CertificateOfDeposit account; 
	 private BigDecimal basicBalance = new BigDecimal(600); 
	 
	    @Before
	    public void setUp() throws InvalidInputException {
	        //account = new CertificateOfDeposit(CertificateOfDeposit.Term.ZERO, new BigDecimal(basicBalance);
	        account = new CertificateOfDeposit(CertificateOfDeposit.Term.ONE_YEAR, basicBalance);
	        TestUtil.assertEquals(basicBalance, account.getBalance());
	    }

	    @After
	    public void tearDown() {
	        account = null;
	    }

	    @Test
	    public void testWithdrawNotMature() throws InvalidInputException, InsufficientFundsException {
	    	account = new CertificateOfDeposit(CertificateOfDeposit.Term.ONE_YEAR, new BigDecimal(1234.56));
	        TestUtil.assertEquals(1234.56, account.getBalance());
	        BigDecimal preBalance = account.getBalance(); 
	        account.withdraw(new BigDecimal(123.45));
	        BigDecimal result = account.getBalance(); 
	        BigDecimal expected = new BigDecimal(1111.11);
	        expected = expected.subtract(preBalance.multiply(account.getInterestRate()).divide(new BigDecimal("2"))); 
	        TestUtil.assertEquals(expected, result);
	    }

	    @Test (expected = InsufficientFundsException.class)
	    public void testWithdrawToMin() throws InvalidInputException, InsufficientFundsException {
	        TestUtil.assertEquals(basicBalance, account.getBalance());
	        account.withdraw(new BigDecimal(100));
	        TestUtil.assertEquals(basicBalance, account.getBalance());
	    }
	    
	    @Test
	    public void testWithDrawMature() throws InvalidInputException, InsufficientFundsException {
	    	account = new CertificateOfDeposit(CertificateOfDeposit.Term.ZERO, basicBalance);
	        account.withdraw(new BigDecimal(123.456));
	        TestUtil.assertEquals(476.544, account.getBalance());
	    }
	    
	    @Test
	    public void testInterestMature() throws InvalidInputException, InsufficientFundsException {
	    	account = new CertificateOfDeposit(CertificateOfDeposit.Term.ZERO, basicBalance);
	    	account.doPayments();
	    	TestUtil.assertEquals(basicBalance, account.getBalance());
	    }
	    
	    @Test
	    public void testInterestNotMature() throws InvalidInputException, InsufficientFundsException {
	    	account = new CertificateOfDeposit(CertificateOfDeposit.Term.ZERO, basicBalance);
	    	account.doPayments();
	    	BigDecimal finalBalance = basicBalance.add(basicBalance.multiply(account.getInterestRate()).divide(new BigDecimal(12)));
	    	TestUtil.assertEquals(finalBalance, account.getBalance());
	    }
	}

