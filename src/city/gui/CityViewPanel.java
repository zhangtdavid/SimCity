package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import city.gui.buildings.RestaurantChoiPanel;
import city.gui.buildings.RestaurantChungPanel;
import city.gui.buildings.RestaurantJPPanel;
import city.gui.buildings.RestaurantTimmsPanel;
import city.gui.buildings.RestaurantZhangPanel;
import city.gui.views.CityViewBuilding;
import city.gui.views.CityViewRestaurant;
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
				if (c.getRectangle().intersects(temp.getRectangle())) {
					AlertLog.getInstance().logError(AlertTag.GENERAL_CITY, this.name, "Can't add building, location obstructed!");
					return;
				}
			}
			AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, this.name, "Building successfully added");
			addingObject = false;
			mainframe.buildingView.addView(temp.getBuilding(), temp.getID());
			temp = null;
		}
		for (CityViewBuilding c: statics) {
			if (c.contains(arg0.getX(), arg0.getY())) {
				mainframe.buildingView.setView(c.getID());
				AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, this.name, "Building Selected: " + c.getID());
			}
		}
	}
	
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	public void addObject(CityViewBuilding.BUILDINGTYPE type) {
		if (addingObject)
			return;
		addingObject = true;
		switch (type) {
		case RESTAURANTZHANG: temp = new CityViewRestaurant(-100, -100, "Restaurant " + (statics.size()), Color.magenta, new RestaurantZhangPanel(Color.magenta, new Dimension(CITY_WIDTH, CITY_HEIGHT))); break;
		case RESTAURANTCHOI: temp = new CityViewRestaurant(-100, -100, "Restaurant " + (statics.size()), Color.cyan, new RestaurantChoiPanel(Color.cyan, new Dimension(CITY_WIDTH, CITY_HEIGHT))); break;
		case RESTAURANTJP: temp = new CityViewRestaurant(-100, -100, "Restaurant " + (statics.size()), Color.orange, new RestaurantJPPanel(Color.orange, new Dimension(CITY_WIDTH, CITY_HEIGHT))); break;
		case RESTAURANTTIMMS: temp = new CityViewRestaurant(-100, -100, "Restaurant " + (statics.size()), Color.yellow, new RestaurantTimmsPanel(Color.yellow, new Dimension(CITY_WIDTH, CITY_HEIGHT))); break;
		case RESTAURANTCHUNG: temp = new CityViewRestaurant(-100, -100, "Restaurant " + (statics.size()), Color.red, new RestaurantChungPanel(Color.red, new Dimension(500, 500))); break;
		// TODO BankBranch c0e51d580a4
		// case BANK: temp = new CityViewBank(-100, -100, "Bank " + (statics.size()), Color.green, new BankPanel(mainframe, Color.green, new Dimension(500, 500))); break;
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
