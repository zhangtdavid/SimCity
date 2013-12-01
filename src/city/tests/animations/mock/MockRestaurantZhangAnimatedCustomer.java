package city.tests.animations.mock;

import java.awt.Graphics2D;

import utilities.RestaurantZhangTable;
import city.abstracts.MockAnimation;
import city.animations.interfaces.RestaurantZhangAnimatedCustomer;
import city.interfaces.RestaurantZhangCustomer;

public class MockRestaurantZhangAnimatedCustomer extends MockAnimation implements RestaurantZhangAnimatedCustomer {

	private RestaurantZhangCustomer customer;

	public MockRestaurantZhangAnimatedCustomer(RestaurantZhangCustomer c) {
		this.customer = c;
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
	public void DoGoToSeat(RestaurantZhangTable t) {
		customer.msgAnimationFinishedGoToSeat();
	}

	@Override
	public void DoExitRestaurant() {
		customer.msgAnimationFinishedLeaveRestaurant();
		
	}

	@Override
	public void DoGoToEntrance() {
		customer.msgAnimationFinishedEnterRestaurant();
		
	}

	@Override
	public void setFoodLabel(String choice, boolean isHere) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWaitingPosition(int x, int y) {
		// TODO Auto-generated method stub
		
	}
	
}
