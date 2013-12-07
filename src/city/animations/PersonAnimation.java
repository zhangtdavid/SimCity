package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

import city.agents.interfaces.Person;
import city.animations.interfaces.AnimatedPerson;
import city.bases.Animation;
import city.gui.interiors.HousePanel;

public class PersonAnimation extends Animation implements AnimatedPerson { // needs code standard review
	
	// Data
	
	private static enum Command {noCommand, AtDoor, InBed, ToBed, ToRef, ToStove, ToTable, ToDoor, ToRoomEntrance, StationaryAtStove, StationaryAtTable, StationaryAtRef};
	public static boolean beingTested;
	private int xDestination;
	private int yDestination;
	private Person person = null;
	private Command command = Command.noCommand;
	private Timer timer = new Timer();
	
	// TODO what are these?
	private boolean leaving;
	private String status = "";
	private boolean personSemaphoreIsAcquired;
	private String foodToEat;
	private boolean isAtHome;
	
	// Constructor
	
	public PersonAnimation(Person p) {
		this.person = p;
		this.xDestination = HousePanel.HDX;
		this.yDestination = HousePanel.HDY+10;
		this.xPos = HousePanel.HDX; 
		this.yPos = HousePanel.HDY+10;
	}
	
	// Abstract implementors
	
	@Override
	public void updatePosition() {
		
		// Movement
		
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;
		if(yPos > 480)
			yPos--;
		else if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		
		// Processing
		
		if (xPos == xDestination && yPos == yDestination && personSemaphoreIsAcquired && !leaving) {
			if (command == Command.ToBed) {
				// Going to bed
				personSemaphoreIsAcquired = false;
				person.guiAtDestination();
				command = Command.InBed;
				status = "zzz";				
			} else if (command == Command.ToRef) { // ToRef: look for food
				//cook and eat food: 1st, go to refrigerator
				command = Command.StationaryAtRef;
				person.print("At refrigerator");
				this.cookAndEatFood(foodToEat);

				//has left refrigerator, to stove
			} else if (command == Command.ToStove) { // rStove: ^ then cook food
				command = Command.StationaryAtStove;
				status = "Cooking " + foodToEat;
				if(!PersonAnimation.beingTested){
					timer.schedule(new TimerTask() { // timer untested. but its counterpart (instant) works (see below)
						public void run() {
							cookAndEatFood(foodToEat);
							person.print("Done cooking");
						}
					}, 3000);
				}
				else{ // being tested; skip timer
					cookAndEatFood(foodToEat);
					person.print("Done cooking");
				}
				//has left stove with food, to table
			} else if (command == Command.ToTable) { // rTable: ^ then eat food
				command = Command.StationaryAtTable;
				status = "Eating " + foodToEat;
				if(!PersonAnimation.beingTested){
					timer.schedule(new TimerTask() { // see above timer with regards to testing
						public void run() {
							person.print("Done eating");
							status = "";
							// release semaphore now
							personSemaphoreIsAcquired = false;  
							person.guiAtDestination();
							person.print("Semaphore released, at table, eating now (timer)");
							//send him outside now.
							leaving = true;
							command = Command.noCommand;
						}
					}, 4000);
				}else{
					person.print("Done eating");
					status = "";
					// release semaphore now
					personSemaphoreIsAcquired = false;  
					person.guiAtDestination();
					person.print("Semaphore released, at table, eating now (timer)");
					//send him outside now.
					leaving = true;
					command = Command.noCommand;
				}
				
			}
		}

	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, AnimatedPerson.WIDTH, AnimatedPerson.WIDTH);
		g.setColor(Color.WHITE);
		g.drawString(status, xPos, yPos - 8); // draw string (status)
	} 

	// Actions
	
	/**
	 * Moves the person to the door so he can go outside (and leave the house)
	 */
	@Override
	public void goOutside() {
		setAcquired();
		// if we really want to go outside (i.e. leave the house)
		xDestination = HousePanel.HDX;
		yDestination = HousePanel.HDY + 10;
		leaving = false;
		isAtHome = false;
		person.guiAtDestination();
	}

	/**
	 * Moves the person to bed. 
	 */
	@Override
	public void goToSleep() {
		setAcquired();
		isAtHome = true;
		leaving = false;
		command = Command.ToBed;
		xDestination = HousePanel.HBXi-20;
		yDestination = HousePanel.HBYi;
	}

	/**
	 * Moves the person to refrigerator to check for food stocks.
	 * Person always knows how much food there is in the refrigerator. Or does he?
	 * What if his room mate ate all the food in there without person knowing? Too real. Thus, we check.
	 * This is before actually checking the food; it moves the Person to the refrigerator.
	 */
	@Override
	public void verifyFood() {
		command = Command.ToRef;
		xDestination = HousePanel.HRX+20;
		yDestination = HousePanel.HRY;
	}

	/**
	 * Moves the person to stove to cook food.
	 * (To be called after verifyFood().)
	 * This animation takes Person to the stove with item, (then timer - cook)
	 */
	@Override
	public void cookAndEatFood(String in) {
		foodToEat = in; // use this for setOrderIcon and stuff.
		isAtHome = true;
		if (command == Command.StationaryAtStove) { // sent here from stove
			command = Command.ToTable;
			status = "Going to eat " + foodToEat;
			// get here after calling this 2nd time
			// basically self-message
			xDestination = HousePanel.HTX+20;
			yDestination = HousePanel.HTY;
		} else if(command == Command.StationaryAtRef){ // sent here from refrig
			command = Command.ToStove;
			status = "Found "  + foodToEat;
			xDestination = HousePanel.HSX+20;
			yDestination = HousePanel.HSY;
		} else { // sent here from PersonAgent's desires
			setAcquired();
			verifyFood();
		}
	}
	
	// Getters
	
	@Override
	public String getCommand() {
		return command.toString();
	}

	@Override
	public int[] getDestination() {
		return new int[] { xDestination, yDestination };
	}
	
	@Override
	public String getStatus() {
		return this.status;
	}
	
	// Setters
	
	@Override
	public void setCoords(int x, int y) {
		xPos = x;
		yPos = y;
	}
	
	@Override
	public void setGraphicStatus(String in){
		status = in;
	}

	@Override
	public void setAtHome() {
		isAtHome = true;
	}

	@Override
	public void setAcquired(){
		personSemaphoreIsAcquired = true;
	}
}
