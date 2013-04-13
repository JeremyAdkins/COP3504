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
     * Fee:      -005.00
     * Deposit:  +100.00
     * Withdraw: -050.55
     * Balance:  +044.45
     */
    @Test
    public void testDoPayment() throws InvalidInputException, InsufficientFundsException {
    	Transaction testRepeat1 = new Transaction(Transaction.Type.DEPOSIT, new BigDecimal(100.00));
    	account.addRepeatingPayment(testRepeat1);
    	Transaction testRepeat2 = new Transaction(Transaction.Type.WITHDRAWAL, new BigDecimal(50.55));
    	account.addRepeatingPayment(testRepeat2);
    	account.doPayments();
    	TestUtil.assertEquals(44.45, account.getBalance());
    }
}
