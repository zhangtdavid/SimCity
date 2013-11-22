package city.interfaces;

import city.roles.BankTellerRole;
import city.roles.BankCustomerRole.state;

public interface BankCustomer extends RoleInterface {

	// Data
	public void msgDepositCompleted();
	
	public void msgWhatDoYouWant(int boothnumber, BankTellerRole tell);
	
	public void msgAccountCreated(int acct);
	
	public void msgHereIsWithdrawal(double money);
	
	public void msgLoanGranted(double loanMoney);
	
	public void msgTransactionCompleted();
	
	public void msgTransactionDenied();
	
	// Constructor
	
	// Messages
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
