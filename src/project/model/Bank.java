package project.model;

import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

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
	
	public User getUser(String username) throws LoginException {
		if (!users.containsKey(username)) {
            throw new LoginException(LoginException.Type.USER_NOT_FOUND, username);
		}
		return users.get(username);
	}

	public void addUser(String username, User user) throws LoginException {
		if (users.containsKey(username)) {
            throw new LoginException(LoginException.Type.DUPLICATE_USERNAME, username);
		}
		users.put(username, user);
	}

	PaymentSchedule getPaymentSchedule() {
		return schedule;
	}

	public BigDecimal getLoanCap() {
		return loanCap;
	}

	public void setLoanCap(BigDecimal loanCap) throws InvalidInputException {
		if (loanCap.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidInputException(loanCap, "loan cap must be non-negative");
		}
		this.loanCap = loanCap;
	}

    void authorizeLoan(BigDecimal loanAmount) throws LoanCapException {
        if (loanCap.compareTo(loanAmount) < 0) {
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
}
