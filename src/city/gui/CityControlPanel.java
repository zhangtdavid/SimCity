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
	JButton addRestaurantZhang, addRestaurantChoi, addRestaurantJP, addRestaurantTimms, addRestaurantChung, addBank;

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

		int gridBagYPos = 0;

		addRestaurantZhang = new JButton("Add RestaurantZhang");
		addRestaurantZhang.addActionListener(this);
		c.gridx = 0; c.gridy = gridBagYPos++;
		add(addRestaurantZhang, c);

		addRestaurantChoi = new JButton("Add RestaurantChoi");
		addRestaurantChoi.addActionListener(this);
		c.gridx = 0; c.gridy = gridBagYPos++;
		add(addRestaurantChoi, c);

		addRestaurantJP = new JButton("Add RestaurantJP");
		addRestaurantJP .addActionListener(this);
		c.gridx = 0; c.gridy = gridBagYPos++;
		add(addRestaurantJP, c);

		addRestaurantTimms = new JButton("Add RestaurantTimms");
		addRestaurantTimms.addActionListener(this);
		c.gridx = 0; c.gridy = gridBagYPos++;
		add(addRestaurantTimms, c);
		
		addRestaurantChung = new JButton("Add RestaurantChung");
		addRestaurantChung.addActionListener(this);
		c.gridx = 0; c.gridy = gridBagYPos++;
		add(addRestaurantChung, c);

		addBank = new JButton("Add Bank");
		addBank.addActionListener(this);
		c.gridx = 0; c.gridy = gridBagYPos++;
		add(addBank, c);

		//Trace panel buttons
		toggleInfo = new JRadioButton("Show Level: INFO", true);
		toggleInfo.addActionListener(this);
		toggleRestaurantTag = new JRadioButton("Show Tag: RESTAURANT", true);
		toggleRestaurantTag.addActionListener(this);
		toggleBankTag = new JRadioButton("Show Tag: BANK", true);
		toggleBankTag.addActionListener(this);

		c.gridx = 0; c.gridy = gridBagYPos++;
		this.add(toggleInfo, c);
		c.gridx = 0; c.gridy = gridBagYPos++;
		this.add(toggleRestaurantTag, c);
		c.gridx = 0; c.gridy = gridBagYPos++;
		this.add(toggleBankTag, c);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addRestaurantZhang)) {
			mainframe.cityView.addObject(CityViewBuilding.BuildingType.RESTAURANTZHANG);
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT, this.name, "Adding New RestaurantZhang");
		} else if (e.getSource().equals(addRestaurantChoi)) {
			mainframe.cityView.addObject(CityViewBuilding.BuildingType.RESTAURANTCHOI);
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT, this.name, "Adding New RestaurantChoi");
		} else if (e.getSource().equals(addRestaurantJP)) {
			mainframe.cityView.addObject(CityViewBuilding.BuildingType.RESTAURANTJP);
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT, this.name, "Adding New RestaurantJP");
		} else if (e.getSource().equals(addRestaurantTimms)) {
			mainframe.cityView.addObject(CityViewBuilding.BuildingType.RESTAURANTTIMMS);
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT, this.name, "Adding New RestaurantTimms");
		} else if (e.getSource().equals(addRestaurantChung)) {
			mainframe.cityView.addObject(CityViewBuilding.BuildingType.RESTAURANTCHUNG);
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANT, this.name, "Adding New RestaurantChung");
		} else if (e.getSource().equals(addBank)) {
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
