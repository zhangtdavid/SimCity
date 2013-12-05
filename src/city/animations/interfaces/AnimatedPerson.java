package city.animations.interfaces;

import city.bases.interfaces.AnimationInterface;
import city.buildings.interfaces.BusStop;

public interface AnimatedPerson extends AnimationInterface {
	
	// Data
	public static int RES_BED_INDEX = -1; // not final. Assigned when person "moves in" - gets assigned to a residence.
	public static enum Command {noCommand, ToHome, ToBuilding, ToBusStop}; // r=residence; w=walking
    public static final int WIDTH = 20; // Person is a 20x20 rect, standard.
	
	// Constructor
	
	// Abstract
	
	// Movement
	
	public void goToBusStop(BusStop b);
	
    
    // Getters
    
    // Setters
    
    // Utilities

}
