package city.animations.interfaces;

import city.bases.Building;
import city.bases.interfaces.AnimationInterface;
import city.buildings.interfaces.BusStop;

public interface AnimatedBus extends AnimationInterface {
	
	// Data
	public static final int SIZE = 25;
	
	// Constructor
	
	// Abstract
	
	// Movement
	
	public void goToDestination(Building destination);

	public void doGoToNextStop(BusStop nextStop);
    
    // Getters
    
    // Setters
    
    // Utilities

}
