package project.model;

import hw1.DateTime;

import java.util.HashSet;
import java.util.Set;

public final class User {
    public static enum Role {
        TELLER, ACCOUNT_MANAGER, ACCOUNTANT, AUDITOR, OPERATIONS_MANAGER;
    }

	private final String firstName;

	private final String lastName;

	private final DateTime birthdate;

	private final int ssn;

	private final String email;

	private final Set<Account> accounts;

	private Role role;

    public User(String firstName, String lastName, DateTime birthdate, int ssn, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.ssn = ssn;
        this.email = email;
        this.accounts = new HashSet<Account>();
        this.role = null;
    }

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
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
		return new HashSet<Account>(accounts);
	}
	
	public void addAccount(Account account) {
		accounts.add(account);
	}
	
	public boolean isActiveCustomer() {
		for (Account account : accounts) {
			if (!account.isClosed()) {
				return true;
			}
		}
		return false;
	}
	
	public void sendEmail(String subject, String body){
		// TODO WORRY ABOUT THIS LATER
	}
}
