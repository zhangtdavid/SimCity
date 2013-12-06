package city.animations;

import java.awt.Graphics2D;

import city.agents.interfaces.Person;
import city.animations.interfaces.AnimatedPersonAtHome;
import city.bases.Animation;

public class AptResidentAnimation extends Animation implements AnimatedPersonAtHome{
	//Data
	boolean currentlyAcquired;
	Person person = null;
	Command command = Command.noCommand;
	int xDestination, yDestination;
	public boolean beingTested;
	
	//Constructor
	public AptResidentAnimation(Person p){
		person = p;
	}

	//Movements
	
	//This should really be called upon entrance of the Apartment. Always.
	@Override
	public void goToRoom(int roomNo) {
		command = Command.ToRoomEntrance;		
	}
	
	@Override
	public void goToSleep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyFood() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void cookAndEatFood(String in) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void goOutside() {
		// TODO Auto-generated method stub
		
	}
	
	//Update and Draw
	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAcquired() {
		currentlyAcquired = true;		
	}

	@Override
	public int[] getDestination() {
		return new int[]{xDestination, yDestination};
	}

	@Override
	public void setCoords(int x, int y) {
		xPos = x;
		yPos = y;
	}

	@Override
	public boolean getBeingTested() {
		return this.beingTested ;
	}

	@Override
	public void setGraphicStatus(String in) {
		// TODO Auto-generated method stub
		
	}
}
