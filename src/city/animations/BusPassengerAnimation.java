package city.animations;

import java.util.Random;

import city.animations.interfaces.AnimatedBusPassenger;
import city.bases.interfaces.BuildingInterface;
import city.gui.CityRoad;
import city.gui.CitySidewalk;
import city.gui.CitySidewalkLayout;
import city.roles.interfaces.BusPassenger;

public class BusPassengerAnimation extends WalkerAnimation implements AnimatedBusPassenger {

	private BusPassenger busPassenger;

	public BusPassengerAnimation(BusPassenger busPassenger, BuildingInterface startingBuilding, CitySidewalkLayout sidewalks) {
		super(null, startingBuilding, sidewalks);
		this.busPassenger = busPassenger;
	}

	@Override
	public void updatePosition() {
		// Getting on the first road
		if(startingSidewalk != null && !(xPos == startingSidewalk.getX() && yPos == startingSidewalk.getY())) {
			if(startingSidewalk.setCurrentOccupant(this) == false) {
				return;
			}
			if (xPos < startingSidewalk.getX()) {
				xPos++;
				imageToRender = cityViewWalkerEastImage;
			}
			else if (xPos > startingSidewalk.getX()) {
				xPos--;
				imageToRender = cityViewWalkerWestImage;
			}

			if (yPos < startingSidewalk.getY()) {
				yPos++;
				imageToRender = cityViewWalkerSouthImage;
			}
			else if (yPos > startingSidewalk.getY()) {
				yPos--;
				imageToRender = cityViewWalkerNorthImage;
			}
			return;
		}
		// Traveling along sidewalks
		if(atDestinationRoad == false && currentSidewalk != null) {
			if(startingSidewalk != null) {
				currentSidewalk = startingSidewalk;
				startingSidewalk = null;
			}
			if(xPos < currentSidewalk.getX()) {
				xPos++;
				imageToRender = cityViewWalkerEastImage;
			} else if(xPos > currentSidewalk.getX()) {
				xPos--;
				imageToRender = cityViewWalkerWestImage;
			} else if(yPos < currentSidewalk.getY()) {
				yPos++;
				imageToRender = cityViewWalkerSouthImage;
			} else if(yPos > currentSidewalk.getY()) {
				yPos--;
				imageToRender = cityViewWalkerNorthImage;
			}
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
			if (xPos < xDestination) {
				xPos++;
				imageToRender = cityViewWalkerEastImage;
			} else if (xPos > xDestination) {
				xPos--;
				imageToRender = cityViewWalkerWestImage;
			}
			
			if (yPos < yDestination) {
				yPos++;
				imageToRender = cityViewWalkerSouthImage;
			} else if (yPos > yDestination) {
				yPos--;
				imageToRender = cityViewWalkerNorthImage;
			}
		}
		//
		if(xPos == xDestination && yPos == yDestination && atDestination == false) {
			atDestination = true;
			atDestinationRoad = false;
			if(busPassenger != null) {
				this.setVisible(false);
				busPassenger.msgImAtDestination();
			}
		}
	}

	@Override
	public void goToBus() {
		startingSidewalk = sidewalks.getClosestSidewalk(busPassenger.getBusStopToWaitAt().getCityViewBuilding().getX(),
				busPassenger.getBusStopToWaitAt().getCityViewBuilding().getY());
		startingSidewalk.setCurrentOccupant(this);
		currentSidewalk = startingSidewalk;
		this.xDestination = busPassenger.getBus().getAnimation().getXPos();
		this.yDestination = busPassenger.getBus().getAnimation().getYPos();
		if(xDestination > busPassenger.getBusStopToWaitAt().getCityViewBuilding().getX() + 2)
			xDestination -= (int)(sidewalks.getSidewalkSize());
		else if(xDestination < busPassenger.getBusStopToWaitAt().getCityViewBuilding().getX() - 2) {
			xDestination += 25;
		}
		if(yDestination > busPassenger.getBusStopToWaitAt().getCityViewBuilding().getY() + 2)
			yDestination -= (int)(sidewalks.getSidewalkSize());
		else if(yDestination < busPassenger.getBusStopToWaitAt().getCityViewBuilding().getY() - 2) {
			yDestination += 25;
		}
		endSidewalk = sidewalks.getClosestSidewalk(xDestination, yDestination);
		atDestination = false;
		atDestinationRoad = false;
		sidewalkPath = sidewalks.getBestPath(startingSidewalk, endSidewalk);
		this.setVisible(true);
	}

	@Override
	public void getOffBus() {
		int startingX = xPos = busPassenger.getBus().getAnimation().getXPos();
		int startingY = yPos = busPassenger.getBus().getAnimation().getYPos();
		if(startingX > busPassenger.getBusStopDestination().getCityViewBuilding().getX() + 2)
			startingX -= (int)(sidewalks.getSidewalkSize());
		else if(startingX < busPassenger.getBusStopDestination().getCityViewBuilding().getX() - 2)
			startingX += 25;
		if(startingY > busPassenger.getBusStopDestination().getCityViewBuilding().getY() + 2)
			startingY -= (int)(sidewalks.getSidewalkSize());
		else if(startingY <busPassenger.getBusStopDestination().getCityViewBuilding().getY() - 2)
			startingY += 25;
		startingSidewalk = sidewalks.getClosestSidewalk(startingX, startingY);
		startingSidewalk.setCurrentOccupant(this);
		currentSidewalk = startingSidewalk;
		this.xDestination = busPassenger.getBusStopDestination().getCityViewBuilding().getX();
		this.yDestination = busPassenger.getBusStopDestination().getCityViewBuilding().getY();
		endSidewalk = sidewalks.getClosestSidewalk(xDestination, yDestination);
		atDestination = false;
		atDestinationRoad = false;
		sidewalkPath = sidewalks.getBestPath(startingSidewalk, endSidewalk);
		this.setVisible(true);
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
