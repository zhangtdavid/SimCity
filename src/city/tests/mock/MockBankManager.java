package city.tests.mock;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAvailable(BankTellerRole t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWithdraw(int acctNum, int money, BankTellerRole t) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void msgCreateLoan(int amt, int monthly, int acct) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDirectDeposit(int acctNum, int money, BankCustomerRole r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTryDeposit(int money, int acctNum, BankTellerRole t) {
		// TODO Auto-generated method stub
		
	}

}
