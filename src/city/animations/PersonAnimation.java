package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

import city.agents.interfaces.Person;
import city.animations.interfaces.AnimatedPerson;
import city.bases.Animation;
import city.buildings.interfaces.Apt;
import city.buildings.interfaces.House;
import city.gui.interiors.AptPanel;
import city.gui.interiors.HousePanel;

public class PersonAnimation extends Animation implements AnimatedPerson {
	
	// Data
	
	private static enum Command {noCommand, AtDoor, InBed, ToBed, ToRef, ToStove, ToTable, ToDoor, ToRoomEntrance, StationaryAtStove, StationaryAtTable, StationaryAtRef};
	private int xDestination;
	private int yDestination;
	private Person person = null;
	private Command command = Command.noCommand;
	private Timer timer = new Timer();
	
	private boolean leaving; // in process of leaving the house.
	private String status = ""; // like orderIcon, a string above the person. 
	private boolean personSemaphoreIsAcquired; // knows that a release must happen
	private String foodToEat; // food, for status.
	private boolean isAtHome; // knows to restrict movement for aesthetics
	
	// Constructor
	
	public PersonAnimation() {
		super();
		this.person = null; // Expects to have this set immediately after creation!!!!!!!!!!!!!!!!!!!!!!
		this.xDestination = -20;
		this.yDestination = -20;
		this.xPos = -20; 
		this.yPos = -20;
		
	}
	
	// Abstract implementors
	
	@Override
	public void updatePosition() {
		if (person == null) {
			throw new IllegalStateException("PersonAnimation does not have a Person object.");
		}

		// Base case
		if(!isAtHome && !personSemaphoreIsAcquired){
			if(person.getHome() instanceof House){ //house
				this.xDestination = HousePanel.HDX;
				this.yDestination = HousePanel.HDY+20;
				this.xPos = HousePanel.HDX; 
				this.yPos = HousePanel.HDY+20;
			}else if(person.getHome() instanceof Apt){ // or apartment
				this.xDestination = AptPanel.APT_DOOR[person.getRoomNumber()-1][0]-20;
				this.yDestination = AptPanel.APT_DOOR[person.getRoomNumber()-1][1];
				this.xPos = AptPanel.APT_DOOR[person.getRoomNumber()-1][0]-20;
				this.yPos = AptPanel.APT_DOOR[person.getRoomNumber()-1][1];
			}
		}
		
		// Movement
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;
		if(person.getHome() instanceof House && yPos > 450 && xPos != xDestination && leaving)
			yPos--;
		else if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;
		
		// Processing
		if (xPos == xDestination && yPos == yDestination && personSemaphoreIsAcquired) {
			if(command == Command.ToDoor){
				//At door; can exit building now
				personSemaphoreIsAcquired = false;
				person.guiAtDestination();
				command = Command.noCommand;
			}
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
					timer.schedule(new TimerTask() { // timer untested. but its counterpart (instant) works (see below)
						public void run() {
							cookAndEatFood(foodToEat);
							person.print("Done cooking");
						}
					}, 4000);
				//has left stove with food, to table
			} else if (command == Command.ToTable) { // rTable: ^ then eat food
				command = Command.StationaryAtTable;
				status = "Eating " + foodToEat;
					timer.schedule(new TimerTask() { // see above timer with regards to testing
						public void run() {
							person.print("Done eating");
							status = "";
							// release semaphore now
							personSemaphoreIsAcquired = false;  
							person.guiAtDestination();
							person.print("Semaphore released, done eating");
							//leave him hangin
							leaving = true;
							command = Command.noCommand;
						}
					}, 4000);
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.white);
		g.drawString("Resident: "+ person.getName(), AptPanel.APT_DOOR[this.person.getRoomNumber()-1][0]+30, AptPanel.APT_DOOR[this.person.getRoomNumber()-1][1]);
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, AnimatedPerson.WIDTH, AnimatedPerson.WIDTH);
		g.setColor(Color.WHITE);
		g.drawString(status, xPos, yPos - 6); // draw string (status)
	} 

	// Actions
	
	/**
	 * Moves the person to the door so he can "go outside" (and leave the house)
	 */
	@Override
	public void goOutside() {
		setAcquired();
		if(person.getHome() instanceof House){
		xDestination = HousePanel.HDX;
		yDestination = HousePanel.HDY + 10;
		}else if(person.getHome() instanceof Apt){
			xDestination = AptPanel.APT_DOOR[person.getRoomNumber()-1][0]-10;
			yDestination = AptPanel.APT_DOOR[person.getRoomNumber()-1][1];	
		}
		leaving = true;
		isAtHome = false;
		command = Command.ToDoor;
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
		if(person.getHome() instanceof House){
			xDestination = HousePanel.HBXi-20;
			yDestination = HousePanel.HBYi;
		}else if(person.getHome() instanceof Apt){
			xDestination = AptPanel.APT_BED[person.getRoomNumber()-1][0]-20;
			yDestination = AptPanel.APT_BED[person.getRoomNumber()-1][1];
		}
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
		if(person.getHome() instanceof House){
			xDestination = HousePanel.HRX+20;
			yDestination = HousePanel.HRY;
		}else if(person.getHome() instanceof Apt){
			xDestination = AptPanel.APT_REFRIG[person.getRoomNumber()-1][0];
			yDestination = AptPanel.APT_REFRIG[person.getRoomNumber()-1][1]+20;
		}
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
			if(person.getHome() instanceof House){
				xDestination = HousePanel.HTX+20;
				yDestination = HousePanel.HTY;
			}else if(person.getHome() instanceof Apt){
				xDestination = AptPanel.APT_TABLE[person.getRoomNumber()-1][0];
				yDestination = AptPanel.APT_TABLE[person.getRoomNumber()-1][1]+20;
			}
		} else if(command == Command.StationaryAtRef){ // sent here from refrig
			command = Command.ToStove;
			status = "Found "  + foodToEat;
			if(person.getHome() instanceof House){
				xDestination = HousePanel.HSX+20;
				yDestination = HousePanel.HSY;
			}else if(person.getHome() instanceof Apt){
				xDestination = AptPanel.APT_STOVE[person.getRoomNumber()-1][0];
				yDestination = AptPanel.APT_STOVE[person.getRoomNumber()-1][1]+20;
			}
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
	public void setPerson(Person p) {
		this.person = p;
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
