package city.gui;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.List;

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
	
//	public List<CitySidewalk> getAllConnectedSidewalks(CitySidewalk parentSidewalk) {
//		List<CitySidewalk>
//		for(int i = 0; i < height; i++) {
//			for(int j = 0; j < width; j++) {
//				if(sidewalkGrid[i][j] == parentSidewalk) {
//					if(i - 1 >= 0)
//						
//				}
//			}
//		}
//		return null;
//	}
	
	public CitySidewalk getSidewalkClosestTo(CitySidewalk targetSidewalk, List<CitySidewalk> potentialSidewalks) {
		double closestDistance = 10000000;
		CitySidewalk closestSidewalk = potentialSidewalks.get(0);
		for(CitySidewalk currentSidewalk : potentialSidewalks) {
			if(currentSidewalk == null)
				continue;
			System.out.println(potentialSidewalks.indexOf(currentSidewalk));
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
}
