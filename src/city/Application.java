package city;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import city.agents.PersonAgent;
import city.gui.CityRoad;
import city.gui.MainFrame;
import city.interfaces.Person;

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

	}
	
	public static class CityMap {
		private static HashMap<BUILDING, List<Building>> map = new HashMap<BUILDING, List<Building>>();
		private static List<CityRoad> roads = new ArrayList<CityRoad>();
		
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
			//Collections.shuffle(list);
			return list.get(0);
		}

		/**
		 * Return the building of type closest to the person's location
		 */
		public static Building findClosestBuilding() {
			return null;
		}
		
		/**
		 * Return the building of type closest to the destination building
		 * 
		 * @param b the destination you wish to reach
		 */
		public static Building findClosestBuilding(BUILDING type, Building b) {
			int x = b.getCityViewBuilding().x;
			int y = b.getCityViewBuilding().y;
			double closestDistance = 1000000;
			Building returnBuilding = null;
			for(Building tempBuilding : map.get(type)) {
				double distance = Math.sqrt((double)(Math.pow(tempBuilding.getCityViewBuilding().x - x, 2) + Math.pow(tempBuilding.getCityViewBuilding().y - y, 2)));
				if( distance < closestDistance) {
					closestDistance = distance;
					returnBuilding = tempBuilding;
				}
			}
			return returnBuilding;
		}
		
		public static Building findClosestBuilding(BUILDING type, PersonAgent p) {
			int x = 100; // p.animation.getXPos(); // TODO RestaurantZhang 92f655cfd5
			int y = 100; // p.animation.getYPos(); // TODO RestaurantZhang 92f655cfd5
			double closestDistance = 1000000;
			Building returnBuilding = null;
			for(Building b : map.get(type)) {
				double distance = Math.sqrt((double)(Math.pow(b.getCityViewBuilding().x - x, 2) + Math.pow(b.getCityViewBuilding().y - y, 2)));
				if( distance < closestDistance) {
					closestDistance = distance;
					returnBuilding = b;
				}
			}
			return returnBuilding;
		}
		
		public static CityRoad findClosestRoad(Building b) {
			int x = b.getCityViewBuilding().x;
			int y = b.getCityViewBuilding().y;
			double closestDistance = 1000000;
			CityRoad returnRoad = null;
			for(CityRoad r : roads) {
				double distance = Math.sqrt((double)(Math.pow(r.x - x, 2) + Math.pow(r.y - y, 2)));
				if( distance < closestDistance) {
					closestDistance = distance;
					returnRoad = r;
				}
			}
			return returnRoad;
		}
	}

}
