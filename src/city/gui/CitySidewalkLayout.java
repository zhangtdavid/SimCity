package city.gui;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import utilities.CitySidewalkAStar;
import utilities.TrafficControl;
import city.bases.interfaces.AnimationInterface;

public class CitySidewalkLayout {

	// Data

	private CitySidewalk[][] sidewalkGrid;
	private MainFrame mainFrame;
	private int width;
	private int height;
	private double sidewalkSize;
	private TrafficControl roads;

	// Constructor

	public CitySidewalkLayout(MainFrame mf, int width, int height, int xOrigin, int yOrigin, double sidewalkSize, Color sidewalkColor, List<Rectangle> nonSidewalkArea, TrafficControl roads) {
		mainFrame = mf;
		this.width = width;
		this.height = height;
		this.roads = roads;
		this.sidewalkSize = sidewalkSize;
		sidewalkGrid = new CitySidewalk[height][width];
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				sidewalkGrid[i][j] = new CitySidewalk((int) (xOrigin + j * sidewalkSize), (int) (yOrigin + i * sidewalkSize), sidewalkSize, 1, sidewalkColor);
				mainFrame.cityView.addMoving(sidewalkGrid[i][j]);
				// This gigantic if statement checks if it is an intersection crosswalk
				CityRoad westRoadClose = roads.getRoadAt(sidewalkGrid[i][j].getX() - (int)sidewalkSize, sidewalkGrid[i][j].getY());
				CityRoad westRoadFar = roads.getRoadAt(sidewalkGrid[i][j].getX() - (int)(2 * sidewalkSize), sidewalkGrid[i][j].getY());
				CityRoad eastRoadClose = roads.getRoadAt(sidewalkGrid[i][j].getX() + (int)sidewalkSize, sidewalkGrid[i][j].getY());
				CityRoad eastRoadFar = roads.getRoadAt(sidewalkGrid[i][j].getX() + (int)(2 *sidewalkSize), sidewalkGrid[i][j].getY());
				CityRoad northRoadClose = roads.getRoadAt(sidewalkGrid[i][j].getX(), sidewalkGrid[i][j].getY() - (int)sidewalkSize);
				CityRoad northRoadFar = roads.getRoadAt(sidewalkGrid[i][j].getX(), sidewalkGrid[i][j].getY()  - (int)(2 * sidewalkSize));
				CityRoad southRoadClose = roads.getRoadAt(sidewalkGrid[i][j].getX(), sidewalkGrid[i][j].getY() + (int)sidewalkSize);
				CityRoad southRoadFar = roads.getRoadAt(sidewalkGrid[i][j].getX(), sidewalkGrid[i][j].getY()  + (int)(2 * sidewalkSize));
				if((westRoadClose != null && westRoadFar != null) && (eastRoadClose != null && eastRoadFar != null)) {
					if((westRoadClose.isRedLight() || westRoadFar.isRedLight() || westRoadClose.getClass() == CityRoadIntersection.class || westRoadFar.getClass() == CityRoadIntersection.class) &&
							(eastRoadClose.isRedLight() || eastRoadFar.isRedLight() || eastRoadClose.getClass() == CityRoadIntersection.class || eastRoadFar.getClass() == CityRoadIntersection.class)) {
						sidewalkGrid[i][j].setImageToRender(null);
					}
				}
				if((northRoadClose != null && northRoadFar != null) && (southRoadClose != null && southRoadFar != null)) {
					if((northRoadClose.isRedLight() || northRoadFar.isRedLight() || northRoadClose.getClass() == CityRoadIntersection.class || northRoadFar.getClass() == CityRoadIntersection.class) &&
							(southRoadClose.isRedLight() || southRoadFar.isRedLight() || southRoadClose.getClass() == CityRoadIntersection.class || southRoadFar.getClass() == CityRoadIntersection.class)) {
						sidewalkGrid[i][j].setImageToRender(null);
					}
				}
				for(int k = 0; k < nonSidewalkArea.size(); k++) { // Remove if in the nonSidewalkArea
					Rectangle currentNonSidewalkArea = nonSidewalkArea.get(k);
					if((j >= currentNonSidewalkArea.getX() && j < (currentNonSidewalkArea.getX() + currentNonSidewalkArea.getWidth())) &&
							(i >= currentNonSidewalkArea.getY() && i < (currentNonSidewalkArea.getY() + currentNonSidewalkArea.getHeight()))) {
						mainFrame.cityView.movings.remove(sidewalkGrid[i][j]);
						sidewalkGrid[i][j] = null;
					}
				}
			}
		}
		this.setRoads(roads);
	}

	// Getters

	public double getSidewalkSize() {
		return sidewalkSize;
	}

	public CitySidewalk getClosestSidewalk(int x, int y) {
		double closestDistance = 10000000;
		CitySidewalk closestSidewalk = null;
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				CitySidewalk currentSidewalk = sidewalkGrid[i][j];
				if(currentSidewalk == null)
					continue;
				double distance = Math.sqrt((double)(Math.pow(currentSidewalk.getX() - x, 2) + Math.pow(currentSidewalk.getY() - y, 2)));
				if( distance < closestDistance) {
					closestDistance = distance;
					closestSidewalk = currentSidewalk;
				}
			}
		}
		return closestSidewalk;
	}

	public boolean isCarAt(int x, int y) {
		if(roads == null)
			return false;
		List<AnimationInterface> allVehicles = roads.getAllVehicles();
		CitySidewalk currentSidewalk = getClosestSidewalk(x, y);
		Rectangle sidewalkRect = new Rectangle(currentSidewalk.getX(), currentSidewalk.getY(), (int)sidewalkSize, (int)sidewalkSize);
		for(AnimationInterface a : allVehicles) {
			Rectangle vehicleRect = new Rectangle(a.getXPos(), a.getYPos(), (int)(sidewalkSize * 2), (int)(sidewalkSize * 2));
			if(vehicleRect.intersects(sidewalkRect)) {
				return true;
			}
		}
		return false;
	}

	public List<AnimationInterface> getAllWalkers() {
		List<AnimationInterface> listToReturn = new ArrayList<AnimationInterface>();
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if(sidewalkGrid[i][j] == null)
					continue;
				if(sidewalkGrid[i][j].getCurrentOccupant() != null)
					listToReturn.add(sidewalkGrid[i][j].getCurrentOccupant());
			}
		}
		return listToReturn;
	}

	public List<CityRoad> getBorderingRoads(CitySidewalk parentSidewalk) {
		if(roads == null)
			return null;
		List<CityRoad> listToReturn = new ArrayList<CityRoad>(4);
		listToReturn.add(roads.getRoadAt(parentSidewalk.getX(), parentSidewalk.getY() + ((int)(parentSidewalk.size + 1) * 2)));
		listToReturn.add(roads.getRoadAt(parentSidewalk.getX() + ((int)(parentSidewalk.size + 1) * 2), parentSidewalk.getY()));
		listToReturn.add(roads.getRoadAt(parentSidewalk.getX(), parentSidewalk.getY() - ((int)(parentSidewalk.size) * 2 + 1)));
		listToReturn.add(roads.getRoadAt(parentSidewalk.getX() - ((int)(parentSidewalk.size) * 2 + 1), parentSidewalk.getY()));
		return listToReturn;
	}

	public boolean isAnIntersection(CitySidewalk parentSidewalk) {
		List<CityRoad> borderingRoads = getBorderingRoads(parentSidewalk);
		boolean hasIntersection = false;
		boolean hasStoplight = false;
		CityRoad correspondingStoplight = null;
		for(CityRoad r : borderingRoads) {
			if(r == null)
				continue;
			if(r.isRedLight()) {
				hasStoplight = true;
				correspondingStoplight = r;
			}
			if(r.getClass() == CityRoadIntersection.class)
				hasIntersection = true;
		}
		if(hasIntersection && hasStoplight) {
			parentSidewalk.sidewalkColor = Color.magenta;
			parentSidewalk.setCrosswalk(true);
			parentSidewalk.setCorrespondingStoplight(correspondingStoplight);
			correspondingStoplight.addIntersectionSidewalk(parentSidewalk);
			return true;
		}
		return false;
	}

	public CitySidewalk getSidewalkWest(int x, int y) {
		CitySidewalk currentSidewalk = getClosestSidewalk(x, y);
		if(currentSidewalk == null) {
			return null;
		} else {
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					if(sidewalkGrid[i][j] == currentSidewalk) {
						if(j <= 0)
							return null;
						else
							return sidewalkGrid[i][j - 1];
					}
				}
			}
		}
		return null;
	}

	public CitySidewalk getSidewalkWest(CitySidewalk parentSidewalk) {
		CitySidewalk currentSidewalk = parentSidewalk;
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if(sidewalkGrid[i][j] == currentSidewalk) {
					if(j <= 0)
						return null;
					else
						return sidewalkGrid[i][j - 1];
				}
			}
		}
		return null;
	}

	public CitySidewalk getSidewalkEast(int x, int y) {
		CitySidewalk currentSidewalk = getClosestSidewalk(x, y);
		if(currentSidewalk == null) {
			return null;
		} else {
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					if(sidewalkGrid[i][j] == currentSidewalk) {
						if(j >= width - 1)
							return null;
						else
							return sidewalkGrid[i][j + 1];
					}
				}
			}
		}
		return null;
	}

	public CitySidewalk getSidewalkEast(CitySidewalk parentSidewalk) {
		CitySidewalk currentSidewalk = parentSidewalk;
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if(sidewalkGrid[i][j] == currentSidewalk) {
					if(j >= width - 1)
						return null;
					else
						return sidewalkGrid[i][j + 1];
				}
			}
		}
		return null;
	}

	public CitySidewalk getSidewalkNorth(int x, int y) {
		CitySidewalk currentSidewalk = getClosestSidewalk(x, y);
		if(currentSidewalk == null) {
			return null;
		} else {
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					if(sidewalkGrid[i][j] == currentSidewalk) {
						if(i <= 0)
							return null;
						else
							return sidewalkGrid[i - 1][j];
					}
				}
			}
		}
		return null;
	}

	public CitySidewalk getSidewalkNorth(CitySidewalk parentSidewalk) {
		CitySidewalk currentSidewalk = parentSidewalk;
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if(sidewalkGrid[i][j] == currentSidewalk) {
					if(i <= 0)
						return null;
					else
						return sidewalkGrid[i - 1][j];
				}
			}
		}
		return null;
	}

	public CitySidewalk getSidewalkSouth(int x, int y) {
		CitySidewalk currentSidewalk = getClosestSidewalk(x, y);
		if(currentSidewalk == null) {
			return null;
		} else {
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					if(sidewalkGrid[i][j] == currentSidewalk) {
						if(i >= height - 1)
							return null;
						else
							return sidewalkGrid[i + 1][j];
					}
				}
			}
		}
		return null;
	}

	public CitySidewalk getSidewalkSouth(CitySidewalk parentSidewalk) {
		CitySidewalk currentSidewalk = parentSidewalk;
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if(sidewalkGrid[i][j] == currentSidewalk) {
					if(i >= height - 1)
						return null;
					else
						return sidewalkGrid[i + 1][j];
				}
			}
		}
		return null;
	}

	public CitySidewalk getSidewalkClosestTo(CitySidewalk targetSidewalk, List<CitySidewalk> potentialSidewalks) {
		double closestDistance = 10000000;
		CitySidewalk closestSidewalk = potentialSidewalks.get(0);
		for(CitySidewalk currentSidewalk : potentialSidewalks) {
			if(currentSidewalk == null)
				continue;
			double distance = Math.sqrt((double)(Math.pow(targetSidewalk.getX() - currentSidewalk.getX(), 2) + Math.pow(targetSidewalk.getY() - currentSidewalk.getY(), 2)));
			if( distance < closestDistance) {
				closestDistance = distance;
				closestSidewalk = currentSidewalk;
			}
		}
		return closestSidewalk;
	}

	// Setters

	public void setRoads(TrafficControl newRoads) {
		roads = newRoads;
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				if(sidewalkGrid[i][j] != null)
					isAnIntersection(sidewalkGrid[i][j]);
			}
		}
	}

	public boolean isWalkerAt(int x, int y) {
		if(getClosestSidewalk(x, y).getCurrentOccupant() == null)
			return false;
		else
			return true;
	}

	// A Star
	public Stack<CitySidewalk> getBestPath(CitySidewalk startingSidewalk, CitySidewalk endingSidewalk) {
		Stack<CitySidewalk> listToReturn = new Stack<CitySidewalk>();
		PriorityQueue<CitySidewalkAStar> openList = new PriorityQueue<CitySidewalkAStar>(1, CitySidewalkAStar.CitySidewalkAStarNameComparator);
		HashSet<CitySidewalk> closedSet = new HashSet<CitySidewalk>();

		openList.add(new CitySidewalkAStar(null, startingSidewalk, endingSidewalk, 0));

		while(!openList.isEmpty()) {
			CitySidewalkAStar currentAStar = openList.remove();
			closedSet.add(currentAStar.getSidewalk());
			// Found the solution
			if(currentAStar.getSidewalk() == endingSidewalk) {
				CitySidewalkAStar iterator = currentAStar; // Iterator of previous sidewalks
				do {
					listToReturn.add(iterator.getSidewalk());
					iterator = iterator.getPreviousAStarSidewalk();
				} while(iterator != null);
				return listToReturn;
			}
			// Explore next paths
			List<CitySidewalkAStar> possibleAStars = new ArrayList<CitySidewalkAStar>();
			for(int i = 0; i < 4; i++) {
				CitySidewalk possibleSidewalk;
				switch(i) {
				case 0:
					possibleSidewalk = getSidewalkNorth(currentAStar.getSidewalk());
					break;
				case 1:
					possibleSidewalk = getSidewalkEast(currentAStar.getSidewalk());
					break;
				case 2:
					possibleSidewalk = getSidewalkSouth(currentAStar.getSidewalk());
					break;
				case 3:
					possibleSidewalk = getSidewalkWest(currentAStar.getSidewalk());
					break;
				default:
					possibleSidewalk = null;
					break;
				}
				if(possibleSidewalk != null) {
					possibleAStars.add(new CitySidewalkAStar(currentAStar, possibleSidewalk, endingSidewalk, currentAStar.getNumMovesFromStart() + 1));
				}
			}
			for(CitySidewalkAStar i : possibleAStars) {
				if(!closedSet.contains(i.getSidewalk())) {
					openList.add(i);
				}
			}
		}
		System.out.println("Could not find path");
		return null;
	}


}
