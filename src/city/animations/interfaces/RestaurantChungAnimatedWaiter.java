package city.animations.interfaces;

import java.util.LinkedList;

import city.bases.interfaces.AnimationInterface;
import city.roles.interfaces.RestaurantChungWaiter;

public interface RestaurantChungAnimatedWaiter extends AnimationInterface {

	LinkedList<RestaurantChungWaiter> getWaiterPositions();

	void addTable(int x, int y);

	int findTableX(int table);

	int findTableY(int table);

	void setAskedForBreak();

	void setOnBreak();

	void setOffBreak();

	String isOnBreak();

	void removeFromWaiterHomePositions();

	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
    
    // Getters
    
    // Setters
    
    // Utilities

}
