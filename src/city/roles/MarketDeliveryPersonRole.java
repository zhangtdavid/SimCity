package city.roles;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application;
import city.Application.FOOD_ITEMS;
import city.agents.CarAgent;
import city.agents.interfaces.Car;
import city.animations.CarAnimation;
import city.bases.JobRole;
import city.buildings.MarketBuilding;
import city.buildings.interfaces.Market;
import city.roles.interfaces.CarPassenger;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.MarketDeliveryPerson;

public class MarketDeliveryPersonRole extends JobRole implements MarketDeliveryPerson {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();
	Timer timer = new Timer();

	public enum DeliveryState {None, Pending, Delivering, Arrived, Received, ReturningToRestaurant};

	private DeliveryState s;
	
	private Market market;
	
	private Car car;
	private CarPassenger carPassenger;
	
	private MarketCustomerDelivery customerDelivery;
	private int orderId;
	private Map<FOOD_ITEMS, Integer> collectedItems = new HashMap<FOOD_ITEMS, Integer>();
	
	private WorkingState workingState;
	
	private boolean restaurantClosed;

//	Constructor
//	=====================================================================
	public MarketDeliveryPersonRole(Market b, int t1, int t2) {
		super();
		market = b;
		this.setShift(t1, t2);
		this.setWorkplace(market);
		this.setSalary(MarketBuilding.WORKER_SALARY);
		car = new CarAgent(market, this); // setting b to be the current location of the car
		CarAnimation carAnim = new CarAnimation(car, market);
		carAnim.setVisible(true);;
		car.setAnimation(carAnim);
		car.startThread();
		Application.getMainFrame().cityView.addAnimation(carAnim);

		workingState = WorkingState.Working;
		s = DeliveryState.None;
		
		restaurantClosed = false;
    }
	
//  Activity
//	=====================================================================	
	@Override
	public void setInactive(){
		workingState = WorkingState.GoingOffShift;
	}
	
//  Messages
//	=====================================================================	
//	Cashier
//	---------------------------------------------------------------
	@Override
	public void msgDeliverOrder(MarketCustomerDelivery c, Map<FOOD_ITEMS, Integer> i, int id) {
		log.add(new LoggedEvent("Market DeliveryPerson received msgDeliverOrder from Market Cashier."));
		print("Market DeliveryPerson received msgDeliverOrder from Market Cashier.");
		customerDelivery = c;
        for (FOOD_ITEMS s: i.keySet()) {
        	collectedItems.put(s, i.get(s)); // initialize all values in collectedItems to 0
        }
        orderId = id;
        s = DeliveryState.Pending;
        stateChanged();
	}
	
	@Override
	public void msgArrivedAtDestination() {
		if (s == DeliveryState.ReturningToRestaurant) {
			
		}
		
		else {
	        s = DeliveryState.Arrived;		
		}
		stateChanged();
	}
	
//  Scheduler
//	=====================================================================
	@Override
	public boolean runScheduler() {
		// Role Scheduler
		boolean blocking = false;
		if (carPassenger != null && carPassenger.getActive()) {
			blocking  = true;
			carPassenger.runScheduler();
		}	
		
		if (workingState == WorkingState.GoingOffShift && customerDelivery == null) {
			if (market.getDeliveryPeople().size() > 1) {
				market.removeDeliveryPerson(this);
				super.setInactive();				
			}
		}
		
		if (restaurantClosed) {
			timer.schedule(new TimerTask() {
				public void run() {
//					 check if restaurant is open
//					if(customerDelivery.getRestaurant().getBusinessIsOpen())
						restaurantClosed = false;
						s = DeliveryState.Pending;
//					else {
//						stateChanged();
//					}
				}
			},
			5000);
		}
//		
		if (s == DeliveryState.Pending) {
			deliverItems();
			return true;
		}
		
		if (s == DeliveryState.Arrived) {
			checkOpen();
			return true;
		}

		return blocking;
	}
	
//  Actions
//	=====================================================================	
	private void deliverItems() {
		market.getCashier().msgDeliveringItems(this);
		carPassenger = new CarPassengerRole(car, customerDelivery.getRestaurant(), this);
		carPassenger.setPerson(this.getPerson());
		carPassenger.setActive();
		this.getPerson().setCar(car); // overwrites the person's personal car.
		
		s = DeliveryState.Delivering;
	}
	
	public void checkOpen() {
//		if(customerDelivery.getRestaurant().getBusinessIsOpen()) {
			giveItems();
//		}
//		else {
//			restaurantClosed = true;
//			carPassenger = new CarPassengerRole(car, market);
			s = DeliveryState.ReturningToRestaurant;
//		}
	}
	
	public void giveItems() {
		customerDelivery.msgHereIsOrderDelivery(collectedItems, orderId);
		market.getCashier().msgFinishedDeliveringItems(this, orderId);
		customerDelivery = null;
	}
	
//  Getters
//	=====================================================================
	// Market
	@Override
	public Market getMarket() {
		return market;
	}
	
	@Override
	public MarketCustomerDelivery getCustomerDelivery() {
		return customerDelivery;
	}
	
	@Override
	public int getOrderId() {
		return orderId;
	}
	
	@Override
	public Map<FOOD_ITEMS, Integer> getCollectedItems() {
		return collectedItems;
	}
	
	@Override
	public WorkingState getWorkingState() {
		return workingState;
	}
	
//  Setters
//	=====================================================================	
	@Override
	public void setMarket(Market market) {
		this.market = market;
	}
	
//  Utilities
//	=====================================================================
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("MarketDeliveryPerson", msg);
        AlertLog.getInstance().logMessage(AlertTag.MARKET, "MarketDeliveryPersonRole " + this.getPerson().getName(), msg);
    }
}
