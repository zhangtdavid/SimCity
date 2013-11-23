package city.tests.mock;

import utilities.LoggedEvent;
import city.MockRole;
import city.interfaces.BankCustomer;
import city.interfaces.BankTeller;

public class MockBankTeller extends MockRole implements BankTeller {

	public MockBankTeller() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAddressCustomer(BankCustomer bc) {
		log.add(new LoggedEvent("Received msgAddressCustomer " + bc));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsAccount(int acctNum) {
		log.add(new LoggedEvent("Received msgHereIsAccount " + acctNum));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWithdrawalFailed() {
		log.add(new LoggedEvent("Received msgWithdrawalFailed"));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTransactionSuccessful() {
		log.add(new LoggedEvent("Received msgTransactionFailed"));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWithdraw(int acctNum, int money, int salary) {
		log.add(new LoggedEvent("Received msgWithdraw " + acctNum + " " + money + " " + salary));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneAndLeaving() {
		log.add(new LoggedEvent("Received msgDoneAndLeaving"));
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDeposit(int money, int acctNum) {
		log.add(new LoggedEvent("Received msgDeposit " + money + " " + acctNum));
		// TODO Auto-generated method stub
		
	}

}
