package city.animations.interfaces;

import city.agents.interfaces.Person;
import city.bases.interfaces.AnimationInterface;

public interface AnimatedPerson extends AnimationInterface {
	
	// Data
	
	public static int RES_BED_INDEX = -1; // not final. Assigned when person "moves in" - gets assigned to a residence.
	public static enum Command {noCommand, ToHome, ToBuilding, ToBusStop, ToBed, ToStove, ToRef, ToTable, ToDoor, InBed, StationaryAtStove, StationaryAtTable};
    public static final int WIDTH = 20; // Person is a 20x20 rect, standard.
	public static enum OrderIcon {Steak, Chicken, Pizza, Salad}; // could use strings or something on Person's side instead
	
	// Constructor
	
	// Abstract
	
	// Actions
    
	public void goToSleep();
	public void verifyFood();
	public void cookAndEatFood(String in);
	public void goOutside();

    // Getters
	
    public int[] getDestination();
	public String getCommand();
	public String getStatus();
	
    // Setters
	
	public void setCoords(int x, int y);
	public void setGraphicStatus(String in);
	public void setPerson(Person p);
	public void setAtHome();
	public void setAcquired();
    
    // Utilities

}
