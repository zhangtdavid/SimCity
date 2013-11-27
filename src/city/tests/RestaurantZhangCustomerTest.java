package city.tests;

import junit.framework.TestCase;
import utilities.RestaurantZhangMenu;
import city.roles.RestaurantZhangCustomerRole;
import city.tests.animations.mock.MockRestaurantZhangAnimatedCustomer;
import city.tests.mock.MockPerson;
import city.tests.mock.MockRestaurantZhangCashier;
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
		customer = new RestaurantZhangCustomerRole();
		anim = new MockRestaurantZhangAnimatedCustomer(customer); 
		host = new MockRestaurantZhangHost();
		waiter = new MockRestaurantZhangWaiterRegular();
		cashier = new MockRestaurantZhangCashier();
		menu = new RestaurantZhangMenu();

		customer.setPerson(new MockPerson("Person"));
		customer.setHost(host);
		customer.setCashier(cashier);
		customer.setAnimation(anim);
		customer.money = 30;
	}

	public void testOneNormalCustomerScenario() { // Tests when customer has more than enough money to pay
		// Check to make sure customer's initial state is okay
		assertEquals("customer should have no table. It doesn't.", customer.myTable, null);
		assertEquals("customer should have no check. It doesn't.", customer.myCheck, null);
		assertTrue("customer should have money. It doesn't.", customer.money > 0);
		assertTrue("customer should have no tab. It doesn't.", customer.myTab <= 0);
		assertEquals("customer should have a host. It doesn't", customer.host, host);
		assertEquals("customer should have a cashier. It doesn't", customer.myCashier, cashier);
		assertEquals("customer should have no waiter. It doesn't", customer.myWaiter, null);
		assertEquals("customer's state should be doing nothing. Instead it's " + customer.state.name(), customer.state, RestaurantZhangCustomerRole.AgentState.DoingNothing);
		assertEquals("customer's event should be none. Instead it's " + customer.state.name(), customer.event, RestaurantZhangCustomerRole.AgentEvent.none);

		// Step 1: Set active
		customer.setActive();
		assertEquals("customer should have no table. It doesn't.", customer.myTable, null);
		assertEquals("customer should have no check. It doesn't.", customer.myCheck, null);
		assertTrue("customer should have money. It doesn't.", customer.money > 0);
		assertTrue("customer should have no tab. It doesn't.", customer.myTab <= 0);
		assertEquals("customer should have a host. It doesn't", customer.host, host);
		assertEquals("customer should have a cashier. It doesn't", customer.myCashier, cashier);
		assertEquals("customer should have no waiter. It doesn't", customer.myWaiter, null);
		assertEquals("customer's state should be doing nothing. Instead it's " + customer.state.name(), customer.state, RestaurantZhangCustomerRole.AgentState.DoingNothing);
		assertEquals("customer's event should be gotHungry. Instead it's " + customer.state.name(), customer.event, RestaurantZhangCustomerRole.AgentEvent.gotHungry);

		// Step 2: Run the scheduler
		assertTrue("Customer's scheduler didn't run", customer.runScheduler());
		assertEquals("customer should have no table. It doesn't.", customer.myTable, null);
		assertEquals("customer should have no check. It doesn't.", customer.myCheck, null);
		assertTrue("customer should have money. It doesn't.", customer.money > 0);
		assertTrue("customer should have no tab. It doesn't.", customer.myTab <= 0);
		assertEquals("customer should have a host. It doesn't", customer.host, host);
		assertEquals("customer should have a cashier. It doesn't", customer.myCashier, cashier);
		assertEquals("customer should have no waiter. It doesn't", customer.myWaiter, null);
		assertEquals("customer's state should be doing nothing. Instead it's " + customer.state.name(), customer.state, RestaurantZhangCustomerRole.AgentState.AtEntrance);
		assertEquals("customer's event should be gotHungry. Instead it's " + customer.state.name(), customer.event, RestaurantZhangCustomerRole.AgentEvent.gotHungry);

		// Step 3: Message the customer to wait
		customer.msgHereIsYourWaitingPosition(0);
		assertEquals("customer should have waiting position of 0. It doesn't.", customer.waitingPosition, 0);
		assertEquals("customer should have no table. It doesn't.", customer.myTable, null);
		assertEquals("customer should have no check. It doesn't.", customer.myCheck, null);
		assertTrue("customer should have money. It doesn't.", customer.money > 0);
		assertTrue("customer should have no tab. It doesn't.", customer.myTab <= 0);
		assertEquals("customer should have a host. It doesn't", customer.host, host);
		assertEquals("customer should have a cashier. It doesn't", customer.myCashier, cashier);
		assertEquals("customer should have no waiter. It doesn't", customer.myWaiter, null);
		assertEquals("customer's state should be doing nothing. Instead it's " + customer.state.name(), customer.state, RestaurantZhangCustomerRole.AgentState.AtEntrance);
		assertEquals("customer's event should be gotWaitingPosition. Instead it's " + customer.state.name(), customer.event, RestaurantZhangCustomerRole.AgentEvent.gotWaitingPosition);

		// Step 4: Run the scheduler
		assertTrue("Customer's scheduler didn't run", customer.runScheduler());
		assertEquals("customer should have no table. It doesn't.", customer.myTable, null);
		assertEquals("customer should have no check. It doesn't.", customer.myCheck, null);
		assertTrue("customer should have money. It doesn't.", customer.money > 0);
		assertTrue("customer should have no tab. It doesn't.", customer.myTab <= 0);
		assertEquals("customer should have a host. It doesn't", customer.host, host);
		assertEquals("customer should have a cashier. It doesn't", customer.myCashier, cashier);
		assertEquals("customer should have no waiter. It doesn't", customer.myWaiter, null);
		assertEquals("customer's state should be GoingToWaitingPosition. Instead it's " + customer.state.name(), customer.state, RestaurantZhangCustomerRole.AgentState.GoingToWaitingPosition);
		assertEquals("customer's event should be atWaitingPosition. Instead it's " + customer.state.name(), customer.event, RestaurantZhangCustomerRole.AgentEvent.atWaitingPosition);
	}
}