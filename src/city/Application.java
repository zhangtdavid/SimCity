package city;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import city.agents.PersonAgent;
import city.animations.RestaurantChungCashierAnimation;
import city.animations.RestaurantChungCookAnimation;
import city.animations.RestaurantChungCustomerAnimation;
import city.animations.RestaurantChungWaiterAnimation;
import city.buildings.BusStopBuilding;
import city.gui.MainFrame;
import city.interfaces.Person;
import city.roles.RestaurantChungCashierRole;
import city.roles.RestaurantChungCookRole;
import city.roles.RestaurantChungCustomerRole;
import city.roles.RestaurantChungHostRole;
import city.roles.RestaurantChungWaiterBaseRole;
import city.roles.RestaurantChungWaiterMessageCookRole;

public class Application {

	private static MainFrame mainFrame;
	private static List<Person> people = new ArrayList<Person>();
	private static Timer timer = new Timer();
	private static Date date = new Date(0);
	
	public static final int INTERVAL = 10000; // One interval is the simulation's equivalent of a half-hour
	public static final int RENT_DUE_INTERVAL = 0; // TODO set the global interval at which rent is expected/paid
	public static final int PAYCHECK_INTERVAL = 0; // TODO set the global interval at which people are paid
	public static enum BANK_SERVICE {accountCreate, moneyWithdraw, directDeposit};
	public static enum TRANSACTION_TYPE {personal, business};
	public static enum FOOD_ITEMS {steak, chicken, salad, pizza};
	public static enum BUILDING {bank, busStop, house, market};
	
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
//         Set up the staff
        PersonAgent p1 = new PersonAgent("Cashier 1", date);
        RestaurantChungCashierRole p1r1 = new RestaurantChungCashierRole();
        RestaurantChungCashierAnimation p1a1 = new RestaurantChungCashierAnimation(p1r1);
        mainFrame.restaurantChungPanel.addVisualizationElement(p1a1);
        p1.setOccupation(p1r1);
        people.add(p1);
        p1.startThread();
        
        PersonAgent p2 = new PersonAgent("Cook 1", date);
        RestaurantChungCookRole p2r1 = new RestaurantChungCookRole();
        RestaurantChungCookAnimation p2a1 = new RestaurantChungCookAnimation(p2r1);
        mainFrame.restaurantChungPanel.addVisualizationElement(p2a1);
        p2.setOccupation(p2r1);
        people.add(p2);
        p2.startThread();
        
        PersonAgent p3 = new PersonAgent("Host 1", date);
        RestaurantChungHostRole p3r1 = new RestaurantChungHostRole();
        p3.setOccupation(p3r1);
        people.add(p3);
        p3.startThread();
        
        System.out.println(p3r1.getNumTables());
        
        PersonAgent p4 = new PersonAgent("Waiter 1", date);
        RestaurantChungWaiterBaseRole p4r1 = new RestaurantChungWaiterMessageCookRole(p3r1, p2r1, p1r1);
        RestaurantChungWaiterAnimation p4a1 = new RestaurantChungWaiterAnimation(p4r1);
        p4r1.setGui(p4a1);
        p3r1.msgWaiterAvailable(p4r1);
        mainFrame.restaurantChungPanel.addVisualizationElement(p4a1);
        p4.setOccupation(p4r1);
        people.add(p4);
        p4.startThread();
        
    	for (int i = 0; i < p3r1.getNumTables(); i++) {
    		p4a1.addTable(125+((i%5)*80), 200+((i/5)*80));
    	}
        
        // Wait for things to get in position
        try {
                Thread.sleep(4000);
        } catch (InterruptedException e) {}
        
        // Send in a customer
        PersonAgent p5 = new PersonAgent("Customer 1", date);
        RestaurantChungCustomerRole p5r1 = new RestaurantChungCustomerRole("c1");
        p5r1.setHost(p3r1);
        p5r1.setCashier(p1r1);
        RestaurantChungCustomerAnimation p5a1 = new RestaurantChungCustomerAnimation(p5r1);
        p5r1.setGui(p5a1);
        mainFrame.restaurantChungPanel.addVisualizationElement(p5a1);
        p5.addRole(p5r1);
        people.add(p5);
        p5.startThread();
        p5r1.gotHungry();
        
        // TODO these shouldn't be necessary, figure out why they're needed
        p5r1.setActive();
        p5.stateChanged();
	}
	
	public static class CityMap {
		private static HashMap<BUILDING, List<Building>> map = new HashMap<BUILDING, List<Building>>();
		
		public void addBuilding(BUILDING type, Building b) {
			if(map.containsKey(type))
				map.get(type).add(b); // Get the value from the type key, and add the building to the value (which is a list)
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
