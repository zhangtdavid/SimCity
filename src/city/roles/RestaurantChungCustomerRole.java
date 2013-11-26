package city.roles;

import java.util.*;
import java.util.concurrent.Semaphore;

import utilities.RestaurantChungMenu;
import city.Role;
import city.animations.RestaurantChungCustomerAnimation;
import city.interfaces.RestaurantChungCashier;
import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungHost;
import city.interfaces.RestaurantChungWaiter;

/**
 * Restaurant Customer agent.
 */
public class RestaurantChungCustomerRole extends Role implements RestaurantChungCustomer {
	private int hungerLevel = 10; // determines length of meal
	private RestaurantChungHost host;
	private RestaurantChungCashier cashier;
	private RestaurantChungWaiter waiter;
	int positionInLine;
	Timer timer = new Timer();
	private Semaphore atSeat = new Semaphore(0, true);
	private Semaphore atCashier = new Semaphore(0, true);
	int bill;
	RestaurantChungMenu menu;
	private String order;


	public enum AgentState
	{DoingNothing, GoingToRestaurant, WaitingInRestaurant, DecidedToStay, BeingSeated, DecideLeaving, Deciding, CallingWaiter, WaitingForFood, Eating, WaitingForCheck, GoingToCashier, Paying, WaitingForChange, Leaving};
	private AgentState state = AgentState.DoingNothing; //The start state
	
	public enum AgentEvent
	{none, gotHungry, getInLine, standInLine, followWaiter, noTables, decidedToLeave, seated, readyToOrder, askedForOrder, receivedFood, doneEating, receivedCheck, atCashier, receivedChange, doneLeaving, kickedOut};
	private AgentEvent event = AgentEvent.none;
	

//	Constructor
//	====================================================================
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 */
	public RestaurantChungCustomerRole(){
		super();
	}

//	public void setActive(){
//		this.setActivityBegun();
//	}
	
//  Messages
//	=====================================================================
	public void gotHungry() {//from animation
//		print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}

	public void msgGetInLinePosition(int pos) {
		print("Customer received msgGetInLinePosition " + pos); // TODO
		positionInLine = pos;
		event = AgentEvent.getInLine;
		stateChanged();
	}
	
	public void msgNoTablesAvailable() {
		print("Customer received msgNoTablesAvailable");
		event = AgentEvent.noTables;		
		stateChanged();
	}
	
	public void msgSelfDecidedToLeave() {
		event = AgentEvent.decidedToLeave;		
		stateChanged();
	}
	
	public void msgFollowMeToTable(RestaurantChungWaiter waiter, RestaurantChungMenu menu) {
		print("Customer received msgFollowMeToTable");
		this.waiter = waiter;
		this.menu = new RestaurantChungMenu(menu);
		event = AgentEvent.followWaiter;
		stateChanged();
	}
	
	public void msgAnimationAtSeat() {
		print("Customer received msgAnimationAtSeat");
		event = AgentEvent.seated;
		//from animation
		atSeat.release();// = true;
		stateChanged();
	}
	
	public void msgSelfReadyToOrder() {
		event = AgentEvent.readyToOrder;
		stateChanged();
	}
	
	public void msgWhatWouldYouLike() {
		print("Customer received msgWhatWouldYouLike");
		event = AgentEvent.askedForOrder;
		stateChanged();
	}
	
	public void msgOutOfItem(String choice, RestaurantChungMenu menu) {
		print("Customer received msgOutOfItem " + choice);
		this.menu = new RestaurantChungMenu(menu);
		event = AgentEvent.seated;
		stateChanged();
	}
	
	public void msgHereIsYourFood() {
		print("Customer received msgHereIsYourFood");
		event = AgentEvent.receivedFood;
		stateChanged();
	}
	
	public void msgSelfDoneEating() {
		event = AgentEvent.doneEating;
		stateChanged();
	}
	
	public void msgHereIsCheck(int price) {
		event = AgentEvent.receivedCheck;
		bill = price;
		stateChanged();
	}
	
	public void msgAnimationAtCashier() {
		print("Customer received msgAnimationAtCashier");
		event = AgentEvent.atCashier;
		//from animation
		atCashier.release();// = true;
		stateChanged();	
	}
	
	public void msgHereIsChange(int change) {
		this.getPerson().setCash(this.getPerson().getCash() + change);
		event = AgentEvent.receivedChange;
		stateChanged();		
	}
	
	public void msgAnimationFinishedLeaveRestaurant() {
		print("Customer received msgAnimationFinishedLeaveRestaurant");
		event = AgentEvent.doneLeaving;
		super.setInactive();
	}
	
	public void msgKickingYouOutAfterPaying(int debt) {
		print("Customer received msgKickingYouOutAfterPaying");
//		money += 20;
		bill = debt;
		state = AgentState.WaitingForCheck;
		event = AgentEvent.receivedCheck;
		stateChanged();		
	}

//  Scheduler
//	=====================================================================
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean runScheduler() {
		//	CustomerAgent is a finite state machine
		System.out.println("STATE: " + state);
		System.out.println("EVENT: " + event);

//		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry) {
//			goToRestaurant();
//			return true;
//		}
		
//		if (state == AgentState.GoingToRestaurant && event == AgentEvent.getInLine || state == AgentState.WaitingInRestaurant && event == AgentEvent.getInLine) {
//			getInLine();
//			return true;
//		}
		
		if (state == AgentState.DoingNothing && event == AgentEvent.getInLine || state == AgentState.WaitingInRestaurant && event == AgentEvent.getInLine) {
			getInLine();
			return true;
		}
		
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter) {
			sitDown();
			return true;
		}
	
		if (state == AgentState.Deciding && event == AgentEvent.readyToOrder) {
			callWaiter();
			return true;
		}
		
		if (state == AgentState.CallingWaiter && event == AgentEvent.askedForOrder) {
			giveOrder();
			return true;
		}
		
		if (state == AgentState.WaitingForFood && event == AgentEvent.receivedFood) {
			eatFood();
			return true;
		}
		
		if (state == AgentState.Eating && event == AgentEvent.doneEating) {
			askForCheck();
			return true;
		}
		
		if (state == AgentState.WaitingForCheck && event == AgentEvent.receivedCheck) {
			goToCashier();
			return true;
		}
		
		if (state == AgentState.GoingToCashier && event == AgentEvent.atCashier) {
			payForFood();
			return true;
		}
		
		if (state == AgentState.Paying && event == AgentEvent.receivedChange) {
			leaveRestaurant();
			return true;
		}
		
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving) {
			state = AgentState.DoingNothing;
			return true;
		}
		
//  Non-Normative Scenarios
//		---------------------------------------------------------------	
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.kickedOut) {
			leaveRestaurant();
			return true;
		}
		
		// No tables available
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.noTables) {
			decideLeaving();
			return true;
		}
		
		if (state == AgentState.DecidedToStay && event == AgentEvent.followWaiter) {
			sitDown();
			return true;
		}
		
		if (state == AgentState.DecideLeaving && event == AgentEvent.decidedToLeave) {
			leaveRestaurant();
			return true;
		}
		
		// Original order is unavailable, have to reorder
		if (state == AgentState.BeingSeated && event == AgentEvent.seated || state == AgentState.WaitingForFood && event == AgentEvent.seated) {
			decideFood();
			return true;
		}
		
		// Customer cannot afford anything
		if (state == AgentState.Deciding && event == AgentEvent.decidedToLeave) {
			leaveTable();
			return true;
		}
		
		return false;
	}

//  Actions
//	=====================================================================
	private void goToRestaurant() {
		print("Going to restaurant");
		state = AgentState.GoingToRestaurant;
		host.msgIWantToEat(this);//send our instance, so he can respond to us
	}

	
	private void getInLine() {
		print("Getting in position " + positionInLine + " of the line at restaurant");
		state = AgentState.WaitingInRestaurant;
		this.getAnimation(RestaurantChungCustomerAnimation.class).DoGoToWaitingArea(positionInLine);
		event = AgentEvent.standInLine;
	}
	
	private void sitDown() {
		print("Sitting down");
		state = AgentState.BeingSeated;
		try {
			atSeat.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void decideFood() {
		print("Deciding food");
		state = AgentState.Deciding;
		order = null; // erases any old orders
		// Randomly select food from the menu
		final int money = this.getPerson().getCash(); // Need to do this to get access to money in the timer task
		System.out.println(money);
		timer.schedule(new TimerTask() {
			public void run() {
				// HACK------------------------------------------------------------------
//				
//				double nameDouble = -1;
//				
//				try {
//					nameDouble = Double.parseDouble(name);
//				} catch (NumberFormatException nfe){
//				
//					
//				}
//				
//				if (nameDouble == 8.99) {
//					money = nameDouble; // TODO Remove hacks
//					
//					for(int i = 0; i < menu.items.size(); i++)  {
//						if (menu.items.get(i).item.equals("Salad")) {
//							order = "Salad";
//							print("Finished deciding food, customer wants " + order);
//							msgSelfReadyToOrder();
//							return;
//						}
//					}
//					order = "Pizza";
//					print("Finished deciding food, customer wants " + order);
//					msgSelfReadyToOrder();
//					return;	
//				}
//				
//				if (nameDouble == 5.99) {
//					money = nameDouble;
//				}
//				
//				if (nameDouble == 5) {
//					money = nameDouble;
//				}
//				
//				if (name.equals("Steak")) {
//					for(int i = 0; i < menu.items.size(); i++)  {
//						if (menu.items.get(i).item.equals("Steak"))
//							if (menu.items.get(i).price > money) {
//								msgSelfDecidedToLeave();
//								return;
//							}
//							else {
//								order = "Steak";
//								print("Finished deciding food, customer wants " + order);
//								msgSelfReadyToOrder();
//								return;
//							}
//					}
//					msgSelfDecidedToLeave();
//					return;
//				}
//				
//				else if (name.equals("Chicken")) {
//					for(int i = 0; i < menu.items.size(); i++)  {
//						if (menu.items.get(i).item.equals("Chicken"))
//							if (menu.items.get(i).price > money) {
//								msgSelfDecidedToLeave();
//								return;
//							}
//							else {
//								order = "Chicken";
//								print("Finished deciding food, customer wants " + order);
//								msgSelfReadyToOrder();
//								return;
//							}
//					}
//					msgSelfDecidedToLeave();
//					return;
//				}
//				
//				else if (name.equals("Salad")) {
//					for(int i = 0; i < menu.items.size(); i++)  {
//						if (menu.items.get(i).item.equals("Salad"))
//							if (menu.items.get(i).price > money) {
//								msgSelfDecidedToLeave();
//								return;
//							}
//							else {
//								order = "Salad";
//								print("Finished deciding food, customer wants " + order);
//								msgSelfReadyToOrder();
//								return;
//							}
//					}
//					msgSelfDecidedToLeave();
//					return;
//				}
//				
//				else if (name.equals("Pizza")) {
//					for(int i = 0; i < menu.items.size(); i++)  {
//						if (menu.items.get(i).item.equals("Pizza"))
//							if (menu.items.get(i).price > money) {
//								msgSelfDecidedToLeave();
//								return;
//							}
//							else {
//								order = "Pizza";
//								print("Finished deciding food, customer wants " + order);
//								msgSelfReadyToOrder();
//								return;
//							}
//					}
//					msgSelfDecidedToLeave();
//					return;
//				}
//				
//				else if (name.equals("Flake")) {
//					money = 5.00;
//					for(int i = 0; i < menu.items.size(); i++)  {
//						// Deliberately orders something too expensive
//						if (menu.items.get(i).price > money) {
//							order = menu.items.get(i).item;
//							print("Finished deciding food, customer wants " + order);
//							msgSelfReadyToOrder();
//							return;
//						}
//					}
//					msgSelfDecidedToLeave();
//					return;
//				}
				// END HACK------------------------------------------------------------------
				
//				else {
					Random rand = new Random();
					while (order == null) {
						if (menu.items.size() == 0) {
							msgSelfDecidedToLeave();
							return;
						}
						int randInt = rand.nextInt(menu.items.size());
						if (menu.items.get(randInt).price > money) {
							menu.items.remove(randInt);						
						}
						else {
							order = menu.items.get(randInt).item;
							print("Finished deciding food, customer wants " + order);
							msgSelfReadyToOrder();	
						}
					}
				}
//			}
		},
		1000);
	}
	
	private void callWaiter() {
		print("Call Waiter");
		state = AgentState.CallingWaiter;
		waiter.msgReadyToOrder(this);
	}
	
	private void giveOrder() {
		print("Ordering " + order);
		state = AgentState.WaitingForFood;
		waiter.msgHereIsMyOrder(this, order);
	}

	private void eatFood() {
		print("Eating Food");
		state = AgentState.Eating;
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			public void run() {
				print("Done eating " + order);
				msgSelfDoneEating();
				//isHungry = false;
			}
		},
		getHungerLevel() * 800);//how long to wait before running task
	}

	private void askForCheck() {
		print("Asking for check");
		state = AgentState.WaitingForCheck;
		waiter.msgGetCheck(this);
	}
	
	private void goToCashier() {
		print("Going to cashier");
		state = AgentState.GoingToCashier;
		this.getAnimation(RestaurantChungCustomerAnimation.class).DoGoToCashier();		
	}
	
	private void payForFood() {
		print("Paying for food");
		state = AgentState.Paying;
		
		if (bill > this.getPerson().getCash()) {
			cashier.msgHereIsPayment(this, this.getPerson().getCash());
			this.getPerson().setCash(0);
			return;
		}
		
		cashier.msgHereIsPayment(this, bill);		
		this.getPerson().setCash(this.getPerson().getCash() - bill);
	}
	
	private void leaveRestaurant() {
		print("Leaving");
		state = AgentState.Leaving;
		host.msgLeaving(this);
		this.getAnimation(RestaurantChungCustomerAnimation.class).DoExitRestaurant();
	}
	
//  Non-Normative Scenarios
//	---------------------------------------------------------------	
	private void decideLeaving() {
		state = AgentState.DecideLeaving;
		Random rand = new Random();
		int leaving = rand.nextInt(2);

// HACK------------------------------------------------------------------
//		if (name.equals("Leave")) leaving = 0;
//		else if (name.equals("Stay")) leaving = 1;
// END HACK------------------------------------------------------------------

		if (leaving == 0) {
			print("Decided to leave");
			msgSelfDecidedToLeave();
		}
		else if(leaving == 1) {
			print("Decided to stay");
			state = AgentState.DecidedToStay;
			host.msgDecidedToStay(this);
		}
	}
	
	private void leaveTable() {
		print("Leaving");
		state = AgentState.Leaving;
		waiter.msgLeaving(this);
		this.getAnimation(RestaurantChungCustomerAnimation.class).DoExitRestaurant();
	}
	
	
//  Utilities
//	=====================================================================
//	public void setHungerLevel(int hungerLevel) {
//		this.hungerLevel = hungerLevel;
//		//could be a state change. Maybe you don't
//		//need to eat until hunger lever is > 5?
//	}
	
//	public void setGui(RestaurantChungCustomerAnimation g) {
//		customerGui = g;
//	}
	
	/**
	 * hack to establish connection to Host agent.
	 */	
	public void setHost(RestaurantChungHost host) {
		this.host = host;
	}
	
	public void setCashier(RestaurantChungCashier cashier) {
		this.cashier = cashier;		
	}
	
	public int getHungerLevel() {
		return hungerLevel;
	}
	
//	public RestaurantChungCustomerAnimation getGui() {
//		return customerGui;
//	}
	
	public String getState() {
		return state.toString();
	}

	public String getOrder() {
		return order;
	}
}


