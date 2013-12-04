package city.tests.mock;

import utilities.EventLog;
import utilities.LoggedEvent;
import utilities.RestaurantChungMenu;
import city.abstracts.MockRole;
import city.interfaces.RestaurantChungCashier;
import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungWaiter;

public class MockRestaurantChungCustomer extends MockRole implements RestaurantChungCustomer {
	public EventLog log = new EventLog();
	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public RestaurantChungCashier cashier;

//	public MockRestaurantChungCustomer(String name) {
//		super(name);
//
//	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgNoTablesAvailable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSelfDecidedToLeave() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFollowMeToTable(RestaurantChungWaiter w, RestaurantChungMenu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAnimationAtSeat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSelfReadyToOrder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgWhatWouldYouLike() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfItem(String choice, RestaurantChungMenu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsYourFood() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSelfDoneEating() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsCheck(int price) {
//		log.add(new LoggedEvent("Received msgHereIsCheck from waiter. Check is for " + price));
//		System.out.println("Customer: Received msgHereIsCheck from waiter. Check is for " + price);
	}

	@Override
	public void msgAnimationAtCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsChange(int change) {
		log.add(new LoggedEvent("Customer received msgHereIsChange from cashier. Change is for " + change));
		System.out.println("Customer received msgHereIsChange from cashier. Change is for " + change);		
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgKickingYouOutAfterPaying(int debt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHungerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgGetInLinePosition(int positionInLine) {
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
