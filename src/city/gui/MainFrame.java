package city.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import trace.TracePanel;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -1848134216103293730L;

	private static int WINDOWX = 1200;
	private static int WINDOWY = 700;
	private static int CITYVIEWX = 500;
	private static int CITYVIEWY = 500;
	private static int BUILDINGVIEWX = 500;
	private static int BUILDINGVIEWY = 500;
	private static int CONTROLPANELX = 200;
	private static int CONTROLPANELY = 700;
	private static int TRACEPANELX = 1200;
	private static int TRACEPANELY = 200;

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
		// Set up the city panel
		cityView = new CityViewPanel(this);
		cityView.setPreferredSize(new Dimension(CITYVIEWX, CITYVIEWY));
		cityView.setMaximumSize(new Dimension(CITYVIEWX, CITYVIEWY));
		cityView.setMinimumSize(new Dimension(CITYVIEWX, CITYVIEWY));
		// Set up the building panel
		buildingView = new BuildingView(this);
		buildingView.setPreferredSize(new Dimension(BUILDINGVIEWX, BUILDINGVIEWY));
		buildingView.setMaximumSize(new Dimension(BUILDINGVIEWX, BUILDINGVIEWY));
		buildingView.setMinimumSize(new Dimension(BUILDINGVIEWX, BUILDINGVIEWY));
		// Set up the city control panel
		CP = new CityControlPanel(this);
		CP.setPreferredSize(new Dimension(CONTROLPANELX, CONTROLPANELY));
		CP.setMaximumSize(new Dimension(CONTROLPANELX, CONTROLPANELY));
		CP.setMinimumSize(new Dimension(CONTROLPANELX, CONTROLPANELY));
		// Set up the trace panel
		tracePanel = new TracePanel();
		tracePanel.showAlertsForAllLevels();
		tracePanel.showAlertsForAllTags();
		tracePanel.setDimension(new Dimension(TRACEPANELX, TRACEPANELY));

		// Layout for gridbaglayout
		this.setLayout(new GridBagLayout());

		c.gridx = 0; c.gridy = 0;
		c.gridwidth = CITYVIEWX/100; c.gridheight = CITYVIEWY/100;
		this.add(cityView, c);

		c.gridx = 5; c.gridy = 0;
		c.gridwidth = BUILDINGVIEWX/100; c.gridheight = BUILDINGVIEWY/100;
		this.add(buildingView, c);

		c.gridx = 10; c.gridy = 0;
		c.gridwidth = CONTROLPANELX/100; c.gridheight = CONTROLPANELY/100;
		this.add(CP, c);
		//		
		c.gridx = 0; c.gridy = 5;
		c.gridwidth = TRACEPANELX/100; c.gridheight = TRACEPANELY/100;
		c.fill = GridBagConstraints.BOTH;
		this.add(tracePanel, c);

		// Set up the window
		this.setTitle("SimCity201");
		this.setVisible(true);
		this.setResizable(false);
		this.setBounds(50, 50, WINDOWX, WINDOWY);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
