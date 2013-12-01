package city.animations.interfaces;

import city.AnimationInterface;
import city.Building;

public interface AnimatedCar extends AnimationInterface {
	
	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
	
	public void goToDestination(Building destination);
    
    // Getters
    
    // Setters
	public void setXPos(int x);
	public void setYPos(int y);
    
    // Utilities

}
