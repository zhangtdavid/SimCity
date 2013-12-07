package utilities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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

	private boolean atDestinationRoad = false;
	private boolean atDestination = false;
	
	private Stack<CitySidewalk>sidewalkPath;

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
			if(xPos < currentSidewalk.getX())
				xPos++;
			else if(xPos > currentSidewalk.getX())
				xPos--;
			else if(yPos < currentSidewalk.getY())
				yPos++;
			else if(yPos > currentSidewalk.getY())
				yPos--;
			else if(!sidewalkPath.isEmpty())
				currentSidewalk = sidewalkPath.pop();
			else if(currentSidewalk == endSidewalk)
				atDestinationRoad = true;
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
		sidewalkPath = sidewalks.getBestPath(startingSidewalk, endSidewalk);
		print("Going to destination " + destination.getName());
	}

	@Override
	public void goToBusStop(BusStop b) {
		// TODO Auto-generated method stub

	}

	public void print(String msg) {
		AlertLog.getInstance().logMessage(AlertTag.BANK, "PersonAnimationTest", msg);
	}
}
