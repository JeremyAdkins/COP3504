package project.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.lang.String;

import com.thoughtworks.xstream.XStream;

public final class Bank {
    public static final MathContext MATH_CONTEXT = new MathContext(4, RoundingMode.HALF_EVEN);

	private static final File FILE = new File("bank.xml");
	private static Bank instance;
	static {
		if (FILE.exists()) {
			try {
				XStream xstream = new XStream();
				instance = (Bank) xstream.fromXML(new FileReader(FILE));
			} catch (IOException iox) {
				throw new ExceptionInInitializerError("could not read bank.xml");
			}
		} else {
			instance = new Bank();
		}
	}
	
	public static Bank getInstance() {
		return instance;
	}
	
	public static void saveInstance() throws IOException {
		if (!FILE.exists()) {
			FILE.createNewFile();
		}
		XStream xstream = new XStream();
		xstream.toXML(instance, new FileWriter(FILE));
	}
	
	private final Map<String, User> users;

	private final PaymentSchedule schedule;

	private BigDecimal loanCap;

	private int currentMonth;

	private int lastAccountNumber;
	
	private Bank() {
		users = new HashMap<String, User>();
		schedule = new PaymentSchedule();
		currentMonth = 0;
	}
	
	public User getUser(String username) {
		if (!users.containsKey(username)) {
			throw new IllegalArgumentException("Username doesn't exist");
		}
		return users.get(username);
	}

	public void addUser(String username, User user) {
		if (users.containsKey(username)) {
			throw new IllegalArgumentException("Username already taken");
		}
		users.put(username, user);
	}

	PaymentSchedule getPaymentSchedule() {
		return schedule;
	}

	public BigDecimal getLoanCap() {
		return loanCap;
	}

	public void setLoanCap(BigDecimal loanCap) {
		if (loanCap.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("LoanCap must be non-negative");
		}
		this.loanCap = loanCap;
	}

    void authorizeLoan(BigDecimal loanAmount) throws LoanCapException {
        if (loanCap.compareTo(loanAmount) > 0) {
            throw new LoanCapException();
        }
        loanCap = loanCap.subtract(loanAmount);
    }

    void returnLoan(BigDecimal returnedAmount) {
        loanCap = loanCap.add(returnedAmount);
    }

    public int getCurrentMonth() {
        return currentMonth;
    }
	
	public void advanceCurrentMonth() {
        // before we advance the month, go through all accounts and call doPayments()
        // do this now so that the Transaction objects reflect the current month
        for (User user : users.values()) {
            for (Account account : user.getAccounts()) {
                try {
                    account.doPayments();
                } catch (Exception x) {
                    // TODO maybe this should be handled differently
                    // for now I'm going to make it print to the console
                    x.printStackTrace();
                }
            }
        }
        currentMonth += 1;
    }
	
	public int assignAccountNumber() {
		return ++lastAccountNumber;
	}
	
	public Map<String, String> getBankStats(){//avg balance, number of employees, number of users, number of customers, total number of accounts, sumtotal of assests/sumtotal liabilities
		BigDecimal averageBalance = BigDecimal.ZERO;
		BigDecimal sumAssets = BigDecimal.ZERO;
		BigDecimal sumLiabilities = BigDecimal.ZERO;
		int totalAccounts = 0;
		int totalUsers = 0;
		int totalCustomers = 0;
		int totalEmployees = 0;
		int totalAccountManagers = 0;
		int totalTellers = 0;
		int totalAccountants = 0;
		int totalAuditors = 0;
		int totalOperationsManagers = 0;
		int temp = 0;

		for(User user:users.values()){
			totalUsers ++;
			if(user.getRole() == null){
				totalCustomers ++;
			} else {
				if(user.getRole() == User.Role.ACCOUNT_MANAGER){
					totalAccountManagers ++;
				}
				if(user.getRole() == User.Role.ACCOUNTANT){
					totalAccountants ++;
				}
				if(user.getRole() == User.Role.TELLER){
					totalTellers ++;
				}
				if(user.getRole() == User.Role.AUDITOR){
					totalAuditors ++;
				}
				if(user.getRole() == User.Role.OPERATIONS_MANAGER){
					totalOperationsManagers ++;
				}
				totalEmployees ++;
			}
			for(Account account:user.getAccounts()){
				totalAccounts ++;
				if(account.getType() == Account.Type.CHECKING || account.getType() == Account.Type.SAVINGS || account.getType() == Account.Type.CD){
					sumAssets = sumAssets.add(account.getBalance());		
					temp ++;
				} else {
					sumLiabilities = sumLiabilities.add(account.getBalance());
				}
			}
		}
		averageBalance = sumAssets.divide(new BigDecimal(temp));
		Map<String, String> bankStats = new HashMap<String, String>();
		bankStats.put("Average Balance across all Accounts", averageBalance.toString());
		bankStats.put("Sumtotal of Assets", sumAssets.toString());
		bankStats.put("Sumtotal of Liabilities", sumLiabilities.toString());
		bankStats.put("Total number of Accounts", String.valueOf(totalAccounts));
		bankStats.put("Total number of Users", String.valueOf(totalUsers));
		bankStats.put("Total number of Customers", String.valueOf(totalCustomers));
		bankStats.put("Total number of Employees", String.valueOf(totalEmployees));
		bankStats.put("Total number of Operations Managers", String.valueOf(totalOperationsManagers));
		bankStats.put("Total number of Auditors", String.valueOf(totalAuditors));
		bankStats.put("Total number of Tellers", String.valueOf(totalTellers));
		bankStats.put("Total number of Account Managers", String.valueOf(totalAccountManagers));
		bankStats.put("Total number of Accountants", String.valueOf(totalAccountants));
		return bankStats;
	}
}
