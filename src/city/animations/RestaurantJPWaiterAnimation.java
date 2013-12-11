package city.animations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import utilities.RestaurantJPWaiterBase;
import city.bases.Animation;

public class RestaurantJPWaiterAnimation extends Animation{

    private RestaurantJPWaiterBase agent = null;
    private boolean onBreak = false;
    
    private int xPos = 0, yPos = 0;//default waiter position
    private int xDestination = 0, yDestination = 0;//default start position

    public static final int xTable = 200;
    public static final int yTable = 250;
    public static final int checkIn = -10;
    public static final int waiterSize = 20;
    public int xHome = 0;
    public int yHome = 0;
    
    public static final int tableSeparation = 100;
    public static final int CookX = 150;
    public static final int CookY = 380;
    
    private boolean delivering = false;
    private String food = new String();
    
    public Map <Integer, Dimension> TableLocations = new HashMap<Integer, Dimension>(); 

    public RestaurantJPWaiterAnimation(RestaurantJPWaiterBase w, int place) {
        agent = w;
		xPos = -20;
		yPos = -20;
		xDestination = xPos;
		yDestination = yPos;
		xHome = 400;
		yHome = 50 + place*50;
        //TableLocations.put(1, (200, 250));
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

        if (xPos == xDestination && yPos == yDestination
        		&& ((xDestination == xTable + waiterSize || xDestination == checkIn) || yDestination == CookY || xDestination == xHome + 20) ) {
           agent.msgAtDestination();
           xDestination = xHome;
           yDestination = yHome;
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, waiterSize, waiterSize);
        if(delivering){
        	g.setColor(Color.BLACK);
        	g.drawString(food, xPos, yPos);
        }
    }

    public boolean isPresent() {
        return true;						//USed to just return true
    }

    public void DoGoToOrigin() {
    	delivering = false;
    	xDestination = xHome;
    	yDestination = yHome;
    }
    
    public void DoGoToTable(Integer table) {
        xDestination = xTable + waiterSize;
        yDestination = yTable - (table - 1)*tableSeparation - waiterSize;
    }

    public void DoDeliverFood(String f){
    	delivering = true;
    	food = f;
    }
    public void DoLeaveCustomer() {
        xDestination = xHome;					//Sloppy little hack to make sure update position works
        yDestination = yHome;
    }

    public void DoPickUpCustomer(){
    	xDestination = xHome + 20;
    	yDestination = yHome + 20;
    }
    public void DoGoToCook(){
    	xDestination = CookX;
    	yDestination = CookY;
    }
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void setDelivering(boolean b){
    	if(b)
    		delivering = true;
    	else
    		delivering = false;
    }
    
    public void setBreakRequested() {
		onBreak = true;
	}
    
    public void setOffBreakRequested(){
    	onBreak = false;
    }
    
	public boolean hasAskedForBreak() {
		return onBreak;
	}
}
