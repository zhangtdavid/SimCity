package city.tests;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import junit.framework.TestCase;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangOrder;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import utilities.RestaurantZhangOrder.OrderStatus;
import city.animations.RestaurantZhangCookAnimation;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantZhangBuilding;
import city.gui.RestaurantZhangPanel;
import city.interfaces.MarketCustomerDelivery;
import city.interfaces.RestaurantZhangCashier;
import city.interfaces.RestaurantZhangWaiter;
import city.roles.RestaurantZhangCookRole;
import city.tests.animations.mock.MockRestaurantZhangAnimatedCook;
import city.tests.mock.MockPerson;
import city.tests.mock.MockRestaurantZhangWaiterRegular;

/**
 * Restaurant Cashier Agent
 */

public class RestaurantZhangCookTest extends TestCase {
	RestaurantZhangCookRole cook = new RestaurantZhangCookRole(new RestaurantZhangBuilding("Building", new RestaurantZhangPanel(Color.black, new Dimension())), 0, 100);
	RestaurantZhangMenu menu = new RestaurantZhangMenu();
	RestaurantZhangRevolvingStand stand = new RestaurantZhangRevolvingStand();
	MockRestaurantZhangAnimatedCook anim = new MockRestaurantZhangAnimatedCook(cook);
	MockRestaurantZhangWaiterRegular waiter = new MockRestaurantZhangWaiterRegular();
	RestaurantZhangTable table = new RestaurantZhangTable(1);

	public void setUp() throws Exception {
		super.setUp();
		cook.setPerson(new MockPerson("Cook"));
		cook.setRevolvingStand(stand);
		cook.setAnimation(anim);
		cook.setMenuTimes(menu, cook.getWorkplace(RestaurantZhangBuilding.class).foods);
	}

	public void testOneNormalCustomerOrderScenario() {
		// Check preconditions
		assertTrue("Cook's ordersToCook list should be empty. It isn't", cook.ordersToCook.isEmpty());
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.myOrderStand);
		assertFalse("Cook should not be waiting to check stand. It isn't", cook.waitingToCheckStand);
		assertEquals("Cook doesn't have a menu. It should", menu, cook.mainMenu);
		assertEquals("Cook's inventory should have three types of food. It instead has " + cook.cookInventory.size(), 3, cook.cookInventory.size());
		assertTrue("Cook's invoice list should be empty. It isn't", cook.cookInvoiceList.isEmpty());

		// Step one, set active
		cook.setActive();
		assertTrue("Cook's ordersToCook list should be empty. It isn't", cook.ordersToCook.isEmpty());
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.myOrderStand);
		assertTrue("Cook should be waiting to check stand. It isn't", cook.waitingToCheckStand);
		assertEquals("Cook doesn't have a menu. It should", menu, cook.mainMenu);
		assertEquals("Cook's inventory should have three types of food. It instead has " + cook.cookInventory.size(), 3, cook.cookInventory.size());
		assertTrue("Cook's invoice list should be empty false. It isn't", cook.cookInvoiceList.isEmpty());
		assertFalse("Cook's scheduler should not have ran, but it did", cook.runScheduler());

		// Step two, msg an order from waiter
		cook.msgHereIsAnOrder(waiter, "Steak", table);
		assertEquals("Cook's ordersToCook list should have one order. It doesn't", 1, cook.ordersToCook.size());
		assertEquals("Order should have a waiter. It doesn't", waiter, cook.ordersToCook.get(0).w);
		assertEquals("Order status should be created. It's instead " + cook.ordersToCook.get(0).status, RestaurantZhangOrder.OrderStatus.created, cook.ordersToCook.get(0).status);
		assertEquals("Order choice should be steak, but is instead " + cook.ordersToCook.get(0).choice, "Steak", cook.ordersToCook.get(0).choice);
		assertEquals("Order table should be set, but isn't", table, cook.ordersToCook.get(0).t);
		assertEquals("Order pos should be 0, but is instead " + cook.ordersToCook.get(0).pos, 0, cook.ordersToCook.get(0).pos);
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.myOrderStand);
		assertTrue("Cook should be waiting to check stand. It isn't", cook.waitingToCheckStand);
		assertEquals("Cook doesn't have a menu. It should", menu, cook.mainMenu);
		assertEquals("Cook's inventory should have three types of food. It instead has " + cook.cookInventory.size(), 3, cook.cookInventory.size());
		assertTrue("Cook's invoice list should be empty. It isn't", cook.cookInvoiceList.isEmpty());

		// Step three, run scheduler
		assertTrue("Cook's scheduler should have ran, but it didn't", cook.runScheduler());
		assertEquals("Cook's ordersToCook list should have one order. It doesn't", 1, cook.ordersToCook.size());
		assertEquals("Order should have a waiter. It doesn't", waiter, cook.ordersToCook.get(0).w);
		assertEquals("Order status should be cooking. It's instead " + cook.ordersToCook.get(0).status, RestaurantZhangOrder.OrderStatus.cooking, cook.ordersToCook.get(0).status);
		assertEquals("Order choice should be steak, but is instead " + cook.ordersToCook.get(0).choice, "Steak", cook.ordersToCook.get(0).choice);
		assertEquals("Order table should be set, but isn't", table, cook.ordersToCook.get(0).t);
		assertEquals("Order pos should be 0, but is instead " + cook.ordersToCook.get(0).pos, 0, cook.ordersToCook.get(0).pos);
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.myOrderStand);
		assertTrue("Cook should be waiting to check stand. It isn't", cook.waitingToCheckStand);
		assertEquals("Cook doesn't have a menu. It should", menu, cook.mainMenu);
		assertEquals("Cook's inventory should have three types of food. It instead has " + cook.cookInventory.size(), 3, cook.cookInventory.size());
		assertTrue("Cook's invoice list should be empty. It isn't", cook.cookInvoiceList.isEmpty());
		assertFalse("Cook's scheduler should not have ran, but it did", cook.runScheduler());

		// Step four, call orderIsReady, which should be called from a timer. I don't want to wait for the timer
		cook.orderIsReady(cook.ordersToCook.get(0));
		assertEquals("Cook's ordersToCook list should have one order. It doesn't", 1, cook.ordersToCook.size());
		assertEquals("Order should have a waiter. It doesn't", waiter, cook.ordersToCook.get(0).w);
		assertEquals("Order status should be doneCooking. It's instead " + cook.ordersToCook.get(0).status, RestaurantZhangOrder.OrderStatus.doneCooking, cook.ordersToCook.get(0).status);
		assertEquals("Order choice should be steak, but is instead " + cook.ordersToCook.get(0).choice, "Steak", cook.ordersToCook.get(0).choice);
		assertEquals("Order table should be set, but isn't", table, cook.ordersToCook.get(0).t);
		assertEquals("Order pos should be 0, but is instead " + cook.ordersToCook.get(0).pos, 0, cook.ordersToCook.get(0).pos);
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.myOrderStand);
		assertTrue("Cook should be waiting to check stand. It isn't", cook.waitingToCheckStand);
		assertEquals("Cook doesn't have a menu. It should", menu, cook.mainMenu);
		assertEquals("Cook's inventory should have three types of food. It instead has " + cook.cookInventory.size(), 3, cook.cookInventory.size());
		assertTrue("Cook's invoice list should be empty. It isn't", cook.cookInvoiceList.isEmpty()); 

		// Step three, run scheduler
		assertTrue("Cook's scheduler should have ran, but it didn't", cook.runScheduler());
		assertEquals("Cook's ordersToCook list should have one order. It doesn't", 1, cook.ordersToCook.size());
		assertEquals("Order should have a waiter. It doesn't", waiter, cook.ordersToCook.get(0).w);
		assertEquals("Order status should be plated. It's instead " + cook.ordersToCook.get(0).status, RestaurantZhangOrder.OrderStatus.plated, cook.ordersToCook.get(0).status);
		assertEquals("Order choice should be steak, but is instead " + cook.ordersToCook.get(0).choice, "Steak", cook.ordersToCook.get(0).choice);
		assertEquals("Order table should be set, but isn't", table, cook.ordersToCook.get(0).t);
		assertEquals("Order pos should be 0, but is instead " + cook.ordersToCook.get(0).pos, 0, cook.ordersToCook.get(0).pos);
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.myOrderStand);
		assertTrue("Cook should be waiting to check stand. It isn't", cook.waitingToCheckStand);
		assertEquals("Cook doesn't have a menu. It should", menu, cook.mainMenu);
		assertEquals("Cook's inventory should have three types of food. It instead has " + cook.cookInventory.size(), 3, cook.cookInventory.size());
		assertTrue("Cook's invoice list should be empty. It isn't", cook.cookInvoiceList.isEmpty());
		assertTrue("Waiter's log is wrong. It reads " + waiter.log.getLastLoggedEvent(), waiter.log.getLastLoggedEvent().toString().contains("Order for table 1 and choice Steak received"));
		assertFalse("Cook's scheduler should not have ran, but it did", cook.runScheduler());

		// Step four, msg cook that waiter has recevied order
		cook.msgGotCompletedOrder(table);
		assertEquals("Cook's ordersToCook list should have one order. It doesn't", 1, cook.ordersToCook.size());
		assertEquals("Order should have a waiter. It doesn't", waiter, cook.ordersToCook.get(0).w);
		assertEquals("Order status should be plated. It's instead " + cook.ordersToCook.get(0).status, RestaurantZhangOrder.OrderStatus.removed, cook.ordersToCook.get(0).status);
		assertEquals("Order choice should be steak, but is instead " + cook.ordersToCook.get(0).choice, "Steak", cook.ordersToCook.get(0).choice);
		assertEquals("Order table should be set, but isn't", table, cook.ordersToCook.get(0).t);
		assertEquals("Order pos should be 0, but is instead " + cook.ordersToCook.get(0).pos, 0, cook.ordersToCook.get(0).pos);
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.myOrderStand);
		assertTrue("Cook should be waiting to check stand. It isn't", cook.waitingToCheckStand);
		assertEquals("Cook doesn't have a menu. It should", menu, cook.mainMenu);
		assertEquals("Cook's inventory should have three types of food. It instead has " + cook.cookInventory.size(), 3, cook.cookInventory.size());
		assertTrue("Cook's invoice list should be empty. It isn't", cook.cookInvoiceList.isEmpty());
		assertTrue("Waiter's log is wrong. It reads " + waiter.log.getLastLoggedEvent(), waiter.log.getLastLoggedEvent().toString().contains("Order for table 1 and choice Steak received"));

		// Step five, run scheduler
		assertTrue("Cook's scheduler should have ran, but it didn't", cook.runScheduler());
		assertEquals("Cook's ordersToCook list should be empty. It isnt't", 0, cook.ordersToCook.size());
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.myOrderStand);
		assertTrue("Cook should be waiting to check stand. It isn't", cook.waitingToCheckStand);
		assertEquals("Cook doesn't have a menu. It should", menu, cook.mainMenu);
		assertEquals("Cook's inventory should have three types of food. It instead has " + cook.cookInventory.size(), 3, cook.cookInventory.size());
		assertTrue("Cook's invoice list should be empty. It isn't", cook.cookInvoiceList.isEmpty());
		assertFalse("Cook's scheduler should not have ran, but it did", cook.runScheduler());
	}
}