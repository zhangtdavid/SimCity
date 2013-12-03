package city.tests.animations.mock;

import java.awt.Graphics2D;

import city.abstracts.MockAnimation;
import city.animations.interfaces.AnimatedPerson;
import city.interfaces.BusStop;
import city.interfaces.Person;

public class MockAnimatedPerson extends MockAnimation implements AnimatedPerson {
	
	public MockAnimatedPerson() {
		// TODO Auto-generated constructor stub
		
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
	public void goToBusStop(BusStop b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goToSleep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cookAndEatFood() {
		Person p = (Person) this.getAgent();
		p.guiAtDestination(); // Reply immediately since this is a mock
	}

	@Override
	public void verifyFood() {
		// TODO Auto-generated method stub
		
	}
/*
	@Override
	public void cookAndEatFoodPart2() {
		// TODO Auto-generated method stub
		
	}*/

}
