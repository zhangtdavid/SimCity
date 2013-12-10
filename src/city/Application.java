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
import city.animations.WalkerAnimation;
import city.bases.Building;
import city.bases.interfaces.BuildingInterface;
import city.bases.interfaces.JobRoleInterface;
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
import city.gui.exteriors.CityViewRestaurantChoi;
import city.gui.exteriors.CityViewRestaurantChung;
import city.gui.exteriors.CityViewRestaurantJP;
import city.gui.exteriors.CityViewRestaurantTimms;
import city.gui.exteriors.CityViewRestaurantZhang;
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
import city.roles.RestaurantChungWaiterRevolvingStandRole;
import city.roles.RestaurantTimmsCashierRole;
import city.roles.RestaurantTimmsCookRole;
import city.roles.RestaurantTimmsHostRole;
import city.roles.RestaurantTimmsWaiterRole;
import city.roles.RestaurantZhangCashierRole;
import city.roles.RestaurantZhangCookRole;
import city.roles.RestaurantZhangHostRole;
import city.roles.RestaurantZhangWaiterSharedDataRole;
import city.roles.interfaces.MarketCashier;
import city.roles.interfaces.MarketDeliveryPerson;
import city.roles.interfaces.MarketEmployee;
import city.roles.interfaces.MarketManager;

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
		sidewalks = new CitySidewalkLayout(mainFrame, 30, 30, 50, 50, 12.5, Color.orange, nonSidewalkArea, trafficControl);
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

		createBuilding(CityViewBuilding.BUILDINGTYPE.MARKET, 150, 125);

		RestaurantZhangBuilding rzb1 = (RestaurantZhangBuilding) createBuilding(CityViewBuilding.BUILDINGTYPE.RESTAURANTZHANG, 175, 125);

		createBuilding(CityViewBuilding.BUILDINGTYPE.HOUSE);
		createBuilding(CityViewBuilding.BUILDINGTYPE.APT);


//		PersonAgent p0Timms = new PersonAgent("Landlord Timms", date, new PersonAnimation(), rhb1Timms);
//		LandlordRole p0r1Timms = new LandlordRole();
//		p0Timms.addRole(p0r1Timms);
//		rhb1Timms.setLandlord(p0r1Timms);
//		p0Timms.setCash(10);
//		p0r1Timms.setActive();
//		model.addPerson(p0Timms);
//
//		// Create people
//		PersonAgent p1Timms = new PersonAgent("Cashier 1 Timms", date, new PersonAnimation(), rhb1Timms);
//		PersonAgent p2Timms = new PersonAgent("Cook 1 Timms", date, new PersonAnimation(), rhb1Timms);
//		PersonAgent p3Timms = new PersonAgent("Host 1 Timms", date, new PersonAnimation(), rhb1Timms);
//		PersonAgent p4Timms = new PersonAgent("Waiter 1 Timms", date, new PersonAnimation(), rhb1Timms);
//		model.addPerson(p1Timms);
//		model.addPerson(p2Timms);
//		model.addPerson(p3Timms);
//		model.addPerson(p4Timms);
//
//		// Give people cars
//		CarAgent c0Timms = new CarAgent(busStop3, p0Timms);
//		CarAnimation c0AnimTimms = new CarAnimation(c0Timms, busStop3);
//		c0Timms.setAnimation(c0AnimTimms);
//		mainFrame.cityView.addAnimation(c0AnimTimms);
//		CarAgent c1Timms = new CarAgent(busStop3, p1Timms);
//		CarAnimation c1AnimTimms = new CarAnimation(c1Timms, busStop3);
//		c1Timms.setAnimation(c1AnimTimms);
//		mainFrame.cityView.addAnimation(c1AnimTimms);
//		CarAgent c2Timms = new CarAgent(busStop3, p2Timms);
//		CarAnimation c2AnimTimms = new CarAnimation(c2Timms, busStop3);
//		c2Timms.setAnimation(c2AnimTimms);
//		mainFrame.cityView.addAnimation(c2AnimTimms);
//		CarAgent c3Timms = new CarAgent(busStop3, p3Timms);
//		CarAnimation c3AnimTimms = new CarAnimation(c3Timms, busStop3);
//		c3Timms.setAnimation(c3AnimTimms);
//		mainFrame.cityView.addAnimation(c3AnimTimms);
//		CarAgent c4Timms = new CarAgent(busStop3, p4Timms);
//		CarAnimation c4AnimTimms = new CarAnimation(c4Timms, busStop3);
//		c4Timms.setAnimation(c4AnimTimms);
//		mainFrame.cityView.addAnimation(c4AnimTimms);
//
//		// Create cashier
//		RestaurantTimmsCashierRole p1r1Timms = new RestaurantTimmsCashierRole(rtb, 0, 100);
//		rtb.addOccupyingRole(p1r1Timms);
//		p1Timms.setOccupation(p1r1Timms);
//
//		// Create cook
//		RestaurantTimmsCookRole p2r1Timms = new RestaurantTimmsCookRole(rtb, 0, 100);
//		rtb.addOccupyingRole(p2r1Timms);
//		p2Timms.setOccupation(p2r1Timms);
//
//		// Create host
//		RestaurantTimmsHostRole p3r1Timms = new RestaurantTimmsHostRole(rtb, 0, 100);
//		rtb.addOccupyingRole(p3r1Timms);
//		p3Timms.setOccupation(p3r1Timms);
//
//		// Create waiter
//		RestaurantTimmsWaiterRole p4r1Timms = new RestaurantTimmsWaiterRole(rtb, 0, 100);
//		rtb.addOccupyingRole(p4r1Timms);
//		p4Timms.setOccupation(p4r1Timms);



		// RESTAURANTCHUNG------------------------------------------------------------------------------
		RestaurantChungPanel restaurantChungPanel1 = new RestaurantChungPanel(Color.black);
		CityViewRestaurantChung cityViewRestaurantChung1 = new CityViewRestaurantChung(425, 150, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.yellow, restaurantChungPanel1); 
		RestaurantChungBuilding restaurantChungBuilding1 = new RestaurantChungBuilding("RestaurantChung1", restaurantChungPanel1, cityViewRestaurantChung1);
		setBuilding(restaurantChungPanel1, cityViewRestaurantChung1, restaurantChungBuilding1);
		
		AptPanel aptPanelChung1 = new AptPanel(Color.black);
		CityViewApt cityViewAptChung1 = new CityViewApt(425,250, "Chung Apartment" + (mainFrame.cityView.getStaticsSize()), Color.gray, aptPanelChung1);
		AptBuilding aptBuildingChung1 = new AptBuilding("Chung Apartment", null, aptPanelChung1, cityViewAptChung1);
		setBuilding(aptPanelChung1, cityViewAptChung1, aptBuildingChung1);

		// BANK------------------------------------------------------------------------------
		BankPanel bankPanel11 = new BankPanel(Color.black);
		CityViewBank cityViewBank11 = new CityViewBank(425, 100, "Bank " + (mainFrame.cityView.getStaticsSize()), Color.yellow, bankPanel11); 
		BankBuilding bankBuilding11 = new BankBuilding("Bank 1", bankPanel11, cityViewBank11);
		setBuilding(bankPanel11, cityViewBank11, bankBuilding11);
		
		
		
		// MARKET------------------------------------------------------------------------------
		MarketPanel marketPanel1 = new MarketPanel(Color.black);
		CityViewMarket cityViewMarket1 = new CityViewMarket(425, 125, "Market " + (mainFrame.cityView.getStaticsSize()), Color.yellow, marketPanel1); 
		MarketBuilding marketBuilding1 = new MarketBuilding("Market 1", marketPanel1, cityViewMarket1);
		setBuilding(marketPanel1, cityViewMarket1, marketBuilding1);
		
		
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
//		p4Zhang.startThread();
		//              c0Timms.startThread();
		//              c1Timms.startThread();
		//              c2Timms.startThread();
		//              c3Timms.startThread();
		//              c4Timms.startThread();
		//              p0Timms.startThread();
		//              p1Timms.startThread();
		//              p2Timms.startThread();
		//              p3Timms.startThread();
		//              p4Timms.startThread();
		//              c0Choi.startThread();
		//              p0Choi.startThread();
		//              p1Choi.startThread();
		//              p2Choi.startThread();
		//              p3Choi.startThread();
		//              p4Choi.startThread();
		//              p5Choi.startThread();
		//              p6Choi.startThread();
		//              p7Choi.startThread();
		//              p8Choi.startThread();
		//              p9Choi.startThread();
		//              p10Choi.startThread();
		//              c1Choi.startThread();
		//              c2Choi.startThread();
		//              c3Choi.startThread();
		//              c4Choi.startThread();
		//              c5Choi.startThread();
		//              c6Choi.startThread();
		//              c7Choi.startThread();
		//              c8Choi.startThread();
		//              c9Choi.startThread();
		//              c10Choi.startThread();
		//              c0Chung.startThread();
		//              c1Chung.startThread();
		//              c2Chung.startThread();
		//              c3Chung.startThread();
		//              c4Chung.startThread();
		//              p0Chung.startThread();
		//              p1Chung.startThread();
		//              p2Chung.startThread();
		//              p3Chung.startThread();
		//              p4Chung.startThread();
		//              p0JP1.startThread();
		//              p1JP.startThread();
		//              p2JP.startThread();
		//              p3JP.startThread();
		//              p4JP.startThread();
		//              c1JP.startThread();
		//              c2JP.startThread();
		//              c3JP.startThread();
		//              c4JP.startThread();
		//             
//		for(int j = 0; j < 700; j++) {
//			WalkerAnimation testPersonAnimation = new WalkerAnimation(null, CityMap.findRandomBuilding(BUILDING.busStop), sidewalks);
//			testPersonAnimation.setVisible(true);
//			mainFrame.cityView.addAnimation(testPersonAnimation);
//			testPersonAnimation.goToDestination(CityMap.findRandomBuilding(BUILDING.busStop));
//		}
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
			cityViewBuilding = new CityViewRestaurantZhang(x, y, "RestaurantZhang " + (mainFrame.cityView.statics.size()), Color.magenta, new RestaurantZhangPanel(Color.magenta));
			building = new RestaurantZhangBuilding("RestaurantZhang " + mainFrame.cityView.statics.size(), (RestaurantZhangPanel)cityViewBuilding.getBuilding(), cityViewBuilding);
			setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
			return building;
		case RESTAURANTCHOI:
			cityViewBuilding = new CityViewRestaurantChoi(x, y, "RestaurantChoi " + mainFrame.cityView.statics.size(), Color.cyan, new RestaurantChoiPanel(Color.cyan));
			building = new RestaurantChoiBuilding("RestaurantChoi " + mainFrame.cityView.statics.size(),
					(RestaurantChoiPanel)(cityViewBuilding.getBuilding()), cityViewBuilding);
			setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
			return building;
		case RESTAURANTJP:
			cityViewBuilding = new CityViewRestaurantJP(x, y, "RestaurantJP " + mainFrame.cityView.statics.size(), Color.orange, new RestaurantJPPanel(Color.orange));
			building = new RestaurantJPBuilding("RestaurantJP " + mainFrame.cityView.statics.size(),
					(RestaurantJPPanel)(cityViewBuilding.getBuilding()), cityViewBuilding);
			setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
			return building;
		case RESTAURANTTIMMS:
			cityViewBuilding = new CityViewRestaurantTimms(x, y, "RestaurantTimms " + mainFrame.cityView.statics.size(), Color.yellow, new RestaurantTimmsPanel(Color.yellow));
			building = new RestaurantTimmsBuilding("RestaurantTimms " + mainFrame.cityView.statics.size(),
					(RestaurantTimmsPanel)(cityViewBuilding.getBuilding()), cityViewBuilding);
			setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
			return building;
		case RESTAURANTCHUNG:
			cityViewBuilding = new CityViewRestaurantChung(x, y, "RestaurantChung " + mainFrame.cityView.statics.size(), Color.red, new RestaurantChungPanel(Color.red));
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
			building = new MarketBuilding("Bank " + mainFrame.cityView.statics.size(),
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
			//                      int x  = 100;
			//                      int y = 100;
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
