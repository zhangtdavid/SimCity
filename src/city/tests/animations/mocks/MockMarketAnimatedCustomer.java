package city.tests.animations.mocks;

import java.awt.Graphics2D;

import city.animations.interfaces.MarketAnimatedCustomer;
import city.roles.interfaces.MarketCustomer;
import city.roles.interfaces.MarketEmployee;
import city.tests.bases.mocks.MockAnimation;

public class MockMarketAnimatedCustomer extends MockAnimation implements MarketAnimatedCustomer {
	
	private MarketCustomer customer;
	
	public MockMarketAnimatedCustomer(MarketCustomer c) {
		customer = c;
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
	public void DoStandInWaitingForServiceLine() {
		// TODO Auto-generated method stub

	}

	@Override
	public void DoGoToCounter(MarketEmployee e) {
		customer.msgAnimationAtCounter();
	}

	@Override
	public void DoStandInWaitingForItemsLine() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void DoGoToCashier() {
		customer.msgAnimationAtCashier();	
	}

	@Override
	public void DoExitMarket() {
		customer.msgAnimationFinishedLeaveMarket();	
	}

	@Override
	public void setUgly(boolean newUgly) {
		// TODO Auto-generated method stub
		
	}




}
