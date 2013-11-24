package city.tests.mock;

import utilities.LoggedEvent;
import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangTable;
import city.MockRole;
import city.interfaces.RestaurantZhangCustomer;
import city.interfaces.RestaurantZhangWaiter;

public class MockRestaurantZhangCustomer extends MockRole implements RestaurantZhangCustomer {
	
	public MockRestaurantZhangCustomer() {
		
	}
	
	public void msgHereIsCustCheck(RestaurantZhangCheck c) {
		log.add(new LoggedEvent("Received check from waiter."));
	}
	
	public void msgHereIsChange(double change) {
		log.add(new LoggedEvent("Got change " + change + " from cashier."));
	}
	
	public void msgPayLater(double tab) {
		log.add(new LoggedEvent("Got tab " + tab + " from cashier."));
	}

	@Override
	public void msgRestaurantFull() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourWaitingPosition(int pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMe(RestaurantZhangWaiter w, RestaurantZhangMenu waiterMenu, RestaurantZhangTable table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderAgain() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood(String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgAnimationFinishedEnterRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}
}
