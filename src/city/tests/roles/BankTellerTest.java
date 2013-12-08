package city.tests.roles;

import junit.framework.TestCase;
import city.buildings.BankBuilding;
import city.roles.BankTellerRole;
import city.tests.agents.mocks.MockPerson;
import city.tests.roles.mocks.MockBankCustomer;
import city.tests.roles.mocks.MockBankManager;

public class BankTellerTest extends TestCase {

	MockBankCustomer customer;
	MockBankManager manager;
	
	MockPerson tellerPerson;
	BankTellerRole teller;

	public void setUp() throws Exception {
		super.setUp();
		BankBuilding b = new BankBuilding("Bank", null, null);
		this.customer = new MockBankCustomer();
		this.manager = new MockBankManager();
		b.setManager(manager);
		this.tellerPerson = new MockPerson("Teller");
		this.teller = new BankTellerRole(b, 0, 12);
		teller.setPerson(tellerPerson);
	}
	
	public void testAccountCreationScenario() {

		// Preconditions
		assertEquals("Customers's log should be empty.", 0, customer.log.size());
		assertEquals("Teller should have no customer.", null, teller.currentCustomer);
		
		teller.setActive();
		
		assertEquals("Manager's log length should be 1.", 1, manager.log.size());
		assertTrue("Manager should be notified of available teller.", manager.log.containsString("Received msgAvailable " + teller));
		// Send a message from the host to seat a customer
		teller.msgAddressCustomer(customer);
		
		assertEquals("Teller should have a customer.", true, teller.currentCustomer != null);
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("Customer's log length should be 1.", 1, customer.log.size());
		assertTrue("Customer should be addressed.", customer.log.containsString("Received msgWhatDoYouWant"));
		assertFalse("Teller's scheduler should return false.", teller.runScheduler());
//		
//		// Release the waiterHover Semaphore so that each action will return before the next one is called.
//		waiter.waiterHover.release();
//		
		// Send a message from the customer to take an order. 
		teller.msgDeposit(-1, 50);
		
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("Manager's log length should be 2.", 2, manager.log.size());
		assertTrue("Manager should be asked to try deposit.", manager.log.containsString("Received msgTryDeposit"));
		
		// Send a message from the customer to order Steak
		teller.msgHereIsAccount(1);
		
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("InternalCustomer's account number should be 1.", 1, teller.currentCustomer.acctNum);
		assertEquals("Customer's log length should be 2.", 2, customer.log.size());
		assertTrue("Customer should receive account number.", customer.log.containsString("Received msgAccountCreated 1"));
		
		teller.msgDoneAndLeaving();
		
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("CurrentCustomer should equal null.", null, teller.currentCustomer);
		assertEquals("Manager's log length should be 3.", 3, manager.log.size());
		assertTrue("Manager should be notified that teller is available.", manager.log.containsString("Received msgAvailable"));
	}
	
	public void testInPersonDeposit(){
		assertEquals("Customers's log should be empty.", 0, customer.log.size());
		assertEquals("Teller should have no customer.", null, teller.currentCustomer);
		
		// Send a message from the host to seat a customer
		teller.msgAddressCustomer(customer);
		
		assertEquals("Teller should have a customer.", true, teller.currentCustomer != null);
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("Customer's log length should be 1.", 1, customer.log.size());
		assertTrue("Customer should be addressed.", customer.log.containsString("Received msgWhatDoYouWant"));
		assertFalse("Teller's scheduler should return false.", teller.runScheduler());
//		
//		// Release the waiterHover Semaphore so that each action will return before the next one is called.
//		waiter.waiterHover.release();
//		
		// Send a message from the customer to take an order. 
		teller.msgDeposit(12, 50);
		
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("Manager's log length should be 1.", 1, manager.log.size());
		assertTrue("Manager should be asked to try deposit.", manager.log.containsString("Received msgTryDeposit"));
		
		// Send a message from the customer to order Steak
		teller.msgTransactionSuccessful();
		
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("Customer's log length should be 2.", 2, customer.log.size());
		assertTrue("Customer should receive notification.", customer.log.containsString("Received msgDepositCompleted"));
		
		teller.msgDoneAndLeaving();
		
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("CurrentCustomer should equal null.", null, teller.currentCustomer);
		assertEquals("Manager's log length should be 2.", 2, manager.log.size());
		assertTrue("Manager should be notified that teller is available.", manager.log.containsString("Received msgAvailable"));

	}
	public void testNormativeWithdrawal(){
		assertEquals("Customers's log should be empty.", 0, customer.log.size());
		assertEquals("Teller should have no customer.", null, teller.currentCustomer);
		
		// Send a message from the host to seat a customer
		teller.msgAddressCustomer(customer);
		
		assertEquals("Teller should have a customer.", true, teller.currentCustomer != null);
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("Customer's log length should be 1.", 1, customer.log.size());
		assertTrue("Customer should be addressed.", customer.log.containsString("Received msgWhatDoYouWant"));
		assertFalse("Teller's scheduler should return false.", teller.runScheduler());
//		
		teller.msgWithdraw(0, 10, 10);
		
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("Manager's log length should be 1.", 1, manager.log.size());
		assertTrue("Manager should be asked to try deposit.", manager.log.containsString("Received msgWithdraw"));
		
		teller.msgTransactionSuccessful();
		
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("Customer's log length should be 2.", 2, customer.log.size());
		assertTrue("Customer should receive notification. His log reads instead: " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgHereIsWithdrawal 10"));
		
		teller.msgDoneAndLeaving();
		
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("CurrentCustomer should equal null.", null, teller.currentCustomer);
		assertEquals("Manager's log length should be 2.", 2, manager.log.size());
		assertTrue("Manager should be notified that teller is available.", manager.log.containsString("Received msgAvailable"));

	}
	public void testFailedWithdrawal(){
		assertEquals("Customers's log should be empty.", 0, customer.log.size());
		assertEquals("Teller should have no customer.", null, teller.currentCustomer);
		
		// Send a message from the host to seat a customer
		teller.msgAddressCustomer(customer);
		
		assertEquals("Teller should have a customer.", true, teller.currentCustomer != null);
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("Customer's log length should be 1.", 1, customer.log.size());
		assertTrue("Customer should be addressed.", customer.log.containsString("Received msgWhatDoYouWant"));
		assertFalse("Teller's scheduler should return false.", teller.runScheduler());
//		
		teller.msgWithdraw(0, 10, 10);
		
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("Manager's log length should be 1.", 1, manager.log.size());
		assertTrue("Manager should be asked to try deposit.", manager.log.containsString("Received msgWithdraw"));
		
		teller.msgWithdrawalFailed();
		
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("Manager's log length should be 2.", 2, manager.log.size());
		assertTrue("Manager should create loan. His log reads instead: " + manager.log.getLastLoggedEvent().toString(), manager.log.containsString("Received msgCreateLoan 10 2 0"));
		assertEquals("Customer's log length should be 2.", 2, customer.log.size());
		assertTrue("Customer should receive loan. His log reads instead: " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received msgLoanGranted 10"));
		
		teller.msgDoneAndLeaving();
		
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("CurrentCustomer should equal null.", null, teller.currentCustomer);
		assertEquals("Manager's log length should be 3.", 3, manager.log.size());
		assertTrue("Manager should be notified that teller is available.", manager.log.containsString("Received msgAvailable"));

	}
}
