package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;

import city.animations.interfaces.RestaurantChungAnimatedCustomer;
import city.bases.Animation;
import city.gui.interiors.RestaurantChungPanel;
import city.roles.interfaces.RestaurantChungCustomer;

public class RestaurantChungCustomerAnimation extends Animation implements RestaurantChungAnimatedCustomer {
	private RestaurantChungCustomer agent = null;
	
	private static LinkedList<RestaurantChungCustomer> waitingCustomers = new LinkedList<RestaurantChungCustomer>(); 
	
	private boolean waiting = false;	

	private boolean isPresent = false;
	private boolean isHungry = false;

//	Location Information
//	=====================================================================
	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, GoToCashier, LeaveRestaurant};
	private Command command=Command.noCommand;

//	Image Variables
//	=====================================================================
//	Not working because of path issues TODO
//	private ImageIcon chicken = new ImageIcon("Images/Chicken.png");
//	private ImageIcon pizza = new ImageIcon("Images/Pizza.png");
//	private ImageIcon salad = new ImageIcon("Images/Salad.png");
//	private ImageIcon steak = new ImageIcon("Images/Steak.png");

//	Constructor
//	=====================================================================
	public RestaurantChungCustomerAnimation(RestaurantChungCustomer c) {
		agent = c;
		xPos = RestaurantChungPanel.ENTRANCEX;
		yPos = RestaurantChungPanel.ENTRANCEY;
		xDestination = xPos;
		yDestination = yPos;
		DoGoToWaitingArea();
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
		
		if (waiting) {
			xDestination = RestaurantChungPanel.WAITINGAREAX;
			yDestination = RestaurantChungPanel.WAITINGAREAY+(waitingCustomers.indexOf(agent)*30);
		}

		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.GoToSeat) agent.msgAnimationAtSeat(); // Released atSeat semaphore
			else if (command == Command.GoToCashier) {
				agent.msgAnimationAtCashier();
			}
			else if (command == Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
//				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		// Paints Customer Square
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, RestaurantChungPanel.RECTDIM, RestaurantChungPanel.RECTDIM);
		
		// Paints Customer's order
		g.setColor(Color.RED);
		if (agent.getState().equals("WaitingForFood")) {
			g.drawString(agent.getOrder() + "?", xPos+5, yPos+25);
		}
		
		// Paints Customer's fulfilled order
		g.setColor(Color.BLACK);		
		if (agent.getState().equals("Eating")) {
			g.drawString(agent.getOrder().toString(), xPos+5, yPos+25);
//			if (agent.getOrder().equals("Chicken")){
//				g.drawImage(chicken.getImage(), xPos+3, yPos+20, null);
//			}
//			else if (agent.getOrder().equals("Salad")) {
//				g.drawImage(salad.getImage(), xPos+3, yPos+20, null);
//			}
//			else if (agent.getOrder().equals("Steak")) {
//				g.drawImage(steak.getImage(), xPos+3, yPos+20, null);
//			}
//			else if (agent.getOrder().equals("Pizza")) {
//				g.drawImage(pizza.getImage(), xPos+3, yPos+20, null);
//			}
		}
	}

//	Utilities
//	=====================================================================
	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public boolean isPresent() {
		return isPresent;
	}
	
//	public void setHungry() {
//		isHungry = true;
//		agent.gotHungry();
//		setPresent(true);
//	}
	
	public boolean isHungry() {
		return isHungry;
	}

	public void DoGoToWaitingArea() {
		waitingCustomers.add(agent);
		waiting = true;
		xDestination = RestaurantChungPanel.WAITINGAREAX;
		yDestination = RestaurantChungPanel.WAITINGAREAY+(waitingCustomers.indexOf(agent)*30);
	}
	
	public void DoGoToSeat(int x, int y) {
		waitingCustomers.remove(agent);
    	waiting = false;
    	xDestination = x;
		yDestination = y;
		command = Command.GoToSeat;
	}
	
	public void DoExitRestaurant() {
		waitingCustomers.remove(agent);
    	waiting = false;
		xDestination = RestaurantChungPanel.EXITX;
		yDestination = RestaurantChungPanel.EXITY;
		command = Command.LeaveRestaurant;
	}

	public void DoGoToCashier() {
		xDestination = RestaurantChungPanel.CASHIERINTERACTIONX;
		yDestination = RestaurantChungPanel.CASHIERINTERACTIONY;
		command = Command.GoToCashier;
	}
}