/*package city.tests;

import junit.framework.TestCase;
import city.Application;
import city.roles.RestaurantTimmsWaiterRole;
import city.roles.RestaurantTimmsWaiterRole.InternalCustomer;
import city.tests.animations.mock.MockRestaurantTimmsAnimatedWaiter;
import city.tests.mock.MockPerson;
import city.tests.mock.MockRestaurantTimmsCashier;
import city.tests.mock.MockRestaurantTimmsCook;
import city.tests.mock.MockRestaurantTimmsCustomer;
import city.tests.mock.MockRestaurantTimmsHost;

public class RestaurantTimmsWaiterTest extends TestCase {
	
	MockRestaurantTimmsCashier cashier;
	MockRestaurantTimmsCook cook;
	MockRestaurantTimmsCustomer customer;
	MockRestaurantTimmsHost host;
	
	MockPerson waiterPerson;
	RestaurantTimmsWaiterRole waiter;
	MockRestaurantTimmsAnimatedWaiter animation;
	
// TODO Needed for setting menu prices. Will city market do this or..?
//		CookAgent cookAgent;

	public void setUp() throws Exception {
		super.setUp();
		this.cashier = new MockRestaurantTimmsCashier();
		this.cook = new MockRestaurantTimmsCook();
		this.customer = new MockRestaurantTimmsCustomer();
		this.host = new MockRestaurantTimmsHost();
		this.waiterPerson = new MockPerson("Waiter");
		//this.waiter = new RestaurantTimmsWaiterRole(cook, host, cashier, 0);//TODO commented by ryan choi, error was bugging me
		this.animation = new MockRestaurantTimmsAnimatedWaiter(waiter);
		//waiter.setAnimation(animation);//TODO commented by ryan choi, error was bugging me
		waiter.setPerson(waiterPerson);
	
		// TODO Required to set prices for menu items
		// cookAgent = new CookAgent("Cook", cashier);
	}
	
	public void testNormalScenario() {
		int steakPrice = 0 ; // TODO CookAgent.getMenuItemPrice(MarketAgent.StockItem.Steak);
		
		// Preconditions
		assertEquals("Cashier's log should be empty.", 0, cashier.log.size());
		assertEquals("Customers's log should be empty.", 0, customer.log.size());
		assertEquals("Cook's log should be empty.", 0, cook.log.size());
		assertEquals("Waiter should have no customers.", 0, waiter.customers.size());
		
		// Send a message from the host to seat a customer
		waiter.msgSeatCustomer(customer, 1);
		
		assertEquals("Waiter should have 1 customer.", 1, waiter.customers.size());
		assertTrue("Waiter's scheduler should return true.", waiter.runScheduler());
		assertEquals("Customer's log length should be 1.", 1, customer.log.size());
		assertTrue("Customers should be seated.", customer.log.containsString("Received msgGoToTable. Position: 1"));
		assertEquals("InternalCustomer's table should be 1.", 1, waiter.customers.get(0).getTableNumber());
		assertFalse("Waiter's scheduler should return false.", waiter.runScheduler());
//		
//		// Release the waiterHover Semaphore so that each action will return before the next one is called.
//		waiter.waiterHover.release();
//		
		// Send a message from the customer to take an order. 
		waiter.msgWantFood(customer);
		waiter.waiterHover.release();
		
		assertTrue("Waiter's scheduler should return true.", waiter.runScheduler());
		assertEquals("Customer's log length should be 2.", 2, customer.log.size());
		assertTrue("Customer should be asked for order.", customer.log.containsString("Received msgOrderFromWaiter."));
		
		// Send a message from the customer to order Steak
		waiter.msgOrderFood(customer, Application.FOOD_ITEMS.steak);
		
		assertTrue("Waiter's scheduler should return true.", waiter.runScheduler());
		assertEquals("InternalCustomer's choice should be Steak.", Application.FOOD_ITEMS.steak, waiter.customers.get(0).getStockItem());
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
	}

}
*/