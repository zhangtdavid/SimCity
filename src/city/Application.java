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

import city.agents.CarAgent;
import city.agents.PersonAgent;
import city.buildings.BankBuilding;
import city.buildings.BusStopBuilding;
import city.buildings.HouseBuilding;
import city.buildings.RestaurantTimmsBuilding;
import city.gui.CityViewPanel;
import city.gui.CityViewRestaurant;
import city.gui.HousePanel;
import city.gui.MainFrame;
import city.gui.RestaurantTimmsPanel;
import city.interfaces.Person;
import city.roles.LandlordRole;
import city.roles.RestaurantTimmsCashierRole;
import city.roles.RestaurantTimmsCookRole;
import city.roles.RestaurantTimmsHostRole;
import city.roles.RestaurantTimmsWaiterRole;

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
		
        // Create panels
		RestaurantTimmsPanel rtp1 = new RestaurantTimmsPanel(Color.GRAY, new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		CityViewRestaurant cvr1 = new CityViewRestaurant(150, 150, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.magenta, rtp1); 
		mainFrame.cityView.addStatic(cvr1);
		// Skipping creating a bank panel
		HousePanel rhp1 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529), new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		
        // Create buildings
        RestaurantTimmsBuilding rtb = (RestaurantTimmsBuilding) Application.CityMap.addBuilding(BUILDING.restaurant, new RestaurantTimmsBuilding("RestaurantTimms", rtp1));
        mainFrame.buildingView.addView(rtp1, cvr1.ID);
        CityMap.addBuilding(BUILDING.restaurant, rtb);
        Application.CityMap.addBuilding(BUILDING.bank, new BankBuilding("BankBuilding"));
        // Skipping creating a house

		// Create landlord
		PersonAgent p0 = new PersonAgent("Landlord", date);
		LandlordRole p0r1 = new LandlordRole();
		p0.addRole(p0r1);
		HouseBuilding h0 = new HouseBuilding("House 0", p0r1, rhp1);
		p0.setHome(h0);
		p0r1.setActive();
		people.add(p0);

		// Create houses
		HouseBuilding h1 = new HouseBuilding("House 1", p0r1, rhp1);
		HouseBuilding h2 = new HouseBuilding("House 2", p0r1, rhp1);
		HouseBuilding h3 = new HouseBuilding("House 3", p0r1, rhp1);
		HouseBuilding h4 = new HouseBuilding("House 4", p0r1, rhp1);

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
		CarAgent c0 = new CarAgent();
		CarAgent c1 = new CarAgent();
		CarAgent c2 = new CarAgent();
		CarAgent c3 = new CarAgent();
		CarAgent c4 = new CarAgent();
		p0.setCar(c0);
		p1.setCar(c1);
		p2.setCar(c2);
		p3.setCar(c3);
		p4.setCar(c4);

		// Create cashier
		RestaurantTimmsCashierRole p1r1 = new RestaurantTimmsCashierRole(rtb, 0, 100); // TODO Change shift times
		rtb.addRole(p1r1);
		p1.setOccupation(p1r1);

		// Create cook
		RestaurantTimmsCookRole p2r1 = new RestaurantTimmsCookRole(rtb, 0, 100); // TODO Change shift times
		rtb.addRole(p2r1);
		p2.setOccupation(p2r1);

		// Create host
		RestaurantTimmsHostRole p3r1 = new RestaurantTimmsHostRole(rtb, 0, 100); // TODO Change shift times
		rtb.addRole(p3r1);
		p3.setOccupation(p3r1);

		// Create waiter
		RestaurantTimmsWaiterRole p4r1 = new RestaurantTimmsWaiterRole(rtb, 0, 100); // TODO Change shift times
		rtb.addRole(p4r1);
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
			Building b = new BusStopBuilding("placeholder");
			return b;
		}
		
		/**
		 * Return the building of type closest to the destination building
		 * 
		 * @param b the destination you wish to reach
		 */
		public static Building findClosestBuilding(BUILDING type, Building b) {
			// TODO
			Building d = new BusStopBuilding("placeholder");
			return d;
		}

	}

}
