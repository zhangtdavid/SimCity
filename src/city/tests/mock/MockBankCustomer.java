package city.tests.mock;

import utilities.LoggedEvent;
import city.Application;
import city.Application.BANK_SERVICE;
import city.Application.TRANSACTION_TYPE;
import city.BuildingInterface;
import city.abstracts.MockRole;
import city.interfaces.Bank;
import city.interfaces.BankCustomer;
import city.interfaces.BankTeller;
import city.roles.BankManagerRole;

public class MockBankCustomer extends MockRole implements BankCustomer {
	
	// Data
	
	private Bank building;
	private BuildingInterface business;
	private Application.TRANSACTION_TYPE depositType;
	private Application.BANK_SERVICE service;

	private BankManagerRole b;
	private int netTransaction = 0;
	private STATE st;
	private int amount;
	private BankTeller t;
	public int acctNum = -1;
	private int boothNumber;

	// Constructor
	
	public MockBankCustomer(BuildingInterface b) {
		building = null;
		business = b;
	}
	
	public MockBankCustomer() {
		building = null;
	}
	
	// Messages

	@Override
	public void msgDepositCompleted() {
		log.add(new LoggedEvent("Received msgDepositCompleted"));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatDoYouWant(int boothnumber, BankTeller tell) {
		log.add(new LoggedEvent("Received msgWhatDoYouWant"));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAccountCreated(int acct) {
		log.add(new LoggedEvent("Received msgAccountCreated " + acct));
		// TODO Auto-generated method stub
	}

	@Override
	public void msgHereIsWithdrawal(int money) {
		log.add(new LoggedEvent("Received msgHereIsWithdrawal " + money));
		// TODO Auto-generated method stub
	}

	@Override
	public void msgLoanGranted(int loanMoney) {
		log.add(new LoggedEvent("Received msgLoanGranted "  + loanMoney));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTransactionDenied() {
		log.add(new LoggedEvent("Received msgTransactionDenied"));
		// TODO Auto-generated method stub
		
	}
	
	// Scheduler
	
	@Override
	public boolean runScheduler() {
		
		if (depositType == TRANSACTION_TYPE.business) {
			business.setCash(building.getCash() + netTransaction);
		} else if (depositType == Application.TRANSACTION_TYPE.personal) {
			this.getPerson().setCash(this.getPerson().getCash() + netTransaction);
		}
		netTransaction = 0;
		super.setInactive();
		
		return false;
	}
	
	// Getters
	
	@Override
	public BANK_SERVICE getService() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Setters

	@Override
	public void setActive(BANK_SERVICE s, int money, TRANSACTION_TYPE t) {
		log.add(new LoggedEvent("Received setActive"));
		this.service = s;
		this.depositType = t;
		this.amount = money;
		this.st = STATE.entering;
		if (s == BANK_SERVICE.atmDeposit || s == BANK_SERVICE.deposit) {
			this.netTransaction = (-money);
		} else if (s == BANK_SERVICE.moneyWithdraw) {
			this.netTransaction = money;
		}
		super.setActive();
	}

}
