package city.tests.animations.mocks;

import java.awt.Graphics2D;

import city.animations.interfaces.RestaurantZhangAnimatedCook;
import city.roles.interfaces.RestaurantZhangCook;
import city.tests.bases.mocks.MockAnimation;

public class MockRestaurantZhangAnimatedCook extends MockAnimation implements RestaurantZhangAnimatedCook {

	private RestaurantZhangCook cook;
	
	public MockRestaurantZhangAnimatedCook(RestaurantZhangCook cook) {
		this.cook = cook;
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
	public void GoToDestination(int x, int y) {
		cook.msgAtDestination();
	}

	@Override
	public void addToPlatingArea(String s, int pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFromPlatingArea(int pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToGrill(String s, int pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFromGrill(int pos) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goToBase() {
		cook.msgAtDestination();
	}

	@Override
	public void goToPlating() {
		cook.msgAtDestination();
	}

	@Override
	public void goToGrill(int grillNumber) {
		cook.msgAtDestination();
	}

	@Override
	public boolean ReturnToBase() {
		cook.msgAtDestination();
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
	
	
}
