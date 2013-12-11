package city.animations;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import city.bases.Animation;
import city.roles.BankManagerRole;
import city.roles.RestaurantJPCookRole;
import city.roles.interfaces.BankCustomer;

public class BankManagerAnimation extends Animation{

    private BankManagerRole agent = null;
    
    private int xPos;
    private int yPos;//default waiter position
    private int xDestination, yDestination;//default start position

    public static final int checkIn = -10;
    public static final int customerSize = 20;
    
    private boolean moving;
    private boolean depositing = false;
    private String dialogue = new String();
    
    public Map<Integer, Integer> TellerLocations = new HashMap<Integer, Integer>();
    

    public BankManagerAnimation(BankManagerRole m) {
        agent = m;
        xPos = -20;
        yPos = -20;
		xDestination = 400;
		yDestination = 400;
		moving = false;
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

        if (xPos == xDestination && yPos == yDestination && moving){
        	   moving = false;
        }
    }

    public void draw(Graphics2D g) {
    	
        g.setColor(Color.BLACK);
        g.fillRect(xPos, yPos, customerSize, customerSize);
        if(depositing){
        	g.setColor(Color.BLACK);
        	g.drawString(dialogue, 450, 260);
        }
    }
    
    public void DoEnterBank(){
    	moving = true;
    }
    public void DoDirectDeposit(int amount){
    	dialogue = ("+$" + amount); 
    	
    }
}
