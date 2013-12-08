package city.gui;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import city.Application;
import city.gui.tabs.AddBuildingTab;
import city.gui.tabs.AddPersonTab;
import city.gui.tabs.EditBuildingTab;
import city.gui.tabs.EditPersonTab;
import city.gui.tabs.ScenariosTab;
import city.gui.tabs.TraceTab;

public class CityControlPanel extends JPanel {

	private static final long serialVersionUID = 9166425422374406573L;

	private MainFrame mainframe;
	public TraceTab traceTab;
	public ScenariosTab scenariosTab;
	public AddBuildingTab addBuildingsTab;
	public EditBuildingTab editBuildingsTab;
	public AddPersonTab addPersonTab;
	public EditPersonTab editPersonTab;
	private JTabbedPane tabbedPane;

	public CityControlPanel(MainFrame mf) {
		this.mainframe = mf;
		this.setPreferredSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMaximumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMinimumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setVisible(true);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		tabbedPane.setFocusable(false);
		
		traceTab = new TraceTab(mf);
		tabbedPane.addTab("Trace", null, traceTab);
		
		scenariosTab = new ScenariosTab(mf);
		tabbedPane.addTab("Scenarios", null, scenariosTab);
		
		addBuildingsTab = new AddBuildingTab(mf);
		tabbedPane.addTab("Add Buildings", null, addBuildingsTab);
		
		editBuildingsTab = new EditBuildingTab(mf);
		tabbedPane.addTab("Edit Buildings", null, editBuildingsTab);
		
		addPersonTab = new AddPersonTab(mf);
		addPersonTab.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent arg0) {
				Application.getMainFrame().CP.addPersonTab.prepareFormForUse();
			}
		});
		tabbedPane.addTab("Add Person", null, addPersonTab);
		
		editPersonTab = new EditPersonTab(mf);
		tabbedPane.addTab("Edit Person", null, editPersonTab);
		
		add(tabbedPane);
	}

	public MainFrame getMainframe() {
		return mainframe;
	}
	
	public void displayTab(JPanel tab) {
		tabbedPane.setSelectedComponent(tab);
	}
}
