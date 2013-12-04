package city.animations;

import java.awt.Graphics2D;

import city.Animation;
import city.Application.BUILDING;
import city.Application.CityMap;
import city.animations.interfaces.AnimatedPerson;
import city.interfaces.BusStop;
import city.interfaces.Person;

public class PersonAnimation extends Animation implements AnimatedPerson { // needs code standard review
	
	//Data
	int xDestination, yDestination;
	Person person = null;
	
	//Update position (Drawing)
	@Override
	public void updatePosition() {

	}

	/**
	 * Draws person and a string symbolizing the food he is making/eating at the time. 
	 */
	@Override
	public void draw(Graphics2D g) {
	} 

	
	//Movement
	
	/**
	 * Moves the person to a bus stop (already determined which is closest) 
	 */
	@Override
	public void goToBusStop(BusStop b) {
		xDestination = CityMap.findClosestBuilding(BUILDING.busStop, person).getCityViewBuilding().x;
		xDestination = CityMap.findClosestBuilding(BUILDING.busStop, person).getCityViewBuilding().y;
	}
}
