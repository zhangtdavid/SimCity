package city.tests.animations.mocks;

import java.awt.Graphics2D;

import city.animations.interfaces.RestaurantTimmsAnimatedCook;
import city.animations.interfaces.RestaurantTimmsAnimatedCustomer;
import city.animations.interfaces.RestaurantTimmsAnimatedWaiter;
import city.roles.interfaces.RestaurantTimmsWaiter;
import city.tests.bases.mocks.MockAnimation;

public class MockRestaurantTimmsAnimatedWaiter extends MockAnimation implements RestaurantTimmsAnimatedWaiter {
	
	private RestaurantTimmsWaiter waiter;
	
	public MockRestaurantTimmsAnimatedWaiter(RestaurantTimmsWaiter w) {
		this.waiter = w;
	}

	@Override
	public void goOnBreak() {
		return;
	}

	@Override
	public void goOffBreak() {
		return;
	}

	@Override
	public void goToCustomer(RestaurantTimmsAnimatedCustomer customer) {
		waiter.guiAtCustomer();
	}

	@Override
	public void goToTable(int tableNumber, String menuItem) {
		waiter.guiAtTable();
	}

	@Override
	public void goToKitchen(RestaurantTimmsAnimatedCook cook) {
		waiter.guiAtKitchen();
	}

	@Override
	public void goToHome(int position) {
		waiter.guiAtHome();
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getXPos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getYPos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setUgly(boolean newUgly) {
		// TODO Auto-generated method stub
		
	}

}
