package city.animations.interfaces;

import city.bases.interfaces.AnimationInterface;

public interface RestaurantChoiAnimatedFurniture extends AnimationInterface{

	// Data
	static final int TABLEX_INIT = 400;
	static final int TABLEX_INCR = 50;
	static final int TABLEY = 250;
	static final int CPX = 250; // plating (cook3)
	static final int CPY = -20;
	static final int CRX = 150; // refrig (cook1)
	static final int CRY = -20;
	static final int CGX = 200; // grills (cook2)
	static final int CGY = -20;
	static final int DISHES_X = 480; // dishes
	static final int DISHES_Y = 250;
	static final int WIDTH = 30;
	static final int TABLE_COUNT = 4;
	// Constructor

	// Abstract

	// Movement

	// Getters
	public abstract int getTableX(int position);
	public abstract int getTableY(int position);
	public abstract int getRefrigeratorX();
	public abstract int getRefrigeratorY();
	public abstract int getPlatingX();
	public abstract int getPlatingY();
	public abstract int getGrillX();
	public abstract int getGrillY();
	// Setters

	// Utilities

}