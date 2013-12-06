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
	private boolean isAtHome; // to know whether to move the graphic out, or to bring it in
	private boolean leaving;
	public Timer timer = new Timer(); // set public for testing (can set timer
										// events as activated)

	// Constructor
	public HouseResidentAnimation(Person p) {
		person = p;
		xDestination = HousePanel.HDX;
		yDestination = HousePanel.HDY+10;
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
		
		
		if(xPos == xDestination && yDestination == yPos && leaving){
			if(command == Command.noCommand) 
				this.goToRoom(person.getRoomNumber()); // go to room first
			else goOutside(); // now go outside
		}
		
		if (xPos == xDestination && yPos == yDestination && personSemaphoreIsAcquired && !leaving) {
			System.out.println(command.toString());
			//entering or leaving a building must begin with setting yourself to enter your room.
			if (command == Command.ToRoomEntrance) {
				//for a house, do nothing
				command = Command.noCommand;
				
				//going to bed
			} else if (command == Command.ToBed) {
				personSemaphoreIsAcquired = false;
				if(!beingTested){ // if not in a test (real run), do the semaphore stuff.
					person.guiAtDestination();
					person.print("Semaphore released, at bed"); // test output
				}
				command = Command.InBed;
				
				//cook and eat food: 1st, go to refrigerator
			} else if (command == Command.ToRef) { // ToRef: look for food
				command = Command.StationaryAtRef;
				if(!beingTested){
					person.print("At refrigerator"); // test output
				}
				person.print("At refrigerator");
				this.cookAndEatFood();
				
				//has left refrigerator, to stove
			} else if (command == Command.ToStove) { // rStove: ^ then cook food
				command = Command.StationaryAtStove;
				if (!beingTested) { // in practical conditions
					timer.schedule(new TimerTask() { // timer untested. but its counterpart (instant) works (see below)
						public void run() {
							cookAndEatFood();
							person.print("Done cooking");
						}
					}, 3000);
				} else { // if you're in a test, skip the timer.
					cookAndEatFood();
					person.print("Skipped timer; done cooking");
				}

				//has left stove with food, to table
			} else if (command == Command.ToTable) { // rTable: ^ then eat food
				command = Command.StationaryAtTable;
				if (!beingTested) { // in practical conditions
					timer.schedule(new TimerTask() { // see above timer with regards to testing
						public void run() {
							person.print("Done eating");
							// release semaphore now
							personSemaphoreIsAcquired = false;  
							person.guiAtDestination();
							person.print("Semaphore released, at table, eating now (timer)");
							//send him outside now.
							leaving = true;
							command = Command.noCommand;
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
		// if we really want to go outside
		xDestination = HousePanel.HDX;
		yDestination = HousePanel.HDY + 10;
		leaving = false;
		isAtHome = false;
	}

	@Override
	public void goToSleep() {
		leaving = false;
		isAtHome = true;
		command = Command.ToBed;
		xDestination = HousePanel.HBXi-20;
		yDestination = HousePanel.HBYi;
	}

	@Override
	public void verifyFood() {
		command = Command.ToRef;
		xDestination = HousePanel.HRX+20;
		yDestination = HousePanel.HRY;
		//TODO remove a food item, and set the order icon
	}

	@Override
	public void cookAndEatFood() {
		isAtHome = true;
		if (command == Command.StationaryAtStove) { // sent here from stove
			command = Command.ToTable;
			// get here after calling this 2nd time
			// basically self-message
			xDestination = HousePanel.HTX+20;
			yDestination = HousePanel.HTY;
		} else if(command == Command.StationaryAtRef){ // sent here from refrig
			command = Command.ToStove;
			xDestination = HousePanel.HSX+20;
			yDestination = HousePanel.HSY;
		} else { // sent here from PersonAgent's desires
			verifyFood();
		}
	}

	@Override
	public void goToRoom(int roomNo) {
		isAtHome = true;
		command = Command.ToRoomEntrance;
		xDestination = HousePanel.HDX;
		yDestination = HousePanel.HDY-40;
		// this does nothing for persons who live in houses.
	}

	// Getters (for testing)
	public String getCommand() {
		return command.toString();
	}

	@Override
	public int[] getDestination() {
		return new int[] { xDestination, yDestination };
	}

	@Override
	public boolean getBeingTested() {
		return HouseResidentAnimation.beingTested;
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
}
