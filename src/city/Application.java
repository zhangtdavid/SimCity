package city;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import city.agents.PersonAgent;
import city.animations.RestaurantJPCookAnimation;
import city.animations.RestaurantJPCustomerAnimation;
import city.animations.RestaurantJPWaiterAnimation;
import city.gui.MainFrame;
import city.interfaces.Person;
import city.roles.BankTellerRole;
import city.roles.RestaurantJPCashierRole;
import city.roles.RestaurantJPCookRole;
import city.roles.RestaurantJPCustomerRole;
import city.roles.RestaurantJPHostRole;
import city.roles.RestaurantJPWaiterRole;

public class Application {

	public static MainFrame mainFrame;
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
		// Load a scenario
		mainFrame = new MainFrame();
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
	}
	
	/**
	 * This will eventually load some type of configuration file that specifies how many
	 * people to create and what roles to create them in.
	 */
	private static void parseConfig() {
<<<<<<< Updated upstream
		PersonAgent p1 = new PersonAgent(date);
		BankTellerRole p1r1 = new BankTellerRole();
		
		p1.setOccupation(p1r1);
		
		people.add(p1);
		p1.startThread();
=======
		 PersonAgent p1 = new PersonAgent("Cashier 1", date);
         RestaurantJPCashierRole p1r1 = new RestaurantJPCashierRole();
         p1.setOccupation(p1r1);
         people.add(p1);
         p1.startThread();
         
         PersonAgent p2 = new PersonAgent("Cook 1", date);
         RestaurantJPCookRole p2r1 = new RestaurantJPCookRole(p1r1);
         RestaurantJPCookAnimation p2a1 = new RestaurantJPCookAnimation(p2r1);
         p2r1.setAnimation(p2a1);
         mainFrame.restaurantJPPanel.addGui(p2a1);
         p2.setOccupation(p2r1);
         people.add(p2);
         p2.startThread();
         
         PersonAgent p3 = new PersonAgent("Host 1", date);
         RestaurantJPHostRole p3r1 = new RestaurantJPHostRole("Eugene");
         p3.setOccupation(p3r1);
         people.add(p3);
         p3.startThread();
         
         PersonAgent p4 = new PersonAgent("Waiter 1", date);
         RestaurantJPWaiterRole p4r1 = new RestaurantJPWaiterRole(p2r1, p3r1, p1r1, 0);
         RestaurantJPWaiterAnimation p4a1 = new RestaurantJPWaiterAnimation(p4r1, 0);
         p4r1.setAnimation(p4a1);
         p3r1.addWaiter(p4r1, "W1");
         mainFrame.restaurantJPPanel.addGui(p4a1);
         p4.setOccupation(p4r1);
         people.add(p4);
         p4.startThread();
         
         // Set up the table
         
         // Wait for things to get in position
         try {
                 Thread.sleep(4000);
         } catch (InterruptedException e) {}
         
         // Send in a customer
         PersonAgent p5 = new PersonAgent("Customer 1", date);
         RestaurantJPCustomerRole p5r1 = new RestaurantJPCustomerRole(p3r1, p1r1);
         RestaurantJPCustomerAnimation p5a1 = new RestaurantJPCustomerAnimation(p5r1, 0);
         p5r1.setAnimation(p5a1);
         mainFrame.restaurantJPPanel.addGui(p5a1);
         p5.addRole(p5r1);
         people.add(p5);
         p5.startThread();
         p5r1.gotHungry();
         
         // TODO these shouldn't be necessary, figure out why they're needed
         p5r1.setActive();
         p5.stateChanged();
>>>>>>> Stashed changes
	}
	
	public static class CityMap {
		private static HashMap<String, List<Building>> map = new HashMap<String, List<Building>>();
		
		public void addBuilding(String type, Building b) {
			if(map.containsKey(type))
				map.get(type).add(b); // Get the value from the type key, and add the building to the value (which is a list)
		}
		
	}

}
