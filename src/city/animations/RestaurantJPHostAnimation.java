package city.animations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import city.bases.Animation;
import city.roles.RestaurantJPCustomerRole;
import city.roles.RestaurantJPHostRole;

public class RestaurantJPHostAnimation extends Animation{


    private int xPos = -20, yPos = -20;//default waiter position
    private int xDestination = -20, yDestination = -20;//default start position

    public static final int xTable = 200;
    public static final int yTable = 250;
    
    public Map <Integer, Dimension> TableLocations = new HashMap<Integer, Dimension>(); 

    public RestaurantJPHostAnimation(RestaurantJPHostRole agent) {
       //  this.agent = agent;
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
        		& (xDestination == xTable + 20) /*& (yDestination == yTable - 20)*/) {
           //agent.msgAtTable();
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, 20, 20);
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(RestaurantJPCustomerRole customer, Integer table) {
        xDestination = xTable + 20;
        yDestination = yTable - (table - 1)*100 - 20;
    }

    public void DoLeaveCustomer() {
        xDestination = -20;
        yDestination = -20;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
}
