package city.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import city.Animation;
import city.animations.RestaurantJPCookAnimation;
import city.animations.RestaurantJPCustomerAnimation;
import city.animations.RestaurantJPHostAnimation;
import city.animations.RestaurantJPWaiterAnimation;

public class RestaurantJPPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -287022985385911182L;
	
	private final int WINDOWX = 500;
    private final int WINDOWY = 500;
    private final int TABLEORIGINX = 200;
    private final int TABLEORIGINY = 250;
    private final int TABLELENGTH = 50;
    private final int TABLESEPARATION = 100;

    private List<Animation> animations = new ArrayList<Animation>();

    public RestaurantJPPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
 
    	Timer timer = new Timer(20, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here is the table
        for(int i=0; i<3; i++)			//HACK, need 3 to be a variable!
        {
        g2.setColor(Color.ORANGE);
        g2.fillRect(TABLEORIGINX, TABLEORIGINY - i*TABLESEPARATION, TABLELENGTH, TABLELENGTH);
        }
        for(Animation a : animations) {
            if (a.getVisible()) {
                a.updatePosition();
            }
        }

        for(Animation a : animations) {
            if (a.getVisible()) {
                a.draw(g2);
            }
        }
    }

    public void addGui(RestaurantJPCustomerAnimation a) {
        animations.add(a);
        a.isVisible = true;
    }

    public void addGui(RestaurantJPHostAnimation a) {
        animations.add(a);
        a.isVisible = true;
    }
    
    public void addGui(RestaurantJPWaiterAnimation a){
    	animations.add(a);
    	a.isVisible = true;
    }
    public void addGui(RestaurantJPCookAnimation a){
    	animations.add(a);
    	a.isVisible = true;
    }
}