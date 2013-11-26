package city.tests;

import city.buildings.BankBuilding;
import city.roles.BankManagerRole;
import city.roles.BankTellerRole;
import city.tests.mock.MockBankCustomer;
import city.tests.mock.MockBankManager;
import city.tests.mock.MockBankTeller;
import city.tests.mock.MockPerson;
import junit.framework.TestCase;

public class BankManagerTest extends TestCase {

	MockBankCustomer customer;
	MockBankTeller teller;
	
	MockPerson managerPerson;
	BankManagerRole manager;

	public void setUp() throws Exception {
		super.setUp();
		BankBuilding b = new BankBuilding("Bank");
		this.customer = new MockBankCustomer();
		this.teller = new MockBankTeller();
		this.managerPerson = new MockPerson("Manager");
		this.manager = new BankManagerRole(b);
		teller.setPerson(managerPerson);
		b.setManager(manager);
	}
	
	/*public void testDirectDepositScenario() {

		// Preconditions
		assertEquals("Customers's log should be empty.", 0, customer.log.size());
		assertEquals("Manger should have no tasks.", 0, manager.bankTasks.getSize());
		
		// Send a message from the host to seat a customer
		manager.msgDirectDeposit(customer);
		
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
		assertEquals("Manager's log length should be 1.", 1, manager.log.size());
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
		assertEquals("Manager's log length should be 2.", 2, manager.log.size());
		assertTrue("Manager should be notified that teller is available.", manager.log.containsString("Received msgAvailable"));
	}*/
}
