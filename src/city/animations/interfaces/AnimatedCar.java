package city.animations.interfaces;

import city.AnimationInterface;
import city.BuildingInterface;

public interface AnimatedCar extends AnimationInterface {
	
	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
	
	public void goToDestination(BuildingInterface destination);
    
    // Getters
    
    // Setters
	public void setXPos(int x);
	public void setYPos(int y);
    
    // Utilities

}
