package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;

import javax.imageio.ImageIO;

import city.animations.interfaces.AnimatedWalker;
import city.bases.Animation;
import city.bases.interfaces.BuildingInterface;
import city.gui.CityRoad;
import city.gui.CitySidewalk;
import city.gui.CitySidewalkLayout;
import city.gui.exteriors.CityViewApt;
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
	
	protected static BufferedImage cityViewWalkerNorthImage = null;
	protected static BufferedImage cityViewWalkerEastImage = null;
	protected static BufferedImage cityViewWalkerSouthImage = null;
	protected static BufferedImage cityViewWalkerWestImage = null;
	protected BufferedImage imageToRender;
	
	public WalkerAnimation(Walker walker, BuildingInterface startingBuilding, CitySidewalkLayout sidewalks) {
		this.walker = walker;
		if(startingBuilding.getCityViewBuilding() != null) {
			xDestination = xPos = startingBuilding.getCityViewBuilding().getX();
			yDestination = yPos = startingBuilding.getCityViewBuilding().getY();
		}
		if(sidewalks != null)
		startingSidewalk = sidewalks.getClosestSidewalk(xPos, yPos);
		this.sidewalks = sidewalks;
		try {
			if(cityViewWalkerNorthImage == null || cityViewWalkerEastImage == null || cityViewWalkerSouthImage == null || cityViewWalkerWestImage == null) {
				cityViewWalkerNorthImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewWalkerNorthImage.png"));
				cityViewWalkerEastImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewWalkerEastImage.png"));
				cityViewWalkerSouthImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewWalkerSouthImage.png"));
				cityViewWalkerWestImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewWalkerWestImage.png"));
			}
			imageToRender = cityViewWalkerNorthImage;
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			} else if (xPos > startingSidewalk.getX()) {
				xPos--;
				imageToRender = cityViewWalkerWestImage;
			}
			if (yPos < startingSidewalk.getY()) {
				yPos++;
				imageToRender = cityViewWalkerSouthImage;
			} else if (yPos > startingSidewalk.getY()) {
				yPos--;
				imageToRender = cityViewWalkerNorthImage;
			}
			return;
		}
		// Traveling along sidewalks
		if(atDestinationRoad == false  && currentSidewalk != null) {
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
			this.setVisible(false);
			if(walker != null) {
				walker.msgImAtDestination();
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		if(isUgly) {
			g.setColor(Color.blue);
			g.fillRect(xPos, yPos, (int)(sidewalks.getSidewalkSize()), (int)(sidewalks.getSidewalkSize()));
		} else
			g.drawImage(imageToRender, xPos, yPos, null);
	}

	@Override
	public void goToDestination(BuildingInterface destination) {
		if(sidewalks != null) {
			startingSidewalk = sidewalks.getClosestSidewalk(xPos, yPos);
			startingSidewalk.setCurrentOccupant(this);
		}
		currentSidewalk = startingSidewalk;
		xDestination = destination.getCityViewBuilding().getX();
		yDestination = destination.getCityViewBuilding().getY();
		if(sidewalks != null)
			endSidewalk = sidewalks.getClosestSidewalk(destination.getCityViewBuilding().getX(), destination.getCityViewBuilding().getY());
		atDestination = false;
		atDestinationRoad = false;
		if(sidewalks != null)
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
