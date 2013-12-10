package city.tests.animations.mocks;

import java.awt.Graphics2D;

import utilities.RestaurantZhangTable;
import city.animations.interfaces.RestaurantZhangAnimatedWaiter;
import city.roles.interfaces.RestaurantZhangWaiter;
import city.tests.bases.mocks.MockAnimation;

public class MockRestaurantZhangAnimatedWaiter extends MockAnimation implements RestaurantZhangAnimatedWaiter {

	private RestaurantZhangWaiter waiter;

	public MockRestaurantZhangAnimatedWaiter(RestaurantZhangWaiter w) {
		this.waiter = w;
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
	public void GoToTable(RestaurantZhangTable t) {
		 waiter.msgAtDestination();
	}

	@Override
	public void GoToDestination(int x, int y) {
		// TODO Auto-generated method stub
		waiter.msgAtDestination();
	}

	@Override
	public void GoToCustomer(int pos) {
		// TODO Auto-generated method stub
		waiter.msgAtDestination();
	}

	@Override
	public void GoToBreak() {
		// TODO Auto-generated method stub
		waiter.msgAtDestination();
	}

	@Override
	public boolean ReturnToBase() {
		waiter.msgAtDestination();
		return false;
	}

	@Override
	public void setFoodLabel(String choice, boolean isHere) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUgly(boolean newUgly) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getBaseX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBaseY() {
		// TODO Auto-generated method stub
		return 0;
	}
}
