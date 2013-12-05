package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.animations.interfaces.RestaurantTimmsAnimatedCashier;
import city.animations.interfaces.RestaurantTimmsAnimatedCustomer;
import city.bases.Animation;
import city.roles.interfaces.RestaurantTimmsCustomer;

public class RestaurantTimmsCustomerAnimation extends Animation implements RestaurantTimmsAnimatedCustomer {
	
	// Data
	
	// Commands for keeping track of the animation state
	private enum Command {noCommand, goToRestaurant, GoToTable, GoToCashier, LeaveRestaurant};
	private Command command = Command.noCommand;
	
	// Variables unique to a Customer
	private RestaurantTimmsCustomer customer;
	private String plate;
	private boolean atRestaurant = false;
	// private ControlPanel controlPanel; // TODO
	public static final int AT_CASHIER_MARGIN = 20;

	// Variables used to draw the object's position
	private int xDestination, yDestination;
	
	// Constants defining the properties of the animation
	public static final int INITIAL_X = -40;
	public static final int INITIAL_Y = -40;
	public static final int WAITING_X = 30;
	public static final int WAITING_Y = 50;
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;
	public static final Color COLOR = Color.GREEN;
	public static final Color TEXT_COLOR = Color.BLACK;
	
	// Constructor

	public RestaurantTimmsCustomerAnimation(RestaurantTimmsCustomer customer) {
		this.customer = customer;
		// this.controlPanel = control; // TODO
		xPos = INITIAL_X;
		yPos = INITIAL_Y;
		xDestination = INITIAL_X;
		yDestination = INITIAL_Y;
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
			if (command == Command.goToRestaurant) {
				customer.guiAtLine();
			} else if (command == Command.GoToTable) {
				customer.guiAtTable();
			} else if (command == Command.GoToCashier) {
				customer.guiAtCashier();
			} else if (command == Command.LeaveRestaurant) {
				customer.guiAtExit();
				atRestaurant = false;
				this.setVisible(false);
			}
			command = Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(COLOR);
		g.fillRect(xPos, yPos, WIDTH, HEIGHT);
		
		if (plate != null) {
			g.setColor(TEXT_COLOR);
			g.drawString(plate, xPos, (yPos + HEIGHT/2));
		}
	}
	
	// Movement
	
	/**
	 * Moves the customer to the restaurant
	 */
	public void goToRestaurant() {
		atRestaurant = true;
		this.setVisible(true);
		xDestination = WAITING_X;
		yDestination = WAITING_Y;
		command = Command.goToRestaurant;
	}
	
    /**
     * Moves the customer from the waiting area to the specified table.
     * 
     * @param table the number of the restaurant's table used for positioning
     */
	public void goToTable(int table) {
		xDestination = RestaurantTimmsTableAnimation.getXPos(table);
		yDestination = RestaurantTimmsTableAnimation.getYPos(table);
		command = Command.GoToTable;
	}
	
    /**
     * Moves the customer from its table to the specified cashier.
     * 
     * @param cashier the cashier to move towards
     */
	public void goToCashier(RestaurantTimmsAnimatedCashier cashier) {
		plate = null;
		command = Command.GoToCashier;
		xDestination = (cashier.getXPos() - getMargin());
		yDestination = (cashier.getYPos() - getMargin());
	}

	/**
	 * Moves the customer from on-screen to off-screen
	 */
	public void goToExit() {
		xDestination = INITIAL_X;
		yDestination = INITIAL_Y;
		command = Command.LeaveRestaurant;
	}
	
	// Getters
	
    /**
     * Returns whether or not the customer is at the restaurant. If the
     * customer is at the restaurant, this means that he shouldn't be told
     * to go to the restaurant by the ControlPanel GUI.
     */
	public boolean getAtRestaurant() {
		return atRestaurant;
	}
	
	// Setters
	
    /**
     * Allows food to be displayed in the possession of a customer.
     * 
     * @param plate the contents of the customer's plate
     */
	public void setPlate(String plate) {
		this.plate = plate.substring(0, 2).toUpperCase();
	}
	
	// Utilities
	
	private int getMargin() {
		int answer = 0;
		if (command == Command.GoToCashier)
			answer = AT_CASHIER_MARGIN;
		return answer;
	}
}
