package city.tests.roles;

import java.awt.Color;

import junit.framework.TestCase;
import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import utilities.RestaurantZhangWaiterBase;
import utilities.RestaurantZhangWaiterBase.MyCustomer;
import city.buildings.RestaurantZhangBuilding;
import city.gui.interiors.RestaurantZhangPanel;
import city.roles.RestaurantZhangWaiterRegularRole;
import city.tests.agents.mocks.MockPerson;
import city.tests.animations.mocks.MockRestaurantZhangAnimatedWaiter;
import city.tests.roles.mocks.MockRestaurantZhangCashier;
import city.tests.roles.mocks.MockRestaurantZhangCook;
import city.tests.roles.mocks.MockRestaurantZhangCustomer;
import city.tests.roles.mocks.MockRestaurantZhangHost;

public class RestaurantZhangWaiterRegularTest extends TestCase {
	RestaurantZhangWaiterRegularRole waiter;
	MockRestaurantZhangAnimatedWaiter anim;
	MockRestaurantZhangHost host;
	MockRestaurantZhangCook cook;
	MockRestaurantZhangCashier cashier;
	MockRestaurantZhangCustomer customer;
	RestaurantZhangMenu menu;
	RestaurantZhangRevolvingStand revolvingStand;
	RestaurantZhangTable table;

	public void setUp() throws Exception {
		super.setUp();
		waiter = new RestaurantZhangWaiterRegularRole(new RestaurantZhangBuilding("Building", new RestaurantZhangPanel(Color.black), null), 0, 100);
		waiter.setPerson(new MockPerson("Waiter"));
		anim = new MockRestaurantZhangAnimatedWaiter(waiter); 
		host = new MockRestaurantZhangHost();
		cook = new MockRestaurantZhangCook();
		cashier = new MockRestaurantZhangCashier();
		customer = new MockRestaurantZhangCustomer();
		customer.setPerson(new MockPerson("Customer"));
		menu = new RestaurantZhangMenu();
		revolvingStand = new RestaurantZhangRevolvingStand();
		table = new RestaurantZhangTable(0, 1, 1, 1, 1);

		waiter.setPerson(new MockPerson("Waiter"));
		waiter.setHost(host);
		waiter.setCook(cook);
		waiter.setCashier(cashier);
		waiter.setAnimation(anim);
		waiter.setMenu(menu);
		waiter.setRevolvingStand(revolvingStand);
	}

	public void testOneNormalWaiterScenario() { // Tests when customer has more than enough money to pay
		// Check to make sure waiter's initial state is okay
		assertTrue("Waiter should not have customers. It does", waiter.getCustomerList().isEmpty());
		assertEquals("Waiter should not have a number of customers served. It's instead " + waiter.getNumCustomersServed(), 0, waiter.getNumCustomersServed());
		assertEquals("Waiter should have a host. It doesn't", host, waiter.getHost());
		assertEquals("Waiter should have a cook. It doesn't", cook, waiter.getCook());
		assertEquals("Waiter should have a cashier. It doesn't", cashier, waiter.getCashier());
		assertEquals("Waiter should have a menu. It doesn't", menu, waiter.getMenu());
		assertEquals("Waiter should have a revolving stand. It doesn't", revolvingStand, waiter.getOrderStand());
		assertEquals("Waiter should have a gui. It doesn't", anim, waiter.getAnimation());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());

		// Step 1, make waiter active
		waiter.setActive();
		assertTrue("Waiter should not have customers. It does", waiter.getCustomerList().isEmpty());
		assertEquals("Waiter should not have a number of customers served. It's instead " + waiter.getNumCustomersServed(), 0, waiter.getNumCustomersServed());
		assertEquals("Waiter should have a host. It doesn't", host, waiter.getHost());
		assertEquals("Waiter should have a cook. It doesn't", cook, waiter.getCook());
		assertEquals("Waiter should have a cashier. It doesn't", cashier, waiter.getCashier());
		assertEquals("Waiter should have a menu. It doesn't", menu, waiter.getMenu());
		assertEquals("Waiter should have a revolving stand. It doesn't", revolvingStand, waiter.getOrderStand());
		assertEquals("Waiter should have a gui. It doesn't", anim, waiter.getAnimation());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());

		// Step 2, customer is here (from host)
		waiter.msgSeatCustomer(table, customer);
		assertEquals("Waiter should one customer. It doesn't", 1, waiter.getCustomerList().size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.getCustomerList().get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.getCustomerList().get(0).table);
		assertEquals("Waiter's customer's choice should be null. It isn't", null, waiter.getCustomerList().get(0).choice);
		assertEquals("Waiter's customer's state should be waiting. Instead it's " + waiter.getCustomerList().get(0).state, MyCustomer.STATE.waiting, waiter.getCustomerList().get(0).state);
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.getNumCustomersServed(), 1, waiter.getNumCustomersServed());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());

		// Step 3, run the scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.getCustomerList().size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.getCustomerList().get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.getCustomerList().get(0).table);
		assertEquals("Waiter's customer's choice should be null. It isn't", null, waiter.getCustomerList().get(0).choice);
		assertEquals("Waiter's customer's state should be deciding. Instead it's " + waiter.getCustomerList().get(0).state, MyCustomer.STATE.deciding, waiter.getCustomerList().get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter Waiter seated me at table 0"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.getNumCustomersServed(), 1, waiter.getNumCustomersServed());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());
		assertTrue("Customer's log is wrong. It reads " + customer.log.getLastLoggedEvent(), customer.log.getLastLoggedEvent().toString().contains("Waiter Waiter seated me at table 0"));
		
		// Step 4, msg ready to order (from customer)
		waiter.msgReadyToOrder(customer);
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.getCustomerList().size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.getCustomerList().get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.getCustomerList().get(0).table);
		assertEquals("Waiter's customer's choice should be null. It isn't", null, waiter.getCustomerList().get(0).choice);
		assertEquals("Waiter's customer's state should be readyToOrder. Instead it's " + waiter.getCustomerList().get(0).state, MyCustomer.STATE.readyToOrder, waiter.getCustomerList().get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter Waiter seated me at table 0"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.getNumCustomersServed(), 1, waiter.getNumCustomersServed());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());

		// Step 5, run the scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.getCustomerList().size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.getCustomerList().get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.getCustomerList().get(0).table);
		assertEquals("Waiter's customer's choice should be null. It isn't", null, waiter.getCustomerList().get(0).choice);
		assertEquals("Waiter's customer's state should be ordering. Instead it's " + waiter.getCustomerList().get(0).state, MyCustomer.STATE.ordering, waiter.getCustomerList().get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter asked what I would like"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.getNumCustomersServed(), 1, waiter.getNumCustomersServed());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());
		assertTrue("Customer's log is wrong. It reads " + customer.log.getLastLoggedEvent(), customer.log.getLastLoggedEvent().toString().contains("Waiter asked what I would like"));

		// Step 6, run the scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.getCustomerList().size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.getCustomerList().get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.getCustomerList().get(0).table);
		assertEquals("Waiter's customer's choice should be null. It isn't", null, waiter.getCustomerList().get(0).choice);
		assertEquals("Waiter's customer's state should be ordering. Instead it's " + waiter.getCustomerList().get(0).state, MyCustomer.STATE.ordering, waiter.getCustomerList().get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter asked what I would like"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.getNumCustomersServed(), 1, waiter.getNumCustomersServed());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());
		
		// Step 7, run the scheduler
		waiter.msgHereIsMyChoice(customer, "steak");
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.getCustomerList().size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.getCustomerList().get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.getCustomerList().get(0).table);
		assertEquals("Waiter's customer's choice should be steak. It isn't", "steak", waiter.getCustomerList().get(0).choice);
		assertEquals("Waiter's customer's state should be ordered. Instead it's " + waiter.getCustomerList().get(0).state, MyCustomer.STATE.ordered, waiter.getCustomerList().get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter asked what I would like"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.getNumCustomersServed(), 1, waiter.getNumCustomersServed());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());

		// Step 8, run the scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.getCustomerList().size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.getCustomerList().get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.getCustomerList().get(0).table);
		assertEquals("Waiter's customer's choice should be steak. It isn't", "steak", waiter.getCustomerList().get(0).choice);
		assertEquals("Waiter's customer's state should be orderCooking. Instead it's " + waiter.getCustomerList().get(0).state, MyCustomer.STATE.orderCooking, waiter.getCustomerList().get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter asked what I would like"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.getNumCustomersServed(), 1, waiter.getNumCustomersServed());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());

		// Step 9, get food from cook msg
		waiter.msgOrderIsReady("steak", table);
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.getCustomerList().size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.getCustomerList().get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.getCustomerList().get(0).table);
		assertEquals("Waiter's customer's choice should be steak. It isn't", "steak", waiter.getCustomerList().get(0).choice);
		assertEquals("Waiter's customer's state should be orderReady. Instead it's " + waiter.getCustomerList().get(0).state, MyCustomer.STATE.orderReady, waiter.getCustomerList().get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter asked what I would like"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.getNumCustomersServed(), 1, waiter.getNumCustomersServed());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());

		// Step 10, run the scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.getCustomerList().size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.getCustomerList().get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.getCustomerList().get(0).table);
		assertEquals("Waiter's customer's choice should be steak. It isn't", "steak", waiter.getCustomerList().get(0).choice);
		assertEquals("Waiter's customer's state should be orderReady. Instead it's " + waiter.getCustomerList().get(0).state, MyCustomer.STATE.eating, waiter.getCustomerList().get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter gave me my food steak"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.getNumCustomersServed(), 1, waiter.getNumCustomersServed());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());
		assertTrue("Customer's log is wrong. It reads " + customer.log.getLastLoggedEvent(), customer.log.getLastLoggedEvent().toString().contains("Waiter gave me my food steak"));

		// Step 11, get finished eating msg from customer
		waiter.msgHereWasMyOrder(customer, "steak");
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.getCustomerList().size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.getCustomerList().get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.getCustomerList().get(0).table);
		assertEquals("Waiter's customer's choice should be steak. It isn't", "steak", waiter.getCustomerList().get(0).choice);
		assertEquals("Waiter's customer's state should be doneEating. Instead it's " + waiter.getCustomerList().get(0).state, MyCustomer.STATE.doneEating, waiter.getCustomerList().get(0).state);
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.getNumCustomersServed(), 1, waiter.getNumCustomersServed());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());

		// Step 12, run scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.getCustomerList().size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.getCustomerList().get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.getCustomerList().get(0).table);
		assertEquals("Waiter's customer's choice should be steak. It isn't", "steak", waiter.getCustomerList().get(0).choice);
		assertEquals("Waiter's customer's state should be waitingForCheck. Instead it's " + waiter.getCustomerList().get(0).state, MyCustomer.STATE.waitingForCheck, waiter.getCustomerList().get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter gave me my food steak"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.getNumCustomersServed(), 1, waiter.getNumCustomersServed());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());
		assertTrue("Cashier's log is wrong. It reads: " + cashier.log.getLastLoggedEvent(), cashier.log.getLastLoggedEvent().toString().contains("Making bill from Waiter for order steak"));
		RestaurantZhangCheck check = new RestaurantZhangCheck(waiter, customer, menu.getPrice("steak"));

		// Step 13, msg from cashier that check is created
		waiter.msgHereIsWaiterCheck(check);
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.getCustomerList().size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.getCustomerList().get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.getCustomerList().get(0).table);
		assertEquals("Waiter's customer's choice should be steak. It isn't", "steak", waiter.getCustomerList().get(0).choice);
		assertEquals("Waiter's customer's state should be waitingForCheck. Instead it's " + waiter.getCustomerList().get(0).state, MyCustomer.STATE.waitingForCheck, waiter.getCustomerList().get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter gave me my food steak"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.getNumCustomersServed(), 1, waiter.getNumCustomersServed());
		assertEquals("Waiter's checkList should have one. It doesn'", 1, waiter.getCheckList().size());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());

		// Step 14, run the scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.getCustomerList().size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.getCustomerList().get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.getCustomerList().get(0).table);
		assertEquals("Waiter's customer's choice should be steak. It isn't", "steak", waiter.getCustomerList().get(0).choice);
		assertEquals("Waiter's customer's state should be waitingForCheck. Instead it's " + waiter.getCustomerList().get(0).state, MyCustomer.STATE.waitingForCheck, waiter.getCustomerList().get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Received check from waiter"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.getNumCustomersServed(), 1, waiter.getNumCustomersServed());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());
		assertTrue("Customer's log is wrong. It reads " + customer.log.getLastLoggedEvent(), customer.log.getLastLoggedEvent().toString().contains("Received check from waiter"));

		// Step 15, msg from cust that he is leaving
		waiter.msgLeavingTable(customer);
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.getCustomerList().size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.getCustomerList().get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.getCustomerList().get(0).table);
		assertEquals("Waiter's customer's choice should be steak. It isn't", "steak", waiter.getCustomerList().get(0).choice);
		assertEquals("Waiter's customer's state should be leaving. Instead it's " + waiter.getCustomerList().get(0).state, MyCustomer.STATE.leaving, waiter.getCustomerList().get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Received check from waiter"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.getNumCustomersServed(), 1, waiter.getNumCustomersServed());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());

		// Step 16, run the scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have no customers. It doesn't", 0, waiter.getCustomerList().size());
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.getNumCustomersServed(), 1, waiter.getNumCustomersServed());
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.getCheckList().isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.getBreakStatus(), RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.getBreakStatus());
		assertTrue("Host's log is wrong. It reads " + host.log.getLastLoggedEvent(), host.log.getLastLoggedEvent().toString().contains("Table 0 is empty"));

	}
}