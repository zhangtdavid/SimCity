package city.animations.interfaces;

import java.util.LinkedList;

import city.bases.interfaces.AnimationInterface;
import city.roles.interfaces.MarketEmployee;

public interface MarketAnimatedEmployee extends AnimationInterface{

	void doGoToPhone();

	void doCollectItems();

	void doDeliverItems();

	void doGoToCounter();

	LinkedList<MarketEmployee> getEmployeeStalls();

	int getCounterLoc();

	void removeFromEmployeeStalls();

	// Data

	// Constructor
	
	// Abstract
	
	// Movement
    
    // Getters
    
    // Setters
    
    // Utilities

}
