package city.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

import trace.TracePanel;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -1848134216103293730L;

	public final static int WINDOWX = 1300;
	public final static int WINDOWY = 700;
	public final static int CITYVIEWX = 500;
	public final static int CITYVIEWY = 500;
	public final static int BUILDINGVIEWX = 500;
	public final static int BUILDINGVIEWY = 500;
	public final static int CONTROLPANELX = 300;
	public final static int CONTROLPANELY = 700;
	public final static int TRACEPANELX = 500;
	public final static int TRACEPANELY = 190;

	public CityViewPanel cityView;
	public BuildingView buildingView;
	public TracePanel tracePanel;
	public TracePanel personTracePanel;
	public CityControlPanel CP;
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
		// Set up the trace panel
		tracePanel = new TracePanel();
		tracePanel.showAlertsForAllLevels();
		tracePanel.showAlertsForAllTags();
		tracePanel.setDimension(new Dimension(TRACEPANELX, TRACEPANELY));
		// Set up the person trace panel
		personTracePanel = new TracePanel();
		personTracePanel.showAlertsForAllLevels();
		personTracePanel.hideAllAlerts();
		personTracePanel.setDimension(new Dimension(TRACEPANELX, TRACEPANELY));
		// Set up the city control panel
		CP = new CityControlPanel(this);
		CP.setPreferredSize(new Dimension(CONTROLPANELX, CONTROLPANELY));
		CP.setMaximumSize(new Dimension(CONTROLPANELX, CONTROLPANELY));
		CP.setMinimumSize(new Dimension(CONTROLPANELX, CONTROLPANELY));

		// Layout for gridbaglayout
		this.setLayout(new GridBagLayout());

		c.gridx = 0; c.gridy = 0;
		c.gridwidth = CITYVIEWX/100; c.gridheight = CITYVIEWY/100;
		this.add(cityView, c);
		this.add(cityView, c);

		c.gridx = 5; c.gridy = 0;
		c.gridwidth = BUILDINGVIEWX/100; c.gridheight = BUILDINGVIEWY/100;
		this.add(buildingView, c);

		c.gridx = 10; c.gridy = 0;
		c.gridwidth = CONTROLPANELX/100; c.gridheight = CONTROLPANELY/100;
		this.add(CP, c);

		c.gridx = 0; c.gridy = 5;
		c.gridwidth = TRACEPANELX/100; c.gridheight = TRACEPANELY/100;
		c.fill = GridBagConstraints.BOTH;
		this.add(tracePanel, c);
		
		c.gridx = 5; c.gridy = 5;
		c.gridwidth = TRACEPANELX/100; c.gridheight = TRACEPANELY/100;
		c.fill = GridBagConstraints.BOTH;
		this.add(personTracePanel, c);

		// Set up the window
		this.setTitle("SimCity201");
		this.setVisible(true);
		this.setResizable(false);
		this.setBounds(50, 50, WINDOWX, WINDOWY);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void setDisplayDate(String s) {
		this.setTitle("SimCity201 - " + s);
	}

}
