package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.animations.interfaces.MarketAnimatedEmployee;
import city.bases.Animation;
import city.gui.interiors.MarketPanel;
import city.roles.interfaces.MarketEmployee;

public class MarketEmployeeAnimation extends Animation implements MarketAnimatedEmployee {
	private MarketEmployee employee = null;
	
//	Location Information
//	=====================================================================	
	private int xPos = 20, yPos = 20;
	private int xDestination = xPos, yDestination = yPos;
	private enum Command {noCommand, GoToPhone, CollectItems, GoToCashier, GoToCounter};
	private Command command=Command.noCommand;
	
	public MarketEmployeeAnimation(MarketEmployee e) {
		employee = e;
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

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToPhone) employee.msgAnimationAtPhone();
			else if (command == Command.CollectItems) employee.msgFinishedCollectingItems();
			else if (command == Command.GoToCashier) employee.msgAnimationAtCashier();
			else if (command == Command.GoToCounter) employee.msgAnimationAtCounter();
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, MarketPanel.RECTDIM, MarketPanel.RECTDIM);
	}

	public boolean isPresent() {
		return true;
	}

	public void doGoToPhone() {
        xDestination = MarketPanel.PHONEX;
        yDestination = MarketPanel.PHONEY;
		command = Command.GoToPhone;		
	}
	
	public void doDeliverItems() {
		// TODO Auto-generated method stub
		
	}

	public void doCollectItems() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doGoToCounter() {
        xDestination = MarketPanel.CASHIEREMPINTERACTIONX;
        yDestination = MarketPanel.CASHIEREMPINTERACTIONY;
		command = Command.GoToCounter;			
	}
}
