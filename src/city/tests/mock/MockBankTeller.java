package city.tests.mock;

import city.MockRole;
import city.interfaces.BankTeller;
import city.roles.BankCustomerRole;

public class MockBankTeller extends MockRole implements BankTeller {

	public MockBankTeller() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAddressCustomer(BankCustomerRole bc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsAccount(int acctNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWithdrawalFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTransactionSuccessful() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWithdraw(int acctNum, int money, int salary) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCreateAccount(int money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneAndLeaving() {
		// TODO Auto-generated method stub
		
	}

}
