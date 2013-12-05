package city.animations.interfaces;

import utilities.RestaurantZhangTable;
import city.bases.interfaces.AnimationInterface;

public interface RestaurantZhangAnimatedCustomer extends AnimationInterface {
	
	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
	
	public void DoGoToSeat(RestaurantZhangTable t);
	public void DoExitRestaurant();
	public void DoGoToEntrance();
	public void setFoodLabel(String choice, boolean isHere);
	public void setWaitingPosition(int x, int y);
    
    // Getters
    
    // Setters
    
    // Utilities

}
