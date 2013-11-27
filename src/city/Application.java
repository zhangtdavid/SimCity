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

import city.agents.BusAgent;
import city.agents.CarAgent;
import city.agents.PersonAgent;
import city.animations.BusAnimation;
import city.animations.CarAnimation;
import city.animations.RestaurantTimmsTableAnimation;
import city.buildings.BankBuilding;
import city.buildings.BusStopBuilding;
import city.buildings.HouseBuilding;
import city.buildings.RestaurantTimmsBuilding;
import city.buildings.RestaurantZhangBuilding;
import city.gui.BusStopPanel;
import city.gui.CityRoad;
import city.gui.CityViewBusStop;
import city.gui.CityViewPanel;
import city.gui.CityViewRestaurant;
import city.gui.HousePanel;
import city.gui.MainFrame;
import city.gui.RestaurantTimmsPanel;
import city.gui.RestaurantZhangPanel;
import city.interfaces.Person;
import city.roles.LandlordRole;
import city.roles.RestaurantTimmsCashierRole;
import city.roles.RestaurantTimmsCookRole;
import city.roles.RestaurantTimmsHostRole;
import city.roles.RestaurantTimmsWaiterRole;
import city.roles.RestaurantZhangCashierRole;
import city.roles.RestaurantZhangCookRole;
import city.roles.RestaurantZhangHostRole;
import city.roles.RestaurantZhangWaiterSharedDataRole;

public class Application {

	private static MainFrame mainFrame;
	private static List<Person> people = new ArrayList<Person>();
	private static Timer timer = new Timer();
	private static Date date = new Date(0);

	public static final int INTERVAL = 1000; // 10000; // One interval is the simulation's equivalent of a half-hour
	public static final int PAYCHECK_INTERVAL = 0; // TODO set the global interval at which people are paid
	public static enum BANK_SERVICE {none, deposit, moneyWithdraw, atmDeposit};
	public static enum TRANSACTION_TYPE {personal, business};
	public static enum FOOD_ITEMS {steak, chicken, salad, pizza};
	public static enum BUILDING {bank, busStop, house, market, restaurant};

	static List<CityRoad> roads = new ArrayList<CityRoad>();

	/**
	 * Main routine to start the program.
	 * 
	 * When the program is started, this is the first call. It opens the GUI window, loads
	 * configuration files, and causes the program to run.
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
		// Create roads
		for(int i = 375; i >= 125; i -= 25) {
			CityRoad tempRoad = new CityRoad(i, 100, 25, 25, -1, 0, true, Color.black);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		for(int i = 100; i <= 300; i+=25) {
			CityRoad tempRoad = new CityRoad(100, i, 25, 25, 0, 1, false, Color.black);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		for(int i = 100; i <= 350; i+=25) {
			CityRoad tempRoad = new CityRoad(i, 325, 25, 25, 1, 0, true, Color.black);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		for(int i = 325; i >= 125; i-=25) {
			CityRoad tempRoad = new CityRoad(375, i, 25, 25, 0, -1, false, Color.black);
			roads.add(tempRoad);
			mainFrame.cityView.addMoving(tempRoad);
		}
		for(int i = 0; i < roads.size(); i++) { // Connect all roads
			if(i == roads.size() - 1) {
				roads.get(i).nextRoad = roads.get(0);
				continue;
			}
			roads.get(i).nextRoad = roads.get(i+1);
		}

		// Bus Stops!!!!!!!!
		BusStopPanel bsp1 = new BusStopPanel(Color.white, new Dimension(mainFrame.cityView.CITY_WIDTH, mainFrame.cityView.CITY_HEIGHT));
		CityViewBusStop cityViewBusStop1 = new CityViewBusStop(150, 50, "Bus Stop " + (mainFrame.cityView.getStaticsSize()), Color.white, bsp1);
		mainFrame.cityView.addStatic(cityViewBusStop1);
		BusStopBuilding busStop1 = new BusStopBuilding("Bus Stop 1", bsp1, cityViewBusStop1);
		mainFrame.buildingView.addView(bsp1, cityViewBusStop1.ID);
		Application.CityMap.addBuilding(BUILDING.busStop, busStop1);

		BusStopPanel bsp2 = new BusStopPanel(Color.white, new Dimension(mainFrame.cityView.CITY_WIDTH, mainFrame.cityView.CITY_HEIGHT));
		CityViewBusStop cityViewBusStop2 = new CityViewBusStop(50, 300, "Bus Stop " + (mainFrame.cityView.getStaticsSize()), Color.white, bsp2);
		mainFrame.cityView.addStatic(cityViewBusStop2);
		BusStopBuilding busStop2 = new BusStopBuilding("Bus Stop 2", bsp2, cityViewBusStop2);
		mainFrame.buildingView.addView(bsp2, cityViewBusStop2.ID);
		Application.CityMap.addBuilding(BUILDING.busStop, busStop2); 

		BusStopPanel bsp3 = new BusStopPanel(Color.white, new Dimension(mainFrame.cityView.CITY_WIDTH, mainFrame.cityView.CITY_HEIGHT));
		CityViewBusStop cityViewBusStop3 = new CityViewBusStop(300, 375, "Bus Stop " + (mainFrame.cityView.getStaticsSize()), Color.white, bsp3);
		mainFrame.cityView.addStatic(cityViewBusStop3);
		BusStopBuilding busStop3 = new BusStopBuilding("Bus Stop 3", bsp3, cityViewBusStop3);
		mainFrame.buildingView.addView(bsp3, cityViewBusStop3.ID);
		Application.CityMap.addBuilding(BUILDING.busStop, busStop3); 

		BusStopPanel bsp4 = new BusStopPanel(Color.white, new Dimension(mainFrame.cityView.CITY_WIDTH, mainFrame.cityView.CITY_HEIGHT));
		CityViewBusStop cityViewBusStop4 = new CityViewBusStop(400, 150, "Bus Stop " + (mainFrame.cityView.getStaticsSize()), Color.white, bsp4);
		mainFrame.cityView.addStatic(cityViewBusStop4);
		BusStopBuilding busStop4 = new BusStopBuilding("Bus Stop 4", bsp4, cityViewBusStop4);
		mainFrame.buildingView.addView(bsp4, cityViewBusStop4.ID);
		Application.CityMap.addBuilding(BUILDING.busStop, busStop4 ); 

		// Create buildings
		Application.CityMap.addBuilding(BUILDING.bank, new BankBuilding("BankBuilding"));

		busStop1.setNextStop(busStop2);
		busStop1.setPreviousStop(busStop4);
		busStop2.setNextStop(busStop3);
		busStop2.setPreviousStop(busStop1);
		busStop3.setNextStop(busStop4);
		busStop3.setPreviousStop(busStop2);
		busStop4.setNextStop(busStop1);
		busStop4.setPreviousStop(busStop3);

		// Create buses
		BusAgent bus1 = new BusAgent(busStop1, busStop2);
		BusAnimation b1Anim = new BusAnimation(bus1, busStop2);
		bus1.setAnimation(b1Anim);
		mainFrame.cityView.addAnimation(b1Anim);
		CityMap.findClosestRoad(busStop2).vehicle = b1Anim; 
		bus1.startThread();

		// RESTAURANTZHANG------------------------------------------------------------
		// FIRST add a panel
		RestaurantZhangPanel rzp1 = new RestaurantZhangPanel(Color.DARK_GRAY, new Dimension(mainFrame.cityView.CITY_WIDTH, mainFrame.cityView.CITY_HEIGHT));
		HousePanel rhp1 = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529), new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		// SECOND create a city view restaurant, the above panel is the last argument
		CityViewRestaurant cityViewRestaurantZhang1 = new CityViewRestaurant(100, 50, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.magenta, rzp1); 
		// THIRD add it to the list of statics in the cityView
		mainFrame.cityView.addStatic(cityViewRestaurantZhang1);
		// FOURTH create a new building, last argument is the panel in step ONE
		RestaurantZhangBuilding rzb1 = new RestaurantZhangBuilding("RestaurantZhang1", rzp1, cityViewRestaurantZhang1);
		rzp1.setTables(rzb1.tables);
		// FIFTH add the new building to the buildingView
		mainFrame.buildingView.addView(rzp1, cityViewRestaurantZhang1.ID);
		// SIXTH add the new building to the map
		CityMap.addBuilding(BUILDING.restaurant, rzb1);
		// SEVENTH create all your roles after

		// Create landlord
		PersonAgent p0Zhang = new PersonAgent("Landlord Zhang", date);
		LandlordRole p0r1Zhang = new LandlordRole();
		p0Zhang.addRole(p0r1Zhang);
		HouseBuilding h0 = new HouseBuilding("House 0 Zhang", p0r1Zhang, rhp1);
		p0Zhang.setHome(h0);
		p0r1Zhang.setActive();
		people.add(p0Zhang);

		// Create houses
		HouseBuilding h1Zhang = new HouseBuilding("House 1 Zhang", p0r1Zhang, rhp1);
		HouseBuilding h2Zhang = new HouseBuilding("House 2 Zhang", p0r1Zhang, rhp1);
		HouseBuilding h3Zhang = new HouseBuilding("House 3 Zhang", p0r1Zhang, rhp1);
		HouseBuilding h4Zhang = new HouseBuilding("House 4 Zhang", p0r1Zhang, rhp1);

		// Create people
		PersonAgent p1Zhang = new PersonAgent("Cashier 1 Zhang", date);
		PersonAgent p2Zhang = new PersonAgent("Cook 1 Zhang", date);
		PersonAgent p3Zhang = new PersonAgent("Host 1 Zhang", date);
		PersonAgent p4Zhang = new PersonAgent("Waiter 1 Zhang", date);
		people.add(p1Zhang);
		people.add(p2Zhang);
		people.add(p3Zhang);
		people.add(p4Zhang);
		p1Zhang.setHome(h1Zhang);
		p2Zhang.setHome(h2Zhang);
		p3Zhang.setHome(h3Zhang);
		p4Zhang.setHome(h4Zhang);

		// Give people cars
		CarAgent c0Zhang = new CarAgent(busStop2);
		CarAnimation c0AnimZhang = new CarAnimation(c0Zhang, busStop2);
		c0Zhang.setAnimation(c0AnimZhang);
		mainFrame.cityView.addAnimation(c0AnimZhang);
		CarAgent c1Zhang = new CarAgent(busStop2);
		CarAnimation c1AnimZhang = new CarAnimation(c1Zhang, busStop2);
		c1Zhang .setAnimation(c1AnimZhang);
		mainFrame.cityView.addAnimation(c1AnimZhang);
		CarAgent c2Zhang = new CarAgent(busStop2);
		CarAnimation c2AnimZhang = new CarAnimation(c2Zhang, busStop2);
		c2Zhang.setAnimation(c2AnimZhang);
		mainFrame.cityView.addAnimation(c2AnimZhang);
		CarAgent c3Zhang = new CarAgent(busStop2);
		CarAnimation c3AnimZhang = new CarAnimation(c3Zhang, busStop2);
		c3Zhang.setAnimation(c3AnimZhang);
		mainFrame.cityView.addAnimation(c3AnimZhang);
		CarAgent c4Zhang = new CarAgent(busStop2);
		CarAnimation c4AnimZhang = new CarAnimation(c4Zhang, busStop2);
		c4Zhang.setAnimation(c4AnimZhang);
		mainFrame.cityView.addAnimation(c4AnimZhang);
		//p0.setCar(c0);
		p1Zhang.setCar(c1Zhang);
		p2Zhang.setCar(c2Zhang);
		p3Zhang.setCar(c3Zhang);
		p4Zhang.setCar(c4Zhang);

		// Create cashier
		RestaurantZhangCashierRole p1r1Zhang = new RestaurantZhangCashierRole(rzb1, 0, 100); // TODO Change shift times
		rzb1.addRole(p1r1Zhang);
		p1Zhang.setOccupation(p1r1Zhang);

		// Create cook
		RestaurantZhangCookRole p2r1Zhang = new RestaurantZhangCookRole(rzb1, 0, 100); // TODO Change shift times
		rzb1.addRole(p2r1Zhang);
		p2Zhang.setOccupation(p2r1Zhang);

		// Create host
		RestaurantZhangHostRole p3r1Zhang = new RestaurantZhangHostRole(rzb1, 0, 100); // TODO Change shift times
		rzb1.addRole(p3r1Zhang);
		p3Zhang.setOccupation(p3r1Zhang);

		// Create waiter
		RestaurantZhangWaiterSharedDataRole p4r1Zhang = new RestaurantZhangWaiterSharedDataRole(rzb1, 0, 100); // TODO Change shift times
		rzb1.addRole(p4r1Zhang);
		p4Zhang.setOccupation(p4r1Zhang);

		// RESTAURANTTIMMS---------------------------------------------------------------------------------------
		// Create panels
		RestaurantTimmsPanel rtp1 = new RestaurantTimmsPanel(Color.GRAY, new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		CityViewRestaurant cvr1 = new CityViewRestaurant(150, 150, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.cyan, rtp1); 
		mainFrame.cityView.addStatic(cvr1);
		// Skipping creating a bank panel
		HousePanel rhp1Timms = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529), new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		// Create buildings
		RestaurantTimmsBuilding rtb = (RestaurantTimmsBuilding) Application.CityMap.addBuilding(BUILDING.restaurant, new RestaurantTimmsBuilding("RestaurantTimms", rtp1, cvr1));
		mainFrame.buildingView.addView(rtp1, cvr1.ID);
		CityMap.addBuilding(BUILDING.restaurant, rtb);
		Application.CityMap.addBuilding(BUILDING.bank, new BankBuilding("BankBuilding"));
		// Skipping creating a house

		// Create tables
		int i = 0;
		while (i < 9) {
			rtp1.addVisualizationElement(new RestaurantTimmsTableAnimation(i));
			i++;
		}

		// Create landlord
		PersonAgent p0Timms = new PersonAgent("Landlord Timms", date);
		LandlordRole p0r1Timms = new LandlordRole();
		p0Timms.addRole(p0r1Timms);
		HouseBuilding h0Timms = new HouseBuilding("House 0 Timms", p0r1Timms, rhp1Timms);
		p0Timms.setHome(h0Timms);
		p0Timms.setCash(10);
		p0r1Timms.setActive();
		people.add(p0Timms);

		// Create houses
		HouseBuilding h1Timms = new HouseBuilding("House 1 Timms", p0r1Timms, rhp1Timms);
		HouseBuilding h2Timms = new HouseBuilding("House 2 Timms", p0r1Timms, rhp1Timms);
		HouseBuilding h3Timms = new HouseBuilding("House 3 Timms", p0r1Timms, rhp1Timms);
		HouseBuilding h4Timms = new HouseBuilding("House 4 Timms", p0r1Timms, rhp1Timms);

		// Create people
		PersonAgent p1Timms = new PersonAgent("Cashier 1 Timms", date);
		PersonAgent p2Timms = new PersonAgent("Cook 1 Timms", date);
		PersonAgent p3Timms = new PersonAgent("Host 1 Timms", date);
		PersonAgent p4Timms = new PersonAgent("Waiter 1 Timms", date);
		people.add(p1Timms);
		people.add(p2Timms);
		people.add(p3Timms);
		people.add(p4Timms);
		p1Timms.setHome(h1Timms);
		p2Timms.setHome(h2Timms);
		p3Timms.setHome(h3Timms);
		p4Timms.setHome(h4Timms);

		// Give people cars
		CarAgent c0Timms = new CarAgent(busStop3);
		CarAnimation c0AnimTimms = new CarAnimation(c0Timms, busStop3);
		c0Timms.setAnimation(c0AnimTimms);
		mainFrame.cityView.addAnimation(c0AnimTimms);
		CarAgent c1Timms = new CarAgent(busStop3);
		CarAnimation c1AnimTimms = new CarAnimation(c1Timms, busStop3);
		c1Timms.setAnimation(c1AnimTimms);
		mainFrame.cityView.addAnimation(c1AnimTimms);
		CarAgent c2Timms = new CarAgent(busStop3);
		CarAnimation c2AnimTimms = new CarAnimation(c2Timms, busStop3);
		c2Timms.setAnimation(c2AnimTimms);
		mainFrame.cityView.addAnimation(c2AnimTimms);
		CarAgent c3Timms = new CarAgent(busStop3);
		CarAnimation c3AnimTimms = new CarAnimation(c3Timms, busStop3);
		c3Timms.setAnimation(c3AnimTimms);
		mainFrame.cityView.addAnimation(c3AnimTimms);
		CarAgent c4Timms = new CarAgent(busStop3);
		CarAnimation c4AnimTimms = new CarAnimation(c4Timms, busStop3);
		c4Timms.setAnimation(c4AnimTimms);
		mainFrame.cityView.addAnimation(c4AnimTimms);
		p0Timms.setCar(c0Timms);
		p1Timms.setCar(c1Timms);
		p2Timms.setCar(c2Timms);
		p3Timms.setCar(c3Timms);
		p4Timms.setCar(c4Timms);

		// Create cashier
		RestaurantTimmsCashierRole p1r1Timms = new RestaurantTimmsCashierRole(rtb, 0, 100); // TODO Change shift times
		rtb.addRole(p1r1Timms);
		p1Timms.setOccupation(p1r1Timms);

		// Create cook
		RestaurantTimmsCookRole p2r1Timms = new RestaurantTimmsCookRole(rtb, 0, 100); // TODO Change shift times
		rtb.addRole(p2r1Timms);
		p2Timms.setOccupation(p2r1Timms);

		// Create host
		RestaurantTimmsHostRole p3r1Timms = new RestaurantTimmsHostRole(rtb, 0, 100); // TODO Change shift times
		rtb.addRole(p3r1Timms);
		p3Timms.setOccupation(p3r1Timms);

		// Create waiter
		RestaurantTimmsWaiterRole p4r1Timms = new RestaurantTimmsWaiterRole(rtb, 0, 100); // TODO Change shift times
		rtb.addRole(p4r1Timms);
		p4Timms.setOccupation(p4r1Timms);

		/*
		//RESTAURANTJP------------------------------------------------------------------------
		// FIRST add a panel
		RestaurantJPPanel rjpp1 = new RestaurantJPPanel(Color.DARK_GRAY, new Dimension(mainFrame.cityView.CITY_WIDTH, mainFrame.cityView.CITY_HEIGHT));
		// SECOND create a city view restaurant, the above panel is the last argument
		CityViewRestaurant restaurantJP1 = new CityViewRestaurant(400, 200, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.green, rjpp1); 
		// THIRD add it to the list of statics in the cityView
		mainFrame.cityView.addStatic(restaurantJP1);
		// FOURTH create a new building, last argument is the panel in step ONE
		RestaurantJPBuilding rjpb1 = new RestaurantJPBuilding("RestaurantJP1", rjpp1);
		// FIFTH add the new building to the buildingView
		mainFrame.buildingView.addView(rjpp1, restaurantJP1.ID);
		// SIXTH add the new building to the map
		CityMap.addBuilding(BUILDING.restaurant, rjpb1);
		// SEVENTH add roles
		PersonAgent p0JP1 = new PersonAgent("Landlord JP", date);
		LandlordRole p0r1JP1 = new LandlordRole();
		p0JP1.addRole(p0r1JP1);
		HouseBuilding h0JP = new HouseBuilding("House 0 JP", p0r1JP1, rhp1);
		p0JP1.setHome(h0JP);
		p0r1JP1.setActive();
		people.add(p0JP1);

		// Create houses
		HouseBuilding h1JP = new HouseBuilding("House 1 JP", p0r1JP1, rhp1);
		HouseBuilding h2JP = new HouseBuilding("House 2 JP", p0r1JP1, rhp1);
		HouseBuilding h3JP = new HouseBuilding("House 3 JP", p0r1JP1, rhp1);
		HouseBuilding h4JP = new HouseBuilding("House 4 JP", p0r1JP1, rhp1);

		// Create people
		PersonAgent p1JP = new PersonAgent("Cashier 1 JP", date);
		PersonAgent p2JP = new PersonAgent("Cook 1 JP", date);
		PersonAgent p3JP = new PersonAgent("Host 1 JP", date);
		PersonAgent p4JP = new PersonAgent("Waiter 1 JP", date);
		people.add(p1JP);
		people.add(p2JP);
		people.add(p3JP);
		people.add(p4JP);
		p1JP.setHome(h1JP);
		p2JP.setHome(h2JP);
		p3JP.setHome(h3JP);
		p4JP.setHome(h4JP);

		// Give people cars
		CarAgent c0JP = new CarAgent(busStop3);
		CarAnimation c0AnimJP = new CarAnimation(c0JP, busStop3);
		c0JP.setAnimation(c0AnimJP);
		mainFrame.cityView.addAnimation(c0AnimJP);
		CarAgent c1JP = new CarAgent(busStop3);
		CarAnimation c1AnimJP = new CarAnimation(c1JP, busStop3);
		c1JP .setAnimation(c1AnimJP);
		mainFrame.cityView.addAnimation(c1AnimJP);
		CarAgent c2JP = new CarAgent(busStop3);
		CarAnimation c2AnimJP = new CarAnimation(c2JP, busStop3);
		c2JP.setAnimation(c2AnimJP);
		mainFrame.cityView.addAnimation(c2AnimJP);
		CarAgent c3JP = new CarAgent(busStop3);
		CarAnimation c3AnimJP = new CarAnimation(c3JP, busStop3);
		c3JP.setAnimation(c3AnimJP);
		mainFrame.cityView.addAnimation(c3AnimJP);
		CarAgent c4JP = new CarAgent(busStop2);
		CarAnimation c4AnimJP = new CarAnimation(c4JP, busStop3);
		c4JP.setAnimation(c4AnimJP);
		mainFrame.cityView.addAnimation(c4AnimJP);
		//p0.setCar(c0);
		p1JP.setCar(c1JP);
		p2JP.setCar(c2JP);
		p3JP.setCar(c3JP);
		p4JP.setCar(c4JP);

		// Create cashier
		RestaurantJPCashierRole p1r1JP = new RestaurantJPCashierRole(rjpb1, 0, 100); // TODO Change shift times
		rjpb1.addRole(p1r1JP);
		p1JP.setOccupation(p1r1JP);

		// Create cook
		RestaurantJPCookRole p2r1JP = new RestaurantJPCookRole(rjpb1, 0, 100); // TODO Change shift times
		rjpb1.addRole(p2r1JP);
		p2JP.setOccupation(p2r1JP);

		// Create host
		RestaurantJPHostRole p3r1JP = new RestaurantJPHostRole(rjpb1, 0, 100); // TODO Change shift times
		rjpb1.addRole(p3r1JP);
		p3JP.setOccupation(p3r1JP);

		// Create waiter
		RestaurantJPWaiterRole p4r1JP = new RestaurantJPWaiterRole(rjpb1, 0, 100); // TODO Change shift times
		rjpb1.addRole(p4r1JP);
		p4JP.setOccupation(p4r1JP);

		/*
		// Create landlord
		PersonAgent p1 = new PersonAgent("Cashier 1", date);
		RestaurantJPCashierRole p1r1 = new RestaurantJPCashierRole(rjpb1, 0, 12);
		p1.setOccupation(p1r1);
		people.add(p1);
		p1.startThread();         

		PersonAgent p2 = new PersonAgent("Cook 1", date);
		RestaurantJPCookRole p2r1 = new RestaurantJPCookRole(rjpb1, 0, 12);
		RestaurantJPCookAnimation p2a1 = new RestaurantJPCookAnimation(p2r1);
		p2r1.setAnimation(p2a1);
		rjpp1.addVisualizationElement(p2a1);
		p2.setOccupation(p2r1);
		people.add(p2);
		p2.startThread();

		PersonAgent p3 = new PersonAgent("Host 1", date);
		RestaurantJPHostRole p3r1 = new RestaurantJPHostRole(rjpb1, 0, 12);
		p3.setOccupation(p3r1);
		people.add(p3);
		p3.startThread();

		PersonAgent p4 = new PersonAgent("Waiter 1", date);
		RestaurantJPWaiterRole p4r1 = new RestaurantJPWaiterRole(rjpb1, 0, 12);
		RestaurantJPWaiterAnimation p4a1 = new RestaurantJPWaiterAnimation(p4r1, 0);
		p4r1.setAnimation(p4a1);
		p3r1.addWaiter(p4r1, "W1");
		rjpp1.addVisualizationElement(p4a1);
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
		RestaurantJPCustomerRole p5r1 = new RestaurantJPCustomerRole(rjpb1);
		RestaurantJPCustomerAnimation p5a1 = new RestaurantJPCustomerAnimation(p5r1, 0);
		p5r1.setAnimation(p5a1);
		rjpp1.addVisualizationElement(p5a1);
		p5.addRole(p5r1);
		people.add(p5);
		p5.startThread();
		p5r1.gotHungry();

		// TODO these shouldn't be necessary, figure out why they're needed
		p5r1.setActive();
		p5.stateChanged();


		 */








		// Wait for stuff to get set up
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Start threads
		c0Timms.startThread();
		c1Timms.startThread();
		c2Timms.startThread();
		c3Timms.startThread();
		c4Timms.startThread();
		p0Timms.startThread();
		p1Timms.startThread();
		p2Timms.startThread();
		p3Timms.startThread();
		p4Timms.startThread();

		// Wait for stuff to get set up
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Start threads for RestaurantZhang
		c0Zhang.startThread();
		c1Zhang.startThread();
		c2Zhang.startThread();
		c3Zhang.startThread();
		c4Zhang.startThread();
		p0Zhang.startThread();
		p1Zhang.startThread();
		p2Zhang.startThread();
		p3Zhang.startThread();
		p4Zhang.startThread();
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
		public static Building findClosestBuilding() {
			return null;
		}

		/**
		 * Return the building of type closest to the destination building
		 * 
		 * @param b the destination you wish to reach
		 */
		public static Building findClosestBuilding(BUILDING type, Building b) {
			int x = b.getCityViewBuilding().x;
			int y = b.getCityViewBuilding().y;
			double closestDistance = 1000000;
			Building returnBuilding = null;
			for(Building tempBuilding : map.get(type)) {
				double distance = Math.sqrt((double)(Math.pow(tempBuilding.getCityViewBuilding().x - x, 2) + Math.pow(tempBuilding.getCityViewBuilding().y - y, 2)));
				if( distance < closestDistance) {
					closestDistance = distance;
					returnBuilding = tempBuilding;
				}
			}
			return returnBuilding;
		}

		public static Building findClosestBuilding(BUILDING type, PersonAgent p) {
			int x = 100; // p.animation.getXPos(); // TODO RestaurantZhang 92f655cfd5
			int y = 100; // p.animation.getYPos(); // TODO RestaurantZhang 92f655cfd5
			double closestDistance = 1000000;
			Building returnBuilding = null;
			for(Building b : map.get(type)) {
				double distance = Math.sqrt((double)(Math.pow(b.getCityViewBuilding().x - x, 2) + Math.pow(b.getCityViewBuilding().y - y, 2)));
				if( distance < closestDistance) {
					closestDistance = distance;
					returnBuilding = b;
				}
			}
			return returnBuilding;
		}

		public static CityRoad findClosestRoad(Building b) {
			int x = b.getCityViewBuilding().x;
			int y = b.getCityViewBuilding().y;
			double closestDistance = 1000000;
			CityRoad returnRoad = null;
			for(CityRoad r : roads) {
				double distance = Math.sqrt((double)(Math.pow(r.x - x, 2) + Math.pow(r.y - y, 2)));
				if( distance < closestDistance) {
					closestDistance = distance;
					returnRoad = r;
				}
			}
			return returnRoad;
		}
	}

}
