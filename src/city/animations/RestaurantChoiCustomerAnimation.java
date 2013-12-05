package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChoiAnimatedCustomer;
import city.bases.Animation;
import city.roles.interfaces.RestaurantChoiCustomer;

public class RestaurantChoiCustomerAnimation extends Animation implements RestaurantChoiAnimatedCustomer{

	//Data
	private RestaurantChoiCustomer choicustomer = null;
	private boolean isPresent = false; // present at restaurant?
	private boolean isHungry = false;
	//RestaurantGui gui; // shouldn't need this
	private int xPos, yPos;
	public static final int WIDTH = 20;
	private static int leaveCoordX = 520;//cashier conveniently placed here
	private static int leaveCoordY = -20; 
	private static int enterCoordX = 520;// in case we have situations where entrance != exit
	private static int enterCoordY = -20;
	private static int waitCoordX = 420; // people queue here
	private static int waitCoordY = 170;
	private static final int XDISHES = 480; // this is where the dishes are
	private static final int YDISHES = 250;
	private int xDestination, yDestination;
	private String orderIcon = new String();
	private int waitY; // so no stacking of customers (TODO need to fix collisions in general)

	private Command command = Command.noCommand;

	//Constructor
	public RestaurantChoiCustomerAnimation(RestaurantChoiCustomer c) { 
		choicustomer = c;
		xPos = enterCoordX; 
		yPos = enterCoordY;
		xDestination = enterCoordX;
		yDestination = enterCoordY;
	}

	//Abstract
	public void updatePosition() {
		if (xPos < xDestination) {
			xPos+=1;
		} else if (xPos > xDestination) {
			xPos-=1;
		}
		if (yPos < yDestination) {
			yPos+=1;
		} else if (yPos > yDestination) {
			yPos-=1;
		}
		if (xPos == xDestination && yPos == yDestination) {
			if(command == Command.GoToWaiting){
				
			}
			if(command == Command.GoToDishes){
				choicustomer.msgAnimationFinishedGoToDishes();
			}
			if(command == Command.GoToCashier){
				choicustomer.msgAnimationFinishedGoToCashier();
			}
			if (command == Command.GoToSeat) {
				choicustomer.msgAnimationFinishedGoToSeat();
			} else if (command == Command.LeaveRestaurant) {
				choicustomer.msgAnimationFinishedLeaveRestaurant();
				isHungry = false;
				//gui.setCustomerEnabled(agent); TODO deal with this somehow; we shouldn't need to enable customer, we just set customer as not hungry now (he's a person)
			}
			command = Command.noCommand;
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, WIDTH, WIDTH);
		g.drawString(orderIcon,xPos,yPos);
		g.setColor(Color.BLACK);
		g.drawString(choicustomer.getPerson().getName(), xPos,yPos+WIDTH/2);
	}

	//Movements
	
	public void DoGoToSeat(int x, int y) {
		xDestination = x;
		yDestination = y;
		command = Command.GoToSeat;
	}
	
	public void DoGoToDishes(){
		xDestination = XDISHES;
		yDestination = YDISHES;
		command = Command.GoToDishes;
	}
	public void DoGoToWaiting(int offset){
		xDestination = waitCoordX;
		waitY = waitCoordY+offset*10;
		yDestination = waitY;
		
		command = Command.GoToWaiting;
	}
 
	public void DoExitRestaurant() {
		xDestination = leaveCoordX; // exit top right
		yDestination = leaveCoordY;
		command = Command.LeaveRestaurant;
	}

	public void DoGoToCashier() {
		xDestination = enterCoordX;
		yDestination = enterCoordY;
		command = Command.GoToCashier;
	}
	
	//Getters
	public int getXWait(){
		return waitCoordX;
	}
	
	public int getYWait(){
		return waitY;
	}
	
	public boolean isPresent() {
		return isPresent;
	}

	public boolean isHungry() {
		return isHungry;
	}

	//Setters
	public void setHungry() {
		isHungry = true;
		//agent.gotHungry(); // TODO This now seems backwards for the purposes of the restaurant
		setPresent(true);
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}
	
	public void resetLocation(){
		xPos = enterCoordX;
		yPos = enterCoordY;
	}
	
	public void setOrderIcon(FOOD_ITEMS choice, boolean gotFood){
		if(choice == FOOD_ITEMS.steak) orderIcon = "St";
		else if(choice == FOOD_ITEMS.chicken) orderIcon = "C";
		else if(choice == FOOD_ITEMS.salad) orderIcon = "Sa";
		else if(choice == FOOD_ITEMS.pizza) orderIcon = "P";
		else orderIcon = "";
		//if you haven't gotten food; if gotFood is false; add "?"
		if(!gotFood){
			orderIcon += "?";
		}
	}
}
