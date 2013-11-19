package city;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import city.agents.PersonAgent;
import city.animations.RestaurantTimmsCashierAnimation;
import city.animations.RestaurantTimmsCookAnimation;
import city.animations.RestaurantTimmsCustomerAnimation;
import city.animations.RestaurantTimmsHostAnimation;
import city.animations.RestaurantTimmsTableAnimation;
import city.animations.RestaurantTimmsWaiterAnimation;
import city.gui.MainFrame;
import city.interfaces.Person;
import city.roles.RestaurantTimmsCashierRole;
import city.roles.RestaurantTimmsCookRole;
import city.roles.RestaurantTimmsCustomerRole;
import city.roles.RestaurantTimmsHostRole;
import city.roles.RestaurantTimmsWaiterRole;

public class Application {

	private static MainFrame mainFrame;
	private static List<Person> people = new ArrayList<Person>();
	private static Timer timer = new Timer();
	private static Date date = new Date(0);
	
	public static final int INTERVAL = 10000; // One interval is the simulation's equivalent of a half-hour
	public static final int RENT_DUE_INTERVAL = 0; // TODO set the global interval at which rent is expected/paid
	public static final int PAYCHECK_INTERVAL = 0; // TODO set the global interval at which people are paid
	public static enum BANK_SERVICES {accountCreate, acctClose, moneyWithdraw, loanRequest};
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
		// Set up the staff
		PersonAgent p1 = new PersonAgent("Cashier 1", date);
		RestaurantTimmsCashierRole p1r1 = new RestaurantTimmsCashierRole();
		RestaurantTimmsCashierAnimation p1a1 = new RestaurantTimmsCashierAnimation();
		p1r1.setAnimation(p1a1);
		mainFrame.restaurantTimmsPanel.addVisualizationElement(p1a1);
		p1.setOccupation(p1r1);
		people.add(p1);
		p1.startThread();
		
		PersonAgent p2 = new PersonAgent("Cook 1", date);
		RestaurantTimmsCookRole p2r1 = new RestaurantTimmsCookRole(p1r1);
		RestaurantTimmsCookAnimation p2a1 = new RestaurantTimmsCookAnimation();
		p2r1.setAnimation(p2a1);
		mainFrame.restaurantTimmsPanel.addVisualizationElement(p2a1);
		p2.setOccupation(p2r1);
		people.add(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Host 1", date);
		RestaurantTimmsHostRole p3r1 = new RestaurantTimmsHostRole();
		RestaurantTimmsHostAnimation p3a1 = new RestaurantTimmsHostAnimation();
		p3r1.setAnimation(p3a1);
		mainFrame.restaurantTimmsPanel.addVisualizationElement(p3a1);
		p3.setOccupation(p3r1);
		people.add(p3);
		p3.startThread();
		
		PersonAgent p4 = new PersonAgent("Waiter 1", date);
		RestaurantTimmsWaiterRole p4r1 = new RestaurantTimmsWaiterRole(p2r1, p3r1, p1r1, 0);
		RestaurantTimmsWaiterAnimation p4a1 = new RestaurantTimmsWaiterAnimation(p4r1);
		p4r1.setAnimation(p4a1);
		p3r1.addWaiter(p4r1);
		mainFrame.restaurantTimmsPanel.addVisualizationElement(p4a1);
		p4.setOccupation(p4r1);
		people.add(p4);
		p4.startThread();
		
		// Set up the table
		RestaurantTimmsTableAnimation t1 = new RestaurantTimmsTableAnimation(0);
		mainFrame.restaurantTimmsPanel.addVisualizationElement(t1);
		p3r1.addTable(0);
		
		// Wait for things to get in position
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {}
		
		// Send in a customer
		PersonAgent p5 = new PersonAgent("Customer 1", date);
		RestaurantTimmsCustomerRole p5r1 = new RestaurantTimmsCustomerRole(p3r1, p1r1);
		RestaurantTimmsCustomerAnimation p5a1 = new RestaurantTimmsCustomerAnimation(p5r1);
		p5r1.setAnimation(p5a1);
		mainFrame.restaurantTimmsPanel.addVisualizationElement(p5a1);
		p5.addRole(p5r1);
		people.add(p5);
		p5.startThread();
		p5r1.msgGoToRestaurant();
		
		// TODO these shouldn't be necessary, figure out why they're needed
		p5r1.setActive();
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
