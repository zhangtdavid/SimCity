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
	private boolean atIntersection = false;

	public CityRoadIntersection(int xo, int yo, int w, int h, Color lc) {
		super(xo, yo, w, h, 0, 0, false, lc);
	}

	// Paint stuff

	@Override
	public void paint( Graphics g2 ) {
		g2.setColor( laneColor );
		((Graphics2D) g2).fill( rectangle );

		if(vehicle == null) {
			return;
		}
		double x = 0;
		double y = 0;
		double vWidth = 0;
		double vHeight = 0;
		CityRoad thisNextRoad = nextRoads.get(0);
		
		if(vehicle instanceof CarAnimation) {
			vehicle = (CarAnimation) vehicle;

			// Move it to the intersection fully
			if(atIntersection == false) {
				if(((CarAnimation) vehicle).getXPos() < xOrigin) {
					((CarAnimation) vehicle).setXPos(vehicle.getXPos() + 1);
					return;
				} else if(((CarAnimation) vehicle).getXPos() > xOrigin) {
					((CarAnimation) vehicle).setXPos(vehicle.getXPos() - 1);
					return;
				} else if(((CarAnimation) vehicle).getYPos() < yOrigin) {
					((CarAnimation) vehicle).setYPos(vehicle.getYPos() + 1);
					return;
				} else if(((CarAnimation) vehicle).getYPos() > yOrigin) {
					((CarAnimation) vehicle).setYPos(vehicle.getYPos() - 1);
					return;
				}
				atIntersection = true;
			}
			
			// At the destination
			if(((CarAnimation) vehicle).getEndRoad() == this) { // This car is at its destination road
				((CarAnimation) vehicle).setAtDestinationRoad(true);
				vehicle = null;
				atIntersection = false;
				return;
			}

			// Find which connecting road is the closest
			synchronized(nextRoads) {
				double closestDistance = 1000000;
				for(CityRoad r : nextRoads) {
					CityRoad tempRoad = r;
					while(tempRoad.getNextRoad().getClass() != CityRoadIntersection.class) {
						tempRoad = tempRoad.getNextRoad();
					}
					double distance = Math.sqrt((double)(Math.pow(tempRoad.getX() - ((CarAnimation) vehicle).getEndRoad().getX(), 2) 
							+ Math.pow(tempRoad.getY() - ((CarAnimation) vehicle).getEndRoad().getY(), 2)));
					if(distance < closestDistance) {
						closestDistance = distance;
						thisNextRoad = r;
					}
				}
			}

			// If the next road is clear, move it to the road
			if(thisNextRoad.vehicle == null && ((CarAnimation) vehicle).getStartingRoad() == null) {
				((CarAnimation) vehicle).setXPos(vehicle.getXPos() + thisNextRoad.xVelocity);
				((CarAnimation) vehicle).setYPos(vehicle.getYPos() + thisNextRoad.yVelocity);
			}

			// Needed for future moveToRoad calculation
			x = vehicle.getXPos();
			y = vehicle.getYPos();
			vWidth = CarAnimation.SIZE;
			vHeight = CarAnimation.SIZE;
		}
		if(vehicle instanceof BusAnimation) {
			vehicle = (BusAnimation)vehicle;

			// Move it to the intersection fully
			if(atIntersection == false) {
				if(((BusAnimation) vehicle).getXPos() < xOrigin) {
					((BusAnimation) vehicle).setXPos(vehicle.getXPos() + 1);
					return;
				} else if(((BusAnimation) vehicle).getXPos() > xOrigin) {
					((BusAnimation) vehicle).setXPos(vehicle.getXPos() - 1);
					return;
				} else if(((BusAnimation) vehicle).getYPos() < yOrigin) {
					((BusAnimation) vehicle).setYPos(vehicle.getYPos() + 1);
					return;
				} else if(((BusAnimation) vehicle).getYPos() > yOrigin) {
					((BusAnimation) vehicle).setYPos(vehicle.getYPos() - 1);
					return;
				}
				atIntersection = true;
			}
			
			// At the destination
			if(((BusAnimation) vehicle).isAtDestination()) {
				return;
			}

			// Find which connecting road is the closest
			synchronized(nextRoads) {
				double closestDistance = 1000000;
				for(CityRoad r : nextRoads) {
					CityRoad tempRoad = r;
					while(tempRoad.getNextRoad().getClass() != CityRoadIntersection.class) {
						tempRoad = tempRoad.getNextRoad();
					}
					double distance = Math.sqrt((double)(Math.pow(tempRoad.getX() - ((BusAnimation) vehicle).getBus().getNextStop().getRoadLocatedOn().getX(), 2) 
							+ Math.pow(tempRoad.getY() - ((BusAnimation) vehicle).getBus().getNextStop().getRoadLocatedOn().getY(), 2)));
					if(distance < closestDistance) {
						closestDistance = distance;
						thisNextRoad = r;
					}
				}
			}
			// If the next road is clear, move it to the road
			if(thisNextRoad.vehicle == null) {
				((BusAnimation) vehicle).setXPos(vehicle.getXPos() + thisNextRoad.xVelocity);
				((BusAnimation) vehicle).setYPos(vehicle.getYPos() + thisNextRoad.yVelocity);
			}

			x = vehicle.getXPos();
			y = vehicle.getYPos();
			vWidth = BusAnimation.SIZE;
			vHeight = BusAnimation.SIZE;
		}

		//Remove the vehicle from the list if it is at the end of the lane
		//End of lane is xOrigin + width - vehicle width
		if ( x >= xOrigin + vWidth) {
			thisNextRoad.setVehicle(vehicle);
			vehicle = null;
			atIntersection = false;
		} else if ( x <= xOrigin - vWidth ) {
			thisNextRoad.setVehicle(vehicle);
			vehicle = null;
			atIntersection = false;
		}
		if ( y >= yOrigin + vHeight ) {
			thisNextRoad.setVehicle(vehicle);
			vehicle = null;
			atIntersection = false;
		} else if ( y <= yOrigin - vHeight ) {
			thisNextRoad.setVehicle(vehicle);
			vehicle = null;
			atIntersection = false;
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
