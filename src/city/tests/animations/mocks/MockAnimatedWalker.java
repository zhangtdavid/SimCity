package city.tests.animations.mocks;

import java.awt.Graphics2D;

import utilities.LoggedEvent;
import city.animations.interfaces.AnimatedWalker;
import city.bases.interfaces.BuildingInterface;
import city.roles.interfaces.Walker;
import city.tests.bases.mocks.MockAnimation;

public class MockAnimatedWalker extends MockAnimation implements AnimatedWalker {

	private Walker walker;

	public MockAnimatedWalker(Walker walker) {
		this.walker = walker;
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
	public void goToDestination(BuildingInterface destination) {
		log.add(new LoggedEvent("Walker " + walker.getPerson().getName() + " is going to destination " + destination.getName()));
	}

	@Override
	public void setUgly(boolean newUgly) {
		// TODO Auto-generated method stub
		
	}
}