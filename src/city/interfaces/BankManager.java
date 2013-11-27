package city.interfaces;

public interface BankManager extends RoleInterface {

	// Data
	public enum state {available, busy, gone};
	public enum type {deposit, atmDeposit, withdrawal, loanPayment, acctCreate};
	static final int LoanInterval = 50;
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
	
	// Setters
	
	// Utilities
	
	// Classes	
	
}
