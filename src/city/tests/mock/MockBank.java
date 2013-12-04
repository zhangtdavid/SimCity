package city.tests.mock;

import java.util.List;

import city.abstracts.MockBuilding;
import city.buildings.BankBuilding.Account;
import city.buildings.BankBuilding.Loan;
import city.interfaces.Bank;
import city.interfaces.BankManager;

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
	
}
