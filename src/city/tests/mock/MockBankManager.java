package city.tests.mock;

import java.util.List;

import utilities.LoggedEvent;
import city.MockRole;
import city.interfaces.BankCustomer;
import city.interfaces.BankManager;
import city.interfaces.BankTeller;
import city.roles.BankManagerRole.BankTask;

public class MockBankManager extends MockRole implements BankManager {

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

}
