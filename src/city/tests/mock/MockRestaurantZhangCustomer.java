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
		super();
	}
	
	public void msgHereIsCustCheck(RestaurantZhangCheck c) {
		log.add(new LoggedEvent("Received check from waiter."));
	}
	
	public void msgHereIsChange(int change) {
		log.add(new LoggedEvent("Got change " + change + " from cashier."));
	}
	
	public void msgPayLater(int tab) {
		log.add(new LoggedEvent("Got tab " + tab + " from cashier."));
	}

	@Override
	public void msgRestaurantFull() {
		log.add(new LoggedEvent("Host said restaurant is full"));
	}

	@Override
	public void msgHereIsYourWaitingPosition(int pos) {
		log.add(new LoggedEvent("Host gave me waitingposition " + pos));
	}

	@Override
	public void msgFollowMe(RestaurantZhangWaiter w, RestaurantZhangMenu waiterMenu, RestaurantZhangTable table) {
		log.add(new LoggedEvent("Waiter " + w.getName() + " seated me at table " + table.tableNumber));
	}

	@Override
	public void msgWhatWouldYouLike() {
		log.add(new LoggedEvent("Waiter asked what I would like"));
	}

	@Override
	public void msgOrderAgain() {
		log.add(new LoggedEvent("Waiter asked me to order again"));
	}

	@Override
	public void msgHereIsYourFood(String choice) {
		log.add(new LoggedEvent("Waiter gave me my food " + choice));
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

	@Override
	public void msgRestaurantClosed() {
		// TODO Auto-generated method stub
		
	}
}
