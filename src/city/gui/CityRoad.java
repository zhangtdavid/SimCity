package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import city.Animation;
import city.animations.BusAnimation;
import city.animations.CarAnimation;

public class CityRoad extends CityViewBuilding {
	ArrayList<Line2D.Double> sides;
	int xVelocity;
	int yVelocity;
	boolean redLight;
	int xOrigin;
	int yOrigin;
	int width;
	int height;
	public boolean isHorizontal;
	boolean startAtOrigin;
	Color laneColor;
	public Animation vehicle = null;
	public CityRoad nextRoad;

	public CityRoad(int xo, int yo, int w, int h, int xv, int yv, boolean ish, Color lc) {
		super(xo, yo, lc);
		redLight = false;
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

	public boolean addVehicle( Animation v ) {
		if(vehicle == null) {
			vehicle = v;
			return true;
		}
		return false;
	}

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
			vehicle = (CarAnimation)vehicle;
			if(((CarAnimation) vehicle).endRoad == this) {
				((CarAnimation) vehicle).atDestinationRoad = true;
				vehicle = null;
				return;
			}
			if(nextRoad.vehicle == null && ((CarAnimation) vehicle).startingRoad == null) {
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
				nextRoad.vehicle = vehicle;
				vehicle = null;
			} else if ( xVelocity < 0 && x <= xOrigin - vWidth ) {
				nextRoad.vehicle = vehicle;
				vehicle = null;
			}
		} else {
			if ( yVelocity > 0 && y >= yOrigin + vHeight ) {
				nextRoad.vehicle = vehicle;
				vehicle = null;
			} else if ( yVelocity < 0 && y <= yOrigin - vHeight ) {
				nextRoad.vehicle = vehicle;
				vehicle = null;
			}
		}

//		if(vehicle != null)
//			vehicle.draw((Graphics2D) g2);
	}

	public void redLight() {
		redLight = true;
	}

	public void greenLight() {
		redLight = false;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub

	}
}
