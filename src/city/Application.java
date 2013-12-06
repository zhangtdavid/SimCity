package city;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import utilities.DataModel;
import utilities.TrafficControl;
import city.agents.BusAgent;
import city.agents.CarAgent;
import city.agents.PersonAgent;
import city.agents.interfaces.Person;
import city.animations.BusAnimation;
import city.animations.CarAnimation;
import city.animations.HouseResidentAnimation;
import city.animations.RestaurantTimmsTableAnimation;
import city.bases.Building;
import city.bases.interfaces.BuildingInterface;
import city.buildings.ApartmentBuilding;
import city.buildings.BankBuilding;
import city.buildings.BusStopBuilding;
import city.buildings.HouseBuilding;
import city.buildings.RestaurantTimmsBuilding;
import city.buildings.RestaurantZhangBuilding;
import city.gui.BuildingCard;
import city.gui.CityRoad;
import city.gui.CityRoad.STOPLIGHTTYPE;
import city.gui.CityRoadIntersection;
import city.gui.CitySidewalkLayout;
import city.gui.MainFrame;
import city.gui.exteriors.CityViewBank;
import city.gui.exteriors.CityViewBuilding;
import city.gui.exteriors.CityViewBusStop;
import city.gui.exteriors.CityViewHouse;
import city.gui.exteriors.CityViewRestaurant;
import city.gui.interiors.AptPanel;
import city.gui.interiors.BankPanel;
import city.gui.interiors.BusStopPanel;
import city.gui.interiors.HousePanel;
import city.gui.interiors.RestaurantTimmsPanel;
import city.gui.interiors.RestaurantZhangPanel;
import city.roles.LandlordRole;
import city.roles.RestaurantChoiCashierRole;
import city.roles.RestaurantChoiCookRole;
import city.roles.RestaurantChoiHostRole;
import city.roles.RestaurantChoiWaiterQueueRole;
import city.roles.RestaurantChungCashierRole;
import city.roles.RestaurantChungCookRole;
import city.roles.RestaurantChungHostRole;
import city.roles.RestaurantChungWaiterMessageCookRole;
import city.roles.RestaurantJPCashierRole;
import city.roles.RestaurantJPCookRole;
import city.roles.RestaurantJPHostRole;
import city.roles.RestaurantJPWaiterRole;
import city.roles.RestaurantTimmsCashierRole;
import city.roles.RestaurantTimmsCookRole;
import city.roles.RestaurantTimmsHostRole;
import city.roles.RestaurantTimmsWaiterRole;
import city.roles.RestaurantZhangCashierRole;
import city.roles.RestaurantZhangCookRole;
import city.roles.RestaurantZhangHostRole;
import city.roles.RestaurantZhangWaiterSharedDataRole;
import city.tests.animations.PersonAnimationTest;

public class Application {

	private static MainFrame mainFrame;
	private static Timer timer = new Timer();
	private static Date date = new Date(0);

	public static final int HALF_HOUR = 1800000; // A half hour in milliseconds
	public static final int INTERVAL = 1000; // One interval is the simulation's equivalent of a half-hour
	public static final int PAYCHECK_INTERVAL = 0; // TODO set the global interval at which people are paid
	public static enum BANK_SERVICE {none, deposit, moneyWithdraw, atmDeposit};
	public static enum TRANSACTION_TYPE {personal, business};
	public static enum FOOD_ITEMS {steak, chicken, salad, pizza};
	public static enum BUILDING {bank, busStop, house, market, restaurant};

	static List<CityRoad> roads = new ArrayList<CityRoad>();
	public static TrafficControl trafficControl;
	
	static CitySidewalkLayout sidewalks;
	
	private static final DataModel model = new DataModel();

	/**
	 * Main routine to start the program.
	 * 
	 * When the program is started, this is the first call. It opens the GUI window, loads
	 * configuration files, and causes the program to run.
	 */
	public static void main(String[] args) {
		// Open the animation GUI
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		mainFrame = new MainFrame();

		// Load a scenario
		parseConfig();

		// Start the simulation
		TimerTask tt = new TimerTask() {
			public void run() {
				date.setTime(date.getTime() + HALF_HOUR);
				for (Person p : model.getPeople()) {
					p.setDate(date);
				}
			}
		};
		timer.scheduleAtFixedRate(tt, 0, INTERVAL);
	}

	/**
	 * This will eventually load some type of configuration file that specifies how many
	 * people to create and what roles to create them in.
	 */
	private static void parseConfig() {
		// Create roads
		// North roads
		for(int i = 375; i >= 100; i -= 25) {
			if(i == 225)
				continue;
			CityRoad tempRoad = new CityRoad(i, 75, 25, 25, -1, 0, true, Color.black);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		// West roads
		for(int i = 75; i <= 350; i+=25) {
			if(i == 225)
				continue;
			CityRoad tempRoad = new CityRoad(75, i, 25, 25, 0, 1, false, Color.black);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		// South roads
		for(int i = 75; i <= 350; i+=25) {
			if(i == 225)
				continue;
			CityRoad tempRoad = new CityRoad(i, 375, 25, 25, 1, 0, true, Color.black);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		// East roads
		for(int i = 375; i >= 100; i-=25) {
			if(i == 225)
				continue;
			CityRoad tempRoad = new CityRoad(375, i, 25, 25, 0, -1, false, Color.black);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		// North/South middle roads
		for(int i = 350; i >= 100; i-=25) {
			if(i == 225)
				continue;
			CityRoad tempRoad = new CityRoad(225, i, 25, 25, 0, -1, false, Color.black);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		// East/West middle roads
		for(int i = 350; i >= 100; i -= 25) {
			if(i == 225)
				continue;
			CityRoad tempRoad = new CityRoad(i, 225, 25, 25, -1, 0, true, Color.black);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		// North intersection
		CityRoadIntersection intersectionNorth = new CityRoadIntersection(225, 75, 25, 25, Color.gray);
		roads.add(intersectionNorth);
		mainFrame.cityView.addMoving(intersectionNorth);
		// West intersection
		CityRoadIntersection intersectionWest = new CityRoadIntersection(75, 225, 25, 25, Color.gray);
		roads.add(intersectionWest);
		mainFrame.cityView.addMoving(intersectionWest);
		// South intersection
		CityRoadIntersection intersectionSouth = new CityRoadIntersection(225, 375, 25, 25, Color.gray);
		roads.add(intersectionSouth);
		mainFrame.cityView.addMoving(intersectionSouth);
		// East intersection
		CityRoadIntersection intersectionEast = new CityRoadIntersection(375, 225, 25, 25, Color.gray);
		roads.add(intersectionEast);
		mainFrame.cityView.addMoving(intersectionEast);
		// Center intersection
		CityRoadIntersection intersectionCenter = new CityRoadIntersection(225, 225, 25, 25, Color.gray);
		roads.add(intersectionCenter);
		mainFrame.cityView.addMoving(intersectionCenter);
		// Connect all roads
		for(int i = 0; i < roads.size() - 1; i++) {
			if(roads.get(i).getX() == intersectionNorth.getX() + 25 && roads.get(i).getY() == intersectionNorth.getY()) { // Set nextRoad of road to east of north intersection
				roads.get(i).setNextRoad(intersectionNorth);
				roads.get(i).setStopLightType(STOPLIGHTTYPE.HORIZONTALOFF);
				continue;
			} else if(roads.get(i).getY() == intersectionNorth.getY() + 25 && roads.get(i).getX() == intersectionNorth.getX()) { // Set nextRoad of road to south of north intersection
				roads.get(i).setNextRoad(intersectionNorth);
				roads.get(i).setStopLightType(STOPLIGHTTYPE.VERTICALOFF);
				continue;
			} else if(roads.get(i).getX() == intersectionNorth.getX() - 25 && roads.get(i).getY() == intersectionNorth.getY()) { // Set nextRoad of road to west of north intersection
				intersectionNorth.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getY() == intersectionWest.getY() - 25 && roads.get(i).getX() == intersectionWest.getX()) { // Set nextRoad of road to north of west intersection
				roads.get(i).setNextRoad(intersectionWest);
				roads.get(i).setStopLightType(STOPLIGHTTYPE.VERTICALOFF);
				continue;
			} else if(roads.get(i).getY() == intersectionWest.getY() + 25 && roads.get(i).getX() == intersectionWest.getX()) { // Set nextRoad of road to south of west intersection
				intersectionWest.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getX() == intersectionWest.getX() + 25 && roads.get(i).getY() == intersectionWest.getY()) { // Set nextRoad of road to east of west intersection
				roads.get(i).setNextRoad(intersectionWest);
				roads.get(i).setStopLightType(STOPLIGHTTYPE.HORIZONTALOFF);
				continue;
			} else if(roads.get(i).getY() == intersectionSouth.getY() - 25 && roads.get(i).getX() == intersectionSouth.getX()) { // Set nextRoad of road to north of south intersection
				intersectionSouth.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getX() == intersectionSouth.getX() - 25 && roads.get(i).getY() == intersectionSouth.getY()) { // Set nextRoad of road to west of south intersection
				roads.get(i).setNextRoad(intersectionSouth);
				roads.get(i).setStopLightType(STOPLIGHTTYPE.HORIZONTALOFF);
				continue;
			} else if(roads.get(i).getX() == intersectionSouth.getX() + 25 && roads.get(i).getY() == intersectionSouth.getY()) { // Set nextRoad of road to east of south intersection
				intersectionSouth.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getY() == intersectionEast.getY() - 25 && roads.get(i).getX() == intersectionEast.getX()) { // Set nextRoad of road to north of east intersection
				intersectionEast.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getX() == intersectionEast.getX() - 25 && roads.get(i).getY() == intersectionEast.getY()) { // Set nextRoad of road to west of east intersection
				intersectionEast.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getY() == intersectionEast.getY() + 25 && roads.get(i).getX() == intersectionEast.getX()) { // Set nextRoad of road to south of east intersection
				roads.get(i).setNextRoad(intersectionEast);
				roads.get(i).setStopLightType(STOPLIGHTTYPE.VERTICALOFF);
				continue;
			} else if(roads.get(i).getY() == intersectionCenter.getY() - 25 && roads.get(i).getX() == intersectionCenter.getX()) { // Set nextRoad of road to north of center intersection
				intersectionCenter.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getX() == intersectionCenter.getX() + 25 && roads.get(i).getY() == intersectionCenter.getY()) { // Set nextRoad of road to east of center intersection
				roads.get(i).setNextRoad(intersectionCenter);
				roads.get(i).setStopLightType(STOPLIGHTTYPE.HORIZONTALOFF);
				continue;
			} else if(roads.get(i).getX() == intersectionCenter.getX() - 25 && roads.get(i).getY() == intersectionCenter.getY()) { // Set nextRoad of road to west of center intersection
				intersectionCenter.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getY() == intersectionCenter.getY() + 25 && roads.get(i).getX() == intersectionCenter.getX()) { // Set nextRoad of road to south of center intersection
				roads.get(i).setNextRoad(intersectionCenter);
				roads.get(i).setStopLightType(STOPLIGHTTYPE.VERTICALOFF);
				continue;
			} else if(roads.get(i).getX() == 375 && roads.get(i).getY() == 100) { // Last road in the outer loop
				roads.get(i).setNextRoad(roads.get(0));
				continue;
			}
			// Straight road
			if(roads.get(i).getClass() != CityRoadIntersection.class)
				roads.get(i).setNextRoad(roads.get(i+1));
		}
		trafficControl = new TrafficControl(roads);
		
		// Sidewalks
		ArrayList<Rectangle> nonSidewalkArea = new ArrayList<Rectangle>();
		nonSidewalkArea.add(new Rectangle(2, 2, 14, 2)); // Top left
		nonSidewalkArea.add(new Rectangle(18, 2, 10, 2)); // Top right
		nonSidewalkArea.add(new Rectangle(2, 4, 2, 8)); // Topmid left
		nonSidewalkArea.add(new Rectangle(14, 6, 2, 10)); // Topmid center
		nonSidewalkArea.add(new Rectangle(26, 4, 2, 12)); // Topmid right
		nonSidewalkArea.add(new Rectangle(6, 14, 10, 2)); // Center left
		nonSidewalkArea.add(new Rectangle(18, 14, 10, 2)); // Center right
		nonSidewalkArea.add(new Rectangle(2, 14, 2, 12)); // Bottommid left
		nonSidewalkArea.add(new Rectangle(14, 18, 2, 8)); // Bottommid center
		nonSidewalkArea.add(new Rectangle(26, 18, 2, 8)); // Bottommid right
		nonSidewalkArea.add(new Rectangle(2, 26, 10, 2)); // Bottom left
		nonSidewalkArea.add(new Rectangle(14, 26, 14, 2)); // Bottom right
		nonSidewalkArea.add(new Rectangle(6, 6, 6, 6)); // Top left square
		nonSidewalkArea.add(new Rectangle(18, 6, 6, 6)); // Top right square
		nonSidewalkArea.add(new Rectangle(6, 18, 6, 6)); // Bottom left square
		nonSidewalkArea.add(new Rectangle(18, 18, 6, 6)); // Bottom right square
		sidewalks = new CitySidewalkLayout(mainFrame, 30, 30, 50, 50, 12.5, Color.orange, nonSidewalkArea);
		
		// Bus Stops!!!!!!!!
		BusStopPanel bsp1 = new BusStopPanel(Color.white);
		CityViewBusStop cityViewBusStop1 = new CityViewBusStop(350, 0, "Bus Stop 1", Color.white, bsp1);
		BusStopBuilding busStop1 = new BusStopBuilding("Bus Stop 1", bsp1, cityViewBusStop1);
		createBuilding(bsp1, cityViewBusStop1, busStop1);
		
		BusStopPanel bsp2 = new BusStopPanel(Color.white);
		CityViewBusStop cityViewBusStop2 = new CityViewBusStop(0, 125, "Bus Stop 2", Color.white, bsp2);
		BusStopBuilding busStop2 = new BusStopBuilding("Bus Stop 2", bsp2, cityViewBusStop2);
		createBuilding(bsp2, cityViewBusStop2, busStop2);

		BusStopPanel bsp3 = new BusStopPanel(Color.white);
		CityViewBusStop cityViewBusStop3 = new CityViewBusStop(275, 275, "Bus Stop 3", Color.white, bsp3);
		BusStopBuilding busStop3 = new BusStopBuilding("Bus Stop 3", bsp3, cityViewBusStop3);
		createBuilding(bsp3, cityViewBusStop3, busStop3);

		BusStopPanel bsp4 = new BusStopPanel(Color.white);
		CityViewBusStop cityViewBusStop4 = new CityViewBusStop(75, 425, "Bus Stop 4", Color.white, bsp4);
		BusStopBuilding busStop4 = new BusStopBuilding("Bus Stop 4", bsp4, cityViewBusStop4);
		createBuilding(bsp4, cityViewBusStop4, busStop4);
		
		// Create buildings
		BankPanel bankPanel1 = new BankPanel(Color.green);
		CityViewBank cityViewBank1 = new CityViewBank(450, 200, "Bank " + mainFrame.cityView.getStaticsSize(), Color.green, bankPanel1);
		BankBuilding bankBuilding1 = new BankBuilding("BankBuilding", bankPanel1, cityViewBank1);
		createBuilding(bankPanel1, cityViewBank1, bankBuilding1);

		busStop1.setNextStop(busStop2);
		busStop1.setPreviousStop(busStop4);
		busStop2.setNextStop(busStop3);
		busStop2.setPreviousStop(busStop1);
		busStop3.setNextStop(busStop4);
		busStop3.setPreviousStop(busStop2);
		busStop4.setNextStop(busStop1);
		busStop4.setPreviousStop(busStop3);

		// Create buses
		BusAgent bus1 = new BusAgent(busStop1, busStop2);
		BusAnimation b1Anim = new BusAnimation(bus1, busStop2);
		bus1.setAnimation(b1Anim);
		mainFrame.cityView.addAnimation(b1Anim);
		CityMap.findClosestRoad(busStop2).setVehicle(b1Anim); 
		bus1.startThread();

		// Create houses
		HousePanel[] hp = new HousePanel[30];
		for(int i = 0; i < hp.length; i++){ // all house panels have the same background?
			hp[i] = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529));
		}
		

		RestaurantZhangPanel restaurantZhangPanel1 = new RestaurantZhangPanel(Color.DARK_GRAY);
		CityViewRestaurant cityViewRestaurantZhang1 = new CityViewRestaurant(150, 150, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.magenta, restaurantZhangPanel1);
		RestaurantZhangBuilding rzb1 = new RestaurantZhangBuilding("RestaurantZhang1", restaurantZhangPanel1, cityViewRestaurantZhang1);
		restaurantZhangPanel1.setTables(rzb1.tables);
		createBuilding(restaurantZhangPanel1, cityViewRestaurantZhang1, rzb1);

//		HousePanel housePanelZhang1 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529));
//		CityViewHouse cityViewHouseZhang1 = new CityViewHouse(150, 300, "Zhang Landlord House", Color.gray, housePanelZhang1);
//		HouseBuilding houseBuildingZhang1 = new HouseBuilding("House 0 Zhang", null, housePanelZhang1, cityViewHouseZhang1);
//		createBuilding(housePanelZhang1, cityViewHouseZhang1, houseBuildingZhang1);
		
		//rchoi edit: if you're wondering why this is in the middle of the road, it's because of a car error when someone wants to go to where he's already at by driving
		//he takes a right turn, but stoplights are only made for left turns in this city, so he gets locked in the intersection. TODO issue #66 (don't leave if you don't have to)
		//this is literally the only place this works right now for some reason
		HousePanel apartmentPanelZhang1 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529)); // this is now a house, because I just finished house.
		CityViewHouse cityViewHouseZhang1 = new CityViewHouse(150, 300, "Zhang Landlord House", Color.gray, apartmentPanelZhang1); 
		//if you want to see house animation, try 75,225 for location (: and uncomment lines 869, 874.
		HouseBuilding apartmentBuildingZhang1 = new HouseBuilding("Apt 0 Zhang", null, apartmentPanelZhang1, cityViewHouseZhang1);
		createBuilding(apartmentPanelZhang1, cityViewHouseZhang1, apartmentBuildingZhang1);
		
		apartmentBuildingZhang1.addFood(FOOD_ITEMS.chicken, 500); //
		apartmentBuildingZhang1.addFood(FOOD_ITEMS.salad, 500); //
		apartmentBuildingZhang1.addFood(FOOD_ITEMS.pizza, 500); //
		apartmentBuildingZhang1.addFood(FOOD_ITEMS.steak, 500); // this prevents him from going to the market (testing house animation)

		// Create landlord
		PersonAgent p0Zhang = new PersonAgent("Landlord Zhang", date);
		HouseResidentAnimation homeAnimation = new HouseResidentAnimation(p0Zhang);
		p0Zhang.setHomeAnimation(homeAnimation);
		LandlordRole p0r1Zhang = new LandlordRole();
		p0Zhang.addRole(p0r1Zhang);
		apartmentBuildingZhang1.setLandlord(p0r1Zhang);
		p0Zhang.setHome(apartmentBuildingZhang1);
		p0r1Zhang.setActive();
		model.addPerson(p0Zhang);

		// Create people
		PersonAgent p1Zhang = new PersonAgent("Cashier 1 Zhang", date);
		PersonAgent p2Zhang = new PersonAgent("Cook 1 Zhang", date);
		PersonAgent p3Zhang = new PersonAgent("Host 1 Zhang", date);
		PersonAgent p4Zhang = new PersonAgent("Waiter 1 Zhang", date);
		model.addPerson(p1Zhang);
		model.addPerson(p2Zhang);
		model.addPerson(p3Zhang);
		model.addPerson(p4Zhang);
		p1Zhang.setHome(apartmentBuildingZhang1);
		p2Zhang.setHome(apartmentBuildingZhang1);
		p3Zhang.setHome(apartmentBuildingZhang1);
		p4Zhang.setHome(apartmentBuildingZhang1);

		// Give people cars
		CarAgent c0Zhang = new CarAgent(busStop2,p0Zhang);
		CarAnimation c0AnimZhang = new CarAnimation(c0Zhang, busStop2);
		c0Zhang.setAnimation(c0AnimZhang);
		mainFrame.cityView.addAnimation(c0AnimZhang);
		CarAgent c1Zhang = new CarAgent(busStop2, p1Zhang);
		CarAnimation c1AnimZhang = new CarAnimation(c1Zhang, busStop2);
		c1Zhang .setAnimation(c1AnimZhang);
		mainFrame.cityView.addAnimation(c1AnimZhang);
		CarAgent c2Zhang = new CarAgent(busStop2, p2Zhang);
		CarAnimation c2AnimZhang = new CarAnimation(c2Zhang, busStop2);
		c2Zhang.setAnimation(c2AnimZhang);
		mainFrame.cityView.addAnimation(c2AnimZhang);
		CarAgent c3Zhang = new CarAgent(busStop2, p3Zhang);
		CarAnimation c3AnimZhang = new CarAnimation(c3Zhang, busStop2);
		c3Zhang.setAnimation(c3AnimZhang);
		mainFrame.cityView.addAnimation(c3AnimZhang);
		CarAgent c4Zhang = new CarAgent(busStop2, p4Zhang);
		CarAnimation c4AnimZhang = new CarAnimation(c4Zhang, busStop2);
		c4Zhang.setAnimation(c4AnimZhang);
		mainFrame.cityView.addAnimation(c4AnimZhang);

		// Create cashier
		RestaurantZhangCashierRole p1r1Zhang = new RestaurantZhangCashierRole(rzb1, 0, 100);
		rzb1.addOccupyingRole(p1r1Zhang);
		p1Zhang.setOccupation(p1r1Zhang);

		// Create cook
		RestaurantZhangCookRole p2r1Zhang = new RestaurantZhangCookRole(rzb1, 0, 100);
		rzb1.addOccupyingRole(p2r1Zhang);
		p2Zhang.setOccupation(p2r1Zhang);

		// Create host
		RestaurantZhangHostRole p3r1Zhang = new RestaurantZhangHostRole(rzb1, 0, 100);
		rzb1.addOccupyingRole(p3r1Zhang);
		p3Zhang.setOccupation(p3r1Zhang);

		// Create waiter
		RestaurantZhangWaiterSharedDataRole p4r1Zhang = new RestaurantZhangWaiterSharedDataRole(rzb1, 0, 100);
		rzb1.addOccupyingRole(p4r1Zhang);
		p4Zhang.setOccupation(p4r1Zhang);

		// RESTAURANTTIMMS---------------------------------------------------------------------------------------
		// Create panels
		RestaurantTimmsPanel rtp1 = new RestaurantTimmsPanel(Color.GRAY);
		CityViewRestaurant cvr1 = new CityViewRestaurant(150, 150, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.cyan, rtp1); 
		RestaurantTimmsBuilding rtb = new RestaurantTimmsBuilding("RestaurantTimms", rtp1, cvr1);
		createBuilding(rtp1, cvr1, rtb);
		
		// Create buildings
		HousePanel rhp1Timms = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529));
		CityViewHouse rhcv1Timms = new CityViewHouse(300, 430, "House " + mainFrame.cityView.getStaticsSize(), Color.gray, rhp1Timms);
		HouseBuilding rhb1Timms = new HouseBuilding("Timms House", null, rhp1Timms, rhcv1Timms);
		createBuilding(rhp1Timms, rhcv1Timms, rhb1Timms);

		// Create tables
		int i = 0;
		while (i < 9) {
			rtp1.addVisualizationElement(new RestaurantTimmsTableAnimation(i));
			i++;
		}

		// Create landlord
		PersonAgent p0Timms = new PersonAgent("Landlord Timms", date);
		LandlordRole p0r1Timms = new LandlordRole();
		p0Timms.addRole(p0r1Timms);

		rhb1Timms.setLandlord(p0r1Timms);
		p0Timms.setHome(rhb1Timms);
		p0Timms.setCash(10);
		p0r1Timms.setActive();
		model.addPerson(p0Timms);


		// Create people
		PersonAgent p1Timms = new PersonAgent("Cashier 1 Timms", date);
		PersonAgent p2Timms = new PersonAgent("Cook 1 Timms", date);
		PersonAgent p3Timms = new PersonAgent("Host 1 Timms", date);
		PersonAgent p4Timms = new PersonAgent("Waiter 1 Timms", date);
		model.addPerson(p1Timms);
		model.addPerson(p2Timms);
		model.addPerson(p3Timms);
		model.addPerson(p4Timms);
		p1Timms.setHome(rhb1Timms);
		p2Timms.setHome(rhb1Timms);
		p3Timms.setHome(rhb1Timms);
		p4Timms.setHome(rhb1Timms);

		// Give people cars
		CarAgent c0Timms = new CarAgent(busStop3, p0Timms);
		CarAnimation c0AnimTimms = new CarAnimation(c0Timms, busStop3);
		c0Timms.setAnimation(c0AnimTimms);
		mainFrame.cityView.addAnimation(c0AnimTimms);
		CarAgent c1Timms = new CarAgent(busStop3, p1Timms);
		CarAnimation c1AnimTimms = new CarAnimation(c1Timms, busStop3);
		c1Timms.setAnimation(c1AnimTimms);
		mainFrame.cityView.addAnimation(c1AnimTimms);
		CarAgent c2Timms = new CarAgent(busStop3, p2Timms);
		CarAnimation c2AnimTimms = new CarAnimation(c2Timms, busStop3);
		c2Timms.setAnimation(c2AnimTimms);
		mainFrame.cityView.addAnimation(c2AnimTimms);
		CarAgent c3Timms = new CarAgent(busStop3, p3Timms);
		CarAnimation c3AnimTimms = new CarAnimation(c3Timms, busStop3);
		c3Timms.setAnimation(c3AnimTimms);
		mainFrame.cityView.addAnimation(c3AnimTimms);
		CarAgent c4Timms = new CarAgent(busStop3, p4Timms);
		CarAnimation c4AnimTimms = new CarAnimation(c4Timms, busStop3);
		c4Timms.setAnimation(c4AnimTimms);
		mainFrame.cityView.addAnimation(c4AnimTimms);

		// Create cashier
		RestaurantTimmsCashierRole p1r1Timms = new RestaurantTimmsCashierRole(rtb, 0, 100);
		rtb.addOccupyingRole(p1r1Timms);
		p1Timms.setOccupation(p1r1Timms);

		// Create cook
		RestaurantTimmsCookRole p2r1Timms = new RestaurantTimmsCookRole(rtb, 0, 100);
		rtb.addOccupyingRole(p2r1Timms);
		p2Timms.setOccupation(p2r1Timms);

		// Create host
		RestaurantTimmsHostRole p3r1Timms = new RestaurantTimmsHostRole(rtb, 0, 100);
		rtb.addOccupyingRole(p3r1Timms);
		p3Timms.setOccupation(p3r1Timms);

		// Create waiter
		RestaurantTimmsWaiterRole p4r1Timms = new RestaurantTimmsWaiterRole(rtb, 0, 100);
		rtb.addOccupyingRole(p4r1Timms);
		p4Timms.setOccupation(p4r1Timms);


//		// RESTAURANTCHOI----------------------------------------------------------------------------
//		MarketPanel marketPanelChoi1 = new MarketPanel(Color.black);
//		CityViewMarket cityViewMarketChoi1 = new CityViewMarket(250, 450, "Choi Market 1", Color.orange, marketPanelChoi1);
//		MarketBuilding marketBuildingChoi1 = new MarketBuilding("Choi Market 1", marketPanelChoi1, cityViewMarketChoi1);
//		createBuilding(marketPanelChoi1, cityViewMarketChoi1, marketBuildingChoi1);
//
//		RestaurantChoiPanel restaurantChoiPanel1 = new RestaurantChoiPanel(Color.GRAY);
//		CityViewRestaurant cityViewRestaurantChoi1 = new CityViewRestaurant(200, 200, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.cyan, restaurantChoiPanel1);
//		RestaurantChoiBuilding restaurantChoiBuilding1 = new RestaurantChoiBuilding("RestaurantChoi1", restaurantChoiPanel1, cityViewRestaurantChoi1);
//		createBuilding(restaurantChoiPanel1, cityViewRestaurantChoi1, restaurantChoiBuilding1);
//		
//		HousePanel rhp1Choi = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529));
//		CityViewHouse rhcv1Choi = new CityViewHouse(350, 430, "House " + mainFrame.cityView.getStaticsSize(), Color.gray, rhp1Choi);
//		HouseBuilding rhb1Choi = new HouseBuilding("Choi House", null, rhp1Choi, rhcv1Choi);
//		createBuilding(rhp1Choi, rhcv1Choi, rhb1Choi);
//		
//		// Create landlord
//		PersonAgent p0Choi = new PersonAgent("Landlord Choi", date);
//		LandlordRole p0r1Choi = new LandlordRole();
//		p0Choi.addRole(p0r1Choi);
//		rhb1Choi.setLandlord(p0r1Choi);
//		p0Choi.setHome(rhb1Choi);
//		p0r1Choi.setActive();
//		model.addPerson(p0Choi);
//
//		// Create people
//		PersonAgent p1Choi = new PersonAgent("Cashier 1 Choi", date);
//		PersonAgent p2Choi = new PersonAgent("Cook 1 Choi", date);
//		PersonAgent p3Choi = new PersonAgent("Host 1 Choi", date);
//		PersonAgent p4Choi = new PersonAgent("Waiter 1 Choi", date);
//		PersonAgent p5Choi = new PersonAgent("Market Mgr Choi", date);
//		PersonAgent p6Choi = new PersonAgent("Market Cshr Choi", date);
//		PersonAgent p7Choi = new PersonAgent("Market Emp Choi", date);
//		PersonAgent p8Choi = new PersonAgent("Market Dlvry Choi", date);
//		PersonAgent p9Choi = new PersonAgent("Bank manager Choi", date);
//		PersonAgent p10Choi = new PersonAgent("Bank Teller Choi", date);
//
//		model.addPerson(p1Choi);
//		model.addPerson(p2Choi);
//		model.addPerson(p3Choi);
//		model.addPerson(p4Choi);
//		model.addPerson(p5Choi);
//		model.addPerson(p6Choi);
//		model.addPerson(p7Choi);
//		model.addPerson(p8Choi);
//		model.addPerson(p9Choi);
//		model.addPerson(p10Choi);
//
//		p1Choi.setHome(rhb1Choi);
//		p2Choi.setHome(rhb1Choi);
//		p3Choi.setHome(rhb1Choi);
//		p4Choi.setHome(rhb1Choi);

//		p5Choi.setHome(rhb1Choi);
//		p6Choi.setHome(rhb1Choi);
//		p7Choi.setHome(rhb1Choi);
//		p8Choi.setHome(rhb1Choi);
//		p9Choi.setHome(rhb1Choi);
//		p10Choi.setHome(rhb1Choi);

//
//		// Landlord
//		RestaurantChoiCashierRole p1r1Choi = new RestaurantChoiCashierRole(restaurantChoiBuilding1, 0, 24);
//		restaurantChoiBuilding1.addOccupyingRole(p1r1Choi);
//		p1Choi.setOccupation(p1r1Choi);
//
//		// Create cook
//		RestaurantChoiCookRole p2r1Choi = new RestaurantChoiCookRole(restaurantChoiBuilding1, 0, 24);
//		restaurantChoiBuilding1.addOccupyingRole(p2r1Choi);
//		p2Choi.setOccupation(p2r1Choi);
//		p2r1Choi.addMarket(marketBuildingChoi1);
//
//		// Create host
//		RestaurantChoiHostRole p3r1Choi = new RestaurantChoiHostRole(restaurantChoiBuilding1, 0, 24);
//		restaurantChoiBuilding1.addOccupyingRole(p3r1Choi);
//		p3Choi.setOccupation(p3r1Choi);
//
//		// Create waiter
//		RestaurantChoiWaiterQueueRole p4r1Choi = new RestaurantChoiWaiterQueueRole(restaurantChoiBuilding1, 0, 24);
//		restaurantChoiBuilding1.addOccupyingRole(p4r1Choi);
//		p4Choi.setOccupation(p4r1Choi);
//
//		//Create bank roles
//
//		BankManagerRole p9r1Choi = new BankManagerRole(bankBuilding1, 0, 24);
//		p9Choi.setOccupation(p9r1Choi);
//		p9r1Choi.setPerson(p9Choi);
//		BankTellerRole p10r1Choi = new BankTellerRole(bankBuilding1, 0, 24);
//		p10Choi.setOccupation(p10r1Choi);
//		p10r1Choi.setPerson(p10Choi);
//		bankBuilding1.addOccupyingRole(p9r1Choi);
//		bankBuilding1.addOccupyingRole(p10r1Choi);
//
//		//Create Market people
//		MarketManagerRole p5r1Choi = new MarketManagerRole(marketBuildingChoi1, 0, 24);
//		MarketCashierRole p6r1Choi = new MarketCashierRole(marketBuildingChoi1, 0, 24);
//		MarketEmployeeRole p7r1Choi = new MarketEmployeeRole(marketBuildingChoi1, 0, 24);
//		MarketDeliveryPersonRole p8r1Choi = new MarketDeliveryPersonRole(marketBuildingChoi1, 0, 24);
//		p5Choi.setOccupation(p5r1Choi);
//		p5r1Choi.setPerson(p5Choi);
//		p6Choi.setOccupation(p6r1Choi);
//		p6r1Choi.setPerson(p6Choi);
//		p7Choi.setOccupation(p7r1Choi);
//		p7r1Choi.setPerson(p7Choi);
//		p8r1Choi.setPerson(p8Choi);
//		p8Choi.setOccupation(p8r1Choi);
//		marketBuildingChoi1.addOccupyingRole(p5r1Choi);
//		marketBuildingChoi1.addOccupyingRole(p6r1Choi);
//		marketBuildingChoi1.addOccupyingRole(p7r1Choi);
//		marketBuildingChoi1.addOccupyingRole(p8r1Choi);
//		marketBuildingChoi1.setManager(p5r1Choi);
//		marketBuildingChoi1.setCashier(p6r1Choi);
//		marketBuildingChoi1.addEmployee(p7r1Choi);
//		marketBuildingChoi1.addDeliveryPerson(p8r1Choi);
//
//		// Give people cars
//		CarAgent c0Choi = new CarAgent(busStop4, p0Choi);
//		CarAnimation c0AnimChoi = new CarAnimation(c0Choi, busStop4);
//		c0Choi.setAnimation(c0AnimChoi);
//		mainFrame.cityView.addAnimation(c0AnimChoi);
//		CarAgent c1Choi = new CarAgent(busStop4);
//		CarAnimation c1AnimChoi = new CarAnimation(c1Choi, busStop4);
//		c1Choi.setAnimation(c1AnimChoi);
//		mainFrame.cityView.addAnimation(c1AnimChoi);
//		CarAgent c2Choi = new CarAgent(busStop4);
//		CarAnimation c2AnimChoi = new CarAnimation(c2Choi, busStop4);
//		c2Choi.setAnimation(c2AnimChoi);
//		mainFrame.cityView.addAnimation(c2AnimChoi);
//		CarAgent c3Choi = new CarAgent(busStop4);
//		CarAnimation c3AnimChoi = new CarAnimation(c3Choi, busStop4);
//		c3Choi.setAnimation(c3AnimChoi);
//		mainFrame.cityView.addAnimation(c3AnimChoi);
//		CarAgent c4Choi = new CarAgent(busStop4);
//		CarAnimation c4AnimChoi = new CarAnimation(c4Choi, busStop4);
//		c4Choi.setAnimation(c4AnimChoi);
//		mainFrame.cityView.addAnimation(c4AnimChoi);
//		CarAgent c5Choi = new CarAgent(busStop4);
//		CarAnimation c5AnimChoi = new CarAnimation(c5Choi, busStop4);
//		c5Choi.setAnimation(c5AnimChoi);
//		mainFrame.cityView.addAnimation(c5AnimChoi);
//		CarAgent c6Choi = new CarAgent(busStop4, p6Choi);
//		CarAnimation c6AnimChoi = new CarAnimation(c6Choi, busStop4);
//		c6Choi.setAnimation(c6AnimChoi);
//		mainFrame.cityView.addAnimation(c6AnimChoi);
//		CarAgent c7Choi = new CarAgent(busStop4, p7Choi);
//		CarAnimation c7AnimChoi = new CarAnimation(c7Choi, busStop4);
//		c7Choi.setAnimation(c7AnimChoi);
//		mainFrame.cityView.addAnimation(c7AnimChoi);
//		CarAgent c8Choi = new CarAgent(busStop4, p8Choi);
//		CarAnimation c8AnimChoi = new CarAnimation(c8Choi, busStop4);
//		c8Choi.setAnimation(c8AnimChoi);
//		mainFrame.cityView.addAnimation(c8AnimChoi);
//		CarAgent c9Choi = new CarAgent(busStop4, p9Choi);
//		CarAnimation c9AnimChoi = new CarAnimation(c9Choi, busStop4);
//		c9Choi.setAnimation(c9AnimChoi);
//		mainFrame.cityView.addAnimation(c9AnimChoi);
//		CarAgent c10Choi = new CarAgent(busStop4, p10Choi);
//		CarAnimation c10AnimChoi = new CarAnimation(c10Choi, busStop4);
//		c10Choi.setAnimation(c10AnimChoi);
//		mainFrame.cityView.addAnimation(c10AnimChoi);
//
//		p0Choi.setCar(c0Choi);
//		p1Choi.setCar(c1Choi);
//		p2Choi.setCar(c2Choi);
//		p3Choi.setCar(c3Choi);
//		p4Choi.setCar(c4Choi);
//		p5Choi.setCar(c5Choi);
//		p6Choi.setCar(c6Choi);
//		p7Choi.setCar(c7Choi);
//		p8Choi.setCar(c8Choi);
//		p9Choi.setCar(c9Choi);
//		p10Choi.setCar(c10Choi);
//
//		// RESTAURANTCHUNG------------------------------------------------------------------------------
//		
//		// RESTAURANTCHUNGTESTING FOR ANIMATION IN GUI
//		RestaurantChungPanel restaurantChungPanel1 = new RestaurantChungPanel(Color.black);
//		CityViewRestaurant cityViewRestaurantChung1 = new CityViewRestaurant(400, 250, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.yellow, restaurantChungPanel1); 
//		RestaurantChungBuilding restaurantChungBuilding1 = new RestaurantChungBuilding("RestaurantChung1", restaurantChungPanel1, cityViewRestaurantChung1);
//		createBuilding(restaurantChungPanel1, cityViewRestaurantChung1, restaurantChungBuilding1);
//		
//		HousePanel housePanelChung1 = new HousePanel(Color.black);
//		CityViewHouse cityViewHouseChung1 = new CityViewHouse(425,250, "Chung House" + (mainFrame.cityView.getStaticsSize()), Color.gray, housePanelChung1);
//		HouseBuilding houseBuildingChung1 = new HouseBuilding("Chung House", null, housePanelChung1, cityViewHouseChung1);
//		createBuilding(housePanelChung1, cityViewHouseChung1, houseBuildingChung1);
//		
//		// Create landlord
//		PersonAgent p0Chung = new PersonAgent("Landlord Chung", date);
//		System.out.println(p0Chung);
//		System.out.println(p0Chung.getCash());
//		p0Chung.setCash(50); // TODO remove later
//		LandlordRole p0r1Chung = new LandlordRole();
//		p0Chung.addRole(p0r1Chung);
//		houseBuildingChung1.setLandlord(p0r1Chung);
//		p0Chung.setHome(houseBuildingChung1);
//		p0r1Chung.setActive();
//		model.addPerson(p0Chung);
//
//		// Create people
//		PersonAgent p1Chung = new PersonAgent("Cashier 1 Chung", date);
//		PersonAgent p2Chung = new PersonAgent("Cook 1 Chung", date);
//		PersonAgent p3Chung = new PersonAgent("Host 1 Chung", date);
//		PersonAgent p4Chung = new PersonAgent("Waiter 1 Chung", date);
//		model.addPerson(p1Chung);
//		model.addPerson(p2Chung);
//		model.addPerson(p3Chung);
//		model.addPerson(p4Chung);
//		p1Chung.setHome(houseBuildingChung1);
//		p2Chung.setHome(houseBuildingChung1);
//		p3Chung.setHome(houseBuildingChung1);
//		p4Chung.setHome(houseBuildingChung1);
//
//		// Give people cars
//		CarAgent c0Chung = new CarAgent(busStop1, p0Chung);
//		CarAnimation c0AnimChung = new CarAnimation(c0Chung, busStop1);
//		c0Chung.setAnimation(c0AnimChung);
//		mainFrame.cityView.addAnimation(c0AnimChung);
//		CarAgent c1Chung = new CarAgent(busStop1, p1Chung);
//		CarAnimation c1AnimChung = new CarAnimation(c1Chung, busStop1);
//		c1Chung.setAnimation(c1AnimChung);
//		mainFrame.cityView.addAnimation(c1AnimChung);
//		CarAgent c2Chung = new CarAgent(busStop1, p2Chung);
//		CarAnimation c2AnimChung = new CarAnimation(c2Chung, busStop1);
//		c2Chung.setAnimation(c2AnimChung);
//		mainFrame.cityView.addAnimation(c2AnimChung);
//		CarAgent c3Chung = new CarAgent(busStop1, p3Chung);
//		CarAnimation c3AnimChung = new CarAnimation(c3Chung, busStop1);
//		c3Chung.setAnimation(c3AnimChung);
//		mainFrame.cityView.addAnimation(c3AnimChung);
//		CarAgent c4Chung = new CarAgent(busStop1, p4Chung);
//		CarAnimation c4AnimChung = new CarAnimation(c4Chung, busStop1);
//		c4Chung.setAnimation(c4AnimChung);
//		mainFrame.cityView.addAnimation(c4AnimChung);
//
//		// Create cashier
//		RestaurantChungCashierRole p1r1Chung = new RestaurantChungCashierRole(restaurantChungBuilding1, 0, 12);
//		p1r1Chung.setPerson(p1Chung);
//		p1r1Chung.setMarketCustomerDeliveryPaymentPerson();
//		p1r1Chung.setBankCustomerPerson();
//		restaurantChungBuilding1.addOccupyingRole(p1r1Chung);
//		p1Chung.setOccupation(p1r1Chung);
//		
//		// Create cook
//		RestaurantChungCookRole p2r1Chung = new RestaurantChungCookRole(restaurantChungBuilding1, 0, 12);
//		p2r1Chung.setPerson(p2Chung);		
//		restaurantChungBuilding1.addOccupyingRole(p2r1Chung);
//		p2Chung.setOccupation(p2r1Chung);
//		
//		// Create host
//		RestaurantChungHostRole p3r1Chung = new RestaurantChungHostRole(restaurantChungBuilding1, 0, 12);
//		p3r1Chung.setPerson(p3Chung);		
//		restaurantChungBuilding1.addOccupyingRole(p3r1Chung);
//		p3Chung.setOccupation(p3r1Chung);
//		
//		// Create waiter
//		RestaurantChungWaiterMessageCookRole p4r1Chung = new RestaurantChungWaiterMessageCookRole(restaurantChungBuilding1, 0, 12);
//		p4r1Chung.setPerson(p4Chung);		
//		restaurantChungBuilding1.addOccupyingRole(p4r1Chung);
//		p4Chung.setOccupation(p4r1Chung);
//
//		//RESTAURANTJP------------------------------------------------------------------------
//		RestaurantJPPanel restaurantJPPanel1 = new RestaurantJPPanel(Color.DARK_GRAY);
//		CityViewRestaurant cityViewRestaurantJP1 = new CityViewRestaurant(400, 200, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.green, restaurantJPPanel1); 
//		RestaurantJPBuilding restaurantJPBuilding1 = new RestaurantJPBuilding("RestaurantJP1", restaurantJPPanel1, cityViewRestaurantJP1);
//		createBuilding(restaurantJPPanel1, cityViewRestaurantJP1, restaurantJPBuilding1);
//		
//		HousePanel housePanelJP1 = new HousePanel(Color.black);
//		CityViewHouse cityViewHouseJP1 = new CityViewHouse(400, 250, "JP House" + (mainFrame.cityView.getStaticsSize()), Color.gray, housePanelJP1);
//		HouseBuilding houseBuildingJP1 = new HouseBuilding("JP House", null, housePanelJP1, cityViewHouseJP1);
//		createBuilding(housePanelJP1, cityViewHouseJP1, houseBuildingJP1);
//		
//		PersonAgent p0JP1 = new PersonAgent("Landlord JP", date);
//		LandlordRole p0r1JP1 = new LandlordRole();
//		p0JP1.addRole(p0r1JP1);
//		houseBuildingJP1.setLandlord(p0r1JP1);
//		p0JP1.setHome(houseBuildingJP1);
//		p0r1JP1.setActive();
//		model.addPerson(p0JP1);
//		
//		// Create people
//		PersonAgent p1JP = new PersonAgent("Cashier 1 JP", date);
//		PersonAgent p2JP = new PersonAgent("Cook 1 JP", date);
//		PersonAgent p3JP = new PersonAgent("Host 1 JP", date);
//		PersonAgent p4JP = new PersonAgent("Waiter 1 JP", date);
//		model.addPerson(p1JP);
//		model.addPerson(p2JP);
//		model.addPerson(p3JP);
//		model.addPerson(p4JP);
//
//		p1JP.setHome(houseBuildingJP1);
//		p2JP.setHome(houseBuildingJP1);
//		p3JP.setHome(houseBuildingJP1);
//		p4JP.setHome(houseBuildingJP1);
//
//		// Give people cars

//		CarAgent c0JP = new CarAgent(busStop3);
//		CarAnimation c0AnimJP = new CarAnimation(c0JP, busStop3);
//		c0JP.setAnimation(c0AnimJP);
//		mainFrame.cityView.addAnimation(c0AnimJP);
//		CarAgent c1JP = new CarAgent(busStop3, p1JP);
//		CarAnimation c1AnimJP = new CarAnimation(c1JP, busStop3);
//		c1JP .setAnimation(c1AnimJP);
//		mainFrame.cityView.addAnimation(c1AnimJP);
//		CarAgent c2JP = new CarAgent(busStop3, p2JP);
//		CarAnimation c2AnimJP = new CarAnimation(c2JP, busStop3);
//		c2JP.setAnimation(c2AnimJP);
//		mainFrame.cityView.addAnimation(c2AnimJP);
//		CarAgent c3JP = new CarAgent(busStop3, p3JP);
//		CarAnimation c3AnimJP = new CarAnimation(c3JP, busStop3);
//		c3JP.setAnimation(c3AnimJP);
//		mainFrame.cityView.addAnimation(c3AnimJP);
//		CarAgent c4JP = new CarAgent(busStop2, p4JP);
//		CarAnimation c4AnimJP = new CarAnimation(c4JP, busStop3);
//		c4JP.setAnimation(c4AnimJP);
//		mainFrame.cityView.addAnimation(c4AnimJP);
//
//		// Create cashier
//		RestaurantJPCashierRole p1r1JP = new RestaurantJPCashierRole(restaurantJPBuilding1, 0, 100);
//		restaurantJPBuilding1.addOccupyingRole(p1r1JP);
//		p1JP.setOccupation(p1r1JP);
//
//		// Create cook
//		RestaurantJPCookRole p2r1JP = new RestaurantJPCookRole(restaurantJPBuilding1, 0, 100);
//		restaurantJPBuilding1.addOccupyingRole(p2r1JP);
//		p2JP.setOccupation(p2r1JP);
//
//		// Create host
//		RestaurantJPHostRole p3r1JP = new RestaurantJPHostRole(restaurantJPBuilding1, 0, 100);
//		restaurantJPBuilding1.addOccupyingRole(p3r1JP);
//		p3JP.setOccupation(p3r1JP);
//
//		// Create waiter
//		RestaurantJPWaiterRole p4r1JP = new RestaurantJPWaiterRole(restaurantJPBuilding1, 0, 100);
//		restaurantJPBuilding1.addOccupyingRole(p4r1JP);
//		p4JP.setOccupation(p4r1JP);


		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}

		//c0Zhang.startThread();
		c1Zhang.startThread();
		c2Zhang.startThread();
		c3Zhang.startThread();
		c4Zhang.startThread();
		//p0Zhang.startThread();
		/*
		p1Zhang.startThread();
		p2Zhang.startThread();
		p3Zhang.startThread();

		p4Zhang.startThread();
		c0Timms.startThread();
		c1Timms.startThread();
		c2Timms.startThread();
		c3Timms.startThread();
		c4Timms.startThread();
		p0Timms.startThread();
		p1Timms.startThread();
		p2Timms.startThread();
		p3Timms.startThread();
		p4Timms.startThread();

//		p0Choi.startThread();
//		p1Choi.startThread();
//		p2Choi.startThread();
//		p3Choi.startThread();
//		p4Choi.startThread();
//		p5Choi.startThread();
//		p6Choi.startThread();
//		p7Choi.startThread();
//		p8Choi.startThread();
//		p9Choi.startThread();
//		p10Choi.startThread();
//		c0Choi.startThread();
//		c1Choi.startThread();
//		c2Choi.startThread();
//		c3Choi.startThread();
//		c4Choi.startThread();
//		c5Choi.startThread();
//		c6Choi.startThread();
//		c7Choi.startThread();
//		c8Choi.startThread();
//		c9Choi.startThread();
//		c10Choi.startThread();
//		p0Chung.startThread();
//		p1Chung.startThread();
//		p2Chung.startThread();
//		p3Chung.startThread();
//		p4Chung.startThread();
//		c0Chung.startThread();
//		c1Chung.startThread();
//		c2Chung.startThread();
//		c3Chung.startThread();
//		c4Chung.startThread();
//		p0JP1.startThread();
//		p1JP.startThread();
//		p2JP.startThread();
//		p3JP.startThread();
//		p4JP.startThread();
//		c1JP.startThread();
//		c2JP.startThread();
//		c3JP.startThread();
//		c4JP.startThread();

		PersonAnimationTest testPersonAnimation = new PersonAnimationTest(busStop2, sidewalks);
		mainFrame.cityView.addAnimation(testPersonAnimation);
*/
	}
	
	public static DataModel getModel() {
		return model;
	}
	
	public static MainFrame getMainFrame() {
		return mainFrame;
	}

	public static void createBuilding(BuildingCard panel, CityViewBuilding cityView, Building building) {
		mainFrame.cityView.addStatic(cityView);
		mainFrame.buildingView.addView(panel, cityView.getID());
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
		}
	}

	public static class CityMap {
		private static HashMap<BUILDING, List<BuildingInterface>> map = new HashMap<BUILDING, List<BuildingInterface>>();

		/**
		 * Adds a new building to the HashMap
		 * 
		 * If the map already has a key for the type of building, it adds the new building to that key's
		 * list. If the key for that type does not exist, it creates the key and gives it a list of
		 * length one that contains the new building.
		 * 
		 * @param type the type of building from the BUILDING enumeration
		 * @param b the building to add
		 */
		public static BuildingInterface addBuilding(BUILDING type, BuildingInterface b) {
			if(map.containsKey(type)) {
				map.get(type).add(b);
			} else {
				List<BuildingInterface> list = new ArrayList<BuildingInterface>();
				list.add(b);
				map.put(type, list);
			}
			model.addBuilding(b);
			return b;
		}

		/**
		 * Returns a random building of type
		 */

		public static BuildingInterface findRandomBuilding(BUILDING type) {
			List<BuildingInterface> list = map.get(type);
			Collections.shuffle(list);
			return list.get(0);
		}

		/**
		 * Find the building of type closest to the destination building
		 */
		public static BuildingInterface findClosestBuilding(BUILDING type, BuildingInterface b) {
			int x = b.getCityViewBuilding().getX();
			int y = b.getCityViewBuilding().getY();
			double closestDistance = 1000000;
			BuildingInterface returnBuilding = null;
			for(BuildingInterface tempBuilding : map.get(type)) {
				double distance = Math.sqrt((double)(Math.pow(tempBuilding.getCityViewBuilding().getX() - x, 2) + Math.pow(tempBuilding.getCityViewBuilding().getY() - y, 2)));
				if( distance < closestDistance) {
					closestDistance = distance;
					returnBuilding = tempBuilding;
				}
			}
			return returnBuilding;
		}

		/**
		 * Find the building of type closest to the person's location
		 */
		public static BuildingInterface findClosestBuilding(BUILDING type, Person p) {
			int x = p.getAnimation().getXPos();
			int y = p.getAnimation().getYPos();
			double closestDistance = 1000000;
			BuildingInterface returnBuilding = null;
			for(BuildingInterface b : map.get(type)) {
				double distance = Math.sqrt((double)(Math.pow(b.getCityViewBuilding().getX() - x, 2) + Math.pow(b.getCityViewBuilding().getY() - y, 2)));
				if( distance < closestDistance) {
					closestDistance = distance;
					returnBuilding = b;
				}
			}
			return returnBuilding;
		}

		public static CityRoad findClosestRoad(BuildingInterface b) {
			int x = b.getCityViewBuilding().getX();
			int y = b.getCityViewBuilding().getY();
			double closestDistance = 1000000;
			CityRoad returnRoad = null;
			for(CityRoad r : roads) {
				double distance = Math.sqrt((double)(Math.pow(r.getX() - x, 2) + Math.pow(r.getY() - y, 2)));
				if( distance < closestDistance) {
					closestDistance = distance;
					returnRoad = r;
				}
			}
			return returnRoad;
		}

		public static void clearMap() {
			map.clear();
		}
		
		public static ArrayList<BuildingInterface> getBuildings() {
			ArrayList<BuildingInterface> list = new ArrayList<BuildingInterface>();
			for (List<BuildingInterface> l : map.values()) {
				list.addAll(l);
			}
			return list;
		}
	}

}
