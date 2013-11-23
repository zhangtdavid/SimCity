package city.interfaces;

import city.roles.BankCustomerRole;

public interface BankTeller extends RoleInterface {

	// Data
	public enum serviceState{needsService, pending, inProgress, confirmed, newAccount, finished, done, failed};
	public enum serviceType{withdrawal, deposit};
	// Constructor
	
	// Messages
	public void msgAddressCustomer(BankCustomerRole bc);
	public void msgHereIsAccount(int acctNum);
	public void msgWithdrawalFailed();
	public void msgTransactionSuccessful();
	//From BankCustomer
	public void msgWithdraw(int acctNum, int money, int salary);
	public void msgDeposit(int money, int acctNum);
	public void msgDoneAndLeaving();
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
