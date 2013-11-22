package city.roles;

import city.Application;
import city.Role;
import city.buildings.BankBuilding;
import city.interfaces.BankCustomer;

public class BankCustomerRole extends Role implements BankCustomer {
	
	// Data
	BankBuilding building;
	Application.BANK_SERVICES service;
	BankManagerRole b;
	int netTransaction = 0;
	state st;
	int salary;
	int amount;
	BankTellerRole t;
	int acctNum;
	int boothNumber;
	
	public void setActive(Application.BANK_SERVICES s, int money){
		print("Customer has been set active");
		this.service = s;
		st = state.entering;
		amount = money;
		stateChanged();
	}
	// Constructor
	
	public BankCustomerRole(BankBuilding b) {
		building = b;
	}
	
	// Messages
	public void msgWhatDoYouWant(int booth, BankTellerRole tell) {
		print("WhatDoYouWant message received");
		t = tell;
		boothNumber = booth;
		st = state.requestService;
		stateChanged();
	}
	
	public void msgDepositCompleted() {
		print("DepositCompleted message received");
		st = state.exit;
	    stateChanged();
	}
	
	public void msgAccountCreated(int acct) {
		print("AccountCreated message received");
		acctNum = acct;
		st = state.exit;
		stateChanged();
	}
	
	public void msgHereIsWithdrawal(int money) {
		print("HereIsWithdrawal message received");
		netTransaction += money;
		st = state.exit;
		stateChanged();
	}
	
	public void msgLoanGranted(int loanMoney){
		print("LoanGranted message received");
		netTransaction += loanMoney;
		stateChanged();
	}
	
	public void msgTransactionDenied(){
		print("TransactionDenied message received");
		stateChanged();
		//more to come...
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
		netTransaction -= amount;
		t.msgCreateAccount(amount);
	}
	
	public void RequestWithdrawal(){
		st = state.inProgress;
		t.msgWithdraw(acctNum, amount, salary);
	}
	
	public void ExitBank(){
		st = state.inProgress;
		t.msgDoneAndLeaving();
		this.getPerson().setCash(netTransaction);
		netTransaction = 0;
	}
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes 
	
} 
