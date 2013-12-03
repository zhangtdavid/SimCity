package city.animations.interfaces;

import city.AnimationInterface;
import city.interfaces.BusStop;

public interface AnimatedPerson extends AnimationInterface {
	
	// Data
	public static int RES_BED_INDEX = 0; // not final. Assigned when person "moves in" - gets assigned to a residence.
	public static final int RES_BED_Y_INTERVAL = 100;
	public static final int RES_BED[] = {490,50};
	public static final int RES_REFRIGERATOR[] = {-10,100}; // {x, y}
	public static final int RES_STOVE[] = {-10,250};
	public static final int RES_KITCHEN_TABLE[] = {-10,400};
	public static final int RES_DOOR[] = {250,490};
	public static enum Command {noCommand, rToBed, rToRef, rToStove, rToTable, rToDoor, wToBuilding, wToBusStop};
	public static enum OrderIcon {Steak, Chicken, Pizza, Salad}; // could use strings or something on Person's side instead
    public static final int WIDTH = 20; // Person is a 20x20 rect, standard.
	
	// Constructor
	
	// Abstract
	
	// Movement
	
	public void goToBusStop(BusStop b);
	public void goToSleep();
	public void verifyFood();
	public void cookAndEatFood();
	public void cookAndEatFoodPart2();
	
    
    // Getters
    
    // Setters
    
    // Utilities

}
