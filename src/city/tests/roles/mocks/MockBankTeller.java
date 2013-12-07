package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;

import utilities.LoggedEvent;
import city.Application;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.BankTeller;
import city.tests.bases.mocks.MockRole;

public class MockBankTeller extends MockRole implements BankTeller {

	public static enum STATE {needToPayRent, none}; 
	public static long RENT_DUE_INTERVAL = (Application.INTERVAL * 336); // 7 days
	
	public MockBankTeller() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAddressCustomer(BankCustomer bc) {
		log.add(new LoggedEvent("Received msgAddressCustomer " + bc));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsAccount(int acctNum) {
		log.add(new LoggedEvent("Received msgHereIsAccount " + acctNum));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWithdrawalFailed() {
		log.add(new LoggedEvent("Received msgWithdrawalFailed"));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTransactionSuccessful() {
		log.add(new LoggedEvent("Received msgTransactionSuccessful"));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWithdraw(int acctNum, int money, int salary) {
		log.add(new LoggedEvent("Received msgWithdraw " + acctNum + " " + money + " " + salary));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneAndLeaving() {
		log.add(new LoggedEvent("Received msgDoneAndLeaving"));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDeposit(int money, int acctNum) {
		log.add(new LoggedEvent("Received msgDeposit " + money + " " + acctNum));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActive() {
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

}
