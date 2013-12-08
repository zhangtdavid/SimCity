package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;

import utilities.LoggedEvent;
import utilities.RestaurantJPMenuClass;
import city.roles.RestaurantJPCashierRole;
import city.roles.interfaces.RestaurantJPCashier;
import city.roles.interfaces.RestaurantJPCustomer;
import city.roles.interfaces.RestaurantJPWaiter;
import city.tests.bases.mocks.MockRole;

public class MockRestaurantJPCustomer extends MockRole implements RestaurantJPCustomer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public RestaurantJPCashierRole cashier;

	public MockRestaurantJPCustomer(String name) {
		super();

	}

	@Override
	public void msgHereIsCheck(int check, RestaurantJPCashier csh) {
		log.add(new LoggedEvent("Received check."));

		if(check == 10){
			csh.msgFlaking(this, 0);
			return;
		}
			
		int bill = check;
		cashier.msgPayment(this, bill);
	}

	@Override
	public void msgFollowMeToTable(RestaurantJPMenuClass menu, int tableNumber,
			RestaurantJPWaiter restaurantJPWaiterBase) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfChoice(RestaurantJPMenuClass menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood() {
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
