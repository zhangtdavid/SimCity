package utilities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import trace.AlertLog;
import trace.AlertTag;
import city.Application;
import city.Application.BUILDING;
import city.animations.interfaces.AnimatedPerson;
import city.bases.Animation;
import city.bases.interfaces.BuildingInterface;
import city.buildings.interfaces.BusStop;
import city.gui.CitySidewalk;
import city.gui.CitySidewalkLayout;

public class PersonAnimationTest extends Animation implements AnimatedPerson {

	private int xPos, yPos;
	private int xDestination, yDestination;

	private BuildingInterface currentBuilding = null;
	private BuildingInterface destinationBuilding = null;
	private CitySidewalk startingSidewalk = null;
	private CitySidewalk endSidewalk = null;
	private CitySidewalk currentSidewalk = null;
	private CitySidewalkLayout sidewalks = null;

	private static enum DIRECTIONOFTRAVEL{NORTH, SOUTH, EAST, WEST}; 
	private DIRECTIONOFTRAVEL directionOfTravel = DIRECTIONOFTRAVEL.NORTH;

	private boolean atDestinationRoad = false;
	private boolean atDestination = false;

	public PersonAnimationTest(BuildingInterface startingBuilding, CitySidewalkLayout sidewalks) {
		xDestination = xPos = startingBuilding.getCityViewBuilding().getX();
		yDestination = yPos = startingBuilding.getCityViewBuilding().getY();
		currentBuilding = startingBuilding;
		startingSidewalk = sidewalks.getClosestSidewalk(xPos, yPos);
		this.sidewalks = sidewalks;
		goToDestination(Application.CityMap.findRandomBuilding(BUILDING.busStop));
	}

	@Override
	public void updatePosition() {
		// Getting on the first road
		if(startingSidewalk != null && !(xPos == startingSidewalk.getX() && yPos == startingSidewalk.getY())) {
			if(startingSidewalk.setCurrentOccupant(this) == false && startingSidewalk.getCurrentOccupant() != this 
					&& sidewalks.isCarAt(startingSidewalk.getX(),  startingSidewalk.getY())) {
				return;
			}
			if (xPos < startingSidewalk.getX())
				xPos++;
			else if (xPos > startingSidewalk.getX())
				xPos--;

			if (yPos < startingSidewalk.getY())
				yPos++;
			else if (yPos > startingSidewalk.getY())
				yPos--;
			return;
		}
		// Traveling along sidewalks
		if(atDestinationRoad == false) {
			if(startingSidewalk != null) {
				currentSidewalk = startingSidewalk;
				startingSidewalk = null;
			}
			CitySidewalk nextSidewalk = null;
			switch(directionOfTravel) {
			case NORTH:
				System.out.println("North");
				List<CitySidewalk> potentialSidewalks = new ArrayList<CitySidewalk>();
				potentialSidewalks.add(sidewalks.getSidewalkNorth(currentSidewalk));
				potentialSidewalks.add(sidewalks.getSidewalkEast(currentSidewalk));
				potentialSidewalks.add(sidewalks.getSidewalkWest(currentSidewalk));
				nextSidewalk = sidewalks.getSidewalkClosestTo(endSidewalk, potentialSidewalks);
				switch(potentialSidewalks.indexOf(nextSidewalk)) {
				case 0:
					yPos--;
					break;
				case 1:
					directionOfTravel = DIRECTIONOFTRAVEL.EAST;
					break;
				case 2:
					directionOfTravel = DIRECTIONOFTRAVEL.WEST;
					break;
				}
				if(nextSidewalk.getX() == xPos && nextSidewalk.getY() >= yPos)
					currentSidewalk = nextSidewalk;
				break;
			case EAST:
				System.out.println("East");
				nextSidewalk = sidewalks.getSidewalkEast(currentSidewalk);
				if(nextSidewalk == null) { 
					if(endSidewalk.getY() > yPos) {
						nextSidewalk = sidewalks.getSidewalkSouth(currentSidewalk);
						directionOfTravel = DIRECTIONOFTRAVEL.SOUTH;
					}
					else if(endSidewalk.getY() < yPos) {
						nextSidewalk = sidewalks.getSidewalkNorth(currentSidewalk);
						directionOfTravel = DIRECTIONOFTRAVEL.NORTH;
					}
					else
						atDestinationRoad = true;
				} else {
					xPos++;
				}
				if(nextSidewalk.getX() <= xPos && nextSidewalk.getY() == yPos)
					currentSidewalk = nextSidewalk;
				break;
			case SOUTH:
				nextSidewalk = sidewalks.getSidewalkSouth(currentSidewalk);
				if(nextSidewalk == null) { 
					if(endSidewalk.getX() > xPos) {
						nextSidewalk = sidewalks.getSidewalkEast(currentSidewalk);
						directionOfTravel = DIRECTIONOFTRAVEL.EAST;
					}
					else if(endSidewalk.getX() < xPos) {
						nextSidewalk = sidewalks.getSidewalkWest(currentSidewalk);
						directionOfTravel = DIRECTIONOFTRAVEL.WEST;
					}
					else
						atDestinationRoad = true;
				} else {
					yPos++;
				}

				if(nextSidewalk.getX() == xPos && nextSidewalk.getY() <= yPos)
					currentSidewalk = nextSidewalk;
				break;
			case WEST:
				nextSidewalk = sidewalks.getSidewalkWest(currentSidewalk);
				if(nextSidewalk == null) { 
					if(endSidewalk.getY() > yPos) {
						nextSidewalk = sidewalks.getSidewalkSouth(currentSidewalk);
						directionOfTravel = DIRECTIONOFTRAVEL.SOUTH;
					}
					else if(endSidewalk.getY() < yPos) {
						nextSidewalk = sidewalks.getSidewalkNorth(currentSidewalk);
						directionOfTravel = DIRECTIONOFTRAVEL.NORTH;
					}
					else
						atDestinationRoad = true;
				} else {
					xPos--;
				}
				if(nextSidewalk.getX() >= xPos && nextSidewalk.getY() == yPos)
					currentSidewalk = nextSidewalk;
				break;
			}
//			if(nextSidewalk.getX() == xPos && nextSidewalk.getY() == yPos)
//				currentSidewalk = nextSidewalk;
			if(currentSidewalk == endSidewalk)
				atDestinationRoad = true;
			System.out.println("Currentsidewalk: " + currentSidewalk.getX() + " " + currentSidewalk.getY());
			System.out.println("Nextsidewalk: " + nextSidewalk.getX() + " " + nextSidewalk.getY());
			System.out.println("Current Position:  " + xPos + " " + yPos);
		}
		// Finished walking to sidewalk, walk into building
		if(atDestinationRoad == true) {
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;

			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;
		}
		//
		if(xPos == xDestination && yPos == yDestination && atDestination == false) {
			atDestination = true;
			atDestinationRoad = false;
			goToDestination(Application.CityMap.findRandomBuilding(BUILDING.busStop));
		}
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.blue);
		g.fillRect(xPos, yPos, (int)(sidewalks.getSidewalkSize()), (int)(sidewalks.getSidewalkSize()));
	}

	public void goToDestination(BuildingInterface destination) {
		destinationBuilding = destination;
		startingSidewalk = sidewalks.getClosestSidewalk(xPos, yPos);
		startingSidewalk.setCurrentOccupant(this);
		currentSidewalk = startingSidewalk;
		xDestination = destination.getCityViewBuilding().getX();
		yDestination = destination.getCityViewBuilding().getY();
		endSidewalk = sidewalks.getClosestSidewalk(destination.getCityViewBuilding().getX(), destination.getCityViewBuilding().getY());
		atDestination = false;
		atDestinationRoad = false;
		print("Going to destination " + destination.getName());
	}

	@Override
	public void goToBusStop(BusStop b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void goToSleep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cookAndEatFood() {
		// TODO Auto-generated method stub

	}

	public void print(String msg) {
		AlertLog.getInstance().logMessage(AlertTag.BANK, "PersonAnimationTest", msg);
	}
}
