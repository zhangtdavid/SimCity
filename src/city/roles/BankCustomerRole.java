package city.roles;

import city.Application;
import city.Role;
import city.buildings.BankBuilding;
import city.interfaces.BankCustomer;

public class BankCustomerRole extends Role implements BankCustomer {
	
	// Data
	BankBuilding building;
	enum service {createAcct, withdraw};
	public enum state {entering, requestService, inProgress, exit};
	Application.BANK_SERVICES service;
	BankManagerRole b;
	double cash = 0;
	state st;
	double salary;
	double amount;
	BankTellerRole t;
	int acctNum;
	int boothNumber;
	
	// Constructor
	
	public BankCustomerRole(Application.BANK_SERVICES s, BankBuilding b) {
		building = b;
		this.service = s;
		st = state.entering;
	}
	
	// Messages
	
	public void msgDepositCompleted() {
		st = state.exit;
	    stateChanged();
	}
	
	public void msgWhatDoYouWant(int booth, BankTellerRole tell) {
		t = tell;
		boothNumber = booth;
		st = state.requestService;
		stateChanged();
	}
	
	public void msgAccountCreated(int acct) {
		acctNum = acct;
		stateChanged();
	}
	
	public void msgHereIsWithdrawal(double money) {
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
		if (st == state.entering){
			AskForService();
			return true;
		}
		if(st == state.requestService){
			if(service == service.accountCreate){
				RequestAccount();
				return true;
			}
			if(service == service.moneyWithdraw){
				RequestWithdrawal();
				return true;
			}
		}
		if(st == state.exit){
			ExitBank();
			return true;
		}
		return false;
	}
	
	// Actions
	public void AskForService(){
		st = state.inProgress;
		building.manager.msgNeedService(this);
	}
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
		st = state.inProgress;
		t.msgDoneAndLeaving();
	}
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes 
	
} 
