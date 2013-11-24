package city.tests;

import junit.framework.TestCase;
import city.Application.FOOD_ITEMS;
import city.roles.RestaurantTimmsCustomerRole;
import city.tests.animations.mock.MockRestaurantTimmsAnimatedCustomer;
import city.tests.mock.MockPerson;
import city.tests.mock.MockRestaurantTimmsCashier;
import city.tests.mock.MockRestaurantTimmsHost;
import city.tests.mock.MockRestaurantTimmsWaiter;

public class RestaurantTimmsCustomerTest extends TestCase {
	
	MockRestaurantTimmsCashier cashier;
	MockRestaurantTimmsHost host;
	MockRestaurantTimmsWaiter waiter;
	
	MockPerson customerPerson;
	RestaurantTimmsCustomerRole customer;
	MockRestaurantTimmsAnimatedCustomer animation;
	
// TODO Needed for setting menu prices. Will city market do this or..?
//	CookAgent cookAgent;

	public void setUp() throws Exception {
		super.setUp();
		this.cashier = new MockRestaurantTimmsCashier();
		this.host = new MockRestaurantTimmsHost();
		this.waiter = new MockRestaurantTimmsWaiter();
		this.customerPerson = new MockPerson("Customer");
		this.customer = new RestaurantTimmsCustomerRole(host, cashier);
		this.animation = new MockRestaurantTimmsAnimatedCustomer(customer);
		
		customer.setPerson(customerPerson);
		customer.setAnimation(animation);
		customer.setPerson(customerPerson);
		
		// TODO Required to set prices for menu items
		// cookAgent = new CookAgent("Cook", cashier);
	}
	
	public void testNormalScenario() throws InterruptedException {
		// Preconditions
		assertEquals("Cashier's log should be empty.", 0, cashier.log.size());
		assertEquals("Host's log should be empty.", 0, host.log.size());
		assertEquals("Waiter's log should be empty.", 0, waiter.log.size());
		assertEquals("Customer's state should be none.", "none", customer.getState());
		
		// Send a message to send the customer to the restaurant.
		customer.msgGoToRestaurant();
		
		assertEquals("Customer's state should be goToRestaurant.", "goToRestaurant", customer.getState());
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());
		assertEquals("Host's log length should be 1.", 1, host.log.size());
		assertTrue("Host should recognize new customer.", host.log.containsString("Received msgWantSeat from Customer."));
		assertEquals("Customer's state should be waitingInLine.", "waitingInLine", customer.getState());
		
		// Send a message from a Waiter to move the customer to a table. This is also expected to cause the customer to read the menu.
		customer.msgGoToTable(waiter, 0);
		
		assertEquals("Customer's state should be goToTable.", "goToTable", customer.getState());
		assertEquals("Waiter's log should be empty.", 0, waiter.log.size());
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());
		Thread.sleep((customer.pickiness * 1000) + 1000); // Wait for the timer to finish
		assertEquals("Waiter's log length should be 1.", 1, waiter.log.size());
		assertTrue("Waiter should have been told to take customer's order.", waiter.log.containsString("Received msgWantFood from Customer."));
		assertEquals("Customer's state should be goToTable.", "goToTable", customer.getState());
		
		// Send a message from a Waiter to take the customer's order
		customer.msgOrderFromWaiter();
		
		assertEquals("Customer's state should be orderFromWaiter.", "orderFromWaiter", customer.getState());
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());
		assertEquals("Waiter's log length should be 2.", 2, waiter.log.size());
		assertTrue("Waiter should have received customer's order.", waiter.log.containsString("Received msgOrderFood from Customer. Item: " + customer.orderItem.toString())); // Item is also included in the response, but it is random
		assertEquals("Customer's state should be hasOrdered.", "hasOrdered", customer.getState());
		
		// Send a message from a Waiter to deliver the customer's food. This is also expected to cause the customer to leave the restaurant.
		customer.msgWaiterDeliveredFood(FOOD_ITEMS.steak); // The food that was ordered is random, the test delivers steak regardless of the order.
		
		assertEquals("Customer's state should be waiterDeliveredFood.", "waiterDeliveredFood", customer.getState());
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());
		Thread.sleep((customer.hunger * 1000) + 1000); // Wait for the timer to finish
		assertEquals("Host's log length should be 2.", 2, host.log.size());
		assertTrue("Host should be notified customer is leaving.", host.log.containsString("Received msgLeaving from Customer. Table: 0"));
		assertEquals("Cashier's log length should be 1.", 1, cashier.log.size());
		assertTrue("Cashier should receive payment.", cashier.log.containsString("Received msgMakePayment from Customer. Money: " + customer.money));
		assertEquals("Customer's state should be none.", "none", customer.getState());
	}
	
}
