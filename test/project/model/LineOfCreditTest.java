package project.model;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public final class LineOfCreditTest {
    private final BigDecimal creditLim = new BigDecimal(3000);
    private final BigDecimal premiumInt = new BigDecimal(.05);
    private LineOfCredit account;

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

    @Test(expected = LoanCapException.class)
    public void testPastBankLimit() throws InvalidInputException, InsufficientFundsException, LoanCapException {
        tearDown();
        account = new LineOfCredit(new BigDecimal(5000), premiumInt);
    }

    @Test(expected = InvalidInputException.class)
    public void testNegativePremiumInt() throws InvalidInputException, InsufficientFundsException, LoanCapException {
        tearDown();
        account = new LineOfCredit(creditLim, new BigDecimal(-.5));
    }

    @Test
    public void testBasicWithdraw() throws InvalidInputException, InsufficientFundsException {
        account.withdraw(new BigDecimal(123.45));
        TestUtil.assertEquals(-123.45, account.getBalance());
    }

    @Test
    public void testBasicDeposit() throws InvalidInputException, InsufficientFundsException {
        account.withdraw(new BigDecimal(123.45));
        account.deposit(new BigDecimal(123.45));
        TestUtil.assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test(expected = InsufficientFundsException.class)
    public void testExcessWithdraw() throws InvalidInputException, InsufficientFundsException {
        account.withdraw(new BigDecimal("5678.90"));
    }

    @Test(expected = InvalidInputException.class)
    public void testNegativeDeposit() throws InvalidInputException, InsufficientFundsException, LoanCapException {
        account.withdraw(new BigDecimal("300.00"));
        account.deposit(new BigDecimal("-150.00"));
    }

    @Test(expected = InvalidInputException.class)
    public void testNegativeWithdrawal() throws InvalidInputException, InsufficientFundsException, LoanCapException {
        account.withdraw(new BigDecimal("-300.00"));
    }

    @Test(expected = InvalidInputException.class)
    public void testExcessDeposit() throws InvalidInputException, InsufficientFundsException {
        account.deposit(new BigDecimal(1234.56));
    }

    @Test
    public void testBasicCreditSet() throws InvalidInputException, LoanCapException {
        account.setCreditLimit(new BigDecimal(1000));
        TestUtil.assertEquals(1000, account.getCreditLimit());
    }

    @Test(expected = InvalidInputException.class)
    public void testLowerCreditSet() throws InvalidInputException, InsufficientFundsException, LoanCapException {
        account.withdraw(new BigDecimal(500));
        account.setCreditLimit(new BigDecimal(400));
    }

    @Test(expected = InvalidInputException.class)
    public void testNegativeCreditSet() throws InvalidInputException, LoanCapException {
        account.setCreditLimit(new BigDecimal(-500));
    }

    @Test
    public void testClose() throws IllegalArgumentException {
        account.close();
        assertTrue(account.isClosed());
    }

    @Test
    public void testBalanceMinimumPayment() throws InvalidInputException, InsufficientFundsException {
        //Note that, for this test, locpercentpayment is 2% and locfixedpayment is 50$
        account.withdraw(new BigDecimal(10));
        BigDecimal min = account.getMinimumPayment();
        TestUtil.assertEquals(10, min);
    }

    @Test
    public void testZeroMonthlyCharge() {
        TestUtil.assertEquals(BigDecimal.ZERO, account.getMonthlyCharge());
    }

    @Test
    public void testPositiveMonthlyCharge() throws InvalidInputException, InsufficientFundsException {
        account.withdraw(new BigDecimal("600.00"));
        TestUtil.assertEquals(account.getPenalty(), account.getMonthlyCharge());
    }

    @Test
    public void testMinDepositPaid() throws InvalidInputException {
        account.deposit(account.getMinimumPayment().multiply(new BigDecimal(-2)));
        TestUtil.assertEquals(BigDecimal.ZERO, account.getMonthlyCharge());
    }

    @Test
    public void testPercentMinimumPayment() throws InvalidInputException, InsufficientFundsException {
        //Note that, for this test, locpercentpayment is 2% and locfixedpayment is 50$
        account.withdraw(new BigDecimal(2800));
        BigDecimal min = account.getMinimumPayment();
        TestUtil.assertEquals(56, min);
    }

    @Test
    public void testFixedMinimumPayment() throws InvalidInputException, InsufficientFundsException {
        //Note that, for this test, locpercentpayment is 2% and locfixedpayment is 50$
        account.withdraw(new BigDecimal(550));
        BigDecimal min = account.getMinimumPayment();
        TestUtil.assertEquals(50, min);
    }
}
