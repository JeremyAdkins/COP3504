package project.model;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class User {
    public static enum Role {
        TELLER("Teller"), ACCOUNT_MANAGER("Account Manager"), ACCOUNTANT("Accountant"), AUDITOR("Auditor"), OPERATIONS_MANAGER("Operations Manager");
        
        private final String displayName;

        private Role(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

	private final String firstName;

	private final String lastName;

	private final Calendar birthdate;

	private final String ssn; // ###-##-####

	private final String email;

	private final Set<Account> accounts;

	private Role role;
/**
 * 
 * @param firstName
 * @param lastName
 * @param birthdate
 * @param ssn
 * @param email 
 */
    public User(String firstName, String lastName, Calendar birthdate, String ssn, String email) {
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

	public Calendar getBirthdate() {
		return birthdate;
	}

	public String getSsn() {
		return ssn;
	}

	public String getEmail() {
		return email;
	}

	public Set<Account> getAccounts() {
		return Collections.unmodifiableSet(accounts);
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
