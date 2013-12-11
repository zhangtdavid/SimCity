package city.tests.roles;

import junit.framework.TestCase;
import city.buildings.BankBuilding;
import city.buildings.BankBuilding.Account;
import city.buildings.BankBuilding.Loan;
import city.roles.BankManagerRole;
import city.tests.agents.mocks.MockPerson;
import city.tests.roles.mocks.MockBankCustomer;
import city.tests.roles.mocks.MockBankTeller;

public class BankManagerTest extends TestCase {

	MockBankCustomer customer;
	MockBankTeller teller;
	
	MockPerson managerPerson;
	BankManagerRole manager;

	public void setUp() throws Exception {
		super.setUp();
		BankBuilding b = new BankBuilding("Bank", null, null);
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
		assertEquals("Manger should have no tasks.", 0, manager.getBankTasks().size());
		
		// Send a message from the host to seat a customer
		manager.msgDirectDeposit(-1, 50, customer);
		
		assertEquals("Manager should have a bankTask.", true, manager.getBankTasks().size() == 1);
		assertTrue("Manager's scheduler should return true.", manager.runScheduler());
		assertEquals("Customer's log length should be 1.", 1, customer.log.size());
		assertTrue("Customer should be informed of account creation.", customer.log.containsString("Received msgAccountCreated"));
		assertTrue("New account should exist in building accounts.", manager.building.accounts.get(0).balance == 50);
		
		manager.msgDirectDeposit(1, 50, customer);
		
		assertEquals("Manager should have a bankTask.", true, manager.getBankTasks().size() == 1);
		assertTrue("Manager's scheduler should return true.", manager.runScheduler());
		assertEquals("Customer's log length should be 2.", 2, customer.log.size());
		assertTrue("Customer should be informed of account creation.", customer.log.containsString("Received msgAccountCreated"));
		assertTrue("New account should exist in building accounts.", manager.building.accounts.get(0).balance == 100);
	}
	
	public void testNormativeAcctCreateThenWithdrawalScenario(){
		assertEquals("Customers's log should be empty.", 0, customer.log.size());
		assertEquals("Manger should have no tasks.", 0, manager.bankTasks.size());
		
		
		manager.myTellers.add(manager.new MyTeller(teller));
		manager.msgAvailable(teller);
		manager.msgNeedService(customer);
		
		
		assertEquals("Manager should have a waiting customer.", true, manager.customers.size() == 1);
		assertTrue("Manager's scheduler should return true.", manager.runScheduler());
		assertEquals("Manager should not have any waiting customers,", true, manager.customers.size() == 0);
		assertEquals("Teller's log length should be 1.", 1, teller.log.size());
		assertTrue("Teller should be told to address customer. His log reads instead: " + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Received msgAddressCustomer"));
		
		
		// Send a message from the host to seat a customer
		manager.msgTryDeposit(50, -1, teller);
		
		assertEquals("Manager should have a bankTask.", true, manager.bankTasks.size() == 1);
		assertTrue("Manager's scheduler should return true.", manager.runScheduler());
		assertEquals("Teller's log length should be 2.", 2, teller.log.size());
		assertTrue("Teller should be informed of account creation. His log reads instead: " + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Received msgHereIsAccount 1"));
		
		manager.msgWithdraw(1, 50, teller);
		
		assertEquals("Manager should have a bankTask.", true, manager.bankTasks.size() == 1);
		assertTrue("Manager's scheduler should return true.", manager.runScheduler());
		assertEquals("Teller's log length should be 3.", 3, teller.log.size());
		assertTrue("Teller should be allowed to give withdrawal. His log reads instead: " + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Received msgTransactionSuccessful"));
		assertTrue("Account funds should be depleted.", manager.building.accounts.get(0).balance == 0);
	}
	
	public void testNormativeDepositandLoanPayment(){
		assertEquals("Customers's log should be empty.", 0, customer.log.size());
		assertEquals("Manger should have no tasks.", 0, manager.bankTasks.size());
		manager.building.accounts.add(new Account(2, 0));
		manager.building.loans.add(new Loan(50, 50, 2));
		
		manager.msgAvailable(teller);
		manager.msgNeedService(customer);
		
		assertEquals("Manager should have a waiting customer.", true, manager.customers.size() == 1);
		assertTrue("Manager's scheduler should return true.", manager.runScheduler());
		assertEquals("Manager should not have any waiting customers,", true, manager.customers.size() == 0);
		assertEquals("Teller's log length should be 1.", 1, teller.log.size());
		assertTrue("Teller should be told to address customer. His log reads instead: " + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Received msgAddressCustomer"));
		
		// Send a message from the host to seat a customer
		manager.msgTryDeposit(50, 2, teller);
		
		assertEquals("Manager should have a bankTask.", true, manager.bankTasks.size() == 1);
		assertTrue("Manager's scheduler should return true.", manager.runScheduler());
		assertEquals("Teller's log length should be 2.", 2, teller.log.size());
		assertTrue("Teller should be informed of successful deposit. His log reads instead: " + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Received msgTransactionSuccessful"));
	}
	
	public void testWithdrawalFailed(){
		assertEquals("Customers's log should be empty.", 0, customer.log.size());
		assertEquals("Manger should have no tasks.", 0, manager.bankTasks.size());
		
		// Send a message from the host to seat a customer
		manager.msgTryDeposit(50, -1, teller);
		
		assertEquals("Manager should have a bankTask.", true, manager.bankTasks.size() == 1);
		assertTrue("Manager's scheduler should return true.", manager.runScheduler());
		assertEquals("Teller's log length should be 1.", 1, teller.log.size());
		assertTrue("Teller should be informed of account creation. His log reads instead: " + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Received msgHereIsAccount 1"));
		
		manager.msgWithdraw(1, 80, teller);
		
		assertEquals("Manager should have a bankTask.", true, manager.bankTasks.size() == 1);
		assertTrue("Manager's scheduler should return true.", manager.runScheduler());
		assertEquals("Teller's log length should be 2.", 2, teller.log.size());
		assertTrue("Teller should be notified of failed withdrawal. His log reads instead: " + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Received msgWithdrawalFailed"));
		assertTrue("Account funds should not be depleted.", manager.building.accounts.get(0).balance == 50);
	}
	
	public void testCreateLoan(){
		assertEquals("Customers's log should be empty.", 0, customer.log.size());
		assertEquals("Manger should have no tasks.", 0, manager.bankTasks.size());
		manager.building.accounts.add(new Account(1, 0));
		// Send a message from the host to seat a customer
		manager.msgCreateLoan(0, 50, 1);
		
		manager.msgTryDeposit(80, 1, teller);
		
		assertEquals("Manager should have a bankTask.", true, manager.bankTasks.size() == 1);
		assertTrue("Manager's scheduler should return true.", manager.runScheduler());
		assertEquals("Teller's log length should be 1.", 1, teller.log.size());
		assertTrue("No loans should exist.", manager.building.loans.size() == 0);
	}
	
	public void testNoAvailableTellers(){
		assertEquals("Customers's log should be empty.", 0, customer.log.size());
		assertEquals("Manger should have no tasks.", 0, manager.bankTasks.size());
		
		
		manager.myTellers.add(manager.new MyTeller(teller));
		manager.msgUnavailable(teller);
		manager.msgNeedService(customer);
		
		
		assertEquals("Manager should have a waiting customer.", true, manager.customers.size() == 1);
		assertFalse("Manager's scheduler should return false.", manager.runScheduler());
		assertEquals("Manager should still have a waiting customer,", true, manager.customers.size() == 1);
	}
	
	public void testSetInactive(){
		manager.building.setManager(new BankManagerRole(manager.building, 0, 12));	
		manager.setInactive();
	}
	public void testDelayedSetInactive(){
		manager.setInactive();
		manager.building.setManager(new BankManagerRole(manager.building, 0, 12));
	}
}
