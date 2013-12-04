package city.roles;

import trace.AlertLog;
import trace.AlertTag;
import city.Application;
import city.Application.BANK_SERVICE;
import city.Building;
import city.Role;
import city.buildings.BankBuilding;
import city.interfaces.Bank;
import city.interfaces.BankCustomer;
import city.interfaces.BankTeller;

public class BankCustomerRole extends Role implements BankCustomer {
	
	// Data
	
	private Bank building;
	private Building business;
	private Application.TRANSACTION_TYPE depositType;
	private Application.BANK_SERVICE service;
	private int netTransaction = 0;
	private STATE st;
	private int amount;
	private BankTeller t;
	public int acctNum = -1;
	private int boothNumber;

	// Constructor
	
	public BankCustomerRole(Building bus, BankBuilding b) {
		building = b;
		business = bus;
	}
	
	public BankCustomerRole(Bank b) {
		building = b;
	}
	
	
	// Messages

	public void msgWhatDoYouWant(int booth, BankTeller tell) {
		print("WhatDoYouWant message received");
		t = tell;
		boothNumber = booth;
		st = STATE.requestService;
		stateChanged();
	}
	
	@Override
	public void msgDepositCompleted() {
		print("DepositCompleted message received");
		st = STATE.exit;
	    //stateChanged();
	}
	
	@Override
	public void msgAccountCreated(int acct) {
		print("AccountCreated message received");
		acctNum = acct;
		st = STATE.exit;
		//stateChanged();
	}
	
	@Override
	public void msgHereIsWithdrawal(int money) {
		print("HereIsWithdrawal message received");
		netTransaction += money;
		st = STATE.exit;
		//stateChanged();
	}
	
	@Override
	public void msgLoanGranted(int loanMoney){
		print("LoanGranted message received");
		netTransaction += loanMoney;
		stateChanged();
	}
	
	@Override
	public void msgTransactionDenied(){
		print("TransactionDenied message received");
		stateChanged();
		//more to come...
	}
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		if(service == BANK_SERVICE.atmDeposit){
			if(st == STATE.entering){
				DirectDeposit();
				return true;
			}
		}
		if (st == STATE.entering){
			AskForService();
			return true;
		}
		if(st == STATE.requestService){
			if(service == BANK_SERVICE.deposit){
				Deposit();
				return true;
			}
			if(service == BANK_SERVICE.moneyWithdraw){
				RequestWithdrawal();
				return true;
			}
		}
		if(st == STATE.exit){
			ExitBank();
			return true;
		}
		return false;
	}
	
	// Actions
	
	private void DirectDeposit(){
		st = STATE.inProgress;
		netTransaction -= amount;
		building.getManager().msgDirectDeposit(acctNum, amount, this);
	}
	

	private void AskForService(){
		if(building == null)
			print("Null building. what the fuck");
		st = STATE.inProgress;
		building.getManager().msgNeedService(this);
	}
	
	private void Deposit(){
		st = STATE.inProgress;
		netTransaction -= amount;
		t.msgDeposit(amount, acctNum);
	}
	
	private void RequestWithdrawal(){
		st = STATE.inProgress;
		t.msgWithdraw(acctNum, amount, this.getPerson().getOccupation().getSalary());
	}
	
	private void ExitBank(){
		st = STATE.inProgress;
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
	
	@Override
	public Application.BANK_SERVICE getService() {
		return service;
	}
	
	// Setters
	
	@Override
	public void setActive(Application.BANK_SERVICE s, int money, Application.TRANSACTION_TYPE t){
		print("Customer has been set active");
		this.service = s;
		this.depositType = t;
		amount = money;
		st = STATE.entering;
		super.setActive();
	}
	
	// Utilities
	
	@Override
	public void print(String msg) {
        AlertLog.getInstance().logMessage(AlertTag.BANK, "BankCustomerRole " + this.getPerson().getName(), msg);
    }
	
	// Classes 
	
} 
