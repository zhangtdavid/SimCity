package city.tests;

import utilities.RestaurantChoiOrder;
import utilities.RestaurantChoiRevolvingStand;
import city.Application.FOOD_ITEMS;
import city.agents.PersonAgent;
import city.roles.RestaurantChoiCookRole;
import city.tests.mock.MockPerson;
import city.tests.mock.MockRestaurantChoiWaiter;
import city.tests.mock.MockRestaurantChoiWaiter2;
import junit.framework.TestCase;

public class RestaurantChoiRevolvingStandCookTest extends TestCase{
	RestaurantChoiCookRole cook;
	RestaurantChoiRevolvingStand rs;
	PersonAgent p;
	MockRestaurantChoiWaiter waiter1; 
	MockRestaurantChoiWaiter2 waiter2;
	
	public void setUp() throws Exception{
		super.setUp();		
		MockPerson p = new MockPerson("Cook"); 
		cook = new RestaurantChoiCookRole();
		waiter1 = new MockRestaurantChoiWaiter("mockw");
		waiter2 = new MockRestaurantChoiWaiter2("mockw2");
		rs = new RestaurantChoiRevolvingStand();
		waiter1.setRevolvingStand(rs);
		cook.setRevolvingStand(rs);
		p.addRole(cook);
		cook.setPerson(p);
		cook.orders.clear();
	}
	
	/**
	 * waiter1 is the type of waiter that goes directly to the cook and msgs him an order
	 * @throws InterruptedException 
	 */
	public void testWaiter2() throws InterruptedException{
		//create the order
		RestaurantChoiOrder o = new RestaurantChoiOrder(FOOD_ITEMS.steak,4,waiter2); // choice, table#, waiter
		assertTrue("List of orders should initially be empty", cook.orders.isEmpty());
		assertTrue("Order should be state ",  o.getState()==RestaurantChoiOrder.ORDERED);
		assertTrue("Order should be just like how I set it", o.getChoice() == FOOD_ITEMS.steak);
		assertTrue("Order should be just like how I set it", o.getTableNumber() == 4);
		assertTrue("Order should be just like how I set it", o.getWaiter().equals(waiter2));
		//introduce the order to the cook directly 
		cook.msgHeresAnOrder(o);
		assertTrue("Cook should have exactly 1 order", cook.orders.size() == 1);
		assertTrue("this order should have state RECOGNIZED", cook.orders.get(0).getState() == RestaurantChoiOrder.RECOGNIZED);
		assertTrue("this order should have same choice as before", cook.orders.get(0).getChoice() == FOOD_ITEMS.steak);
		assertTrue("this order should have same table# as before", cook.orders.get(0).getTableNumber() == 4);
		//cook.runScheduler();
		//analyzing the order. skip gui since haven't initialized it
		assertTrue("analyzeOrder should return true", cook.AnalyzeCookOrder(o));
		assertTrue("After analyzeOrder returns true, order should have state TO_COOK", o.getState() == RestaurantChoiOrder.TO_COOK);
		//cooking the order
		assertTrue("CookOrder should return true", cook.CookOrder(o));
		assertTrue("order state should be cooking", o.getState() == RestaurantChoiOrder.COOKING); // and we're cookin!
		//this was really in v2.1 anyways.
	}
	
	/**
	 * waiter1 is the type of waiter that goes to the stand and places an order there for the cook to check.
	 * @throws InterruptedException 
	 */
	public void testWaiter1() throws InterruptedException{
		assertTrue("List of orders should initially be empty", cook.orders.isEmpty());
		RestaurantChoiOrder o = new RestaurantChoiOrder(FOOD_ITEMS.chicken,4,waiter2);
		assertTrue("Order should be state ",  o.getState()==RestaurantChoiOrder.ORDERED);
		assertTrue("Order should be just like how I set it", o.getChoice() == FOOD_ITEMS.chicken);
		assertTrue("Order should be just like how I set it", o.getTableNumber() == 4);
		assertTrue("Order should be just like how I set it", o.getWaiter().equals(waiter2));
		assertTrue(rs.isEmpty());
		rs.add(o); // equivalent to sendOrderToCook on this waiter's end minus the graphics
		assertTrue(!rs.isEmpty());
		assertTrue(rs.peek().getWaiter().equals(waiter2));
		assertTrue(rs.peek().getTableNumber() == 4);
		assertTrue(rs.peek().getChoice() == FOOD_ITEMS.chicken);
		//cook.CheckBack(); checkback relies on a timer event, so i'll just pretend the timer's up and the cook takes the action inside.
		RestaurantChoiOrder oo = rs.poll();
		cook.orders.add(oo);
		cook.AnalyzeCookOrder(oo);
		assertTrue("After cook checks, rs should be empty", rs.isEmpty());
		assertTrue("Cook should have one order after checking", cook.orders.size() == 1);
		assertTrue("The one order should be able to be cooked, because initialization starts with >0 of all foods", cook.orders.get(0).getState() == RestaurantChoiOrder.TO_COOK); 
	}
}
