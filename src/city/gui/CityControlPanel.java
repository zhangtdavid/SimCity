package city.gui;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class CityControlPanel extends JPanel {

	private static final long serialVersionUID = 9166425422374406573L;

	private MainFrame mainframe;

	public CityControlPanel(MainFrame mf) {
		this.mainframe = mf;
		this.setPreferredSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMaximumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMinimumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setVisible(true);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		CityControlPanelTraceTab traceTab = new CityControlPanelTraceTab(mf);
		tabbedPane.addTab("Trace", null, traceTab);
		
		CityControlPanelScenariosTab scenariosTab = new CityControlPanelScenariosTab(mf);
		tabbedPane.addTab("Scenarios", null, scenariosTab);
		
		CityControlPanelAddBuildingTab addBuildingsTab = new CityControlPanelAddBuildingTab(mf);
		tabbedPane.addTab("Add Buildings", null, addBuildingsTab);
		
		CityControlPanelEditBuildingTab editBuildingsTab = new CityControlPanelEditBuildingTab(mf);
		tabbedPane.addTab("Edit Buildings", null, editBuildingsTab);
		
		CityControlPanelAddPersonTab addPersonTab = new CityControlPanelAddPersonTab(mf);
		tabbedPane.addTab("Add Person", null, addPersonTab);
		
		CityControlPanelEditPersonTab editPersonTab = new CityControlPanelEditPersonTab(mf);
		tabbedPane.addTab("Edit Person", null, editPersonTab);
		
		add(tabbedPane);
	}

	public MainFrame getMainframe() {
		return mainframe;
	}
}
