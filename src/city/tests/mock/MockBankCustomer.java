package city.tests.mock;

import utilities.LoggedEvent;
import city.Application.BANK_SERVICE;
import city.Application.TRANSACTION_TYPE;
import city.abstracts.MockRole;
import city.interfaces.BankCustomer;
import city.roles.BankTellerRole;

public class MockBankCustomer extends MockRole implements BankCustomer {
	
	public MockBankCustomer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDepositCompleted() {
		log.add(new LoggedEvent("Received msgDepositCompleted"));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatDoYouWant(int boothnumber, BankTellerRole tell) {
		log.add(new LoggedEvent("Received msgWhatDoYouWant"));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAccountCreated(int acct) {
		log.add(new LoggedEvent("Received msgAccountCreated " + acct));
		// TODO Auto-generated method stub
	}

	@Override
	public void msgHereIsWithdrawal(int money) {
		log.add(new LoggedEvent("Received msgHereIsWithdrawal " + money));
		// TODO Auto-generated method stub
	}

	@Override
	public void msgLoanGranted(int loanMoney) {
		log.add(new LoggedEvent("Received msgLoanGranted "  + loanMoney));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTransactionDenied() {
		log.add(new LoggedEvent("Received msgTransactionDenied"));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActive(BANK_SERVICE atmdeposit, int i,
			TRANSACTION_TYPE business) {
		// TODO Auto-generated method stub
		
	}

}
