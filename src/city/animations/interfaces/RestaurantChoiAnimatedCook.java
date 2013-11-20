package city.animations.interfaces;

import city.interfaces.AnimationInterface;

public interface RestaurantChoiAnimatedCook extends AnimationInterface {


	// Data
	static final int CPX = 250; // plating (cook3)
	static final int CPY = 0;
	static final int CRX = 150; // refrig (cook1)
	static final int CRY = 0;
	static final int CGX = 200; // grills (cook2)
	static final int CGY = 0;
	static final int WIDTH = 20;
	static final int restingCoordX = 200;//default cook position
	static final int restingCoordY = 50;
	// Constructor

	// Abstract

	// Movement
	public void DoLeave();
	public void toRef();
	public void toGrill();
	public void toPlate();
	
	// Getters
	int getXPos();
	int getYPos();
	
	// Setters
	void setOrderIcon(int choice);    
	void setAcquired();
	
	// Utilities
	boolean isPresent();
}
