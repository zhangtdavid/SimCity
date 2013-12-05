package city.animations.interfaces;

import city.bases.interfaces.AnimationInterface;

public interface RestaurantZhangAnimatedCook extends AnimationInterface {
	
	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
	
	public void GoToDestination(int x, int y);
    
    public void addToPlatingArea(String s, int pos);
    
    public void removeFromPlatingArea(int pos);
    
    public void addToGrill(String s, int pos);
    
    public void removeFromGrill(int pos);
    
    public void goToBase();
    
    public void goToPlating();
    
    public void goToGrill(int grillNumber);

    public boolean ReturnToBase();
    
    public void setFoodLabel(String choice, boolean isHere);
    
    // Getters
    
    // Setters
    
    // Utilities

}
