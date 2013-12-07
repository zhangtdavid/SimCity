package city.gui.tabs;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.text.Format;
import java.text.NumberFormat;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utilities.DataModel;
import city.bases.interfaces.BuildingInterface;
import city.gui.MainFrame;
import java.awt.Container;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.BorderLayout;

public class AddPersonTab extends JPanel implements ActionListener{

	private static final long serialVersionUID = 9166425422374406573L;
	
	//============================================================================//
	// Data       
    //============================================================================//
	
	private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(getDefaultLocale());
	
	private DataModel dataModel;
	private DefaultComboBoxModel<String> jobComboBoxModel;
	private DefaultComboBoxModel<BuildingInterface> workplaceComboBoxModel;
	private String jobSelectedFromComboBox;
	private BuildingInterface workplaceSelectedFromComboBox;
	
	private JPanel panelName;
	private JLabel label;
	private JTextField textField;
	private JPanel panelCash;
	private JLabel label_1;
	private JFormattedTextField formattedTextField;
	private JPanel panelJobContainer;
	private JPanel panelJobTitle;
	private JLabel label_2;
	private JPanel panelJob;
	private JLabel label_3;
	private JComboBox<String> comboBox;
	private JPanel panelWorkplace;
	private JLabel label_4;
	private JComboBox<BuildingInterface> comboBox_1;
	private JPanel panelShift;
	private JLabel label_5;
	private JLabel label_6;
	private JSpinner spinner;
	private JLabel label_7;
	private JSpinner spinner_1;
	private JPanel panelTitle;
	private JLabel label_8;
	private JPanel panelCar;
	private JPanel panelResidence;
	private JRadioButton rdbtnCar;
	private JRadioButton rdbtnNoCar;
	private JRadioButton rdbtnHouse;
	private JRadioButton rdbtnApartment;
	private JCheckBox chckbxRenter;
	private final ButtonGroup buttonGroupCar = new ButtonGroup();
	private final ButtonGroup buttonGroupResidence = new ButtonGroup();
	private JPanel panelCreate;
	private JButton btnCreate;
	
	//============================================================================//
	// Constructor        
    //============================================================================//
	
	public AddPersonTab(MainFrame mf) {
		
		//--------------------------------------//
		// Start setup         
		//--------------------------------------//
		
		// Set up variables
		this.setVisible(true);
	    this.currencyFormat.setRoundingMode(RoundingMode.DOWN);
	    this.currencyFormat.setParseIntegerOnly(true);
	    
		// Set up the JPanel
		this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.setAlignmentY(Component.TOP_ALIGNMENT);
		this.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.setPreferredSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMaximumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setMinimumSize(new Dimension(MainFrame.CONTROLPANELX, MainFrame.CONTROLPANELY));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// The ComboBoxModel is an object which stores what the ComboBox displays
		this.jobComboBoxModel = new DefaultComboBoxModel<String>();
		this.workplaceComboBoxModel = new DefaultComboBoxModel<BuildingInterface>();
		jobComboBoxModel.addElement("city.roles.BankManagerRole");
		jobComboBoxModel.addElement("city.roles.BankTellerRole");
		jobComboBoxModel.addElement("city.roles.MarketCashierRole");
		jobComboBoxModel.addElement("city.roles.MarketDeliveryPersonRole");
		jobComboBoxModel.addElement("city.roles.MarketEmployeeRole");
		jobComboBoxModel.addElement("city.roles.MarketManagerRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChoiCashierRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChoiCookRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChoiHostRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChoiWaiterDirectRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChoiWaiterQueueRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChungCashierRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChungCookRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChungHostRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChungWaiterMessageCookRole");
		jobComboBoxModel.addElement("city.roles.RestaurantChungWaiterRevolvingStandRole");
		jobComboBoxModel.addElement("city.roles.RestaurantJPCashierRole");
		jobComboBoxModel.addElement("city.roles.RestaurantJPCookRole");
		jobComboBoxModel.addElement("city.roles.RestaurantJPHostRole");
		jobComboBoxModel.addElement("city.roles.RestaurantJPWaiterRole");
		jobComboBoxModel.addElement("city.roles.RestaurantJPWaiterSharedDataRole");
		jobComboBoxModel.addElement("city.roles.RestaurantTimmsCashierRole");
		jobComboBoxModel.addElement("city.roles.RestaurantTimmsCookRole");
		jobComboBoxModel.addElement("city.roles.RestaurantTimmsHostRole");
		jobComboBoxModel.addElement("city.roles.RestaurantTimmsWaiterRole");
		jobComboBoxModel.addElement("city.roles.RestaurantZhangCashierRole");
		jobComboBoxModel.addElement("city.roles.RestaurantZhangCookRole");
		jobComboBoxModel.addElement("city.roles.RestaurantZhangHostRole");
		jobComboBoxModel.addElement("city.roles.RestaurantZhangWaiterRegularRole");
		jobComboBoxModel.addElement("city.roles.RestaurantZhangWaiterSharedDataRole");
		
		//--------------------------------------//
		//        
		//--------------------------------------//
		
		panelTitle = new JPanel();
		panelTitle.setPreferredSize(new Dimension(300, 25));
		panelTitle.setMinimumSize(new Dimension(300, 25));
		panelTitle.setMaximumSize(new Dimension(300, 25));
		panelTitle.setBorder(new EmptyBorder(5, 0, 0, 0));
		panelTitle.setLayout(new BoxLayout(panelTitle, BoxLayout.X_AXIS));
		add(panelTitle);
		
		label_8 = new JLabel("Configure person:");
		label_8.setFont(getFont().deriveFont(Font.BOLD));
		label_8.setPreferredSize(new Dimension(140, 20));
		label_8.setMinimumSize(new Dimension(140, 20));
		label_8.setMaximumSize(new Dimension(140, 20));
		label_8.setBorder(new EmptyBorder(0, 10, 0, 10));
		panelTitle.add(label_8);
		
		panelName = new JPanel();
		panelName.setPreferredSize(new Dimension(300, 30));
		panelName.setMinimumSize(new Dimension(300, 30));
		panelName.setMaximumSize(new Dimension(300, 30));
		panelName.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		panelName.setLayout(new BoxLayout(panelName, BoxLayout.X_AXIS));
		add(panelName);
		
		label = new JLabel("Name");
		label.setPreferredSize(new Dimension(70, 14));
		label.setMinimumSize(new Dimension(70, 14));
		label.setMaximumSize(new Dimension(70, 14));
		label.setBorder(new EmptyBorder(0, 10, 0, 10));
		panelName.add(label);
		
		textField = new JTextField();
		textField.setPreferredSize(new Dimension(200, 20));
		textField.setMinimumSize(new Dimension(200, 20));
		textField.setMaximumSize(new Dimension(200, 20));
		textField.setColumns(10);
		panelName.add(textField);
		
		panelCash = new JPanel();
		panelCash.setPreferredSize(new Dimension(300, 30));
		panelCash.setMinimumSize(new Dimension(300, 30));
		panelCash.setMaximumSize(new Dimension(300, 30));
		panelCash.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		panelCash.setLayout(new BoxLayout(panelCash, BoxLayout.X_AXIS));
		add(panelCash);
		
		label_1 = new JLabel("Cash");
		label_1.setPreferredSize(new Dimension(70, 14));
		label_1.setMinimumSize(new Dimension(70, 14));
		label_1.setMaximumSize(new Dimension(70, 14));
		label_1.setBorder(new EmptyBorder(0, 10, 0, 10));
		panelCash.add(label_1);
		
		formattedTextField = new JFormattedTextField((Format) null);
		formattedTextField.setPreferredSize(new Dimension(200, 20));
		formattedTextField.setMinimumSize(new Dimension(200, 20));
		formattedTextField.setMaximumSize(new Dimension(200, 20));
		formattedTextField.setColumns(10);
		panelCash.add(formattedTextField);
		
		panelCar = new JPanel();
		panelCar.setPreferredSize(new Dimension(300, 30));
		panelCar.setMinimumSize(new Dimension(300, 30));
		panelCar.setMaximumSize(new Dimension(300, 30));
		add(panelCar);
		panelCar.setLayout(new BoxLayout(panelCar, BoxLayout.X_AXIS));
		
		rdbtnCar = new JRadioButton("Has a car");
		rdbtnCar.setFocusable(false);
		buttonGroupCar.add(rdbtnCar);
		rdbtnCar.setBorder(new EmptyBorder(0, 10, 0, 0));
		rdbtnCar.setPreferredSize(new Dimension(100, 23));
		rdbtnCar.setMinimumSize(new Dimension(100, 23));
		rdbtnCar.setMaximumSize(new Dimension(100, 23));
		panelCar.add(rdbtnCar);
		
		rdbtnNoCar = new JRadioButton("Has no car");
		rdbtnNoCar.setFocusable(false);
		buttonGroupCar.add(rdbtnNoCar);
		rdbtnNoCar.setPreferredSize(new Dimension(100, 23));
		rdbtnNoCar.setMinimumSize(new Dimension(100, 23));
		rdbtnNoCar.setMaximumSize(new Dimension(100, 23));
		panelCar.add(rdbtnNoCar);
		
		panelResidence = new JPanel();
		panelResidence.setPreferredSize(new Dimension(300, 30));
		panelResidence.setMinimumSize(new Dimension(300, 30));
		panelResidence.setMaximumSize(new Dimension(300, 30));
		add(panelResidence);
		panelResidence.setLayout(new BoxLayout(panelResidence, BoxLayout.X_AXIS));
		
		rdbtnHouse = new JRadioButton("House");
		rdbtnHouse.setFocusable(false);
		buttonGroupResidence.add(rdbtnHouse);
		rdbtnHouse.setPreferredSize(new Dimension(100, 23));
		rdbtnHouse.setMinimumSize(new Dimension(100, 23));
		rdbtnHouse.setMaximumSize(new Dimension(100, 23));
		rdbtnHouse.setBorder(new EmptyBorder(0, 10, 0, 0));
		panelResidence.add(rdbtnHouse);
		
		rdbtnApartment = new JRadioButton("Apartment");
		rdbtnApartment.setFocusable(false);
		buttonGroupResidence.add(rdbtnApartment);
		rdbtnApartment.setPreferredSize(new Dimension(100, 23));
		rdbtnApartment.setMinimumSize(new Dimension(100, 23));
		rdbtnApartment.setMaximumSize(new Dimension(100, 23));
		panelResidence.add(rdbtnApartment);
		
		chckbxRenter = new JCheckBox("Is a renter?");
		chckbxRenter.setFocusable(false);
		chckbxRenter.setPreferredSize(new Dimension(100, 23));
		chckbxRenter.setMinimumSize(new Dimension(100, 23));
		chckbxRenter.setMaximumSize(new Dimension(100, 23));
		chckbxRenter.setHorizontalTextPosition(SwingConstants.LEFT);
		chckbxRenter.setHorizontalAlignment(SwingConstants.LEFT);
		panelResidence.add(chckbxRenter);
		
		panelJobContainer = new JPanel();
		panelJobContainer.setPreferredSize(new Dimension(300, 115));
		panelJobContainer.setMinimumSize(new Dimension(300, 115));
		panelJobContainer.setMaximumSize(new Dimension(300, 115));
		panelJobContainer.setLayout(new BoxLayout(panelJobContainer, BoxLayout.Y_AXIS));
		add(panelJobContainer);
		
		panelJobTitle = new JPanel();
		panelJobTitle.setPreferredSize(new Dimension(300, 25));
		panelJobTitle.setMinimumSize(new Dimension(300, 25));
		panelJobTitle.setMaximumSize(new Dimension(300, 25));
		panelJobTitle.setBorder(new EmptyBorder(5, 0, 0, 0));
		panelJobTitle.setLayout(new BoxLayout(panelJobTitle, BoxLayout.X_AXIS));
		panelJobContainer.add(panelJobTitle);
		
		label_2 = new JLabel("Set person's job:");
		label_2.setFont(getFont().deriveFont(Font.BOLD));
		label_2.setPreferredSize(new Dimension(140, 20));
		label_2.setMinimumSize(new Dimension(140, 20));
		label_2.setMaximumSize(new Dimension(140, 20));
		label_2.setBorder(new EmptyBorder(0, 10, 0, 10));
		panelJobTitle.add(label_2);
		
		panelJob = new JPanel();
		panelJob.setPreferredSize(new Dimension(300, 30));
		panelJob.setMinimumSize(new Dimension(300, 30));
		panelJob.setMaximumSize(new Dimension(300, 30));
		panelJob.setLayout(new BoxLayout(panelJob, BoxLayout.X_AXIS));
		panelJobContainer.add(panelJob);
		
		label_3 = new JLabel("Job");
		label_3.setPreferredSize(new Dimension(100, 16));
		label_3.setMinimumSize(new Dimension(100, 16));
		label_3.setMaximumSize(new Dimension(100, 16));
		label_3.setBorder(new EmptyBorder(0, 10, 0, 10));
		panelJob.add(label_3);
		
		comboBox = new JComboBox<String>(jobComboBoxModel);
		comboBox.setPreferredSize(new Dimension(190, 20));
		comboBox.setMinimumSize(new Dimension(190, 20));
		comboBox.setMaximumSize(new Dimension(190, 20));
		comboBox.setFocusable(false);
		panelJob.add(comboBox);
		
		panelWorkplace = new JPanel();
		panelWorkplace.setPreferredSize(new Dimension(300, 30));
		panelWorkplace.setMinimumSize(new Dimension(300, 30));
		panelWorkplace.setMaximumSize(new Dimension(300, 30));
		panelWorkplace.setLayout(new BoxLayout(panelWorkplace, BoxLayout.X_AXIS));
		panelJobContainer.add(panelWorkplace);
		
		label_4 = new JLabel("Workplace");
		label_4.setPreferredSize(new Dimension(100, 16));
		label_4.setMinimumSize(new Dimension(100, 16));
		label_4.setMaximumSize(new Dimension(100, 16));
		label_4.setBorder(new EmptyBorder(0, 10, 0, 10));
		panelWorkplace.add(label_4);
		
		comboBox_1 = new JComboBox<BuildingInterface>(workplaceComboBoxModel);
		comboBox_1.setPreferredSize(new Dimension(190, 20));
		comboBox_1.setMinimumSize(new Dimension(190, 20));
		comboBox_1.setMaximumSize(new Dimension(190, 20));
		comboBox_1.setFocusable(false);
		panelWorkplace.add(comboBox_1);
		
		panelShift = new JPanel();
		panelShift.setPreferredSize(new Dimension(300, 30));
		panelShift.setMinimumSize(new Dimension(300, 30));
		panelShift.setMaximumSize(new Dimension(300, 30));
		panelShift.setLayout(new BoxLayout(panelShift, BoxLayout.X_AXIS));
		panelJobContainer.add(panelShift);
		
		label_5 = new JLabel("Shift");
		label_5.setPreferredSize(new Dimension(100, 16));
		label_5.setMinimumSize(new Dimension(100, 16));
		label_5.setMaximumSize(new Dimension(100, 16));
		label_5.setBorder(new EmptyBorder(0, 10, 0, 10));
		panelShift.add(label_5);
		
		label_6 = new JLabel("Start");
		label_6.setBorder(new EmptyBorder(0, 0, 0, 5));
		panelShift.add(label_6);
		
		spinner = new JSpinner();
		spinner.setPreferredSize(new Dimension(40, 20));
		spinner.setMinimumSize(new Dimension(40, 20));
		spinner.setMaximumSize(new Dimension(40, 20));
		panelShift.add(spinner);
		
		label_7 = new JLabel("End");
		label_7.setBorder(new EmptyBorder(0, 15, 0, 5));
		panelShift.add(label_7);
		
		spinner_1 = new JSpinner();
		spinner_1.setPreferredSize(new Dimension(40, 20));
		spinner_1.setMinimumSize(new Dimension(40, 20));
		spinner_1.setMaximumSize(new Dimension(40, 20));
		panelShift.add(spinner_1);
		
		panelCreate = new JPanel();
		panelCreate.setBorder(new EmptyBorder(10, 10, 0, 10));
		panelCreate.setPreferredSize(new Dimension(300, 40));
		panelCreate.setMaximumSize(new Dimension(300, 40));
		panelCreate.setMinimumSize(new Dimension(300, 40));
		add(panelCreate);
		panelCreate.setLayout(new BorderLayout(0, 0));
		
		btnCreate = new JButton("Create");
		btnCreate.setFocusable(false);
		btnCreate.setHorizontalTextPosition(SwingConstants.CENTER);
		btnCreate.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnCreate.setFont(getFont().deriveFont(Font.BOLD));
		panelCreate.add(btnCreate);
		
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Create all actions for buttons/control components here
	}
}
