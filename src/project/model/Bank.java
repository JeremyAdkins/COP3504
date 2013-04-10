package project.model;

import hw1.DateTime;
import hw1.Time;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.XStream;


public final class Bank {
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
	
	public User getUser(String username){
		if(!users.containsKey(username)){
			throw new IllegalArgumentException("Username doesn't exist");
		}
		return users.get(username);
	}
	public void addUser(String username, User user){
		if(users.containsKey(username)){
			throw new IllegalArgumentException("Username already taken");
		}
		users.put(username,user);
	}
	PaymentSchedule getPaymentSchedule(){
		return schedule;
	}
	public BigDecimal getLoanCap(){
		return loanCap;
	}
	public void setLoanCap(BigDecimal loanCap){
		if(loanCap.compareTo(BigDecimal.ZERO)<0){
			throw new IllegalArgumentException("LoanCap must be non-negative");
		}
		this.loanCap = loanCap;
	}
	
	public DateTime getEffectiveTime() {
		return new DateTime().add(timeOffset);
	}
	
	public Time getTimeOffset(){//Check next month's amount of days and add that?
		return timeOffset;
	}
	public void setTimeOffset(Time timeOffset){
		this.timeOffset = timeOffset;
	}
	
	public int assignAccountNumber() {
		return ++lastAccountNumber;
	}
}
