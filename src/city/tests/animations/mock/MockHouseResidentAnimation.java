package city.tests.animations.mock;

import java.awt.Graphics2D;

import city.Animation;
import city.animations.interfaces.AnimatedPersonAtHome;
import city.animations.interfaces.AnimatedPersonAtHome.Command;
import city.gui.buildings.HousePanel;
import city.interfaces.Person;

public class MockHouseResidentAnimation extends Animation implements AnimatedPersonAtHome{
	
	//Data
	private int xDestination, yDestination;
	private Person person = null;
	private String orderIcon = "";
	private Command command = Command.noCommand;
	
	//Constructor
	public MockHouseResidentAnimation(Person p){
		person = p;
		xDestination = HousePanel.HDX;
		xDestination = HousePanel.HDY;
		xPos = HousePanel.HDX; // the door is in the front center bottom of the house panel
		yPos = HousePanel.HDY; // so start from a little outside the door (drawn from top to bottom, so can't see)
	}
	
	//Movement
	@Override
	public void goToSleep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cookAndEatFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goToRoom(int roomNo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goOutside() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

}
