package city.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import utilities.DataModel;
import city.Application;
import city.agents.CarAgent;
import city.agents.PersonAgent;
import city.animations.CarAnimation;
import city.animations.PersonAnimation;
import city.bases.JobRole;
import city.bases.interfaces.BuildingInterface;
import city.bases.interfaces.ResidenceBuildingInterface;
import city.gui.MainFrame;
import city.roles.LandlordRole;

public class AddPersonTab extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 9166425422374406573L;
	
	//============================================================================//
	// Data       
    //============================================================================//
	
	private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(getDefaultLocale());
	
	private MainFrame mainFrame;
	private DataModel dataModel;
	private DefaultComboBoxModel<ResidenceBuildingInterface> residenceComboBoxModel;
	private DefaultComboBoxModel<String> jobComboBoxModel;
	private DefaultComboBoxModel<BuildingInterface> workplaceComboBoxModel;
	private ResidenceBuildingInterface residenceSelectedFromComboBox;
	private String jobSelectedFromComboBox;
	private BuildingInterface workplaceSelectedFromComboBox;
	
	private JPanel panelName;
	private JLabel label;
	private JTextField nameField;
	private JPanel panelCash;
	private JLabel label_1;
	private JFormattedTextField formattedTextField;
	private JPanel panelJobContainer;
	private JPanel panelJobTitle;
	private JLabel label_2;
	private JPanel panelJob;
	private JLabel label_3;
	private JComboBox<String> cbJob;
	private JPanel panelWorkplace;
	private JLabel label_4;
	private JComboBox<BuildingInterface> cbWorkplace;
	private JPanel panelShift;
	private JLabel label_5;
	private JLabel label_6;
	private JSpinner spinner;
	private JLabel label_7;
	private JSpinner spinner_1;
	private JPanel panelTitle;
	private JLabel label_8;
	private JPanel panelCar;
	private JPanel panelLandlord;
	private JRadioButton rdbtnCar;
	private JRadioButton rdbtnNoCar;
	private JCheckBox chckbxLandlord;
	private final ButtonGroup buttonGroupCar = new ButtonGroup();
	private JPanel panelCreate;
	private JButton btnCreate;
	private JPanel panelResidence;
	private JComboBox<ResidenceBuildingInterface> cbResidence;
	private JLabel lblResidence;
	private JLabel lblError;
	private JPanel panel;
	private JCheckBox chckbxHasJob;
	
	//============================================================================//
	// Constructor        
    //============================================================================//
	
	public AddPersonTab(MainFrame mf) {
		
		//--------------------------------------//
		// Start setup         
		//--------------------------------------//
		
		// Set up variables
		this.mainFrame = mf;
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
		
		// The DataModel allows the list objects to auto-update
		this.dataModel = Application.getModel();
		this.dataModel.getPropertyChangeSupport().addPropertyChangeListener(DataModel.BUILDINGS, this);

		// The ComboBoxModel is an object which stores what the ComboBox displays
		this.residenceComboBoxModel = new DefaultComboBoxModel<ResidenceBuildingInterface>();
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
		// Configure person 
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
		
		//--------------------------------------//
		// Name    
		//--------------------------------------//
		
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
		
		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(200, 20));
		nameField.setMinimumSize(new Dimension(200, 20));
		nameField.setMaximumSize(new Dimension(200, 20));
		nameField.setColumns(10);
		panelName.add(nameField);
		
		//--------------------------------------//
		// Cash       
		//--------------------------------------//
		
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
		
		formattedTextField = new JFormattedTextField(currencyFormat);
		formattedTextField.setPreferredSize(new Dimension(200, 20));
		formattedTextField.setMinimumSize(new Dimension(200, 20));
		formattedTextField.setMaximumSize(new Dimension(200, 20));
		formattedTextField.setColumns(10);
		panelCash.add(formattedTextField);
		
		//--------------------------------------//
		// Car       
		//--------------------------------------//
		
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
		
		//--------------------------------------//
		// Residence       
		//--------------------------------------//
		
		panelResidence = new JPanel();
		panelResidence.setPreferredSize(new Dimension(300, 30));
		panelResidence.setMinimumSize(new Dimension(300, 30));
		panelResidence.setMaximumSize(new Dimension(300, 30));
		add(panelResidence);
		panelResidence.setLayout(new BoxLayout(panelResidence, BoxLayout.X_AXIS));
		
		lblResidence = new JLabel("Residence");
		lblResidence.setBorder(new EmptyBorder(0, 10, 0, 0));
		lblResidence.setPreferredSize(new Dimension(100, 16));
		lblResidence.setMinimumSize(new Dimension(100, 16));
		lblResidence.setMaximumSize(new Dimension(100, 16));
		panelResidence.add(lblResidence);
		
		cbResidence = new JComboBox<ResidenceBuildingInterface>(residenceComboBoxModel);
		cbResidence.setFocusable(false);
		cbResidence.setPreferredSize(new Dimension(190, 20));
		cbResidence.setMinimumSize(new Dimension(190, 20));
		cbResidence.setMaximumSize(new Dimension(190, 20));
		cbResidence.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		residenceSelectedFromComboBox = (ResidenceBuildingInterface) cbResidence.getSelectedItem();
        	}
        });
		cbResidence.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -4988832297955832718L;
			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof BuildingInterface) {
                    ((JLabel) renderer).setText(((BuildingInterface) value).getName());
                }
                return renderer;
            }
        });
		panelResidence.add(cbResidence);
		
		//--------------------------------------//
		// Renter       
		//--------------------------------------//
		
		panelLandlord = new JPanel();
		panelLandlord.setPreferredSize(new Dimension(300, 30));
		panelLandlord.setMinimumSize(new Dimension(300, 30));
		panelLandlord.setMaximumSize(new Dimension(300, 30));
		add(panelLandlord);
		panelLandlord.setLayout(new BoxLayout(panelLandlord, BoxLayout.X_AXIS));
		
		chckbxLandlord = new JCheckBox("Is a landlord?");
		chckbxLandlord.setBorder(new EmptyBorder(0, 10, 0, 0));
		chckbxLandlord.setFocusable(false);
		chckbxLandlord.setPreferredSize(new Dimension(120, 23));
		chckbxLandlord.setMinimumSize(new Dimension(120, 23));
		chckbxLandlord.setMaximumSize(new Dimension(120, 23));
		chckbxLandlord.setHorizontalTextPosition(SwingConstants.LEFT);
		chckbxLandlord.setHorizontalAlignment(SwingConstants.LEFT);
		panelLandlord.add(chckbxLandlord);
		
		chckbxHasJob = new JCheckBox("Has a job?");
		chckbxHasJob.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxHasJob.isSelected()) {
					panelJobContainer.setVisible(true);
				} else {
					panelJobContainer.setVisible(false);
				}
			}
		});
		chckbxHasJob.setFocusable(false);
		chckbxHasJob.setSelected(true);
		chckbxHasJob.setHorizontalTextPosition(SwingConstants.LEFT);
		chckbxHasJob.setHorizontalAlignment(SwingConstants.LEFT);
		panelLandlord.add(chckbxHasJob);
		
		//--------------------------------------//
		// Job Container      
		//--------------------------------------//
		
		panelJobContainer = new JPanel();
		panelJobContainer.setPreferredSize(new Dimension(300, 115));
		panelJobContainer.setMinimumSize(new Dimension(300, 115));
		panelJobContainer.setMaximumSize(new Dimension(300, 115));
		panelJobContainer.setLayout(new BoxLayout(panelJobContainer, BoxLayout.Y_AXIS));
		add(panelJobContainer);
		
		//--------------------------------------//
		// Set person's job       
		//--------------------------------------//
		
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
		
		//--------------------------------------//
		// Job       
		//--------------------------------------//
		
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
		
		cbJob = new JComboBox<String>(jobComboBoxModel);
		cbJob.setPreferredSize(new Dimension(190, 20));
		cbJob.setMinimumSize(new Dimension(190, 20));
		cbJob.setMaximumSize(new Dimension(190, 20));
		cbJob.setFocusable(false);
		cbJob.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		jobSelectedFromComboBox = (String) cbJob.getSelectedItem();
        		updateWorkplaceComboBox();
        	}
        });
		panelJob.add(cbJob);
		
		//--------------------------------------//
		// Workplace       
		//--------------------------------------//
		
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
		
		cbWorkplace = new JComboBox<BuildingInterface>(workplaceComboBoxModel);
		cbWorkplace.setPreferredSize(new Dimension(190, 20));
		cbWorkplace.setMinimumSize(new Dimension(190, 20));
		cbWorkplace.setMaximumSize(new Dimension(190, 20));
		cbWorkplace.setFocusable(false);
		cbWorkplace.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		workplaceSelectedFromComboBox = (BuildingInterface) cbWorkplace.getSelectedItem();
        	}
        });
		cbWorkplace.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 7112080767015019155L;
			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof BuildingInterface) {
                    ((JLabel) renderer).setText(((BuildingInterface) value).getName());
                }
                return renderer;
            }
        });
		panelWorkplace.add(cbWorkplace);
		
		//--------------------------------------//
		// Shift        
		//--------------------------------------//
		
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
		
		//--------------------------------------//
		// Create       
		//--------------------------------------//
		
		panelCreate = new JPanel();
		panelCreate.setFocusable(false);
		panelCreate.setBorder(new EmptyBorder(10, 10, 0, 10));
		panelCreate.setPreferredSize(new Dimension(300, 40));
		panelCreate.setMaximumSize(new Dimension(300, 40));
		panelCreate.setMinimumSize(new Dimension(300, 40));
		add(panelCreate);
		panelCreate.setLayout(new BorderLayout(0, 0));
		
		btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createPerson();
			}
		});
		btnCreate.setFocusable(false);
		btnCreate.setHorizontalTextPosition(SwingConstants.CENTER);
		btnCreate.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnCreate.setFont(getFont().deriveFont(Font.BOLD));
		panelCreate.add(btnCreate, BorderLayout.CENTER);
		
		//--------------------------------------//
		// Error       
		//--------------------------------------//
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(280, 30));
		panel.setMinimumSize(new Dimension(280, 30));
		panel.setMaximumSize(new Dimension(280, 30));
		panel.setBorder(new EmptyBorder(10, 10, 0, 10));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		add(panel);
		
		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		lblError.setPreferredSize(new Dimension(260, 20));
		lblError.setMinimumSize(new Dimension(260, 20));
		lblError.setMaximumSize(new Dimension(260, 20));
		panel.add(lblError);
		
		//--------------------------------------//
		// Finish setup
		//--------------------------------------//
		
		prepareFormForUse();
		
	}
	
	//============================================================================//
	// Actions   
    //============================================================================//
	
	/**
	 * If the person is told to be a landlord, he will become the landlord of his
	 * residence, even if that residence already has a landlord.
	 */
	private void createPerson() {
		lblError.setText("");
		try {
			formattedTextField.commitEdit();
			spinner.commitEdit();
			spinner_1.commitEdit();
		} catch (ParseException e) {}
		if (nameField.getText().isEmpty()) {
			lblError.setText("You must enter a name.");
		} else if (buttonGroupCar.getSelection() == null) {
			lblError.setText("You must select whether the person will have a car.");
		} else if (residenceSelectedFromComboBox == null) {
			lblError.setText("The person must have a residence.");
		} else if (chckbxHasJob.isSelected() && jobSelectedFromComboBox == null) {
			lblError.setText("The person must have a job.");
		} else if (chckbxHasJob.isSelected() && workplaceSelectedFromComboBox == null ) {
			lblError.setText("The person must have a workplace.");
		} else {
			PersonAgent newPerson = new PersonAgent(nameField.getText(), Application.getDate(), new PersonAnimation(), residenceSelectedFromComboBox);
			newPerson.setCash(Integer.parseInt(formattedTextField.getValue().toString()));
			if (rdbtnCar.isSelected()) {
				CarAgent newCar = new CarAgent(residenceSelectedFromComboBox, newPerson);
				CarAnimation newCarAnimation = new CarAnimation(newCar, residenceSelectedFromComboBox);
				mainFrame.cityView.addAnimation(newCarAnimation);
				newPerson.setCar(newCar);
				newCar.startThread();
			}
			if (chckbxLandlord.isSelected()) {
				LandlordRole newLandlord = new LandlordRole();
				residenceSelectedFromComboBox.setLandlord(newLandlord);
				newPerson.addRole(newLandlord);
				newLandlord.setActive();
			}
			if (chckbxHasJob.isSelected()) {
				try {
					Class<?> c0 = Class.forName(jobSelectedFromComboBox);
					Class<?> c1 = Class.forName(workplaceSelectedFromComboBox.getBuildingClassName());
					Constructor<?> r0 = c0.getConstructor(c1, Integer.TYPE, Integer.TYPE);
					JobRole j0 = (JobRole) r0.newInstance(workplaceSelectedFromComboBox, (Integer) spinner.getValue(), (Integer) spinner_1.getValue());
					newPerson.setOccupation(j0);
				} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			dataModel.addPerson(newPerson);
			newPerson.startThread();
			mainFrame.CP.editPersonTab.displayPerson(newPerson);
			mainFrame.CP.displayTab(mainFrame.CP.editPersonTab);
		}
	}
	
	//============================================================================//
	// Utilities    
    //============================================================================//
	
	//--------------------------------------//
	// Update combo boxes
	//--------------------------------------//
	
	private void updateResidenceComboBox() {
		residenceComboBoxModel.removeAllElements();
		for (BuildingInterface b : Application.CityMap.getBuildings()) {
			if (b instanceof ResidenceBuildingInterface) {
				ResidenceBuildingInterface r = (ResidenceBuildingInterface) b;
				if (!r.getIsFull()) {
					residenceComboBoxModel.addElement(r);
				}
			}
		}
	}
	
	private void updateWorkplaceComboBox() {
		workplaceComboBoxModel.removeAllElements();
		if (jobSelectedFromComboBox != null) {
			for (BuildingInterface b : Application.CityMap.getBuildings()) {
				if (b.getWorkerRoleClassNames().contains(jobSelectedFromComboBox)) {
					workplaceComboBoxModel.addElement(b);
				}
			}
		}
	}
	
	//--------------------------------------//
	// Clear the form / make it ready again
	//--------------------------------------//
	
	public void prepareFormForUse() {
		nameField.setText("");
		formattedTextField.setValue(0);
		buttonGroupCar.clearSelection();
		updateResidenceComboBox();
		chckbxLandlord.setSelected(false);
		chckbxHasJob.setSelected(true);
		panelJobContainer.setVisible(true);
		cbJob.setSelectedIndex(0);
		updateWorkplaceComboBox();
		spinner.setValue(0);
		spinner_1.setValue(0);
		lblError.setText("");
	}
	
	//============================================================================//
	// Listeners     
    //============================================================================//
	
	/**
	 * This updates the list when buildings are added and removed
	 */
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    propertyChange(evt);
                }
            });
            return;
        }
        if (evt.getSource() == dataModel) {
            // A building has been added or removed from the combo box list
            if (DataModel.BUILDINGS.equals(evt.getPropertyName())) {
            	updateResidenceComboBox();
            	updateWorkplaceComboBox();
            }
        }
    }
	
}
