package city.tests.mock;

import city.MockRole;
import city.Role;
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
	public void msgDirectDeposit(int acctNum, double money, Role r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAvailable(BankTellerRole t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWithdraw(int acctNum, double money, BankTellerRole t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCreateAccount(double money, BankTellerRole t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCreateLoan(double amt, double monthly, int acct) {
		// TODO Auto-generated method stub
		
	}

}
