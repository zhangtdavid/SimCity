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

import sun.org.mozilla.javascript.internal.ast.ForInLoop;
import city.agents.BusAgent;
import city.agents.CarAgent;
import city.agents.PersonAgent;
import city.animations.BusAnimation;
import city.animations.CarAnimation;
import city.animations.RestaurantTimmsTableAnimation;
import city.buildings.ApartmentBuilding;
import city.buildings.BankBuilding;
import city.buildings.BusStopBuilding;
import city.buildings.HouseBuilding;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantChoiBuilding;
import city.buildings.RestaurantChungBuilding;
import city.buildings.RestaurantJPBuilding;
import city.buildings.RestaurantTimmsBuilding;
import city.buildings.RestaurantZhangBuilding;
import city.gui.CityRoad;
import city.gui.CityViewPanel;
import city.gui.MainFrame;
import city.gui.buildings.BankPanel;
import city.gui.buildings.BusStopPanel;
import city.gui.buildings.HousePanel;
import city.gui.buildings.MarketPanel;
import city.gui.buildings.RestaurantChoiPanel;
import city.gui.buildings.RestaurantChungPanel;
import city.gui.buildings.RestaurantJPPanel;
import city.gui.buildings.RestaurantTimmsPanel;
import city.gui.buildings.RestaurantZhangPanel;
import city.gui.views.CityViewBank;
import city.gui.views.CityViewBusStop;
import city.gui.views.CityViewHouse;
import city.gui.views.CityViewMarket;
import city.gui.views.CityViewRestaurant;
import city.interfaces.Person;
import city.roles.BankManagerRole;
import city.roles.BankTellerRole;
import city.roles.LandlordRole;
import city.roles.MarketCashierRole;
import city.roles.MarketDeliveryPersonRole;
import city.roles.MarketEmployeeRole;
import city.roles.MarketManagerRole;
import city.roles.RestaurantChoiCashierRole;
import city.roles.RestaurantChoiCookRole;
import city.roles.RestaurantChoiHostRole;
import city.roles.RestaurantChoiWaiterQueueRole;
import city.roles.RestaurantChungCashierRole;
import city.roles.RestaurantChungCookRole;
import city.roles.RestaurantChungHostRole;
import city.roles.RestaurantChungWaiterMessageCookRole;
import city.roles.RestaurantJPCashierRole;
import city.roles.RestaurantJPCookRole;
import city.roles.RestaurantJPHostRole;
import city.roles.RestaurantJPWaiterRole;
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

	public static final int HALF_HOUR = 1800000; // A half hour in milliseconds
	public static final int INTERVAL = 1000; // One interval is the simulation's equivalent of a half-hour
	public static final int PAYCHECK_INTERVAL = 0; // TODO set the global interval at which people are paid
	public static enum BANK_SERVICE {none, deposit, moneyWithdraw, atmDeposit};
	public static enum TRANSACTION_TYPE {personal, business};
	public static enum FOOD_ITEMS {steak, chicken, salad, pizza};
	public static enum BUILDING {bank, busStop, house, market, restaurant};
	public static Dimension stdDim = new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT);

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
				date.setTime(date.getTime() + HALF_HOUR);
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
		BusStopPanel bsp1 = new BusStopPanel(Color.white, new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		CityViewBusStop cityViewBusStop1 = new CityViewBusStop(250, 50, "Bus Stop " + (mainFrame.cityView.getStaticsSize()), Color.white, bsp1);
		mainFrame.cityView.addStatic(cityViewBusStop1);
		BusStopBuilding busStop1 = new BusStopBuilding("Bus Stop 1", bsp1, cityViewBusStop1);
		mainFrame.buildingView.addView(bsp1, cityViewBusStop1.ID);
		Application.CityMap.addBuilding(BUILDING.busStop, busStop1);

		BusStopPanel bsp2 = new BusStopPanel(Color.white, new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		CityViewBusStop cityViewBusStop2 = new CityViewBusStop(50, 300, "Bus Stop " + (mainFrame.cityView.getStaticsSize()), Color.white, bsp2);
		mainFrame.cityView.addStatic(cityViewBusStop2);
		BusStopBuilding busStop2 = new BusStopBuilding("Bus Stop 2", bsp2, cityViewBusStop2);
		mainFrame.buildingView.addView(bsp2, cityViewBusStop2.ID);
		Application.CityMap.addBuilding(BUILDING.busStop, busStop2); 

		BusStopPanel bsp3 = new BusStopPanel(Color.white, new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		CityViewBusStop cityViewBusStop3 = new CityViewBusStop(300, 375, "Bus Stop " + (mainFrame.cityView.getStaticsSize()), Color.white, bsp3);
		mainFrame.cityView.addStatic(cityViewBusStop3);
		BusStopBuilding busStop3 = new BusStopBuilding("Bus Stop 3", bsp3, cityViewBusStop3);
		mainFrame.buildingView.addView(bsp3, cityViewBusStop3.ID);
		Application.CityMap.addBuilding(BUILDING.busStop, busStop3); 

		BusStopPanel bsp4 = new BusStopPanel(Color.white, new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
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
		RestaurantZhangPanel rzp1 = new RestaurantZhangPanel(Color.DARK_GRAY, new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
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

		// Create houses
		HousePanel[] hp = new HousePanel[30];
		for(int i = 0; i < hp.length; i++){ // all house panels have the same background?
			hp[i] = new HousePanel(Color.getHSBColor((float)37, (float).53, (float).529), stdDim);
		}
		
		// Create landlord
		PersonAgent p0Zhang = new PersonAgent("Landlord Zhang", date);
		LandlordRole p0r1Zhang = new LandlordRole();
		p0Zhang.addRole(p0r1Zhang);
		CityViewHouse cityViewHouseZ1 = new CityViewHouse(300, 50, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[0]);
		CityViewHouse cityViewHouseZ2 = new CityViewHouse(305, 55, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[1]);
		CityViewHouse cityViewHouseZ3 = new CityViewHouse(310, 60, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[2]);
		CityViewHouse cityViewHouseZ4 = new CityViewHouse(315, 65, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[3]);
		CityViewHouse cityViewHouseZ5 = new CityViewHouse(320, 70, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[4]);
		HouseBuilding h0 = new HouseBuilding("House 0 Zhang", p0r1Zhang, hp[0], cityViewHouseZ1);
		p0Zhang.setHome(h0);
		p0r1Zhang.setActive();
		people.add(p0Zhang);
		
		HouseBuilding h1Zhang = new HouseBuilding("House 1 Zhang", p0r1Zhang, hp[1], cityViewHouseZ2);
		HouseBuilding h2Zhang = new HouseBuilding("House 2 Zhang", p0r1Zhang, hp[2], cityViewHouseZ3);
		HouseBuilding h3Zhang = new HouseBuilding("House 3 Zhang", p0r1Zhang, hp[3], cityViewHouseZ4);
		HouseBuilding h4Zhang = new HouseBuilding("House 4 Zhang", p0r1Zhang, hp[4], cityViewHouseZ5);

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
		rzb1.addOccupyingRole(p1r1Zhang);
		p1Zhang.setOccupation(p1r1Zhang);

		// Create cook
		RestaurantZhangCookRole p2r1Zhang = new RestaurantZhangCookRole(rzb1, 0, 100); // TODO Change shift times
		rzb1.addOccupyingRole(p2r1Zhang);
		p2Zhang.setOccupation(p2r1Zhang);

		// Create host
		RestaurantZhangHostRole p3r1Zhang = new RestaurantZhangHostRole(rzb1, 0, 100); // TODO Change shift times
		rzb1.addOccupyingRole(p3r1Zhang);
		p3Zhang.setOccupation(p3r1Zhang);

		// Create waiter
		RestaurantZhangWaiterSharedDataRole p4r1Zhang = new RestaurantZhangWaiterSharedDataRole(rzb1, 0, 100); // TODO Change shift times
		rzb1.addOccupyingRole(p4r1Zhang);
		p4Zhang.setOccupation(p4r1Zhang);

		// RESTAURANTTIMMS---------------------------------------------------------------------------------------
		// Create panels
		RestaurantTimmsPanel rtp1 = new RestaurantTimmsPanel(Color.GRAY, new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		CityViewRestaurant cvr1 = new CityViewRestaurant(150, 150, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.cyan, rtp1); 
		mainFrame.cityView.addStatic(cvr1);
		// Skipping creating a bank panel
		// Create buildings
		RestaurantTimmsBuilding rtb = new RestaurantTimmsBuilding("RestaurantTimms", rtp1, cvr1);
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
		CityViewHouse cityViewHouseT1 = new CityViewHouse(325, 75, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[5]);
		CityViewHouse cityViewHouseT2 = new CityViewHouse(330, 80, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[5]);
		CityViewHouse cityViewHouseT3 = new CityViewHouse(335, 85, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[5]);
		CityViewHouse cityViewHouseT4 = new CityViewHouse(340, 90, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[5]);
		CityViewHouse cityViewHouseT5 = new CityViewHouse(345, 95, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[5]);
		HouseBuilding h0Timms = new HouseBuilding("House 0 Timms", p0r1Timms, hp[5], cityViewHouseT1);
		p0Timms.setHome(h0Timms);
		p0Timms.setCash(10);
		p0r1Timms.setActive();
		people.add(p0Timms);

		// Create houses
		HouseBuilding h1Timms = new HouseBuilding("House 1 Timms", p0r1Timms, hp[6], cityViewHouseT2);
		HouseBuilding h2Timms = new HouseBuilding("House 2 Timms", p0r1Timms, hp[7], cityViewHouseT3);
		HouseBuilding h3Timms = new HouseBuilding("House 3 Timms", p0r1Timms, hp[8], cityViewHouseT4);
		HouseBuilding h4Timms = new HouseBuilding("House 4 Timms", p0r1Timms, hp[9], cityViewHouseT5);

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
		rtb.addOccupyingRole(p1r1Timms);
		p1Timms.setOccupation(p1r1Timms);

		// Create cook
		RestaurantTimmsCookRole p2r1Timms = new RestaurantTimmsCookRole(rtb, 0, 100); // TODO Change shift times
		rtb.addOccupyingRole(p2r1Timms);
		p2Timms.setOccupation(p2r1Timms);

		// Create host
		RestaurantTimmsHostRole p3r1Timms = new RestaurantTimmsHostRole(rtb, 0, 100); // TODO Change shift times
		rtb.addOccupyingRole(p3r1Timms);
		p3Timms.setOccupation(p3r1Timms);

		// Create waiter
		RestaurantTimmsWaiterRole p4r1Timms = new RestaurantTimmsWaiterRole(rtb, 0, 100); // TODO Change shift times
		rtb.addOccupyingRole(p4r1Timms);
		p4Timms.setOccupation(p4r1Timms);

		// RESTAURANTCHOI----------------------------------------------------------------------------
		Application.CityMap.addBuilding(BUILDING.bank, new BankBuilding("BankBuilding"));
		MarketPanel mp1 = new MarketPanel(Color.black, new Dimension(500,500));
		MarketBuilding m1 = new MarketBuilding("MarketBuilding", mp1);
		// Create buildings
		BankPanel bp1 = new BankPanel(Color.blue, new Dimension(500,500));
		BankBuilding b1 = new BankBuilding("BankBuilding");

		// FIRST add a panel
		RestaurantChoiPanel rchoip1 = new RestaurantChoiPanel(Color.GRAY, new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		// SECOND create a city view restaurant, the above panel is the last argument
		CityViewRestaurant restaurantChoi1 = new CityViewRestaurant(200, 200, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.cyan, rchoip1);
		CityViewBank bank1 = new CityViewBank(300,250,"Bank " + (mainFrame.cityView.getStaticsSize()), Color.green, bp1);
		CityViewMarket market1 = new CityViewMarket(400,400,"Market " + (mainFrame.cityView.getStaticsSize()), Color.yellow, mp1);
		// THIRD add it to the list of statics in the cityView
		mainFrame.cityView.addStatic(restaurantChoi1);
		//mainFrame.cityView.addStatic(house1);
		mainFrame.cityView.addStatic(bank1);
		mainFrame.cityView.addStatic(market1);
		// FOURTH create a new building, last argument is the panel in step ONE
		RestaurantChoiBuilding rchoib1 = new RestaurantChoiBuilding("RestaurantChoi1", rchoip1, restaurantChoi1);
		// FIFTH add the new building to the buildingView
		mainFrame.buildingView.addView(rchoip1, restaurantChoi1.ID);
		mainFrame.buildingView.addView(bp1, bank1.ID);
		mainFrame.buildingView.addView(mp1, market1.ID);
		// SIXTH map stuff
		CityMap.addBuilding(BUILDING.restaurant, rchoib1);
		CityMap.addBuilding(BUILDING.bank, b1);
		CityMap.addBuilding(BUILDING.market, m1);

		// Create landlord
		PersonAgent p0Choi = new PersonAgent("Landlord Choi", date);
		LandlordRole p0r1Choi = new LandlordRole();
		p0Choi.addRole(p0r1Choi);
		CityViewHouse cityViewHouseR1 = new CityViewHouse(350, 100, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[10]);
		CityViewHouse cityViewHouseR2 = new CityViewHouse(355, 105, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[11]);
		CityViewHouse cityViewHouseR3 = new CityViewHouse(360, 110, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[12]);
		CityViewHouse cityViewHouseR4 = new CityViewHouse(365, 115, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[13]);
		CityViewHouse cityViewHouseR5 = new CityViewHouse(370, 120, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[14]);
		HouseBuilding h0Choi = new HouseBuilding("House 0 Choi", p0r1Choi, hp[10], cityViewHouseR1);
		p0Choi.setHome(h0Choi);
		p0r1Choi.setActive();
		people.add(p0Choi);

		// Create houses
		HouseBuilding h1Choi = new HouseBuilding("House 1 Choi", p0r1Choi, hp[11], cityViewHouseR2); // name, landlord, panel.
		HouseBuilding h2Choi = new HouseBuilding("House 2 Choi", p0r1Choi, hp[12], cityViewHouseR3);
		HouseBuilding h3Choi = new HouseBuilding("House 3 Choi", p0r1Choi, hp[13], cityViewHouseR4);
		HouseBuilding h4Choi = new HouseBuilding("House 4 Choi", p0r1Choi, hp[14], cityViewHouseR5);
		ApartmentBuilding app0Choi = new ApartmentBuilding("AptBuilding Choi", p0r1Choi); // this landlord owns everything!
		ApartmentBuilding app1Choi = new ApartmentBuilding("AptBuilding Choi", p0r1Choi); // this landlord owns everything!

		// Create people
		PersonAgent p1Choi = new PersonAgent("Cashier 1 Choi", date);
		PersonAgent p2Choi = new PersonAgent("Cook 1 Choi", date);
		PersonAgent p3Choi = new PersonAgent("Host 1 Choi", date);
		PersonAgent p4Choi = new PersonAgent("Waiter 1 Choi", date);
		PersonAgent p5Choi = new PersonAgent("Market Mgr Choi", date);
		PersonAgent p6Choi = new PersonAgent("Market Cshr Choi", date);
		PersonAgent p7Choi = new PersonAgent("Market Emp Choi", date);
		PersonAgent p8Choi = new PersonAgent("Market Dlvry Choi", date);
		PersonAgent p9Choi = new PersonAgent("Bank manager Choi", date);
		PersonAgent p10Choi = new PersonAgent("Bank Teller Choi", date);

		people.add(p1Choi);
		people.add(p2Choi);
		people.add(p3Choi);
		people.add(p4Choi);
		people.add(p5Choi);
		people.add(p6Choi);
		people.add(p7Choi);
		people.add(p8Choi);
		people.add(p9Choi);
		people.add(p10Choi);

		p1Choi.setHome(h1Choi);
		p2Choi.setHome(h2Choi);
		p3Choi.setHome(h3Choi);
		p4Choi.setHome(h4Choi);
		p5Choi.setHome(app0Choi);
		p6Choi.setHome(app0Choi);
		p7Choi.setHome(app0Choi);
		p8Choi.setHome(app0Choi);
		p9Choi.setHome(app1Choi);
		p10Choi.setHome(app1Choi);

		// Landlord
		RestaurantChoiCashierRole p1r1Choi = new RestaurantChoiCashierRole(rchoib1, 0, 24);
		rchoib1.addOccupyingRole(p1r1Choi);
		p1Choi.setOccupation(p1r1Choi);

		// Create cook
		RestaurantChoiCookRole p2r1Choi = new RestaurantChoiCookRole(rchoib1, 0, 24);
		rchoib1.addOccupyingRole(p2r1Choi);
		p2Choi.setOccupation(p2r1Choi);
		p2r1Choi.addMarket(m1);

		// Create host
		RestaurantChoiHostRole p3r1Choi = new RestaurantChoiHostRole(rchoib1, 0, 24);
		rchoib1.addOccupyingRole(p3r1Choi);
		p3Choi.setOccupation(p3r1Choi);

		// Create waiter
		RestaurantChoiWaiterQueueRole p4r1Choi = new RestaurantChoiWaiterQueueRole(rchoib1, 0, 24);
		rchoib1.addOccupyingRole(p4r1Choi);
		p4Choi.setOccupation(p4r1Choi);

		//Create bank roles

		BankManagerRole p9r1Choi = new BankManagerRole(b1, 0, 24);
		p9Choi.setOccupation(p9r1Choi);
		p9r1Choi.setPerson(p9Choi);
		BankTellerRole p10r1Choi = new BankTellerRole(b1, 0, 24);
		p10Choi.setOccupation(p10r1Choi);
		p10r1Choi.setPerson(p10Choi);
		b1.addOccupyingRole(p9r1Choi);
		b1.addOccupyingRole(p10r1Choi);

		//Create Market people
		MarketManagerRole p5r1Choi = new MarketManagerRole(m1, 0, 24);
		MarketCashierRole p6r1Choi = new MarketCashierRole(m1, 0, 24);
		MarketEmployeeRole p7r1Choi = new MarketEmployeeRole(m1, 0, 24);
		MarketDeliveryPersonRole p8r1Choi = new MarketDeliveryPersonRole(m1, 0, 24);
		p5Choi.setOccupation(p5r1Choi);
		p5r1Choi.setPerson(p5Choi);
		p6Choi.setOccupation(p6r1Choi);
		p6r1Choi.setPerson(p6Choi);
		p7Choi.setOccupation(p7r1Choi);
		p7r1Choi.setPerson(p7Choi);
		p8r1Choi.setPerson(p8Choi);
		p8Choi.setOccupation(p8r1Choi);
		m1.addOccupyingRole(p5r1Choi);
		m1.addOccupyingRole(p6r1Choi);
		m1.addOccupyingRole(p7r1Choi);
		m1.addOccupyingRole(p8r1Choi);
		m1.manager = p5r1Choi;
		m1.cashier = p6r1Choi;
		m1.addEmployee(p7r1Choi);
		m1.addDeliveryPerson(p8r1Choi);

		// Give people cars
		CarAgent c0Choi = new CarAgent(busStop4);
		CarAnimation c0AnimChoi = new CarAnimation(c0Choi, busStop4);
		c0Choi.setAnimation(c0AnimChoi);
		mainFrame.cityView.addAnimation(c0AnimChoi);
		CarAgent c1Choi = new CarAgent(busStop4);
		CarAnimation c1AnimChoi = new CarAnimation(c1Choi, busStop4);
		c1Choi.setAnimation(c1AnimChoi);
		mainFrame.cityView.addAnimation(c1AnimChoi);
		CarAgent c2Choi = new CarAgent(busStop4);
		CarAnimation c2AnimChoi = new CarAnimation(c2Choi, busStop4);
		c2Choi.setAnimation(c2AnimChoi);
		mainFrame.cityView.addAnimation(c2AnimChoi);
		CarAgent c3Choi = new CarAgent(busStop4);
		CarAnimation c3AnimChoi = new CarAnimation(c3Choi, busStop4);
		c3Choi.setAnimation(c3AnimChoi);
		mainFrame.cityView.addAnimation(c3AnimChoi);
		CarAgent c4Choi = new CarAgent(busStop4);
		CarAnimation c4AnimChoi = new CarAnimation(c4Choi, busStop4);
		c4Choi.setAnimation(c4AnimChoi);
		mainFrame.cityView.addAnimation(c4AnimChoi);
		CarAgent c5Choi = new CarAgent(busStop4);
		CarAnimation c5AnimChoi = new CarAnimation(c5Choi, busStop4);
		c5Choi.setAnimation(c5AnimChoi);
		mainFrame.cityView.addAnimation(c5AnimChoi);
		CarAgent c6Choi = new CarAgent(busStop4);
		CarAnimation c6AnimChoi = new CarAnimation(c6Choi, busStop4);
		c6Choi.setAnimation(c6AnimChoi);
		mainFrame.cityView.addAnimation(c6AnimChoi);
		CarAgent c7Choi = new CarAgent(busStop4);
		CarAnimation c7AnimChoi = new CarAnimation(c7Choi, busStop4);
		c7Choi.setAnimation(c7AnimChoi);
		mainFrame.cityView.addAnimation(c7AnimChoi);
		CarAgent c8Choi = new CarAgent(busStop4);
		CarAnimation c8AnimChoi = new CarAnimation(c8Choi, busStop4);
		c8Choi.setAnimation(c8AnimChoi);
		mainFrame.cityView.addAnimation(c8AnimChoi);
		CarAgent c9Choi = new CarAgent(busStop4);
		CarAnimation c9AnimChoi = new CarAnimation(c9Choi, busStop4);
		c9Choi.setAnimation(c9AnimChoi);
		mainFrame.cityView.addAnimation(c9AnimChoi);
		CarAgent c10Choi = new CarAgent(busStop4);
		CarAnimation c10AnimChoi = new CarAnimation(c10Choi, busStop4);
		c10Choi.setAnimation(c10AnimChoi);
		mainFrame.cityView.addAnimation(c10AnimChoi);

		p0Choi.setCar(c0Choi);
//		p1Choi.setCar(c1Choi);
//		p2Choi.setCar(c2Choi);
//		p3Choi.setCar(c3Choi);
//		p4Choi.setCar(c4Choi);
//		p5Choi.setCar(c5Choi);
		p6Choi.setCar(c6Choi);
		p7Choi.setCar(c7Choi);
		p8Choi.setCar(c8Choi);
		p9Choi.setCar(c9Choi);
		p10Choi.setCar(c10Choi);
		// RESTAURANTCHUNG------------------------------------------------------------------------------
		
		// RESTAURANTCHUNGTESTING FOR ANIMATION IN GUI
		RestaurantChungPanel rcp1 = new RestaurantChungPanel(Color.black, new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		// SECOND create a city view restaurant, the above panel is the last argument
		CityViewRestaurant restaurantChung1 = new CityViewRestaurant(400, 250, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.yellow, rcp1); 
		// THIRD add it to the list of statics in the cityView
		mainFrame.cityView.addStatic(restaurantChung1);
		// FOURTH create a new building, last argument is the panel in step ONE
		RestaurantChungBuilding rcb1 = new RestaurantChungBuilding("RestaurantChung1", rcp1, restaurantChung1);
		// FIFTH add the new building to the buildingView
		mainFrame.buildingView.addView(rcp1, restaurantChung1.ID);
		// SIXTH add the new building to the map
		CityMap.addBuilding(BUILDING.restaurant, rcb1);
		// SEVENTH create all your roles after
		
		// Create landlord
		PersonAgent p0Chung = new PersonAgent("Landlord Chung", date);
		System.out.println(p0Chung);
		System.out.println(p0Chung.getCash());
		p0Chung.setCash(50); // TODO remove later
		LandlordRole p0r1Chung = new LandlordRole();
		p0Chung.addRole(p0r1Chung);
		CityViewHouse cityViewHouseC1 = new CityViewHouse(375, 125, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[15]);
		CityViewHouse cityViewHouseC2 = new CityViewHouse(380, 130, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[16]);
		CityViewHouse cityViewHouseC3 = new CityViewHouse(385, 135, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[17]);
		CityViewHouse cityViewHouseC4 = new CityViewHouse(390, 140, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[18]);
		CityViewHouse cityViewHouseC5 = new CityViewHouse(395, 145, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[19]);
		                                                                                                                              
		                                                                                                                              
		HouseBuilding h0Chung = new HouseBuilding("House 0 Chung", p0r1Chung, hp[15], cityViewHouseC1);
		p0Chung.setHome(h0Chung);
		p0r1Chung.setActive();
		people.add(p0Chung);

		// Create houses
		HouseBuilding h1Chung = new HouseBuilding("House 1 Chung", p0r1Chung, hp[16], cityViewHouseC2);
		HouseBuilding h2Chung = new HouseBuilding("House 2 Chung", p0r1Chung, hp[17], cityViewHouseC3);
		HouseBuilding h3Chung = new HouseBuilding("House 3 Chung", p0r1Chung, hp[18], cityViewHouseC4);
		HouseBuilding h4Chung = new HouseBuilding("House 4 Chung", p0r1Chung, hp[19], cityViewHouseC5);

		// Create people
		PersonAgent p1Chung = new PersonAgent("Cashier 1 Chung", date);
		PersonAgent p2Chung = new PersonAgent("Cook 1 Chung", date);
		PersonAgent p3Chung = new PersonAgent("Host 1 Chung", date);
		PersonAgent p4Chung = new PersonAgent("Waiter 1 Chung", date);
		people.add(p1Chung);
		people.add(p2Chung);
		people.add(p3Chung);
		people.add(p4Chung);
		p1Chung.setHome(h1Chung);
		p2Chung.setHome(h2Chung);
		p3Chung.setHome(h3Chung);
		p4Chung.setHome(h4Chung);

		// Give people cars
		CarAgent c0Chung = new CarAgent(busStop1);
		CarAnimation c0AnimChung = new CarAnimation(c0Chung, busStop1);
		c0Chung.setAnimation(c0AnimChung);
		mainFrame.cityView.addAnimation(c0AnimChung);
		CarAgent c1Chung = new CarAgent(busStop1);
		CarAnimation c1AnimChung = new CarAnimation(c1Chung, busStop1);
		c1Chung.setAnimation(c1AnimChung);
		mainFrame.cityView.addAnimation(c1AnimChung);
		CarAgent c2Chung = new CarAgent(busStop1);
		CarAnimation c2AnimChung = new CarAnimation(c2Chung, busStop1);
		c2Chung.setAnimation(c2AnimChung);
		mainFrame.cityView.addAnimation(c2AnimChung);
		CarAgent c3Chung = new CarAgent(busStop1);
		CarAnimation c3AnimChung = new CarAnimation(c3Chung, busStop1);
		c3Chung.setAnimation(c3AnimChung);
		mainFrame.cityView.addAnimation(c3AnimChung);
		CarAgent c4Chung = new CarAgent(busStop1);
		CarAnimation c4AnimChung = new CarAnimation(c4Chung, busStop1);
		c4Chung.setAnimation(c4AnimChung);
		mainFrame.cityView.addAnimation(c4AnimChung);
		p0Chung.setCar(c0Chung);
		p1Chung.setCar(c1Chung);
		p2Chung.setCar(c2Chung);
		p3Chung.setCar(c3Chung);
		p4Chung.setCar(c4Chung);

		// Create cashier
		RestaurantChungCashierRole p1r1Chung = new RestaurantChungCashierRole(rcb1, 0, 12); // TODO Change shift times
		p1r1Chung.setPerson(p1Chung);
		p1r1Chung.setMarketCustomerDeliveryPaymentPerson();
		p1r1Chung.setBankCustomerPerson();
		rcb1.addOccupyingRole(p1r1Chung);
		p1Chung.setOccupation(p1r1Chung);
		
		// Create cook
		RestaurantChungCookRole p2r1Chung = new RestaurantChungCookRole(rcb1, 0, 12); // TODO Change shift times
		p2r1Chung.setPerson(p2Chung);		
		rcb1.addOccupyingRole(p2r1Chung);
		p2Chung.setOccupation(p2r1Chung);
		
		// Create host
		RestaurantChungHostRole p3r1Chung = new RestaurantChungHostRole(rcb1, 0, 12); // TODO Change shift times
		p3r1Chung.setPerson(p3Chung);		
		rcb1.addOccupyingRole(p3r1Chung);
		p3Chung.setOccupation(p3r1Chung);
		
		// Create waiter
		RestaurantChungWaiterMessageCookRole p4r1Chung = new RestaurantChungWaiterMessageCookRole(rcb1, 0, 12); // TODO Change shift times
		p4r1Chung.setPerson(p4Chung);		
		rcb1.addOccupyingRole(p4r1Chung);
		p4Chung.setOccupation(p4r1Chung);
		
		// Start threads

		//RESTAURANTJP------------------------------------------------------------------------
		// FIRST add a panel
		RestaurantJPPanel rjpp1 = new RestaurantJPPanel(Color.DARK_GRAY, new Dimension(CityViewPanel.CITY_WIDTH, CityViewPanel.CITY_HEIGHT));
		// SECOND create a city view restaurant, the above panel is the last argument
		CityViewRestaurant restaurantJP1 = new CityViewRestaurant(400, 200, "Restaurant " + (mainFrame.cityView.getStaticsSize()), Color.green, rjpp1); 
		// THIRD add it to the list of statics in the cityView
		mainFrame.cityView.addStatic(restaurantJP1);
		// FOURTH create a new building, last argument is the panel in step ONE
		RestaurantJPBuilding rjpb1 = new RestaurantJPBuilding("RestaurantJP1", rjpp1, restaurantJP1);
		// FIFTH add the new building to the buildingView
		mainFrame.buildingView.addView(rjpp1, restaurantJP1.ID);
		// SIXTH add the new building to the map
		CityMap.addBuilding(BUILDING.restaurant, rjpb1);
		// SEVENTH add roles
		PersonAgent p0JP1 = new PersonAgent("Landlord JP", date);
		LandlordRole p0r1JP1 = new LandlordRole();
		p0JP1.addRole(p0r1JP1);
		CityViewHouse cityViewHouseJ1 = new CityViewHouse(400, 150, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[20]);
		CityViewHouse cityViewHouseJ2 = new CityViewHouse(400, 150, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[21]);
		CityViewHouse cityViewHouseJ3 = new CityViewHouse(400, 150, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[22]);
		CityViewHouse cityViewHouseJ4 = new CityViewHouse(400, 150, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[23]);
		CityViewHouse cityViewHouseJ5 = new CityViewHouse(400, 150, "House " + (mainFrame.cityView.getStaticsSize()), Color.white, hp[24]);

		HouseBuilding h0JP = new HouseBuilding("House 0 JP", p0r1JP1, hp[20], cityViewHouseJ1);
		p0JP1.setHome(h0JP);
		p0r1JP1.setActive();
		people.add(p0JP1);

		// Create houses
		HouseBuilding h1JP = new HouseBuilding("House 1 JP", p0r1JP1, hp[21], cityViewHouseJ2);
		HouseBuilding h2JP = new HouseBuilding("House 2 JP", p0r1JP1, hp[22], cityViewHouseJ3);
		HouseBuilding h3JP = new HouseBuilding("House 3 JP", p0r1JP1, hp[23], cityViewHouseJ4);
		HouseBuilding h4JP = new HouseBuilding("House 4 JP", p0r1JP1, hp[24], cityViewHouseJ5);
		// Create people
		PersonAgent p1JP = new PersonAgent("Cashier 1 JP", date);
		PersonAgent p2JP = new PersonAgent("Cook 1 JP", date);
		PersonAgent p3JP = new PersonAgent("Host 1 JP", date);
		PersonAgent p4JP = new PersonAgent("Waiter 1 JP", date);
		PersonAgent p5JP = new PersonAgent("Customer 1 JP", date);
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
		rjpb1.addOccupyingRole(p1r1JP);
		p1JP.setOccupation(p1r1JP);

		// Create cook
		RestaurantJPCookRole p2r1JP = new RestaurantJPCookRole(rjpb1, 0, 100); // TODO Change shift times
		rjpb1.addOccupyingRole(p2r1JP);
		p2JP.setOccupation(p2r1JP);

		// Create host
		RestaurantJPHostRole p3r1JP = new RestaurantJPHostRole(rjpb1, 0, 100); // TODO Change shift times
		rjpb1.addOccupyingRole(p3r1JP);
		p3JP.setOccupation(p3r1JP);

		// Create waiter
		RestaurantJPWaiterRole p4r1JP = new RestaurantJPWaiterRole(rjpb1, 0, 100); // TODO Change shift times
		rjpb1.addOccupyingRole(p4r1JP);
		p4JP.setOccupation(p4r1JP);

		// Wait for stuff to get set up
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {}

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
		} catch (InterruptedException e) {}

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
		
		// Wait for stuff to get set up
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {}
		
		// Start threads for RestaurantChoi
		c0Choi.startThread();
		c1Choi.startThread();
		c2Choi.startThread();
		c3Choi.startThread();
		c4Choi.startThread();
		c5Choi.startThread();
		c6Choi.startThread();
		c7Choi.startThread();
		c8Choi.startThread();
		c9Choi.startThread();
		c10Choi.startThread();
		p0Choi.startThread();
		p1Choi.startThread();
		p2Choi.startThread();
		p3Choi.startThread();
		p4Choi.startThread();
		p5Choi.startThread();
		p6Choi.startThread();
		p7Choi.startThread();
		p8Choi.startThread();
		p9Choi.startThread();
		p10Choi.startThread();
		
		// Wait for stuff to get set up
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}
		
		// Start threads for RestaurantChung
		c0Chung.startThread();
		c1Chung.startThread();
		c2Chung.startThread();
		c3Chung.startThread();
		c4Chung.startThread();
		p0Chung.startThread();
		p1Chung.startThread();
		p2Chung.startThread();
		p3Chung.startThread();
		p4Chung.startThread();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {}
		
		c0JP.startThread();
		c1JP.startThread();
		c2JP.startThread();
		c3JP.startThread();
		c4JP.startThread();
		p0JP1.startThread();
		p1JP.startThread();
		p2JP.startThread();
		p3JP.startThread();
		p4JP.startThread();
		
	}

	public static class CityMap {
		private static HashMap<BUILDING, List<BuildingInterface>> map = new HashMap<BUILDING, List<BuildingInterface>>();
		public static int restaurantNumber = 0;
		
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
		public static BuildingInterface addBuilding(BUILDING type, BuildingInterface b) {
			if(map.containsKey(type)) {
				map.get(type).add(b);
			} else {
				List<BuildingInterface> list = new ArrayList<BuildingInterface>();
				list.add(b);
				map.put(type, list);
			}
			return b;
		}

		/**
		 * Returns a random building of type
		 */

		public static BuildingInterface findRandomBuilding(BUILDING type) {
			if(type == BUILDING.restaurant) {
				List<BuildingInterface> list = map.get(type);
				if(restaurantNumber > list.size()) {
					restaurantNumber = 0;
				}
				BuildingInterface buildingToReturn = list.get(restaurantNumber);
				if(++restaurantNumber > list.size()) {
					restaurantNumber = 0;
				}
				return buildingToReturn;
			}
			
			List<BuildingInterface> list = map.get(type);
			Collections.shuffle(list);
			return list.get(0);
		}

		/**
		 * Find the building of type closest to the destination building
		 */
		public static BuildingInterface findClosestBuilding(BUILDING type, BuildingInterface b) {
			int x = b.getCityViewBuilding().x;
			int y = b.getCityViewBuilding().y;
			double closestDistance = 1000000;
			BuildingInterface returnBuilding = null;
			for(BuildingInterface tempBuilding : map.get(type)) {
				double distance = Math.sqrt((double)(Math.pow(tempBuilding.getCityViewBuilding().x - x, 2) + Math.pow(tempBuilding.getCityViewBuilding().y - y, 2)));
				if( distance < closestDistance) {
					closestDistance = distance;
					returnBuilding = tempBuilding;
				}
			}
			return returnBuilding;
		}

		/**
		 * Find the building of type closest to the person's location
		 */
		public static BuildingInterface findClosestBuilding(BUILDING type, Person p) {
			int x = 100; // p.animation.getXPos(); // TODO RestaurantZhang 92f655cfd5
			int y = 100; // p.animation.getYPos(); // TODO RestaurantZhang 92f655cfd5
			double closestDistance = 1000000;
			BuildingInterface returnBuilding = null;
			for(BuildingInterface b : map.get(type)) {
				double distance = Math.sqrt((double)(Math.pow(b.getCityViewBuilding().x - x, 2) + Math.pow(b.getCityViewBuilding().y - y, 2)));
				if( distance < closestDistance) {
					closestDistance = distance;
					returnBuilding = b;
				}
			}
			return returnBuilding;
		}

		public static CityRoad findClosestRoad(BuildingInterface b) {
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
		

		public static void clearMap() {

			map.clear();
		}
	}

}
