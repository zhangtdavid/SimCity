package city.animations.interfaces;

import city.animations.interfaces.RestaurantTimmsAnimatedCashier;
import city.interfaces.AbstractAnimation;

public interface RestaurantTimmsAnimatedCustomer extends AbstractAnimation {
	
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
