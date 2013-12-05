package city.gui.tabs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import city.bases.interfaces.BuildingInterface;
import city.gui.MainFrame;

public class EditBuildingTab extends JPanel implements ActionListener{

	private static final long serialVersionUID = 9166425422374406573L;
	
	private MainFrame mainFrame;
	
	// TODO add controls like buttons, radio buttons, etc. here
	
	public EditBuildingTab(MainFrame mf) {
		mainFrame = mf;
		this.setPreferredSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMaximumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMinimumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setVisible(true);

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		// TODO Instantiate and add all buttons/control components here. Use any layout you want, I find gridbaglayout to be the best
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Create all actions for buttons/control components here
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}
	
	public void displayBuilding(BuildingInterface b) {
		// TODO
		return;
	}
}
