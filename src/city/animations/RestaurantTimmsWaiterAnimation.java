package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.animations.interfaces.RestaurantTimmsAnimatedCook;
import city.animations.interfaces.RestaurantTimmsAnimatedCustomer;
import city.animations.interfaces.RestaurantTimmsAnimatedWaiter;
import city.bases.Animation;
import city.roles.interfaces.RestaurantTimmsWaiter;

public class RestaurantTimmsWaiterAnimation extends Animation implements RestaurantTimmsAnimatedWaiter {
	
	// Data

	// Commands for keeping track of the animation state
	private enum Command {noCommand, GoToCustomer, GoToTable, GoToKitchen, GoToHome};
	private Command command = Command.noCommand;
	
	// Variables unique to a Waiter
	private RestaurantTimmsWaiter waiter;
	private String menuItem;
	public static final int BREAK_X = 100;
	public static final int BREAK_Y = 400;
	public static final int HOME_START_X = 400;
	public static final int HOME_START_Y = 80;
	public static final int AT_CUSTOMER_MARGIN = 20;
	public static final int AT_TABLE_MARGIN = 20;
	public static final int AT_KITCHEN_MARGIN = 20;
	// private ControlPanel controlPanel; // TODO

	// Variables used to draw the object's position
	private int xDestination, yDestination;
	
	// Constants defining the properties of the animation
	public static final int INITIAL_X = -40;
	public static final int INITIAL_Y = -40;
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;
	public static final Color COLOR = Color.BLUE;
	public static final Color TEXT_COLOR = Color.WHITE;

    // Constructor 
	
	public RestaurantTimmsWaiterAnimation(RestaurantTimmsWaiter w) {
		// this.controlPanel = control; // TODO
		this.waiter = w;
		this.menuItem = null;
		this.xPos = INITIAL_X;
		this.yPos = INITIAL_Y;
		this.xDestination = INITIAL_X;
		this.yDestination = INITIAL_Y;
	}
	
	// Abstract
	
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
			if (command == Command.GoToCustomer) {
				waiter.guiAtCustomer();
			}
			if (command == Command.GoToTable) {
				waiter.guiAtTable();
				menuItem = null;
			}
			if (command == Command.GoToKitchen) {
				waiter.guiAtKitchen();
			}
			if (command == Command.GoToHome) {
				waiter.guiAtHome();
			}
			command = Command.noCommand;
		}
	}
	
	public void draw(Graphics2D g) {
		g.setColor(COLOR);
		g.fillRect(xPos, yPos, WIDTH, HEIGHT);
		
		if (menuItem != null) {
			g.setColor(TEXT_COLOR);
			g.drawString(menuItem, xPos, (yPos + HEIGHT/2));
		}
	}	
	
	// Movement
	
	public void goOnBreak() {
		xDestination = BREAK_X;
		yDestination = BREAK_Y;
		this.menuItem = "#";
	}
	
	public void goOffBreak() {
		// controlPanel.setWaiterEnabled(waiter); // TODO
		this.menuItem = null;
	}

	public void goToCustomer(RestaurantTimmsAnimatedCustomer c) {
		command = Command.GoToCustomer;
		xDestination = (c.getXPos() - getMargin());
		yDestination = (c.getYPos() - getMargin());
	}
	
	public void goToTable(int position, String menuItem) {
		command = Command.GoToTable;
		xDestination = (RestaurantTimmsTableAnimation.getXPos(position) - getMargin());
		yDestination = (RestaurantTimmsTableAnimation.getYPos(position) - getMargin());
		
		if (menuItem != null)
			this.menuItem = menuItem.substring(0, 2).toUpperCase();
	}
	
	public void goToKitchen(RestaurantTimmsAnimatedCook cook) {
		command = Command.GoToKitchen;
		xDestination = (cook.getXPos() - getMargin());
		yDestination = (cook.getYPos());
	}
	
	public void goToHome(int position) {
		command = Command.GoToHome;
		xDestination = (HOME_START_X);
		yDestination = (HOME_START_Y + (position * HEIGHT) + (position * (HEIGHT / 2)));
	}
	
	// Getters
	
	// Setters
	
	// Utilities
	
	private int getMargin() {
		int answer = 0;
		if (command == Command.GoToCustomer)
			answer = AT_CUSTOMER_MARGIN;
		if (command == Command.GoToTable)
			answer = AT_TABLE_MARGIN;
		if (command == Command.GoToKitchen)
			answer = AT_KITCHEN_MARGIN;
		return answer;
	}
	
}
