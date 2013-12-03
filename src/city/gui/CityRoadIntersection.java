package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import city.animations.BusAnimation;
import city.animations.CarAnimation;

public class CityRoadIntersection extends CityRoad {

	private List<CityRoad> nextRoads = Collections.synchronizedList(new ArrayList<CityRoad>());

	public CityRoadIntersection(int xo, int yo, int w, int h, int xv, int yv, Color lc) {
		super(xo, yo, w, h, xv, yv, false, lc);
	}

	// Paint stuff

	@Override
	public void paint( Graphics g2 ) {
		g2.setColor( laneColor );
		((Graphics2D) g2).fill( getRectangle() );

		if(getVehicle() == null) 
			return;
		double x = 0;
		double y = 0;
		double vWidth = 0;
		double vHeight = 0;
		if(getVehicle() instanceof CarAnimation) {
			setVehicle((CarAnimation)getVehicle());
			if(((CarAnimation) getVehicle()).getEndRoad() == this) {
				((CarAnimation) getVehicle()).setAtDestinationRoad(true);
				setVehicle(null);
				return;
			}
			if(nextRoad.getVehicle() == null && ((CarAnimation) getVehicle()).getStartingRoad() == null) {
				((CarAnimation) getVehicle()).setXPos(getVehicle().getXPos() + xVelocity);
				((CarAnimation) getVehicle()).setYPos(getVehicle().getYPos() + yVelocity);
			}
			x = getVehicle().getXPos();
			y = getVehicle().getYPos();
			vWidth = CarAnimation.SIZE;
			vHeight = CarAnimation.SIZE;
		}
		if(getVehicle() instanceof BusAnimation) {
			setVehicle((BusAnimation)getVehicle());
			if(((BusAnimation) getVehicle()).atDestination) {
				return;
			}
			if(nextRoad.getVehicle() == null) {
				((BusAnimation) getVehicle()).setXPos(getVehicle().getXPos() + xVelocity);
				((BusAnimation) getVehicle()).setYPos(getVehicle().getYPos() + yVelocity);
			}
			x = getVehicle().getXPos();
			y = getVehicle().getYPos();
			vWidth = CarAnimation.SIZE;
			vHeight = CarAnimation.SIZE;
		}
		//Remove the vehicle from the list if it is at the end of the lane
		if ( isHorizontal ) {
			//End of lane is xOrigin + width - vehicle width
			if ( xVelocity > 0 && x >= xOrigin + vWidth) {
				nextRoad.setVehicle(vehicle);
				setVehicle(null);
			} else if ( xVelocity < 0 && x <= xOrigin - vWidth ) {
				nextRoad.setVehicle(vehicle);
				setVehicle(null);
			}
		} else {
			if ( yVelocity > 0 && y >= yOrigin + vHeight ) {
				nextRoad.setVehicle(vehicle);
				setVehicle(null);
			} else if ( yVelocity < 0 && y <= yOrigin - vHeight ) {
				nextRoad.setVehicle(vehicle);
				setVehicle(null);
			}
		}
	}
	
	// Getters
	
	public List<CityRoad> getNextRoads() {
		return nextRoads;
	}
	
	// Setters
	
	@Override
	public void setNextRoad( CityRoad r ) {
		nextRoads.add(r);
	}
}
