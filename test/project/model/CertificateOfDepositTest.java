package project.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

//Test ideas 
/* Test withdraw(not matured, penalty) and deposit
 * Test a withdraw when matured 
 * Test a withdraw that goes to the min and charges a fee
 * Test that interest is not accumulated when matured
 * Test that interest IS accumulated when not matured
 * NOTE: CdMin for this test is 500 and interest is always 5% annual 
 */
public final class CertificateOfDepositTest {
	private CertificateOfDeposit account;
    private BigDecimal basicBalance = new BigDecimal("600.00");

    @Before
    public void setUp() throws InvalidInputException {
        account = new CertificateOfDeposit(basicBalance, CertificateOfDeposit.Term.ONE_YEAR);
        TestUtil.assertEquals(basicBalance, account.getBalance());
    }

    @After
    public void tearDown() {
        account = null;
    }

    @Test
    public void testWithdrawNotMature() throws InvalidInputException, InsufficientFundsException {
        account.withdraw(new BigDecimal("55.55"));
        TestUtil.assertEquals(536.95, account.getBalance());
    }

    @Test(expected = InsufficientFundsException.class)
    public void testWithdrawToMin() throws InvalidInputException, InsufficientFundsException {
        account.withdraw(new BigDecimal(100));
    }

    @Test
    public void testWithdrawMature() throws InvalidInputException, InsufficientFundsException {
        account.monthsElapsed = 12;
        account.withdraw(new BigDecimal(123.456));
        TestUtil.assertEquals(476.544, account.getBalance());
    }

    @Test
    public void testInterestMature() throws InvalidInputException, InsufficientFundsException {
        account.monthsElapsed = 12;
        account.doPayments();
        TestUtil.assertEquals(basicBalance, account.getBalance());
    }

    @Test
    public void testInterestNotMature() throws InvalidInputException, InsufficientFundsException {
        account.monthsElapsed = 12;
        account.doPayments();
        BigDecimal finalBalance = basicBalance.add(basicBalance.multiply(account.getInterestRate()).divide(new BigDecimal(12)));
        TestUtil.assertEquals(finalBalance, account.getBalance());
    }

    @Test
    public void testAdvanceMonths() throws InvalidInputException, InsufficientFundsException {
        int x;
        for(x = 0; x < 12; x++){
            account.doPayments();
        }
        TestUtil.assertEquals(BigDecimal.ZERO, account.getInterestRate());
        assertEquals(x, account.monthsElapsed);
    }
}
