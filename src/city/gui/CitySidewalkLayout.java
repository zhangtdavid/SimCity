package city.gui;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.TreeSet;

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

	public CitySidewalkLayout(MainFrame mf, int width, int height, int xOrigin, int yOrigin, double sidewalkSize, Color sidewalkColor, List<Rectangle> nonSidewalkArea) {
		mainFrame = mf;
		this.width = width;
		this.height = height;
		this.sidewalkSize = sidewalkSize;
		sidewalkGrid = new CitySidewalk[height][width];
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				sidewalkGrid[i][j] = new CitySidewalk((int) (xOrigin + j * sidewalkSize), (int) (yOrigin + i * sidewalkSize), sidewalkSize, 1, sidewalkColor);
				mainFrame.cityView.addMoving(sidewalkGrid[i][j]);
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
		AnimationInterface currentVehicle = roads.getClosestRoad(x, y).getVehicle();
		Rectangle vehicleRect = new Rectangle(currentVehicle.getXPos(), currentVehicle.getYPos(), (int)(sidewalkSize * 2), (int)(sidewalkSize * 2));
		CitySidewalk currentSidewalk = getClosestSidewalk(x, y);
		Rectangle sidewalkRect = new Rectangle(currentSidewalk.getX(), currentSidewalk.getY(), (int)sidewalkSize, (int)sidewalkSize);
		if(vehicleRect.intersects(sidewalkRect))
			return true;
		else
			return false;
	}

	public boolean isAnIntersection(CitySidewalk parentSidewalk) {
		
		
		return false;
	}
	
//	public CityRoad getClosestRoad(int x, int y) {
//		double closestDistance = 10000000;
//		CityRoad closestRoad = null;
//		for(CityRoad r : roads) {
//			double distance = Math.sqrt((double)(Math.pow(r.getX() - x, 2) + Math.pow(r.getY() - y, 2)));
//			if( distance < closestDistance) {
//				closestDistance = distance;
//				closestRoad = r;
//			}
//		}
//		return closestRoad;
//	}
	
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
	}

	// A Star
	public Stack<CitySidewalk> getBestPath(CitySidewalk startingSidewalk, CitySidewalk endingSidewalk) {
		System.out.println("Finding best path");
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
		return null;
	}


}
