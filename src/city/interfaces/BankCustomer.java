package city.interfaces;

import city.Application;
import city.Application.BANK_SERVICE;
import city.Application.TRANSACTION_TYPE;
import city.RoleInterface;

public interface BankCustomer extends RoleInterface {

	// Data
	
	public static enum SERVICE {createAcct, withdraw};
	public static enum STATE {entering, requestService, inProgress, exit};
	
	// Constructor
	
	// Messages
	
	public void msgWhatDoYouWant(int boothnumber, BankTeller tell);

	public void msgDepositCompleted();
	public void msgAccountCreated(int acct);
	public void msgHereIsWithdrawal(int money);
	public void msgLoanGranted(int loanMoney);
	public void msgTransactionDenied();
	
	// Scheduler

	public void setActive(BANK_SERVICE atmdeposit, int i, TRANSACTION_TYPE business);
	
	// Actions
	
	// Getters
	
	public Application.BANK_SERVICE getService();
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
