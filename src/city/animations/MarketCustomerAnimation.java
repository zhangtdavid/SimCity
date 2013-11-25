package city.animations;

import java.awt.*;

import city.Animation;
import city.animations.interfaces.MarketAnimatedCustomer;
import city.interfaces.MarketCustomer;

public class MarketCustomerAnimation extends Animation implements MarketAnimatedCustomer {
	private MarketCustomer customer = null;

	private boolean isPresent = false;
	
//	Fixed Numbers
//	=====================================================================
	private static final int CRECTDIM = 20;
    private static final int xEntrance = 15, yEntrance = 20;
    private static final int xExit = 275, yExit = -40;
    private static final int xWaitingArea = 50, yWaitingArea = 100;
    private static final int xCounter = 15, yCounter = 320+25;
    private static final int xRegister= 465, yRegister = 320+25;

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
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
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
		g.fillRect(xPos, yPos, CRECTDIM, CRECTDIM);
	}

//	Utilities
//	=====================================================================
	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public boolean isPresent() {
		return isPresent;
	}

	public void DoStandInWaitingForServiceLine() {
		xDestination = xWaitingArea;
		yDestination = yWaitingArea;//+(waitingPosition*30);		
	}
	
	public void DoGoToCounter(int loc) {
		xDestination = xCounter+(50*loc);
		yDestination = yCounter;		
		command = Command.GoToCounter;
	}
	
	public void DoGoToWaitingArea() {
		xDestination = xWaitingArea;
		yDestination = yWaitingArea;//+(waitingPosition*30);
	}
	
	public void DoGoToCashier() {
		xDestination = xRegister;
		yDestination = yRegister;
		command = Command.GoToCashier;
	}
	
	public void DoExitMarket() {
		xDestination = xExit;
		yDestination = yExit;
		command = Command.LeaveMarket;
	}
}