package city.tests;

import java.awt.Color;
import java.awt.Dimension;

import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import city.buildings.RestaurantZhangBuilding;
import city.gui.RestaurantZhangPanel;
import city.interfaces.RestaurantZhangWaiterBase;
import city.roles.RestaurantZhangWaiterSharedDataRole;
import city.tests.animations.mock.MockRestaurantZhangAnimatedWaiter;
import city.tests.mock.MockPerson;
import city.tests.mock.MockRestaurantZhangCashier;
import city.tests.mock.MockRestaurantZhangCook;
import city.tests.mock.MockRestaurantZhangCustomer;
import city.tests.mock.MockRestaurantZhangHost;
import junit.framework.TestCase;

public class RestaurantZhangWaiterSharedDataTest extends TestCase {
	RestaurantZhangWaiterSharedDataRole waiter;
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
		waiter = new RestaurantZhangWaiterSharedDataRole(new RestaurantZhangBuilding("Building", new RestaurantZhangPanel(Color.black, new Dimension()), null), 0, 100);
		anim = new MockRestaurantZhangAnimatedWaiter(waiter); 
		host = new MockRestaurantZhangHost();
		cook = new MockRestaurantZhangCook();
		cashier = new MockRestaurantZhangCashier();
		customer = new MockRestaurantZhangCustomer();
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
		assertTrue("Waiter should not have customers. It does", waiter.myCustomerList.isEmpty());
		assertEquals("Waiter should not have a number of customers served. It's instead " + waiter.numberCustomersServed, 0, waiter.numberCustomersServed);
		assertEquals("Waiter should have a host. It doesn't", host, waiter.myHost);
		assertEquals("Waiter should have a cook. It doesn't", cook, waiter.myCook);
		assertEquals("Waiter should have a cashier. It doesn't", cashier, waiter.myCashier);
		assertEquals("Waiter should have a menu. It doesn't", menu, waiter.waiterMenu);
		assertEquals("Waiter should have a revolving stand. It doesn't", revolvingStand, waiter.myOrderStand);
		assertEquals("Waiter should have a gui. It doesn't", anim, waiter.thisGui);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);

		// Step 1, make waiter active
		waiter.setActive();
		assertTrue("Waiter should not have customers. It does", waiter.myCustomerList.isEmpty());
		assertEquals("Waiter should not have a number of customers served. It's instead " + waiter.numberCustomersServed, 0, waiter.numberCustomersServed);
		assertEquals("Waiter should have a host. It doesn't", host, waiter.myHost);
		assertEquals("Waiter should have a cook. It doesn't", cook, waiter.myCook);
		assertEquals("Waiter should have a cashier. It doesn't", cashier, waiter.myCashier);
		assertEquals("Waiter should have a menu. It doesn't", menu, waiter.waiterMenu);
		assertEquals("Waiter should have a revolving stand. It doesn't", revolvingStand, waiter.myOrderStand);
		assertEquals("Waiter should have a gui. It doesn't", anim, waiter.thisGui);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);

		// Step 2, customer is here (from host)
		waiter.msgSeatCustomer(table, customer);
		assertEquals("Waiter should one customer. It doesn't", 1, waiter.myCustomerList.size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.myCustomerList.get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.myCustomerList.get(0).table);
		assertEquals("Waiter's customer's choice should be null. It isn't", null, waiter.myCustomerList.get(0).choice);
		assertEquals("Waiter's customer's state should be waiting. Instead it's " + waiter.myCustomerList.get(0).state, RestaurantZhangWaiterBase.mcState.waiting, waiter.myCustomerList.get(0).state);
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.numberCustomersServed, 1, waiter.numberCustomersServed);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);

		// Step 3, run the scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.myCustomerList.size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.myCustomerList.get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.myCustomerList.get(0).table);
		assertEquals("Waiter's customer's choice should be null. It isn't", null, waiter.myCustomerList.get(0).choice);
		assertEquals("Waiter's customer's state should be deciding. Instead it's " + waiter.myCustomerList.get(0).state, RestaurantZhangWaiterBase.mcState.deciding, waiter.myCustomerList.get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter Waiter seated me at table 0"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.numberCustomersServed, 1, waiter.numberCustomersServed);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);
		assertTrue("Customer's log is wrong. It reads " + customer.log.getLastLoggedEvent(), customer.log.getLastLoggedEvent().toString().contains("Waiter Waiter seated me at table 0"));
		
		// Step 4, msg ready to order (from customer)
		waiter.msgReadyToOrder(customer);
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.myCustomerList.size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.myCustomerList.get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.myCustomerList.get(0).table);
		assertEquals("Waiter's customer's choice should be null. It isn't", null, waiter.myCustomerList.get(0).choice);
		assertEquals("Waiter's customer's state should be readyToOrder. Instead it's " + waiter.myCustomerList.get(0).state, RestaurantZhangWaiterBase.mcState.readyToOrder, waiter.myCustomerList.get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter Waiter seated me at table 0"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.numberCustomersServed, 1, waiter.numberCustomersServed);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);

		// Step 5, run the scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.myCustomerList.size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.myCustomerList.get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.myCustomerList.get(0).table);
		assertEquals("Waiter's customer's choice should be null. It isn't", null, waiter.myCustomerList.get(0).choice);
		assertEquals("Waiter's customer's state should be ordering. Instead it's " + waiter.myCustomerList.get(0).state, RestaurantZhangWaiterBase.mcState.ordering, waiter.myCustomerList.get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter asked what I would like"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.numberCustomersServed, 1, waiter.numberCustomersServed);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);
		assertTrue("Customer's log is wrong. It reads " + customer.log.getLastLoggedEvent(), customer.log.getLastLoggedEvent().toString().contains("Waiter asked what I would like"));

		// Step 6, run the scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.myCustomerList.size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.myCustomerList.get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.myCustomerList.get(0).table);
		assertEquals("Waiter's customer's choice should be null. It isn't", null, waiter.myCustomerList.get(0).choice);
		assertEquals("Waiter's customer's state should be ordering. Instead it's " + waiter.myCustomerList.get(0).state, RestaurantZhangWaiterBase.mcState.ordering, waiter.myCustomerList.get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter asked what I would like"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.numberCustomersServed, 1, waiter.numberCustomersServed);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);
		
		// Step 7, run the scheduler
		waiter.msgHereIsMyChoice(customer, "Steak");
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.myCustomerList.size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.myCustomerList.get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.myCustomerList.get(0).table);
		assertEquals("Waiter's customer's choice should be Steak. It isn't", "Steak", waiter.myCustomerList.get(0).choice);
		assertEquals("Waiter's customer's state should be ordered. Instead it's " + waiter.myCustomerList.get(0).state, RestaurantZhangWaiterBase.mcState.ordered, waiter.myCustomerList.get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter asked what I would like"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.numberCustomersServed, 1, waiter.numberCustomersServed);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);

		// Step 8, run the scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.myCustomerList.size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.myCustomerList.get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.myCustomerList.get(0).table);
		assertEquals("Waiter's customer's choice should be Steak. It isn't", "Steak", waiter.myCustomerList.get(0).choice);
		assertEquals("Waiter's customer's state should be orderCooking. Instead it's " + waiter.myCustomerList.get(0).state, RestaurantZhangWaiterBase.mcState.orderCooking, waiter.myCustomerList.get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter asked what I would like"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.numberCustomersServed, 1, waiter.numberCustomersServed);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);
		assertTrue("Order stand should have an order in it. It doesn't", !revolvingStand.RestaurantZhangOrderList.isEmpty());

		// Step 9, get food from cook msg
		waiter.msgOrderIsReady("Steak", table);
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.myCustomerList.size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.myCustomerList.get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.myCustomerList.get(0).table);
		assertEquals("Waiter's customer's choice should be Steak. It isn't", "Steak", waiter.myCustomerList.get(0).choice);
		assertEquals("Waiter's customer's state should be orderReady. Instead it's " + waiter.myCustomerList.get(0).state, RestaurantZhangWaiterBase.mcState.orderReady, waiter.myCustomerList.get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter asked what I would like"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.numberCustomersServed, 1, waiter.numberCustomersServed);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);

		// Step 10, run the scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.myCustomerList.size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.myCustomerList.get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.myCustomerList.get(0).table);
		assertEquals("Waiter's customer's choice should be Steak. It isn't", "Steak", waiter.myCustomerList.get(0).choice);
		assertEquals("Waiter's customer's state should be orderReady. Instead it's " + waiter.myCustomerList.get(0).state, RestaurantZhangWaiterBase.mcState.eating, waiter.myCustomerList.get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter gave me my food Steak"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.numberCustomersServed, 1, waiter.numberCustomersServed);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);
		assertTrue("Customer's log is wrong. It reads " + customer.log.getLastLoggedEvent(), customer.log.getLastLoggedEvent().toString().contains("Waiter gave me my food Steak"));

		// Step 11, get finished eating msg from customer
		waiter.msgHereWasMyOrder(customer, "Steak");
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.myCustomerList.size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.myCustomerList.get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.myCustomerList.get(0).table);
		assertEquals("Waiter's customer's choice should be Steak. It isn't", "Steak", waiter.myCustomerList.get(0).choice);
		assertEquals("Waiter's customer's state should be doneEating. Instead it's " + waiter.myCustomerList.get(0).state, RestaurantZhangWaiterBase.mcState.doneEating, waiter.myCustomerList.get(0).state);
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.numberCustomersServed, 1, waiter.numberCustomersServed);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);

		// Step 12, run scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.myCustomerList.size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.myCustomerList.get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.myCustomerList.get(0).table);
		assertEquals("Waiter's customer's choice should be Steak. It isn't", "Steak", waiter.myCustomerList.get(0).choice);
		assertEquals("Waiter's customer's state should be waitingForCheck. Instead it's " + waiter.myCustomerList.get(0).state, RestaurantZhangWaiterBase.mcState.waitingForCheck, waiter.myCustomerList.get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter gave me my food Steak"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.numberCustomersServed, 1, waiter.numberCustomersServed);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);
		assertTrue("Cashier's log is wrong. It reads: " + cashier.log.getLastLoggedEvent(), cashier.log.getLastLoggedEvent().toString().contains("Making bill from Waiter for order Steak"));
		RestaurantZhangCheck check = new RestaurantZhangCheck(waiter, customer, menu.getPrice("Steak"));

		// Step 13, msg from cashier that check is created
		waiter.msgHereIsWaiterCheck(check);
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.myCustomerList.size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.myCustomerList.get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.myCustomerList.get(0).table);
		assertEquals("Waiter's customer's choice should be Steak. It isn't", "Steak", waiter.myCustomerList.get(0).choice);
		assertEquals("Waiter's customer's state should be waitingForCheck. Instead it's " + waiter.myCustomerList.get(0).state, RestaurantZhangWaiterBase.mcState.waitingForCheck, waiter.myCustomerList.get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Waiter gave me my food Steak"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.numberCustomersServed, 1, waiter.numberCustomersServed);
		assertEquals("Waiter's checkList should have one. It doesn'", 1, waiter.checkList.size());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);

		// Step 14, run the scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.myCustomerList.size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.myCustomerList.get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.myCustomerList.get(0).table);
		assertEquals("Waiter's customer's choice should be Steak. It isn't", "Steak", waiter.myCustomerList.get(0).choice);
		assertEquals("Waiter's customer's state should be waitingForCheck. Instead it's " + waiter.myCustomerList.get(0).state, RestaurantZhangWaiterBase.mcState.waitingForCheck, waiter.myCustomerList.get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Received check from waiter"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.numberCustomersServed, 1, waiter.numberCustomersServed);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);
		assertTrue("Customer's log is wrong. It reads " + customer.log.getLastLoggedEvent(), customer.log.getLastLoggedEvent().toString().contains("Received check from waiter"));

		// Step 15, msg from cust that he is leaving
		waiter.msgLeavingTable(customer);
		assertEquals("Waiter should have one customer. It doesn't", 1, waiter.myCustomerList.size());
		assertEquals("Waiter's customer's customer should be set. It isn't", customer, waiter.myCustomerList.get(0).customer);
		assertEquals("Waiter's customer's table should be set. It isn't", table, waiter.myCustomerList.get(0).table);
		assertEquals("Waiter's customer's choice should be Steak. It isn't", "Steak", waiter.myCustomerList.get(0).choice);
		assertEquals("Waiter's customer's state should be leaving. Instead it's " + waiter.myCustomerList.get(0).state, RestaurantZhangWaiterBase.mcState.leaving, waiter.myCustomerList.get(0).state);
		assertTrue("Waiter's customer's log is incorrect. It reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.getLastLoggedEvent().toString().contains("Received check from waiter"));
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.numberCustomersServed, 1, waiter.numberCustomersServed);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);

		// Step 16, run the scheduler
		assertTrue("Scheduler should have run but didn't", waiter.runScheduler());
		assertEquals("Waiter should have no customers. It doesn't", 0, waiter.myCustomerList.size());
		assertEquals("Waiter should have one for number of customers served. It's instead " + waiter.numberCustomersServed, 1, waiter.numberCustomersServed);
		assertTrue("Waiter's checkList should be empty. It isn't", waiter.checkList.isEmpty());
		assertEquals("Waiter's break status should be notOnBreak. It's instead " + waiter.wBreakStatus, RestaurantZhangWaiterBase.breakStatus.notOnBreak, waiter.wBreakStatus);
		assertTrue("Host's log is wrong. It reads " + host.log.getLastLoggedEvent(), host.log.getLastLoggedEvent().toString().contains("Table 0 is empty"));

	}
}