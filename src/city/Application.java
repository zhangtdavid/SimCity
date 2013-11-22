package city;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import city.agents.PersonAgent;
import city.buildings.BankBuilding;
import city.gui.MainFrame;
import city.interfaces.Person;
import city.roles.BankCustomerRole;
import city.roles.BankManagerRole;
import city.roles.BankTellerRole;

public class Application {

	private static List<Person> people = new ArrayList<Person>();
	private static Timer timer = new Timer();
	private static Date date = new Date(0);
	
	public static final int INTERVAL = 10000; // One interval is the simulation's equivalent of a half-hour
	public static final int RENT_DUE_INTERVAL = 0; // TODO set the global interval at which rent is expected/paid
	public static final int PAYCHECK_INTERVAL = 0; // TODO set the global interval at which people are paid
	public static enum BANK_SERVICES {accountCreate, moneyWithdraw};
	public static enum MARKET_ITEMS {steak, chicken, salad, pizza};
	
    /**
     * Main routine to start the program.
     * 
     * When the program is started, this is the first call. It opens the GUI window, loads
     * configuration files, and causes the program to run.
     *
     * @param args no input required
     */
	public static void main(String[] args) {
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
		
		// Open the animation GUI
		new MainFrame();
	}
	
	/**
	 * This will eventually load some type of configuration file that specifies how many
	 * people to create and what roles to create them in.
	 */
	private static void parseConfig() {
		BankBuilding b = new BankBuilding("Bank");
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
        BankCustomerRole p3r1 = new BankCustomerRole(b);
        p3.setOccupation(p3r1);
        people.add(p3);
        p3.startThread();

// Set up the table
        p1r1.msgAvailable(p2r1);
// Wait for things to get in position
       

// Send in a customer
        
// TODO these shouldn't be necessary, figure out why they're needed
        p3r1.setActive(BANK_SERVICES.accountCreate, 50);
        p3.stateChanged();
        try {
        	Thread.sleep(9000);
        } catch (InterruptedException e) {}
        p3r1.setActive(BANK_SERVICES.moneyWithdraw, 50);
        p3.stateChanged();
	}
	
	public static class CityMap {
		private static HashMap<String, List<Building>> map = new HashMap<String, List<Building>>();
		
		public void addBuilding(String type, Building b) {
			if(map.containsKey(type))
				map.get(type).add(b); // Get the value from the type key, and add the building to the value (which is a list)
		}
		
		public List<Building> getBuildings(String type){
			return map.get(type);
		}
		
	}

}
