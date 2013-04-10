package project.model;

import com.thoughtworks.xstream.XStream;
import hw1.DateTime;
import hw1.Time;

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

	private Time timeOffset;

	private int lastAccountNumber;
	
	private Bank() {
		users = new HashMap<String, User>();
		schedule = new PaymentSchedule();
		timeOffset = new Time(0, 0, 0, 0);
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
	
	public DateTime getEffectiveTime() {
        // TODO did we ever figure out how this was going to work?
		return new DateTime().add(timeOffset);
	}
	
	public Time getTimeOffset(){
	    // Check next month's amount of days and add that?
		return timeOffset;
	}

	public void setTimeOffset(Time timeOffset) {
		this.timeOffset = timeOffset;
	}
	
	public int assignAccountNumber() {
		return ++lastAccountNumber;
	}
}
