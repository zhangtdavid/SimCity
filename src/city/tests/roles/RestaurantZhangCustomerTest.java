package city.tests.roles;

import junit.framework.TestCase;
import utilities.RestaurantZhangMenu;
import city.roles.RestaurantZhangCustomerRole;
import city.roles.interfaces.RestaurantZhangCustomer;
import city.tests.agents.mocks.MockPerson;
import city.tests.animations.mocks.MockRestaurantZhangAnimatedCustomer;
import city.tests.roles.mocks.MockRestaurantZhangCashier;
import city.tests.roles.mocks.MockRestaurantZhangHost;
import city.tests.roles.mocks.MockRestaurantZhangWaiterRegular;

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
		customer.setMoney(30);
	}

	public void testOneNormalCustomerScenario() { // Tests when customer has more than enough money to pay
		// Check to make sure customer's initial state is okay
		assertEquals("customer should have no table. It doesn't.", customer.getTable(), null);
		assertEquals("customer should have no check. It doesn't.", customer.getCheck(), null);
		assertTrue("customer should have money. It doesn't.", customer.getMoney() > 0);
		assertTrue("customer should have no tab. It doesn't.", customer.getTab() <= 0);
		assertEquals("customer should have a host. It doesn't", customer.getHost(), host);
		assertEquals("customer should have a cashier. It doesn't", customer.getCashier(), cashier);
		assertEquals("customer should have no waiter. It doesn't", customer.getWaiter(), null);
		assertEquals("customer's state should be doing nothing. Instead it's " + customer.getState().name(), customer.getState(), RestaurantZhangCustomer.AGENTSTATE.DoingNothing);
		assertEquals("customer's event should be none. Instead it's " + customer.getState().name(), customer.getEvent(), RestaurantZhangCustomer.AGENTEVENT.none);

		// Step 1: Set active
		customer.setActive();
		assertEquals("customer should have no table. It doesn't.", customer.getTable(), null);
		assertEquals("customer should have no check. It doesn't.", customer.getCheck(), null);
		assertTrue("customer should have money. It doesn't.", customer.getMoney() > 0);
		assertTrue("customer should have no tab. It doesn't.", customer.getTab() <= 0);
		assertEquals("customer should have a host. It doesn't", customer.getHost(), host);
		assertEquals("customer should have a cashier. It doesn't", customer.getCashier(), cashier);
		assertEquals("customer should have no waiter. It doesn't", customer.getWaiter(), null);
		assertEquals("customer's state should be doing nothing. Instead it's " + customer.getState().name(), customer.getState(), RestaurantZhangCustomer.AGENTSTATE.DoingNothing);
		assertEquals("customer's event should be gotHungry. Instead it's " + customer.getState().name(), customer.getEvent(), RestaurantZhangCustomer.AGENTEVENT.gotHungry);

		// Step 2: Run the scheduler
		assertTrue("Customer's scheduler didn't run", customer.runScheduler());
		assertEquals("customer should have no table. It doesn't.", customer.getTable(), null);
		assertEquals("customer should have no check. It doesn't.", customer.getCheck(), null);
		assertTrue("customer should have money. It doesn't.", customer.getMoney() > 0);
		assertTrue("customer should have no tab. It doesn't.", customer.getTab() <= 0);
		assertEquals("customer should have a host. It doesn't", customer.getHost(), host);
		assertEquals("customer should have a cashier. It doesn't", customer.getCashier(), cashier);
		assertEquals("customer should have no waiter. It doesn't", customer.getWaiter(), null);
		assertEquals("customer's state should be doing nothing. Instead it's " + customer.getState().name(), customer.getState(), RestaurantZhangCustomer.AGENTSTATE.AtEntrance);
		assertEquals("customer's event should be gotHungry. Instead it's " + customer.getState().name(), customer.getEvent(), RestaurantZhangCustomer.AGENTEVENT.gotHungry);

		// Step 3: Message the customer to wait
		customer.msgHereIsYourWaitingPosition(0);
		assertEquals("customer should have waiting position of 0. It doesn't.", customer.getWaitingPosition(), 0);
		assertEquals("customer should have no table. It doesn't.", customer.getTable(), null);
		assertEquals("customer should have no check. It doesn't.", customer.getCheck(), null);
		assertTrue("customer should have money. It doesn't.", customer.getMoney() > 0);
		assertTrue("customer should have no tab. It doesn't.", customer.getTab() <= 0);
		assertEquals("customer should have a host. It doesn't", customer.getHost(), host);
		assertEquals("customer should have a cashier. It doesn't", customer.getCashier(), cashier);
		assertEquals("customer should have no waiter. It doesn't", customer.getWaiter(), null);
		assertEquals("customer's state should be doing nothing. Instead it's " + customer.getState().name(), customer.getState(), RestaurantZhangCustomer.AGENTSTATE.AtEntrance);
		assertEquals("customer's event should be gotWaitingPosition. Instead it's " + customer.getState().name(), customer.getEvent(), RestaurantZhangCustomer.AGENTEVENT.gotWaitingPosition);

		// Step 4: Run the scheduler
		assertTrue("Customer's scheduler didn't run", customer.runScheduler());
		assertEquals("customer should have no table. It doesn't.", customer.getTable(), null);
		assertEquals("customer should have no check. It doesn't.", customer.getCheck(), null);
		assertTrue("customer should have money. It doesn't.", customer.getMoney() > 0);
		assertTrue("customer should have no tab. It doesn't.", customer.getTab() <= 0);
		assertEquals("customer should have a host. It doesn't", customer.getHost(), host);
		assertEquals("customer should have a cashier. It doesn't", customer.getCashier(), cashier);
		assertEquals("customer should have no waiter. It doesn't", customer.getWaiter(), null);
		assertEquals("customer's state should be GoingToWaitingPosition. Instead it's " + customer.getState().name(), customer.getState(), RestaurantZhangCustomer.AGENTSTATE.GoingToWaitingPosition);
		assertEquals("customer's event should be atWaitingPosition. Instead it's " + customer.getState().name(), customer.getEvent(), RestaurantZhangCustomer.AGENTEVENT.atWaitingPosition);
	}
}