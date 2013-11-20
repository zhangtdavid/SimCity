package city.interfaces;

import city.roles.BankCustomerRole;
import city.roles.BankTellerRole.MyCustomer;
import city.roles.BankTellerRole.serviceState;
import city.roles.BankTellerRole.serviceType;

public interface BankTeller extends RoleInterface {

	// Data
	
	// Constructor
	
	// Messages
	public void msgAddressCustomer(BankCustomerRole bc);
	public void msgHereIsAccount(int acctNum);
	public void msgWithdrawalFailed();
	public void msgTransactionFailed();
	public void msgTransactionSuccessful();
	//From BankCustomer
	public void msgWithdraw(int acctNum, double money, double salary);
	public void msgCreateAccount(double money);
	public void msgDoneAndLeaving();
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes
	
}
