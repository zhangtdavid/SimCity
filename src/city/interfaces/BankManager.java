package city.interfaces;

import city.Role;
import city.buildings.BankBuilding.Loan;
import city.roles.BankCustomerRole;
import city.roles.BankTellerRole;
import city.roles.BankManagerRole.BankTask;
import city.roles.BankManagerRole.MyTeller;

public interface BankManager extends RoleInterface {

	// Data
	public enum state {available, busy, gone};
	public enum type {deposit, directDeposit, withdrawal, loanPayment, acctCreate};
	static final int LoanInterval = 50;
	// Constructor
	
	// Messages
	public void msgNeedService(BankCustomerRole bc);
	
	public void msgDirectDeposit(int acctNum, int money, Role r);

	public void msgAvailable(BankTellerRole t);
	
	public void msgWithdraw(int acctNum, int money, BankTellerRole t);

	public void msgCreateAccount(int money, BankTellerRole t);

	public void msgCreateLoan(int amt, int monthly, int acct);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes	
	
}
