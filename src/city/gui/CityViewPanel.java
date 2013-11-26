package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import trace.AlertLog;
import trace.AlertTag;

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
		for(int i = 0; i < Math.min(CITY_WIDTH, CITY_HEIGHT); i += (Math.min(CITY_WIDTH, CITY_HEIGHT) * 3/ 10)) {
			for(int j = CITY_WIDTH / 10; j < CITY_WIDTH; j+= CITY_WIDTH * 3 / 10) {
				CityRoad road = new CityRoad(j, i, CITY_WIDTH / 5, CITY_WIDTH / 10, 1, 0, true, Color.black, Color.gray);
				statics.add(road);
				road.setPosition(new Point(j, i));
			}
			for(int j = CITY_WIDTH / 10; j < CITY_HEIGHT; j+= CITY_HEIGHT * 3 / 10) {
				CityRoad road = new CityRoad(i, j, CITY_HEIGHT/ 10, CITY_HEIGHT / 5, 0, 1, false, Color.black, Color.gray);
				statics.add(road);
				road.setPosition(new Point(i, j));
			}
		}
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
			mainframe.buildingView.addView(temp.building, temp.ID);
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
		case RESTAURANTZHANG: temp = new CityViewRestaurant(-100, -100, "Restaurant " + (statics.size()), Color.magenta, new RestaurantZhangPanel(Color.magenta, new Dimension(CITY_WIDTH, CITY_HEIGHT))); break;
		case RESTAURANTCHOI: temp = new CityViewRestaurant(-100, -100, "Restaurant " + (statics.size()), Color.cyan, new RestaurantChoiPanel(Color.cyan, new Dimension(CITY_WIDTH, CITY_HEIGHT))); break;
		case RESTAURANTJP: temp = new CityViewRestaurant(-100, -100, "Restaurant " + (statics.size()), Color.orange, new RestaurantJPPanel(Color.orange, new Dimension(CITY_WIDTH, CITY_HEIGHT))); break;
		case RESTAURANTTIMMS: temp = new CityViewRestaurant(-100, -100, "Restaurant " + (statics.size()), Color.yellow, new RestaurantTimmsPanel(Color.yellow, new Dimension(CITY_WIDTH, CITY_HEIGHT))); break;
		case RESTAURANTCHUNG: temp = new CityViewRestaurant(-100, -100, "Restaurant " + (statics.size()), Color.red, new RestaurantChungPanel(Color.red, new Dimension(500, 500))); break;
		case BANK: temp = new CityViewBank(-100, -100, "Bank " + (statics.size()), Color.green, new BankPanel(mainframe, Color.green, new Dimension(500, 500))); break;
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
