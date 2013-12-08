package city.tests.roles.mocks;

import java.beans.PropertyChangeSupport;

import utilities.LoggedEvent;
import utilities.RestaurantZhangCheck;
import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangTable;
import city.animations.interfaces.RestaurantZhangAnimatedCustomer;
import city.roles.interfaces.RestaurantZhangCashier;
import city.roles.interfaces.RestaurantZhangCustomer;
import city.roles.interfaces.RestaurantZhangHost;
import city.roles.interfaces.RestaurantZhangWaiter;
import city.tests.bases.mocks.MockRole;

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
		log.add(new LoggedEvent("Waiter " + w.getPerson().getName() + " seated me at table " + table.tableNumber));
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
	public void msgGotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRestaurantClosed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RestaurantZhangTable getTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestaurantZhangHost getHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestaurantZhangWaiter getWaiter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWaitingPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RestaurantZhangMenu getCustomerMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChoice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestaurantZhangCashier getCashier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestaurantZhangCheck getCheck() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMoney() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTab() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AGENTSTATE getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AGENTEVENT getEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWaitingPosition(int newPos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAnimation(RestaurantZhangAnimatedCustomer g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(RestaurantZhangCashier c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(RestaurantZhangHost host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMoney(int newMoney) {
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
