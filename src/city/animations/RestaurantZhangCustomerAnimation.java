package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import utilities.RestaurantZhangTable;
import city.animations.interfaces.RestaurantZhangAnimatedCustomer;
import city.bases.Animation;
import city.gui.exteriors.CityViewApt;
import city.roles.interfaces.RestaurantZhangCustomer;

public class RestaurantZhangCustomerAnimation extends Animation implements RestaurantZhangAnimatedCustomer {

	private static final int CUSTGUIXPOS = -40;
	private static final int CUSTGUIYPOS = -40;
	private static final int CUSTGUIXDEST = -40;
	private static final int CUSTGUIYDEST = -40;
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
	
	private static BufferedImage restaurantZhangCustomerNorthImage = null;
	private static BufferedImage restaurantZhangCustomerEastImage = null;
	private static BufferedImage restaurantZhangCustomerSouthImage = null;
	private static BufferedImage restaurantZhangCustomerWestImage = null;
	private BufferedImage imageToRender;
	
	public RestaurantZhangCustomerAnimation(RestaurantZhangCustomer c){ //HostAgent m) {
		role = c;
		xPos = CUSTGUIXPOS;
		yPos = CUSTGUIYPOS;
		xDestination = CUSTGUIXDEST;
		yDestination = CUSTGUIYDEST;
		try {
			if(restaurantZhangCustomerNorthImage == null || restaurantZhangCustomerEastImage == null || restaurantZhangCustomerSouthImage == null || restaurantZhangCustomerWestImage == null) {
				restaurantZhangCustomerNorthImage = ImageIO.read(CityViewApt.class.getResource("/icons/restaurantZhangPanel/restaurantZhangCustomerNorthImage.png"));
				restaurantZhangCustomerEastImage = ImageIO.read(CityViewApt.class.getResource("/icons/restaurantZhangPanel/restaurantZhangCustomerEastImage.png"));
				restaurantZhangCustomerSouthImage = ImageIO.read(CityViewApt.class.getResource("/icons/restaurantZhangPanel/restaurantZhangCustomerSouthImage.png"));
				restaurantZhangCustomerWestImage = ImageIO.read(CityViewApt.class.getResource("/icons/restaurantZhangPanel/restaurantZhangCustomerWestImage.png"));
				imageToRender = restaurantZhangCustomerNorthImage;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void updatePosition() {
		if (xPos < xDestination) {
			xPos++;
			imageToRender = restaurantZhangCustomerEastImage;
		} else if (xPos > xDestination) {
			xPos--;
			imageToRender = restaurantZhangCustomerWestImage;
		}
		if (yPos < yDestination) {
			yPos++;
			imageToRender = restaurantZhangCustomerSouthImage;
		} else if (yPos > yDestination) {
			yPos--;
			imageToRender = restaurantZhangCustomerNorthImage;
		}

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
		g.drawImage(imageToRender, xPos, yPos, null);
		if(foodString != null)
			g.drawString(foodString, xPos, yPos + CUSTHEIGHT + TEXTHEIGHT);
	}

	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		role.msgGotHungry();
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
