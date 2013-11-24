package city.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import trace.AlertLevel;
import trace.AlertLog;
import trace.AlertTag;

public class CityControlPanel extends JPanel implements ActionListener{

	MainFrame mainframe;
	public static final int CP_WIDTH = 200, CP_HEIGHT = 700;
	JButton addRestaurant, addBank;

	//For managing traces
	JButton enableInfoButton;		//You could (and probably should) substitute a JToggleButton to replace both
	JButton disableInfoButton;		//of these, but I split it into enable and disable for clarity in the demo.
	JButton enableRestaurantTagButton;		
	JButton disableRestaurantTagButton;		
	JButton enableBankTagButton;
	JButton disableBankTagButton;
	
	String name = "Control Panel";

	public CityControlPanel(MainFrame mf) {
		this.mainframe = mf;
		this.setPreferredSize(new Dimension(CP_WIDTH, CP_HEIGHT));
		this.setMaximumSize(new Dimension(CP_WIDTH, CP_HEIGHT));
		this.setMinimumSize(new Dimension(CP_WIDTH, CP_HEIGHT));
		this.setVisible(true);

		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		addRestaurant = new JButton("Add Restaurant");
		addRestaurant.addActionListener(this);
		c.gridx = 0; c.gridy = 0;
		add(addRestaurant, c);
		addBank = new JButton("Add Bank");
		addBank.addActionListener(this);
		c.gridx = 0; c.gridy = 1;
		add(addBank, c);

		//Trace panel buttons
		enableInfoButton = new JButton("Show Level: INFO");
		enableInfoButton.addActionListener(this);
		disableInfoButton = new JButton("Hide Level: INFO");
		disableInfoButton.addActionListener(this);
		enableRestaurantTagButton = new JButton("Show Tag: RESTAURANT");
		enableRestaurantTagButton.addActionListener(this);
		disableRestaurantTagButton = new JButton("Hide Tag: RESTAURANT");
		disableRestaurantTagButton.addActionListener(this);
		enableBankTagButton = new JButton("Show Tag: BANK");
		enableBankTagButton.addActionListener(this);
		disableBankTagButton = new JButton("Hide Tag: BANK");
		disableBankTagButton.addActionListener(this);
		
		c.gridx = 1; c.gridy = 0;
		this.add(enableInfoButton, c);
		c.gridx = 1; c.gridy = 1;
		this.add(disableInfoButton, c);
		c.gridx = 2; c.gridy = 0;
		this.add(enableRestaurantTagButton, c);
		c.gridx = 2; c.gridy = 1;
		this.add(disableRestaurantTagButton, c);
		c.gridx = 3; c.gridy = 0;
		this.add(enableBankTagButton, c);
		c.gridx = 3; c.gridy = 1;
		this.add(disableBankTagButton, c);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addRestaurant)) {
			mainframe.cityView.addObject(CityViewBuilding.BuildingType.RESTAURANT);
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT, this.name, "Adding New Restaurant");
		}
		else if (e.getSource().equals(addBank)) {
			AlertLog.getInstance().logInfo(AlertTag.BANK, this.name, "Adding New Bank");
			mainframe.cityView.addObject(CityViewBuilding.BuildingType.BANK);
		}
		else if(e.getSource().equals(enableInfoButton)) {
			mainframe.tracePanel.showAlertsWithLevel(AlertLevel.INFO);
		}
		else if(e.getSource().equals(disableInfoButton)) {
			mainframe.tracePanel.hideAlertsWithLevel(AlertLevel.INFO);
		}
		else if(e.getSource().equals(enableRestaurantTagButton)) {
			mainframe.tracePanel.showAlertsWithTag(AlertTag.RESTAURANT);
		}
		else if(e.getSource().equals(disableRestaurantTagButton)) {
			mainframe.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANT);
		}
		else if(e.getSource().equals(enableBankTagButton)) {
			mainframe.tracePanel.showAlertsWithTag(AlertTag.BANK);
		}
		else if(e.getSource().equals(disableBankTagButton)) {
			mainframe.tracePanel.hideAlertsWithTag(AlertTag.BANK);
		}
	}
}
