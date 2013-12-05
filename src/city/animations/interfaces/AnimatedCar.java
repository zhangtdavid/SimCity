package city.animations.interfaces;

import city.AnimationInterface;
import city.BuildingInterface;
import city.gui.CityRoad;
import city.interfaces.Car;

public interface AnimatedCar extends AnimationInterface {
	
	// Data

	static final int SIZE = 25;
	
	// Constructor
	
	// Abstract
	
	// Movement
	
	public void goToDestination(BuildingInterface destination);
    
    // Getters
	
	public BuildingInterface getDestinationBuilding();
	public CityRoad getEndRoad();
	public boolean getAtDestinationRoad();
	public CityRoad getStartingRoad();
	public Car getCar();
    
    // Setters
	
	public void setXPos(int x);
	public void setYPos(int y);
	public void setDestinationBuilding(BuildingInterface destinationBuilding);
	public void setEndRoad(CityRoad endRoad);
	public void setAtDestinationRoad(boolean atDestinationRoad);
	public void setStartingRoad(CityRoad startingRoad);
    
    // Utilities
	
	public boolean contains(int x, int y);

}
