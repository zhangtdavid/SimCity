package city.animations.interfaces;

import java.util.LinkedList;

import city.bases.interfaces.AnimationInterface;
import city.roles.interfaces.RestaurantChungCustomer;
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

	void DoReturnToWaiterHome();

	void DoGoToCustomerLine();

	void DoGoToTable(int table);

	void DoBringToTable(RestaurantChungCustomer customer, int table);

	void DoDeliverFood(int table, String choice);

	void DoGoToCook();

	void DoReturnToEntrance();

	void DoGoToCashier();

	void DoGoOnBreak();

	void DoGoOffBreak();

	// Data
	
	// Constructor
	
	// Abstract
	
	// Movement
    
    // Getters
    
    // Setters
    
    // Utilities

}
