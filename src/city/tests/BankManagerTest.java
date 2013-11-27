package city.tests;

import junit.framework.TestCase;
import city.buildings.BankBuilding;
import city.roles.BankManagerRole;
import city.tests.mock.MockBankCustomer;
import city.tests.mock.MockBankTeller;
import city.tests.mock.MockPerson;

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
		this.manager = new BankManagerRole(b, 0, 12);
		manager.setPerson(managerPerson);
		b.setManager(manager);
	}
	
	public void testDirectDepositScenario() {

		// Preconditions
		assertEquals("Customers's log should be empty.", 0, customer.log.size());
		assertEquals("Manger should have no tasks.", 0, manager.bankTasks.size());
		
		// Send a message from the host to seat a customer
		manager.msgDirectDeposit(-1, 50, customer);
		
		assertEquals("Manager should have a bankTask.", true, manager.bankTasks.size() == 1);
		assertTrue("Manager's scheduler should return true.", manager.runScheduler());
		assertEquals("Customer's log length should be 1.", 1, customer.log.size());
		assertTrue("Customer should be informed of account creation.", customer.log.containsString("Received msgAccountCreated"));
	}
}
