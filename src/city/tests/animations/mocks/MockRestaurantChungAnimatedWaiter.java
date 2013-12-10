package city.tests.animations.mocks;

import java.awt.Graphics2D;
import java.util.LinkedList;

import city.animations.interfaces.RestaurantChungAnimatedWaiter;
import city.roles.interfaces.RestaurantChungCustomer;
import city.roles.interfaces.RestaurantChungWaiter;
import city.tests.bases.mocks.MockAnimation;

public class MockRestaurantChungAnimatedWaiter extends MockAnimation implements RestaurantChungAnimatedWaiter {
	
	private RestaurantChungWaiter agent;
	
	public MockRestaurantChungAnimatedWaiter(RestaurantChungWaiter w) {
		agent = w;
	}

	@Override
	public void updatePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getXPos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getYPos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LinkedList<RestaurantChungWaiter> getWaiterPositions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addTable(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int findTableX(int table) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int findTableY(int table) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAskedForBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOffBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String isOnBreak() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeFromWaiterHomePositions() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoReturnToWaiterHome() {
		agent.msgAnimationAtWaiterHome();
	}

	@Override
	public void DoGoToCustomerLine() {
		agent.msgAnimationAtLine();		
	}

	@Override
	public void DoGoToTable(int table) {
		agent.msgAnimationAtTable();		
	}

	@Override
	public void DoBringToTable(RestaurantChungCustomer customer, int table) {
		agent.msgAnimationAtTable();				
	}

	@Override
	public void DoDeliverFood(int table, String choice) {
		agent.msgAnimationAtTable();						
	}

	@Override
	public void DoGoToCook() {
		agent.msgAnimationAtCook();		
	}

	@Override
	public void DoReturnToEntrance() {
		agent.msgAnimationAtEntrance();		
	}

	@Override
	public void DoGoToCashier() {
		agent.msgAnimationAtCashier();		
	}

	@Override
	public void DoGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoGoOffBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUgly(boolean newUgly) {
		// TODO Auto-generated method stub
		
	}
}
