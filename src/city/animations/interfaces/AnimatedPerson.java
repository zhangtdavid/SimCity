package city.animations.interfaces;

import city.AnimationInterface;
import city.buildings.BusStopBuilding;

public interface AnimatedPerson extends AnimationInterface {
	
	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
	
	public void goToBusStop(BusStopBuilding b);
	public void goToSleep();
	public void cookAndEatFood();
    
    // Getters
    
    // Setters
    
    // Utilities

}
