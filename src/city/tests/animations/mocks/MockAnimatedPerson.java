package city.tests.animations.mocks;

import java.awt.Graphics2D;

import city.agents.interfaces.Person;
import city.animations.interfaces.AnimatedPerson;
import city.tests.bases.mocks.MockAnimation;

public class MockAnimatedPerson extends MockAnimation implements AnimatedPerson {
	
	// Data
	
	private Person person = null;
	
	// Constructor
	
	public MockAnimatedPerson(Person p) {
		this.person = p;
	}
	
	// Abstract

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}
	
	// Actions

	@Override
	public void goToSleep() {
		person.guiAtDestination();
	}

	@Override
	public void verifyFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cookAndEatFood(String in) {
		person.guiAtDestination();
	}


	@Override
	public void goOutside() {
		person.guiAtDestination();
	}

	// Getters
	
	@Override
	public int[] getDestination() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	// Setters

	@Override
	public void setCoords(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGraphicStatus(String in) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAtHome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAcquired() {
		// TODO Auto-generated method stub
		
	}
}
