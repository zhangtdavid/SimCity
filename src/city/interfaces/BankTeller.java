package city.interfaces;

import city.Application;
import city.RoleInterface;

public interface BankTeller extends RoleInterface {

	// Data
	
	public static enum SERVICE_STATE {needsService, pending, inProgress, confirmed, newAccount, finished, done, failed};
	public static enum SERVICE_TYPE {withdrawal, deposit};
	
	// Constructor
	
	// Messages
	
	public void msgAddressCustomer(BankCustomer bc);
	public void msgHereIsAccount(int acctNum);
	public void msgWithdrawalFailed();
	public void msgTransactionSuccessful();
	public void msgWithdraw(int acctNum, int money, int salary);
	public void msgDeposit(int money, int acctNum);
	public void msgDoneAndLeaving();
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	public void setActive(Application.BANK_SERVICE s, int money, Application.TRANSACTION_TYPE t);
	
	// Utilities
	
	// Classes
	
}
