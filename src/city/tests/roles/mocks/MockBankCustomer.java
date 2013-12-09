package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;

import utilities.LoggedEvent;
import city.Application.BANK_SERVICE;
import city.Application.TRANSACTION_TYPE;
import city.bases.interfaces.BuildingInterface;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.BankTeller;
import city.tests.bases.mocks.MockRole;

public class MockBankCustomer extends MockRole implements BankCustomer {
	
	public MockBankCustomer() {
		// TODO Auto-generated method stub
		
	}

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

	@Override
	public void setActive(BANK_SERVICE atmdeposit, int i,
			TRANSACTION_TYPE business) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BuildingInterface getBusiness() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BANK_SERVICE getService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		
	}

}
