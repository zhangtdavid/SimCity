package city.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import trace.AlertLog;
import trace.AlertTag;
import city.Application;
import city.Application.BUILDING;
import city.Application.CityMap;
import city.animations.interfaces.AnimatedCar;
import city.bases.interfaces.AnimationInterface;
import city.bases.interfaces.BuildingInterface;
import city.buildings.AptBuilding;
import city.buildings.BankBuilding;
import city.buildings.HouseBuilding;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantChoiBuilding;
import city.buildings.RestaurantChungBuilding;
import city.buildings.RestaurantJPBuilding;
import city.buildings.RestaurantTimmsBuilding;
import city.buildings.RestaurantZhangBuilding;
import city.gui.exteriors.CityViewApt;
import city.gui.exteriors.CityViewBank;
import city.gui.exteriors.CityViewBuilding;
import city.gui.exteriors.CityViewHouse;
import city.gui.exteriors.CityViewMarket;
import city.gui.exteriors.CityViewRestaurantChoi;
import city.gui.exteriors.CityViewRestaurantChung;
import city.gui.exteriors.CityViewRestaurantJP;
import city.gui.exteriors.CityViewRestaurantTimms;
import city.gui.exteriors.CityViewRestaurantZhang;
import city.gui.interiors.AptPanel;
import city.gui.interiors.BankPanel;
import city.gui.interiors.HousePanel;
import city.gui.interiors.MarketPanel;
import city.gui.interiors.RestaurantChoiPanel;
import city.gui.interiors.RestaurantChungPanel;
import city.gui.interiors.RestaurantJPPanel;
import city.gui.interiors.RestaurantTimmsPanel;
import city.gui.interiors.RestaurantZhangPanel;

public class CityViewPanel extends CityPanel implements MouseMotionListener {

	private static final long serialVersionUID = 3622803906501755529L;

	public static final int CITY_WIDTH = 500, CITY_HEIGHT = 500;
	public boolean addingObject = false;

	public static NewBuilding temp;

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
		temp = new NewBuilding(null, null);
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
				if (c.equals(temp.getCityViewBuilding()))
					continue;
				if (c.getRectangle().intersects(temp.getCityViewBuilding().getRectangle())) {
					AlertLog.getInstance().logError(AlertTag.GENERAL_CITY, this.name, "Can't add building, location obstructed!");
					return;
				}
			}
			for (CityViewBuilding c: movings) {
				if (c.equals(temp.getCityViewBuilding()))
					continue;
				if (c.getRectangle().intersects(temp.getCityViewBuilding().getRectangle())) {
					AlertLog.getInstance().logError(AlertTag.GENERAL_CITY, this.name, "Can't add building, location obstructed!");
					return;
				}
			}
			AlertLog.getInstance().logInfo(AlertTag.GENERAL_CITY, this.name, "Building successfully added");
			addingObject = false;
			createBuilding(temp.getCityViewBuilding().getBuilding(), temp.getCityViewBuilding(), temp.getBuilding());
			temp.setCityViewBuilding(null);
			temp.setBuilding(null);
		}
		for (CityViewBuilding c: statics) {
			if (c.contains(arg0.getX(), arg0.getY())) {
				mainframe.buildingView.setView(c.getID());
				AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, this.name, "Building Selected: " + c.getID());
			}
		}
		for (AnimationInterface a : animations) {
			if (AnimatedCar.class.isInstance(a) && a.getVisible()) {
				AnimatedCar c = (AnimatedCar) a;
				if (c.contains(arg0.getX(), arg0.getY())) {
					AlertLog.getInstance().logMessage(AlertTag.GENERAL_CITY, this.name, "Car Selected: " + c.getCar().getName());
					Application.getMainFrame().CP.editPersonTab.displayPerson(c.getCar().getOwner());
				}
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
		case APT:
			temp.setCityViewBuilding(new CityViewApt(-100, -100, "Apartment " + (statics.size()), Color.gray, new AptPanel(Color.gray)));
			temp.setBuilding(new AptBuilding("Apartment " + statics.size(), null,
					(AptPanel)(temp.getCityViewBuilding().getBuilding()), temp.getCityViewBuilding()));
			break;
		case HOUSE:
			temp.setCityViewBuilding(new CityViewHouse(-100, -100, "House " + (statics.size()), Color.darkGray, new HousePanel(Color.darkGray)));
			temp.setBuilding(new HouseBuilding("House " + statics.size(), null,
					(HousePanel)(temp.getCityViewBuilding().getBuilding()), temp.getCityViewBuilding()));
			break;
		case MARKET:
			temp.setCityViewBuilding(new CityViewMarket(-100, -100, "Market " + (statics.size()), Color.blue, new MarketPanel(Color.blue)));
			temp.setBuilding(new MarketBuilding("Market " + statics.size(),
					(MarketPanel)(temp.getCityViewBuilding().getBuilding()), temp.getCityViewBuilding()));
			break;
		case RESTAURANTZHANG:
			temp.setCityViewBuilding(new CityViewRestaurantZhang(-100, -100, "Restaurant " + (statics.size()), Color.magenta, new RestaurantZhangPanel(Color.magenta)));
			temp.setBuilding(new RestaurantZhangBuilding("RestaurantZhang " + statics.size(),
					(RestaurantZhangPanel)(temp.getCityViewBuilding().getBuilding()), temp.getCityViewBuilding()));
			break;
		case RESTAURANTCHOI:
			temp.setCityViewBuilding(new CityViewRestaurantChoi(-100, -100, "Restaurant " + (statics.size()), Color.cyan, new RestaurantChoiPanel(Color.cyan)));
			temp.setBuilding(new RestaurantChoiBuilding("RestaurantChoi " + statics.size(),
					(RestaurantChoiPanel)(temp.getCityViewBuilding().getBuilding()), temp.getCityViewBuilding()));
			break;
		case RESTAURANTJP:

			temp.setCityViewBuilding(new CityViewRestaurantJP(-100, -100, "Restaurant " + (statics.size()), Color.orange, new RestaurantJPPanel(Color.darkGray)));

			temp.setBuilding(new RestaurantJPBuilding("RestaurantJP " + statics.size(),
					(RestaurantJPPanel)(temp.getCityViewBuilding().getBuilding()), temp.getCityViewBuilding()));
			break;
		case RESTAURANTTIMMS:
			temp.setCityViewBuilding(new CityViewRestaurantTimms(-100, -100, "Restaurant " + (statics.size()), Color.yellow, new RestaurantTimmsPanel(Color.yellow)));
			temp.setBuilding(new RestaurantTimmsBuilding("RestaurantTimms " + statics.size(),
					(RestaurantTimmsPanel)(temp.getCityViewBuilding().getBuilding()), temp.getCityViewBuilding()));
			break;
		case RESTAURANTCHUNG:
			temp.setCityViewBuilding(new CityViewRestaurantChung(-100, -100, "Restaurant " + (statics.size()), Color.red, new RestaurantChungPanel(Color.red)));
			temp.setBuilding(new RestaurantChungBuilding("RestaurantChung " + statics.size(),
					(RestaurantChungPanel)(temp.getCityViewBuilding().getBuilding()), temp.getCityViewBuilding()));
			break;
		case BANK: 
			temp.setCityViewBuilding(new CityViewBank(-100, -100, "Bank " + (statics.size()), Color.green, new BankPanel(Color.green)));
			temp.setBuilding(new BankBuilding("Bank " + statics.size(),
					(BankPanel)(temp.getCityViewBuilding().getBuilding()), temp.getCityViewBuilding()));
			break;
		default: return;
		}
		addStatic(temp.getCityViewBuilding());
	}

	public void mouseDragged(MouseEvent arg0) {

	}

	public void mouseMoved(MouseEvent arg0) {
		if (addingObject) {
			int newX = (int)(arg0.getPoint().getX() - arg0.getPoint().getX() % 25);
			int newY = (int)(arg0.getPoint().getY() - arg0.getPoint().getY() % 25);

			if(newX < 25)
				newX = 25;
			else if(newX > 425)
				newX = 425;

			if(newY < 25)
				newY = 25;
			else if(newY > 425)
				newY = 425;

			if(newX == 25 && (newY == 25 || newY == 425))
				newX = 50;
			else if(newX == 425 && (newY == 25 || newY == 425))
				newX = 400;
			if(temp.getCityViewBuilding() != null)
				temp.getCityViewBuilding().setPosition(newX, newY);
		}
	}
	
	/*
	 *  Since the addObject function already sets the cityviewbuilding in the list of statics, I can't call application's setBuilding()
	 */
	private void createBuilding(BuildingCard panel, CityViewBuilding cityView, BuildingInterface building) {
		Application.getMainFrame().buildingView.addView(panel, cityView.getID());
		if(building.getClass().getName().contains("Restaurant")) {
			CityMap.addBuilding(BUILDING.restaurant, building);
		} else if(building.getClass().getName().contains("Bank")) {
			CityMap.addBuilding(BUILDING.bank, building);
		} else if(building.getClass().getName().contains("Market")) {
			CityMap.addBuilding(BUILDING.market, building);
		} else if(building.getClass().getName().contains("BusStop")) {
			CityMap.addBuilding(BUILDING.busStop, building);
		} else if(building.getClass().getName().contains("House")) {
			CityMap.addBuilding(BUILDING.house, building);
		} else if(building.getClass().getName().contains("Apt")) {
			CityMap.addBuilding(BUILDING.apartment, building);
		}
	}
	
	public CityViewBuilding getBuildingAt(int x, int y) {
		for (CityViewBuilding c: statics) {
			if(c.getX() == x && c.getY() == y)
				return c;
		}
		for (CityViewBuilding m: movings) {
			if(m.getX() == x && m.getY() == y)
				return m;
		}
		return null;
	}

	private class NewBuilding {
		private CityViewBuilding cityViewBuilding;
		private BuildingInterface building;
		NewBuilding(CityViewBuilding cityViewBuilding, BuildingInterface building) {
			this.cityViewBuilding = cityViewBuilding;
			this.building = building;
		}

		CityViewBuilding getCityViewBuilding() {
			return cityViewBuilding;
		}

		BuildingInterface getBuilding() {
			return building;
		}

		void setCityViewBuilding(CityViewBuilding newCityViewBuilding) {
			cityViewBuilding = newCityViewBuilding;
		}

		void setBuilding(BuildingInterface newBuilding) {
			building = newBuilding;
		}
	}
}
