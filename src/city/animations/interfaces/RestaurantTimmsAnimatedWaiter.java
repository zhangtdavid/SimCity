package city.animations.interfaces;

import city.bases.interfaces.AnimationInterface;

public interface RestaurantTimmsAnimatedWaiter extends AnimationInterface {
	
	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
	
	public abstract void goOnBreak();
	public abstract void goOffBreak();
	public abstract void goToCustomer(RestaurantTimmsAnimatedCustomer customer);
	public abstract void goToTable(int position, String menuItem);
	public abstract void goToKitchen(RestaurantTimmsAnimatedCook cook);
	public abstract void goToHome(int position);
    
    // Getters
    
    // Setters
    
    // Utilities

}
