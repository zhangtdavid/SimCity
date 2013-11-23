package city.tests;

import city.buildings.BankBuilding;
import city.roles.BankTellerRole;
import city.tests.mock.MockBankCustomer;
import city.tests.mock.MockBankManager;
import city.tests.mock.MockPerson;
import junit.framework.TestCase;

public class BankTellerTest extends TestCase {

	MockBankCustomer customer;
	MockBankManager manager;
	
	MockPerson tellerPerson;
	BankTellerRole teller;

	public void setUp() throws Exception {
		super.setUp();
		BankBuilding b = new BankBuilding("Bank");
		this.customer = new MockBankCustomer();
		this.manager = new MockBankManager();
		b.setManager(manager);
		this.tellerPerson = new MockPerson("Teller");
		this.teller = new BankTellerRole(b);
		teller.setPerson(tellerPerson);
	}
	
	public void testNormalScenario() {
		int steakPrice = 0 ; // TODO CookAgent.getMenuItemPrice(MarketAgent.StockItem.Steak);
		
		// Preconditions
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
		teller.msgDeposit(-1, 50);
		
		assertTrue("Teller's scheduler should return true.", teller.runScheduler());
		assertEquals("Customer's log length should be 2.", 2, customer.log.size());
		assertTrue("Manager should be asked to try deposit.", manager.log.containsString("Received msgTryDeposit"));
		
		// Send a message from the customer to order Steak
		/*waiter.msgOrderFood(customer, Application.MARKET_ITEM.steak);
		
		assertTrue("Waiter's scheduler should return true.", waiter.runScheduler());
		assertEquals("InternalCustomer's choice should be Steak.", Application.MARKET_ITEM.steak, waiter.customers.get(0).getStockItem());
		assertEquals("Cook's log length should be 1.", 1, cook.log.size());
		assertTrue("Cook should receive request to make Steak.", cook.log.containsString("Received msgCookOrder from Waiter. Item: steak"));
		
		// Send a message from the Cook acknowledging that the order has been received 
		waiter.msgOrderPlaced(customer, true);
		
		assertFalse("Waiter's scheduler should return false.", waiter.runScheduler());
		assertEquals("InternalCustomer's state should be makingFood.", InternalCustomer.State.makingFood, waiter.customers.get(0).getState());
		
		// Send a message from the Cook indicating food is ready
		waiter.msgFoodReady(customer);
		
		assertTrue("Waiter's scheduler should return true.", waiter.runScheduler());
		assertEquals("Cook's log length should be 2.", 2, cook.log.size());
		assertTrue("Cook should receive request to pick up order.", cook.log.containsString("Received msgPickUpOrder from Waiter."));
		assertEquals("Customer's log length should be 3.", 3, customer.log.size());
		assertTrue("Customer should receive Steak.", customer.log.containsString("Received msgWaiterDeliveredFood. Item: steak"));
		assertEquals("Cashier's log length should be 1.", 1, cashier.log.size());
		assertTrue("Cashier should be asked to compute check.", cashier.log.containsString("Received msgComputeCheck from Waiter. Amount: " + steakPrice));
		
		// Send a message from the Cashier indicating the check is ready
		waiter.msgCheckReady();
		
		assertEquals("Waiter should receive check.", "msgCheckReady", waiter.lastMessage);
	*/}

	
}
