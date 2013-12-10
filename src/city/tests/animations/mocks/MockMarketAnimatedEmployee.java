package city.tests.animations.mocks;

import java.awt.Graphics2D;
import java.util.LinkedList;

import city.animations.interfaces.MarketAnimatedEmployee;
import city.roles.interfaces.MarketEmployee;
import city.tests.bases.mocks.MockAnimation;

public class MockMarketAnimatedEmployee extends MockAnimation implements MarketAnimatedEmployee {
	
	private MarketEmployee employee;
	private static LinkedList<MarketEmployee> employeeStalls = new LinkedList<MarketEmployee>(); 
	
	public MockMarketAnimatedEmployee(MarketEmployee e) {
		employee = e;
		employeeStalls.add(employee);
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
	public void doGoToPhone() {
		employee.msgAnimationAtPhone();		
	}

	@Override
	public void doCollectItems() {
		employee.msgFinishedCollectingItems();
	}

	@Override
	public void doDeliverItems() {
		employee.msgAnimationAtCashier();
	}

	@Override
	public void doGoToCounter() {
		employee.msgAnimationAtCounter();
	}

	@Override
	public LinkedList<MarketEmployee> getEmployeeStalls() {
		return employeeStalls;
	}

	@Override
	public int getCounterLoc() {
		return employeeStalls.indexOf(employee);
	}

	@Override
	public void removeFromEmployeeStalls() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUgly(boolean newUgly) {
		// TODO Auto-generated method stub
		
	}
}
