package city.animations.interfaces;

import city.bases.interfaces.AnimationInterface;
import city.bases.interfaces.BuildingInterface;

public interface AnimatedBusPassenger extends AnimationInterface {
	
	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
	
	void goToDestination(BuildingInterface destination);

	void getOffBus();

	void goToBus();
    
    // Getters
    
    // Setters
    
    // Utilities

}
