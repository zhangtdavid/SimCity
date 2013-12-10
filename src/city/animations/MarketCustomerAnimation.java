package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

import city.animations.interfaces.MarketAnimatedCustomer;
import city.animations.interfaces.MarketAnimatedEmployee;
import city.bases.Animation;
import city.gui.interiors.MarketPanel;
import city.roles.interfaces.MarketCustomer;
import city.roles.interfaces.MarketEmployee;

public class MarketCustomerAnimation extends Animation implements MarketAnimatedCustomer {
	private MarketCustomer customer = null;
	
	private static LinkedList<MarketCustomer> customersWaitingForService = new LinkedList<MarketCustomer>(); 
	private static LinkedList<MarketCustomer> customersWaitingForItems = new LinkedList<MarketCustomer>(); 

	private boolean waitingForService = false;
	private boolean waitingForItems = false;
	
	private boolean isPresent = false;

//	Location Information
//	=====================================================================
	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToEntrance, GoToWaitingLine, GoToCounter, GoToPickUpLine, GoToCashier, LeaveMarket};
	private Command command=Command.noCommand;

//	Constructor
//	=====================================================================
	public MarketCustomerAnimation(MarketCustomer c) {
		customer = c;
		xPos = 20;
		yPos = 540;
		xDestination = MarketPanel.ENTRANCEX;
		yDestination = MarketPanel.ENTRANCEY;
	}

//	Gui Updater
//	=====================================================================
	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (waitingForService) {
			xDestination = MarketPanel.SERVICEWAITINGX+(customersWaitingForService.indexOf(customer)*30);
			yDestination = MarketPanel.SERVICEWAITINGY;
		}
		
		else if (waitingForItems) {
			xDestination = MarketPanel.ITEMSWAITINGX-(customersWaitingForItems.indexOf(customer)*30)-20;
			yDestination = MarketPanel.ITEMSWAITINGY;
		}
		
		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.GoToCounter) customer.msgAnimationAtCounter();
			if (command == Command.GoToCashier) customer.msgAnimationAtCashier();
			if (command == Command.LeaveMarket) customer.msgAnimationFinishedLeaveMarket();
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		// Paints Customer Square
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, MarketPanel.RECTDIM, MarketPanel.RECTDIM);
	}

//	Utilities
//	=====================================================================
	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public boolean isPresent() {
		return isPresent;
	}

	@Override
	public void DoStandInWaitingForServiceLine() {
		customersWaitingForService.add(customer);
		waitingForService = true;
		xDestination = MarketPanel.SERVICEWAITINGX+(customersWaitingForService.indexOf(customer)*30);
		yDestination = MarketPanel.SERVICEWAITINGY;	
	}
	
	@Override
	public void DoGoToCounter(MarketEmployee employee) {
		customersWaitingForService.remove(customer);
		waitingForService = false;
		xDestination = MarketPanel.COUNTERX+(((employee.getAnimation(MarketAnimatedEmployee.class).getCounterLoc())+1)*45);
		yDestination = MarketPanel.COUNTERINTERACTIONY;
		command = Command.GoToCounter;
	}
	
	@Override
	public void DoStandInWaitingForItemsLine() {
		customersWaitingForItems.add(customer);
		waitingForItems = true;
		xDestination = MarketPanel.ITEMSWAITINGX-(customersWaitingForItems.indexOf(customer)*30)-20;;
		yDestination = MarketPanel.ITEMSWAITINGY;
	}
	
	@Override
	public void DoGoToCashier() {
		customersWaitingForItems.remove(customer);
		waitingForItems = false;
		xDestination = MarketPanel.CASHIERX;
		yDestination = MarketPanel.CASHIERCUSTINTERACTIONY;
		command = Command.GoToCashier;
	}
	
	@Override
	public void DoExitMarket() {
		xDestination = MarketPanel.EXITX;
		yDestination = MarketPanel.EXITY;
		command = Command.LeaveMarket;
	}
}