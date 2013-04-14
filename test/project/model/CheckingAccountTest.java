package project.model;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;

public final class CheckingAccountTest {
    private CheckingAccount account;
    private static User user1;
    
    @BeforeClass
    public static void setUpClass() throws InvalidInputException {
    	user1 = new User("user1FirstName", "user1LastName", null, 123456789, "user1Email");
        Bank.getInstance().addUser("user1", user1);
    }

    @Before
    public void setUp() {
        account = new CheckingAccount();
        TestUtil.assertEquals(BigDecimal.ZERO, account.getBalance());
        user1.addAccount(account);
    }

    @After
    public void tearDown() {
        account = null;
    }

    /**  2234.56 deposit
     *   123.45  withdrawal
     *    balance should be 2111.11
     *    signal passage of one month, balance should remain at 2111.11 since balance is above $2000, avoiding monthly checking charge
     */
    @Test 
    public void testWithdrawSufficient() throws InvalidInputException, InsufficientFundsException {
        account.deposit(new BigDecimal("2234.56"));
        TestUtil.assertEquals(2234.56, account.getBalance());
        account.withdraw(new BigDecimal("123.45"));
        TestUtil.assertEquals(2111.11, account.getBalance());
        Bank.getInstance().advanceCurrentMonth();
        TestUtil.assertEquals(2111.11, account.getBalance());
    }

    /**  1234.56 deposit
     *   1234.57 withdrawal
     *    balance should be (0.01)
     *    No exceptions should be thrown since balance is above overdraft limit of (50.00)
     */
    @Test
    public void testWithdrawToBelowZeroButAboveODLimit() throws InvalidInputException, InsufficientFundsException {
        account.deposit(new BigDecimal("1234.56"));
        TestUtil.assertEquals(1234.56, account.getBalance());
        account.withdraw(new BigDecimal("1234.57"));
        TestUtil.assertEquals(-0.01, account.getBalance());
    }
    
    @Test(expected = InsufficientFundsException.class)
    public void testWithdrawToBelowZeroAndHitOD() throws InvalidInputException, InsufficientFundsException {
        account.deposit(new BigDecimal("1234.56"));
        TestUtil.assertEquals(1234.56, account.getBalance());
        account.withdraw(new BigDecimal("1234.57"));
        TestUtil.assertEquals(-0.01, account.getBalance());
        account.withdraw(new BigDecimal("1234.57"));
        TestUtil.assertEquals(-40.01, account.getBalance());
        account.withdraw(new BigDecimal("1234.57"));
        TestUtil.assertEquals(-80.01, account.getBalance());
    }

    /**  1234.56 deposit
     *   1334.56 withdrawal
     *    Withdrawal should be denied since balance would go below overdraft limit of (50.00)
     *    balance should be 1194.56 since $40 overdraft fee was assessed
     *    InsufficientFundsException should be thrown
     */    
    @Test(expected = InsufficientFundsException.class)
    public void testWithdrawInsufficient() throws InvalidInputException, InsufficientFundsException {
        account.deposit(new BigDecimal("1234.56"));
        TestUtil.assertEquals(1234.56, account.getBalance());
        account.withdraw(new BigDecimal("1334.56"));
        TestUtil.assertEquals(1194.56, account.getBalance());
    }

    /**  2500.00 deposit
     *   600.00  withdrawal
     *    balance should be 1900.00
     *    signal passage of one month of time, balance should be 1892.00 after $8 monthly checking charge for balance under $2000
     *    signal passage of one month of time, balance should be 1884.00 after $8 monthly checking charge for balance under $2000
     */
    @Test
    public void testCheckingServiceCharge() throws InvalidInputException, InsufficientFundsException {
        account.deposit(new BigDecimal("2500.00"));
        TestUtil.assertEquals(2500.00, account.getBalance());
        account.withdraw(new BigDecimal("600.00"));
        TestUtil.assertEquals(1900.00, account.getBalance());
        Bank.getInstance().advanceCurrentMonth();
        TestUtil.assertEquals(1892.00, account.getBalance());
        Bank.getInstance().advanceCurrentMonth();
        TestUtil.assertEquals(1884.00, account.getBalance());
    }

    /**  100.00 deposit
     *   140.00 withdrawal
     *    balance should be (40.00)
     *    signal passage of one month of time, balance should be 48.00 after $8 monthly checking charge for balance under $2000
     *    signal passage of one month of time, balance should be 56.00 after $8 monthly checking charge for balance under $2000
     */
    @Test
    public void testBlah() throws InvalidInputException, InsufficientFundsException {
        account.deposit(new BigDecimal("100.00"));
        TestUtil.assertEquals(100.00, account.getBalance());
        account.withdraw(new BigDecimal("140.00"));
        TestUtil.assertEquals(-40.00, account.getBalance());
        Bank.getInstance().advanceCurrentMonth();
        TestUtil.assertEquals(-48.00, account.getBalance());
        Bank.getInstance().advanceCurrentMonth();
        TestUtil.assertEquals(-56.00, account.getBalance());
    }

}
