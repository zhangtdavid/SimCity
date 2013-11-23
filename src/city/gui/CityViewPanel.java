package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import city.trace.AlertLog;
import city.trace.AlertTag;

public class CityViewPanel extends CityPanel implements MouseMotionListener {

	private static final long serialVersionUID = 3622803906501755529L;
	
	public static final int CITY_WIDTH = 500, CITY_HEIGHT = 500;
	boolean addingObject = false;
	CityViewBuilding temp;
	
	String name = "City Panel";
	
	public CityViewPanel(MainFrame mf) {
		super(mf);
		this.setPreferredSize(new Dimension(CITY_WIDTH, CITY_HEIGHT));
		this.setMinimumSize(new Dimension(CITY_WIDTH, CITY_HEIGHT));
		this.setMaximumSize(new Dimension(CITY_WIDTH, CITY_HEIGHT));
		this.setVisible(true);
		background = new Color(128, 64, 0);
		this.addStatic(new CityViewRestaurant(30, 30));
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void mouseClicked(MouseEvent arg0) {
		
	}
	
	public void mouseEntered(MouseEvent arg0) {
		
	}
	
	public void mouseExited(MouseEvent arg0) {
		
	}
	
	public void mousePressed(MouseEvent arg0) {
		if (addingObject) {
			//make sure we aren't overlapping anything
			for (CityViewBuilding c: statics) {
				if (c.equals(temp))
					continue;
				if (c.rectangle.intersects(temp.rectangle)) {
					AlertLog.getInstance().logError(AlertTag.GENERAL_CITY, this.name, "Can't add building, location obstructed!");
					return;
				}
			}
			AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, this.name, "Building successfully added");
			addingObject = false;
			mainframe.buildingView.addView(new BuildingCard(mainframe, Color.pink), temp.ID);
			temp = null;
		}
		for (CityViewBuilding c: statics) {
			if (c.contains(arg0.getX(), arg0.getY())) {
				mainframe.buildingView.setView(c.ID);
				AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, this.name, "Building Selected: " + c.ID);
			}
		}
	}
	
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	public void addObject(CityViewBuilding.BuildingType type) {
		if (addingObject)
			return;
		addingObject = true;
		switch (type) {
		case RESTAURANT: temp = new CityViewRestaurant(-100, -100, "Restaurant " + (statics.size()-19)); break;
//		case ROAD: temp = new CityRoad(-100, RoadDirection.HORIZONTAL); break; //NOTE: DON'T MAKE NEW ROADS
		case BANK: temp = new CityViewBank(-100, -100, "Bank " + (statics.size()-19)); break;
		default: return;
		}
		addStatic(temp);
	}

	public void mouseDragged(MouseEvent arg0) {
		
	}

	public void mouseMoved(MouseEvent arg0) {
		if (addingObject) {
			temp.setPosition(arg0.getPoint());
		}
	}
	
}
