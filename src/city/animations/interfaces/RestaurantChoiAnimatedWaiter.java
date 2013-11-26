package city.animations.interfaces;

import city.Application.FOOD_ITEMS;
import city.interfaces.AnimationInterface;
import city.interfaces.RestaurantChoiCustomer;

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

	public abstract void setOrderIcon(FOOD_ITEMS food_ITEMS);
    
    // Getters
    
    // Setters
    
    // Utilities
}
