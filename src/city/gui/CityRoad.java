package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import city.Application;
import city.animations.BusAnimation;
import city.animations.CarAnimation;
import city.bases.Animation;
import city.bases.interfaces.AnimationInterface;
import city.gui.exteriors.CityViewBuilding;

public class CityRoad extends CityViewBuilding {

	// Data
	public enum STOPLIGHTTYPE{HORIZONTALON, HORIZONTALOFF, VERTICALON, VERTICALOFF};

	protected int xVelocity;
	protected int yVelocity;
	protected int xOrigin;
	protected int yOrigin;
	protected int width;
	protected int height;
	protected Color laneColor;

	protected volatile Animation vehicle = null;
	protected CityRoad nextRoad;
	protected boolean isHorizontal;
	protected boolean isRedLight = false;
	protected STOPLIGHTTYPE stopLightType = STOPLIGHTTYPE.HORIZONTALOFF;

	protected List<CitySidewalk> intersectionSidewalks = new ArrayList<CitySidewalk>();

	protected BufferedImage imageToRender = null;

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

	public CityRoad(int xo, int yo, int w, int h, int xv, int yv, boolean ish, Color lc, STOPLIGHTTYPE spotLightType) {
		super(xo, yo, lc);
		width = w;
		height = h;
		xVelocity = xv;
		yVelocity = yv;
		xOrigin = xo;
		yOrigin = yo;
		isHorizontal = ish;
		laneColor = lc;
		isRedLight = true;
		this.stopLightType = spotLightType;

		//Make the lane surface
		rectangle = new Rectangle( xOrigin, yOrigin, width, height );
	}

	// Paint stuff

	@Override
	public void paint( Graphics g2 ) {
		if(isUgly) {
			if(isRedLight && (stopLightType == STOPLIGHTTYPE.HORIZONTALOFF || stopLightType == STOPLIGHTTYPE.VERTICALOFF)) {
				laneColor = Color.green;
			}
			if(isRedLight && (stopLightType == STOPLIGHTTYPE.HORIZONTALON || stopLightType == STOPLIGHTTYPE.VERTICALON)) {
				laneColor = Color.red;
			}
		g2.setColor(laneColor);
		g2.fillRect(xOrigin, yOrigin, width, height);
		} else
			g2.drawImage(imageToRender, xOrigin, yOrigin, null);
		if(vehicle == null) 
			return;
		double x = 0;
		double y = 0;
		double vWidth = 0;
		double vHeight = 0;
		// Check for pedestrians at intersection
		for(CitySidewalk sidewalksAtIntersection : intersectionSidewalks) {
			if(sidewalksAtIntersection.getCurrentOccupant() != null)
				return;
		}
		if(vehicle instanceof CarAnimation) {
			vehicle = (CarAnimation) vehicle;
			if(((CarAnimation) vehicle).getEndRoad() == this) {
				((CarAnimation) vehicle).setAtDestinationRoad(true);
				vehicle = null;
				return;
			}
			if(nextRoad.vehicle == null && ((CarAnimation) vehicle).getStartingRoad() == null) {
				if(!(isRedLight && (stopLightType == STOPLIGHTTYPE.HORIZONTALON || stopLightType == STOPLIGHTTYPE.VERTICALON) && ((CarAnimation)vehicle).getXPos() == xOrigin && ((CarAnimation)vehicle).getYPos() == yOrigin)) {
					for(CitySidewalk sidewalksAtIntersection : intersectionSidewalks) {
						if(sidewalksAtIntersection.getCurrentOccupant() != null)
							return;
					}
					((CarAnimation) vehicle).setXPos(vehicle.getXPos() + xVelocity);
					((CarAnimation) vehicle).setYPos(vehicle.getYPos() + yVelocity);
				}
			}
			x = vehicle.getXPos();
			y = vehicle.getYPos();
			vWidth = CarAnimation.SIZE;
			vHeight = CarAnimation.SIZE;
		}
		if(vehicle instanceof BusAnimation) {
			vehicle = (BusAnimation)vehicle;
			if(((BusAnimation) vehicle).isAtDestination()) {
				return;
			}
			if(nextRoad.vehicle == null) {
				if(!(isRedLight && (stopLightType == STOPLIGHTTYPE.HORIZONTALON || stopLightType == STOPLIGHTTYPE.VERTICALON) && ((BusAnimation)vehicle).getXPos() == xOrigin && ((BusAnimation)vehicle).getYPos() == yOrigin)) {
					for(CitySidewalk sidewalksAtIntersection : intersectionSidewalks) {
						if(sidewalksAtIntersection.getCurrentOccupant() != null)
							return;
					}
					((BusAnimation) vehicle).setXPos(vehicle.getXPos() + xVelocity);
					((BusAnimation) vehicle).setYPos(vehicle.getYPos() + yVelocity);
				}
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
				if(nextRoad.setVehicle(vehicle))
					vehicle = null;
			} else if ( xVelocity < 0 && x <= xOrigin - vWidth ) {
				if(nextRoad.setVehicle(vehicle))
					vehicle = null;
			}
		} else {
			if ( yVelocity > 0 && y >= yOrigin + vHeight ) {
				if(nextRoad.setVehicle(vehicle))
					vehicle = null;
			} else if ( yVelocity < 0 && y <= yOrigin - vHeight ) {
				if(nextRoad.setVehicle(vehicle))
					vehicle = null;
			}
		}
	}

	@Override
	public void updatePosition() { };

	// Getters

	public int getXVelocity() {
		return xVelocity;
	}

	public int getYVelocity() {
		return yVelocity;
	}

	public CityRoad getNextRoad() {
		return nextRoad;
	}

	public Animation getVehicle() {
		return vehicle;
	}

	public boolean getHorizontal() {
		return isHorizontal;
	}

	public boolean isRedLight() {
		return isRedLight;
	}

	public STOPLIGHTTYPE getStopLightType() {
		return stopLightType;
	}

	public List<CitySidewalk> getIntersectionSidewalks() {
		return intersectionSidewalks;
	}

	public boolean isWalkerAt(int x, int y) {
		if(Application.sidewalks == null || Application.trafficControl == null)
			return false;
		List<AnimationInterface> walkersList = Application.sidewalks.getAllWalkers();
		for(AnimationInterface a : walkersList) {
			int walkerSize = (int)(Application.sidewalks.getSidewalkSize());
			Rectangle walkerRect = new Rectangle(a.getXPos(), a.getYPos(), walkerSize, walkerSize);
			if(walkerRect.intersects(new Rectangle(x, y, height, width)))
				return true;
		}
		return false;
		/*if(Application.sidewalks.isWalkerAt(x, y) ||
				Application.sidewalks.isWalkerAt(x + (int)(Application.sidewalks.getSidewalkSize()), y) ||
				Application.sidewalks.isWalkerAt(x, y + (int)(Application.sidewalks.getSidewalkSize())) ||
				Application.sidewalks.isWalkerAt(x + (int)(Application.sidewalks.getSidewalkSize()),
						y + (int)(Application.sidewalks.getSidewalkSize())))
			return true;
		else
			return false;*/
	}

	// Setters

	public void setNextRoad( CityRoad r ) {
		nextRoad = r;
	}

	public synchronized boolean setVehicle( Animation v ) {
		if(vehicle == null || v == null || vehicle == v) {
			vehicle = v;
			return true;
		}
		return false;
	}

	public void setStopLightType(STOPLIGHTTYPE newType) {
		stopLightType = newType;
		isRedLight = true;
	}

	public void addIntersectionSidewalk(CitySidewalk intersectionSidewalk) {
		if(!intersectionSidewalks.contains(intersectionSidewalk))
			intersectionSidewalks.add(intersectionSidewalk);
	}

	public void setImageToRender(BufferedImage image) {
		imageToRender = image;
	}
}
