package city.animations.interfaces;

import city.AnimationInterface;
import city.Building;
import city.interfaces.BusStop;

public interface AnimatedBus extends AnimationInterface {
	
	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
	
	public void goToDestination(Building destination);

	public void DoGoToNextStop(BusStop nextStop);
    
    // Getters
    
    // Setters
    
    // Utilities

}
