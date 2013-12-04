package city;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import city.agents.BusAgent;
import city.agents.CarAgent;
import city.agents.PersonAgent;
import city.animations.BusAnimation;
import city.animations.CarAnimation;
import city.animations.RestaurantTimmsTableAnimation;
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
import city.gui.CityRoadIntersection;
import city.gui.MainFrame;
import city.gui.buildings.BankPanel;
import city.gui.buildings.BusStopPanel;
import city.gui.buildings.HousePanel;
import city.gui.buildings.MarketPanel;
import city.gui.buildings.RestaurantChoiPanel;
import city.gui.buildings.RestaurantChungPanel;
import city.gui.buildings.RestaurantJPPanel;
import city.gui.buildings.RestaurantTimmsPanel;
import city.gui.buildings.RestaurantZhangPanel;
import city.gui.views.CityViewBank;
import city.gui.views.CityViewBuilding;
import city.gui.views.CityViewBusStop;
import city.gui.views.CityViewHouse;
import city.gui.views.CityViewMarket;
import city.gui.views.CityViewRestaurant;
import city.interfaces.Person;
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



public class Application {

	private static MainFrame mainFrame;
	private static List<Person> people = new ArrayList<Person>();
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

	/**
	 * Main routine to start the program.
	 * 
	 * When the program is started, this is the first call. It opens the GUI window, loads
	 * configuration files, and causes the program to run.
	 */
	public static void main(String[] args) {
		// Open the animation GUI
		mainFrame = new MainFrame();

		// Load a scenario
		parseConfig();

		// Start the simulation
		TimerTask tt = new TimerTask() {
			public void run() {
				date.setTime(date.getTime() + HALF_HOUR);
				for (Person p : people) {
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
		for(int i = 375; i >= 125; i -= 25) {
			if(i == 225)
				continue;
			CityRoad tempRoad = new CityRoad(i, 100, 25, 25, -1, 0, true, Color.black);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		// West roads
		for(int i = 100; i <= 300; i+=25) {
			if(i == 225)
				continue;
			CityRoad tempRoad = new CityRoad(100, i, 25, 25, 0, 1, false, Color.black);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		// South roads
		for(int i = 100; i <= 350; i+=25) {
			if(i == 225)
				continue;
			CityRoad tempRoad = new CityRoad(i, 325, 25, 25, 1, 0, true, Color.black);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		// East roads
		for(int i = 325; i >= 125; i-=25) {
			if(i == 225)
				continue;
			CityRoad tempRoad = new CityRoad(375, i, 25, 25, 0, -1, false, Color.black);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		// North/South middle roads
		for(int i = 300; i >= 125; i-=25) {
			if(i == 225)
				continue;
			CityRoad tempRoad = new CityRoad(225, i, 25, 25, 0, -1, false, Color.red);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		// East/West middle roads
		for(int i = 350; i >= 125; i -= 25) {
			if(i == 225)
				continue;
			CityRoad tempRoad = new CityRoad(i, 225, 25, 25, -1, 0, true, Color.orange);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		// North intersection
		CityRoadIntersection intersectionNorth = new CityRoadIntersection(225, 100, 25, 25, Color.gray);
		roads.add(intersectionNorth);
		mainFrame.cityView.addMoving(intersectionNorth);
		// West intersection
		CityRoadIntersection intersectionWest = new CityRoadIntersection(100, 225, 25, 25, Color.gray);
		roads.add(intersectionWest);
		mainFrame.cityView.addMoving(intersectionWest);
		// South intersection
		CityRoadIntersection intersectionSouth = new CityRoadIntersection(225, 325, 25, 25, Color.gray);
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
			} else if(roads.get(i).getY() == intersectionNorth.getY() + 25 && roads.get(i).getX() == intersectionNorth.getX()) { // Set nextRoad of road to south of north intersection
				roads.get(i).setNextRoad(intersectionNorth);
				continue;
			} else if(roads.get(i).getX() == intersectionNorth.getX() - 25 && roads.get(i).getY() == intersectionNorth.getY()) { // Set nextRoad of road to west of north intersection
				intersectionNorth.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getY() == intersectionWest.getY() - 25 && roads.get(i).getX() == intersectionWest.getX()) { // Set nextRoad of road to north of west intersection
				roads.get(i).setNextRoad(intersectionWest);
				continue;
			} else if(roads.get(i).getY() == intersectionWest.getY() + 25 && roads.get(i).getX() == intersectionWest.getX()) { // Set nextRoad of road to south of west intersection
				intersectionWest.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getX() == intersectionWest.getX() + 25 && roads.get(i).getY() == intersectionWest.getY()) { // Set nextRoad of road to east of west intersection
				roads.get(i).setNextRoad(intersectionWest);
				continue;
			} else if(roads.get(i).getY() == intersectionSouth.getY() - 25 && roads.get(i).getX() == intersectionSouth.getX()) { // Set nextRoad of road to north of south intersection
				intersectionSouth.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getX() == intersectionSouth.getX() - 25 && roads.get(i).getY() == intersectionSouth.getY()) { // Set nextRoad of road to west of south intersection
				roads.get(i).setNextRoad(intersectionSouth);
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
				continue;
			} else if(roads.get(i).getY() == intersectionCenter.getY() - 25 && roads.get(i).getX() == intersectionCenter.getX()) { // Set nextRoad of road to north of center intersection
				intersectionCenter.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getX() == intersectionCenter.getX() + 25 && roads.get(i).getY() == intersectionCenter.getY()) { // Set nextRoad of road to east of center intersection
				roads.get(i).setNextRoad(intersectionCenter);
				continue;
			} else if(roads.get(i).getX() == intersectionCenter.getX() - 25 && roads.get(i).getY() == intersectionCenter.getY()) { // Set nextRoad of road to west of center intersection
				intersectionCenter.setNextRoad(roads.get(i));
				roads.get(i).setNextRoad(roads.get(i + 1));
				continue;
			} else if(roads.get(i).getY() == intersectionCenter.getY() + 25 && roads.get(i).getX() == intersectionCenter.getX()) { // Set nextRoad of road to south of center intersection
				roads.get(i).setNextRoad(intersectionCenter);
				continue;
			} else if(roads.get(i).getX() == 375 && roads.get(i).getY() == 125) { // Last road in the outer loop
				roads.get(i).setNextRoad(roads.get(0));
				continue;
			}
			// Straight road
			if(roads.get(i).getClass() != CityRoadIntersection.class)
				roads.get(i).setNextRoad(roads.get(i+1));
		}

		// Bus Stops!!!!!!!!
		BusStopPanel bsp1 = new BusStopPanel(Color.white);
		CityViewBusStop cityViewBusStop1 = new CityViewBusStop(250, 50, "Bus Stop " + (mainFrame.cityView.getStaticsSize()), Color.white, bsp1);
		mainFrame.cityView.addStatic(cityViewBusStop1);
		BusStopBuilding busStop1 = new BusStopBuilding("Bus Stop 1", bsp1, cityViewBusStop1);
		mainFrame.buildingView.addView(bsp1, cityViewBusStop1.getID());
		Application.CityMap.addBuilding(BUILDING.busStop, busStop1);

		BusStopPanel bsp2 = new BusStopPanel(Color.white);
		CityViewBusStop cityViewBusStop2 = new CityViewBusStop(50, 300, "Bus Stop " + (mainFrame.cityView.getStaticsSize()), Color.white, bsp2);
		mainFrame.cityView.addStatic(cityViewBusStop2);
		BusStopBuilding busStop2 = new BusStopBuilding("Bus Stop 2", bsp2, cityViewBusStop2);
		mainFrame.buildingView.addView(bsp2, cityViewBusStop2.getID());
		Application.CityMap.addBuilding(BUILDING.busStop, busStop2); 

		BusStopPanel bsp3 = new BusStopPanel(Color.white);
		CityViewBusStop cityViewBusStop3 = new CityViewBusStop(250, 250, "Bus Stop " + (mainFrame.cityView.getStaticsSize()), Color.white, bsp3);
		mainFrame.cityView.addStatic(cityViewBusStop3);
		BusStopBuilding busStop3 = new BusStopBuilding("Bus Stop 3", bsp3, cityViewBusStop3);
		mainFrame.buildingView.addView(bsp3, cityViewBusStop3.getID());
		Application.CityMap.addBuilding(BUILDING.busStop, busStop3); 

		BusStopPanel bsp4 = new BusStopPanel(Color.white);
		CityViewBusStop cityViewBusStop4 = new CityViewBusStop(400, 150, "Bus Stop " + (mainFrame.cityView.getStaticsSize()), Color.white, bsp4);
		mainFrame.cityView.addStatic(cityViewBusStop4);
		BusStopBuilding busStop4 = new BusStopBuilding("Bus Stop 4", bsp4, cityViewBusStop4);
		mainFrame.buildingView.addView(bsp4, cityViewBusStop4.getID());
		Application.CityMap.addBuilding(BUILDING.busStop, busStop4 );
		
		// Create buildings
		BankPanel bankPanel1 = new BankPanel(Color.green);
		CityViewBank cityViewBank1 = new CityViewBank(400, 200, "Bank " + mainFrame.cityView.getStaticsSize(), Color.green, bankPanel1);
		BankBuilding bankBuilding1 = new BankBuilding("BankBuilding", bankPanel1, cityViewBank1);
		Application.CityMap.addBuilding(BUILDING.bank, bankBuilding1);

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

		// RESTAURANTZHANG------------------------------------------------------------
		RestaurantZhangPanel restaurantZhangPanel1 = new RestaurantZhangPanel(Color.DARK_GRAY);
		CityViewRestaurant cityViewRestaurantZhang1 = new CityViewRestaurant(100, 50, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.magenta, restaurantZhangPanel1);
		RestaurantZhangBuilding rzb1 = new RestaurantZhangBuilding("RestaurantZhang1", restaurantZhangPanel1, cityViewRestaurantZhang1);
		restaurantZhangPanel1.setTables(rzb1.tables);
		createBuilding(restaurantZhangPanel1, cityViewRestaurantZhang1, rzb1);

		HousePanel housePanelZhang1 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529));
		CityViewHouse cityViewHouseZhang1 = new CityViewHouse(150, 300, "Zhang Landlord House", Color.gray, housePanelZhang1);
		HouseBuilding houseBuildingZhang1 = new HouseBuilding("House 0 Zhang", null, housePanelZhang1, cityViewHouseZhang1);
		createBuilding(housePanelZhang1, cityViewHouseZhang1, houseBuildingZhang1);
		
		// Create landlord
		PersonAgent p0Zhang = new PersonAgent("Landlord Zhang", date);
		LandlordRole p0r1Zhang = new LandlordRole();
		p0Zhang.addRole(p0r1Zhang);
		houseBuildingZhang1.setLandlord(p0r1Zhang);
		p0Zhang.setHome(houseBuildingZhang1);
		p0r1Zhang.setActive();
		people.add(p0Zhang);

		// Create people
		PersonAgent p1Zhang = new PersonAgent("Cashier 1 Zhang", date);
		PersonAgent p2Zhang = new PersonAgent("Cook 1 Zhang", date);
		PersonAgent p3Zhang = new PersonAgent("Host 1 Zhang", date);
		PersonAgent p4Zhang = new PersonAgent("Waiter 1 Zhang", date);
		people.add(p1Zhang);
		people.add(p2Zhang);
		people.add(p3Zhang);
		people.add(p4Zhang);
		p1Zhang.setHome(houseBuildingZhang1);
		p2Zhang.setHome(houseBuildingZhang1);
		p3Zhang.setHome(houseBuildingZhang1);
		p4Zhang.setHome(houseBuildingZhang1);

		// Give people cars
		CarAgent c0Zhang = new CarAgent(busStop2);
		CarAnimation c0AnimZhang = new CarAnimation(c0Zhang, busStop2);
		c0Zhang.setAnimation(c0AnimZhang);
		mainFrame.cityView.addAnimation(c0AnimZhang);
		CarAgent c1Zhang = new CarAgent(busStop2);
		CarAnimation c1AnimZhang = new CarAnimation(c1Zhang, busStop2);
		c1Zhang .setAnimation(c1AnimZhang);
		mainFrame.cityView.addAnimation(c1AnimZhang);
		CarAgent c2Zhang = new CarAgent(busStop2);
		CarAnimation c2AnimZhang = new CarAnimation(c2Zhang, busStop2);
		c2Zhang.setAnimation(c2AnimZhang);
		mainFrame.cityView.addAnimation(c2AnimZhang);
		CarAgent c3Zhang = new CarAgent(busStop2);
		CarAnimation c3AnimZhang = new CarAnimation(c3Zhang, busStop2);
		c3Zhang.setAnimation(c3AnimZhang);
		mainFrame.cityView.addAnimation(c3AnimZhang);
		CarAgent c4Zhang = new CarAgent(busStop2);
		CarAnimation c4AnimZhang = new CarAnimation(c4Zhang, busStop2);
		c4Zhang.setAnimation(c4AnimZhang);
		mainFrame.cityView.addAnimation(c4AnimZhang);
		//p0.setCar(c0);
		p1Zhang.setCar(c1Zhang);
		p2Zhang.setCar(c2Zhang);
		p3Zhang.setCar(c3Zhang);
		p4Zhang.setCar(c4Zhang);

		// Create cashier
		RestaurantZhangCashierRole p1r1Zhang = new RestaurantZhangCashierRole(rzb1, 0, 100); // TODO Change shift times
		rzb1.addOccupyingRole(p1r1Zhang);
		p1Zhang.setOccupation(p1r1Zhang);

		// Create cook
		RestaurantZhangCookRole p2r1Zhang = new RestaurantZhangCookRole(rzb1, 0, 100); // TODO Change shift times
		rzb1.addOccupyingRole(p2r1Zhang);
		p2Zhang.setOccupation(p2r1Zhang);

		// Create host
		RestaurantZhangHostRole p3r1Zhang = new RestaurantZhangHostRole(rzb1, 0, 100); // TODO Change shift times
		rzb1.addOccupyingRole(p3r1Zhang);
		p3Zhang.setOccupation(p3r1Zhang);

		// Create waiter
		RestaurantZhangWaiterSharedDataRole p4r1Zhang = new RestaurantZhangWaiterSharedDataRole(rzb1, 0, 100); // TODO Change shift times
		rzb1.addOccupyingRole(p4r1Zhang);
		p4Zhang.setOccupation(p4r1Zhang);
		
		// Wait for stuff to get set up
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {}

		// Start threads for RestaurantZhang
		c0Zhang.startThread();
		c1Zhang.startThread();
		c2Zhang.startThread();
		c3Zhang.startThread();
		c4Zhang.startThread();
		p0Zhang.startThread();
		p1Zhang.startThread();
		p2Zhang.startThread();
		p3Zhang.startThread();
		p4Zhang.startThread();
	}

	public static void createBuilding(BuildingCard panel, CityViewBuilding cityView, Building building) {
		mainFrame.cityView.addStatic(cityView);
		mainFrame.buildingView.addView(panel, cityView.getID());
		CityMap.addBuilding(BUILDING.restaurant, building);
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
			if(returnBuilding == null) // TODO remove, hack for market 
				return findRandomBuilding(BUILDING.restaurant);
			return returnBuilding;
		}

		/**
		 * Find the building of type closest to the person's location
		 */
		public static BuildingInterface findClosestBuilding(BUILDING type, Person p) {
			int x = 100; // p.animation.getXPos(); // TODO RestaurantZhang 92f655cfd5
			int y = 100; // p.animation.getYPos(); // TODO RestaurantZhang 92f655cfd5
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
//			if(returnRoad == null) // TODO remove, hack for market
//				returnRoad =  findClosestRoad(findRandomBuilding(BUILDING.restaurant));
			return returnRoad;
		}

		public static void clearMap() {

			map.clear();
		}
	}

}
