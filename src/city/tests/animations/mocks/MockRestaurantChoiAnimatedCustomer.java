package city.tests.animations.mocks;

import java.awt.Graphics2D;

import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChoiAnimatedCustomer;
import city.tests.bases.mocks.MockAnimation;

public class MockRestaurantChoiAnimatedCustomer extends MockAnimation implements RestaurantChoiAnimatedCustomer{

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoExitRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToWaiting(int offset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToDishes() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToSeat(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isHungry() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getXWait() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getYWait() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPresent(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetLocation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOrderIcon(FOOD_ITEMS f, boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUgly(boolean newUgly) {
		// TODO Auto-generated method stub
		
	}

}
