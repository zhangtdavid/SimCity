package city.interfaces;

import city.Application.BANK_SERVICE;
import city.Application.TRANSACTION_TYPE;
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
	
	public void msgTransactionDenied();
	// Scheduler

	public void setActive(BANK_SERVICE atmdeposit, int i,
			TRANSACTION_TYPE business); // Added by Shirley
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
