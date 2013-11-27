package city.roles;

import city.Application;
import city.Application.BANK_SERVICE;
import city.Application.CityMap;
import city.Application.BUILDING;
import city.Building;
import city.Role;
import city.buildings.BankBuilding;
import city.buildings.RestaurantJPBuilding;
import city.interfaces.BankCustomer;

public class BankCustomerRole extends Role implements BankCustomer {
	
	// Data
	BankBuilding building;
	Building business;
	Application.TRANSACTION_TYPE depositType;
	Application.BANK_SERVICE service;
	BankManagerRole b;
	int netTransaction = 0;
	state st;
	int amount;
	BankTellerRole t;
	int acctNum = -1;
	int boothNumber;
	
	public void setActive(Application.BANK_SERVICE s, int money, Application.TRANSACTION_TYPE t){
		super.setActive();
		System.out.println("Customer has been set active");
		this.service = s;
		this.depositType = t;
		amount = money;
		if(s != Application.BANK_SERVICE.atmDeposit)
			st = state.entering;
		this.setActivityBegun();
		runScheduler();
	}
	// Constructor
	
	public BankCustomerRole(Building b) {
		building = (BankBuilding) Application.CityMap.findRandomBuilding(BUILDING.bank);
		business = b;
	}
	
	public BankCustomerRole() {
		building = (BankBuilding) (Application.CityMap.findRandomBuilding(BUILDING.bank));
	}
	
	// Messages
	public void msgWhatDoYouWant(int booth, BankTellerRole tell) {
		System.out.println("WhatDoYouWant message received");
		t = tell;
		boothNumber = booth;
		st = state.requestService;
		stateChanged();
	}
	
	public void msgDepositCompleted() {
		System.out.println("DepositCompleted message received");
		st = state.exit;
	    //stateChanged();
	}
	
	public void msgAccountCreated(int acct) {
		System.out.println("AccountCreated message received");
		acctNum = acct;
		st = state.exit;
		//stateChanged();
	}
	
	public void msgHereIsWithdrawal(int money) {
		System.out.println("HereIsWithdrawal message received");
		netTransaction += money;
		st = state.exit;
		//stateChanged();
	}
	
	public void msgLoanGranted(int loanMoney){
		System.out.println("LoanGranted message received");
		netTransaction += loanMoney;
		stateChanged();
	}
	
	public void msgTransactionDenied(){
		System.out.println("TransactionDenied message received");
		stateChanged();
		//more to come...
	}
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		if(service == BANK_SERVICE.atmDeposit){
			DirectDeposit();
			return true;
		}
		if (st == state.entering){
			AskForService();
			return true;
		}
		if(st == state.requestService){
			if(service == BANK_SERVICE.deposit){
				Deposit();
				return true;
			}
			if(service == BANK_SERVICE.moneyWithdraw){
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
		if(building == null)
			System.out.println("Null building. what the fuck");
		st = state.inProgress;
		building.manager.msgNeedService(this);
	}
	public void Deposit(){
		st = state.inProgress;
		netTransaction -= amount;
		t.msgDeposit(amount, acctNum);
	}
	
	public void RequestWithdrawal(){
		st = state.inProgress;
		t.msgWithdraw(acctNum, amount, this.getPerson().getOccupation().getSalary());
	}
	
	public void ExitBank(){
		st = state.inProgress;
		if(service != Application.BANK_SERVICE.atmDeposit)
			t.msgDoneAndLeaving();
		if(depositType == Application.TRANSACTION_TYPE.business)
			business.setCash(building.getCash() + netTransaction);
		else if (depositType == Application.TRANSACTION_TYPE.personal)
			this.getPerson().setCash(this.getPerson().getCash() + netTransaction);
		netTransaction = 0;
		super.setInactive();
	}
	
	// Getters
	
	// Setters
	
	// Utilities
	
	// Classes 
	
} 
