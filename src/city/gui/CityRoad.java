package city.gui;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

import city.Agent;
import city.Animation;
import city.agents.BusAgent;
import city.agents.CarAgent;
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
	boolean isHorizontal;
	boolean startAtOrigin;
	Color laneColor;
	Color sideColor;
	ArrayList<Animation> vehicles;
	
	public CityRoad(int xo, int yo, int w, int h, int xv, int yv, boolean ish, Color lc, Color sc ) {
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
		sideColor = sc;
		
		//Make the lane surface
		rectangle = new Rectangle( xOrigin, yOrigin, width, height );
		
		//Make the edges to the lane surface
		sides = new ArrayList<Line2D.Double>();
		if ( isHorizontal ) {
			sides.add( new Line2D.Double( xOrigin, yOrigin, xOrigin+width, yOrigin ) );
			sides.add( new Line2D.Double( xOrigin, yOrigin+height, xOrigin+width, yOrigin+height ) );
		} else {
			sides.add( new Line2D.Double( xOrigin, yOrigin, xOrigin, yOrigin+height ) );
			sides.add( new Line2D.Double( xOrigin+width, yOrigin, xOrigin+width, yOrigin+height ) );
		}
		
		vehicles = new ArrayList<Animation>();
	}
	
	public void addVehicle( Animation v ) {
		//We need to set the proper origin for this new vehicle, given the lane starting geometry constraints
		//The +2 is due to my lanes being 20 pixels "wide" and vehicles being 16 pixels "wide". 
		vehicles.add(v);
	}
	
	@Override
	public void paint( Graphics g2 ) {
		g2.setColor( laneColor );
		((Graphics2D) g2).fill( rectangle );
		
		double x = 0;
		double y = 0;
		double vWidth = 0;
		double vHeight = 0;
		for ( int i=vehicles.size()-1; i >= 0; i-- ) {
			Animation v = vehicles.get(i);
			if(v instanceof CarAnimation) {
				v = (CarAnimation)v;
				((CarAnimation) v).setXPos(v.getXPos() + xVelocity);
				((CarAnimation) v).setYPos(v.getYPos() + yVelocity);
				x = v.getXPos();
				y = v.getYPos();
				vWidth = CarAnimation.SIZE;
				vHeight = CarAnimation.SIZE;
				
			}
			if(v instanceof BusAnimation) {
				v = (BusAnimation)v;
				((BusAnimation) v).setXPos(v.getXPos() + xVelocity);
				((BusAnimation) v).setYPos(v.getYPos() + yVelocity);
				x = v.getXPos();
				y = v.getYPos();
				vWidth = CarAnimation.SIZE;
				vHeight = CarAnimation.SIZE;
			}
			//Remove the vehicle from the list if it is at the end of the lane
			if ( isHorizontal ) {
				//End of lane is xOrigin + width - vehicle width
				double endOfLane = xOrigin + width - vWidth;
				if ( xVelocity > 0 && x >= endOfLane ) {
					vehicles.remove(i);					
				} else if ( x <= xOrigin ) {
					vehicles.remove(i);
				}
			} else {
				//End of lane is xOrigin + height - vehicle height
				double endOfLane = yOrigin + height - vHeight;
				if ( yVelocity > 0 && y >= endOfLane ) {
					vehicles.remove(i);					
				} else if ( y <= yOrigin ) {
					vehicles.remove(i);
				}
			}
		}
		
		for ( int i=0; i<vehicles.size(); i++ ) {
			Animation v = vehicles.get(i);
			v.draw((Graphics2D) g2);
		}
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
