package city;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import utilities.DataModel;
import utilities.TrafficControl;
import city.Application.BUILDING;
import city.agents.BusAgent;
import city.agents.CarAgent;
import city.agents.PersonAgent;
import city.agents.interfaces.Person;
import city.animations.BusAnimation;
import city.animations.CarAnimation;
import city.animations.PersonAnimation;
import city.animations.WalkerAnimation;
import city.bases.Building;
import city.bases.interfaces.AnimationInterface;
import city.bases.interfaces.BuildingInterface;
import city.buildings.AptBuilding;
import city.buildings.BankBuilding;
import city.buildings.BusStopBuilding;
import city.buildings.HouseBuilding;
import city.buildings.MarketBuilding;
import city.buildings.RestaurantChoiBuilding;
import city.buildings.RestaurantChungBuilding;
import city.buildings.RestaurantJPBuilding;
import city.buildings.RestaurantTimmsBuilding;
import city.buildings.RestaurantZhangBuilding;
import city.gui.BuildingCard;
import city.gui.CityRoad;
import city.gui.CityRoad.STOPLIGHTTYPE;
import city.gui.CityRoadIntersection;
import city.gui.CitySidewalkLayout;
import city.gui.MainFrame;
import city.gui.exteriors.CityViewApt;
import city.gui.exteriors.CityViewBank;
import city.gui.exteriors.CityViewBuilding;
import city.gui.exteriors.CityViewBusStop;
import city.gui.exteriors.CityViewHouse;
import city.gui.exteriors.CityViewMarket;
import city.gui.exteriors.CityViewRestaurantChoi;
import city.gui.exteriors.CityViewRestaurantChung;
import city.gui.exteriors.CityViewRestaurantJP;
import city.gui.exteriors.CityViewRestaurantTimms;
import city.gui.exteriors.CityViewRestaurantZhang;
import city.gui.interiors.AptPanel;
import city.gui.interiors.BankPanel;
import city.gui.interiors.BusStopPanel;
import city.gui.interiors.HousePanel;
import city.gui.interiors.MarketPanel;
import city.gui.interiors.RestaurantChoiPanel;
import city.gui.interiors.RestaurantChungPanel;
import city.gui.interiors.RestaurantJPPanel;
import city.gui.interiors.RestaurantTimmsPanel;
import city.gui.interiors.RestaurantZhangPanel;
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
import city.roles.RestaurantChungWaiterRevolvingStandRole;
import city.roles.RestaurantTimmsCashierRole;
import city.roles.RestaurantTimmsCookRole;
import city.roles.RestaurantTimmsHostRole;
import city.roles.RestaurantTimmsWaiterRole;
import city.roles.RestaurantZhangCashierRole;
import city.roles.RestaurantZhangCookRole;
import city.roles.RestaurantZhangHostRole;
import city.roles.RestaurantZhangWaiterRegularRole;
import city.roles.RestaurantZhangWaiterSharedDataRole;

public class Application {

        private static MainFrame mainFrame;
        private static Timer timer = new Timer();
        private static Date date = new Date(0);        

        public static final int HALF_HOUR = 1800000; // A half hour in milliseconds
        public static final int INTERVAL = 1000; // One interval is the simulation's equivalent of a half-hour
        public static final int PAYCHECK_INTERVAL = 0; // TODO set the global interval at which people are paid
        public static enum BANK_SERVICE {none, deposit, moneyWithdraw, atmDeposit};
        public static enum TRANSACTION_TYPE {personal, business};
        public static enum FOOD_ITEMS {steak, chicken, salad, pizza};
        public static enum BUILDING {bank, busStop, house, apartment, market, restaurant};

        static List<CityRoad> roads = new ArrayList<CityRoad>();
        public static TrafficControl trafficControl;

        public static CitySidewalkLayout sidewalks;

        private static final DataModel model = new DataModel();

        /**
         * Main routine to start the program.
         *
         * When the program is started, this is the first call. It opens the GUI window, loads
         * configuration files, and causes the program to run.
         */
        public static void main(String[] args) {
                // Open the animation GUI
                try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                        e.printStackTrace();
                }
                mainFrame = new MainFrame();

                // Load a scenario
                parseConfig();

                // Start the simulation
                final DateFormat df = new SimpleDateFormat("MMMM dd HHmm");
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                TimerTask tt = new TimerTask() {
                        public void run() {
                                date.setTime(date.getTime() + HALF_HOUR);
                                mainFrame.setDisplayDate(df.format(date));
                                for (Person p : model.getPeople()) {
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
            // North roads
            for(int i = 375; i >= 100; i -= 25) {
                    if(i == 225)
                            continue;
                    CityRoad tempRoad = new CityRoad(i, 75, 25, 25, -1, 0, true, Color.black);
                    roads.add(tempRoad);
            }
            // West roads
            for(int i = 75; i <= 350; i+=25) {
                    if(i == 225)
                            continue;
                    CityRoad tempRoad = new CityRoad(75, i, 25, 25, 0, 1, false, Color.black);
                    roads.add(tempRoad);
            }
            // South roads
            for(int i = 75; i <= 350; i+=25) {
                    if(i == 225)
                            continue;
                    CityRoad tempRoad = new CityRoad(i, 375, 25, 25, 1, 0, true, Color.black);
                    roads.add(tempRoad);
            }
            // East roads
            for(int i = 375; i >= 100; i-=25) {
                    if(i == 225)
                            continue;
                    CityRoad tempRoad = new CityRoad(375, i, 25, 25, 0, -1, false, Color.black);
                    roads.add(tempRoad);
            }
            // North/South middle roads
            for(int i = 350; i >= 100; i-=25) {
                    if(i == 225)
                            continue;
                    CityRoad tempRoad = new CityRoad(225, i, 25, 25, 0, -1, false, Color.black);
                    roads.add(tempRoad);
            }
            // East/West middle roads
            for(int i = 350; i >= 100; i -= 25) {
                    if(i == 225)
                            continue;
                    CityRoad tempRoad = new CityRoad(i, 225, 25, 25, -1, 0, true, Color.black);
                    roads.add(tempRoad);
            }
            // North intersection
            CityRoadIntersection intersectionNorth = new CityRoadIntersection(225, 75, 25, 25, Color.gray);
            roads.add(intersectionNorth);
            // West intersection
            CityRoadIntersection intersectionWest = new CityRoadIntersection(75, 225, 25, 25, Color.gray);
            roads.add(intersectionWest);
            // South intersection
            CityRoadIntersection intersectionSouth = new CityRoadIntersection(225, 375, 25, 25, Color.gray);
            roads.add(intersectionSouth);
            // East intersection
            CityRoadIntersection intersectionEast = new CityRoadIntersection(375, 225, 25, 25, Color.gray);
            roads.add(intersectionEast);
            // Center intersection
            CityRoadIntersection intersectionCenter = new CityRoadIntersection(225, 225, 25, 25, Color.gray);
            roads.add(intersectionCenter);
            // Connect all roads
            for(int i = 0; i < roads.size() - 1; i++) {
                    if(roads.get(i).getX() == intersectionNorth.getX() + 25 && roads.get(i).getY() == intersectionNorth.getY()) { // Set nextRoad of road to east of north intersection
                            roads.get(i).setNextRoad(intersectionNorth);
                            continue;
                    } else if(roads.get(i).getX() == intersectionNorth.getX() + 50 && roads.get(i).getY() == intersectionNorth.getY()) { // Set stoplight to east of north intersection
                            roads.get(i).setStopLightType(STOPLIGHTTYPE.HORIZONTALOFF);
                    } else if(roads.get(i).getY() == intersectionNorth.getY() + 25 && roads.get(i).getX() == intersectionNorth.getX()) { // Set nextRoad of road to south of north intersection
                            roads.get(i).setNextRoad(intersectionNorth);
                            continue;
                    } else if(roads.get(i).getY() == intersectionNorth.getY() + 50 && roads.get(i).getX() == intersectionNorth.getX()) { // Set stoplight of road to south of north intersection
                            roads.get(i).setStopLightType(STOPLIGHTTYPE.VERTICALOFF);
                    } else if(roads.get(i).getX() == intersectionNorth.getX() - 25 && roads.get(i).getY() == intersectionNorth.getY()) { // Set nextRoad of road to west of north intersection
                            intersectionNorth.setNextRoad(roads.get(i));
                            roads.get(i).setNextRoad(roads.get(i + 1));
                            continue;
                    } else if(roads.get(i).getY() == intersectionWest.getY() - 25 && roads.get(i).getX() == intersectionWest.getX()) { // Set nextRoad of road to north of west intersection
                            roads.get(i).setNextRoad(intersectionWest);
                            continue;
                    } else if(roads.get(i).getY() == intersectionWest.getY() - 50 && roads.get(i).getX() == intersectionWest.getX()) { // Set stoplight of road to north of west intersection
                            roads.get(i).setStopLightType(STOPLIGHTTYPE.VERTICALOFF);
                    } else if(roads.get(i).getY() == intersectionWest.getY() + 25 && roads.get(i).getX() == intersectionWest.getX()) { // Set nextRoad of road to south of west intersection
                            intersectionWest.setNextRoad(roads.get(i));
                            roads.get(i).setNextRoad(roads.get(i + 1));
                            continue;
                    } else if(roads.get(i).getX() == intersectionWest.getX() + 25 && roads.get(i).getY() == intersectionWest.getY()) { // Set nextRoad of road to east of west intersection
                            roads.get(i).setNextRoad(intersectionWest);
                            continue;
                    } else if(roads.get(i).getX() == intersectionWest.getX() + 50 && roads.get(i).getY() == intersectionWest.getY()) { // Set stoplight of road to east of west intersection
                            roads.get(i).setStopLightType(STOPLIGHTTYPE.HORIZONTALOFF);
                    } else if(roads.get(i).getY() == intersectionSouth.getY() - 25 && roads.get(i).getX() == intersectionSouth.getX()) { // Set nextRoad of road to north of south intersection
                            intersectionSouth.setNextRoad(roads.get(i));
                            roads.get(i).setNextRoad(roads.get(i + 1));
                            continue;
                    } else if(roads.get(i).getX() == intersectionSouth.getX() - 25 && roads.get(i).getY() == intersectionSouth.getY()) { // Set nextRoad of road to west of south intersection
                            roads.get(i).setNextRoad(intersectionSouth);
                            continue;
                    } else if(roads.get(i).getX() == intersectionSouth.getX() - 50 && roads.get(i).getY() == intersectionSouth.getY()) { // Set stoplight of road to west of south intersection
                            roads.get(i).setStopLightType(STOPLIGHTTYPE.HORIZONTALOFF);
                    } else if(roads.get(i).getX() == intersectionSouth.getX() + 25 && roads.get(i).getY() == intersectionSouth.getY()) { // Set nextRoad of road to east of south intersection
                            intersectionSouth.setNextRoad(roads.get(i));
                            roads.get(i).setNextRoad(roads.get(i + 1));
                            continue;
                    } else if(roads.get(i).getY() == intersectionEast.getY() - 25 && roads.get(i).getX() == intersectionEast.getX()) { // Set nextRoad of road to north of east intersection
                            intersectionEast.setNextRoad(roads.get(i));
                            roads.get(i).setNextRoad(roads.get(i + 1));
                            continue;
                    } else if(roads.get(i).getX() == intersectionEast.getX() - 25 && roads.get(i).getY() == intersectionEast.getY()) { // Set nextRoad of road to west of east intersection
                            intersectionEast.setNextRoad(roads.get(i));
                            roads.get(i).setNextRoad(roads.get(i + 1));
                            continue;
                    } else if(roads.get(i).getY() == intersectionEast.getY() + 25 && roads.get(i).getX() == intersectionEast.getX()) { // Set nextRoad of road to south of east intersection
                            roads.get(i).setNextRoad(intersectionEast);
                            continue;
                    } else if(roads.get(i).getY() == intersectionEast.getY() + 50 && roads.get(i).getX() == intersectionEast.getX()) { // Set stoplight of road to south of east intersection
                            roads.get(i).setStopLightType(STOPLIGHTTYPE.VERTICALOFF);
                    } else if(roads.get(i).getY() == intersectionCenter.getY() - 25 && roads.get(i).getX() == intersectionCenter.getX()) { // Set nextRoad of road to north of center intersection
                            intersectionCenter.setNextRoad(roads.get(i));
                            roads.get(i).setNextRoad(roads.get(i + 1));
                            continue;
                    } else if(roads.get(i).getX() == intersectionCenter.getX() + 25 && roads.get(i).getY() == intersectionCenter.getY()) { // Set nextRoad of road to east of center intersection
                            roads.get(i).setNextRoad(intersectionCenter);
                            continue;
                    } else if(roads.get(i).getX() == intersectionCenter.getX() + 50 && roads.get(i).getY() == intersectionCenter.getY()) { // Set stoplight of road to east of center intersection
                            roads.get(i).setStopLightType(STOPLIGHTTYPE.HORIZONTALOFF);
                    } else if(roads.get(i).getX() == intersectionCenter.getX() - 25 && roads.get(i).getY() == intersectionCenter.getY()) { // Set nextRoad of road to west of center intersection
                            intersectionCenter.setNextRoad(roads.get(i));
                            roads.get(i).setNextRoad(roads.get(i + 1));
                            continue;
                    } else if(roads.get(i).getY() == intersectionCenter.getY() + 25 && roads.get(i).getX() == intersectionCenter.getX()) { // Set nextRoad of road to south of center intersection
                            roads.get(i).setNextRoad(intersectionCenter);
                            continue;
                    } else if(roads.get(i).getY() == intersectionCenter.getY() + 50 && roads.get(i).getX() == intersectionCenter.getX()) { // Set stoplight of road to south of center intersection
                            roads.get(i).setStopLightType(STOPLIGHTTYPE.VERTICALOFF);
                    } else if(roads.get(i).getX() == 375 && roads.get(i).getY() == 100) { // Last road in the outer loop
                            roads.get(i).setNextRoad(roads.get(0));
                            continue;
                    }
                    // Straight road
                    if(roads.get(i).getClass() != CityRoadIntersection.class)
                            roads.get(i).setNextRoad(roads.get(i+1));
            }
            trafficControl = new TrafficControl(roads);

      // Create Sidewalks
            ArrayList<Rectangle> nonSidewalkArea = new ArrayList<Rectangle>();
            nonSidewalkArea.add(new Rectangle(2, 2, 14, 2)); // Top left
            nonSidewalkArea.add(new Rectangle(18, 2, 10, 2)); // Top right
            nonSidewalkArea.add(new Rectangle(2, 4, 2, 8)); // Topmid left
            nonSidewalkArea.add(new Rectangle(14, 6, 2, 10)); // Topmid center
            nonSidewalkArea.add(new Rectangle(26, 4, 2, 12)); // Topmid right
            nonSidewalkArea.add(new Rectangle(6, 14, 10, 2)); // Center left
            nonSidewalkArea.add(new Rectangle(18, 14, 10, 2)); // Center right
            nonSidewalkArea.add(new Rectangle(2, 14, 2, 12)); // Bottommid left
            nonSidewalkArea.add(new Rectangle(14, 18, 2, 8)); // Bottommid center
            nonSidewalkArea.add(new Rectangle(26, 18, 2, 8)); // Bottommid right
            nonSidewalkArea.add(new Rectangle(2, 26, 10, 2)); // Bottom left
            nonSidewalkArea.add(new Rectangle(14, 26, 14, 2)); // Bottom right
            nonSidewalkArea.add(new Rectangle(6, 6, 6, 6)); // Top left square
            nonSidewalkArea.add(new Rectangle(18, 6, 6, 6)); // Top right square
            nonSidewalkArea.add(new Rectangle(6, 18, 6, 6)); // Bottom left square
            nonSidewalkArea.add(new Rectangle(18, 18, 6, 6)); // Bottom right square
            sidewalks = new CitySidewalkLayout(mainFrame, 30, 30, 50, 50, 12.5, Color.orange, nonSidewalkArea, trafficControl);

      //Create Bus Stops
            BusStopPanel bsp1 = new BusStopPanel(Color.white);
            CityViewBusStop cityViewBusStop1 = new CityViewBusStop(325, 125, "Bus Stop 1", Color.white, bsp1);
            BusStopBuilding busStop1 = new BusStopBuilding("Bus Stop 1", bsp1, cityViewBusStop1);
            setBuilding(bsp1, cityViewBusStop1, busStop1);

            BusStopPanel bsp2 = new BusStopPanel(Color.white);
            CityViewBusStop cityViewBusStop2 = new CityViewBusStop(125, 125, "Bus Stop 2", Color.white, bsp2);
            BusStopBuilding busStop2 = new BusStopBuilding("Bus Stop 2", bsp2, cityViewBusStop2);
            setBuilding(bsp2, cityViewBusStop2, busStop2);

            BusStopPanel bsp3 = new BusStopPanel(Color.white);
            CityViewBusStop cityViewBusStop3 = new CityViewBusStop(325, 325, "Bus Stop 3", Color.white, bsp3);
            BusStopBuilding busStop3 = new BusStopBuilding("Bus Stop 3", bsp3, cityViewBusStop3);
            setBuilding(bsp3, cityViewBusStop3, busStop3);

            BusStopPanel bsp4 = new BusStopPanel(Color.white);
            CityViewBusStop cityViewBusStop4 = new CityViewBusStop(125, 325, "Bus Stop 4", Color.white, bsp4);
            BusStopBuilding busStop4 = new BusStopBuilding("Bus Stop 4", bsp4, cityViewBusStop4);
            setBuilding(bsp4, cityViewBusStop4, busStop4);
            
            busStop1.setNextStop(busStop2);
            busStop1.setPreviousStop(busStop4);
            busStop2.setNextStop(busStop3);
            busStop2.setPreviousStop(busStop1);
            busStop3.setNextStop(busStop4);
            busStop3.setPreviousStop(busStop2);
            busStop4.setNextStop(busStop1);
            busStop4.setPreviousStop(busStop3);
            
            BusAgent bus1 = new BusAgent(busStop1, busStop2);
            BusAnimation b1Anim = new BusAnimation(bus1, busStop1);
            bus1.setAnimation(b1Anim);
            mainFrame.cityView.addAnimation(b1Anim);
            CityMap.findClosestRoad(busStop1).setVehicle(b1Anim);
            bus1.startThread();
            
     // Create buildings
            AptBuilding Apartment1 = (AptBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.APT, 25, 50);
            AptBuilding Apartment2 = (AptBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.APT, 25, 100);
            AptBuilding Apartment3 = (AptBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.APT, 25, 150);
            AptBuilding Apartment4 = (AptBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.APT, 25, 200);
            AptBuilding Apartment5 = (AptBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.APT, 25, 250);
            AptBuilding Apartment6 = (AptBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.APT, 25, 300);
            AptBuilding Apartment7 = (AptBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.APT, 25, 350);
            AptBuilding Apartment8 = (AptBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.APT, 25, 400);
            AptBuilding Apartment9 = (AptBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.APT, 50, 425);
            AptBuilding Apartment10 = (AptBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.APT, 100, 425);
            BankBuilding bank1 = (BankBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.BANK, 425, 50);
            MarketBuilding market1 = (MarketBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.MARKET, 425, 100);
            RestaurantTimmsBuilding restaurantTimms = (RestaurantTimmsBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.RESTAURANTTIMMS, 425, 150);
            RestaurantZhangBuilding restaurantZhang = (RestaurantZhangBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.RESTAURANTZHANG, 425, 200);
            RestaurantChoiBuilding restaurantChoiBuilding1 = (RestaurantChoiBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.RESTAURANTCHOI, 425, 250);
            RestaurantChungBuilding restaurantChungBuilding1 = (RestaurantChungBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.RESTAURANTCHUNG, 425, 300);
            RestaurantJPBuilding restaurantJPBuilding1 = (RestaurantJPBuilding)createBuilding(CityViewBuilding.BUILDINGTYPE.RESTAURANTJP, 425, 350);
            
     // Create People
            PersonAgent p1 = new PersonAgent("Timms Host", date, new PersonAnimation(), Apartment1);
            PersonAgent p2 = new PersonAgent("Timms Cashier", date, new PersonAnimation(), Apartment1);
            PersonAgent p3 = new PersonAgent("Timms Cook", date, new PersonAnimation(), Apartment1);
            PersonAgent p4 = new PersonAgent("Timms Waiter1", date, new PersonAnimation(), Apartment1);
            PersonAgent p5 = new PersonAgent("Timms Waiter2", date, new PersonAnimation(), Apartment1);
            PersonAgent p6 = new PersonAgent("Chung Host", date, new PersonAnimation(), Apartment2);
            PersonAgent p7 = new PersonAgent("Chung Cashier", date, new PersonAnimation(), Apartment2);
            PersonAgent p8 = new PersonAgent("Chung Cook", date, new PersonAnimation(), Apartment2);
            PersonAgent p9 = new PersonAgent("Chung Waiter1", date, new PersonAnimation(), Apartment2);
            PersonAgent p10 = new PersonAgent("Chung Waiter2", date, new PersonAnimation(), Apartment2);
            PersonAgent p11 = new PersonAgent("Zhang Host", date, new PersonAnimation(), Apartment3);
            PersonAgent p12 = new PersonAgent("Zhang Cashier", date, new PersonAnimation(), Apartment3);
            PersonAgent p13 = new PersonAgent("Zhang Cook", date, new PersonAnimation(), Apartment3);
            PersonAgent p14 = new PersonAgent("Zhang Waiter1", date, new PersonAnimation(), Apartment3);
            PersonAgent p15 = new PersonAgent("Zhang Waiter2", date, new PersonAnimation(), Apartment3);
            PersonAgent p16 = new PersonAgent("Choi Host", date, new PersonAnimation(), Apartment4);
            PersonAgent p17 = new PersonAgent("Choi Cashier", date, new PersonAnimation(), Apartment4);
            PersonAgent p18 = new PersonAgent("Choi Cook", date, new PersonAnimation(), Apartment4);
            PersonAgent p19 = new PersonAgent("Choi Waiter1", date, new PersonAnimation(), Apartment4);
            PersonAgent p20 = new PersonAgent("Choi Waiter2", date, new PersonAnimation(), Apartment4);
            PersonAgent p21 = new PersonAgent("JP Host", date, new PersonAnimation(), Apartment5);
            PersonAgent p22 = new PersonAgent("JP Cashier", date, new PersonAnimation(), Apartment5);
            PersonAgent p23 = new PersonAgent("JP Cook", date, new PersonAnimation(), Apartment5);
            PersonAgent p24 = new PersonAgent("JP Waiter1", date, new PersonAnimation(), Apartment5);
            PersonAgent p25 = new PersonAgent("JP Waiter2", date, new PersonAnimation(), Apartment5);
            PersonAgent p26 = new PersonAgent("Bank Manager", date, new PersonAnimation(), Apartment6);
            PersonAgent p27 = new PersonAgent("Bank Teller1", date, new PersonAnimation(), Apartment6);
            PersonAgent p28 = new PersonAgent("Bank Teller2", date, new PersonAnimation(), Apartment6);
            PersonAgent p29 = new PersonAgent("Bank Teller3", date, new PersonAnimation(), Apartment6);
            PersonAgent p30 = new PersonAgent("Bank Robber", date, new PersonAnimation(), Apartment6);
            PersonAgent p31 = new PersonAgent("Market Manager", date, new PersonAnimation(), Apartment7);
            PersonAgent p32 = new PersonAgent("Market Cashier", date, new PersonAnimation(), Apartment7);
            PersonAgent p33 = new PersonAgent("Market Delivery Man", date, new PersonAnimation(), Apartment7);
            PersonAgent p34 = new PersonAgent("Market Employee1", date, new PersonAnimation(), Apartment7);
            PersonAgent p35 = new PersonAgent("Market Employee2", date, new PersonAnimation(), Apartment7);
            PersonAgent p36 = new PersonAgent("Consumer1", date, new PersonAnimation(), Apartment8);
            PersonAgent p37 = new PersonAgent("Consumer2", date, new PersonAnimation(), Apartment8);
            PersonAgent p38 = new PersonAgent("Consumer3", date, new PersonAnimation(), Apartment8);
            PersonAgent p39 = new PersonAgent("Consumer4", date, new PersonAnimation(), Apartment8);
            PersonAgent p40 = new PersonAgent("Consumer5", date, new PersonAnimation(), Apartment8);
            PersonAgent p41 = new PersonAgent("Consumer6", date, new PersonAnimation(), Apartment9);
            PersonAgent p42 = new PersonAgent("Consumer7", date, new PersonAnimation(), Apartment9);
            PersonAgent p43 = new PersonAgent("Consumer8", date, new PersonAnimation(), Apartment9);
            PersonAgent p44 = new PersonAgent("Consumer9", date, new PersonAnimation(), Apartment9);
            PersonAgent p45 = new PersonAgent("Consumer10", date, new PersonAnimation(), Apartment9);
            PersonAgent p46 = new PersonAgent("Consumer11", date, new PersonAnimation(), Apartment10);
            PersonAgent p47 = new PersonAgent("Consumer12", date, new PersonAnimation(), Apartment10);
            PersonAgent p48 = new PersonAgent("Consumer13", date, new PersonAnimation(), Apartment10);
            PersonAgent p49 = new PersonAgent("Consumer14", date, new PersonAnimation(), Apartment10);
            PersonAgent p50 = new PersonAgent("Consumer15", date, new PersonAnimation(), Apartment10);
            
   //Create Landlords
            LandlordRole landlord1 = new LandlordRole();
            p1.addRole(landlord1);
            Apartment1.setLandlord(landlord1);
            
            LandlordRole landlord2 = new LandlordRole();
            p6.addRole(landlord2);
            Apartment2.setLandlord(landlord1);
            
            LandlordRole landlord3 = new LandlordRole();
            p11.addRole(landlord3);
            Apartment3.setLandlord(landlord3);
            
            LandlordRole landlord4 = new LandlordRole();
            p16.addRole(landlord4);
            Apartment4.setLandlord(landlord4);
            
            LandlordRole landlord5 = new LandlordRole();
            p21.addRole(landlord5);
            Apartment5.setLandlord(landlord5);
            
            LandlordRole landlord6 = new LandlordRole();
            p26.addRole(landlord6);
            Apartment6.setLandlord(landlord6);
            
            LandlordRole landlord7 = new LandlordRole();
            p31.addRole(landlord7);
            Apartment7.setLandlord(landlord7);
            
            LandlordRole landlord8 = new LandlordRole();
            p36.addRole(landlord8);
            Apartment8.setLandlord(landlord8);
            
            LandlordRole landlord9 = new LandlordRole();
            p41.addRole(landlord9);
            Apartment9.setLandlord(landlord9);
            
            LandlordRole landlord10 = new LandlordRole();
            p46.addRole(landlord10);
            Apartment10.setLandlord(landlord10);
     // Create occupations
            //RestaurantTimms
            RestaurantTimmsHostRole timmsHost = new RestaurantTimmsHostRole(restaurantTimms, 0, 24);
            restaurantTimms.addOccupyingRole(timmsHost);
            p1.setOccupation(timmsHost);
            
            RestaurantTimmsCashierRole timmsCashier = new RestaurantTimmsCashierRole(restaurantTimms, 0, 24);
            restaurantTimms.addOccupyingRole(timmsCashier);
            p2.setOccupation(timmsCashier);
            
            RestaurantTimmsCookRole timmsCook = new RestaurantTimmsCookRole(restaurantTimms, 0, 24);
            restaurantTimms.addOccupyingRole(timmsCook);
            p3.setOccupation(timmsCook);
            
            RestaurantTimmsWaiterRole timmsWaiter1 = new RestaurantTimmsWaiterRole(restaurantTimms, 0, 24);
            restaurantTimms.addOccupyingRole(timmsWaiter1);
            p4.setOccupation(timmsWaiter1);
            
            RestaurantTimmsWaiterRole timmsWaiter2 = new RestaurantTimmsWaiterRole(restaurantTimms, 0, 24);
            restaurantTimms.addOccupyingRole(timmsWaiter2);
            p5.setOccupation(timmsWaiter2);
            
            //Chung Restaurant
           /* RestaurantHostRole Host = new RestaurantHostRole(restaurant, 0, 24);
            restaurant.addOccupyingRole();
            p.setOccupation();
            
            RestaurantCashierRole Cashier = new RestaurantCashierRole(restaurant, 0, 24);
            restaurant.addOccupyingRole();
            p.setOccupation();
            
            RestaurantCookRole Cook = new RestaurantCookRole(restaurant, 0, 24);
            restaurant.addOccupyingRole();
            p8.setOccupation();
            
            RestaurantZhangWaiterRegularRole zhangWaiter1 = new RestaurantZhangWaiterRegularRole(restaurantZhang, 0, 24);
            restaurantZhang.addOccupyingRole(zhangWaiter1);
            p9.setOccupation(zhangWaiter1);
            
            RestaurantZhangWaiterSharedDataRole zhangWaiter2 = new RestaurantZhangWaiterSharedDataRole(restaurantZhang, 0, 24);
            restaurantZhang.addOccupyingRole(zhangWaiter2);
            p10.setOccupation(zhangWaiter2);
            
            //RestaurantZhang
            RestaurantZhangHostRole zhangHost = new RestaurantZhangHostRole(restaurantZhang, 0, 24);
            restaurantZhang.addOccupyingRole(zhangHost);
            p6.setOccupation(zhangHost);
            
            RestaurantZhangCashierRole zhangCashier = new RestaurantZhangCashierRole(restaurantZhang, 0, 24);
            restaurantZhang.addOccupyingRole(zhangCashier);
            p7.setOccupation(zhangCashier);
            
            RestaurantZhangCookRole zhangCook = new RestaurantZhangCookRole(restaurantZhang, 0, 24);
            restaurantZhang.addOccupyingRole(zhangCook);
            p8.setOccupation(zhangCook);
            
            RestaurantZhangWaiterRegularRole zhangWaiter1 = new RestaurantZhangWaiterRegularRole(restaurantZhang, 0, 24);
            restaurantZhang.addOccupyingRole(zhangWaiter1);
            p9.setOccupation(zhangWaiter1);
            
            RestaurantZhangWaiterSharedDataRole zhangWaiter2 = new RestaurantZhangWaiterSharedDataRole(restaurantZhang, 0, 24);
            restaurantZhang.addOccupyingRole(zhangWaiter2);
            p10.setOccupation(zhangWaiter2);
            
            
            // Create cook
            RestaurantChoiCookRole p2r1Choi = new RestaurantChoiCookRole(restaurantChoiBuilding1, 0, 24);
            restaurantChoiBuilding1.addOccupyingRole(p2r1Choi);
            p2Choi.setOccupation(p2r1Choi);
            p2r1Choi.addMarket(market1);

            // Create host
            RestaurantChoiHostRole p3r1Choi = new RestaurantChoiHostRole(restaurantChoiBuilding1, 0, 24);
            restaurantChoiBuilding1.addOccupyingRole(p3r1Choi);
            p3Choi.setOccupation(p3r1Choi);

            // Create waiter
            RestaurantChoiWaiterQueueRole p4r1Choi = new RestaurantChoiWaiterQueueRole(restaurantChoiBuilding1, 0, 24);
            restaurantChoiBuilding1.addOccupyingRole(p4r1Choi);
            p4Choi.setOccupation(p4r1Choi);

            //Create bank roles

            //Create Market people
            
            MarketManagerRole p5r1Choi = new MarketManagerRole(market1, 0, 24);
            MarketCashierRole p6r1Choi = new MarketCashierRole(market1, 0, 24);
            MarketEmployeeRole p7r1Choi = new MarketEmployeeRole(market1, 0, 24);
            p5Choi.setOccupation(p5r1Choi);
            p5r1Choi.setPerson(p5Choi);
            p6Choi.setOccupation(p6r1Choi); //TODO enabling this breaks car animation code. why? 
            p6r1Choi.setPerson(p6Choi);
            p7Choi.setOccupation(p7r1Choi);
            p7r1Choi.setPerson(p7Choi);
            market1.addOccupyingRole(p5r1Choi);
            market1.addOccupyingRole(p6r1Choi);
            market1.addOccupyingRole(p7r1Choi);
            market1.setManager(p5r1Choi);
            market1.setCashier(p6r1Choi);
            market1.addEmployee(p7r1Choi);
            

            /** 
             * END RYAN'S PART 
             */
            // RESTAURANTCHUNG------------------------------------------------------------------------------
            /*        
            // Create landlord
            PersonAgent p0Chung = new PersonAgent("Landlord Chung", date, new PersonAnimation(), Apartment4);
            p0Chung.setCash(50); // TODO remove later
            LandlordRole p0r1Chung = new LandlordRole();
            p0Chung.addRole(p0r1Chung);
            Apartment4.setLandlord(p0r1Chung);
            p0r1Chung.setActive();
            model.addPerson(p0Chung);

                    // Create people
            PersonAgent p1Chung = new PersonAgent("Cashier 1 Chung", date, new PersonAnimation(), Apartment4);
            PersonAgent p2Chung = new PersonAgent("Cook 1 Chung", date, new PersonAnimation(), Apartment4);
            PersonAgent p3Chung = new PersonAgent("Host 1 Chung", date, new PersonAnimation(), Apartment4);
            PersonAgent p4Chung = new PersonAgent("Waiter 1 Chung", date, new PersonAnimation(), Apartment4);
            model.addPerson(p1Chung);
            model.addPerson(p2Chung);
            model.addPerson(p3Chung);
            model.addPerson(p4Chung);

            // Give people cars

            // Create cashier
            RestaurantChungCashierRole p1r1Chung = new RestaurantChungCashierRole(restaurantChungBuilding1, 0, 12);
            p1r1Chung.setPerson(p1Chung);
            p1r1Chung.setMarketCustomerDeliveryPaymentPerson();
            p1r1Chung.setBankCustomerPerson();
            restaurantChungBuilding1.addOccupyingRole(p1r1Chung);
            p1Chung.setOccupation(p1r1Chung);

            // Create cook
            RestaurantChungCookRole p2r1Chung = new RestaurantChungCookRole(restaurantChungBuilding1, 0, 12);
            p2r1Chung.setPerson(p2Chung);                
            restaurantChungBuilding1.addOccupyingRole(p2r1Chung);
            p2Chung.setOccupation(p2r1Chung);

            // Create host
            RestaurantChungHostRole p3r1Chung = new RestaurantChungHostRole(restaurantChungBuilding1, 0, 12);
            p3r1Chung.setPerson(p3Chung);                
            restaurantChungBuilding1.addOccupyingRole(p3r1Chung);
            p3Chung.setOccupation(p3r1Chung);

            RestaurantChungWaiterRevolvingStandRole p4r1Chung = new RestaurantChungWaiterRevolvingStandRole(restaurantChungBuilding1, 0, 12);
            p4r1Chung.setPerson(p4Chung);                
            restaurantChungBuilding1.addOccupyingRole(p4r1Chung);
            p4Chung.setOccupation(p4r1Chung);
                    
            p1Chung.startThread();
            p2Chung.startThread();
            p3Chung.startThread();
            p4Chung.startThread();
                
           // Create landlord
           PersonAgent p0Bank = new PersonAgent("Landlord Bank", date, new PersonAnimation(), Apartment5);
           p0Bank.setCash(50); // TODO remove later
           LandlordRole p0r1Bank = new LandlordRole();
           p0Bank.addRole(p0r1Bank);
           Apartment5.setLandlord(p0r1Bank);
           p0r1Bank.setActive();
           model.addPerson(p0Bank);

           // Create people
           PersonAgent p1Bank = new PersonAgent("BankManager 1", date, new PersonAnimation(), Apartment5);
           PersonAgent p2Bank = new PersonAgent("BankTeller 1", date, new PersonAnimation(), Apartment5);
           model.addPerson(p1Bank);
           model.addPerson(p2Bank);

                    // Give people cars
                    CarAgent c0Bank = new CarAgent(busStop1, p0Bank);
                    CarAnimation c0AnimBank = new CarAnimation(c0Bank, busStop1);
                    c0Bank.setAnimation(c0AnimBank);
                    mainFrame.cityView.addAnimation(c0AnimBank);
                    CarAgent c1Bank = new CarAgent(busStop1, p1Bank);
                    CarAnimation c1AnimBank = new CarAnimation(c1Bank, busStop1);
                    c1Bank.setAnimation(c1AnimBank);
                    mainFrame.cityView.addAnimation(c1AnimBank);
                    CarAgent c2Bank = new CarAgent(busStop1, p2Bank);
                    CarAnimation c2AnimBank = new CarAnimation(c2Bank, busStop1);
                    c2Bank.setAnimation(c2AnimBank);
                    mainFrame.cityView.addAnimation(c2AnimBank);

                    // Create cashier
                    BankManagerRole p1r1Bank = new BankManagerRole(bank1, 0, 12);
                    p1r1Bank.setPerson(p1Bank);
                    bank1.addOccupyingRole(p1r1Bank);
                    p1Bank.setOccupation(p1r1Bank);

                    // Create cook
                    BankTellerRole p2r1Bank = new BankTellerRole(bank1, 0, 12);
                    p2r1Bank.setPerson(p2Bank);                
                    bank1.addOccupyingRole(p2r1Bank);
                    p2Bank.setOccupation(p2r1Bank);
                    
                    // MARKET------------------------------------------------------------------------------

                    // Create landlord
                    PersonAgent p0Market = new PersonAgent("Landlord Market", date, new PersonAnimation(), Apartment7);
                    p0Market.setCash(50); // TODO remove later
                    LandlordRole p0r1Market = new LandlordRole();
                    p0Market.addRole(p0r1Market);
                    Apartment7.setLandlord(p0r1Market);
                    p0r1Market.setActive();
                    model.addPerson(p0Market);

                    // Create people
                    PersonAgent p1Market = new PersonAgent("MarketCashier 1", date, new PersonAnimation(), Apartment7);
                    PersonAgent p2Market = new PersonAgent("MarketDeliveryPerson 1", date, new PersonAnimation(), Apartment7);
                    PersonAgent p3Market = new PersonAgent("MarketEmployee 1", date, new PersonAnimation(), Apartment7);
                    PersonAgent p4Market = new PersonAgent("MarketManager 1", date, new PersonAnimation(), Apartment7);
                    model.addPerson(p1Market);
                    model.addPerson(p2Market);
                    model.addPerson(p3Market);
                    model.addPerson(p4Market);

                    // Create cashier
                    MarketCashierRole p1r1Market = new MarketCashierRole(market1, 0, 12);
                    p1r1Market.setPerson(p1Market);
                    market1.addOccupyingRole(p1r1Market);
                    p1Market.setOccupation(p1r1Market);
                    
                    // Create delivery person
                    MarketDeliveryPersonRole p2r1Market = new MarketDeliveryPersonRole(market1, 0, 12);
                    p2r1Market.setPerson(p2Market);                
                    market1.addOccupyingRole(p2r1Market);
                    p2Market.setOccupation(p2r1Market);

                    // delivery Person's car
                    CarAgent carDelivery = new CarAgent(market1, p2r1Market); // setting b to be the current location of the car
                    CarAnimation carAnim = new CarAnimation(carDelivery, market1);
//                    carAnim.setVisible(true);
                    carDelivery.setAnimation(carAnim);
                    mainFrame.cityView.addAnimation(carAnim);
                    p2r1Market.setDeliveryCar(carDelivery);
                    carDelivery.startThread();
                    
                    // Create employee
                    MarketEmployeeRole p3r1Market = new MarketEmployeeRole(market1, 0, 12);
                    p3r1Market.setPerson(p3Market);                
                    market1.addOccupyingRole(p3r1Market);
                    p3Market.setOccupation(p3r1Market);

                    // Create manager
                    MarketManagerRole p4r1Market = new MarketManagerRole(market1, 0, 12);
                    p4r1Market.setPerson(p4Market);                
                    market1.addOccupyingRole(p4r1Market);
                    p4Market.setOccupation(p4r1Market);
                    
                    p1Market.startThread();
                    p2Market.startThread();
                    p3Market.startThread();
                    p4Market.startThread();
                    
                    CarAgent c0Chung = new CarAgent(busStop1, p0Chung);
                    CarAnimation c0AnimChung = new CarAnimation(c0Chung, busStop1);
                    c0Chung.setAnimation(c0AnimChung);
                    mainFrame.cityView.addAnimation(c0AnimChung);
                    CarAgent c1Chung = new CarAgent(busStop1, p1Chung);
                    CarAnimation c1AnimChung = new CarAnimation(c1Chung, busStop1);
                    c1Chung.setAnimation(c1AnimChung);
                    mainFrame.cityView.addAnimation(c1AnimChung);
                    CarAgent c2Chung = new CarAgent(busStop1, p2Chung);
                    CarAnimation c2AnimChung = new CarAnimation(c2Chung, busStop1);
                    c2Chung.setAnimation(c2AnimChung);
                    mainFrame.cityView.addAnimation(c2AnimChung);
                    CarAgent c3Chung = new CarAgent(busStop1, p3Chung);
                    CarAnimation c3AnimChung = new CarAnimation(c3Chung, busStop1);
                    c3Chung.setAnimation(c3AnimChung);
                    mainFrame.cityView.addAnimation(c3AnimChung);
                    CarAgent c4Chung = new CarAgent(busStop1, p4Chung);
                    CarAnimation c4AnimChung = new CarAnimation(c4Chung, busStop1);
                    c4Chung.setAnimation(c4AnimChung);
                    mainFrame.cityView.addAnimation(c4AnimChung);
                    
                    CarAgent c0Choi = new CarAgent(Apartment3, p0Choi);
                    CarAnimation c0AnimChoi = new CarAnimation(c0Choi, Apartment3);
                    c0Choi.setAnimation(c0AnimChoi);
                    mainFrame.cityView.addAnimation(c0AnimChoi);
                                   
                    CarAgent c1Choi = new CarAgent(busStop4, p1Choi);
                    CarAnimation c1AnimChoi = new CarAnimation(c1Choi, busStop4);
                    c1Choi.setAnimation(c1AnimChoi);
                    mainFrame.cityView.addAnimation(c1AnimChoi);
                    CarAgent c2Choi = new CarAgent(busStop4, p2Choi);
                    CarAnimation c2AnimChoi = new CarAnimation(c2Choi, busStop4);
                    c2Choi.setAnimation(c2AnimChoi);
                    mainFrame.cityView.addAnimation(c2AnimChoi);
                    CarAgent c3Choi = new CarAgent(busStop4, p3Choi);
                    CarAnimation c3AnimChoi = new CarAnimation(c3Choi, busStop4);
                    c3Choi.setAnimation(c3AnimChoi);
                    mainFrame.cityView.addAnimation(c3AnimChoi);
                    CarAgent c4Choi = new CarAgent(busStop4, p4Choi);
                    CarAnimation c4AnimChoi = new CarAnimation(c4Choi, busStop4);
                    c4Choi.setAnimation(c4AnimChoi);
                    mainFrame.cityView.addAnimation(c4AnimChoi);
                    CarAgent c5Choi = new CarAgent(busStop4, p5Choi);
                    CarAnimation c5AnimChoi = new CarAnimation(c5Choi, busStop4);
                    c5Choi.setAnimation(c5AnimChoi);
                    mainFrame.cityView.addAnimation(c5AnimChoi);
                    CarAgent c6Choi = new CarAgent(busStop4, p6Choi);
                    CarAnimation c6AnimChoi = new CarAnimation(c6Choi, busStop4);
                    c6Choi.setAnimation(c6AnimChoi);
                    mainFrame.cityView.addAnimation(c6AnimChoi);
                    CarAgent c7Choi = new CarAgent(busStop4, p7Choi);
                    CarAnimation c7AnimChoi = new CarAnimation(c7Choi, busStop4);
                    c7Choi.setAnimation(c7AnimChoi);
                    mainFrame.cityView.addAnimation(c7AnimChoi);
                    CarAgent c8Choi = new CarAgent(busStop4, p8Choi);
                    CarAnimation c8AnimChoi = new CarAnimation(c8Choi, busStop4);
                    c8Choi.setAnimation(c8AnimChoi);
                    mainFrame.cityView.addAnimation(c8AnimChoi);
                    CarAgent c9Choi = new CarAgent(busStop3, p9Choi);
                    CarAnimation c9AnimChoi = new CarAnimation(c9Choi, busStop3);
                    c9Choi.setAnimation(c9AnimChoi);
                    mainFrame.cityView.addAnimation(c9AnimChoi);
                    CarAgent c10Choi = new CarAgent(busStop3, p10Choi);
                    CarAnimation c10AnimChoi = new CarAnimation(c10Choi, busStop3);
                    c10Choi.setAnimation(c10AnimChoi);
                    mainFrame.cityView.addAnimation(c10AnimChoi);
                    
                    CarAgent c0Market = new CarAgent(busStop1, p0Market);
                    CarAnimation c0AnimMarket = new CarAnimation(c0Market, busStop1);
                    c0Market.setAnimation(c0AnimMarket);
                    mainFrame.cityView.addAnimation(c0AnimMarket);
                    CarAgent c1Market = new CarAgent(busStop1, p1Market);
                    CarAnimation c1AnimMarket = new CarAnimation(c1Market, busStop1);
                    c1Market.setAnimation(c1AnimMarket);
                    mainFrame.cityView.addAnimation(c1AnimMarket);
                    CarAgent c2Market = new CarAgent(busStop1, p2Market);
                    CarAnimation c2AnimMarket = new CarAnimation(c2Market, busStop1);
                    c2Market.setAnimation(c2AnimMarket);
                    mainFrame.cityView.addAnimation(c2AnimMarket);
                    CarAgent c3Market = new CarAgent(busStop1, p3Market);
                    CarAnimation c3AnimMarket = new CarAnimation(c3Market, busStop1);
                    c3Market.setAnimation(c3AnimMarket);
                    mainFrame.cityView.addAnimation(c3AnimMarket);
                    CarAgent c4Market = new CarAgent(busStop1, p4Market);
                    CarAnimation c4AnimMarket = new CarAnimation(c4Market, busStop1);
                    c4Market.setAnimation(c4AnimMarket);
                    mainFrame.cityView.addAnimation(c4AnimMarket);
                    
                    p0Choi.setCar(c0Choi);
                    p1Choi.setCar(c1Choi);
                    p2Choi.setCar(c2Choi);
                    p3Choi.setCar(c3Choi);
                    p4Choi.setCar(c4Choi);
                    p5Choi.setCar(c5Choi);
                    p6Choi.setCar(c6Choi);
                    p7Choi.setCar(c7Choi);
                    p8Choi.setCar(c8Choi);
                    p9Choi.setCar(c9Choi);
                    p10Choi.setCar(c10Choi);


            try {
                    Thread.sleep(1000);
            } catch (InterruptedException e) {}

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
                          try {
                        	  Thread.sleep(4000);
                              } catch (InterruptedException e) {
                            	  e.printStackTrace();
                              }
                          c0Choi.startThread();
                          p0Choi.startThread();
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
    
            for(int j = 0; j < 0; j++) {
                    WalkerAnimation testPersonAnimation = new WalkerAnimation(null, CityMap.findRandomBuilding(BUILDING.busStop), sidewalks);
                    testPersonAnimation.setVisible(true);
                    mainFrame.cityView.addAnimation(testPersonAnimation);
                    testPersonAnimation.goToDestination(CityMap.findRandomBuilding(BUILDING.busStop));
            }
*/
        }

        public static DataModel getModel() {
                return model;
        }

        public static MainFrame getMainFrame() {
                return mainFrame;
        }

        public static Date getDate() {
                return new Date(date.getTime());
        }

        public static void setBuilding(BuildingCard panel, CityViewBuilding cityView, Building building) {
                mainFrame.cityView.addStatic(cityView);
                mainFrame.buildingView.addView(panel, cityView.getID());
                if(building.getClass().getName().contains("Restaurant")) {
                        CityMap.addBuilding(BUILDING.restaurant, building);
                } else if(building.getClass().getName().contains("Bank")) {
                        CityMap.addBuilding(BUILDING.bank, building);
                } else if(building.getClass().getName().contains("Market")) {
                        CityMap.addBuilding(BUILDING.market, building);
                } else if(building.getClass().getName().contains("BusStop")) {
                        CityMap.addBuilding(BUILDING.busStop, building);
                } else if(building.getClass().getName().contains("House")) {
                        CityMap.addBuilding(BUILDING.house, building);
                } else if(building.getClass().getName().contains("Apt")) {
                        CityMap.addBuilding(BUILDING.apartment, building);
                }
        }

        public static Point findNextOpenLocation() {
                for(int i = 25; i < 450; i += 25) {
                        for(int j = 25; j < 450; j += 25) {
                                if(i == 25 && (j == 25 || j == 425))
                                        i+= 25;
                                if(i == 425 && (j == 25 || j == 425))
                                        break;
                                if(mainFrame.cityView.getBuildingAt(i, j) != null)
                                        continue;
                                else
                                        return new Point(i, j);
                        }
                }
                return null;
        }

        /*
         * Creates a building at the given x and y coordinates. Can and will overlap buildings
         */
        public static void runGhostTown(){

        }
        public static void runVehicleCollision(){

        }
        public static void runPersonCollision(){

        }
        public static void runWeekend(){

        }
        public static void runShiftChange(){

        }

        public static void addAnim(AnimationInterface anim) {
                mainFrame.cityView.addAnimation(anim);
        }

        public static Point findNextOpenLocation(int x, int y) {

                for(int i = x; i < 450; i += 25) {
                        for(int j = y; j < 450; j += 25) {
                                if(i <= 25 && (j <= 25 || j >= 425))
                                        i+= 25;
                                if(i >= 425 && (j <= 25 || j >= 425))
                                        break;
                                if(mainFrame.cityView.getBuildingAt(i, j) != null)
                                        continue;
                                else
                                        return new Point(i, j);
                        }
                }
                return null;
        }

        /*
         * Creates a building at the next available location
         */
        public static BuildingInterface createBuilding(CityViewBuilding.BUILDINGTYPE type) {
                Point nextLocation = findNextOpenLocation(0, 0);
                if(nextLocation == null)
                        return null;
                return createBuilding(type, (int)(nextLocation.getX()), (int)(nextLocation.getY()));
        }

        /*
         * Creates a building at the given x and y coordinates. Can and will overlap buildings
         */
        public static BuildingInterface createBuilding(CityViewBuilding.BUILDINGTYPE type, int potentialX, int potentialY) {
                Point nextLocation = findNextOpenLocation(potentialX, potentialY);
                if(nextLocation == null)
                        return null;
                int x = nextLocation.x;
                int y = nextLocation.y;
                CityViewBuilding cityViewBuilding;
                Building building;
                switch (type) {
                case APT:
                        cityViewBuilding = new CityViewApt(x, y, "Apartment " + (mainFrame.cityView.statics.size()), Color.darkGray, new AptPanel(Color.darkGray));
                        building = new AptBuilding("Apartment " + mainFrame.cityView.statics.size(), null, (AptPanel)cityViewBuilding.getBuilding(), cityViewBuilding);
                        setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
                        return building;
                case HOUSE:
                        cityViewBuilding = new CityViewHouse(x, y, "House " + (mainFrame.cityView.statics.size()), Color.pink, new HousePanel(Color.pink));
                        building = new HouseBuilding("House " + mainFrame.cityView.statics.size(), null, (HousePanel)cityViewBuilding.getBuilding(), cityViewBuilding);
                        setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
                        return building;
                case RESTAURANTZHANG:
                        cityViewBuilding = new CityViewRestaurantZhang(x, y, "RestaurantZhang " + (mainFrame.cityView.statics.size()), Color.magenta, new RestaurantZhangPanel(Color.magenta));
                        building = new RestaurantZhangBuilding("RestaurantZhang " + mainFrame.cityView.statics.size(), (RestaurantZhangPanel)cityViewBuilding.getBuilding(), cityViewBuilding);
                        setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
                        return building;
                case RESTAURANTCHOI:
                        cityViewBuilding = new CityViewRestaurantChoi(x, y, "RestaurantChoi " + mainFrame.cityView.statics.size(), Color.cyan, new RestaurantChoiPanel(Color.cyan));
                        building = new RestaurantChoiBuilding("RestaurantChoi " + mainFrame.cityView.statics.size(),
                                        (RestaurantChoiPanel)(cityViewBuilding.getBuilding()), cityViewBuilding);
                        setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
                        return building;
                case RESTAURANTJP:
                        cityViewBuilding = new CityViewRestaurantJP(x, y, "RestaurantJP " + mainFrame.cityView.statics.size(), Color.orange, new RestaurantJPPanel(Color.orange));
                        building = new RestaurantJPBuilding("RestaurantJP " + mainFrame.cityView.statics.size(),
                                        (RestaurantJPPanel)(cityViewBuilding.getBuilding()), cityViewBuilding);
                        setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
                        return building;
                case RESTAURANTTIMMS:
                        cityViewBuilding = new CityViewRestaurantTimms(x, y, "RestaurantTimms " + mainFrame.cityView.statics.size(), Color.yellow, new RestaurantTimmsPanel(Color.yellow));
                        building = new RestaurantTimmsBuilding("RestaurantTimms " + mainFrame.cityView.statics.size(),
                                        (RestaurantTimmsPanel)(cityViewBuilding.getBuilding()), cityViewBuilding);
                        setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
                        return building;
                case RESTAURANTCHUNG:
                        cityViewBuilding = new CityViewRestaurantChung(x, y, "RestaurantChung " + mainFrame.cityView.statics.size(), Color.red, new RestaurantChungPanel(Color.red));
                        building = new RestaurantChungBuilding("RestaurantChung " + mainFrame.cityView.statics.size(),
                                        (RestaurantChungPanel)(cityViewBuilding.getBuilding()), cityViewBuilding);
                        setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
                        return building;
                case BANK:
                        cityViewBuilding = new CityViewBank(x, y, "Bank " + mainFrame.cityView.statics.size(), Color.green, new BankPanel(Color.green));
                        building = new BankBuilding("Bank " + mainFrame.cityView.statics.size(),
                                        (BankPanel)(cityViewBuilding.getBuilding()), cityViewBuilding);
                        setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
                        return building;
                case MARKET:
                        cityViewBuilding = new CityViewMarket(x, y, "Market " + mainFrame.cityView.statics.size(), Color.LIGHT_GRAY, new MarketPanel(Color.LIGHT_GRAY));
                        building = new MarketBuilding("Bank " + mainFrame.cityView.statics.size(),
                                        (MarketPanel)(cityViewBuilding.getBuilding()), cityViewBuilding);
                        setBuilding(cityViewBuilding.getBuilding(), cityViewBuilding, building);
                        return building;
                default:
                        return null;
                }
        }

        public static class CityMap {
                private static HashMap<BUILDING, List<BuildingInterface>> map = new HashMap<BUILDING, List<BuildingInterface>>();

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
                        model.addBuilding(b);
                        return b;
                }

                /**
                 * Returns a random building of type
                 */
                public static BuildingInterface findRandomBuilding(BUILDING type) {
                        List<BuildingInterface> list = map.get(type);
                        if(list == null)
                                return null;
                        Collections.shuffle(list);
                        return list.get(0);
                }

                /**
                 * Find the building of type closest to the destination building
                 */
                public static BuildingInterface findClosestBuilding(BUILDING type, BuildingInterface b) {
                        int x = b.getCityViewBuilding().getX();
                        int y = b.getCityViewBuilding().getY();
                        double closestDistance = 1000000;
                        BuildingInterface returnBuilding = null;
                        for(BuildingInterface tempBuilding : map.get(type)) {
                                double distance = Math.sqrt((double)(Math.pow(tempBuilding.getCityViewBuilding().getX() - x, 2) + Math.pow(tempBuilding.getCityViewBuilding().getY() - y, 2)));
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
                        int x = p.getCurrentLocation().getCityViewBuilding().getX();
                        int y = p.getCurrentLocation().getCityViewBuilding().getY();
                        double closestDistance = 1000000;
                        BuildingInterface returnBuilding = null;
                        for(BuildingInterface b : map.get(type)) {
                                double distance = Math.sqrt((double)(Math.pow(b.getCityViewBuilding().getX() - x, 2) + Math.pow(b.getCityViewBuilding().getY() - y, 2)));
                                if( distance < closestDistance) {
                                        closestDistance = distance;
                                        returnBuilding = b;
                                }
                        }
                        return returnBuilding;
                }

                /**
                 *
                 * @param b
                 * @return
                 */
                public static CityRoad findClosestRoad(BuildingInterface b) {
                        int x = b.getCityViewBuilding().getX();
                        int y = b.getCityViewBuilding().getY();
                        double closestDistance = 1000000;
                        CityRoad returnRoad = null;
                        for(CityRoad r : roads) {
                                double distance = Math.sqrt((double)(Math.pow(r.getX() - x, 2) + Math.pow(r.getY() - y, 2)));
                                if( distance < closestDistance) {
                                        closestDistance = distance;
                                        returnRoad = r;
                                }
                        }
                        return returnRoad;
                }

                /**
                 * Clears the map of all buildings.
                 * Convenience function for tests.
                 */
                public static void clearMap() {
                        map.clear();
                }

                /**
                 * Returns a list of all buildings in the CityMap
                 */
                public static ArrayList<BuildingInterface> getBuildings() {
                        ArrayList<BuildingInterface> list = new ArrayList<BuildingInterface>();
                        for (List<BuildingInterface> l : map.values()) {
                                list.addAll(l);
                        }
                        return list;
                }
        }


}