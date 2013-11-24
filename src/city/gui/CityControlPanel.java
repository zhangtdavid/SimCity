package city.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import trace.AlertLevel;
import trace.AlertLog;
import trace.AlertTag;

public class CityControlPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 9166425422374406573L;

	MainFrame mainframe;
	public static final int CP_WIDTH = 200, CP_HEIGHT = 700;
	JButton addRestaurant, addBank;

	//For managing traces
	JRadioButton toggleInfo;
	JRadioButton toggleRestaurantTag;		
	JRadioButton toggleBankTag;

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
		toggleInfo = new JRadioButton("Show Level: INFO", true);
		toggleInfo.addActionListener(this);
		toggleRestaurantTag = new JRadioButton("Show Tag: RESTAURANT", true);
		toggleRestaurantTag.addActionListener(this);
		toggleBankTag = new JRadioButton("Show Tag: BANK", true);
		toggleBankTag.addActionListener(this);

		c.gridx = 0; c.gridy = 2;
		this.add(toggleInfo, c);
		c.gridx = 0; c.gridy = 3;
		this.add(toggleRestaurantTag, c);
		c.gridx = 0; c.gridy = 4;
		this.add(toggleBankTag, c);
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
		else if(e.getSource().equals(toggleInfo)) {
			if(toggleInfo.isSelected())
				mainframe.tracePanel.showAlertsWithLevel(AlertLevel.INFO);
			else
				mainframe.tracePanel.hideAlertsWithLevel(AlertLevel.INFO);
		}
		else if(e.getSource().equals(toggleRestaurantTag)) {
			if(toggleRestaurantTag.isSelected())
				mainframe.tracePanel.showAlertsWithTag(AlertTag.RESTAURANT);
			else
				mainframe.tracePanel.hideAlertsWithTag(AlertTag.RESTAURANT);
		}
		else if(e.getSource().equals(toggleBankTag)) {
			if(toggleBankTag.isSelected())
				mainframe.tracePanel.showAlertsWithTag(AlertTag.BANK);
			else
				mainframe.tracePanel.hideAlertsWithTag(AlertTag.BANK);
		}
	}
}
