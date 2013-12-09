package city.tests.roles;

import junit.framework.TestCase;
import city.Application;
import city.buildings.RestaurantTimmsBuilding;
import city.buildings.interfaces.RestaurantTimms;
import city.gui.exteriors.CityViewRestaurantTimms;
import city.roles.RestaurantTimmsCustomerRole;
import city.roles.interfaces.RestaurantTimmsCustomer;
import city.tests.agents.mocks.MockPerson;
import city.tests.animations.mocks.MockRestaurantTimmsAnimatedCustomer;
import city.tests.roles.mocks.MockRestaurantTimmsCashier;
import city.tests.roles.mocks.MockRestaurantTimmsHost;
import city.tests.roles.mocks.MockRestaurantTimmsWaiter;

public class RestaurantTimmsCustomerTest extends TestCase {
	
	MockRestaurantTimmsCashier cashier;
	MockRestaurantTimmsHost host;
	MockRestaurantTimmsWaiter waiter;
	
	RestaurantTimms rtb;
	
	MockPerson customerPerson;
	RestaurantTimmsCustomerRole customer;
	MockRestaurantTimmsAnimatedCustomer animation;

	public void setUp() throws Exception {
		super.setUp();
		
		cashier = new MockRestaurantTimmsCashier();
		host = new MockRestaurantTimmsHost();
		waiter = new MockRestaurantTimmsWaiter();
		
		rtb = new RestaurantTimmsBuilding("RestaurantTimms", null, new CityViewRestaurantTimms(0, 0));
		rtb.setCashier(cashier);
		rtb.setHost(host);
		
		customerPerson = new MockPerson("Customer");
		customerPerson.setCash(100);
		customer = new RestaurantTimmsCustomerRole();
		animation = new MockRestaurantTimmsAnimatedCustomer(customer);
		customer.setPerson(customerPerson);
		customer.setAnimation(animation);
		customer.setRestaurantTimmsBuilding(rtb);
	}
	
	public void testNormalScenario() throws InterruptedException {
		int startCash = customer.getPerson().getCash();
		
		// Preconditions
		assertEquals("Cashier's log should be empty.", 0, cashier.log.size());
		assertEquals("Host's log should be empty.", 0, host.log.size());
		assertEquals("Waiter's log should be empty.", 0, waiter.log.size());
		assertEquals("Customer's state should be none.", "none", customer.getState().toString());
		
		// Set the customer active (so he goes to the restaurant)
		customer.setActive();
		
		assertEquals("Customer's state should be goToRestaurant.", "goToRestaurant", customer.getState().toString());
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());
		assertEquals("Host's log length should be 1.", 1, host.log.size());
		assertTrue("Host should recognize new customer.", host.log.containsString("Received msgWantSeat from Customer."));
		assertEquals("Customer's state should be waitingInLine.", "waitingInLine", customer.getState().toString());
		
		// Send a message from a Waiter to move the customer to a table. This is also expected to cause the customer to read the menu.
		customer.msgGoToTable(waiter, 0);
		
		assertEquals("Customer's state should be goToTable.", "goToTable", customer.getState().toString());
		assertEquals("Waiter's log should be empty.", 0, waiter.log.size());
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());
		Thread.sleep((RestaurantTimmsCustomer.PICKINESS * 1000) + 1000); // Wait for the timer to finish
		assertEquals("Waiter's log length should be 1.", 1, waiter.log.size());
		assertTrue("Waiter should have been told to take customer's order.", waiter.log.containsString("Received msgWantFood from Customer."));
		assertEquals("Customer's state should be goToTable.", "goToTable", customer.getState().toString());
		
		// Send a message from a Waiter to take the customer's order
		customer.msgOrderFromWaiter();
		
		assertEquals("Customer's state should be orderFromWaiter.", "orderFromWaiter", customer.getState().toString());
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());
		assertEquals("Waiter's log length should be 2.", 2, waiter.log.size());
		assertTrue("Waiter should have received customer's order.", waiter.log.containsString("Received msgOrderFood from Customer. Item: " + customer.getOrderItem().toString())); // Item is also included in the response, but it is random
		assertEquals("Customer's state should be hasOrdered.", "hasOrdered", customer.getState().toString());
		
		// Send a message from a Waiter to deliver the customer's food. This is also expected to cause the customer to leave the restaurant.
		customer.msgWaiterDeliveredFood(Application.FOOD_ITEMS.steak); // The food that was ordered is random, the test delivers steak regardless of the order.
		
		assertEquals("Customer's state should be waiterDeliveredFood.", "waiterDeliveredFood", customer.getState().toString());
		assertFalse("Customer's scheduler should return false.", customer.runScheduler());
		Thread.sleep((RestaurantTimmsCustomer.HUNGER * 1000) + 1000); // Wait for the timer to finish
		assertEquals("Host's log length should be 2.", 2, host.log.size());
		assertTrue("Host should be notified customer is leaving.", host.log.containsString("Received msgLeaving from Customer. Table: 0"));
		assertEquals("Cashier's log length should be 1.", 1, cashier.log.size());
		assertTrue("Cashier should receive payment.", cashier.log.containsString("Received msgMakePayment from Customer. Money: " + customer.getPerson().getCash()));
		assertTrue("Customer should have been charged,", (startCash > customer.getPerson().getCash()));
		assertEquals("Customer's state should be none.", "none", customer.getState().toString());
	}
	
}