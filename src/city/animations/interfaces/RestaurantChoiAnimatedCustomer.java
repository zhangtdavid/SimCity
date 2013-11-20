package city.animations.interfaces;

import city.interfaces.AnimationInterface;

public interface RestaurantChoiAnimatedCustomer extends AnimationInterface{


	// Data
	enum Command {
		noCommand, GoToSeat, LeaveRestaurant, GoToDishes, GoToCashier, GoToWaiting
	};
	// Constructor
	
	// Abstract
	
	// Movement
    void DoGoToCashier();
    void DoExitRestaurant();
    void DoGoToWaiting(int offset);
    void DoGoToDishes();
    void DoGoToSeat(int x, int y);
    // Getters
    boolean isHungry();
    boolean isPresent();
    int getXWait();
    int getYWait();
    // Setters
    void setOrderIcon(int i, boolean b);
    void setPresent(boolean b);
    void setHungry();
    // Utilities
    void resetLocation();
}
