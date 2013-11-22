package city.tests.mock;

import city.MockRole;
import city.interfaces.BankCustomer;
import city.roles.BankTellerRole;

public class MockBankCustomer extends MockRole implements BankCustomer {
	
	public MockBankCustomer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDepositCompleted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatDoYouWant(int boothnumber, BankTellerRole tell) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAccountCreated(int acct) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsWithdrawal(int money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLoanGranted(int loanMoney) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTransactionDenied() {
		// TODO Auto-generated method stub
		
	}

}
