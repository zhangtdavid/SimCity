package city.gui;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = -1848134216103293730L;
	
    private static int WINDOWX = 1100;
    private static int WINDOWY = 700;
    
    public RestaurantZhangPanel restaurantZhangPanel;
	
    /**
     * Constructor method
     * 
     * The MainFrame object is the main window of the program GUI.
     */
    public MainFrame() {
    	// Set up the window
        this.setTitle("SimCity201");
        this.setVisible(true);
        this.setResizable(false);
        this.setBounds(50, 50, WINDOWX, WINDOWY);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout((Container) getContentPane(), BoxLayout.X_AXIS));
        
        // TODO testing RestaurantZhangPanel
        Dimension panelDimension = new Dimension(WINDOWX, WINDOWY);
        restaurantZhangPanel = new RestaurantZhangPanel(panelDimension);
        restaurantZhangPanel.setPreferredSize(panelDimension);
        restaurantZhangPanel.setMinimumSize(panelDimension);
        restaurantZhangPanel.setMaximumSize(panelDimension);
        this.add(restaurantZhangPanel);
    }
	
}
