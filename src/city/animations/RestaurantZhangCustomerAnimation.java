package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import utilities.RestaurantZhangTable;
import city.Animation;
import city.animations.interfaces.RestaurantZhangAnimatedCustomer;
import city.interfaces.RestaurantZhangCustomer;

public class RestaurantZhangCustomerAnimation extends Animation implements RestaurantZhangAnimatedCustomer {

	private static final int CUSTGUIXPOS = -40;
	private static final int CUSTGUIYPOS = -40;
	private static final int CUSTGUIXDEST = -40;
	private static final int CUSTGUIYDEST = -40;
	private static final int CUSTWIDTH = 20;
	private static final int CUSTHEIGHT = 20;
	private static final int EXITDESTX = -40;
	private static final int EXITDESTY = -40;
	private static int RESTAURANTENTRANCEX = 30;
	private static int RESTAURANTENTRANCEY = 30;
	
	private String foodString = null;
	private static final int TEXTHEIGHT = 14;
	
	private RestaurantZhangCustomer role = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToRestaurant, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	public RestaurantZhangCustomerAnimation(RestaurantZhangCustomer c){ //HostAgent m) {
		role = c;
		xPos = CUSTGUIXPOS;
		yPos = CUSTGUIYPOS;
		xDestination = CUSTGUIXDEST;
		yDestination = CUSTGUIYDEST;
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
			if(command == Command.GoToRestaurant) {
				role.msgAnimationFinishedEnterRestaurant();
			}
			if (command==Command.GoToSeat) {
				role.msgAnimationFinishedGoToSeat();
			} else if (command==Command.LeaveRestaurant) {
				role.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
//				gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, CUSTWIDTH, CUSTHEIGHT);
		if(foodString != null)
			g.drawString(foodString, xPos, yPos + CUSTHEIGHT + TEXTHEIGHT);
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		role.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToSeat(RestaurantZhangTable t) {//later you will map seatnumber to table coordinates.
		xDestination = t.getX();
		yDestination = t.getY();
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = EXITDESTX;
		yDestination = EXITDESTY;
		foodString = null;
		command = Command.LeaveRestaurant;
	}
	
	public void DoGoToEntrance() {
		xDestination = RESTAURANTENTRANCEX;
		yDestination = RESTAURANTENTRANCEY;
		command = Command.GoToRestaurant;
	}
	
	public void setFoodLabel(String choice, boolean isHere) {
		foodString = choice;
		if(isHere == false) {
			foodString += "?";
		}
	}
	
	public void setWaitingPosition(int x, int y) {
		RESTAURANTENTRANCEX = x;
		RESTAURANTENTRANCEY = y;
	}
}
