package city;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import city.agents.PersonAgent;
import city.animations.RestaurantChoiCashierAnimation;
import city.animations.RestaurantChoiCookAnimation;
import city.animations.RestaurantChoiCustomerAnimation;
import city.animations.RestaurantChoiFurnitureAnimation;
import city.animations.RestaurantChoiHostAnimation;
import city.animations.RestaurantChoiWaiterAnimation;
import city.gui.MainFrame;
import city.gui.RestaurantChoiPanel;
import city.interfaces.Person;
import city.roles.BankTellerRole;
import city.roles.RestaurantChoiCashierRole;
import city.roles.RestaurantChoiCookRole;
import city.roles.RestaurantChoiCustomerRole;
import city.roles.RestaurantChoiHostRole;
import city.roles.RestaurantChoiRevolvingStand;
import city.roles.RestaurantChoiTable;
import city.roles.RestaurantChoiWaiter2Role;
import city.roles.RestaurantChoiWaiterRole;

public class Application {

	private static List<Person> people = new ArrayList<Person>();
	private static Timer timer = new Timer();
	private static Date date = new Date(0);
	static MainFrame mainFrame;

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
		RestaurantChoiCashierRole p1r1 = new RestaurantChoiCashierRole();
		RestaurantChoiCashierAnimation p1a1 = new RestaurantChoiCashierAnimation();
		p1a1.isVisible=true;
		p1r1.setGui(p1a1);
		mainFrame.restaurantChoiPanel.addVisualizationElement(p1a1);
		p1.setOccupation(p1r1);
		people.add(p1);
		p1.startThread();

		PersonAgent p2 = new PersonAgent("Cook 1", date);
		RestaurantChoiCookRole p2r1 = new RestaurantChoiCookRole(/*p1r1*/);
		RestaurantChoiCookAnimation p2a1 = new RestaurantChoiCookAnimation(p2r1);
		p2a1.isVisible=true;
		p2r1.setGui(p2a1);
		mainFrame.restaurantChoiPanel.addVisualizationElement(p2a1);
		p2.setOccupation(p2r1);
		people.add(p2);
		p2.startThread();
		
		PersonAgent p3 = new PersonAgent("Host 1", date);
		RestaurantChoiHostRole p3r1 = new RestaurantChoiHostRole();
		RestaurantChoiHostAnimation p3a1 = new RestaurantChoiHostAnimation();
		p3r1.setAnimation(p3a1);
		p3a1.isVisible=true;
		mainFrame.restaurantChoiPanel.addVisualizationElement(p3a1);
		p3.setOccupation(p3r1);
		people.add(p3);
		p3.startThread();

		PersonAgent p4 = new PersonAgent("Waiter 1", date);
		RestaurantChoiWaiterRole p4r1 = new RestaurantChoiWaiterRole("Waiter 1");
		//alternatively, test RestaurantChoiWaiter2Role
		p4r1.setCashier(p1r1);
		p4r1.setHost(p3r1);
		p4r1.setCook(p2r1);
		RestaurantChoiWaiterAnimation p4a1 = new RestaurantChoiWaiterAnimation(p4r1);
		p4a1.isVisible=true;
		System.out.println(p4a1.getXPos());
		p4r1.setGui(p4a1);
		p3r1.addWaiter(p4r1);
		mainFrame.restaurantChoiPanel.addVisualizationElement(p4a1);
		p4.setOccupation(p4r1);
		people.add(p4);
		p4.startThread();
		
		RestaurantChoiRevolvingStand rs = new RestaurantChoiRevolvingStand(p2r1);
		p4r1.setRevolvingStand(rs);
		p2r1.setRevolvingStand(rs);

		// Set up the tables, furniture
		RestaurantChoiFurnitureAnimation t1 = new RestaurantChoiFurnitureAnimation(0);
		mainFrame.restaurantChoiPanel.addVisualizationElement(t1);
		//p3r1.addTable(0);
		p3r1.tables.add(new RestaurantChoiTable(0));
		t1.isVisible=true;
		// Wait for things to get in position
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {}

		// Send in a customer
		PersonAgent p5 = new PersonAgent("Customer 1", date);
		RestaurantChoiCustomerRole p5r1 = new RestaurantChoiCustomerRole("Customer 1");
		p5r1.setHost(p3r1);
		p5r1.setCashier(p1r1);
		RestaurantChoiCustomerAnimation p5a1 = new RestaurantChoiCustomerAnimation(p5r1);
		p5a1.isVisible=true;
		p5r1.setGui(p5a1);
		mainFrame.restaurantChoiPanel.addVisualizationElement(p5a1);
		p5.addRole(p5r1);
		people.add(p5);
		p5.startThread();
		p5r1.goToRestaurant();

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
