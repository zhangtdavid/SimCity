package city.animations.interfaces;

import city.interfaces.AnimationInterface;

public interface RestaurantTimmsAnimatedWaiter extends AnimationInterface {
	
	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
	public void DoBringToTable(RestaurantChoiAnimatedCustomer customer, int xTableCoord, int yTableCoord);
	public abstract void goOnBreak();
	public abstract void goOffBreak();
	public abstract void goToCustomer(RestaurantChoiAnimatedCustomer customer);
	public abstract void goToTable(int position, String menuItem);
	public abstract void goToKitchen(RestaurantChoiAnimatedCook cook);
	public abstract void goToHome(int position);
    
    // Getters
    
    // Setters
    
    // Utilities

}
