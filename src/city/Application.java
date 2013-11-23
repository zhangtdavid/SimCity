package city;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import city.agents.PersonAgent;
import city.buildings.BankBuilding;
import city.buildings.BusStopBuilding;
import city.buildings.RestaurantTimmsBuilding;
import city.gui.MainFrame;
import city.interfaces.Person;
import city.roles.BankCustomerRole;
import city.roles.BankManagerRole;
import city.roles.BankTellerRole;

public class Application {

	private static MainFrame mainFrame;
	private static List<Person> people = new ArrayList<Person>();
	private static Timer timer = new Timer();
	private static Date date = new Date(0);
	
	public static final int INTERVAL = 10000; // One interval is the simulation's equivalent of a half-hour
	public static final int RENT_DUE_INTERVAL = 0; // TODO set the global interval at which rent is expected/paid
	public static final int PAYCHECK_INTERVAL = 0; // TODO set the global interval at which people are paid
	public static enum BANK_SERVICE {none, deposit, moneyWithdraw, atmDeposit};
	public static enum TRANSACTION_TYPE {personal, business};
	public static enum MARKET_ITEM {steak, chicken, salad, pizza};
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
		BankBuilding b = new BankBuilding("Bank");
		CityMap.addBuilding(BUILDING.bank, b);
        PersonAgent p1 = new PersonAgent("Manager 1", date);
        BankManagerRole p1r1 = new BankManagerRole(b);
        b.setManager(p1r1);
        p1.setOccupation(p1r1);
        people.add(p1);
        p1.startThread();

        PersonAgent p2 = new PersonAgent("Teller 1", date);
        BankTellerRole p2r1 = new BankTellerRole(b);
        p2.setOccupation(p2r1);
        people.add(p2);
        p2.startThread();

        PersonAgent p3 = new PersonAgent("BankCustomer 1", date);
        BankCustomerRole p3r1 = new BankCustomerRole();
        p3.setOccupation(p3r1);
        people.add(p3);
        p3.startThread();

// Set up the table
        p1r1.msgAvailable(p2r1);
// Wait for things to get in position
       

// Send in a customer
        
// TODO these shouldn't be necessary, figure out why they're needed
        p3r1.setActive(BANK_SERVICE.deposit, 50, TRANSACTION_TYPE.personal);
        p3.stateChanged();
        try {
                Thread.sleep(9000);
        } catch (InterruptedException e) {}
        p3r1.setActive(BANK_SERVICE.deposit, 50, TRANSACTION_TYPE.personal);
        p3.stateChanged();

//TODO these shouldn't be necessary, figure out why they're needed
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
		public static void addBuilding(BUILDING type, Building b) {
			if(map.containsKey(type)) {
				map.get(type).add(b);
			} else {
				List<Building> list = new ArrayList<Building>();
				list.add(b);
				map.put(type, list);
			}
		}
		
		/**
		 * Returns a random building of type
		 */
		public static Building findRandomBuilding(BUILDING type) { // TODO
			Building b = new RestaurantTimmsBuilding("placeholder", "RestaurantTimmsCustomerRole");
			return b;
		}
		
		/**
		 * Return the building of type closest to the person's location
		 */
		public static Building findClosestBuilding(BUILDING type, Person p) { // TODO
			Building b = new BusStopBuilding("placeholder");
			return b;
		}
	
		public static BankBuilding findBank(){
			return (BankBuilding) map.get(BUILDING.bank).get(0);
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
