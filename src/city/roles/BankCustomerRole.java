package city.roles;

import city.Application;
import city.Building;
import city.Role;
import city.buildings.BankBuilding;
import city.interfaces.BankCustomer;

public class BankCustomerRole extends Role implements BankCustomer {
	
	// Data
	BankBuilding building;
	Building business;
	Application.DEPOSIT_TYPE depositType;
	Application.BANK_SERVICE service;
	BankManagerRole b;
	int netTransaction = 0;
	state st;
	int amount;
	BankTellerRole t;
	int acctNum;
	int boothNumber;
	
	public void setActive(Application.BANK_SERVICE s, int money, Application.DEPOSIT_TYPE t){
		print("Customer has been set active");
		this.service = s;
		this.depositType = t;
		amount = money;
		if(s != Application.BANK_SERVICE.directDeposit)
			st = state.entering;
		stateChanged();
	}
	// Constructor
	
	public BankCustomerRole(BankBuilding b, Building bus) {
		building = b;
		business = bus;
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
		if(service == service.accountCreate){
			DirectDeposit();
			return true;
		}
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
	public void DirectDeposit(){
		st = state.inProgress;
		netTransaction -= amount;
		building.getManager().msgDirectDeposit(acctNum, amount, this);
	}
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
		t.msgWithdraw(acctNum, amount, this.getPerson().getSalary());
	}
	
	public void ExitBank(){
		st = state.inProgress;
		if(service != Application.BANK_SERVICE.directDeposit)
			t.msgDoneAndLeaving();
		if(depositType == Application.DEPOSIT_TYPE.business)
			business.setCash(building.getCash() + netTransaction);
		else if (depositType == Application.DEPOSIT_TYPE.personal)
			this.getPerson().setCash(this.getPerson().getCash() + netTransaction);
		netTransaction = 0;
	}
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes 
	
} 
