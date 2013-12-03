package city.animations;

import java.awt.*;

import utilities.RestaurantZhangTable;
import city.Animation;
import city.animations.interfaces.RestaurantZhangAnimatedWaiter;
import city.interfaces.RestaurantZhangWaiter;

public class RestaurantZhangWaiterAnimation extends Animation implements RestaurantZhangAnimatedWaiter {

    private RestaurantZhangWaiter waiter = null;

    public final int BASEX;
    public final int BASEY;
    public static final int BREAKX = 600;
    public static final int BREAKY = 600;
    public static final int RESTAURANTENTRANCEX = 30;
    public static final int RESTAURANTENTRANCEY = 30;
    
    private int xPos , yPos;//default waiter position
    private int xDestination, yDestination;//default start position
    
    private String foodString = null;
	private static final int TEXTHEIGHT = 14;
    
    private boolean atDestination = true;
    
    public static final int MOVEMENTINTERVAL = 20;

    public RestaurantZhangWaiterAnimation(RestaurantZhangWaiter waiter, int baseX, int baseY) {
        this.waiter = waiter;
        BASEX = baseX;
        BASEY = baseY;
        xPos = xDestination = BASEX;
        yPos = yDestination = BASEY;
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
        
    	   if(xPos == xDestination && yPos == yDestination && atDestination == false) {
    		   atDestination = true;
    		   waiter.msgAtDestination();
	       }
    }

    public void draw(Graphics2D g) {
    	g.setColor(Color.PINK);
    	g.fillRect(BASEX, BASEY, MOVEMENTINTERVAL, MOVEMENTINTERVAL);
    	g.drawString(waiter.getPerson().getName(), BASEX, BASEY);
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, MOVEMENTINTERVAL, MOVEMENTINTERVAL);
        if(foodString != null)
			g.drawString(foodString, xPos, yPos + MOVEMENTINTERVAL + TEXTHEIGHT);
    }

    public boolean isPresent() {
        return true;
    }

    public void GoToTable(RestaurantZhangTable t) {
    	//currentTable = t;
        xDestination = t.getX() + MOVEMENTINTERVAL;
        yDestination = t.getY() - MOVEMENTINTERVAL;
        atDestination = false;
    }
    
    public void GoToDestination(int x, int y) {
    	xDestination = x;
        yDestination = y;
        atDestination = false;
    }
    
    public void GoToCustomer(int pos) {
    	xDestination = RESTAURANTENTRANCEX  + MOVEMENTINTERVAL;
    	yDestination = RESTAURANTENTRANCEY + pos * 30  + MOVEMENTINTERVAL;
    	atDestination = false;
    }
    
    public void GoToBreak() {
    	xDestination = BREAKX;
    	yDestination = BREAKY;
    	atDestination = false;
    }

    public boolean ReturnToBase() {
    	if(xDestination != BASEX || yDestination != BASEY) {
	    	xDestination = BASEX;
	        yDestination = BASEY;
	    	atDestination = false;
	    	return true;
    	}
    	return false;
    }
    
    public void setFoodLabel(String choice, boolean isHere) {
		foodString = choice;
		if(isHere == false) {
			foodString += "?";
		}
	}

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
