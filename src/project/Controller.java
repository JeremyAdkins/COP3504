/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import project.gui.*;
import project.gui.util.DollarAmountFormatter;
import project.model.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

public final class Controller {

    private Bank instance = Bank.getInstance();
    private User currentUser; //the User currently using the platform based on the window that is open. When a user interacts with a Teller, the teller window is the only window open and this would return the Teller.
    private List<AbstractUserWindow> windows = new ArrayList<AbstractUserWindow>(); //the Windows open and (therefore) those which this Controller is responsible for
    private List<AccountTab> tabs = new ArrayList<AccountTab>(); //the tabs open and (therefore) those which this Controller is responsible for

    public List<AbstractUserWindow> getWindows() {
        return windows;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public static void main(String[] args) throws InvalidInputException {
        setLookAndFeel();
        new Controller();
    }

    //static methods (called by initial thread)
    private static void setLookAndFeel() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private void initializeBank() throws InvalidInputException {
        if (Bank.getInstance().getUsers().isEmpty()) {
            Bank.getInstance().addUser("operations_manager", new User("Operations", "Manager", Calendar.getInstance(), 555555555, "omanager@bank.com"));
            Bank.getInstance().getUser("operations_manager").setRole(User.Role.OPERATIONS_MANAGER);
            Bank.getInstance().addUser("accountant", new User("Accountant", "Employee", Calendar.getInstance(), 123456789, "accountant@bank.com"));
            Bank.getInstance().getUser("accountant").setRole(User.Role.ACCOUNTANT);
            Bank.getInstance().addUser("auditor", new User("Auditor", "Employee", Calendar.getInstance(), 123456780, "auditor@bank.com"));
            Bank.getInstance().getUser("auditor").setRole(User.Role.AUDITOR);
            Bank.getInstance().addUser("account_manager", new User("Account", "Manager", Calendar.getInstance(), 222222222, "amanager@bank.com"));
            Bank.getInstance().getUser("account_manager").setRole(User.Role.ACCOUNT_MANAGER);
            Bank.getInstance().addUser("teller", new User("Teller", "Employee", Calendar.getInstance(), 777777777, "teller@bank.com"));
            Bank.getInstance().getUser("teller").setRole(User.Role.TELLER);
        }
        newLoginWindow();
    }

    //Opens up the first window and gives control to itself
    public Controller() throws InvalidInputException {
        initializeBank();
    }

    //non-static methods 
    //builds GUI
    public void newLoginWindow() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginWindow login = new LoginWindow("Login", Controller.this);
                login.setVisible(true);
            }
        });
    }

    public void newCustomerUserWindow(User user) {
        currentUser = user;
        windows.clear();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                clearTabs();
                setTabs();
                AbstractUserWindow userWindow = new AccountHolderFrame(Controller.this);
                userWindow.setVisible(true);
                windows.add(userWindow);
            }
        });
    }

    public void newEmployeeUserWindow(User user) {
        currentUser = user;
        windows.clear();
        //create Account Holder Frame
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AbstractUserWindow userWindow;
                switch (currentUser.getRole()) {
                    case TELLER:
                        userWindow = new TellerFrame(Controller.this);
                        break;
                    case ACCOUNTANT:
                        userWindow = new AccountantFrame(Controller.this);
                        break;
                    case OPERATIONS_MANAGER:
                        userWindow = new OperationsManagerFrame(Controller.this);
                        break;
                    case AUDITOR:
                        userWindow = new AuditorFrame(Controller.this);
                        break;
                    case ACCOUNT_MANAGER:
                        userWindow = new AccountManagerFrame(Controller.this);
                        break;
                    default:
                        throw new UnsupportedOperationException("unsupported user role");
                }
                userWindow.setVisible(true);
                windows.add(userWindow);
            }
        });
    }

    private void setTabs() {
        for (Account acc : currentUser.getAccounts()) {
            tabs.add(newAccountTab(currentUser, acc));
        }
    }

    public List<AccountTab> getTabs(AccountHolderFrame parent) {
        return tabs;
    }

    public void clearTabs() {
        tabs.clear();
    }

    public AccountTab newAccountTab(User accountOwner, Account account) {
        AccountTab accTab = new AccountTab(this, accountOwner, account);
        accTab.setVisible(true);
        return accTab;
    }

    //Interact with model
    public void withdraw(Account account, String amount) throws InvalidInputException, InsufficientFundsException {
        account.withdraw(new BigDecimal(amount));
        updateBankDisplay();
    }

    public void deposit(Account account, String amount) throws InvalidInputException {
        account.deposit(new BigDecimal(amount));
        updateBankDisplay();
    }

    public BigDecimal getInterestRate(Account account) {
        return account.getInterestRate().multiply(new BigDecimal("100"));
    }

    /**
     *
     * @param firstName
     * @param lastName
     * @param birthdate
     * @param ssn
     * @param email
     * @return
     */
    public User createNewUser(String firstName, String lastName, Calendar birthdate, int ssn, String email, String username) throws InvalidInputException {
        User user = new User(firstName, lastName, birthdate, ssn, email);
        return user;
    }

    public void addUserToBank(String username, User user) throws InvalidInputException {
        Bank.getInstance().addUser(username, user);
    }

    public void addAccountToUser(User user, Account account) {
        user.addAccount(account);
    }

    public void closeAccount(Account account) {
        account.close();
    }

    public Bank getInstance() {
        this.instance = Bank.getInstance();
        return Bank.getInstance();
    }

    /**
     * Lays out all of the accounts of a certain User
     *
     * @return
     */
    public Object[][] updateAccountHolderTableView(boolean hideClosed) {
        Set<Account> accounts = currentUser.getAccounts();
        //get number of rows
        int rows = 0;
        for (Account account : accounts) {
            if (account.isClosed() && hideClosed) {
                continue;
            }
            rows++;
        }
        //layout table
        Object[][] summaryTable = new Object[rows][3];
        int i = 0;
        for (Account account : accounts) {
            if (account.isClosed() && hideClosed) {
                continue;
            }
            summaryTable[i][0] = account.getType();
            summaryTable[i][1] = account.getAccountNumber();
            BigDecimal balance = account.getType().isLoan() ? account.getBalance().negate() : account.getBalance();
            summaryTable[i][2] = new DollarAmountFormatter().valueToString(balance);
            i++;
        }
        return summaryTable;
    }

    /**
     * Lays out all of the Users and their Accounts
     *
     * @return
     */
    public Object[][] updateAccountManagerTableView() {
        Collection<User> users = instance.getUsers();
        //calculate number of rows
        int rows = 0;
        for (User user : users) {
            if (!user.isActiveCustomer()) {
                rows++;
            }
            for (Account acc : user.getAccounts()) {
                if (!acc.isClosed()) {
                    rows++;
                }
            }
        }
        Object[][] accountManagerTable = new Object[rows][4];
        //display users
        int i = 0;
        for (User user : users) {
            accountManagerTable[i][0] = user;
            StringBuilder formattedSsn = new StringBuilder(String.format("%09d", user.getSsn()));
            formattedSsn.insert(3, "-").insert(6, "-");
            accountManagerTable[i][1] = formattedSsn;
            for (Account account : user.getAccounts()) {
                if (!account.isClosed()) {
                    accountManagerTable[i][2] = account;
                    BigDecimal balance = account.getBalance();
                    if (account.getType().isLoan()) {
                        balance = balance.negate();
                    }
                    accountManagerTable[i][3] = new DollarAmountFormatter().valueToString(balance);
                    i++;
                }
            }
            if (!user.isActiveCustomer()) {
                i++;
            }
        }
        return accountManagerTable;
    }

    /**
     * Generates the table data used in the accountant view.
     *
     * @return the accountant table data
     */
    public Object[][] updateAccountantTableView() {
        Map<String, String> stats = Bank.getInstance().getStatistics();
        Object[][] data = new Object[stats.size()][2];
        Iterator<Map.Entry<String, String>> iterator = stats.entrySet().iterator();
        for (int i = 0; i < stats.size(); i++) {
            Map.Entry<String, String> entry = iterator.next();
            data[i][0] = entry.getKey();
            data[i][1] = entry.getValue();
        }
        return data;
    }

    public void handleException(Component parent, Exception x) {
        JOptionPane.showMessageDialog(parent, x.getMessage(), x.getClass().getName(), JOptionPane.ERROR_MESSAGE);
    }

    public void shutDown() {
        try {
            Bank.saveInstance();
        } catch (IOException iox) {
            JOptionPane.showMessageDialog(null, "Could not serialize Bank using XStream!", iox.getClass().getName(), JOptionPane.WARNING_MESSAGE);
        }
    }

    public Object[][] updateAuditorTableView() {
        Collection<User> users = instance.getUsers();
        //finds the number of users
        int accounts = 0;
        for (User user : users) {
            accounts += user.getAccounts().size();
        }
        //lays out the Table contents
        Object[][] AuditorTable = new Object[accounts][5];
        int i = 0;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                AuditorTable[i][0] = user;
                AuditorTable[i][1] = account;
                BigDecimal balance = account.getType().isLoan() ? account.getBalance().negate() : account.getBalance();
                AuditorTable[i][2] = new DollarAmountFormatter().valueToString(balance);
                boolean hasFraud = false;
                for (Transaction transaction : account.getHistory()) {
                    if (transaction.getFraudStatus() == Transaction.FraudStatus.FLAGGED) {
                        hasFraud = true;
                    }
                }
                AuditorTable[i][3] = hasFraud ? Transaction.FraudStatus.FLAGGED : Transaction.FraudStatus.NOT_FLAGGED;
                AuditorTable[i][4] = user.getRole() == null ? "" : user.getRole();
                i++;
            }
        }
        return AuditorTable;
    }

    public List<TellerAccountTab> getTellerTabs(User user) {
        List<TellerAccountTab> accTabs = new ArrayList<TellerAccountTab>();
        for (Account acc : user.getAccounts()) {
            if (!acc.isClosed()) {
                accTabs.add(new TellerAccountTab(this, acc));
            }
        }
        return accTabs;
    }

    public void updateBankDisplay() {
        for (AbstractUserWindow w : windows) {
            if (w.getClass().getSimpleName().equals("AccountHolderFrame")) {
                AccountHolderFrame accountHolderFrame = (AccountHolderFrame) w;
                accountHolderFrame.setSummaryTableModel();
            }
        }
        for (AccountTab accTab : tabs) {
            accTab.update();
        }
    }

    public void incurTellerFees(Account account) throws InvalidInputException {
        account.applyFee(Bank.getInstance().getPaymentSchedule().getTellerFee());
    }
}
