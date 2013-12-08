package city.animations;
import java.awt.Color;
import java.awt.Graphics2D;

import city.Application.FOOD_ITEMS;
import city.animations.interfaces.RestaurantChoiAnimatedWaiter;
import city.bases.Animation;
import city.roles.interfaces.RestaurantChoiCustomer;
import city.roles.interfaces.RestaurantChoiWaiter;

public class RestaurantChoiWaiterAnimation extends Animation implements RestaurantChoiAnimatedWaiter {

	// Data
	RestaurantChoiWaiter agent;
	private int xPos = 540, yPos = -20;//default waiter position
	private int xDestination = 540, yDestination = -20;//default start position
	private boolean currentlyAcquired;
	private String orderIcon = new String();
	private int Y;
	private static int counterY;
	boolean init;
	public static int WAITX = 420;
	public static int WAITY = 150;
	public static int TABLEX = 200;
	public static int TABLEY = 400;
	
	// Constructor
	public RestaurantChoiWaiterAnimation(RestaurantChoiWaiter agent) {
		this.agent = agent;
		Y=counterY;
		counterY += 10;
	}
	
	// Abstract
	public void updatePosition() {
		if (xPos < xDestination)
			xPos+=1;
		else if (xPos > xDestination)
			xPos-=1;
		if (yPos < yDestination)
			yPos+=1;
		else if (yPos > yDestination)
			yPos-=1;
		if (xPos == xDestination && yPos == yDestination && currentlyAcquired) {
			agent.msgRelease();
			currentlyAcquired = false;
		}
	}
	
	public void draw(Graphics2D g){
		g.setColor(Color.GRAY);
		g.setColor(Color.lightGray);
		g.drawString(agent.getPerson().getName(),RESTX, RESTY+Y+10);
		g.setColor(Color.MAGENTA);
		g.fillRect(xPos, yPos, WIDTH, WIDTH);
		g.drawString(orderIcon, xPos, yPos);
		g.setColor(Color.BLACK);
		g.drawString(agent.getPerson().getName(), xPos, yPos+10);
	}
	
	// Movement

	public void DoBringToTable(RestaurantChoiCustomer customer, int xTableCoord, int yTableCoord) {
		TABLEX = xTableCoord;
		TABLEY = yTableCoord;
		xDestination = TABLEX + WIDTH;
		yDestination = TABLEY - WIDTH;
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
		xDestination = RESTX;
		yDestination = RESTY+Y;
	}
	
	public void toTopRight(){
		xDestination = DOORX;
		yDestination = DOORY;
	}
	
	public void toWaitingZone(){
		xDestination = WAITX;
		yDestination = WAITY;
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
