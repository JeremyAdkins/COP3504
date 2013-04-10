package project.model;

import hw1.DateTime;

import java.util.HashSet;
import java.util.Set;


public class User {
	String firstName;
	String lastName;
	DateTime birthdate;
	int ssn;
	String email;
	Set<Account> accounts;
	EmployeeRole role;
        
        public User(){
            accounts = new HashSet<>();            
        }
	
	public enum EmployeeRole{
		TELLER, ACCOUNT_MANAGER, ACCOUNTANT, AUDITOR, OPERATIONS_MANAGER;
	}

	public EmployeeRole getRole() {
		return role;
	}

	public void setRole(EmployeeRole role) {
		this.role = role;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public DateTime getBirthdate() {
		return birthdate;
	}

	public int getSsn() {
		return ssn;
	}

	public String getEmail() {
		return email;
	}

	public Set<Account> getAccounts() {
		return new HashSet<>(accounts);
	}
	
	public void addAccount(Account account){
		accounts.add(account);
	}
	
	public boolean isActiveCustomer(){
		for(Account account:accounts){
			if(!account.isClosed()){
				return true;
			}
		}
		return false;
	}
	
	public void sendEmail(String subject, String body){
		//WORRY ABOUT THIS LATER
	}
}
