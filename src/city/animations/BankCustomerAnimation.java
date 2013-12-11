package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import city.bases.Animation;
import city.roles.interfaces.BankCustomer;

public class BankCustomerAnimation extends Animation{

    private BankCustomer agent = null;
    
    private boolean moving = false;
    private int xPos, yPos;//default waiter position
    private int xDestination, yDestination;//default start position

    public static final int checkIn = -10;
    public static final int customerSize = 20;
    public static final int tellerX = 290;
    public static final int lineX = 100;
    public static final int lineBeginY = 100;
   
    private boolean talking = false;
    private String dialogue = new String();
    
    public Map<Integer, Integer> TellerLocations = new HashMap<Integer, Integer>();
    

    public BankCustomerAnimation(BankCustomer bc) {
        agent = bc;
		xPos = -20;
		yPos = 520;
		for(int i = 0; i<3; i++){
			TellerLocations.put(i, 160+i*100);
		}
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

        if (xPos == xDestination && yPos == yDestination && moving) {		//took out xPos!= 100
           agent.msgAtDestination();
           talking = true;
           moving = false;
        }
    }

    public void draw(Graphics2D g) {
    	
        g.setColor(Color.BLUE);
        g.fillRect(xPos, yPos, customerSize, customerSize);
        if(talking){
        	g.setColor(Color.BLACK);
        	g.drawString(dialogue, xPos, yPos);
        }
    }

    public boolean isPresent() {
        return true;						//USed to just return true
    }

    public void DoGetInLine(int place) {
    	talking = true;
    	xDestination = lineX;
    	yDestination = lineBeginY + 50*place;
    	moving = true;
    }
    
    public void MoveUpInLine(){
    	yDestination = yPos - 50;
    	moving = true;
    }
    
    public void DoGoToTeller(int booth, String s){
    	xDestination = tellerX;
    	yDestination = TellerLocations.get(booth);
    	dialogue = s;
    	moving = true;
    }
    
    public void DoExitBank(){
    	xDestination = -20;
    	yDestination = -20;
    	dialogue = "Thank you. Done and Leaving";
    	talking = true;
    	moving = true;
    }
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    
}
