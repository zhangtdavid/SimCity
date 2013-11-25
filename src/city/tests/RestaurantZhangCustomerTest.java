package city.tests;

import junit.framework.TestCase;
import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import city.animations.RestaurantZhangCustomerAnimation;
import city.roles.RestaurantZhangCustomerRole;
import city.tests.animations.mock.MockRestaurantZhangAnimatedCustomer;
import city.tests.mock.MockPerson;
import city.tests.mock.MockRestaurantZhangCashier;
import city.tests.mock.MockRestaurantZhangCustomer;
import city.tests.mock.MockRestaurantZhangHost;
import city.tests.mock.MockRestaurantZhangWaiterRegular;

/**
 * Restaurant customer Agent
 */

public class RestaurantZhangCustomerTest extends TestCase {
	RestaurantZhangCustomerRole customer;
	MockRestaurantZhangAnimatedCustomer anim;
	MockRestaurantZhangHost host;
	MockRestaurantZhangWaiterRegular waiter;
	MockRestaurantZhangCashier cashier;
	RestaurantZhangMenu menu;

	public void setUp() throws Exception {
		super.setUp();
		customer = new RestaurantZhangCustomerRole("Customer");
		anim = new MockRestaurantZhangAnimatedCustomer(customer); 
		host = new MockRestaurantZhangHost();
		waiter = new MockRestaurantZhangWaiterRegular();
		cashier = new MockRestaurantZhangCashier();
		menu = new RestaurantZhangMenu();
		
		customer.setPerson(new MockPerson("Person"));
		customer.setHost(host);
		customer.setCashier(cashier);
		customer.setAnimation(anim);
	}

	public void testOneNormalCustomerScenario() { // Tests when customer has more than enough money to pay
		// Check to make sure customer's initial state is okay
		assertEquals("customer should have no table. It doesn't.", customer.myTable, null);
		assertEquals("customer should have no check. It doesn't.", customer.myCheck, null);
		assertTrue("customer should have money. It doesn't.", customer.money > 0);
		assertTrue("customer should have no tab. It doesn't.", customer.myTab <= 0);
		assertEquals("customer's state should be doing nothing. Instead it's " + customer.state.name(), customer.state, RestaurantZhangCustomerRole.AgentState.DoingNothing);
		assertEquals("customer's event should be none. Instead it's " + customer.state.name(), customer.event, RestaurantZhangCustomerRole.AgentEvent.none);
		assertTrue("customer should have 0 customers with tabs. It doesn't.", customer.tabCustomers.isEmpty());
		assertFalse("customer should have a menu of items. It doesn't.", customer.menu.isEmpty());
		assertEquals("customer should have 10000 balance. It doesn't.", 10000, Math.round(customer.balance));
		//		assertTrue("customer should have 0 market bills. It doesn't.", customer.marketBills.isEmpty());

		// Step 1 of the test, ask for bill
		customer.msgComputeBill(waiter, customer, "Chicken"); //send the message from a waiter
		check = customer.pendingChecks.get(0);

		// Check postconditions for step 1 and preconditions for step 2
		assertEquals("customer should have 1 check in it. It doesn't.", customer.pendingChecks.size(), 1);
		assertTrue("customer's scheduler should have returned true (process check from waiter), but didn't.",
				customer.runScheduler());
		assertTrue("customer should have 0 checks in it. It doesn't.", customer.pendingChecks.isEmpty());
		assertTrue("Waiter should have logged \"Received check from customer.\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received check from customer."));
		assertTrue("Customer should have logged \"Received check from waiter.\" but didn't. His log reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), customer.log.containsString("Received check from waiter."));
		assertTrue("customer should have 0 customers with tabs. It doesn't.", customer.tabCustomers.isEmpty());
		assertEquals("customer should have 10000 balance. It doesn't.", 10000, Math.round(customer.balance));
		//		assertTrue("customer should have 0 market bills. It doesn't.", customer.marketBills.isEmpty());

		// Step 2 of the test, give payment to customer
		customer.msgHereIsPayment(check, 20); // Sent from customer

		//check postconditions for step 2
		assertEquals("customer should have 1 check in it. It doesn't.", 1, customer.pendingChecks.size());
		assertEquals("Check status should be atCustomer. It isn't.", check.status, RestaurantZhangCheck.CheckStatus.atCustomer);
		assertEquals("Check should have 20 in payment. It doesn't.", Math.round(check.payment), 20);
		assertTrue("customer's scheduler should have returned true (process check from customer), but didn't.",
				customer.runScheduler());
		assertTrue("customer's list of checks should be empty. It isn't.", customer.pendingChecks.isEmpty());
		assertTrue("customer should have 0 customers with tabs. It doesn't.", customer.tabCustomers.isEmpty());
		assertTrue("Customer log should have read \"Got change 11.01 from customer.\" It says instead: " + customer.log.getLastLoggedEvent(), customer.log.containsString("Got change 11.01 from customer."));
		assertEquals("customer should have 10000 balance. It instead has " + customer.balance, 10008.99, customer.balance);
		//		assertTrue("customer should have 0 market bills. It doesn't.", customer.marketBills.isEmpty());
	}
}