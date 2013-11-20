package city.animations.interfaces;

import city.interfaces.AnimationInterface;
import city.interfaces.RestaurantChoiCustomer;
import city.animations.interfaces.RestaurantChoiAnimatedFurniture;

public interface RestaurantChoiAnimatedWaiter extends AnimationInterface{

	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
	public abstract void DoBringToTable(RestaurantChoiCustomer customer, int xTableCoord, int yTableCoord);
	
	public abstract void goOnBreak();
	public abstract void goOffBreak();
	public abstract void GoTo(int x, int y);

	public abstract void DoLeave();

	public abstract void setAcquired();

	public abstract void toTopRight();

	public abstract void toWaitingZone();

	public abstract void setOrderIcon(int choice);
    
    // Getters
    
    // Setters
    
    // Utilities
}
