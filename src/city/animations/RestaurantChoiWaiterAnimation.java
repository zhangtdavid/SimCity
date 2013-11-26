package city.animations;
import java.awt.Color;
import java.awt.Graphics2D;

import city.Animation;
import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChoiAnimatedWaiter;
import city.interfaces.RestaurantChoiCustomer;
import city.interfaces.RestaurantChoiWaiter;

public class RestaurantChoiWaiterAnimation extends Animation implements RestaurantChoiAnimatedWaiter {

	// Data
	RestaurantChoiWaiter agent;
	private int xPos = 640, yPos = -20;//default waiter position
	private int xDestination = 640, yDestination = -20;//default start position
	public static final int COOKX = 270; // go to plating; also put orders there
	public static final int COOKY = 20;    
	public static final int CASHX = 480;
	public static final int CASHY = 0;
	public static int xTable = 200;
	public static int yTable = 400;
	public static final int WIDTH = 20;
	private static final int restingCoordX = 350;
	private static final int restingCoordY = 20; 
	private static final int doorCoordX = 620;
	private static final int doorCoordY = -20;
	private static int waitCoordX = 420;
	private static int waitCoordY = 150;
	private boolean currentlyAcquired;
	private String orderIcon = new String();
	private int Y;
	private static int counterY;
	boolean init;
	
	// Constructor
	public RestaurantChoiWaiterAnimation(RestaurantChoiWaiter agent) {
		this.agent = agent;
		Y=counterY;
		counterY += 10;
	}
	
	// Abstract
	public void updatePosition() {
		if (xPos < xDestination)
			xPos+=2;
		else if (xPos > xDestination)
			xPos-=2;
		if (yPos < yDestination)
			yPos+=2;
		else if (yPos > yDestination)
			yPos-=2;
		if (xPos == xDestination && yPos == yDestination && currentlyAcquired) {
			agent.msgRelease();
			currentlyAcquired = false;
		}
	}
	
	public void draw(Graphics2D g){
		g.setColor(Color.GRAY);
		g.setColor(Color.lightGray);
		g.drawString(agent.getPerson().getName(),restingCoordX, restingCoordY+Y+10);
		g.setColor(Color.MAGENTA);
		g.fillRect(xPos, yPos, WIDTH, WIDTH);
		g.drawString(orderIcon, xPos, yPos);
		g.setColor(Color.BLACK);
		g.drawString(agent.getPerson().getName(), xPos, yPos+10);
	}
	
	// Movement

	public void DoBringToTable(RestaurantChoiCustomer customer, int xTableCoord, int yTableCoord) {
		xTable = xTableCoord;
		yTable = yTableCoord;
		xDestination = xTable + WIDTH;
		yDestination = yTable - WIDTH;
	}

	public void GoTo(int x, int y){
		xDestination = x+WIDTH;
		yDestination = y-WIDTH;
	}

	@Override
	public void goOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goOffBreak() {
		// TODO Auto-generated method stub
		
	}
	
	public void DoLeave() {
		xDestination = restingCoordX;
		yDestination = restingCoordY+Y;
	}
	
	public void toTopRight(){
		xDestination = doorCoordX;
		yDestination = doorCoordY;
	}
	
	public void toWaitingZone(){
		xDestination = waitCoordX;
		yDestination = waitCoordY;
	}

    // Getters
	public boolean isPresent() {
		return true;
	}
	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
	
    // Setters
	public void setOrderIcon(FOOD_ITEMS choice){
		if(choice == FOOD_ITEMS.steak) orderIcon = "St";
		else if(choice == FOOD_ITEMS.chicken) orderIcon = "C";
		else if(choice == FOOD_ITEMS.salad) orderIcon = "Sa";
		else if(choice == FOOD_ITEMS.pizza) orderIcon = "P";
		else orderIcon = "";
	}
	
	public void setAcquired(){
		currentlyAcquired = true;
	}
	
	public void setCheckboxUnchecked(){
		//gui.setWaiterEnabled(agent.getName()); // TODO deal with this in new gui / waiter panel
	}
	
	public void setCheckboxDisabled(){
		//gui.setWaiterDisabled(agent.getName());
	}
	
	public void setCheckboxEnabled(){
		//gui.setWaiterNotDisabled(agent.getName());
	}
    // Utilities

	


/*// something to consider
	@Override
	public void goToCustomer(RestaurantChoiAnimatedCustomer customer) {
		//customer
		
	}
              
	@Override
	public void goToTable(int position, String menuItem) {
		//table
		
	}

	@Override
	public void goToKitchen(RestaurantChoiAnimatedCook cook) {
		//cook
		
	}

	@Override
	public void goToHome(int position) {
		//resting position
		
	}
	*/
}
