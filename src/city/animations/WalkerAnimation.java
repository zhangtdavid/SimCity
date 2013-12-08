package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.Stack;

import trace.AlertLog;
import trace.AlertTag;
import city.animations.interfaces.AnimatedWalker;
import city.bases.Animation;
import city.bases.interfaces.BuildingInterface;
import city.gui.CityRoad;
import city.gui.CitySidewalk;
import city.gui.CitySidewalkLayout;

public class WalkerAnimation extends Animation implements AnimatedWalker {

	private int xPos, yPos;
	private int xDestination, yDestination;
	
	private CitySidewalk startingSidewalk = null;
	private CitySidewalk endSidewalk = null;
	private CitySidewalk currentSidewalk = null;
	private CitySidewalkLayout sidewalks = null;

	private boolean atDestinationRoad = false;
	private boolean atDestination = false;

	private Stack<CitySidewalk>sidewalkPath;
	
	public WalkerAnimation(BuildingInterface startingBuilding, CitySidewalkLayout sidewalks) {
		xDestination = xPos = startingBuilding.getCityViewBuilding().getX();
		yDestination = yPos = startingBuilding.getCityViewBuilding().getY();
		startingSidewalk = sidewalks.getClosestSidewalk(xPos, yPos);
		this.sidewalks = sidewalks;
	}

	@Override
	public void updatePosition() {
		// Getting on the first road
		if(startingSidewalk != null && !(xPos == startingSidewalk.getX() && yPos == startingSidewalk.getY())) {
			if(startingSidewalk.setCurrentOccupant(this) == false 
					) {
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
			else if(!sidewalkPath.isEmpty()) {
				if(sidewalkPath.peek().getCorrespondingStoplight() != null) {
					if((sidewalkPath.peek().getCorrespondingStoplight().getStopLightType() == CityRoad.STOPLIGHTTYPE.HORIZONTALOFF || 
							sidewalkPath.peek().getCorrespondingStoplight().getStopLightType() == CityRoad.STOPLIGHTTYPE.VERTICALOFF) &&
							!currentSidewalk.isCrosswalk()) {
						return;
					}
				}
				if(sidewalkPath.peek().setCurrentOccupant(this)) {
					currentSidewalk.setCurrentOccupant(null);
					currentSidewalk = sidewalkPath.pop();
				} else {
					CitySidewalk possibleSidewalk;
					sidewalkPath.push(currentSidewalk);
					for(int i = 0; i < 4; i++) {
						switch(new Random().nextInt(4)) {
						case 0:
							possibleSidewalk = sidewalks.getSidewalkNorth(currentSidewalk);
							break;
						case 1:
							possibleSidewalk = sidewalks.getSidewalkEast(currentSidewalk);
							break;
						case 2:
							possibleSidewalk = sidewalks.getSidewalkSouth(currentSidewalk);
							break;
						case 3:
							possibleSidewalk = sidewalks.getSidewalkWest(currentSidewalk);
							break;
						default:
							possibleSidewalk = null;
							break;
						}
						if(possibleSidewalk == null)
							continue;
						if(sidewalks.isCarAt(possibleSidewalk.getX(), possibleSidewalk.getY())) {
							sidewalkPath.pop();
							return;
						}
						if(possibleSidewalk.getCorrespondingStoplight() != null) {
							if((possibleSidewalk.getCorrespondingStoplight().getStopLightType() == CityRoad.STOPLIGHTTYPE.HORIZONTALOFF || 
									possibleSidewalk.getCorrespondingStoplight().getStopLightType() == CityRoad.STOPLIGHTTYPE.VERTICALOFF) &&
									!currentSidewalk.isCrosswalk()) {
								continue;
							}
						}
						if(possibleSidewalk.setCurrentOccupant(this)) {
							currentSidewalk.setCurrentOccupant(null);
							currentSidewalk = possibleSidewalk;
							break;
						} else {
							sidewalkPath.pop();
							return;
						}
					}
				}
			} else if(currentSidewalk == endSidewalk)
				atDestinationRoad = true;
		}
		// Finished walking to sidewalk, walk into building
		if(atDestinationRoad == true) {
			currentSidewalk.setCurrentOccupant(null);
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
//			this.goToDestination(Application.CityMap.findRandomBuilding(BUILDING.busStop));
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.blue);
		g.fillRect(xPos, yPos, (int)(sidewalks.getSidewalkSize()), (int)(sidewalks.getSidewalkSize()));
	}
	
	@Override
	public void goToDestination(BuildingInterface destination) {
		startingSidewalk = sidewalks.getClosestSidewalk(xPos, yPos);
		startingSidewalk.setCurrentOccupant(this);
		currentSidewalk = startingSidewalk;
		xDestination = destination.getCityViewBuilding().getX();
		yDestination = destination.getCityViewBuilding().getY();
		endSidewalk = sidewalks.getClosestSidewalk(destination.getCityViewBuilding().getX(), destination.getCityViewBuilding().getY());
		atDestination = false;
		atDestinationRoad = false;
		sidewalkPath = sidewalks.getBestPath(startingSidewalk, endSidewalk);
	}
	
	@Override
	public int getXPos() {
		return xPos;
	}

	@Override
	public int getYPos() {
		return yPos;
	}
}
