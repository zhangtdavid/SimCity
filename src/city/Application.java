package city;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
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
import city.animations.PersonAnimation;
import city.animations.RestaurantTimmsTableAnimation;
import city.animations.WalkerAnimation;
import city.bases.Building;
import city.bases.interfaces.BuildingInterface;
import city.buildings.AptBuilding;
import city.buildings.BankBuilding;
import city.buildings.BusStopBuilding;
import city.buildings.HouseBuilding;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantChoiBuilding;
import city.buildings.RestaurantChungBuilding;
import city.buildings.RestaurantJPBuilding;
import city.buildings.RestaurantTimmsBuilding;
import city.buildings.RestaurantZhangBuilding;
import city.gui.BuildingCard;
import city.gui.CityRoad;
import city.gui.CityRoad.STOPLIGHTTYPE;
import city.gui.CityRoadIntersection;
import city.gui.CitySidewalkLayout;
import city.gui.MainFrame;
import city.gui.exteriors.CityViewApt;
import city.gui.exteriors.CityViewBank;
import city.gui.exteriors.CityViewBuilding;
import city.gui.exteriors.CityViewBusStop;
import city.gui.exteriors.CityViewHouse;
import city.gui.exteriors.CityViewMarket;
import city.gui.exteriors.CityViewRestaurant;
import city.gui.interiors.AptPanel;
import city.gui.interiors.BankPanel;
import city.gui.interiors.BusStopPanel;
import city.gui.interiors.HousePanel;
import city.gui.interiors.MarketPanel;
import city.gui.interiors.RestaurantChoiPanel;
import city.gui.interiors.RestaurantChungPanel;
import city.gui.interiors.RestaurantJPPanel;
import city.gui.interiors.RestaurantTimmsPanel;
import city.gui.interiors.RestaurantZhangPanel;
import city.roles.BankManagerRole;
import city.roles.BankTellerRole;
import city.roles.LandlordRole;
import city.roles.MarketCashierRole;
import city.roles.MarketDeliveryPersonRole;
import city.roles.MarketEmployeeRole;
import city.roles.MarketManagerRole;
import city.roles.RestaurantChoiCashierRole;
import city.roles.RestaurantChoiCookRole;
import city.roles.RestaurantChoiHostRole;
import city.roles.RestaurantChoiWaiterQueueRole;
import city.roles.RestaurantChungCashierRole;
import city.roles.RestaurantChungCookRole;
import city.roles.RestaurantChungHostRole;
import city.roles.RestaurantChungWaiterMessageCookRole;
import city.roles.RestaurantTimmsCashierRole;
import city.roles.RestaurantTimmsCookRole;
import city.roles.RestaurantTimmsHostRole;
import city.roles.RestaurantTimmsWaiterRole;
import city.roles.RestaurantZhangCashierRole;
import city.roles.RestaurantZhangCookRole;
import city.roles.RestaurantZhangHostRole;
import city.roles.RestaurantZhangWaiterSharedDataRole;

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
	public static enum BUILDING {bank, busStop, house, apartment, market, restaurant};

	static List<CityRoad> roads = new ArrayList<CityRoad>();
	public static TrafficControl trafficControl;

	public static CitySidewalkLayout sidewalks;

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
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		mainFrame = new MainFrame();

		// Load a scenario
		parseConfig();

		// Start the simulation
		final DateFormat df = new SimpleDateFormat("MMMM dd HHmm");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		TimerTask tt = new TimerTask() {
			public void run() {
				date.setTime(date.getTime() + HALF_HOUR);
				mainFrame.setDisplayDate(df.format(date));
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
				continue;
			} else if(roads.get(i).getX() == intersectionNorth.getX() + 50 && roads.get(i).getY() == intersectionNorth.getY()) { // Set stoplight to east of north intersection
				roads.get(i).setStopLightType(STOPLIGHTTYPE.HORIZONTALOFF);
			} else if(roads.get(i).getY() == intersectionNorth.getY() + 25 && roads.get(i).getX() == intersectionNorth.getX()) { // Set nextRoad of road to south of north intersection
				roads.get(i).setNextRoad(intersectionNorth);
				continue;
			} else if(roads.get(i).getY() == intersectionNorth.getY() + 50 && roads.get(i).getX() == intersectionNorth.getX()) { // Set stoplight of road to south of north intersection
				roads.get(i).setStopLightType(STOPLIGHTTYPE.VERTICALOFF);
			} else if(roads.get(i).getX() == intersectionNorth.getX() - 25 && roads.get(i).getY() == intersectionNorth.getY()) { // Set nextRoad of road to west of north intersection
				intersectionNorth.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getY() == intersectionWest.getY() - 25 && roads.get(i).getX() == intersectionWest.getX()) { // Set nextRoad of road to north of west intersection
				roads.get(i).setNextRoad(intersectionWest);
				continue;
			} else if(roads.get(i).getY() == intersectionWest.getY() - 50 && roads.get(i).getX() == intersectionWest.getX()) { // Set stoplight of road to north of west intersection
				roads.get(i).setStopLightType(STOPLIGHTTYPE.VERTICALOFF);
			} else if(roads.get(i).getY() == intersectionWest.getY() + 25 && roads.get(i).getX() == intersectionWest.getX()) { // Set nextRoad of road to south of west intersection
				intersectionWest.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getX() == intersectionWest.getX() + 25 && roads.get(i).getY() == intersectionWest.getY()) { // Set nextRoad of road to east of west intersection
				roads.get(i).setNextRoad(intersectionWest);
				continue;
			} else if(roads.get(i).getX() == intersectionWest.getX() + 50 && roads.get(i).getY() == intersectionWest.getY()) { // Set stoplight of road to east of west intersection
				roads.get(i).setStopLightType(STOPLIGHTTYPE.HORIZONTALOFF);
			} else if(roads.get(i).getY() == intersectionSouth.getY() - 25 && roads.get(i).getX() == intersectionSouth.getX()) { // Set nextRoad of road to north of south intersection
				intersectionSouth.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getX() == intersectionSouth.getX() - 25 && roads.get(i).getY() == intersectionSouth.getY()) { // Set nextRoad of road to west of south intersection
				roads.get(i).setNextRoad(intersectionSouth);
				continue;
			} else if(roads.get(i).getX() == intersectionSouth.getX() - 50 && roads.get(i).getY() == intersectionSouth.getY()) { // Set stoplight of road to west of south intersection
				roads.get(i).setStopLightType(STOPLIGHTTYPE.HORIZONTALOFF);
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
				continue;
			} else if(roads.get(i).getY() == intersectionEast.getY() + 50 && roads.get(i).getX() == intersectionEast.getX()) { // Set stoplight of road to south of east intersection
				roads.get(i).setStopLightType(STOPLIGHTTYPE.VERTICALOFF);
			} else if(roads.get(i).getY() == intersectionCenter.getY() - 25 && roads.get(i).getX() == intersectionCenter.getX()) { // Set nextRoad of road to north of center intersection
				intersectionCenter.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getX() == intersectionCenter.getX() + 25 && roads.get(i).getY() == intersectionCenter.getY()) { // Set nextRoad of road to east of center intersection
				roads.get(i).setNextRoad(intersectionCenter);
				continue;
			} else if(roads.get(i).getX() == intersectionCenter.getX() + 50 && roads.get(i).getY() == intersectionCenter.getY()) { // Set stoplight of road to east of center intersection
				roads.get(i).setStopLightType(STOPLIGHTTYPE.HORIZONTALOFF);
			} else if(roads.get(i).getX() == intersectionCenter.getX() - 25 && roads.get(i).getY() == intersectionCenter.getY()) { // Set nextRoad of road to west of center intersection
				intersectionCenter.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getY() == intersectionCenter.getY() + 25 && roads.get(i).getX() == intersectionCenter.getX()) { // Set nextRoad of road to south of center intersection
				roads.get(i).setNextRoad(intersectionCenter);
				continue;
			} else if(roads.get(i).getY() == intersectionCenter.getY() + 50 && roads.get(i).getX() == intersectionCenter.getX()) { // Set stoplight of road to south of center intersection
				roads.get(i).setStopLightType(STOPLIGHTTYPE.VERTICALOFF);
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
		sidewalks.setRoads(trafficControl);
		
		// Bus Stops!!!!!!!!
		BusStopPanel bsp1 = new BusStopPanel(Color.white);
		CityViewBusStop cityViewBusStop1 = new CityViewBusStop(325, 125, "Bus Stop 1", Color.white, bsp1);
		BusStopBuilding busStop1 = new BusStopBuilding("Bus Stop 1", bsp1, cityViewBusStop1);
		setBuilding(bsp1, cityViewBusStop1, busStop1);

		BusStopPanel bsp2 = new BusStopPanel(Color.white);
		CityViewBusStop cityViewBusStop2 = new CityViewBusStop(125, 125, "Bus Stop 2", Color.white, bsp2);
		BusStopBuilding busStop2 = new BusStopBuilding("Bus Stop 2", bsp2, cityViewBusStop2);
		setBuilding(bsp2, cityViewBusStop2, busStop2);

		BusStopPanel bsp3 = new BusStopPanel(Color.white);
		CityViewBusStop cityViewBusStop3 = new CityViewBusStop(325, 325, "Bus Stop 3", Color.white, bsp3);
		BusStopBuilding busStop3 = new BusStopBuilding("Bus Stop 3", bsp3, cityViewBusStop3);
		setBuilding(bsp3, cityViewBusStop3, busStop3);

		BusStopPanel bsp4 = new BusStopPanel(Color.white);
		CityViewBusStop cityViewBusStop4 = new CityViewBusStop(125, 325, "Bus Stop 4", Color.white, bsp4);
		BusStopBuilding busStop4 = new BusStopBuilding("Bus Stop 4", bsp4, cityViewBusStop4);
		setBuilding(bsp4, cityViewBusStop4, busStop4);

		// Create buildings
		BankPanel bankPanel1 = new BankPanel(Color.green);
		CityViewBank cityViewBank1 = new CityViewBank(425, 200, "Bank " + mainFrame.cityView.getStaticsSize(), Color.green, bankPanel1);
		BankBuilding bankBuilding1 = new BankBuilding("BankBuilding", bankPanel1, cityViewBank1);
		setBuilding(bankPanel1, cityViewBank1, bankBuilding1);

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
		BusAnimation b1Anim = new BusAnimation(bus1, busStop1);
		bus1.setAnimation(b1Anim);
		mainFrame.cityView.addAnimation(b1Anim);
		CityMap.findClosestRoad(busStop1).setVehicle(b1Anim); 
		bus1.startThread();

		
		HousePanel unoccupiedHousePanel1 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529));
		CityViewHouse unoccupiedHouseView1 = new CityViewHouse(25, 25, "Unoccupied House", Color.BLUE, unoccupiedHousePanel1);
		HouseBuilding unoccupiedHouseBuilding = new HouseBuilding("Unoccupied House", null, unoccupiedHousePanel1, unoccupiedHouseView1);
		setBuilding(unoccupiedHousePanel1, unoccupiedHouseView1, unoccupiedHouseBuilding);
		//createBuilding(CityViewBuilding.BUILDINGTYPE.MARKET, 150, 125);
		
		RestaurantZhangBuilding rzb1 = (RestaurantZhangBuilding) createBuilding(CityViewBuilding.BUILDINGTYPE.RESTAURANTZHANG, 175, 125);
		
		createBuilding(CityViewBuilding.BUILDINGTYPE.HOUSE);
		createBuilding(CityViewBuilding.BUILDINGTYPE.APT);
		
//		HousePanel unoccupiedHousePanel1 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529));
//		CityViewHouse unoccupiedHouseView1 = new CityViewHouse(100, 100, "Unoccupied House", Color.BLUE, unoccupiedHousePanel1);
//		HouseBuilding unoccupiedHouseBuilding = new HouseBuilding("Unoccupied House", null, unoccupiedHousePanel1, unoccupiedHouseView1);
//		// createBuilding(unoccupiedHousePanel1, unoccupiedHouseView1, unoccupiedHouseBuilding);
//		
//		AptPanel unoccupiedAptPanel1 = new AptPanel(Color.getHSBColor((float)37, (float).53, (float).529));
//		CityViewApt unoccupiedAptView1 = new CityViewApt(100, 100, "Unoccupied Apt", Color.BLUE, unoccupiedAptPanel1);
//		AptBuilding unoccupiedAptBuilding = new AptBuilding("Unoccupied Apt", null, unoccupiedAptPanel1, unoccupiedAptView1);
//		// createBuilding(unoccupiedAptPanel1, unoccupiedAptView1, unoccupiedAptBuilding);
		

		//		HousePanel housePanelZhang1 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529));
		//		CityViewHouse cityViewHouseZhang1 = new CityViewHouse(150, 300, "Zhang Landlord House", Color.gray, housePanelZhang1);
		//		HouseBuilding houseBuildingZhang1 = new HouseBuilding("House 0 Zhang", null, housePanelZhang1, cityViewHouseZhang1);
		//		createBuilding(housePanelZhang1, cityViewHouseZhang1, houseBuildingZhang1);

		AptPanel apartmentPanelZhang1 = new AptPanel(Color.getHSBColor((float)200, (float).68, (float).399)); // this is now a house, because I just finished house.
		CityViewApt cityViewHouseZhang1 = new CityViewApt(275,275, "Zhang Landlord Apartment", Color.gray, apartmentPanelZhang1); 
		//if you want to see house animation, try (75,225) for location until #66 is fixed (: and uncomment lines 869, 874.
		//if you dont want this to block the road just move it to (325,325) or something
		AptBuilding apartmentBuildingZhang1 = new AptBuilding("House 0 Zhang", null, apartmentPanelZhang1, cityViewHouseZhang1);
		setBuilding(apartmentPanelZhang1, cityViewHouseZhang1, apartmentBuildingZhang1);

		// Create landlord
		PersonAgent p0Zhang = new PersonAgent("Landlord Zhang", date, new PersonAnimation(), apartmentBuildingZhang1);
		LandlordRole p0r1Zhang = new LandlordRole();
		p0Zhang.addRole(p0r1Zhang);
		apartmentBuildingZhang1.setLandlord(p0r1Zhang);
		p0r1Zhang.setActive();
		model.addPerson(p0Zhang);

		// Create people
		PersonAgent p1Zhang = new PersonAgent("Cashier 1 Zhang", date, new PersonAnimation(), apartmentBuildingZhang1);
		PersonAgent p2Zhang = new PersonAgent("Cook 1 Zhang", date, new PersonAnimation(), apartmentBuildingZhang1);
		PersonAgent p3Zhang = new PersonAgent("Host 1 Zhang", date, new PersonAnimation(), apartmentBuildingZhang1);
		PersonAgent p4Zhang = new PersonAgent("Waiter 1 Zhang", date, new PersonAnimation(), apartmentBuildingZhang1);
		p4Zhang.getAnimation().setCoords(apartmentBuildingZhang1.getCityViewBuilding().getX(), apartmentBuildingZhang1.getCityViewBuilding().getY());
		model.addPerson(p1Zhang);
		model.addPerson(p2Zhang);
		model.addPerson(p3Zhang);
		model.addPerson(p4Zhang);

		//Give people basically inf. food. NOTE, THAT I DID THIS AFTER setHome(). setHome() sets all foods to 1! can be changed
		HashMap<FOOD_ITEMS, Integer> temp = new HashMap<FOOD_ITEMS, Integer>();
		temp.put(FOOD_ITEMS.chicken, 500);
		temp.put(FOOD_ITEMS.salad, 500);
		temp.put(FOOD_ITEMS.pizza, 500);
		temp.put(FOOD_ITEMS.steak, 500);
		apartmentBuildingZhang1.setFood(p0Zhang, temp); // TODO we put 500 food in his fridge, so don't do that in release
		apartmentBuildingZhang1.setFood(p1Zhang, temp); // TODO we put 500 food in his fridge, so don't do that in release
		apartmentBuildingZhang1.setFood(p2Zhang, temp); // TODO we put 500 food in his fridge, so don't do that in release
		apartmentBuildingZhang1.setFood(p3Zhang, temp); // TODO we put 500 food in his fridge, so don't do that in release
		apartmentBuildingZhang1.setFood(p4Zhang, temp); // TODO we put 500 food in his fridge, so don't do that in release
		
		// Give people cars
				CarAgent c0Zhang = new CarAgent(busStop2,p0Zhang);
				CarAnimation c0AnimZhang = new CarAnimation(c0Zhang, busStop2);
				c0Zhang.setAnimation(c0AnimZhang);
				mainFrame.cityView.addAnimation(c0AnimZhang);
		//		CarAgent c1Zhang = new CarAgent(busStop2, p1Zhang);
		//		CarAnimation c1AnimZhang = new CarAnimation(c1Zhang, busStop2);
		//		c1Zhang .setAnimation(c1AnimZhang);
		//		mainFrame.cityView.addAnimation(c1AnimZhang);
		//		CarAgent c2Zhang = new CarAgent(busStop2, p2Zhang);
		//		CarAnimation c2AnimZhang = new CarAnimation(c2Zhang, busStop2);
		//		c2Zhang.setAnimation(c2AnimZhang);
		//		mainFrame.cityView.addAnimation(c2AnimZhang);
		//		CarAgent c3Zhang = new CarAgent(busStop2, p3Zhang);
		//		CarAnimation c3AnimZhang = new CarAnimation(c3Zhang, busStop2);
		//		c3Zhang.setAnimation(c3AnimZhang);
		//		mainFrame.cityView.addAnimation(c3AnimZhang);
		//		CarAgent c4Zhang = new CarAgent(busStop2, p4Zhang);
		//		CarAnimation c4AnimZhang = new CarAnimation(c4Zhang, busStop2);
		//		c4Zhang.setAnimation(c4AnimZhang);
		//		mainFrame.cityView.addAnimation(c4AnimZhang);

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
		CityViewRestaurant cvr1 = new CityViewRestaurant(275, 150, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.cyan, rtp1); 
		RestaurantTimmsBuilding rtb = new RestaurantTimmsBuilding("RestaurantTimms", rtp1, cvr1);
		setBuilding(rtp1, cvr1, rtb);

		// Create buildings
		AptPanel rhp1Timms = new AptPanel(Color.getHSBColor((float)37, (float).53, (float).529));
		CityViewApt rhcv1Timms = new CityViewApt(450, 425, "Apartment " + mainFrame.cityView.getStaticsSize(), Color.gray, rhp1Timms);
		AptBuilding rhb1Timms = new AptBuilding("Timms Apartment", null, rhp1Timms, rhcv1Timms);
		setBuilding(rhp1Timms, rhcv1Timms, rhb1Timms);

		// Create tables
		int i = 0;
		while (i < 9) {
			rtp1.addVisualizationElement(new RestaurantTimmsTableAnimation(i));
			i++;
		}

		// Create landlord
		PersonAgent p0Timms = new PersonAgent("Landlord Timms", date, new PersonAnimation(), rhb1Timms);
		LandlordRole p0r1Timms = new LandlordRole();
		p0Timms.addRole(p0r1Timms);
		rhb1Timms.setLandlord(p0r1Timms);
		p0Timms.setCash(10);
		p0r1Timms.setActive();
		model.addPerson(p0Timms);

		// Create people
		PersonAgent p1Timms = new PersonAgent("Cashier 1 Timms", date, new PersonAnimation(), rhb1Timms);
		PersonAgent p2Timms = new PersonAgent("Cook 1 Timms", date, new PersonAnimation(), rhb1Timms);
		PersonAgent p3Timms = new PersonAgent("Host 1 Timms", date, new PersonAnimation(), rhb1Timms);
		PersonAgent p4Timms = new PersonAgent("Waiter 1 Timms", date, new PersonAnimation(), rhb1Timms);
		model.addPerson(p1Timms);
		model.addPerson(p2Timms);
		model.addPerson(p3Timms);
		model.addPerson(p4Timms);

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


		// RESTAURANTCHOI----------------------------------------------------------------------------
		MarketBuilding marketBuildingChoi1 = (MarketBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.MARKET, 425, 325);
		RestaurantChoiBuilding restaurantChoiBuilding1 = (RestaurantChoiBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.RESTAURANTCHOI, 225, 425);

		AptPanel rhp1Choi = new AptPanel(Color.getHSBColor((float)37, (float).53, (float).529));
		CityViewApt rhcv1Choi = new CityViewApt(350, 425, "Apt " + mainFrame.cityView.getStaticsSize(), Color.gray, rhp1Choi);
		AptBuilding rhb1Choi = new AptBuilding("Apt Choi1", null, rhp1Choi, rhcv1Choi);
		setBuilding(rhp1Choi, rhcv1Choi, rhb1Choi);

		AptPanel rhp2Choi = new AptPanel(Color.getHSBColor((float)37, (float).53, (float).529));
		CityViewApt rhcv2Choi = new CityViewApt(375, 425, "Apt " + mainFrame.cityView.getStaticsSize(), Color.gray, rhp2Choi);
		AptBuilding rhb2Choi = new AptBuilding("Apt Choi2", null, rhp2Choi, rhcv2Choi);
		setBuilding(rhp2Choi, rhcv2Choi, rhb2Choi);

		AptPanel rhp3Choi = new AptPanel(Color.getHSBColor((float)37, (float).53, (float).529));
		CityViewApt rhcv3Choi = new CityViewApt(325, 425, "Apt " + mainFrame.cityView.getStaticsSize(), Color.gray, rhp3Choi);
		AptBuilding rhb3Choi = new AptBuilding("Apt Choi3", null, rhp3Choi, rhcv3Choi);
		setBuilding(rhp3Choi, rhcv3Choi, rhb3Choi);

		// Create landlord
		PersonAgent p0Choi = new PersonAgent("Landlord Choi", date, new PersonAnimation(), rhb3Choi);
		LandlordRole p0r1Choi = new LandlordRole();
		p0Choi.addRole(p0r1Choi);
		rhb1Choi.setLandlord(p0r1Choi);
		rhb2Choi.setLandlord(p0r1Choi);
		rhb3Choi.setLandlord(p0r1Choi);
		p0r1Choi.setActive();
		model.addPerson(p0Choi);
		p0Choi.setCash(30);
		
		// Create people
		
		PersonAgent p1Choi = new PersonAgent("Cashier 1 Choi", date, new PersonAnimation(), rhb1Choi);
		PersonAgent p2Choi = new PersonAgent("Cook 1 Choi", date, new PersonAnimation(),rhb1Choi);
		PersonAgent p3Choi = new PersonAgent("Host 1 Choi", date, new PersonAnimation(),rhb1Choi);
		PersonAgent p4Choi = new PersonAgent("Waiter 1 Choi", date, new PersonAnimation(), rhb1Choi);
		PersonAgent p5Choi = new PersonAgent("Market Mgr Choi#@@@@@@@@@@@@@@@@@@@", date, new PersonAnimation(),rhb1Choi);

		PersonAgent p6Choi = new PersonAgent("Market Cshr Choi", date, new PersonAnimation(), rhb2Choi);
		PersonAgent p7Choi = new PersonAgent("Market Emp Choi", date, new PersonAnimation(),rhb2Choi);
		PersonAgent p8Choi = new PersonAgent("Market Dlvry Choi", date, new PersonAnimation(),rhb2Choi);
		PersonAgent p9Choi = new PersonAgent("Bank manager Choi", date, new PersonAnimation(),rhb2Choi);
		PersonAgent p10Choi = new PersonAgent("Bank Teller Choi", date, new PersonAnimation(),rhb2Choi);

		model.addPerson(p1Choi);
		model.addPerson(p2Choi);
		model.addPerson(p3Choi);
		model.addPerson(p4Choi);
		model.addPerson(p5Choi);
		model.addPerson(p6Choi);
		model.addPerson(p7Choi);
		model.addPerson(p8Choi);
		model.addPerson(p9Choi);
		model.addPerson(p10Choi);

		// Landlord
		RestaurantChoiCashierRole p1r1Choi = new RestaurantChoiCashierRole(restaurantChoiBuilding1, 0, 24);
		restaurantChoiBuilding1.addOccupyingRole(p1r1Choi);
		p1Choi.setOccupation(p1r1Choi);

		// Create cook
		RestaurantChoiCookRole p2r1Choi = new RestaurantChoiCookRole(restaurantChoiBuilding1, 0, 24);
		restaurantChoiBuilding1.addOccupyingRole(p2r1Choi);
		p2Choi.setOccupation(p2r1Choi);
		p2r1Choi.addMarket(marketBuildingChoi1);

		// Create host
		RestaurantChoiHostRole p3r1Choi = new RestaurantChoiHostRole(restaurantChoiBuilding1, 0, 24);
		restaurantChoiBuilding1.addOccupyingRole(p3r1Choi);
		p3Choi.setOccupation(p3r1Choi);

		// Create waiter
		RestaurantChoiWaiterQueueRole p4r1Choi = new RestaurantChoiWaiterQueueRole(restaurantChoiBuilding1, 0, 24);
		restaurantChoiBuilding1.addOccupyingRole(p4r1Choi);
		p4Choi.setOccupation(p4r1Choi);

		//Create bank roles

		BankManagerRole p9r1Choi = new BankManagerRole(bankBuilding1, 0, 24);
		p9Choi.setOccupation(p9r1Choi);
		p9r1Choi.setPerson(p9Choi);
		BankTellerRole p10r1Choi = new BankTellerRole(bankBuilding1, 0, 24);
		p10Choi.setOccupation(p10r1Choi);
		p10r1Choi.setPerson(p10Choi);
		bankBuilding1.addOccupyingRole(p9r1Choi);
		bankBuilding1.addOccupyingRole(p10r1Choi);

		//Create Market people
		
		MarketManagerRole p5r1Choi = new MarketManagerRole(marketBuildingChoi1, 0, 24);
		MarketCashierRole p6r1Choi = new MarketCashierRole(marketBuildingChoi1, 0, 24);
		MarketEmployeeRole p7r1Choi = new MarketEmployeeRole(marketBuildingChoi1, 0, 24);
		MarketDeliveryPersonRole p8r1Choi = new MarketDeliveryPersonRole(marketBuildingChoi1, 0, 24);
		p5Choi.setOccupation(p5r1Choi);
		p5r1Choi.setPerson(p5Choi);
		p6Choi.setOccupation(p6r1Choi);
		p6r1Choi.setPerson(p6Choi);
		p7Choi.setOccupation(p7r1Choi);
		p7r1Choi.setPerson(p7Choi);
		p8r1Choi.setPerson(p8Choi);
		p8Choi.setOccupation(p8r1Choi);
		marketBuildingChoi1.addOccupyingRole(p5r1Choi);
		marketBuildingChoi1.addOccupyingRole(p6r1Choi);
		marketBuildingChoi1.addOccupyingRole(p7r1Choi);
		marketBuildingChoi1.addOccupyingRole(p8r1Choi);
		marketBuildingChoi1.setManager(p5r1Choi);
		marketBuildingChoi1.setCashier(p6r1Choi);
		marketBuildingChoi1.addEmployee(p7r1Choi);
		marketBuildingChoi1.addDeliveryPerson(p8r1Choi);

		//Give people cars
		CarAgent c0Choi = new CarAgent(busStop4, p0Choi);
		CarAnimation c0AnimChoi = new CarAnimation(c0Choi, busStop4);
		c0Choi.setAnimation(c0AnimChoi);
		mainFrame.cityView.addAnimation(c0AnimChoi);
		
		CarAgent c1Choi = new CarAgent(busStop4, p1Choi);
		CarAnimation c1AnimChoi = new CarAnimation(c1Choi, busStop4);
		c1Choi.setAnimation(c1AnimChoi);
		mainFrame.cityView.addAnimation(c1AnimChoi);
		CarAgent c2Choi = new CarAgent(busStop4, p2Choi);
		CarAnimation c2AnimChoi = new CarAnimation(c2Choi, busStop4);
		c2Choi.setAnimation(c2AnimChoi);
		mainFrame.cityView.addAnimation(c2AnimChoi);
		CarAgent c3Choi = new CarAgent(busStop4, p3Choi);
		CarAnimation c3AnimChoi = new CarAnimation(c3Choi, busStop4);
		c3Choi.setAnimation(c3AnimChoi);
		mainFrame.cityView.addAnimation(c3AnimChoi);
		CarAgent c4Choi = new CarAgent(busStop4, p4Choi);
		CarAnimation c4AnimChoi = new CarAnimation(c4Choi, busStop4);
		c4Choi.setAnimation(c4AnimChoi);
		mainFrame.cityView.addAnimation(c4AnimChoi);
		CarAgent c5Choi = new CarAgent(busStop4, p5Choi);
		CarAnimation c5AnimChoi = new CarAnimation(c5Choi, busStop4);
		c5Choi.setAnimation(c5AnimChoi);
		mainFrame.cityView.addAnimation(c5AnimChoi);
		CarAgent c6Choi = new CarAgent(busStop4, p6Choi);
		CarAnimation c6AnimChoi = new CarAnimation(c6Choi, busStop4);
		c6Choi.setAnimation(c6AnimChoi);
		mainFrame.cityView.addAnimation(c6AnimChoi);
		CarAgent c7Choi = new CarAgent(busStop4, p7Choi);
		CarAnimation c7AnimChoi = new CarAnimation(c7Choi, busStop4);
		c7Choi.setAnimation(c7AnimChoi);
		mainFrame.cityView.addAnimation(c7AnimChoi);
		CarAgent c8Choi = new CarAgent(busStop4, p8Choi);
		CarAnimation c8AnimChoi = new CarAnimation(c8Choi, busStop4);
		c8Choi.setAnimation(c8AnimChoi);
		mainFrame.cityView.addAnimation(c8AnimChoi);
		CarAgent c9Choi = new CarAgent(busStop4, p9Choi);
		CarAnimation c9AnimChoi = new CarAnimation(c9Choi, busStop4);
		c9Choi.setAnimation(c9AnimChoi);
		mainFrame.cityView.addAnimation(c9AnimChoi);
		CarAgent c10Choi = new CarAgent(busStop4, p10Choi);
		CarAnimation c10AnimChoi = new CarAnimation(c10Choi, busStop4);
		c10Choi.setAnimation(c10AnimChoi);
		mainFrame.cityView.addAnimation(c10AnimChoi);
		
		p0Choi.setCar(c0Choi);
		p1Choi.setCar(c1Choi);
		p2Choi.setCar(c2Choi);
		p3Choi.setCar(c3Choi);
		p4Choi.setCar(c4Choi);
		p5Choi.setCar(c5Choi);
		p6Choi.setCar(c6Choi);
		p7Choi.setCar(c7Choi);
		p8Choi.setCar(c8Choi);
		p9Choi.setCar(c9Choi);
		p10Choi.setCar(c10Choi);

		// RESTAURANTCHUNG------------------------------------------------------------------------------

		// RESTAURANTCHUNGTESTING FOR ANIMATION IN GUI
		RestaurantChungPanel restaurantChungPanel1 = new RestaurantChungPanel(Color.black);
		CityViewRestaurant cityViewRestaurantChung1 = new CityViewRestaurant(425, 150, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.yellow, restaurantChungPanel1); 
		RestaurantChungBuilding restaurantChungBuilding1 = new RestaurantChungBuilding("RestaurantChung1", restaurantChungPanel1, cityViewRestaurantChung1);
		setBuilding(restaurantChungPanel1, cityViewRestaurantChung1, restaurantChungBuilding1);
		
		AptPanel aptPanelChung1 = new AptPanel(Color.black);
		CityViewApt cityViewAptChung1 = new CityViewApt(425,250, "Chung Apartment" + (mainFrame.cityView.getStaticsSize()), Color.gray, aptPanelChung1);
		AptBuilding aptBuildingChung1 = new AptBuilding("Chung Apartment", null, aptPanelChung1, cityViewAptChung1);
		setBuilding(aptPanelChung1, cityViewAptChung1, aptBuildingChung1);


		// Create landlord
		PersonAgent p0Chung = new PersonAgent("Landlord Chung", date, new PersonAnimation(), aptBuildingChung1);
		p0Chung.setCash(50); // TODO remove later
		LandlordRole p0r1Chung = new LandlordRole();
		p0Chung.addRole(p0r1Chung);
		aptBuildingChung1.setLandlord(p0r1Chung);
		p0r1Chung.setActive();
		model.addPerson(p0Chung);

		// Create people
		PersonAgent p1Chung = new PersonAgent("Cashier 1 Chung", date, new PersonAnimation(), aptBuildingChung1);
		PersonAgent p2Chung = new PersonAgent("Cook 1 Chung", date, new PersonAnimation(), aptBuildingChung1);
		PersonAgent p3Chung = new PersonAgent("Host 1 Chung", date, new PersonAnimation(), aptBuildingChung1);
		PersonAgent p4Chung = new PersonAgent("Waiter 1 Chung", date, new PersonAnimation(), aptBuildingChung1);
		model.addPerson(p1Chung);
		model.addPerson(p2Chung);
		model.addPerson(p3Chung);
		model.addPerson(p4Chung);

		// Give people cars
		CarAgent c0Chung = new CarAgent(busStop1, p0Chung);
		CarAnimation c0AnimChung = new CarAnimation(c0Chung, busStop1);
		c0Chung.setAnimation(c0AnimChung);
		mainFrame.cityView.addAnimation(c0AnimChung);
		CarAgent c1Chung = new CarAgent(busStop1, p1Chung);
		CarAnimation c1AnimChung = new CarAnimation(c1Chung, busStop1);
		c1Chung.setAnimation(c1AnimChung);
		mainFrame.cityView.addAnimation(c1AnimChung);
		CarAgent c2Chung = new CarAgent(busStop1, p2Chung);
		CarAnimation c2AnimChung = new CarAnimation(c2Chung, busStop1);
		c2Chung.setAnimation(c2AnimChung);
		mainFrame.cityView.addAnimation(c2AnimChung);
		CarAgent c3Chung = new CarAgent(busStop1, p3Chung);
		CarAnimation c3AnimChung = new CarAnimation(c3Chung, busStop1);
		c3Chung.setAnimation(c3AnimChung);
		mainFrame.cityView.addAnimation(c3AnimChung);
		CarAgent c4Chung = new CarAgent(busStop1, p4Chung);
		CarAnimation c4AnimChung = new CarAnimation(c4Chung, busStop1);
		c4Chung.setAnimation(c4AnimChung);
		mainFrame.cityView.addAnimation(c4AnimChung);

		// Create cashier
		RestaurantChungCashierRole p1r1Chung = new RestaurantChungCashierRole(restaurantChungBuilding1, 0, 12);
		p1r1Chung.setPerson(p1Chung);
		p1r1Chung.setMarketCustomerDeliveryPaymentPerson();
		p1r1Chung.setBankCustomerPerson();
		restaurantChungBuilding1.addOccupyingRole(p1r1Chung);
		p1Chung.setOccupation(p1r1Chung);

		// Create cook
		RestaurantChungCookRole p2r1Chung = new RestaurantChungCookRole(restaurantChungBuilding1, 0, 12);
		p2r1Chung.setPerson(p2Chung);		
		restaurantChungBuilding1.addOccupyingRole(p2r1Chung);
		p2Chung.setOccupation(p2r1Chung);

		// Create host
		RestaurantChungHostRole p3r1Chung = new RestaurantChungHostRole(restaurantChungBuilding1, 0, 12);
		p3r1Chung.setPerson(p3Chung);		
		restaurantChungBuilding1.addOccupyingRole(p3r1Chung);
		p3Chung.setOccupation(p3r1Chung);

		// Create waiter
		RestaurantChungWaiterMessageCookRole p4r1Chung = new RestaurantChungWaiterMessageCookRole(restaurantChungBuilding1, 0, 12);
		p4r1Chung.setPerson(p4Chung);		
		restaurantChungBuilding1.addOccupyingRole(p4r1Chung);
		p4Chung.setOccupation(p4r1Chung);
		//
		//		//RESTAURANTJP------------------------------------------------------------------------
				RestaurantJPPanel restaurantJPPanel1 = new RestaurantJPPanel(Color.DARK_GRAY);
				CityViewRestaurant cityViewRestaurantJP1 = new CityViewRestaurant(425, 100, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.green, restaurantJPPanel1); 
				RestaurantJPBuilding restaurantJPBuilding1 = new RestaurantJPBuilding("RestaurantJP1", restaurantJPPanel1, cityViewRestaurantJP1);
				setBuilding(restaurantJPPanel1, cityViewRestaurantJP1, restaurantJPBuilding1);
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

		//		c0Zhang.startThread();
		//		c1Zhang.startThread();
		//		c2Zhang.startThread();
		//		c3Zhang.startThread();
		//		c4Zhang.startThread();
		//		p0Zhang.startThread();
		//		p1Zhang.startThread();
		//		p2Zhang.startThread();
		//		p3Zhang.startThread();
		//p4Zhang.startThread();
		//		c0Timms.startThread();
		//		c1Timms.startThread();
		//		c2Timms.startThread();
		//		c3Timms.startThread();
		//		c4Timms.startThread();
		//		p0Timms.startThread();
		//		p1Timms.startThread();
		//		p2Timms.startThread();
		//		p3Timms.startThread();
		//		p4Timms.startThread();


		p1Choi.startThread();
		p2Choi.startThread();
		p3Choi.startThread();
		p4Choi.startThread();
		p5Choi.startThread();
		p6Choi.startThread();
		p7Choi.startThread();
		p8Choi.startThread();
		//p9Choi.startThread();
		//p10Choi.startThread();
		
		c1Choi.startThread();
		c2Choi.startThread();
		c3Choi.startThread();
		c4Choi.startThread();
		c5Choi.startThread();
		c6Choi.startThread();
		c7Choi.startThread();
		c8Choi.startThread();
		c9Choi.startThread();
		c10Choi.startThread();
		c0Choi.startThread();
		p0Choi.startThread();
		
		
		/*
		c0Chung.startThread();
		c1Chung.startThread();
		c2Chung.startThread();
		c3Chung.startThread();
		c4Chung.startThread();

		p0Chung.startThread();
		p1Chung.startThread();
		p2Chung.startThread();
		p3Chung.startThread();
		p4Chung.startThread();*/
		//		p0JP1.startThread();
		//		p1JP.startThread();
		//		p2JP.startThread();
		//		p3JP.startThread();
		//		p4JP.startThread();
		//		c1JP.startThread();
		//		c2JP.startThread();
		//		c3JP.startThread();
		//		c4JP.startThread();
		//
		//	
		/*
		for(int j = 0; j < 70; j++) {
			WalkerAnimation testPersonAnimation = new WalkerAnimation(null, CityMap.findRandomBuilding(BUILDING.busStop), sidewalks);
			testPersonAnimation.setVisible(true);
			mainFrame.cityView.addAnimation(testPersonAnimation);
			testPersonAnimation.goToDestination(CityMap.findRandomBuilding(BUILDING.busStop));
		}*/
	}

	public static DataModel getModel() {
		return model;
	}

	public static MainFrame getMainFrame() {
		return mainFrame;
	}

	public static Date getDate() {
		return new Date(date.getTime());
	}
	
	public static void setBuilding(BuildingCard panel, CityViewBuilding cityView, Building building) {
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
		} else if(building.getClass().getName().contains("Apt")) {
			CityMap.addBuilding(BUILDING.apartment, building);
		}
	}

	public static Point findNextOpenLocation() {
		for(int i = 25; i < 450; i += 25) {
			for(int j = 25; j < 450; j += 25) {
				if(i == 25 && (j == 25 || j == 425))
					i+= 25;
				if(i == 425 && (j == 25 || j == 425))
					break;
				if(mainFrame.cityView.getBuildingAt(i, j) != null)
					continue;
				else
					return new Point(i, j);
			}
		}
		return null;
	}
	
	/*
	 * Creates a building at the next available location
	 */
	public static BuildingInterface createBuilding(CityViewBuilding.BUILDINGTYPE type) {
		Point nextLocation = findNextOpenLocation();
		if(nextLocation == null)
			return null;
		return createBuilding(type, (int)(nextLocation.getX()), (int)(nextLocation.getY()));
	}
	
	/*
	 * Creates a building at the given x and y coordinates. Can and will overlap buildings
	 */
	public static BuildingInterface createBuilding(CityViewBuilding.BUILDINGTYPE type, int x, int y) {
		CityViewBuilding cityViewBuilding;
		Building building;
		switch (type) {
		case APT:
			cityViewBuilding = new CityViewApt(x, y, "Apartment " + (mainFrame.cityView.statics.size()), Color.darkGray, new AptPanel(Color.darkGray));
			building = new AptBuilding("Apartment " + mainFrame.cityView.statics.size(), null, (AptPanel)cityViewBuilding.getBuilding(), cityViewBuilding);
			setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
			return building;
		case HOUSE:
			cityViewBuilding = new CityViewHouse(x, y, "House " + (mainFrame.cityView.statics.size()), Color.pink, new HousePanel(Color.pink));
			building = new HouseBuilding("House " + mainFrame.cityView.statics.size(), null, (HousePanel)cityViewBuilding.getBuilding(), cityViewBuilding);
			setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
			return building;
		case RESTAURANTZHANG:
			cityViewBuilding = new CityViewRestaurant(x, y, "RestaurantZhang " + (mainFrame.cityView.statics.size()), Color.magenta, new RestaurantZhangPanel(Color.magenta));
			building = new RestaurantZhangBuilding("RestaurantZhang " + mainFrame.cityView.statics.size(), (RestaurantZhangPanel)cityViewBuilding.getBuilding(), cityViewBuilding);
			setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
			return building;
		case RESTAURANTCHOI:
			cityViewBuilding = new CityViewRestaurant(x, y, "RestaurantChoi " + mainFrame.cityView.statics.size(), Color.cyan, new RestaurantChoiPanel(Color.cyan));
			building = new RestaurantChoiBuilding("RestaurantChoi " + mainFrame.cityView.statics.size(),
					(RestaurantChoiPanel)(cityViewBuilding.getBuilding()), cityViewBuilding);
			setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
			return building;
		case RESTAURANTJP:
			cityViewBuilding = new CityViewRestaurant(x, y, "RestaurantJP " + mainFrame.cityView.statics.size(), Color.orange, new RestaurantJPPanel(Color.orange));
			building = new RestaurantJPBuilding("RestaurantJP " + mainFrame.cityView.statics.size(),
					(RestaurantJPPanel)(cityViewBuilding.getBuilding()), cityViewBuilding);
			setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
			return building;
		case RESTAURANTTIMMS:
			cityViewBuilding = new CityViewRestaurant(x, y, "RestaurantTimms " + mainFrame.cityView.statics.size(), Color.yellow, new RestaurantTimmsPanel(Color.yellow));
			building = new RestaurantTimmsBuilding("RestaurantTimms " + mainFrame.cityView.statics.size(),
					(RestaurantTimmsPanel)(cityViewBuilding.getBuilding()), cityViewBuilding);
			setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
			return building;
		case RESTAURANTCHUNG:
			cityViewBuilding = new CityViewRestaurant(x, y, "RestaurantChung " + mainFrame.cityView.statics.size(), Color.red, new RestaurantChungPanel(Color.red));
			building = new RestaurantChungBuilding("RestaurantChung " + mainFrame.cityView.statics.size(),
					(RestaurantChungPanel)(cityViewBuilding.getBuilding()), cityViewBuilding);
			setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
			return building;
		case BANK: 
			cityViewBuilding = new CityViewBank(x, y, "Bank " + mainFrame.cityView.statics.size(), Color.green, new BankPanel(Color.green));
			building = new BankBuilding("Bank " + mainFrame.cityView.statics.size(),
					(BankPanel)(cityViewBuilding.getBuilding()), cityViewBuilding);
			setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
			return building;
		case MARKET: 
			cityViewBuilding = new CityViewMarket(x, y, "Market " + mainFrame.cityView.statics.size(), Color.LIGHT_GRAY, new MarketPanel(Color.LIGHT_GRAY));
			building = new MarketBuilding("Market " + mainFrame.cityView.statics.size(),
					(MarketPanel)(cityViewBuilding.getBuilding()), cityViewBuilding);
			setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
			return building;
		default:
			return null;
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
//			int x  = 100;
//			int y = 100;
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

		/**
		 * 
		 * @param b
		 * @return
		 */
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

		/**
		 * Clears the map of all buildings.
		 * Convenience function for tests.
		 */
		public static void clearMap() {
			map.clear();
		}
		
		/**
		 * Returns a list of all buildings in the CityMap
		 */
		public static ArrayList<BuildingInterface> getBuildings() {
			ArrayList<BuildingInterface> list = new ArrayList<BuildingInterface>();
			for (List<BuildingInterface> l : map.values()) {
				list.addAll(l);
			}
			return list;
		}
	}
}
