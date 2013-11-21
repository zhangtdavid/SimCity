package city.tests.mock;

import java.util.List;

import city.MockRole;
import city.interfaces.RestaurantChoiCashier;
import city.interfaces.RestaurantChoiCook;
import city.interfaces.RestaurantChoiCustomer;
import city.interfaces.RestaurantChoiHost;
import city.interfaces.RestaurantChoiWaiter;
import city.roles.RestaurantChoiOrder;
import city.roles.RestaurantChoiTable;

public class MockRestaurantChoiWaiter extends MockRole implements RestaurantChoiWaiter  {
	String name;

	public MockRestaurantChoiWaiter(String name){
	this.name = name;	
	}
	@Override
	public void msgSeatCustomer(RestaurantChoiCustomer restaurantChoiCustomer,
			RestaurantChoiTable restaurantChoiTable) {
		// TODO Auto-generated method stub
	}

	@Override
	public void msgReadyToOrder(RestaurantChoiCustomer c) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void msgHeresMyOrder(RestaurantChoiCustomer c, int choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderComplete(RestaurantChoiOrder o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImDone(RestaurantChoiCustomer c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfThisFood(RestaurantChoiOrder o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCheckPlz(RestaurantChoiCustomer c, int choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHeresCheck(double total, RestaurantChoiCustomer ca) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRelease() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgBreakOK(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void offBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<myCustomer> getMyCustomers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RestaurantChoiTable> getTables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void requestBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean askedForBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOnBreak() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void seatCustomer(myCustomer customer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void GiveFood(myCustomer mc, RestaurantChoiOrder or) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoSeatCustomer(RestaurantChoiCustomer customer,
			RestaurantChoiTable table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToTable(RestaurantChoiTable table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToCashier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoToCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean needToSeatCustomer() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needToTakeOrder() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needToSendOrderToCook() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needToGetOrderFromCook() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needToNotifyHost() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needToDeliverFood() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needToRetakeOrder() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needToGetCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needToGiveCheck() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setHost(RestaurantChoiHost h) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCook(RestaurantChoiCook c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(RestaurantChoiCashier ca) {
		// TODO Auto-generated method stub
		
	}

}
