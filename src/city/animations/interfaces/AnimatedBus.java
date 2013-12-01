package city.animations.interfaces;

import city.AnimationInterface;
import city.Building;
import city.buildings.BusStopBuilding;

public interface AnimatedBus extends AnimationInterface {
	
	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
	
	public void goToDestination(Building destination);

	public void DoGoToNextStop(BusStopBuilding nextStop);
    
    // Getters
    
    // Setters
    
    // Utilities

}
