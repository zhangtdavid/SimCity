package city.roles;

import city.Role;
import city.animations.RestaurantChoiCashierAnimation;
import city.animations.interfaces.RestaurantChoiAnimatedCashier;
import city.interfaces.RestaurantChoiCashier;
import city.interfaces.RestaurantChoiCustomer;
import city.interfaces.RestaurantChoiWaiter;

public class RestaurantChoiCashierRole extends Role implements RestaurantChoiCashier{
	//Data    
	public double money = 100;
    public int moneyIncoming = 0; // 0 = no money in transit; 1 = money in transit
	//public EventLog log = new EventLog(); // TODO import junit3
	RestaurantChoiAnimatedCashier cashierGui;
    
    //Constructor
    public RestaurantChoiCashierRole(){
		super();
		foodCost.put(1, 15.99);
		foodCost.put(2, 10.99);
		foodCost.put(3, 5.99);
		foodCost.put(4, 8.99);
    }
    
    //Messages
    @Override
    /**
     * Computes check for waiter
     */
	public void msgComputeCheck(RestaurantChoiWaiter w,
			RestaurantChoiCustomer c, double money) {
		// TODO Auto-generated method stub
		
	}
    @Override
    public void msgMakePayment(RestaurantChoiCustomer c, double money) {
		// TODO Auto-generated method stub
		
	}
	@Override
	/**
	 * Computes check for waiter
	 */
	public void msgCompute(int choice, RestaurantChoiCustomer c,
			RestaurantChoiWaiter w) {
		checks.add(new Check(choice, c, w));
		stateChanged();
		
	}
	public void msgHeresMyPayment(RestaurantChoiCustomer c, double allHisCash){
		//find the customer's check as you get a payment
				synchronized(checks){
					for(int i = 0; i < checks.size(); i++){
						if(c.equals(checks.get(i).getca())){
							if(allHisCash < checks.get(i).getBill()){ // if not enough money, SEND TO DISHES
								checks.get(i).setPayment(allHisCash); // first take all his money
								checks.get(i).setState(Check.NOT_FULFILLED); // then mark as needing to work
								break;
							}else{ // else proceed as normal
								checks.get(i).setState(Check.GET_PAID);
								checks.get(i).setPayment(allHisCash);
								break;
							}
						}
					}
				}
				stateChanged();
	}
	/*
 	public void msgHeresYourMarketBill(Market m, int type, int amount){
		double owed = foodCost.get(type)*amount;
		synchronized(marketBills){
			marketBills.put(m, owed);
		}
		stateChanged();
	}
	*/
	@Override
	public void msgHeresYourMoney(double withdrawal) {
		money+=withdrawal;
		moneyIncoming = NOT_IN_TRANSIT;
		stateChanged();
	}

	@Override
	public void msgDoneWithDishes(RestaurantChoiCustomer c) {
		synchronized(checks){
			for(int i = 0; i < checks.size(); i++){
				if(checks.get(i).getca().equals(c)){
					checks.remove(checks.get(i)); // done with dishes, fulfilled, so remove from list
				}
			}	
		}
	}
	
    //Scheduler
	@Override
	public boolean runScheduler() {
		// TODO Auto-generated method stub
		return false;
	}
    //Actions

	@Override
	public void returnCheck(Check c) {
		c.getwa().msgHeresCheck(c.getBill(), c.getca());
		c.setState(Check.GIVEN_TO_WAITER);		
	}

	@Override
	public void sendToDishes(Check ch) {
		ch.getca().msgDoTheDishes((int)(ch.getBill() - ch.getPayment())*3000);
		ch.setState(Check.FULFILL_BY_DISHES); // the deed is done
		
	}

	@Override
	public void returnChange(Check ch) {
		System.out.println("Received customer payment of " + ch.getPayment());
		double change = ch.getPayment()-ch.getBill();
		money+=ch.getBill();
		ch.getca().msgHeresYourChange(change);
		checks.remove(ch);
		
	}

	/*
 @Override
	
	public void getMoney(Banker b) {
		b.msgMoneyPls(1000); // ask for 1000 dollars at a time
		moneyIncoming = IN_TRANSIT;
		
	}

	@Override
	public void payMarketBill(Market m, double payment) {
		m.msgHeresYourPayment(payment);
		money-=payment;
		synchronized(marketBills){
			marketBills.put(m, 0.0); // set bill to 0
		}
		
	}
*/

    //Getters
	@Override
	public RestaurantChoiAnimatedCashier getAnimation() {
		return this.cashierGui;
	}
    
    //Setters
 /*   public void setBanker(Banker b){
		restaurantBanker = b; // only one banker (we can trust...) TODO fix so that this matches with bank in simcity201
    }
    
    public void addMarket(Market m){
     	markets.add(m); // TODO fix so that this matches with market in simcity201
	 	marketBills.put(m,0.0);
	}
   */ 
    public void setGui(RestaurantChoiAnimatedCashier r){
    	this.cashierGui = r;
    }
    //Utilities
}
