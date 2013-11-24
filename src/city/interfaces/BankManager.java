package city.interfaces;

import city.roles.BankCustomerRole;
import city.roles.BankTellerRole;

public interface BankManager extends RoleInterface {

	// Data
	public enum state {available, busy, gone};
	public enum type {deposit, atmDeposit, withdrawal, loanPayment, acctCreate};
	static final int LoanInterval = 50;
	// Constructor
	
	// Messages
	public void msgNeedService(BankCustomerRole bc);
	
	public void msgDirectDeposit(int acctNum, int money, BankCustomerRole r);

	public void msgAvailable(BankTellerRole t);
	
	public void msgWithdraw(int acctNum, int money, BankTellerRole t);

	public void msgTryDeposit(int money, int acctNum, BankTellerRole t);

	public void msgCreateLoan(int amt, int monthly, int acct);

	public void msgUnavailable(BankTellerRole bankTellerRole);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes	
	
}
