package city.animations.interfaces;

import utilities.RestaurantZhangTable;
import city.bases.interfaces.AnimationInterface;

public interface RestaurantZhangAnimatedWaiter extends AnimationInterface {
	
	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
	
	public void GoToTable(RestaurantZhangTable t);
    
    public void GoToDestination(int x, int y);
    
    public void GoToCustomer(int pos);
    
    public void GoToBreak();

    public boolean ReturnToBase();
    
    public void setFoodLabel(String choice, boolean isHere);
    
    // Getters
    
    // Setters
    
    // Utilities

}
