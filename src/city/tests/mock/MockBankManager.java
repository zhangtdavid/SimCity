package city.tests.mock;

import utilities.LoggedEvent;
import city.MockRole;
import city.interfaces.BankManager;
import city.roles.BankCustomerRole;
import city.roles.BankTellerRole;

public class MockBankManager extends MockRole implements BankManager {

	public MockBankManager() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNeedService(BankCustomerRole bc) {
		log.add(new LoggedEvent("Received msgNeedService " + bc));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAvailable(BankTellerRole t) {
		log.add(new LoggedEvent("Received msgAvailable " + t));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWithdraw(int acctNum, int money, BankTellerRole t) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgWithdraw " + acctNum+ " " + money + " " + t));
		
	}


	@Override
	public void msgCreateLoan(int amt, int monthly, int acct) {
		log.add(new LoggedEvent("Received msgCreateLoan " + amt + " " + monthly + " " + acct));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDirectDeposit(int acctNum, int money, BankCustomerRole r) {
		log.add(new LoggedEvent("Received msgDirectDeposit " + acctNum + " " + money + " " + r));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTryDeposit(int money, int acctNum, BankTellerRole t) {
		log.add(new LoggedEvent("Received msgTryDeposit " + money + " " + acctNum + " " + t));
		// TODO Auto-generated method stub
		
	}

}
