package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

import city.animations.interfaces.MarketAnimatedEmployee;
import city.bases.Animation;
import city.gui.interiors.MarketPanel;
import city.roles.interfaces.MarketEmployee;

public class MarketEmployeeAnimation extends Animation implements MarketAnimatedEmployee {
	private MarketEmployee employee = null;
	
//	Location Information
//	=====================================================================	
	private int xPos = MarketPanel.COUNTERX+((employeeStalls.indexOf(employee)+1)*45), yPos = MarketPanel.COUNTERY-MarketPanel.RECTDIM;
	private int xDestination = xPos, yDestination = yPos;
	private enum Command {noCommand, GoToPhone, CollectItems, GoToCashier, GoToCounter};
	private Command command=Command.noCommand;
	
	private static LinkedList<MarketEmployee> employeeStalls = new LinkedList<MarketEmployee>();
	
	private boolean atCounter = true;

	
	public MarketEmployeeAnimation(MarketEmployee e) {
		employee = e;
		employeeStalls.add(employee);
	}

	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		// Could cause problems if an employee is in the middle of talking with a customer, but very brief interaction anyway
		if (atCounter) {
	        xDestination = MarketPanel.COUNTERX+((employeeStalls.indexOf(employee)+1)*45);
	        yDestination = MarketPanel.COUNTERY-MarketPanel.RECTDIM;
	    }
		
		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToPhone) employee.msgAnimationAtPhone();
			else if (command == Command.CollectItems) employee.msgFinishedCollectingItems();
			else if (command == Command.GoToCashier) employee.msgAnimationAtCashier();
			else if (command == Command.GoToCounter) employee.msgAnimationAtCounter();
			command = Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, MarketPanel.RECTDIM, MarketPanel.RECTDIM);
	}

	public boolean isPresent() {
		return true;
	}

	@Override
	public void doGoToPhone() {
        xDestination = MarketPanel.PHONEX;
        yDestination = MarketPanel.PHONEY;
		command = Command.GoToPhone;		
		atCounter = false;
	}

	@Override
	public void doCollectItems() {
        xDestination = MarketPanel.SHELFX-(MarketPanel.RECTDIM)+(employeeStalls.indexOf(employee)*(MarketPanel.SHELFW+MarketPanel.SHELFGAP));
        yDestination = MarketPanel.SHELFY+(MarketPanel.SHELFH/2)-(MarketPanel.RECTDIM/2);
        command = Command.CollectItems;	
		atCounter = false;
	}
	
	@Override
	public void doDeliverItems() {
        xDestination = MarketPanel.CASHIEREMPINTERACTIONX;
        yDestination = MarketPanel.CASHIEREMPINTERACTIONY;		
		command = Command.GoToCashier;
		atCounter = false;
	}

	@Override
	public void doGoToCounter() {
        xDestination = MarketPanel.COUNTERX+((employeeStalls.indexOf(employee)+1)*45);
        yDestination = MarketPanel.COUNTERY-MarketPanel.RECTDIM;
		command = Command.GoToCounter;
		atCounter = true;
	}

//  Getters
//	=====================================================================
	@Override
	public LinkedList<MarketEmployee> getEmployeeStalls() {
		return employeeStalls;
	}
	
	@Override
	public int getCounterLoc() {
		return employeeStalls.indexOf(employee);
	}
	
//  Utilities
//	=====================================================================
	@Override
	public void removeFromEmployeeStalls() {
		employeeStalls.remove(employee);
	}
}
