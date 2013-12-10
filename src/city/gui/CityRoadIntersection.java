package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import city.animations.BusAnimation;
import city.animations.CarAnimation;

public class CityRoadIntersection extends CityRoad {

	private List<CityRoad> nextRoads = Collections.synchronizedList(new ArrayList<CityRoad>());
	private boolean atIntersection = false;

	public CityRoadIntersection(int xo, int yo, int w, int h, Color lc) {
		super(xo, yo, w, h, 0, 0, false, lc);
	}

	private CityRoad currentNextRoad = null;

	// Paint stuff

	@Override
	public void paint( Graphics g2 ) {
		g2.setColor( laneColor );
		((Graphics2D) g2).fill( rectangle );
		g2.drawImage(imageToRender, xOrigin, yOrigin, null);

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
			thisNextRoad = findBestRoad(this, ((CarAnimation) vehicle).getEndRoad());
			currentNextRoad = thisNextRoad;

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
			thisNextRoad = findBestRoad(this, ((BusAnimation) vehicle).getBus().getNextStop().getRoadLocatedOn());
			currentNextRoad = thisNextRoad;
			
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
	
	public CityRoad getCurrentNextRoad() {
		return currentNextRoad;
	}

	/*
	 * Returns a road that will result in the correct destination from the given intersection
	 */
	private CityRoad findBestRoad(CityRoadIntersection intersection, CityRoad destinationRoad) {
		Hashtable<CityRoad, CityRoad> possibleRoads = new Hashtable<CityRoad, CityRoad>();
		for(int i = 0; i < intersection.getNextRoads().size(); i++) {
			possibleRoads.put(intersection.getNextRoads().get(i), intersection.getNextRoads().get(i));
		}
		int counter = 0;
		while(counter <= 1000) {
			counter++;
			ArrayList<CityRoad> keys = new ArrayList<CityRoad>(possibleRoads.keySet());
			for(CityRoad r : keys) {
				if(r.equals(destinationRoad)) // Check if this road is the destination road
					return possibleRoads.get(r); // Return the road it should be on from the original intersection
				if(r.getNextRoad().getClass() != CityRoadIntersection.class) { // Iterate on a road if it's not an intersection
					possibleRoads.put(r.getNextRoad(), possibleRoads.get(r)); // Put the next road in the hashtable to be explored
					possibleRoads.remove(r); // Remove the road from the hashtable
				} else if(r.getNextRoad().getClass() == CityRoadIntersection.class) { // Iterate on a road if it's an intersection
					if(r.getNextRoad().equals(destinationRoad)) // Check if this intersection is the destination road
						return possibleRoads.get(r); // Return the road it should be on from the original intersection
					for(CityRoad intersectionRoads : ((CityRoadIntersection)r.getNextRoad()).getNextRoads()) { // Iterate through all next roads of this intersection
						possibleRoads.put(intersectionRoads, possibleRoads.get(r)); // put next roads into the hashtable
					}
					possibleRoads.remove(r); // remove the road from the hashtable
				}
			}
		}

		return null;
	}
}
