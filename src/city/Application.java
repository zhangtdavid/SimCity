package city;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import utilities.RestaurantZhangMenu;
import utilities.RestaurantZhangTable;
import city.agents.PersonAgent;
import city.animations.RestaurantZhangCookAnimation;
import city.animations.RestaurantZhangCustomerAnimation;
import city.animations.RestaurantZhangWaiterAnimation;
import city.gui.MainFrame;
import city.interfaces.Person;
import city.roles.RestaurantZhangCashierRole;
import city.roles.RestaurantZhangCookRole;
import city.roles.RestaurantZhangCustomerRole;
import city.roles.RestaurantZhangHostRole;
import city.roles.RestaurantZhangWaiterRegularRole;

public class Application {

	private static MainFrame mainFrame;
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
		// Setup the restaurant
		int nTables = 3;
		int TABLEXSTART = 80;
		int TABLEXSPACING = 150;
		int TABLEROW = 3;
		int TABLEYSTART = 250;
		int TABLEYSPACING = 150;
		int TABLECOLUMN = 3;
		int TABLEW = 50;
		int TABLEH = 50;
		List<RestaurantZhangTable> tables = new ArrayList<RestaurantZhangTable>(nTables);
    	for (int ix = 0; ix < nTables; ix++) {
    		tables.add(new RestaurantZhangTable(ix, TABLEXSTART + ((ix % TABLEROW) * TABLEXSPACING), 
    				TABLEYSTART + ((ix / TABLECOLUMN) * TABLEYSPACING),
    				TABLEW, TABLEH));
    	}
    	RestaurantZhangMenu menu = new RestaurantZhangMenu();
		
		// Set up the staff
		PersonAgent p1 = new PersonAgent("Cashier 1", date);
		RestaurantZhangCashierRole p1r1Cashier = new RestaurantZhangCashierRole("Cashier 1");
		p1r1Cashier.setMenu(menu);
		p1.setOccupation(p1r1Cashier);
		people.add(p1);
		p1.startThread();
		
		PersonAgent p2 = new PersonAgent("Cook 1", date);
		RestaurantZhangCookRole p2r1Cook = new RestaurantZhangCookRole("Cook 1");
		p2r1Cook.setMenuTimes(menu);
		RestaurantZhangCookAnimation p2a1Cook = new RestaurantZhangCookAnimation(p2r1Cook);
		p2a1Cook.isVisible = true;
		p2r1Cook.setAnimation(p2a1Cook);
		mainFrame.restaurantZhangPanel.addVisualizationElement(p2a1Cook);
		p2.setOccupation(p2r1Cook);
		people.add(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Host 1", date);
		RestaurantZhangHostRole p3r1Host = new RestaurantZhangHostRole("Host 1");
		p3r1Host.setTables(tables);
		p3.setOccupation(p3r1Host);
		people.add(p3);
		p3.startThread();
		
		PersonAgent p4 = new PersonAgent("Waiter 1", date);
		RestaurantZhangWaiterRegularRole p4r1Waiter = new RestaurantZhangWaiterRegularRole("Waiter 1");
		p4r1Waiter.setCashier(p1r1Cashier);
		p4r1Waiter.setCook(p2r1Cook);
		p4r1Waiter.setHost(p3r1Host);
		p4r1Waiter.setMenu(menu);
		RestaurantZhangWaiterAnimation p4a1Waiter = new RestaurantZhangWaiterAnimation(p4r1Waiter, 200, 200);
		p4a1Waiter.isVisible = true;
		p4r1Waiter.setAnimation(p4a1Waiter);
		p3r1Host.addWaiter(p4r1Waiter);
		mainFrame.restaurantZhangPanel.addVisualizationElement(p4a1Waiter);
		p4.setOccupation(p4r1Waiter);
		people.add(p4);
		p4.startThread();
		
		// Set up the table
		
//		 Wait for things to get in position
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		
		// Send in a customer
		PersonAgent p5 = new PersonAgent("Customer 1", date);
		RestaurantZhangCustomerRole p5r1Customer = new RestaurantZhangCustomerRole("Customer 1");
		p5r1Customer.setHost(p3r1Host);
		p5r1Customer.setCashier(p1r1Cashier);
		RestaurantZhangCustomerAnimation p5a1Customer = new RestaurantZhangCustomerAnimation(p5r1Customer);
		p5a1Customer.isVisible = true;
		p5r1Customer.setAnimation(p5a1Customer);
		mainFrame.restaurantZhangPanel.addVisualizationElement(p5a1Customer);
		p5.addRole(p5r1Customer);
		people.add(p5);
		p5.startThread();
		p5r1Customer.gotHungry();
		
		// TODO these shouldn't be necessary, figure out why they're needed
		p5r1Customer.setActive();
		p5.stateChanged();
	}
	
	public static class CityMap {
		private static HashMap<String, List<Building>> map = new HashMap<String, List<Building>>();
		
		public void addBuilding(String type, Building b) {
			if(map.containsKey(type))
				map.get(type).add(b); // Get the value from the type key, and add the building to the value (which is a list)
		}
		
	}

}
