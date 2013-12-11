package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import city.bases.Animation;
import city.roles.BankTellerRole;

public class BankTellerAnimation extends Animation{

    private BankTellerRole agent = null;
    private boolean onBreak = false;
    private boolean moving = false;
    private boolean talking = false;
    private String dialogue;
    private int xHome = 330;
    private int yHome;
    private int xVault = 420;
    private int yVault = 240;
    
    private static final int tellerSize = 20;
    private int xPos = 0, yPos = 0;//default waiter position
    private int xDestination, yDestination;//default start position
    
    public Map <Integer, Integer> TellerLocations = new HashMap<Integer, Integer>(); 

    public BankTellerAnimation(BankTellerRole r, int place) {
    	agent = r;	//msgAvailable
		xPos = -20;
		yPos = -20;
		moving = false;
		for(int i = 0; i<3; i++){
			TellerLocations.put(i, 140+i*100);
		}
		yHome = TellerLocations.get(place);
		agent.setBoothNumber(place);
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

        if (xPos == xDestination && yPos == yDestination && moving) {
           agent.msgAtDestination();
           moving = false;
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, tellerSize, tellerSize);
        if(talking){
        	g.setColor(Color.BLACK);
        	g.drawString(dialogue, xPos, yPos);
        }
    }

    public boolean isPresent() {
        return true;						//USed to just return true
    }

   
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
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

	public void DoClearString() {
		// TODO Auto-generated method stub
		talking = false;
	}

	public void DoString(String string) {
		// TODO Auto-generated method stub
		dialogue = string;
		talking = true;
	}

	public void DoGoToVault() {
		// TODO Auto-generated method stub
		xDestination = xVault;
		yDestination = yVault;
		moving = true;
	}

	public void DoGoToStation() {
		// TODO Auto-generated method stub
		xDestination = xHome;
		yDestination = yHome;
		moving = true;
	}

	public void DoLeave() {
		// TODO Auto-generated method stub
		xDestination = 400;
		yDestination = 0;
		moving = true;
	}
}
