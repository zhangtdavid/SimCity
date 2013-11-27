package city.animations.interfaces;

import city.Building;
import city.buildings.BusStopBuilding;
import city.interfaces.AnimationInterface;

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
