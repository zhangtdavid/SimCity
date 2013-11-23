package city.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import trace.TracePanel;

public class MainFrame extends JFrame {
	
	private static final long serialVersionUID = -1848134216103293730L;
	
    private static int WINDOWX = 1100;
    private static int WINDOWY = 700;
    
    CityViewPanel cityView;
	BuildingView buildingView;
	TracePanel tracePanel;
	CityControlPanel CP;
	GridBagConstraints c = new GridBagConstraints();
	
    /**
     * Constructor method
     * 
     * The MainFrame object is the main window of the program GUI.
     */
    public MainFrame() {
    	// Set up the city control panel
    	CP = new CityControlPanel(this);
		// Set up the trace panel
		tracePanel = new TracePanel();
		tracePanel.setPreferredSize(new Dimension(1100, 100));//new Dimension(CP.getPreferredSize().width, (int)(1.4*CP.getPreferredSize().height)));
		tracePanel.showAlertsForAllLevels();
		tracePanel.showAlertsForAllTags();
    	// Set up the city panel
		cityView = new CityViewPanel(this);
		// Set up the building panel
		buildingView = new BuildingView(this);
		// Set up the info panel
//		info = new InfoPanel(this);
		
		// Layout for gridbaglayout
		this.setLayout(new GridBagLayout());
		
		c.gridx = 0; c.gridy = 0;
		c.gridwidth = 5; c.gridheight = 5;
		this.add(cityView, c);
		
//		c.gridx = 6; c.gridy = 0;
//		c.gridwidth = 5; c.gridheight = 1;
//		this.add(info, c);

		c.gridx = 5; c.gridy = 0;
		c.gridwidth = 5; c.gridheight = 5;
		this.add(buildingView, c);

		c.gridx = 0; c.gridy = 6;
		c.gridwidth = 5; c.gridheight = 2;
		this.add(CP, c);
//		
		c.gridx = 5; c.gridy = 6;
		c.gridwidth = 5; c.gridheight = 2;
//		c.fill = GridBagConstraints.BOTH;
		this.add(tracePanel, c);
		
    	// Set up the window
        this.setTitle("SimCity201");
        this.setVisible(true);
        this.setResizable(false);
        this.setBounds(50, 50, WINDOWX, WINDOWY);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout((Container) getContentPane(), BoxLayout.X_AXIS));
    }
	
}
