package city.tests.mock;

import utilities.LoggedEvent;
import utilities.RestaurantJPTableClass;
import city.MockAgent;
import city.interfaces.MarketManager;
import city.interfaces.Person;
import city.interfaces.RestaurantJPCook;
import city.interfaces.RestaurantJPWaiter;
import city.roles.RestaurantJPCashierRole;


/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockRestaurantJPCook extends MockAgent implements RestaurantJPCook {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public RestaurantJPCashierRole cashier;
	//public Market Market2;
	public MockRestaurantJPCook(String name) {
		super();

	}

	public void msgHereIsOrder(RestaurantJPWaiter w, String choice, RestaurantJPTableClass table) {
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
	public void msgHereIsAnOrder(RestaurantJPWaiter w, String choice,
			RestaurantJPTableClass table) {
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

}
