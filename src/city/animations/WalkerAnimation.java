package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import java.util.Stack;

import city.animations.interfaces.AnimatedWalker;
import city.bases.Animation;
import city.bases.interfaces.BuildingInterface;
import city.gui.CityRoad;
import city.gui.CitySidewalk;
import city.gui.CitySidewalkLayout;
import city.roles.interfaces.Walker;

public class WalkerAnimation extends Animation implements AnimatedWalker {

	protected int xPos, yPos;
	protected int xDestination, yDestination;

	protected Walker walker;

	protected CitySidewalk startingSidewalk = null;
	protected CitySidewalk endSidewalk = null;
	protected CitySidewalk currentSidewalk = null;
	protected CitySidewalkLayout sidewalks = null;

	protected boolean atDestinationRoad = false;
	protected boolean atDestination = false;

	protected Stack<CitySidewalk>sidewalkPath;
	
	public WalkerAnimation(Walker walker, BuildingInterface startingBuilding, CitySidewalkLayout sidewalks) {
		this.walker = walker;
		xDestination = xPos = startingBuilding.getCityViewBuilding().getX();
		yDestination = yPos = startingBuilding.getCityViewBuilding().getY();
		startingSidewalk = sidewalks.getClosestSidewalk(xPos, yPos);
		this.sidewalks = sidewalks;
	}

	@Override
	public void updatePosition() {
		// Getting on the first road
		if(startingSidewalk != null && !(xPos == startingSidewalk.getX() && yPos == startingSidewalk.getY())) {
			if(startingSidewalk.setCurrentOccupant(this) == false) {
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
		if(atDestinationRoad == false  && currentSidewalk != null) {
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
			} else if(currentSidewalk == endSidewalk) {
				atDestinationRoad = true;
				while(!sidewalkPath.isEmpty())
					sidewalkPath.pop();
				currentSidewalk.setCurrentOccupant(null);
				currentSidewalk = null;
			}
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
			if(walker != null) {
				walker.msgImAtDestination();
			}
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
