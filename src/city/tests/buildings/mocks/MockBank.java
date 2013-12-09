package city.tests.buildings.mocks;

import java.beans.PropertyChangeSupport;
import java.util.List;

import city.buildings.BankBuilding.Account;
import city.buildings.BankBuilding.Loan;
import city.buildings.interfaces.Bank;
import city.roles.interfaces.BankManager;
import city.tests.bases.mocks.MockBuilding;

public class MockBank extends MockBuilding implements Bank {

	public MockBank(String name) {
		super(name);
	}

	@Override
	public List<Loan> getLoans() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Account> getAccounts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BankManager getManager() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setManager(BankManager b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
