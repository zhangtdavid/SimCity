package city.tests;

import utilities.RestaurantChoiOrder;
import utilities.RestaurantChoiRevolvingStand;
import city.agents.PersonAgent;
import city.interfaces.RestaurantChoiCook;
import city.roles.RestaurantChoiCookRole;
import city.tests.mock.MockRestaurantChoiWaiter;
import city.tests.mock.MockRestaurantChoiWaiter2;
import junit.framework.TestCase;

public class RestaurantChoiCookTest extends TestCase{
	RestaurantChoiCookRole cook;
	RestaurantChoiRevolvingStand rs;
	PersonAgent p;
	MockRestaurantChoiWaiter waiter1; 
	MockRestaurantChoiWaiter2 waiter2;
	
	public void setUp() throws Exception{
		super.setUp();		
		p = new PersonAgent("person-cashier", null);
		cook = new RestaurantChoiCookRole(); // cashier has no name		
		waiter1 = new MockRestaurantChoiWaiter("mockw");
		waiter2 = new MockRestaurantChoiWaiter2("mockw2");
		p.addRole(cook);
		cook.setPerson(p);
	}
	
	/**
	 * waiter1 is the type of waiter that 
	 * @throws InterruptedException 
	 */
	public void testWaiter1() throws InterruptedException{
		RestaurantChoiOrder o = new RestaurantChoiOrder(1,4,waiter1); // choice, table#, waiter
		assertTrue("List of orders should initially be empty", RestaurantChoiCook.orders.isEmpty());
		assertTrue("Order should be state ",  o.getState()==RestaurantChoiOrder.ORDERED);
		assertTrue("Order should be just like how I set it", o.getChoice() == 1);
		assertTrue("Order should be just like how I set it", o.getTableNumber() == 4);
		assertTrue("Order should be just like how I set it", o.getWaiter().equals(waiter1));
		cook.msgHeresAnOrder(o);
		assertTrue("Cook should have exactly 1 order", RestaurantChoiCook.orders.size() == 1);
		assertTrue("this order should have state RECOGNIZED", RestaurantChoiCook.orders.get(0).getState() == RestaurantChoiOrder.RECOGNIZED);
		assertTrue("this order should have same choice as before", RestaurantChoiCook.orders.get(0).getChoice() == 1);
		assertTrue("this order should have same table# as before", RestaurantChoiCook.orders.get(0).getTableNumber() == 4);
		//Won't be testing with runScheduler (= re-implementation of the entire gui)
		//besides, that can be demonstrated via MainFrame.
		//Running it with debug will suffice for questioning procedure.
		assertTrue("analyzeOrder should return true", cook.AnalyzeCookOrder(o));
		assertTrue("After analyzeOrder returns true, order should have state TO_COOK", o.getState() == RestaurantChoiOrder.TO_COOK);
		assertTrue("CookOrder should return true", cook.CookOrder(o));
		assertTrue("order state should be cooking", o.getState() == RestaurantChoiOrder.COOKING);
		assertTrue("order state should be COOKED", o.getState() == RestaurantChoiOrder.COOKED);
		//continue test later... more important to test Waiter2
	}
	
	public void testWaiter2() throws InterruptedException{
		RestaurantChoiOrder o = new RestaurantChoiOrder(1,4,waiter2);
		assertTrue("List of orders should initially be empty", RestaurantChoiCook.orders.isEmpty());
		assertTrue("Order should be state ",  o.getState()==RestaurantChoiOrder.ORDERED);
		assertTrue("Order should be just like how I set it", o.getChoice() == 1);
		assertTrue("Order should be just like how I set it", o.getTableNumber() == 4);
		assertTrue("Order should be just like how I set it", o.getWaiter().equals(waiter1));
		//pushing before test is finished so others have reference to completed restaurant
	}
}
