package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

import city.agents.interfaces.Person;
import city.animations.interfaces.AnimatedPerson;
import city.animations.interfaces.AnimatedPersonAtHome;
import city.bases.Animation;
import city.gui.interiors.HousePanel;

public class HouseResidentAnimation extends Animation implements
		AnimatedPersonAtHome {

	// Data

	/**
	 * this is to bypass timer events in testing. Don't EVER use this in real
	 * application
	 */
	public static boolean beingTested;
	private int xDestination, yDestination;
	private Person person = null;
	private String orderIcon = "";
	private Command command = Command.noCommand;
	private boolean personSemaphoreIsAcquired;
	public Timer timer = new Timer(); // set public for testing (can set timer
										// events as activated)

	// Constructor
	public HouseResidentAnimation(Person p) {
		person = p;
		xDestination = HousePanel.HDX;
		xDestination = HousePanel.HDY;
		xPos = HousePanel.HDX; 
		yPos = HousePanel.HDY+10;
	}

	// Update position (Drawing)
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
		// the boolean exists so we don't release the semaphore more than once, hopefully.
		if (xPos == xDestination && yPos == yDestination
				&& personSemaphoreIsAcquired) {
			if(command == Command.noCommand){ // if the person isn't doing anything...
				command = Command.ToDoor; // by default, we'll push the person to the door.
				
			}
			if (command == Command.ToRoomEntrance) {
				//for a house, do nothing
				command = Command.noCommand;
			} else if (command == Command.ToDoor) { // ToDoor: leave
				personSemaphoreIsAcquired = false;
				if(!beingTested){ // if not in a test (real run), do the semaphore stuff.
					person.guiAtDestination();
					person.print("Semaphore released, at door"); // test output
					// msg person if needed here.
				}
				command = Command.AtDoor; //AtDoor is a dead end command; just wait for person to tell you what to do after.
				//TODO IF necessary, msg person that now stepping out of the building?
			} else if (command == Command.ToBed) { // ToBed: sleep
				personSemaphoreIsAcquired = false;
				if(!beingTested){ // if not in a test (real run), do the semaphore stuff.
					person.guiAtDestination();
					person.print("Semaphore released, at bed"); // test output
				}
				command = Command.InBed;
			} else if (command == Command.ToRef) { // ToRef: look for food
				personSemaphoreIsAcquired = false;
				if(!beingTested){
					person.guiAtDestination();
					person.print("Semaphore released, at refrigerator"); // test output
				}
				person.print("At refrigerator");
				command = Command.noCommand;
			} else if (command == Command.ToStove) { // rStove: ^ then cook food
				command = Command.StationaryAtStove;
				
				if (!beingTested) { // in practical conditions
					timer.schedule(new TimerTask() { // timer untested. but its counterpart (instant) works (see below)
						public void run() {
							command = Command.ToTable;
							person.print("Done cooking");
						}
					}, 3000);
					
				} else { // if you're in a test, skip the timer.
					command = Command.ToTable;
					cookAndEatFood();
					person.print("Skipped timer; done cooking");
				}
				//do not set command to noCommand because we want to go straight to table.
			} else if (command == Command.ToTable) { // rTable: ^ then eat food
				command = Command.StationaryAtTable;
				
				if (!beingTested) { // in practical conditions
					timer.schedule(new TimerTask() { // see above timer with regards to testing
						public void run() {
							command = Command.noCommand; // back to person
							person.print("Done eating");
							personSemaphoreIsAcquired = false; 
							// now unlock semaphore and end.
							person.guiAtDestination();
							person.print("Semaphore released, at table, eating now (timer)");
						}
					}, 4000);

				} else { // if you're in a test, skip the timer; skip the
							// stationary phase
					command = Command.noCommand;
					person.print("Skipped timer; done eating");
				}
			}
		}
	}

	/**
	 * Draws person and a string symbolizing the food he is making/eating at the
	 * time.
	 */
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, AnimatedPerson.WIDTH, AnimatedPerson.WIDTH);
		g.setColor(Color.BLACK);
		g.drawString(orderIcon, xPos, yPos + 10); // draw the orderIcon for the
													// person; often will be "".
	}

	// Movement

	@Override
	public void goOutside() {
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
	public void verifyFood() {
		command = Command.ToRef;
		xDestination = HousePanel.HRX;
		yDestination = HousePanel.HRY;
	}

	@Override
	public void cookAndEatFood() {
		if (command == Command.ToTable) { 
			// get here after calling this 2nd time
			// basically self-message
			xDestination = HousePanel.HTX;
			yDestination = HousePanel.HTY;
		} else {
			command = Command.ToStove;
			xDestination = HousePanel.HSX;
			yDestination = HousePanel.HSY;
		}
	}

	@Override
	public void goToRoom(int roomNo) {
		command = Command.ToRoomEntrance;
		// this does nothing for persons who live in houses.
	}

	/**
	 * Moves the person to table to eat food. After cookAndEatFood(). This
	 * animation takes Person to the table with item (then timer - eat).
	 */
	/*
	 * @Override public void cookAndEatFoodPart2(){ xDestination =
	 * AnimatedPerson.RES_KITCHEN_TABLE[0]; xDestination =
	 * AnimatedPerson.RES_KITCHEN_TABLE[1]; }
	 */

	// Getters (for testing)
	public String getCommand() {
		return command.toString();
	}

	@Override
	public int[] getDestination() {
		return new int[] { xDestination, yDestination };
	}

	// Setters (for testing)
	@Override
	public void setCoords(int x, int y) {
		xPos = x;
		yPos = y;
	}

	@Override
	public void setAcquired() {
		personSemaphoreIsAcquired = true;
	}

	@Override
	public boolean getBeingTested() {
		return HouseResidentAnimation.beingTested;
	}
}
