package city.animations.interfaces;

import city.bases.interfaces.AnimationInterface;
import city.roles.interfaces.MarketEmployee;

public interface MarketAnimatedCustomer extends AnimationInterface{

	void DoStandInWaitingForServiceLine();

	void DoGoToCounter(MarketEmployee employee);

	void DoStandInWaitingForItemsLine();

	void DoGoToCashier();

	void DoExitMarket();

	// Data

	// Constructor
	
	// Abstract
	
	// Movement
    
    // Getters
    
    // Setters
    
    // Utilities

}
