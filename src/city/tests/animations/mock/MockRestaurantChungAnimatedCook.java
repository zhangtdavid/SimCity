package city.tests.animations.mock;

import java.awt.Graphics2D;

import city.abstracts.MockAnimation;
import city.animations.interfaces.RestaurantChungAnimatedCook;
import city.interfaces.RestaurantChungCook;

public class MockRestaurantChungAnimatedCook extends MockAnimation implements RestaurantChungAnimatedCook {
	
	private RestaurantChungCook cook;
	
	public MockRestaurantChungAnimatedCook(RestaurantChungCook c) {
		cook = c;
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
	public void DoGoToGrill(String choice) {
		cook.msgAnimationAtGrill();
	}

	@Override
	public void DoReturnToCookHome() {
		cook.msgAnimationAtCookHome();
	}

	@Override
	public void DoGoToPlating(String choice) {
		cook.msgAnimationAtPlating();		
	}

}
