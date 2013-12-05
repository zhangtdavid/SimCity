package city.gui;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class CityControlPanel extends JPanel {

	private static final long serialVersionUID = 9166425422374406573L;

	private MainFrame mainframe;
	public CityControlPanelTraceTab traceTab;
	public CityControlPanelScenariosTab scenariosTab;
	public CityControlPanelAddBuildingTab addBuildingsTab;
	public CityControlPanelEditBuildingTab editBuildingsTab;
	public CityControlPanelAddPersonTab addPersonTab;
	public CityControlPanelEditPersonTab editPersonTab;

	public CityControlPanel(MainFrame mf) {
		this.mainframe = mf;
		this.setPreferredSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMaximumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMinimumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setVisible(true);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		tabbedPane.setFocusable(false);
		
		traceTab = new CityControlPanelTraceTab(mf);
		tabbedPane.addTab("Trace", null, traceTab);
		
		scenariosTab = new CityControlPanelScenariosTab(mf);
		tabbedPane.addTab("Scenarios", null, scenariosTab);
		
		addBuildingsTab = new CityControlPanelAddBuildingTab(mf);
		tabbedPane.addTab("Add Buildings", null, addBuildingsTab);
		
		editBuildingsTab = new CityControlPanelEditBuildingTab(mf);
		tabbedPane.addTab("Edit Buildings", null, editBuildingsTab);
		
		addPersonTab = new CityControlPanelAddPersonTab(mf);
		tabbedPane.addTab("Add Person", null, addPersonTab);
		
		editPersonTab = new CityControlPanelEditPersonTab(mf);
		tabbedPane.addTab("Edit Person", null, editPersonTab);
		
		add(tabbedPane);
	}

	public MainFrame getMainframe() {
		return mainframe;
	}
}
