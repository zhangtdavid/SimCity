package city.tests.mock;


import city.MockRole;
import city.animations.RestaurantChungCustomerAnimation;
import city.animations.interfaces.RestaurantChungAnimatedCustomer;
import city.interfaces.RestaurantChungCashier;
import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungHost;
import city.interfaces.RestaurantChungWaiterBase;
import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.RestaurantChungMenu;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockRestaurantChungCashier extends MockRole implements RestaurantChungCustomer {
	public EventLog log = new EventLog();

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGetInLinePosition(int positionInLine) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNoTablesAvailable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSelfDecidedToLeave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMeToTable(RestaurantChungWaiterBase w,
			RestaurantChungMenu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationAtSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSelfReadyToOrder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfItem(String choice, RestaurantChungMenu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSelfDoneEating() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(int price) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationAtCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsChange(int change) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgKickingYouOutAfterPaying(int debt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(RestaurantChungCustomerAnimation g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(RestaurantChungHost host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(RestaurantChungCashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHungerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RestaurantChungCustomerAnimation getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrder() {
		// TODO Auto-generated method stub
		return null;
	}	
}
