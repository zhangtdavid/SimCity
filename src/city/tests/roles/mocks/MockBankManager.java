package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;
import java.util.List;

import utilities.LoggedEvent;
import city.buildings.interfaces.Bank;
import city.roles.BankManagerRole.BankTask;
import city.roles.interfaces.BankCustomer;
import city.roles.interfaces.BankManager;
import city.roles.interfaces.BankTeller;
import city.tests.bases.mocks.MockRole;

public class MockBankManager extends MockRole implements BankManager {

	public Bank building;

	public MockBankManager() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNeedService(BankCustomer bc) {
		log.add(new LoggedEvent("Received msgNeedService " + bc));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAvailable(BankTeller t) {
		log.add(new LoggedEvent("Received msgAvailable " + t));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWithdraw(int acctNum, int money, BankTeller t) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgWithdraw " + acctNum+ " " + money + " " + t));
		
	}


	@Override
	public void msgCreateLoan(int amt, int monthly, int acct) {
		log.add(new LoggedEvent("Received msgCreateLoan " + amt + " " + monthly + " " + acct));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDirectDeposit(int acctNum, int money, BankCustomer r) {
		log.add(new LoggedEvent("Received msgDirectDeposit " + acctNum + " " + money + " " + r));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTryDeposit(int money, int acctNum, BankTeller t) {
		log.add(new LoggedEvent("Received msgTryDeposit " + money + " " + acctNum + " " + t));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgUnavailable(BankTeller bankTellerRole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BankTask> getBankTasks() {
		// TODO Auto-generated method stub
		return null;
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
