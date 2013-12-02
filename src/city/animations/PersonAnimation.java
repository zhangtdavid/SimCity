package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Animation;
import city.Application.BUILDING;
import city.Application.CityMap;
import city.animations.interfaces.AnimatedPerson;
import city.interfaces.BusStop;
import city.interfaces.Person;

public class PersonAnimation extends Animation implements AnimatedPerson {
	
	//Data
	private int xDestination, yDestination;
	private Person person = null;
	private String orderIcon = "";
	private Command command = Command.noCommand;
	
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

		// Msging, enums have some organization-related prefixes.
		//r signifies residence. The person is in the residence base building inset view
		//w signifies walking. The person is in the city view
		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.rToDoor) { // rDoor: leave
				//TODO msg person to decide his next action... or just do nothing if this has already been done
			} else if (command == Command.rToBed) { // rBed: sleep
				//TODO msg person to go to sleep for x hours
			} else if (command == Command.rToRef) { // rRef: look for food
				//TODO msg person to check inventory of foods / randomly pick a food item to eat
			} else if (command == Command.rToStove) { // rStove: ^ then cook food
				//TODO msg person to cook food (timer event just like in CookAgent)
			} else if (command == Command.rToTable) { // rTable: ^ then eat food
				//TODO msg person to eat food (timer event just like in CustomerAgent)
			} else if (command == Command.wToBuilding){
				//TODO msg person to enter the building and take the appropriate role
			} else if (command == Command.wToBusStop){
				//TODO msg person to go to the nearest bus stop
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
	
	/**
	 * Moves the person to a bus stop (already determined which is closest) 
	 */
	@Override
	public void goToBusStop(BusStop b) {
		xDestination = CityMap.findClosestBuilding(BUILDING.busStop, person).getCityViewBuilding().x;
		xDestination = CityMap.findClosestBuilding(BUILDING.busStop, person).getCityViewBuilding().y;
	}

	@Override
	/**
	 * Moves the person to bed before sleeping. 
	 */
	public void goToSleep() {
		xDestination = AnimatedPerson.RES_BED[0];
		yDestination = AnimatedPerson.RES_BED[1]+AnimatedPerson.RES_BED_Y_INTERVAL;
	}

	/**
	 * Moves the person to refrigerator to check for food stocks.
	 * Person always knows how much food there is in the refrigerator. Or does he?
	 * What if his room mate ate all the food in there without person knowing? Too real. Thus, we check.
	 * This is before actually checking the food; it moves the Person to the refrigerator.
	 */
	@Override
	public void verifyFood(){
		//Send agent to the refrigerator
		xDestination = AnimatedPerson.RES_REFRIGERATOR[0];
		yDestination = AnimatedPerson.RES_REFRIGERATOR[1];
	}
	
	/**
	 * Moves the person to stove to cook food.
	 * After verifyFood().
	 * This animation takes Person to the stove with item, (then timer - cook)
	 */
	@Override
	public void cookAndEatFood() {
		xDestination = AnimatedPerson.RES_STOVE[0];
		xDestination = AnimatedPerson.RES_STOVE[1];
	}
	
	/**
	 * Moves the person to table to eat food.
	 * After cookAndEatFood().
	 * This animation takes Person to the table with item (then timer - eat).
	 */
	@Override
	public void cookAndEatFoodPart2(){
		xDestination = AnimatedPerson.RES_KITCHEN_TABLE[0];
		xDestination = AnimatedPerson.RES_KITCHEN_TABLE[1];
	}
}
