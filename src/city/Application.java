package city;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import city.agents.CarAgent;
import city.agents.PersonAgent;
import city.buildings.BankBuilding;
import city.buildings.BusStopBuilding;
import city.buildings.HouseBuilding;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantChoiBuilding;
import city.buildings.RestaurantChungBuilding;
import city.buildings.RestaurantZhangBuilding;
import city.gui.CityViewPanel;
import city.gui.CityViewRestaurant;
import city.gui.HousePanel;
import city.gui.MainFrame;
import city.gui.RestaurantChoiPanel;
import city.gui.RestaurantChungPanel;
import city.gui.RestaurantZhangPanel;
import city.interfaces.Person;
import city.roles.LandlordRole;
import city.roles.MarketCashierRole;
import city.roles.MarketEmployeeRole;
import city.roles.MarketManagerRole;
import city.roles.RestaurantChoiCashierRole;
import city.roles.RestaurantChoiCookRole;
import city.roles.RestaurantChoiHostRole;
import city.roles.RestaurantChoiWaiterRole;
import city.roles.RestaurantChungCashierRole;
import city.roles.RestaurantChungCookRole;
import city.roles.RestaurantChungHostRole;
import city.roles.RestaurantChungWaiterMessageCookRole;

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

	public static RestaurantChoiBuilding rchoib1;
	public static HouseBuilding hb1;
	public static MarketBuilding m1;
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
		// RESTAURANTZHANGTESTING FOR ANIMATION IN GUI
		m1 = new MarketBuilding("MarketBuilding");
		// Create buildings
		CityMap.addBuilding(BUILDING.bank, new BankBuilding("BankBuilding"));
		//Application.CityMap.addBuilding(BUILDING.house, new HouseBuilding("house1"));
		CityMap.addBuilding(BUILDING.market, m1);
		
		// FIRST add a panel
		RestaurantChoiPanel rchoip1 = new RestaurantChoiPanel(Color.GRAY, new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		HousePanel rhp0 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529), new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		HousePanel rhp1 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529), new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		HousePanel rhp2 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529), new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		HousePanel rhp3 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529), new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		HousePanel rhp4 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529), new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		/*HousePanel rhp5 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529), new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		HousePanel rhp6 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529), new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		HousePanel rhp7 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529), new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		*/
		//MarketPanel mp1
		
		// SECOND create a city view restaurant, the above panel is the last argument
		CityViewRestaurant restaurantChoi1 = new CityViewRestaurant(200, 200, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.cyan, rchoip1);

		// THIRD add it to the list of statics in the cityView
		mainFrame.cityView.addStatic(restaurantChoi1);
		//mainFrame.cityView.addStatic(house1);

		// FOURTH create a new building, last argument is the panel in step ONE
		rchoib1 = new RestaurantChoiBuilding("RestaurantChoi1", rchoip1);
		//hb1 = new HouseBuilding("House1", rhp1);

		// FIFTH add the new building to the buildingView
		mainFrame.buildingView.addView(rchoip1, restaurantChoi1.ID);

		// SIXTH add the new building to the map
		CityMap.addBuilding(BUILDING.restaurant, rchoib1);



		// Create landlord
		PersonAgent p0 = new PersonAgent("Landlord", date);
		LandlordRole p0r1 = new LandlordRole();
		p0.addRole(p0r1);
		HouseBuilding h0 = new HouseBuilding("House 0", p0r1, rhp0);
		p0.setHome(h0);
		p0r1.setResidence(h0); // this is a fix corresponding to changes made in Landlord and Resident
		p0r1.setActive();
		people.add(p0);

		// Create houses
		HouseBuilding h1 = new HouseBuilding("House 1", p0r1, rhp1); // name, landlord, panel.
		HouseBuilding h2 = new HouseBuilding("House 2", p0r1, rhp2);
		HouseBuilding h3 = new HouseBuilding("House 3", p0r1, rhp3);
		HouseBuilding h4 = new HouseBuilding("House 4", p0r1, rhp4);
		/*HouseBuilding h5 = new HouseBuilding("House 5", p0r1, rhp5);
		HouseBuilding h6 = new HouseBuilding("House 5", p0r1, rhp5);
		HouseBuilding h7 = new HouseBuilding("House 5", p0r1, rhp5);
*/
		// Create people
		PersonAgent p1 = new PersonAgent("Cashier 1", date);
		PersonAgent p2 = new PersonAgent("Cook 1", date);
		PersonAgent p3 = new PersonAgent("Host 1", date);
		PersonAgent p4 = new PersonAgent("Waiter 1", date);
		PersonAgent p5 = new PersonAgent("Market Manager1", date);
		PersonAgent p6 = new PersonAgent("Market Cashier1", date);
		PersonAgent p7 = new PersonAgent("Market Employee1", date);
		people.add(p1);
		people.add(p2);
		people.add(p3);
		people.add(p4);
	/*	people.add(p5);
		people.add(p6);
		people.add(p7);*/
		p1.setHome(h1);
		p2.setHome(h2);
		p3.setHome(h3);
		p4.setHome(h4);/*
		p4.setHome(h5);
		p4.setHome(h6);
		p4.setHome(h7);*/
		RestaurantChoiCashierRole p1r1 = new RestaurantChoiCashierRole(rchoib1, 0, 24);
		rchoib1.addRole(p1r1);
		p1.setOccupation(p1r1);

		// Create cook
		RestaurantChoiCookRole p2r1 = new RestaurantChoiCookRole(rchoib1, 0, 24);
		rchoib1.addRole(p2r1);
		p2.setOccupation(p2r1);

		// Create host
		RestaurantChoiHostRole p3r1 = new RestaurantChoiHostRole(rchoib1, 0, 24);
		rchoib1.addRole(p3r1);
		p3.setOccupation(p3r1);

		// Create waiter
		RestaurantChoiWaiterRole p4r1 = new RestaurantChoiWaiterRole(rchoib1, 0, 24);
		rchoib1.addRole(p4r1);
		p4.setOccupation(p4r1);
		
		// Give people cars
		CarAgent c0 = new CarAgent();
		CarAgent c1 = new CarAgent();
		CarAgent c2 = new CarAgent();
		CarAgent c3 = new CarAgent();
		CarAgent c4 = new CarAgent();/*
		CarAgent c5 = new CarAgent();
		CarAgent c6 = new CarAgent();
		CarAgent c7 = new CarAgent();*/
		p0.setCar(c0);
		p1.setCar(c1);
		p2.setCar(c2);
		p3.setCar(c3);
		p4.setCar(c4);/*
		p5.setCar(c5);
		p6.setCar(c6);
		p7.setCar(c7);*/


		// Start threads

		c0.startThread();
		c1.startThread();
		c2.startThread();
		c3.startThread();
		c4.startThread();/*
		c5.startThread();
		c6.startThread();
		c7.startThread();*/
		p0.startThread();
		p1.startThread();
		p2.startThread();
		p3.startThread();
		p4.startThread();/*
		p5.startThread();
		p6.startThread();
		p7.startThread();*/

		//Create Market people
		/*
		MarketManagerRole p5r1 = new MarketManagerRole(m1, 0, 24);
		MarketCashierRole p6r1 = new MarketCashierRole(m1, 0, 24);
		MarketEmployeeRole p7r1 = new MarketEmployeeRole(m1, 0, 24);
		m1.manager = p5r1;
		m1.cashier = p6r1;
		m1.addEmployee(p7r1);
		p7.setOccupation(p7r1);*/
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
