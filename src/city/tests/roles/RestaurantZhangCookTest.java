package city.tests.roles;

import java.awt.Color;

import junit.framework.TestCase;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangOrder;
import utilities.RestaurantZhangRevolvingStand;
import utilities.RestaurantZhangTable;
import city.buildings.RestaurantZhangBuilding;
import city.gui.interiors.RestaurantZhangPanel;
import city.roles.RestaurantZhangCookRole;
import city.tests.agents.mocks.MockPerson;
import city.tests.animations.mocks.MockRestaurantZhangAnimatedCook;
import city.tests.roles.mocks.MockRestaurantZhangCashier;
import city.tests.roles.mocks.MockRestaurantZhangWaiterRegular;

public class RestaurantZhangCookTest extends TestCase {
	RestaurantZhangCookRole cook = new RestaurantZhangCookRole(new RestaurantZhangBuilding("Building", new RestaurantZhangPanel(Color.black), null), 0, 100);
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
		cook.setMenuTimes(menu);
		waiter.setPerson(new MockPerson("Waiter"));
		cook.getWorkplace(RestaurantZhangBuilding.class).cashier = new MockRestaurantZhangCashier();
	}

	public void testOneNormalCustomerOrderScenario() {
		// Check preconditions
		assertTrue("Cook's ordersToCook list should be empty. It isn't", cook.getOrdersToCook().isEmpty());
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.getOrderStand());
		assertFalse("Cook should not be waiting to check stand. It isn't", cook.getWaitingToCheckStand());
		assertEquals("Cook doesn't have a menu. It should", menu, cook.getMainMenu());

		// Step one, set active
		cook.setActive();
		assertTrue("Cook's ordersToCook list should be empty. It isn't", cook.getOrdersToCook().isEmpty());
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.getOrderStand());
		assertTrue("Cook should be waiting to check stand. It isn't", cook.getWaitingToCheckStand());
		assertEquals("Cook doesn't have a menu. It should", menu, cook.getMainMenu());
		assertFalse("Cook's scheduler should not have ran, but it did", cook.runScheduler());

		// Step two, msg an order from waiter
		cook.msgHereIsAnOrder(waiter, "steak", table);
		assertEquals("Cook's ordersToCook list should have one order. It doesn't", 1, cook.getOrdersToCook().size());
		assertEquals("Order should have a waiter. It doesn't", waiter, cook.getOrdersToCook().get(0).w);
		assertEquals("Order status should be created. It's instead " + cook.getOrdersToCook().get(0).status, RestaurantZhangOrder.OrderStatus.created, cook.getOrdersToCook().get(0).status);
		assertEquals("Order choice should be steak, but is instead " + cook.getOrdersToCook().get(0).choice, "steak", cook.getOrdersToCook().get(0).choice);
		assertEquals("Order table should be set, but isn't", table, cook.getOrdersToCook().get(0).t);
		assertEquals("Order pos should be 0, but is instead " + cook.getOrdersToCook().get(0).pos, 0, cook.getOrdersToCook().get(0).pos);
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.getOrderStand());
		assertTrue("Cook should be waiting to check stand. It isn't", cook.getWaitingToCheckStand());
		assertEquals("Cook doesn't have a menu. It should", menu, cook.getMainMenu());

		// Step three, run scheduler
		assertTrue("Cook's scheduler should have ran, but it didn't", cook.runScheduler());
		assertEquals("Cook's ordersToCook list should have one order. It doesn't", 1, cook.getOrdersToCook().size());
		assertEquals("Order should have a waiter. It doesn't", waiter, cook.getOrdersToCook().get(0).w);
		assertEquals("Order status should be cooking. It's instead " + cook.getOrdersToCook().get(0).status, RestaurantZhangOrder.OrderStatus.cooking, cook.getOrdersToCook().get(0).status);
		assertEquals("Order choice should be steak, but is instead " + cook.getOrdersToCook().get(0).choice, "steak", cook.getOrdersToCook().get(0).choice);
		assertEquals("Order table should be set, but isn't", table, cook.getOrdersToCook().get(0).t);
		assertEquals("Order pos should be 0, but is instead " + cook.getOrdersToCook().get(0).pos, 0, cook.getOrdersToCook().get(0).pos);
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.getOrderStand());
		assertTrue("Cook should be waiting to check stand. It isn't", cook.getWaitingToCheckStand());
		assertEquals("Cook doesn't have a menu. It should", menu, cook.getMainMenu());
		assertFalse("Cook's scheduler should not have ran, but it did", cook.runScheduler());

		// Step four, call orderIsReady, which should be called from a timer. I don't want to wait for the timer
		cook.hackOrderIsReady(cook.getOrdersToCook().get(0));
		assertEquals("Cook's ordersToCook list should have one order. It doesn't", 1, cook.getOrdersToCook().size());
		assertEquals("Order should have a waiter. It doesn't", waiter, cook.getOrdersToCook().get(0).w);
		assertEquals("Order status should be doneCooking. It's instead " + cook.getOrdersToCook().get(0).status, RestaurantZhangOrder.OrderStatus.doneCooking, cook.getOrdersToCook().get(0).status);
		assertEquals("Order choice should be steak, but is instead " + cook.getOrdersToCook().get(0).choice, "steak", cook.getOrdersToCook().get(0).choice);
		assertEquals("Order table should be set, but isn't", table, cook.getOrdersToCook().get(0).t);
		assertEquals("Order pos should be 0, but is instead " + cook.getOrdersToCook().get(0).pos, 0, cook.getOrdersToCook().get(0).pos);
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.getOrderStand());
		assertTrue("Cook should be waiting to check stand. It isn't", cook.getWaitingToCheckStand());
		assertEquals("Cook doesn't have a menu. It should", menu, cook.getMainMenu());

		// Step three, run scheduler
		assertTrue("Cook's scheduler should have ran, but it didn't", cook.runScheduler());
		assertEquals("Cook's ordersToCook list should have one order. It doesn't", 1, cook.getOrdersToCook().size());
		assertEquals("Order should have a waiter. It doesn't", waiter, cook.getOrdersToCook().get(0).w);
		assertEquals("Order status should be plated. It's instead " + cook.getOrdersToCook().get(0).status, RestaurantZhangOrder.OrderStatus.plated, cook.getOrdersToCook().get(0).status);
		assertEquals("Order choice should be steak, but is instead " + cook.getOrdersToCook().get(0).choice, "steak", cook.getOrdersToCook().get(0).choice);
		assertEquals("Order table should be set, but isn't", table, cook.getOrdersToCook().get(0).t);
		assertEquals("Order pos should be 0, but is instead " + cook.getOrdersToCook().get(0).pos, 0, cook.getOrdersToCook().get(0).pos);
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.getOrderStand());
		assertTrue("Cook should be waiting to check stand. It isn't", cook.getWaitingToCheckStand());
		assertEquals("Cook doesn't have a menu. It should", menu, cook.getMainMenu());
		assertTrue("Waiter's log is wrong. It reads " + waiter.log.getLastLoggedEvent(), waiter.log.getLastLoggedEvent().toString().contains("Order for table 1 and choice steak received"));
		assertFalse("Cook's scheduler should not have ran, but it did", cook.runScheduler());

		// Step four, msg cook that waiter has recevied order
		cook.msgGotCompletedOrder(table);
		assertEquals("Cook's ordersToCook list should have one order. It doesn't", 1, cook.getOrdersToCook().size());
		assertEquals("Order should have a waiter. It doesn't", waiter, cook.getOrdersToCook().get(0).w);
		assertEquals("Order status should be plated. It's instead " + cook.getOrdersToCook().get(0).status, RestaurantZhangOrder.OrderStatus.removed, cook.getOrdersToCook().get(0).status);
		assertEquals("Order choice should be steak, but is instead " + cook.getOrdersToCook().get(0).choice, "steak", cook.getOrdersToCook().get(0).choice);
		assertEquals("Order table should be set, but isn't", table, cook.getOrdersToCook().get(0).t);
		assertEquals("Order pos should be 0, but is instead " + cook.getOrdersToCook().get(0).pos, 0, cook.getOrdersToCook().get(0).pos);
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.getOrderStand());
		assertTrue("Cook should be waiting to check stand. It isn't", cook.getWaitingToCheckStand());
		assertEquals("Cook doesn't have a menu. It should", menu, cook.getMainMenu());
		assertTrue("Waiter's log is wrong. It reads " + waiter.log.getLastLoggedEvent(), waiter.log.getLastLoggedEvent().toString().contains("Order for table 1 and choice steak received"));

		// Step five, run scheduler
		assertTrue("Cook's scheduler should have ran, but it didn't", cook.runScheduler());
		assertEquals("Cook's ordersToCook list should be empty. It isnt't", 0, cook.getOrdersToCook().size());
		assertEquals("Cook's revolving stand is nonexistent. It shouldn't be", stand, cook.getOrderStand());
		assertTrue("Cook should be waiting to check stand. It isn't", cook.getWaitingToCheckStand());
		assertEquals("Cook doesn't have a menu. It should", menu, cook.getMainMenu());
		assertFalse("Cook's scheduler should not have ran, but it did", cook.runScheduler());
	}
}