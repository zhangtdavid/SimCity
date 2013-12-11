package city.tests.roles;

import junit.framework.TestCase;
import city.Application;
import city.Application.BUILDING;
import city.buildings.BankBuilding;
import city.buildings.RestaurantJPBuilding;
import city.roles.BankCustomerRole;
import city.roles.RestaurantJPWaiterRole;
import city.tests.agents.mocks.MockPerson;
import city.tests.roles.mocks.MockBankManager;
import city.tests.roles.mocks.MockBankTeller;

public class BankCustomerTest extends TestCase {

	MockBankTeller teller;
	MockBankManager manager;
	
	MockPerson customerPerson;
	BankCustomerRole customer;

	public void setUp() throws Exception {
		super.setUp();
		Application.CityMap.clearMap();
		this.teller = new MockBankTeller();
		this.manager = new MockBankManager();
		BankBuilding b = new BankBuilding("Bank", null, null);
		b.setManager(manager);
		Application.CityMap.addBuilding(BUILDING.bank, b);
		this.customerPerson = new MockPerson("Customer");
		customerPerson.setOccupation(new RestaurantJPWaiterRole(new RestaurantJPBuilding(null, null, null), 0, 12)); 
		this.customer = new BankCustomerRole(b);
		customer.setPerson(customerPerson);
		customerPerson.setCash(100);
	}
	
	public void testPersonalDirectDepositScenario() {
		assertEquals("Teller's log should be empty.", 0, teller.log.size());
		assertEquals("Manager's log should be empty.", 0, manager.log.size());
		assertEquals("Customer's cash should equal 100.", 100, customerPerson.getCash());
	
		// Send a message from the host to seat a customer
		customer.setActive(Application.BANK_SERVICE.atmDeposit, 50, Application.TRANSACTION_TYPE.personal);

		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Manager's log length should be 1.", 1, manager.log.size());
		assertTrue("Manager should be asked to direct deposit.", manager.log.containsString("Received msgDirectDeposit " + -1 + " " + 50 + " " + this.customer));
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());

		customer.msgAccountCreated(0);
		
		assertEquals("Customer's account number should equal 0", 0, customer.acctNum);
		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Customer's cash should equal 50.", 50, customerPerson.getCash());
	}
	
	public void testBusinessDirectDepositScenario() {
		BankBuilding b = new BankBuilding(null, null, null);
		b.setManager(manager);
		this.customer = new BankCustomerRole(new RestaurantJPBuilding(null, null, null), b);
		customer.setPerson(customerPerson);
		
		assertEquals("Teller's log should be empty.", 0, teller.log.size());
		assertEquals("Manager's log should be empty.", 0, manager.log.size());
		assertEquals("Customer's cash should equal 100.", 100, customerPerson.getCash());
	
		// Send a message from the host to seat a customer
		customer.setActive(Application.BANK_SERVICE.atmDeposit, 50, Application.TRANSACTION_TYPE.business);

		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Manager's log length should be 1.", 1, manager.log.size());
		assertTrue("Manager should be asked to direct deposit.", manager.log.containsString("Received msgDirectDeposit " + -1 + " " + 50 + " " + this.customer));
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());

		customer.msgAccountCreated(0);
		
		assertEquals("Customer's account number should equal 0", 0, customer.acctNum);
		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Customer's building's cash should equal 1950.", 1950, customer.getBusiness().getCash());
	}
	public void testInPersonAccountCreation(){
		assertEquals("Teller's log should be empty.", 0, teller.log.size());
		assertEquals("Manager's log should be empty.", 0, manager.log.size());
		assertEquals("Customer's cash should equal 100.", 100, customerPerson.getCash());
	
		// Send a message from the host to seat a customer
		customer.setActive(Application.BANK_SERVICE.deposit, 50, Application.TRANSACTION_TYPE.personal);

		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Manager's log length should be 1.", 1, manager.log.size());
		assertTrue("Manager should be notified of needed service.", manager.log.containsString("Received msgNeedService"));
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());

		customer.msgWhatDoYouWant(0, teller);
		
		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Teller's log length should be 1.", 1, teller.log.size());
		assertTrue("Teller should be asked to deposit.", teller.log.containsString("Received msgDeposit "+ 50 + " " + -1));
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());
		
		customer.msgAccountCreated(0);
		
		assertEquals("Customer's account number should equal 0", 0, customer.acctNum);
		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Customer's cash should equal 50.", 50, customerPerson.getCash());
	}
	
	public void testNormativeDeposit(){
		assertEquals("Teller's log should be empty.", 0, teller.log.size());
		assertEquals("Manager's log should be empty.", 0, manager.log.size());
		assertEquals("Customer's cash should equal 100.", 100, customerPerson.getCash());
	
		// Send a message from the host to seat a customer
		customer.setActive(Application.BANK_SERVICE.deposit, 50, Application.TRANSACTION_TYPE.personal);

		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Manager's log length should be 1.", 1, manager.log.size());
		assertTrue("Manager should be notified of needed service.", manager.log.containsString("Received msgNeedService"));
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());

		customer.msgWhatDoYouWant(0, teller);
		
		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Teller's log length should be 1.", 1, teller.log.size());
		assertTrue("Teller should be asked to deposit.", teller.log.containsString("Received msgDeposit "+ 50 + " " + -1));
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());
		
		customer.msgDepositCompleted();
		
		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Customer's cash should equal 50.", 50, customerPerson.getCash());
	}
	public void testNormativeWithdrawal(){
		assertEquals("Teller's log should be empty.", 0, teller.log.size());
		assertEquals("Manager's log should be empty.", 0, manager.log.size());
		assertEquals("Customer's cash should equal 100.", 100, customerPerson.getCash());
	
		// Send a message from the host to seat a customer
		customer.setActive(Application.BANK_SERVICE.moneyWithdraw, 50, Application.TRANSACTION_TYPE.personal);

		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Manager's log length should be 1.", 1, manager.log.size());
		assertTrue("Manager should be notified of needed service.", manager.log.containsString("Received msgNeedService"));
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());

		customer.msgWhatDoYouWant(0, teller);
		
		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Teller's log length should be 1.", 1, teller.log.size());
		assertTrue("Teller should be asked to deposit.", teller.log.containsString("Received msgWithdraw"));
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());
		
		customer.msgHereIsWithdrawal(50);
		
		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Customer's cash should equal 150.", 150, customerPerson.getCash());

	}
	
	public void testLoanGranted(){
		assertEquals("Teller's log should be empty.", 0, teller.log.size());
		assertEquals("Manager's log should be empty.", 0, manager.log.size());
		assertEquals("Customer's cash should equal 100.", 100, customerPerson.getCash());
	
		// Send a message from the host to seat a customer
		customer.setActive(Application.BANK_SERVICE.moneyWithdraw, 50, Application.TRANSACTION_TYPE.personal);

		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Manager's log length should be 1.", 1, manager.log.size());
		assertTrue("Manager should be notified of needed service.", manager.log.containsString("Received msgNeedService"));
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());

		customer.msgWhatDoYouWant(0, teller);
		
		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Teller's log length should be 1.", 1, teller.log.size());
		assertTrue("Teller should be asked to deposit.", teller.log.containsString("Received msgWithdraw"));
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());
		
		customer.msgLoanGranted(50);
		
		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Customer's cash should equal 150.", 150, customerPerson.getCash());

	}
	
	public void testTransactionDenied(){
		assertEquals("Teller's log should be empty.", 0, teller.log.size());
		assertEquals("Manager's log should be empty.", 0, manager.log.size());
		assertEquals("Customer's cash should equal 100.", 100, customerPerson.getCash());
	
		// Send a message from the host to seat a customer
		customer.setActive(Application.BANK_SERVICE.moneyWithdraw, 50, Application.TRANSACTION_TYPE.personal);

		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Manager's log length should be 1.", 1, manager.log.size());
		assertTrue("Manager should be notified of needed service.", manager.log.containsString("Received msgNeedService"));
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());

		customer.msgWhatDoYouWant(0, teller);
		
		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Teller's log length should be 1.", 1, teller.log.size());
		assertTrue("Teller should be asked to deposit.", teller.log.containsString("Received msgWithdraw"));
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());
		
		customer.msgTransactionDenied();
		
		assertTrue("Customer's scheduler should return true.", customer.runScheduler());
		assertEquals("Customer should not receive cash.", 100, customerPerson.getCash());
	}
}
