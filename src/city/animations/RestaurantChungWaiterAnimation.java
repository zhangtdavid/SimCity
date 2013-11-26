package city.animations;


import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

import utilities.StringUtil;
import city.Animation;
import city.animations.interfaces.RestaurantChungAnimatedCustomer;
import city.animations.interfaces.RestaurantChungAnimatedWaiter;
import city.interfaces.RestaurantChungCustomer;
import city.interfaces.RestaurantChungWaiter;

public class RestaurantChungWaiterAnimation extends Animation implements RestaurantChungAnimatedWaiter {
    private RestaurantChungWaiter agent = null;
	
	public enum WaiterState
	{Working, AskedForBreak, OnBreak};
	private WaiterState state = WaiterState.Working;
	
	private boolean delivering = false;
    private String food = null;

//	Fixed Numbers
//	=====================================================================
	private static final int HRECTDIM = 20;
    private static final int xEntrance = -20, yEntrance = -20;
    private static final int xWaiterHome = 410, yWaiterHome = 40;
    private static final int xCustomerLine = 70, yCustomerLine = 80;
    private static final int xCook = 500-70, yCook = 60;
    private static final int xCashier = 50, yCashier = 40+20;  
    private static final int xBreak = 520, yBreak = 150; 
    
//	Location Information
//	=====================================================================
    private int xPos = -20, yPos = -20; //default waiter position
    private int xDestination = -20, yDestination = -20; //default start position
	private enum Command {noCommand, GoToWaiterHome, GoToLine, GoToEntrance, GoToTable, GoToCook, GoToCashier, GoOffBreak};
	private Command command=Command.noCommand;
    
//	Tables
//	=====================================================================   
    private Vector<TableXY> tableXYs = new Vector<TableXY>();
	private class TableXY {
		int tableX;
		int tableY;
		
		public TableXY(int x, int y) {
			tableX = x;
			tableY = y;
		}
	}
    
    private int tableNumX = 0;
    private int tableNumY = 0;

    public RestaurantChungWaiterAnimation(RestaurantChungWaiter wa) {
        agent = wa;
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
        
        if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToEntrance) agent.msgAnimationAtEntrance();
			else if (command == Command.GoToWaiterHome) agent.msgAnimationAtWaiterHome();
			else if (command == Command.GoToLine) agent.msgAnimationAtLine();
			else if (command == Command.GoToTable) {
				agent.msgAnimationAtTable();
		    	food = null;
		    	delivering = false;
		    }
			else if (command == Command.GoToCook) agent.msgAnimationAtCook();
			else if (command == Command.GoToCashier) agent.msgAnimationAtCashier();
			else if (command == Command.GoOffBreak) {
				state = WaiterState.Working;
//				gui.setWaiterWorking(agent);
			}
			command=Command.noCommand;
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, HRECTDIM, HRECTDIM);
		g.setColor(Color.BLACK);
		if (delivering) {
			g.drawString(food, xPos+15, yPos+10);
		}
    }

    public boolean isPresent() {
        return true;
    }

    public void DoReturnToWaiterHome() {
        xDestination = xWaiterHome;
        yDestination = yWaiterHome;
		command = Command.GoToWaiterHome;
    }
    
	public void DoGoToCustomerLine() {
        xDestination = xCustomerLine;
        yDestination = yCustomerLine;
		command = Command.GoToLine;
	}
    
	public void DoGoToTable(int table) {
    	tableNumX = findTableX(table);
    	tableNumY = findTableY(table);
        xDestination = tableNumX + 20;
        yDestination = tableNumY - 20;
		command = Command.GoToTable;
	}
    
    public void DoBringToTable(RestaurantChungCustomer customer, int table) {
    	DoGoToTable(table);
    	System.out.println("Waiter Gui bringing " + customer + " to table " + (table+1));
    	customer.getGui().DoGoToSeat(findTableX(table), findTableY(table));
    }

    public void DoDeliverFood(int table, String choice) {
    	System.out.println("Waiter Gui bringing food to table " + (table+1));
    	delivering = true;
    	food = choice;
    	DoGoToTable(table);
    }
    
    public void DoGoToCook() {
        xDestination = xCook;
        yDestination = yCook;
		command = Command.GoToCook;
    }
    
    public void DoReturnToEntrance() {
        xDestination = xEntrance;
        yDestination = yEntrance;
		command = Command.GoToEntrance;
    }
    
	public void DoGoToCashier() {
        xDestination = xCashier;
        yDestination = yCashier;
		command = Command.GoToCashier;		
	}
    
    public void DoGoOnBreak() {
    	setOnBreak();
        xDestination = xBreak;
        yDestination = yBreak;
    }
    
    public void DoGoOffBreak() {
    	DoReturnToEntrance();
		command = Command.GoOffBreak;
    }
	
//  Utilities
//	=====================================================================
    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void addTable(int x, int y) {
		tableXYs.add(new TableXY(x, y));
	}
	public int findTableX(int table) {
		return tableXYs.get(table).tableX;
	}
	
	public int findTableY(int table) {
		return tableXYs.get(table).tableY;
	}
    
    protected void print(String msg, Throwable e) {
        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append(": ");
        sb.append(msg);
        sb.append("\n");
        if (e != null) {
            sb.append(StringUtil.stackTraceString(e));
        }
        System.out.print(sb.toString());
    }
    
    protected String getName() {
        return StringUtil.shortName(this);
    }

	public void setAskedForBreak() {
		state = WaiterState.AskedForBreak;
		agent.msgAnimationAskedForBreak();
//		gui.setWaiterWaiting(agent);
	}
    
	public void setOnBreak() {
		state = WaiterState.OnBreak;
//		gui.setWaiterOnBreak(agent);
	}
	
	public void setOffBreak() {
		state = WaiterState.Working;
		DoGoOffBreak();
		agent.msgAnimationBreakOver();
//		gui.setWaiterWorking(agent);
	}
	
	public String isOnBreak() {
		return state.toString();
	}    
}
