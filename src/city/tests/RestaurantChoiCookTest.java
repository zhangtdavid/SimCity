package city.tests;

import city.agents.PersonAgent;
import city.roles.RestaurantChoiCashierRole;
import city.roles.RestaurantChoiCookRole;
import city.roles.RestaurantChoiRevolvingStand;
import city.tests.mock.MockRestaurantChoiCustomer;
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
	
	public void testWaiter1(){
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
