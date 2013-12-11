package city.roles;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;

import trace.AlertLog;
import trace.AlertTag;
import utilities.EventLog;
import utilities.LoggedEvent;
import city.Application.FOOD_ITEMS;
import city.agents.interfaces.Car;
import city.bases.JobRole;
import city.buildings.MarketBuilding;
import city.buildings.interfaces.Market;
import city.buildings.interfaces.Market.DeliveryState;
import city.roles.interfaces.CarPassenger;
import city.roles.interfaces.MarketCustomerDelivery;
import city.roles.interfaces.MarketDeliveryPerson;

public class MarketDeliveryPersonRole extends JobRole implements MarketDeliveryPerson {

//  Data
//	=====================================================================	
	public EventLog log = new EventLog();
	Timer timer = new Timer();

	private Market market;

	private Queue<MyDelivery> deliveries;
	
	private Car personalCar;
	private Car car;
	private CarPassenger carPassenger;
	
	private WorkingState workingState;

//	Constructor
//	=====================================================================
	public MarketDeliveryPersonRole(Market b, int t1, int t2) {
		super();
		market = b;
		this.setShift(t1, t2);
		this.setWorkplace(market);
		this.setSalary(MarketBuilding.WORKER_SALARY);
		workingState = WorkingState.Working;
		
		deliveries = new LinkedList<MyDelivery>();
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
		deliveries.add(new MyDelivery(c, i, id));
        stateChanged();
	}
	
	@Override
	public void msgArrivedAtDestination() {
		if (deliveries.peek().s == DeliveryState.ReturningToMarket) {
			if (deliveries.peek().received = true)
				deliveries.remove();
			// If market was closed, move delivery to end of list
			else{
				MyDelivery tempDelivery = deliveries.poll(); // removes head item
				deliveries.add(tempDelivery); // move to end of the list
			}
		}
		else {
			deliveries.peek().s = DeliveryState.Arrived;
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
		
		if (workingState == WorkingState.GoingOffShift && deliveries.size() == 0) {
			if (market.getDeliveryPeople().size() > 1) {
				market.getDeliveryPeople().remove(this);
				if (personalCar != null)
					this.getPerson().setCar(personalCar); // switches back to personal car
				super.setInactive();				
			}
		}
		
		if (deliveries.size() > 0 && deliveries.peek().s == DeliveryState.Pending) {
			deliverItems();
			return true;
		}
		
		if (deliveries.size() > 0 && deliveries.peek().s == DeliveryState.Arrived) {
			checkOpen();
			return true;
		}

		return blocking;
	}
	
//  Actions
//	=====================================================================	
	private void deliverItems() {
		// Before first delivery, store personal car pointer
		if (personalCar == null)
			personalCar = this.getPerson().getCar();
		market.getCashier().msgDeliveringItems(this);
		carPassenger = new CarPassengerRole(car, deliveries.peek().customerDelivery.getRestaurant(), this);
		carPassenger.setPerson(this.getPerson());
		carPassenger.setActive();
		this.getPerson().setCar(car);
		
		deliveries.peek().s = DeliveryState.Delivering;
	}
	
	public void checkOpen() {
		if(deliveries.peek().customerDelivery.getRestaurant().getBusinessIsOpen()) {
			giveItems();
		}

		returnToMarket();
	}
	
	public void giveItems() {
		deliveries.peek().customerDelivery.msgHereIsOrderDelivery(deliveries.peek().collectedItems, deliveries.peek().orderId);
		market.getCashier().msgFinishedDeliveringItems(this, deliveries.peek().orderId);
		deliveries.peek().received = true;
	}
	
	public void returnToMarket() {
		deliveries.peek().s = DeliveryState.ReturningToMarket;
		carPassenger = new CarPassengerRole(car, market, this);
		carPassenger.setPerson(this.getPerson());
		carPassenger.setActive();
	}
	
//  Getters
//	=====================================================================
	// Market
	@Override
	public Market getMarket() {
		return market;
	}
	
	@Override
	public Queue<MyDelivery> getDeliveries() {
		return deliveries;
	}
	
	@Override
	public MarketCustomerDelivery getCustomerDelivery() {
		return deliveries.peek().customerDelivery;
	}
	
	@Override
	public int getOrderId() {
		return deliveries.peek().orderId;
	}
	
	@Override
	public DeliveryState getDeliveryState() {
		return deliveries.peek().s;
	}
	
	@Override
	public Map<FOOD_ITEMS, Integer> getCollectedItems() {
		return deliveries.peek().collectedItems;
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
	
	public void setDeliveryCar(Car car) {
		this.car = car;
	}
	
//  Utilities
//	=====================================================================
	@Override
	public void print(String msg) {
		this.getPerson().printViaRole("MarketDeliveryPerson", msg);
        AlertLog.getInstance().logMessage(AlertTag.MARKET, "MarketDeliveryPersonRole " + this.getPerson().getName(), msg);
    }
	
//  Classes
//	=====================================================================
	public class MyDelivery {
		MarketCustomerDelivery customerDelivery;
		Map<FOOD_ITEMS, Integer> collectedItems  = new HashMap<FOOD_ITEMS, Integer>();
		int orderId;
		DeliveryState s;
		boolean received;
		
		public MyDelivery(MarketCustomerDelivery c, Map<FOOD_ITEMS, Integer> i, int id) {
			customerDelivery = c;
	        for (FOOD_ITEMS s: i.keySet()) {
	        	collectedItems.put(s, i.get(s)); // initialize all values in collectedItems to 0
	        }
            orderId = id;
            s = DeliveryState.Pending;
            received = false;
		}		
	}
}
