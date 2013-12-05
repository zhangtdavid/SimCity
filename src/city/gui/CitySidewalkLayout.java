package city.gui;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.List;

public class CitySidewalkLayout {
	CitySidewalk[][] sidewalkGrid;
	MainFrame mainFrame; 
	
	public CitySidewalkLayout(MainFrame mf, int width, int height, int xOrigin, int yOrigin, double sidewalkSize, Color sidewalkColor, List<Rectangle> nonSidewalkArea) {
		mainFrame = mf;
		sidewalkGrid = new CitySidewalk[height][width];
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				sidewalkGrid[i][j] = new CitySidewalk((int) (xOrigin + j * sidewalkSize), (int) (yOrigin + i * sidewalkSize), sidewalkSize, 1, sidewalkColor);
				mainFrame.cityView.addMoving(sidewalkGrid[i][j]);
				for(int k = 0; k < nonSidewalkArea.size(); k++) { // Remove if in the nonSidewalkArea
					Rectangle currentNonSidewalkArea = nonSidewalkArea.get(k);
					if((j >= currentNonSidewalkArea.getX() && j < (currentNonSidewalkArea.getX() + currentNonSidewalkArea.getWidth())) &&
							(i >= currentNonSidewalkArea.getY() && i < (currentNonSidewalkArea.getY() + currentNonSidewalkArea.getHeight()))) {
						System.out.println(j + " " + i);
						mainFrame.cityView.movings.remove(sidewalkGrid[i][j]);
						sidewalkGrid[i][j] = null;
					}
				}
			}
		}
	}
}
