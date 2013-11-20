package city.roles;

import city.Application;
import city.Role;
import city.interfaces.BankCustomer;

public class BankCustomerRole extends Role implements BankCustomer {
	
	// Data
	
	enum service {createAcct, withdraw};
	public enum state {requestService, inProgress, exit};
	Application.BANK_SERVICES service;
	BankManagerRole b;
	double cash = 0;
	state st;
	double salary;
	double amount;
	BankTellerRole t;
	int acctNum;
	
	// Constructor
	
	public BankCustomerRole(Application.BANK_SERVICES s) {
		this.service = s;
	}
	
	// Messages
	
	public void msgDepositCompleted() {
		st = state.exit;
	    stateChanged();
	}
	
	public void msgWhatDoYouWant(int boothnumber, BankTellerRole tell) {
		t = tell;
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
	
	// Classes 
	
} 
