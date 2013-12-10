package city.tests.roles;

import junit.framework.TestCase;
import city.Application;
import city.buildings.RestaurantTimmsBuilding;
import city.gui.exteriors.CityViewRestaurantZhang;
import city.roles.RestaurantTimmsWaiterRole;
import city.roles.RestaurantTimmsWaiterRole.InternalCustomer;
import city.tests.agents.mocks.MockPerson;
import city.tests.animations.mocks.MockRestaurantTimmsAnimatedWaiter;
import city.tests.roles.mocks.MockRestaurantTimmsCashier;
import city.tests.roles.mocks.MockRestaurantTimmsCook;
import city.tests.roles.mocks.MockRestaurantTimmsCustomer;

public class RestaurantTimmsWaiterTest extends TestCase {
	
	MockRestaurantTimmsCashier cashier;
	MockRestaurantTimmsCook cook;
	MockRestaurantTimmsCustomer customer;
	
	RestaurantTimmsBuilding rtb;
	
	MockPerson waiterPerson;
	RestaurantTimmsWaiterRole waiter;
	MockRestaurantTimmsAnimatedWaiter animation;

	public void setUp() throws Exception {
		super.setUp();
		
		cashier = new MockRestaurantTimmsCashier();
		cook = new MockRestaurantTimmsCook();
		customer = new MockRestaurantTimmsCustomer();
		
		rtb = new RestaurantTimmsBuilding("RestaurantTimms", null, new CityViewRestaurantZhang(0, 0));

		rtb.setCashier(cashier);
		rtb.setCook(cook);
		
		waiterPerson = new MockPerson("Waiter");
		waiter = new RestaurantTimmsWaiterRole(rtb, 0, 23);
		animation = new MockRestaurantTimmsAnimatedWaiter(waiter);
		waiter.setPerson(waiterPerson);
		waiter.setAnimation(animation);
	}
	
	public void testNormalScenario() {
		int steakPrice = cook.getMenuItemPrice(Application.FOOD_ITEMS.steak);
		
		// Preconditions
		assertEquals("Cashier's log should be empty.", 0, cashier.log.size());
		assertEquals("Customers's log should be empty.", 0, customer.log.size());
		assertEquals("Cook's log should be empty.", 0, cook.log.size());
		assertEquals("Waiter should have no customers.", 0, waiter.getCustomers().size());
		
		// Send a message from the host to seat a customer
		waiter.msgSeatCustomer(customer, 1);
		
		assertEquals("Waiter should have 1 customer.", 1, waiter.getCustomers().size());
		assertTrue("Waiter's scheduler should return true.", waiter.runScheduler());
		assertEquals("Customer's log length should be 1.", 1, customer.log.size());
		assertTrue("Customers should be seated.", customer.log.containsString("Received msgGoToTable. Position: 1"));
		assertEquals("InternalCustomer's table should be 1.", 1, waiter.getCustomers().get(0).getTableNumber());
		assertFalse("Waiter's scheduler should return false.", waiter.runScheduler());
		
		// Release the waiterHover Semaphore so that each action will return before the next one is called.
		waiter.waiterHover.release();

		// Send a message from the customer to take an order. 
		waiter.msgWantFood(customer);
		waiter.waiterHover.release();
		
		assertTrue("Waiter's scheduler should return true.", waiter.runScheduler());
		assertEquals("Customer's log length should be 2.", 2, customer.log.size());
		assertTrue("Customer should be asked for order.", customer.log.containsString("Received msgOrderFromWaiter."));
		
		// Send a message from the customer to order Steak
		waiter.msgOrderFood(customer, Application.FOOD_ITEMS.steak);
		
		assertTrue("Waiter's scheduler should return true.", waiter.runScheduler());
		assertEquals("InternalCustomer's choice should be Steak.", Application.FOOD_ITEMS.steak, waiter.getCustomers().get(0).getStockItem());
		assertEquals("Cook's log length should be 1.", 1, cook.log.size());
		assertTrue("Cook should receive request to make Steak.", cook.log.containsString("Received msgCookOrder from Waiter. Item: steak"));
		
		// Send a message from the Cook acknowledging that the order has been received 
		waiter.msgOrderPlaced(customer, true);
		
		assertFalse("Waiter's scheduler should return false.", waiter.runScheduler());
		assertEquals("InternalCustomer's state should be makingFood.", InternalCustomer.State.makingFood, waiter.getCustomers().get(0).getState());
		
		// Send a message from the Cook indicating food is ready
		waiter.msgFoodReady(customer);
		
		assertTrue("Waiter's scheduler should return true.", waiter.runScheduler());
		assertEquals("Cook's log length should be 2.", 2, cook.log.size());
		assertTrue("Cook should receive request to pick up order.", cook.log.containsString("Received msgPickUpOrder from Waiter."));
		assertEquals("Customer's log length should be 3.", 3, customer.log.size());
		assertTrue("Customer should receive Steak.", customer.log.containsString("Received msgWaiterDeliveredFood. Item: steak"));
		assertEquals("Cashier's log length should be 1.", 1, cashier.log.size());
		assertTrue("Cashier should be asked to compute check.", cashier.log.containsString("Received msgComputeCheck from Waiter. Amount: " + steakPrice));
	}

}
