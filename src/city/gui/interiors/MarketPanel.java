package city.gui.interiors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import city.bases.interfaces.AnimationInterface;
import city.gui.BuildingCard;

public class MarketPanel extends BuildingCard implements ActionListener {

	private static final long serialVersionUID = 1355285244678935863L;
//	Data
//	=====================================================================
//	Fixed Numbers
//	--------------------------------------------------------------------
	public static final int RECTDIM = 20;
    public static final int ENTRANCEX = 40, ENTRANCEY = 470;
    public static final int EXITX = 420, EXITY = 520;
    
//	Phone
//	--------------------------------------------------------------------
    public static final int PHONEX = 40, PHONEY = 0;
    public static final int PHONEW = 20, PHONEH = 10;
    
//	Shelf
//	--------------------------------------------------------------------
    public static final int SHELFX = 35, SHELFY = 50;
    public static final int SHELFW = 30, SHELFH = 150;
    public static final int SHELFGAP = 70;
    
//	Counter
//	--------------------------------------------------------------------
    public static final int COUNTERX = 0, COUNTERY = 275;
    public static final int COUNTERW = 500, COUNTERH = 25;
    public static final int COUNTERINTERACTIONY = COUNTERY+COUNTERH;
    
//	Cashier
//	--------------------------------------------------------------------
    public static final int CASHIERX = 440, CASHIERY = 275-RECTDIM;    
    public static final int CASHIEREMPINTERACTIONX = CASHIERX-RECTDIM, CASHIEREMPINTERACTIONY = CASHIERY;
    public static final int CASHIERCUSTINTERACTIONY = COUNTERY+COUNTERH;
    
//	Waiting Areas
//	--------------------------------------------------------------------
    public static final int SERVICEWAITINGX = 100, SERVICEWAITINGY = 420;
    public static final int ITEMSWAITINGX = 400, ITEMSWAITINGY = 320;

	private final int delayMS = 5;
	
//	Constructor
//	=====================================================================
	public MarketPanel(Color color) {
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
		// Clear the screen by painting a rectangle the size of the frame
		
		g2.setColor(background);
		g2.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
		
		g2.setColor(Color.YELLOW);
		g2.fillRect(PHONEX, PHONEY, PHONEW, PHONEH);

		g2.setColor(Color.DARK_GRAY);
		for (int i = 0; i < 5; i++) {
			g2.fillRect(SHELFX+((SHELFW+SHELFGAP)*i), SHELFY, SHELFW, SHELFH);
		}
		
		g2.setColor(Color.YELLOW);
		g2.fillRect(COUNTERX, COUNTERY, COUNTERW, COUNTERH);
		
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
}