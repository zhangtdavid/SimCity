package city.tests.animations.mocks;

import java.awt.Graphics2D;

import city.animations.interfaces.RestaurantTimmsAnimatedCashier;
import city.animations.interfaces.RestaurantTimmsAnimatedCustomer;
import city.roles.interfaces.RestaurantTimmsCustomer;
import city.tests.bases.mocks.MockAnimation;

public class MockRestaurantTimmsAnimatedCustomer extends MockAnimation implements RestaurantTimmsAnimatedCustomer {
	
	private RestaurantTimmsCustomer customer;
	
	public MockRestaurantTimmsAnimatedCustomer(RestaurantTimmsCustomer c) {
		this.customer = c;
	}

	@Override
	public void goToRestaurant() {
		customer.guiAtLine();
	}

	@Override
	public void goToTable(int table) {
		customer.guiAtTable();
	}

	@Override
	public void goToCashier(RestaurantTimmsAnimatedCashier cashier) {
		customer.guiAtCashier();
	}

	@Override
	public void goToExit() {
		customer.guiAtExit();
	}
	
	@Override
	public int getXPos() {
		// This may not be suitable in the future.
		return 0;
	}

	@Override
	public int getYPos() {
		// This may not be suitable in the future.
		return 0;
	}

	@Override
	public boolean getAtRestaurant() {
		// This is not suitable for testing ControlPanel, but otherwise it will work.
		return false;
	}
	
	@Override
	public void setPlate(String plate) {
		return;
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
	public void setUgly(boolean newUgly) {
		// TODO Auto-generated method stub
		
	}

}
