package city.animations.interfaces;

import city.Application.FOOD_ITEMS;
import city.bases.interfaces.AnimationInterface;
import city.roles.interfaces.RestaurantChoiCustomer;

public interface RestaurantChoiAnimatedWaiter extends AnimationInterface{

	// Data
	public static final int COOKX = 270; // go to plating; also put orders there
	public static final int COOKY = 20;    
	public static final int CASHX = 480;
	public static final int CASHY = 0;
	public static final int WIDTH = 20;
	public static final int RESTX = 350;
	public static final int RESTY = 20; 
	public static final int DOORX = 620;
	public static final int DOORY = -20;

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
