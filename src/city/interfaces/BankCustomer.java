package city.interfaces;

import city.roles.BankTellerRole;

public interface BankCustomer extends RoleInterface {

	// Data
	enum service {createAcct, withdraw};
	public enum state {entering, requestService, inProgress, exit};
	
	// Constructor
	
	// Messages
	public void msgDepositCompleted();
	
	public void msgWhatDoYouWant(int boothnumber, BankTellerRole tell);
	
	public void msgAccountCreated(int acct);
	
	public void msgHereIsWithdrawal(int money);
	
	public void msgLoanGranted(int loanMoney);
	
	public void msgTransactionCompleted();
	
	public void msgTransactionDenied();
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
