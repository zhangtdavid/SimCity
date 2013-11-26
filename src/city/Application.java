package city;

import java.awt.Color;
import java.awt.Dimension;
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
import city.buildings.BankBuilding;
import city.buildings.BusStopBuilding;
import city.buildings.HouseBuilding;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantZhangBuilding;
import city.gui.BusStopPanel;
import city.gui.CityRoad;
import city.gui.CityViewBusStop;
import city.gui.CityViewPanel;
import city.gui.CityViewRestaurant;
import city.gui.MainFrame;
import city.gui.RestaurantZhangPanel;
import city.interfaces.Person;
import city.roles.LandlordRole;
import city.roles.RestaurantZhangCashierRole;
import city.roles.RestaurantZhangCookRole;
import city.roles.RestaurantZhangHostRole;
import city.roles.RestaurantZhangWaiterSharedDataRole;

public class Application {

	private static MainFrame mainFrame;
	private static List<Person> people = new ArrayList<Person>();
	private static Timer timer = new Timer();
	private static Date date = new Date(0);

	public static final int INTERVAL = 1000; // 10000; // One interval is the simulation's equivalent of a half-hour
	public static final int PAYCHECK_INTERVAL = 0; // TODO set the global interval at which people are paid
	public static enum BANK_SERVICE {none, deposit, moneyWithdraw, atmDeposit};
	public static enum TRANSACTION_TYPE {personal, business};
	public static enum FOOD_ITEMS {steak, chicken, salad, pizza};
	public static enum BUILDING {bank, busStop, house, market, restaurant};

	public static RestaurantZhangBuilding rzb1;
	public static BusStopBuilding busStop2;
	
	/**
	 * Main routine to start the program.
	 * 
	 * When the program is started, this is the first call. It opens the GUI window, loads
	 * configuration files, and causes the program to run.
	 *
	 * @param args no input required
	 */
	public static void main(String[] args) {
		// Open the animation GUI
		mainFrame = new MainFrame();

		// Load a scenario
		parseConfig();
		// Start the simulation
		TimerTask tt = new TimerTask() {
			public void run() {
				date.setTime(date.getTime() + 1800000);
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
		CityRoad road1 = new CityRoad(100, 100, CityViewPanel.CITY_WIDTH - 200, CityViewPanel.CITY_HEIGHT / 10, -1, 0, true, Color.black, Color.gray);
		mainFrame.cityView.addMoving(road1);
		CityRoad road2 = new CityRoad(100, 100 + CityViewPanel.CITY_HEIGHT / 10, CityViewPanel.CITY_WIDTH / 10, CityViewPanel.CITY_HEIGHT - 300, 0, 1, false, Color.black, Color.gray);
		mainFrame.cityView.addMoving(road2);
		CityRoad road3 = new CityRoad(100, 350, CityViewPanel.CITY_WIDTH - 200, CityViewPanel.CITY_HEIGHT / 10, 1, 0, true, Color.black, Color.gray);
		mainFrame.cityView.addMoving(road3);
		CityRoad road4 = new CityRoad(350, 100 + CityViewPanel.CITY_HEIGHT / 10, CityViewPanel.CITY_WIDTH / 10, CityViewPanel.CITY_HEIGHT - 300, 0, -1, false, Color.black, Color.gray);
		mainFrame.cityView.addMoving(road4);
		
		// RESTAURANTZHANGTESTING FOR ANIMATION IN GUI
		// FIRST add a panel
		RestaurantZhangPanel rzp1 = new RestaurantZhangPanel(Color.DARK_GRAY, new Dimension(mainFrame.cityView.CITY_WIDTH, mainFrame.cityView.CITY_HEIGHT));
		// SECOND create a city view restaurant, the above panel is the last argument
		CityViewRestaurant cityViewRestaurantZhang1 = new CityViewRestaurant(100, 50, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.magenta, rzp1); 
		// THIRD add it to the list of statics in the cityView
		mainFrame.cityView.addStatic(cityViewRestaurantZhang1);
		// FOURTH create a new building, last argument is the panel in step ONE
		rzb1 = new RestaurantZhangBuilding("RestaurantZhang1", rzp1, cityViewRestaurantZhang1);
		rzp1.setTables(rzb1.tables);
		// FIFTH add the new building to the buildingView
		mainFrame.buildingView.addView(rzp1, cityViewRestaurantZhang1.ID);
		// SIXTH add the new building to the map
		CityMap.addBuilding(BUILDING.restaurant, rzb1);
		// SEVENTH create all your roles after
		
		// Bus Stops!!!!!!!!
		BusStopPanel bsp1 = new BusStopPanel(Color.white, new Dimension(mainFrame.cityView.CITY_WIDTH, mainFrame.cityView.CITY_HEIGHT));
		CityViewBusStop cityViewBusStop1 = new CityViewBusStop(150, 50, "Bus Stop " + (mainFrame.cityView.getStaticsSize()), Color.white, bsp1);
		mainFrame.cityView.addStatic(cityViewBusStop1);
		BusStopBuilding busStop1 = new BusStopBuilding("Bus Stop 1", bsp1, cityViewBusStop1);
		mainFrame.buildingView.addView(bsp1, cityViewBusStop1.ID);
		CityMap.addBuilding(BUILDING.busStop, busStop1);
		
		BusStopPanel bsp2 = new BusStopPanel(Color.white, new Dimension(mainFrame.cityView.CITY_WIDTH, mainFrame.cityView.CITY_HEIGHT));
		CityViewBusStop cityViewBusStop2 = new CityViewBusStop(50, 300, "Bus Stop " + (mainFrame.cityView.getStaticsSize()), Color.white, bsp2);
		mainFrame.cityView.addStatic(cityViewBusStop2);
		busStop2 = new BusStopBuilding("Bus Stop 2", bsp2, cityViewBusStop2);
		mainFrame.buildingView.addView(bsp2, cityViewBusStop2.ID);
		CityMap.addBuilding(BUILDING.busStop, busStop2); 
		
		BusStopPanel bsp3 = new BusStopPanel(Color.white, new Dimension(mainFrame.cityView.CITY_WIDTH, mainFrame.cityView.CITY_HEIGHT));
		CityViewBusStop cityViewBusStop3 = new CityViewBusStop(300, 400, "Bus Stop " + (mainFrame.cityView.getStaticsSize()), Color.white, bsp3);
		mainFrame.cityView.addStatic(cityViewBusStop3);
		BusStopBuilding busStop3 = new BusStopBuilding("Bus Stop 3", bsp3, cityViewBusStop3);
		mainFrame.buildingView.addView(bsp3, cityViewBusStop3.ID);
		CityMap.addBuilding(BUILDING.busStop, busStop3); 
		
		BusStopPanel bsp4 = new BusStopPanel(Color.white, new Dimension(mainFrame.cityView.CITY_WIDTH, mainFrame.cityView.CITY_HEIGHT));
		CityViewBusStop cityViewBusStop4 = new CityViewBusStop(400, 150, "Bus Stop " + (mainFrame.cityView.getStaticsSize()), Color.white, bsp4);
		mainFrame.cityView.addStatic(cityViewBusStop4);
		BusStopBuilding busStop4 = new BusStopBuilding("Bus Stop 4", bsp4, cityViewBusStop4);
		mainFrame.buildingView.addView(bsp4, cityViewBusStop4.ID);
		CityMap.addBuilding(BUILDING.busStop, busStop4 ); 
		// Create buildings
		Application.CityMap.addBuilding(BUILDING.bank, new BankBuilding("BankBuilding"));
		Application.CityMap.addBuilding(BUILDING.market, new MarketBuilding("MarketBuilding"));
		
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
		bus1.startThread();

		// Create landlord
		PersonAgent p0 = new PersonAgent("Landlord", date);
		LandlordRole p0r1 = new LandlordRole();
		p0.addRole(p0r1);
		HouseBuilding h0 = new HouseBuilding("House 0", p0r1);
		p0.setHome(h0);
		p0r1.setActive();
		people.add(p0);

		// Create houses
		HouseBuilding h1 = new HouseBuilding("House 1", p0r1);
		HouseBuilding h2 = new HouseBuilding("House 2", p0r1);
		HouseBuilding h3 = new HouseBuilding("House 3", p0r1);
		HouseBuilding h4 = new HouseBuilding("House 4", p0r1);

		// Create people
		PersonAgent p1 = new PersonAgent("Cashier 1", date);
		PersonAgent p2 = new PersonAgent("Cook 1", date);
		PersonAgent p3 = new PersonAgent("Host 1", date);
		PersonAgent p4 = new PersonAgent("Waiter 1", date);
		people.add(p1);
		people.add(p2);
		people.add(p3);
		people.add(p4);
		p1.setHome(h1);
		p2.setHome(h2);
		p3.setHome(h3);
		p4.setHome(h4);

		// Give people cars
		CarAgent c0 = new CarAgent(busStop2);
		CarAnimation c0Anim = new CarAnimation(c0, busStop2);
		c0.setAnimation(c0Anim);
		mainFrame.cityView.addAnimation(c0Anim);
		CarAgent c1 = new CarAgent(busStop2);
		CarAnimation c1Anim = new CarAnimation(c1, busStop2);
		c1 .setAnimation(c1Anim);
		mainFrame.cityView.addAnimation(c1Anim);
		CarAgent c2 = new CarAgent(busStop2);
		CarAnimation c2Anim = new CarAnimation(c2, busStop2);
		c2.setAnimation(c2Anim);
		mainFrame.cityView.addAnimation(c2Anim);
		CarAgent c3 = new CarAgent(busStop2);
		CarAnimation c3Anim = new CarAnimation(c3, busStop2);
		c3.setAnimation(c3Anim);
		mainFrame.cityView.addAnimation(c3Anim);
		CarAgent c4 = new CarAgent(busStop2);
		CarAnimation c4Anim = new CarAnimation(c4, busStop2);
		c4.setAnimation(c4Anim);
		mainFrame.cityView.addAnimation(c4Anim);
		p0.setCar(c0);
		p1.setCar(c1);
		p2.setCar(c2);
		p3.setCar(c3);
		p4.setCar(c4);

		// Create cashier
		RestaurantZhangCashierRole p1r1 = new RestaurantZhangCashierRole(rzb1, 0, 100); // TODO Change shift times
		rzb1.addRole(p1r1);
		p1.setOccupation(p1r1);

		// Create cook
		RestaurantZhangCookRole p2r1 = new RestaurantZhangCookRole(rzb1, 0, 100); // TODO Change shift times
		rzb1.addRole(p2r1);
		p2.setOccupation(p2r1);

		// Create host
		RestaurantZhangHostRole p3r1 = new RestaurantZhangHostRole(rzb1, 0, 100); // TODO Change shift times
		rzb1.addRole(p3r1);
		p3.setOccupation(p3r1);

		// Create waiter
		RestaurantZhangWaiterSharedDataRole p4r1 = new RestaurantZhangWaiterSharedDataRole(rzb1, 0, 100); // TODO Change shift times
		rzb1.addRole(p4r1);
		p4.setOccupation(p4r1);

		// Start threads
		c0.startThread();
		c1.startThread();
		c2.startThread();
		c3.startThread();
		c4.startThread();
		p0.startThread();
		p1.startThread();
		p2.startThread();
		p3.startThread();
		p4.startThread();
	}

	public static class CityMap {
		private static HashMap<BUILDING, List<Building>> map = new HashMap<BUILDING, List<Building>>();
		
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
		public static Building addBuilding(BUILDING type, Building b) {
			if(map.containsKey(type)) {
				map.get(type).add(b);
			} else {
				List<Building> list = new ArrayList<Building>();
				list.add(b);
				map.put(type, list);
			}
			return b;
		}

		/**
		 * Returns a random building of type
		 */
		public static Building findRandomBuilding(BUILDING type) {
			List<Building> list = map.get(type);
			Collections.shuffle(list);
			return list.get(0);
		}

		/**
		 * Return the building of type closest to the person's location
		 */
		public static Building findClosestBuilding(BUILDING type, Person p) {
			// TODO
			return busStop2;
		}
		
		/**
		 * Return the building of type closest to the destination building
		 * 
		 * @param b the destination you wish to reach
		 */
		public static Building findClosestBuilding(BUILDING type, Building b) {
			// TODO
			return busStop2;
		}

	}

}
