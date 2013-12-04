package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

import city.Animation;
import city.animations.interfaces.AnimatedPerson;
import city.animations.interfaces.AnimatedPersonAtHome;
import city.gui.buildings.HousePanel;
import city.interfaces.Person;

public class HouseResidentAnimation extends Animation implements AnimatedPersonAtHome{
	
	//Data
	
	/**
	 * this is to bypass timer events in testing. Don't EVER use this in real application
	 */
	public boolean beingTested = false;
	
	private int xDestination, yDestination;
	private Person person = null;
	private String orderIcon = "";
	private Command command = Command.noCommand;
	private boolean personSemaphoreIsAcquired;
	public Timer timer = new Timer(); // set public for testing (can set timer events as activated)
	
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
		//the boolean exists so we don't release the semaphore more than once, ever.
		if (xPos == xDestination && yPos == yDestination && personSemaphoreIsAcquired) {
			if (command == Command.ToDoor) { // rDoor: leave
				//TODO msg person to decide his next action... or just do nothing if this has already been done
			} else if (command == Command.ToBed) { // rBed: sleep
				//TODO msg person to go to sleep for x hours
			} else if (command == Command.ToRef) { // rRef: look for food
				person.releaseSemaphoreFromAnimation();
				person.print("Semaphore released, at refrigerator"); //test
			} else if (command == Command.ToStove) { // rStove: ^ then cook food
				System.out.println("Semaphore NOT released; at stove; cooking now (timer)");
				command = Command.StationaryAtStove;
				if(!beingTested){ // in practical conditions
				timer.schedule(new TimerTask() {
					public void run() {
						command = Command.ToTable;
						person.print("Done cooking");
					}
				}, 3000);
				}else{ // if you're in a test, skip the timer.
					command = Command.ToTable;
					person.print("Skipped timer; done cooking");
				}
			} else if (command == Command.ToTable) { // rTable: ^ then eat food
				command = Command.StationaryAtTable;
				if(!beingTested){ // in practical conditions
				timer.schedule(new TimerTask() {
					public void run() {
						command = Command.noCommand; // animation doesn't know what to do now... back to Person
						person.print("Done eating");
					}
				}, 4000);
				}else{ // if you're in a test, skip the timer.
					command = Command.ToTable;
					person.print("Skipped timer; done eating");
				}
				person.releaseSemaphoreFromAnimation();
				person.print("Semaphore released, at table, eating now (timer)");
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

	
	//Movement
	
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
		System.out.println("To refrigerator"); // test
	}
	

	@Override
	public void cookAndEatFood() {
		if(command == Command.ToTable){ // get to this point after you call CookAndEatFood the first time, self-message
			xDestination = HousePanel.HTX;
			xDestination = HousePanel.HTY;
			System.out.println("To table"); // test
		}else{
			command = Command.ToStove;
			xDestination = HousePanel.HSX;
			xDestination = HousePanel.HSY;
			System.out.println("To stove"); // test
		}
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
	
	
	//Getters (for testing)
	public String getCommand(){
		return command.toString();
	}
	
	@Override
	public int[] getDestination() {
		return new int[]{xDestination, yDestination};
	}
	
	//Setters (for testing)
	@Override
	public void setCoords(int x, int y){
		xPos = x;
		yPos = y;
	}
	@Override
	public void setAcquired(){
		personSemaphoreIsAcquired = true;
	}
}
