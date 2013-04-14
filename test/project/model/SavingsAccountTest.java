package project.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public final class SavingsAccountTest {
    private SavingsAccount account;

    @Before
    public void setUp() {
        account = new SavingsAccount();
        TestUtil.assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @After
    public void tearDown() {
        account = null;
    }

    /*
     * Deposit:  +1234.56
     * Withdraw: -0123.45
     * Balance:  +1111.11
     */
    @Test
    public void testWithdrawSufficient() throws InvalidInputException, InsufficientFundsException {
        account.deposit(new BigDecimal("1234.56"));
        TestUtil.assertEquals(1234.56, account.getBalance());
        account.withdraw(new BigDecimal("123.45"));
        TestUtil.assertEquals(1111.11, account.getBalance());
    }

    /*
     * Deposit:  +1234.56
     * Withdraw: -1234.57 (InsufficientFundsException)
     */
    @Test(expected = InsufficientFundsException.class)
    public void testWithdrawInsufficient() throws InvalidInputException, InsufficientFundsException {
        account.deposit(new BigDecimal("1234.56"));
        TestUtil.assertEquals(1234.56, account.getBalance());
        account.withdraw(new BigDecimal("1234.57"));
    }

    /*
     * Deposit:  +100.00
     * Withdraw: -050.55
     * Fee:      -005.00
     * Interest: +000.07
     * Balance:  +044.52
     */
    @Test
    public void testDoPayment() throws InvalidInputException, InsufficientFundsException {
    	account.addRepeatingDeposit("deposit", new BigDecimal("100.00"));
    	account.addRepeatingWithdrawal("withdrawal", new BigDecimal("50.55"));
    	account.doPayments();
    	TestUtil.assertEquals(44.52, account.getBalance());
    }
}
