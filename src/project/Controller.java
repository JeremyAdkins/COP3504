/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import project.gui.*;
import project.model.*;

import java.math.BigDecimal;
import java.util.*;



/**
 *
 * @author rich
 */


public final class Controller {

    private Bank instance = Bank.getInstance();
    private User currentUser; //the User currently using the platform based on the window that is open. When a user interacts with a Teller, the teller window is the only window open and this would return the Teller.
    private List<AbstractUserWindow> windows = new ArrayList<AbstractUserWindow>(); //the Windows open and (therefore) those which this Controller is responsible for

    public List<AbstractUserWindow> getWindows() {
        return windows;
    }

    public List<AccountTab> getTabs() {
        return tabs;
    }
    private List<AccountTab> tabs = new ArrayList<AccountTab>(); //the tabs open and (therefore) those which this Controller is responsible for

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
        Bank.getInstance().addUser("admin", new User("Billy", "Bob", Calendar.getInstance(), 555555555, "Bob@mail.com"));
        Bank.getInstance().getUser("admin").setRole(User.Role.OPERATIONS_MANAGER);
        newAbstractUserWindow(Bank.getInstance().getUser("admin"));
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
                LoginWindow login = new LoginWindow("Login", true, Controller.this);
                login.setVisible(true);
            }
        });
    }

    /**
     * Opens the appropriate window based on 'user'.
     *
     * @param user sets currentUser
     */
    public void newAbstractUserWindow(User user) {
        currentUser = user;
        windows.clear();

        //create Account Holder Frame
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                AbstractUserWindow userWindow = null;
                if (currentUser.getRole() == null) {
                    userWindow = new AccountHolderFrame(Controller.this);
                } else {
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
                            userWindow = new project.gui.AccountManagerFrame(Controller.this);
                    }
                }
                userWindow.setVisible(true);
                windows.add(userWindow);
            }
        });
    }

    public List<AccountTab> getTabs(AccountHolderFrame parent) {
        tabs.clear();
        for (Account acc : currentUser.getAccounts()) {
            tabs.add(newAccountTab(acc, parent));
        }
        return tabs;
    }

    //helper method for getTabs
    private AccountTab newAccountTab(Account account, AccountHolderFrame parent) {
        AccountTab accTab = new AccountTab(account, parent, this);
        accTab.setVisible(true);
        return accTab;
    }

    
    //Interact with model
    public void withdraw(Account account, String amount) throws InvalidInputException, InsufficientFundsException {
        account.withdraw(new BigDecimal(amount));
        for (AccountTab accTab : tabs) {
            accTab.updateBalanceLabel();
            accTab.updateHistoryTableModel();
        }
        for (AbstractUserWindow w : windows) {
            w.update("setSummaryTableModel");
        }
    }

    public void deposit(Account account, String amount) throws InvalidInputException {
        account.deposit(new BigDecimal(amount));
        for (AccountTab accTab : tabs) {
            accTab.updateBalanceLabel();
            accTab.updateHistoryTableModel();
        }
        for (AbstractUserWindow w : windows) {
            w.update("setSummaryTableModel");
        }
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
    public void createNewUser(String firstName, String lastName, Calendar birthdate, int ssn, String email, String username) throws InvalidInputException {
        Bank.getInstance().addUser(username, new User(firstName, lastName, birthdate, ssn, email));
    }
    /**
     * 
     * @param firstName
     * @param lastName
     * @param birthdate
     * @param ssn
     * @param email
     * @param username 
     */
    public void createNewEmployee(String firstName, String lastName, Calendar birthdate, int ssn, String email, String username, String role) throws InvalidInputException {
        Bank.getInstance().addUser(username, new User(firstName, lastName, birthdate, ssn, email));
        Bank.getInstance().getUser(username).setRole(User.Role.valueOf(role));
    }
    public void addAccountToUser(String account, String username) throws InvalidInputException {
        Bank.getInstance().getUser(username).addAccount(newAccount(account));
    }
    
    public Account newAccount(String type){
        // TODO I'm sure there's a way to make this use strong types
        if (type.equals("Savings")) {
            return new SavingsAccount();
        } else if (type.equals("Checking")) {
            return new CheckingAccount();
//        } else if (type.equals("Certificate of Deposit")) {
//            return new CertificateOfDeposit();
//        } else if (type.equals("Line of Credit")) {
//            return new LineOfCredit();
//        } else if (type.equals("Loan")) {
//            return new Loan();
        }
        throw new IllegalStateException();
    }
    
    public Bank getInstance() {
        this.instance = Bank.getInstance();
        return Bank.getInstance();
    }
    /**
     * Lays out all of the accounts of a certain User
     * @return 
     */
    public Object[][] updateAccountHolderTableView(){
        Set<Account> accounts = currentUser.getAccounts();
        Object[][] summaryTable = new Object[accounts.size()][3];
        int i = 0;
        for (Account account : accounts) {
            summaryTable[i][0] = account.getType();
            summaryTable[i][1] = account.getAccountNumber();
            summaryTable[i][2] = "$"+account.getBalance();
            i++;
        }
        return summaryTable;
    }
    /**
     * Lays out all of the Users and their Accounts
     * @return 
     */
    public Object[][] updateAccountManagerTableView(){
        Collection<User> users = instance.getUsers();
        int accountHolders = 0;
        for (User user : users) {
            if(!user.isActiveCustomer()){
                continue;
            }
            accountHolders++;
        }
        Object[][] AccountManagerTable = new Object[accountHolders][3];
        int i = 0;
        for (User user : users) {
            if(!user.isActiveCustomer()){
                continue;
            }
            AccountManagerTable[i][0] = user.getLastName()+", "+user.getFirstName();
            for(Account account : user.getAccounts()){
                AccountManagerTable[i][1] = account.getType();
                AccountManagerTable[i][2] = account.getBalance();
                        i++;
            }
        }
        return AccountManagerTable;
    }
    /**
     * Lays out all of the Accounts
     * @return 
     */
    public Object[][] updateAccountantTableView(){
        Collection<User> users = instance.getUsers();
        Object[][] AccountantTable = new Object[users.size()][2];
        int i = 0;
        for (User user : users) {
            for(Account account : user.getAccounts()){
                AccountantTable[i][0] = account.getType();
                AccountantTable[i][1] = account.getBalance();
                        i++;
            }
        }
        return AccountantTable;
    }
    
    public Object[][] updateAuditorTableView(){
        return null; //TODO
    }

    public void shutDown(){
        
    }
    
    public void getPaymentSchedule(){
        
    }
    
    public void shiftTime1Month(){
        
    }
    
//    public void shiftTime1Month(){
//        Bank.getInstance().doPayments();
//    }
}
