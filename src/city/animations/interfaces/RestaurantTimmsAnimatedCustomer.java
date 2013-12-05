package city.animations.interfaces;

import city.bases.interfaces.AnimationInterface;

public interface RestaurantTimmsAnimatedCustomer extends AnimationInterface {
	
	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
	
	public void goToRestaurant();
	public void goToTable(int table);
	public void goToCashier(RestaurantTimmsAnimatedCashier cashier);
	public void goToExit();
    
    // Getters
	
	public boolean getAtRestaurant();
    
    // Setters
	
	public void setPlate(String plate);
    
    // Utilities

}
