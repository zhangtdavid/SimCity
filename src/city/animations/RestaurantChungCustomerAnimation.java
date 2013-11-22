package city.animations;

import java.awt.*;

import city.Animation;
import city.animations.interfaces.RestaurantChungAnimatedCustomer;
import city.interfaces.RestaurantChungCustomer;

public class RestaurantChungCustomerAnimation extends Animation implements RestaurantChungAnimatedCustomer {
	private RestaurantChungCustomer agent = null;
	private RestaurantGui gui;

	private boolean isPresent = false;
	private boolean isHungry = false;
	
//	Fixed Numbers
//	=====================================================================
	private static final int CRECTDIM = 20;
    private static final int xEntrance = -40, yEntrance = -40;
    private static final int xWaitingArea = 50, yWaitingArea = 100;
    private static final int xCashier= 50, yCashier = 40+20;  

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
	public RestaurantChungCustomerAnimation(RestaurantChungCustomer c, RestaurantGui gui) {
		agent = c;
		xPos = -40;
		yPos = -40;
		xDestination = -40;
		yDestination = -40;
		this.gui = gui;
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
			if (command == Command.GoToSeat) agent.msgAnimationAtSeat(); // Released atSeat semaphore
			else if (command == Command.GoToCashier) {
				agent.msgAnimationAtCashier();
			}
			else if (command == Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		// Paints Customer Square
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, CRECTDIM, CRECTDIM);
		
		// Paints Customer's order
		g.setColor(Color.RED);
		if (agent.getState().equals("WaitingForFood")) {
			g.drawString(agent.getOrder() + "?", xPos+5, yPos+25);
		}
		
		// Paints Customer's fulfilled order
		g.setColor(Color.BLACK);		
		if (agent.getState().equals("Eating")) {
			g.drawString(agent.getOrder(), xPos+5, yPos+25);
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
	
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	
	public boolean isHungry() {
		return isHungry;
	}

	public void DoGoToWaitingArea(int waitingPosition) {
		xDestination = xWaitingArea;
		yDestination = yWaitingArea+(waitingPosition*30);
	}
	
	public void DoGoToSeat(int x, int y) {
    	System.out.println("Customer Gui going to seat");
		xDestination = x;
		yDestination = y;
		command = Command.GoToSeat;
	}
	
	public void DoExitRestaurant() {
		xDestination = xEntrance;
		yDestination = yEntrance;
		command = Command.LeaveRestaurant;
	}

	public void DoGoToCashier() {
		xDestination = xCashier;
		yDestination = yCashier;
		command = Command.GoToCashier;
	}
}