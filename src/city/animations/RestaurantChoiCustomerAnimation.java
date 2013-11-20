package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;

import city.Animation;
import city.animations.interfaces.RestaurantChoiAnimatedCustomer;
import city.interfaces.RestaurantChoiCustomer;

public class RestaurantChoiCustomerAnimation extends Animation implements RestaurantChoiAnimatedCustomer{

	//Data
	private RestaurantChoiCustomer choicustomer = null;
	private boolean isPresent = false; // present at restaurant?
	private boolean isHungry = false;
	//RestaurantGui gui; // shouldn't need this
	private int xPos, yPos;
	public static final int WIDTH = 20;
	private static int leaveCoordX = 640;//cashier conveniently placed here
	private static int leaveCoordY = -20; 
	private static int enterCoordX = 640;// in case we have situations where entrance != exit
	private static int enterCoordY = -20;
	private static int waitCoordX = 420; // people queue here
	private static int waitCoordY = 170;
	private static final int XDISHES = 550; // this is where the dishes are
	private static final int YDISHES = 250;
	private int xDestination, yDestination;
	private String orderIcon = new String();
	private int waitY; // so no stacking of customers (TODO need to fix collisions in general)


	private enum Command {
		noCommand, GoToSeat, LeaveRestaurant, GoToDishes, GoToCashier, GoToWaiting
	};
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
			xPos+=2;
		} else if (xPos > xDestination) {
			xPos-=2;
		}
		if (yPos < yDestination) {
			yPos+=2;
		} else if (yPos > yDestination) {
			yPos-=2;
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
				System.out.println("Enabling customer again");
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
		g.drawString(choicustomer.getName(), xPos,yPos+WIDTH/2);
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
	
	public void setOrderIcon(int choice, boolean gotFood){
		switch(choice){
		case 1:
			orderIcon = "St";
			break;
		case 2:
			orderIcon = "C";
			break;
		case 3:
			orderIcon = "Sa";
			break;
		case 4:
			orderIcon = "P";
			break;
		default:
			orderIcon = "";
			break;
		}
		//if you haven't gotten food; if gotFood is false; add "?"
		if(!gotFood){
			orderIcon+=" ?";
		}
	}
}
