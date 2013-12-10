package city.gui.interiors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import city.bases.interfaces.AnimationInterface;
import city.gui.BuildingCard;

public class RestaurantChungPanel extends BuildingCard implements ActionListener {

	private static final long serialVersionUID = 1355285244678935863L;
//	Data
//	=====================================================================	
//	Fixed Numbers
//	--------------------------------------------------------------------
	public static final int RECTDIM = 20;
    public static final int ENTRANCEX = 40, ENTRANCEY = 480;
    public static final int EXITX = 40, EXITY = -RECTDIM;

//	Tables
//	--------------------------------------------------------------------
    public static final int TABLEX = 125, TABLEY = 125;
    public static final int TABLEGAP = 100;
    public static final int TABLEDIM = 50;

//	Kitchen
//	--------------------------------------------------------------------    
    private static final int KITCHEN1X = 300, KITCHEN1Y = 0;
    private static final int KITCHEN1W = 30, KITCHEN1L = 60;
    private static final int KITCHEN2Y = KITCHEN1Y+KITCHEN1L;
    private static final int KITCHEN2W = 200, KITCHEN2L = 30;
    public static final int GRILLX = KITCHEN1X+120, GRILLY = KITCHEN2Y;
    private static final int GRILLW = 60, GRILLL = 15;    
    public static final int COOKGRILLX = KITCHEN1X+120, COOKGRILLY = KITCHEN2Y-RECTDIM;
    public static final int PLATINGX = KITCHEN1X+30, PLATINGY = KITCHEN1L-RECTDIM;
    public static final int STANDX = KITCHEN1X+7, STANDY = KITCHEN1Y+RECTDIM+2;
    public static final int STANDW = 17, STANDL = 15;

//	Cook
//	--------------------------------------------------------------------
    public static final int COOKHOMEX = KITCHEN1X+50, COOKHOMEY = KITCHEN1Y+RECTDIM;	

//	Cashier
//	--------------------------------------------------------------------
    public static final int CASHIERX = 140, CASHIERY = 5;
    public static final int CASHIERDESKX= 140, CASHIERDESKY = RECTDIM+10;  
    private static final int CASHIERDESKW = RECTDIM, CASHIERDESKL = 10;
    public static final int CASHIERINTERACTIONX = 140, CASHIERINTERACTIONY = CASHIERDESKY+CASHIERDESKL;
   
//	Customer
//	--------------------------------------------------------------------
    public static final int WAITINGAREAX = 50, WAITINGAREAY = 100;
    
//	Waiter
//	--------------------------------------------------------------------
    public static final int WAITERHOMEX = 440, WAITERHOMEY = 140;
    public static final int CUSTOMERLINEX = 40, CUSTOMERLINEY = 100;
    public static final int ORDERDROPX = 280, ORDERDROPY = 20;
    public static final int WAITERBREAKX = 260, WAITERBREAKY = 40; 
    
	private final int delayMS = 5;
	private List<AnimationInterface> animations = new ArrayList<AnimationInterface>();
	
	
	public RestaurantChungPanel(Color color) {
		super(color);

		setVisible(true);

		Timer timer = new Timer(delayMS, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}
	
	@Override
	public void paint(Graphics graphics) {
		Graphics2D g2 = (Graphics2D)graphics;
		g2.setColor(background);
		g2.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);

        // kitchen area
        g2.setColor(Color.YELLOW);
        g2.fillRect(KITCHEN1X, KITCHEN1Y, KITCHEN1W, KITCHEN1L);
        g2.fillRect(KITCHEN1X, KITCHEN2Y, KITCHEN2W, KITCHEN2L);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(GRILLX, GRILLY, GRILLW, GRILLL);
        
        g2.setColor(Color.GRAY);
        g2.fillRect(STANDX, STANDY, STANDW, STANDL);
        
        g2.setColor(Color.YELLOW);
        g2.fillRect(CASHIERDESKX, CASHIERDESKY, CASHIERDESKW, CASHIERDESKL);
        
        for (int i = 0; i < 9; i++) {
            g2.setColor(Color.YELLOW);
            g2.fillRect(TABLEX+((i%3)*TABLEGAP), TABLEY+((i/3)*TABLEGAP), TABLEDIM, TABLEDIM);        	
        }
		
		// Update the position of each visible element
		for(AnimationInterface animation : animations) {
//			if (animation.getVisible()) {
				animation.updatePosition();
//			}
		}

		// Draw each visible element after updating their positions
		// TODO generates concurrent modification exception
		for(AnimationInterface animation : animations) {
//			if (animation.getVisible()) {
				animation.draw(g2);
//			}
		}
	}

	public void addVisualizationElement(AnimationInterface ve) {
		animations.add(ve);
	}
}