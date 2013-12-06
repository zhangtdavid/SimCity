package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;

import utilities.RestaurantJPTableClass;
import city.agents.interfaces.Person;
import city.roles.RestaurantJPCashierRole;
import city.roles.interfaces.MarketManager;
import city.roles.interfaces.RestaurantJPCook;
import city.roles.interfaces.RestaurantJPWaiter;
import city.tests.bases.mocks.MockRole;

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

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateString() {
		// TODO Auto-generated method stub
		return null;
	}

}
