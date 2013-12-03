package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import city.Animation;
import city.animations.BusAnimation;
import city.animations.CarAnimation;
import city.gui.views.CityViewBuilding;

public class CityRoad extends CityViewBuilding {
	
	// Data
	
	protected int xVelocity;
	protected int yVelocity;
	protected int xOrigin;
	protected int yOrigin;
	protected int width;
	protected int height;
	protected Color laneColor;
	
	protected Animation vehicle = null;
	protected CityRoad nextRoad;
	protected boolean isHorizontal;
	
	// Constructor
	
	public CityRoad(int xo, int yo, int w, int h, int xv, int yv, boolean ish, Color lc) {
		super(xo, yo, lc);
		width = w;
		height = h;
		xVelocity = xv;
		yVelocity = yv;
		xOrigin = xo;
		yOrigin = yo;
		isHorizontal = ish;
		laneColor = lc;

		//Make the lane surface
		rectangle = new Rectangle( xOrigin, yOrigin, width, height );
	}
	
	// Paint stuff
	
	@Override
	public void paint( Graphics g2 ) {
		g2.setColor( laneColor );
		((Graphics2D) g2).fill( rectangle );
		
		if(vehicle == null) 
			return;
		double x = 0;
		double y = 0;
		double vWidth = 0;
		double vHeight = 0;
		if(vehicle instanceof CarAnimation) {
			vehicle = (CarAnimation) vehicle;
			if(((CarAnimation) vehicle).getEndRoad() == this) {
				((CarAnimation) vehicle).setAtDestinationRoad(true);
				vehicle = null;
				return;
			}
			if(nextRoad.vehicle == null && ((CarAnimation) vehicle).getStartingRoad() == null) {
				((CarAnimation) vehicle).setXPos(vehicle.getXPos() + xVelocity);
				((CarAnimation) vehicle).setYPos(vehicle.getYPos() + yVelocity);
			}
			x = vehicle.getXPos();
			y = vehicle.getYPos();
			vWidth = CarAnimation.SIZE;
			vHeight = CarAnimation.SIZE;
		}
		if(vehicle instanceof BusAnimation) {
			vehicle = (BusAnimation)vehicle;
			if(((BusAnimation) vehicle).atDestination) {
				return;
			}
			if(nextRoad.vehicle == null) {
				((BusAnimation) vehicle).setXPos(vehicle.getXPos() + xVelocity);
				((BusAnimation) vehicle).setYPos(vehicle.getYPos() + yVelocity);
			}
			x = vehicle.getXPos();
			y = vehicle.getYPos();
			vWidth = CarAnimation.SIZE;
			vHeight = CarAnimation.SIZE;
		}
		//Remove the vehicle from the list if it is at the end of the lane
		if ( isHorizontal ) {
			//End of lane is xOrigin + width - vehicle width
			if ( xVelocity > 0 && x >= xOrigin + vWidth) {
				nextRoad.setVehicle(vehicle);
				vehicle = null;
			} else if ( xVelocity < 0 && x <= xOrigin - vWidth ) {
				nextRoad.setVehicle(vehicle);
				vehicle = null;
			}
		} else {
			if ( yVelocity > 0 && y >= yOrigin + vHeight ) {
				nextRoad.setVehicle(vehicle);
				vehicle = null;
			} else if ( yVelocity < 0 && y <= yOrigin - vHeight ) {
				nextRoad.setVehicle(vehicle);
				vehicle = null;
			}
		}
	}

	@Override
	public void updatePosition() { };
	
	 // Getters
	
	public CityRoad getNextRoad() {
		return nextRoad;
	}
	
	public Animation getVehicle() {
		return vehicle;
	}
	
	public boolean getHorizontal() {
		return isHorizontal;
	}
	
	// Setters
	
	public void setNextRoad( CityRoad r ) {
		nextRoad = r;
	}
	
	public boolean setVehicle( Animation v ) {
		if(vehicle == null) {
			vehicle = v;
			return true;
		}
		return false;
	}
}
