package city.gui.tabs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.RoundingMode;
import java.text.NumberFormat;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;

import utilities.DataModel;
import city.Application;
import city.bases.Building;
import city.bases.interfaces.BuildingInterface;
import city.bases.interfaces.RoleInterface;
import city.gui.MainFrame;

public class EditBuildingTab extends JPanel implements PropertyChangeListener, ActionListener{

	private static final long serialVersionUID = 9166425422374406573L;
	
	private MainFrame mainFrame;
	private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(getDefaultLocale());

	// TODO add controls like buttons, radio buttons, etc. here
	private DataModel dataModel;
	private DefaultListModel<Building> buildingModel;
	private Building buildingSelectedFromList;


	
	//constructor	
	
	public EditBuildingTab(MainFrame mf) {
		mainFrame = mf;
		this.setPreferredSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMaximumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMinimumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setVisible(true);
	    this.currencyFormat.setRoundingMode(RoundingMode.DOWN);
	    this.currencyFormat.setParseIntegerOnly(true);
	    this.mainFrame = mf;
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		// TODO Instantiate and add all buttons/control components here. Use any layout you want, I find gridbaglayout to be the best
		this.dataModel = Application.getModel();
		this.dataModel.getPropertyChangeSupport().addPropertyChangeListener(DataModel.BUILDINGS, this);
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

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
