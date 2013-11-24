package city;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import city.agents.PersonAgent;
import city.buildings.BusStopBuilding;
import city.buildings.AptBuilding;
import city.buildings.RestaurantTimmsBuilding;
import city.gui.MainFrame;
import city.interfaces.Person;
import city.roles.LandlordRole;
import city.roles.ResidentRole;

public class Application {

	private static MainFrame mainFrame;
	private static List<Person> people = new ArrayList<Person>();
	private static Timer timer = new Timer();
	private static Date date = new Date(0);

	public static final int INTERVAL = 10000; // One interval is the simulation's equivalent of a half-hour
	public static final int RENT_DUE_INTERVAL = 336000; // TODO set the global interval at which rent is expected/paid
	public static final int PAYCHECK_INTERVAL = 0; // TODO set the global interval at which people are paid
	public static enum BANK_SERVICES {accountCreate, moneyWithdraw, directDeposit};
	public static enum DEPOSIT_TYPE {personal, business};
	public static enum MARKET_ITEMS {steak, chicken, salad, pizza};
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

		
	}

	public static class CityMap {
		private static HashMap<BUILDING, List<Building>> map = new HashMap<BUILDING, List<Building>>();

		public void addBuilding(BUILDING type, Building b) {
			if(map.containsKey(type))
				map.get(type).add(b); // Get the value from the type key, and add the building to the value (which is a list)
		}

		/**
		 * Returns a random building of type
		 */
		public static Building findRandomBuilding(BUILDING type) { // TODO
			/*Building b = new RestaurantTimmsBuilding("placeholder");
			return b;*/
			return null;
		}

		/**
		 * Return the building of type closest to the person's location
		 */
		public static Building findClosestBuilding(BUILDING type, Person p) { // TODO
			Building b = new BusStopBuilding("placeholder");
			return b;
		}

		/**
		 * Return the building of type closest to the destination building
		 * 
		 * @param b the destination you wish to reach
		 */
		public static Building findClosestBuilding(BUILDING type, Building b) { // TODO
			Building d = new BusStopBuilding("placeholder");
			return d;
		}

	}

}
