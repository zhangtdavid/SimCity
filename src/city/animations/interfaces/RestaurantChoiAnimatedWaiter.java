package city.animations.interfaces;

import city.interfaces.AnimationInterface;
import city.interfaces.RestaurantChoiCustomer;

public interface RestaurantChoiAnimatedWaiter extends AnimationInterface{

	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
	public abstract void DoBringToTable(RestaurantChoiAnimatedCustomer customer, int xTableCoord, int yTableCoord);
	
	public abstract void goOnBreak();
	public abstract void goOffBreak();
	public abstract void GoTo(int x, int y);
    
    // Getters
    
    // Setters
    
    // Utilities
}
