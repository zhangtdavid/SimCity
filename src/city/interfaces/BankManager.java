package city.interfaces;

import city.Role;
import city.buildings.BankBuilding.Loan;
import city.roles.BankCustomerRole;
import city.roles.BankTellerRole;
import city.roles.BankManagerRole.BankTask;
import city.roles.BankManagerRole.MyTeller;
import city.roles.BankManagerRole.state;
import city.roles.BankManagerRole.type;

public interface BankManager extends RoleInterface {

	// Data
	static final int LoanInterval = 50;
	// Constructor
	
	// Messages
	public void msgNeedService(BankCustomerRole bc);
	
	public void msgDirectDeposit(int acctNum, double money, Role r);

	public void msgAvailable(BankTellerRole t);
	
	public void msgWithdraw(int acctNum, double money, BankTellerRole t);

	public void msgCreateAccount(double money, BankTellerRole t);

	public void msgCreateLoan(double amt, double monthly, int acct);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes	
	
}
