package city.animations.interfaces;

import city.Building;
import city.interfaces.AnimationInterface;

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
