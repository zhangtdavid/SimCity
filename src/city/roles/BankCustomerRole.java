package city.roles;

import city.Role;
import city.interfaces.BankCustomer;
import city.interfaces.BankTeller;

public class BankCustomerRole extends Role implements BankCustomer {
// Data
	service s;
	BankManagerRole b;
	double cash = 0;
	state st;
	double salary;
	double amount;
	BankTellerRole t;
	int acctNum;
	// Constructor
	
// Messages
	public void msgDepositCompleted(){
		st = state.exit;
	    stateChanged();
	}
	public void msgWhatDoYouWant(int boothnumber, BankTellerRole tell){
		t = tell;
		st = state.requestService;
		stateChanged();
	}
	public void msgAccountCreated(int acct){
		acctNum = acct;
		stateChanged();
	}
	public void msgHereIsWithdrawal(double money){
		cash += money;
		stateChanged();
	}
	public void msgLoanGranted(double loanMoney){
		cash += loanMoney;
		stateChanged();
	}
	public void msgTransactionCompleted(){
		st = state.exit;
		stateChanged();
	}
	public void msgTransactionDenied(){
		
	}
// Scheduler
	
	@Override
	public boolean runScheduler() {
		if(st == state.requestService){
			if(s == service.createAcct){
				RequestAccount();
				return true;
			}
			if(s == service.withdraw){
				RequestWithdrawal();
				return true;
			}
		}
		if(st == state.exit){
			ExitBank();
			return true;
		}
		// TODO Auto-generated method stub
		return false;
	}
	
// Actions
	public void RequestAccount(){
		st = state.inProgress;
		cash -= amount;
		t.msgCreateAccount(amount);
	}
	public void RequestWithdrawal(){
		st = state.inProgress;
		t.msgWithdraw(acctNum, amount, salary);
	}
	public void ExitBank(){
		t.msgDoneAndLeaving();
	}
	// Getters
	
	// Setters
	
// Utilities
	enum service {createAcct, withdraw};
	enum state {requestService, inProgress, exit};
	// Classes
}
