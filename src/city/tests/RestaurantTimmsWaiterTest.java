//package city.tests;
//
//import junit.framework.TestCase;
//import city.Application;
//import city.roles.RestaurantTimmsWaiterRole;
//import city.roles.RestaurantTimmsWaiterRole.InternalCustomer;
//import city.tests.animations.mock.MockRestaurantTimmsAnimatedWaiter;
//import city.tests.mock.MockPerson;
//import city.tests.mock.MockRestaurantTimmsCashier;
//import city.tests.mock.MockRestaurantTimmsCook;
//import city.tests.mock.MockRestaurantTimmsCustomer;
//import city.tests.mock.MockRestaurantTimmsHost;
//
//public class RestaurantTimmsWaiterTest extends TestCase {
//	
//	MockRestaurantTimmsCashier cashier;
//	MockRestaurantTimmsCook cook;
//	MockRestaurantTimmsCustomer customer;
//	MockRestaurantTimmsHost host;
//	
//	MockPerson waiterPerson;
//	RestaurantTimmsWaiterRole waiter;
//	MockRestaurantTimmsAnimatedWaiter animation;
//	
//// TODO Needed for setting menu prices. Will city market do this or..?
////		CookAgent cookAgent;
//
//	public void setUp() throws Exception {
//		super.setUp();
//		this.cashier = new MockRestaurantTimmsCashier();
//		this.cook = new MockRestaurantTimmsCook();
//		this.customer = new MockRestaurantTimmsCustomer();
//		this.host = new MockRestaurantTimmsHost();
//		this.waiterPerson = new MockPerson("Waiter");
//		//this.waiter = new RestaurantTimmsWaiterRole(cook, host, cashier, 0);//TODO commented by ryan choi, error was bugging me
//		this.animation = new MockRestaurantTimmsAnimatedWaiter(waiter);
//		//waiter.setAnimation(animation);//TODO commented by ryan choi, error was bugging me
//		waiter.setPerson(waiterPerson);
//	
//		// TODO Required to set prices for menu items
//		// cookAgent = new CookAgent("Cook", cashier);
//	}
//	
//	public void testNormalScenario() {
//		int steakPrice = 0 ; // TODO CookAgent.getMenuItemPrice(MarketAgent.StockItem.Steak);
//		
//		// Preconditions
//		assertEquals("Cashier's log should be empty.", 0, cashier.log.size());
//		assertEquals("Customers's log should be empty.", 0, customer.log.size());
//		assertEquals("Cook's log should be empty.", 0, cook.log.size());
//		assertEquals("Waiter should have no customers.", 0, waiter.customers.size());
//		
//		// Send a message from the host to seat a customer
//		waiter.msgSeatCustomer(customer, 1);
//		
//		assertEquals("Waiter should have 1 customer.", 1, waiter.customers.size());
//		assertTrue("Waiter's scheduler should return true.", waiter.runScheduler());
//		assertEquals("Customer's log length should be 1.", 1, customer.log.size());
//		assertTrue("Customers should be seated.", customer.log.containsString("Received msgGoToTable. Position: 1"));
//		assertEquals("InternalCustomer's table should be 1.", 1, waiter.customers.get(0).getTableNumber());
//		assertFalse("Waiter's scheduler should return false.", waiter.runScheduler());
//	}
//}