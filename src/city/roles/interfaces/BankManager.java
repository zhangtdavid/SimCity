package city.roles.interfaces;

import java.util.List;

import city.bases.interfaces.RoleInterface;
import city.roles.BankManagerRole.BankTask;

public interface BankManager extends RoleInterface {

	// Data
	
	public static enum STATE {available, busy, gone};
	public static enum TYPE {deposit, atmDeposit, withdrawal, loanPayment, acctCreate};
	
	// Constructor
	
	// Messages
	
	public void msgNeedService(BankCustomer bc);
	public void msgDirectDeposit(int acctNum, int money, BankCustomer r);
	public void msgAvailable(BankTeller t);
	public void msgWithdraw(int acctNum, int money, BankTeller t);
	public void msgTryDeposit(int money, int acctNum, BankTeller t);
	public void msgCreateLoan(int amt, int monthly, int acct);
	public void msgUnavailable(BankTeller bankTellerRole);
	
	// Scheduler
	
	// Actions
	
	// Getters
	
	public List<BankTask> getBankTasks();
	
	// Setters
	
	// Utilities
	
	// Classes	
	
}
