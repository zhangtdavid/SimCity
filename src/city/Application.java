package city;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import city.agents.CarAgent;
import city.agents.PersonAgent;
import city.animations.RestaurantTimmsCashierAnimation;
import city.animations.RestaurantTimmsCookAnimation;
import city.animations.RestaurantTimmsHostAnimation;
import city.animations.RestaurantTimmsTableAnimation;
import city.animations.RestaurantTimmsWaiterAnimation;
import city.buildings.BankBuilding;
import city.buildings.BusStopBuilding;
import city.buildings.HouseBuilding;
import city.buildings.RestaurantTimmsBuilding;
import city.buildings.RestaurantZhangBuilding;
import city.gui.BuildingCard;
import city.gui.CityViewRestaurant;
import city.gui.MainFrame;
import city.gui.RestaurantZhangPanel;
import city.gui.CityViewBuilding;
import city.gui.CityViewRestaurant;
import city.gui.MainFrame;
import city.gui.RestaurantTimmsPanel;
import city.interfaces.Person;
import city.roles.LandlordRole;
import city.roles.ResidentRole;
import city.roles.RestaurantZhangCashierRole;
import city.roles.RestaurantZhangCookRole;
import city.roles.RestaurantZhangCustomerRole;
import city.roles.RestaurantZhangHostRole;
import city.roles.RestaurantZhangWaiterRegularRole;
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
	public static final int RENT_DUE_INTERVAL = 0; // TODO set the global interval at which rent is expected/paid
	public static final int PAYCHECK_INTERVAL = 0; // TODO set the global interval at which people are paid
	public static enum BANK_SERVICE {none, deposit, moneyWithdraw, atmDeposit};
	public static enum TRANSACTION_TYPE {personal, business};
	public static enum FOOD_ITEMS {steak, chicken, salad, pizza};
	public static enum BUILDING {bank, busStop, house, market, restaurant};
	
	// TODO for testing
	public static RestaurantTimmsPanel restaurantTimmsPanel = null;
	
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
//		// RESTAURANTZHANGTESTING FOR ANIMATION IN GUI
//		// FIRST add a panel
//		RestaurantZhangPanel rzp1 = new RestaurantZhangPanel(Color.black, new Dimension(mainFrame.cityView.CITY_WIDTH, mainFrame.cityView.CITY_HEIGHT));
//		// SECOND create a city view restaurant, the above panel is the last argument
//		CityViewRestaurant restaurantZhang1 = new CityViewRestaurant(150, 150, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.magenta, rzp1); 
//		// THIRD add it to the list of statics in the cityView
//		mainFrame.cityView.addStatic(restaurantZhang1);
//		// FOURTH create a new building, last argument is the panel in step ONE
//		RestaurantZhangBuilding rzb1 = new RestaurantZhangBuilding("RestaurantZhang1", "RestaurantZhangCustomerRole", rzp1);
//		// FIFTH add the new building to the buildingView
//		mainFrame.buildingView.addView(rzp1, restaurantZhang1.ID);
//		// SIXTH add the new building to the map
//		CityMap.addBuilding(BUILDING.restaurant, rzb1);
//		// SEVENTH create all your roles after
//		PersonAgent p4 = new PersonAgent("Host 1", date);
//		RestaurantZhangHostRole p4r1 = new RestaurantZhangHostRole("Host 1 Role");
//		rzb1.addRole(p4r1);
//		p4.setOccupation(p4r1);
//		people.add(p4);
//		PersonAgent p5 = new PersonAgent("Cook 1", date);
//		RestaurantZhangCookRole p5r1 = new RestaurantZhangCookRole("Cashier 1 Role");
//		rzb1.addRole(p5r1);
//		p5.setOccupation(p5r1);
//		people.add(p5);
//		PersonAgent p6 = new PersonAgent("Cashier 1", date);
//		RestaurantZhangCashierRole p6r1 = new RestaurantZhangCashierRole("Cashier 1 Role");
//		rzb1.addRole(p6r1);
//		p6.setOccupation(p6r1);
//		people.add(p6);
//		PersonAgent p7 = new PersonAgent("Waiter 1", date);
//		RestaurantZhangWaiterRegularRole p7r1 = new RestaurantZhangWaiterRegularRole("Waiter 1 Role");
//		rzb1.addRole(p7r1);
//		p7.setOccupation(p7r1);
//		people.add(p7);
//		p4.startThread();
//		p5.startThread();
//		p6.startThread();
//		p7.startThread();
//		PersonAgent p8 = new PersonAgent("Customer 1", date);
//		RestaurantZhangCustomerRole p8r1 = new RestaurantZhangCustomerRole("Customer 1");
//		rzb1.addRole(p8r1);
//		p8.setOccupation(p8r1);
//		people.add(p8);
//		p8.startThread();
//		p8r1.gotHungry();

		// Create buildings
		RestaurantTimmsBuilding restaurantTimms = (RestaurantTimmsBuilding) Application.CityMap.addBuilding(BUILDING.restaurant, new RestaurantTimmsBuilding("RestaurantTimms"));
		Application.CityMap.addBuilding(BUILDING.bank, new BankBuilding("BankBuilding"));
		
		// Create landlord
		PersonAgent p0 = new PersonAgent("Landlord", date);
		LandlordRole p0r1 = new LandlordRole();
		p0.addRole(p0r1);
		HouseBuilding h0 = new HouseBuilding("House 0", p0r1);
		p0.setHome(h0);
		p0r1.setActive();
		people.add(p0);
		
		// Create houses
		HouseBuilding h1 = new HouseBuilding("House 1", p0r1);
		HouseBuilding h2 = new HouseBuilding("House 2", p0r1);
		HouseBuilding h3 = new HouseBuilding("House 3", p0r1);
		HouseBuilding h4 = new HouseBuilding("House 4", p0r1);
		
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
		RestaurantTimmsCashierRole p1r1 = new RestaurantTimmsCashierRole(restaurantTimms, 0, 12);
		RestaurantTimmsCashierAnimation p1a1 = new RestaurantTimmsCashierAnimation();
		p1r1.setAnimation(p1a1);
		p1.setOccupation(p1r1);
		
		// Create cook
		RestaurantTimmsCookRole p2r1 = new RestaurantTimmsCookRole(restaurantTimms, 0, 12);
		RestaurantTimmsCookAnimation p2a1 = new RestaurantTimmsCookAnimation();
		p2r1.setAnimation(p2a1);
		p2.setOccupation(p2r1);
		
		// Create host
		RestaurantTimmsHostRole p3r1 = new RestaurantTimmsHostRole(restaurantTimms, 0, 12);
		RestaurantTimmsHostAnimation p3a1 = new RestaurantTimmsHostAnimation();
		p3r1.setAnimation(p3a1);
		p3.setOccupation(p3r1);
		
		// Create waiter
		RestaurantTimmsWaiterRole p4r1 = new RestaurantTimmsWaiterRole(restaurantTimms, 0, 12, 0);
		RestaurantTimmsWaiterAnimation p4a1 = new RestaurantTimmsWaiterAnimation(p4r1);
		p4r1.setAnimation(p4a1);
		p4.setOccupation(p4r1);
		restaurantTimms.restaurantWaiters.add(p4r1);
		
		// Create building animation
		mainFrame.cityView.addObject(CityViewBuilding.BuildingType.RESTAURANTTIMMS);
		for (CityViewBuilding b : mainFrame.cityView.statics) {
			if (b.getClass() == CityViewRestaurant.class) {
				CityViewRestaurant r = (CityViewRestaurant) b;
				if (r.building.getClass() == RestaurantTimmsPanel.class) {
					restaurantTimmsPanel = (RestaurantTimmsPanel) r.building;
				}
			}
		}
		mainFrame.buildingView.addView(restaurantTimmsPanel, "Restaurant 0");
		
		// Add visualization elements
		restaurantTimmsPanel.addVisualizationElement(p1a1);
		restaurantTimmsPanel.addVisualizationElement(p2a1);
		restaurantTimmsPanel.addVisualizationElement(p3a1);
		restaurantTimmsPanel.addVisualizationElement(p4a1);
		
		// Add table visualizations
		int i = 0;
		while (i < 9) {
			restaurantTimmsPanel.addVisualizationElement(new RestaurantTimmsTableAnimation(i));
			i++;
		}
		
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
