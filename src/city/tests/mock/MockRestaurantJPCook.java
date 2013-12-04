package city.tests.mock;

import utilities.RestaurantJPTableClass;
import city.abstracts.MockRole;
import city.interfaces.MarketManager;
import city.interfaces.Person;
import city.interfaces.RestaurantJPCook;
import city.interfaces.RestaurantJPWaiter;
import city.roles.RestaurantJPCashierRole;

public class MockRestaurantJPCook extends MockRole implements RestaurantJPCook {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public RestaurantJPCashierRole cashier;
	//public Market Market2;
	public MockRestaurantJPCook(String name) {
		super();

	}

	public void msgHereIsOrder(RestaurantJPWaiter w, String c, RestaurantJPTableClass t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	/*public void msgOrderCannotBeFulfilled(String food, Market m) {
		log.add(new LoggedEvent(food + " order could not be fulfilled"));
		// TODO Auto-generated method stub
		//Market2.msgNeedFood("Pizza", this);
	}*/

	public void msgShipmentReady(String f) {
		
		
	}

	/*public void msgMarketDry(MarketAgent m) {
		// TODO Auto-generated method stub
		
	}*/

	@Override
	public void msgFoodRetrieved(String f) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean runScheduler() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Person getPerson() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean getActive() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean getActivity() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void setPerson(Person p) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setActive() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setInactive() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setActivityBegun() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setActivityFinished() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgOrderCannotBeFulfilled(String food, MarketManager m) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgMarketDry(MarketManager m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsAnOrder(RestaurantJPWaiter wait, String c,
			RestaurantJPTableClass t) {
		// TODO Auto-generated method stub
		
	}

}
