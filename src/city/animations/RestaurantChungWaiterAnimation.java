package city.animations;


import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.Vector;

import city.animations.interfaces.RestaurantChungAnimatedWaiter;
import city.bases.Animation;
import city.gui.interiors.RestaurantChungPanel;
import city.roles.interfaces.RestaurantChungCustomer;
import city.roles.interfaces.RestaurantChungWaiter;

public class RestaurantChungWaiterAnimation extends Animation implements RestaurantChungAnimatedWaiter {
    private RestaurantChungWaiter agent = null;
	
	public enum WaiterState
	{Working, AskedForBreak, OnBreak};
	private WaiterState state = WaiterState.Working;
	
	private static LinkedList<RestaurantChungWaiter> waiterPositions = new LinkedList<RestaurantChungWaiter>();
	
	private boolean delivering = false;
    private String food = null;

//	Location Information
//	=====================================================================
    private int xPos = RestaurantChungPanel.WAITERHOMEX, yPos = RestaurantChungPanel.WAITERHOMEY+(waiterPositions.indexOf(agent)*30); //default waiter position
    private int xDestination = xPos, yDestination = yPos; //default start position
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
        waiterPositions.add(agent);
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
        g.fillRect(xPos, yPos, RestaurantChungPanel.RECTDIM, RestaurantChungPanel.RECTDIM);
		g.setColor(Color.BLACK);
		if (delivering) {
			g.drawString(food, xPos+15, yPos+10);
		}
    }

    public boolean isPresent() {
        return true;
    }

	@Override
    public void DoReturnToWaiterHome() {
        xDestination = RestaurantChungPanel.WAITERHOMEX;
        yDestination = RestaurantChungPanel.WAITERHOMEY+(waiterPositions.indexOf(agent)*30);
		command = Command.GoToWaiterHome;
    }
    
	@Override
	public void DoGoToCustomerLine() {
        xDestination = RestaurantChungPanel.CUSTOMERLINEX;
        yDestination = RestaurantChungPanel.CUSTOMERLINEY;
		command = Command.GoToLine;
	}
    
	@Override
	public void DoGoToTable(int table) {
    	tableNumX = findTableX(table);
    	tableNumY = findTableY(table);
        xDestination = tableNumX + 20;
        yDestination = tableNumY - 20;
		command = Command.GoToTable;
	}
    
	@Override
    public void DoBringToTable(RestaurantChungCustomer customer, int table) {
    	DoGoToTable(table);
    	System.out.println("Waiter Gui bringing " + customer + " to table " + (table+1));
    	customer.getAnimation(RestaurantChungCustomerAnimation.class).DoGoToSeat(findTableX(table), findTableY(table));
    }

	@Override
    public void DoDeliverFood(int table, String choice) {
    	System.out.println("Waiter Gui bringing food to table " + (table+1));
    	delivering = true;
    	food = choice;
    	DoGoToTable(table);
    }
    
	@Override
    public void DoGoToCook() {
        xDestination = RestaurantChungPanel.ORDERDROPX;
        yDestination = RestaurantChungPanel.ORDERDROPY;
		command = Command.GoToCook;
    }
    
	@Override
    public void DoReturnToEntrance() {
        xDestination = RestaurantChungPanel.ENTRANCEX;
        yDestination = RestaurantChungPanel.ENTRANCEY;
		command = Command.GoToEntrance;
    }
    
	@Override
	public void DoGoToCashier() {
        xDestination = RestaurantChungPanel.CASHIERX;
        yDestination = RestaurantChungPanel.CASHIERY;
		command = Command.GoToCashier;		
	}
    
	@Override
    public void DoGoOnBreak() {
    	setOnBreak();
        xDestination = RestaurantChungPanel.WAITERBREAKX-(waiterPositions.indexOf(agent)*30);
        yDestination = RestaurantChungPanel.WAITERBREAKY;
    }
    
	@Override
    public void DoGoOffBreak() {
    	DoReturnToEntrance();
		command = Command.GoOffBreak;
    }
	
//  Getters
//	=====================================================================
	@Override
   public int getXPos() {
        return xPos;
    }

	@Override
    public int getYPos() {
        return yPos;
    }
    
	@Override
    public LinkedList<RestaurantChungWaiter> getWaiterPositions() {
		return waiterPositions;
	}
   
//  Utilities
//	=====================================================================
	@Override
    public void addTable(int x, int y) {
		tableXYs.add(new TableXY(x, y));
	}

	@Override
	public int findTableX(int table) {
		return tableXYs.get(table).tableX;
	}
	
	@Override
	public int findTableY(int table) {
		return tableXYs.get(table).tableY;
	}

	@Override
	public void setAskedForBreak() {
		state = WaiterState.AskedForBreak;
		agent.msgAnimationAskedForBreak();
//		gui.setWaiterWaiting(agent);
	}
    
	@Override
	public void setOnBreak() {
		state = WaiterState.OnBreak;
//		gui.setWaiterOnBreak(agent);
	}
	
	@Override
	public void setOffBreak() {
		state = WaiterState.Working;
		DoGoOffBreak();
		agent.msgAnimationBreakOver();
//		gui.setWaiterWorking(agent);
	}
	
	@Override
	public String isOnBreak() {
		return state.toString();
	} 
	
	@Override
	public void removeFromWaiterHomePositions() {
		waiterPositions.remove(agent);
	}
}
