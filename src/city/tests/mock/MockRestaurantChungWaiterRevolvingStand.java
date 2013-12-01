package city.tests.mock;

import utilities.EventLog;
import utilities.LoggedEvent;
import city.abstracts.MockRole;
import city.interfaces.RestaurantChungCashier;
import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungWaiter;

public class MockRestaurantChungWaiterRevolvingStand extends MockRole implements RestaurantChungWaiter {
	public EventLog log = new EventLog();

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public RestaurantChungCashier cashier;

//	public MockRestaurantChungWaiterRevolvingStand(String name) {
//		super(name);
//
//	}

	@Override
	public void msgAnimationAskedForBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgApprovedForBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRejectedForBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationBreakOver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(RestaurantChungCustomer c, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder(RestaurantChungCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyOrder(RestaurantChungCustomer c, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfItem(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGetCheck(RestaurantChungCustomer c) {
//		log.add(new LoggedEvent("Waiter received msgGetCheck from customer."));
//		System.out.println("Waiter received msgGetCheck from customer.");	
	}

	@Override
	public void msgLeaving(RestaurantChungCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsBill(RestaurantChungCustomer c, int price) {
		log.add(new LoggedEvent("Waiter received msgHereIsBill from cashier. The bill is for " + price));
		System.out.println("Waiter received msgHereIsBill from cashier. The bill is for " + price);			
	}

	@Override
	public void msgAnimationAtEntrance() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationAtCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationAtCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationAtWaiterHome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationAtLine() {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public void HereIsYourTotal(double total) {
//		log.add(new LoggedEvent("Received HereIsYourTotal from cashier. Total = "+ total));
//
//		if(this.name.toLowerCase().contains("thief")){
//			//test the non-normative scenario where the customer has no money if their name contains the string "theif"
//			cashier.IAmShort(this, 0);
//
//		}else if (this.name.toLowerCase().contains("rich")){
//			//test the non-normative scenario where the customer overpays if their name contains the string "rich"
//			cashier.HereIsMyPayment(this, Math.ceil(total));
//
//		}else{
//			//test the normative scenario
//			cashier.HereIsMyPayment(this, total);
//		}
//	}
//
//	@Override
//	public void HereIsYourChange(double total) {
//		log.add(new LoggedEvent("Received HereIsYourChange from cashier. Change = "+ total));
//	}
//
//	@Override
//	public void YouOweUs(double remaining_cost) {
//		log.add(new LoggedEvent("Received YouOweUs from cashier. Debt = "+ remaining_cost));
//	}

}
