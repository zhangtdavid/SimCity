package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Animation;
import city.animations.interfaces.AnimatedPerson;
import city.animations.interfaces.AnimatedPersonAtHome;
import city.gui.buildings.HousePanel;
import city.interfaces.Person;

public class HouseResidentAnimation extends Animation implements AnimatedPersonAtHome{
	
	//Data
	private int xDestination, yDestination;
	private Person person = null;
	private String orderIcon = "";
	private Command command = Command.noCommand;
	
	//Constructor
	public HouseResidentAnimation(Person p){
		person = p;
		xDestination = HousePanel.HDX;
		xDestination = HousePanel.HDY;
		xPos = HousePanel.HDX; // the door is in the front center bottom of the house panel
		yPos = HousePanel.HDY; // so start from a little outside the door (drawn from top to bottom, so can't see)
	}
	
	//Update position (Drawing)
	@Override
	public void updatePosition() {
		// GUI: translation movement
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;
		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		// MSG back to Person
		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.ToDoor) { // rDoor: leave
				//TODO msg person to decide his next action... or just do nothing if this has already been done
			} else if (command == Command.ToBed) { // rBed: sleep
				//TODO msg person to go to sleep for x hours
			} else if (command == Command.ToRef) { // rRef: look for food
				//TODO msg person to check inventory of foods / randomly pick a food item to eat
			} else if (command == Command.ToStove) { // rStove: ^ then cook food
				//TODO msg person to cook food (timer event just like in CookAgent)
			} else if (command == Command.ToTable) { // rTable: ^ then eat food
				//TODO msg person to eat food (timer event just like in CustomerAgent)
			}
		}
	}

	/**
	 * Draws person and a string symbolizing the food he is making/eating at the time. 
	 */
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, AnimatedPerson.WIDTH, AnimatedPerson.WIDTH);
		g.setColor(Color.BLACK);
		g.drawString(orderIcon, xPos, yPos+10); // draw the orderIcon for the person; often will be "".
	} 

	
	//Msg (from agent)
	
	@Override
	public void goOutside(){
		command = Command.ToDoor;
		xDestination = HousePanel.HDX;
		yDestination = HousePanel.HDY;
	}

	@Override
	public void goToSleep() {
		command = Command.ToBed;
		xDestination = HousePanel.HBXi;
		yDestination = HousePanel.HBYi;
	}


	@Override
	public void verifyFood(){
		command = Command.ToRef;
		xDestination = HousePanel.HRX;
		yDestination = HousePanel.HRY;
	}
	

	@Override
	public void cookAndEatFood() {
		command = Command.ToStove;
		xDestination = HousePanel.HSX;
		xDestination = HousePanel.HSY;
	}

	@Override
	public void goToRoom(int roomNo) {
		//this does nothing for persons who live in houses.
	}
	
	/**
	 * Moves the person to table to eat food.
	 * After cookAndEatFood().
	 * This animation takes Person to the table with item (then timer - eat).
	 */
	/*@Override
	public void cookAndEatFoodPart2(){
		xDestination = AnimatedPerson.RES_KITCHEN_TABLE[0];
		xDestination = AnimatedPerson.RES_KITCHEN_TABLE[1];
	}*/
}
