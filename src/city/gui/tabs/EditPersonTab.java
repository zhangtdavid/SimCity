package city.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.SystemColor;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import utilities.DataModel;
import city.Application;
import city.agents.CarAgent;
import city.agents.interfaces.Person;
import city.animations.CarAnimation;
import city.bases.JobRole;
import city.bases.interfaces.BuildingInterface;
import city.bases.interfaces.JobRoleInterface;
import city.bases.interfaces.RoleInterface;
import city.gui.MainFrame;

public class EditPersonTab extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 9166425422374406573L;
	
	//============================================================================//
	// Data       
    //============================================================================//
	
	private final MainFrame mainFrame;
	
	private final ImageIcon car = new ImageIcon(EditPersonTab.class.getResource("/icons/car.png"));
	private final ImageIcon nocar = new ImageIcon(EditPersonTab.class.getResource("/icons/nocar.png"));
	// private final ImageIcon house = new ImageIcon(EditPersonTab.class.getResource("/icons/home.png")); // TODO
	// private final ImageIcon apartment = new ImageIcon(EditPersonTab.class.getResource("/icons/apartment.png")); // TODO
	private final ImageIcon job = new ImageIcon(EditPersonTab.class.getResource("/icons/job.png"));
	private final ImageIcon nojob = new ImageIcon(EditPersonTab.class.getResource("/icons/nojob.png"));
	// private final ImageIcon sleep = new ImageIcon(EditPersonTab.class.getResource("/icons/sleep.png")); // TODO
	private final ImageIcon terminateWithExtremePrejudice = new ImageIcon(EditPersonTab.class.getResource("/icons/terminate.png"));
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(getDefaultLocale());
	
	private DataModel dataModel;
	private DefaultListModel<Person> peopleListModel;
	private DefaultListModel<RoleInterface> roleListModel;
	private DefaultComboBoxModel<String> jobComboBoxModel;
	private DefaultComboBoxModel<BuildingInterface> workplaceComboBoxModel;
	private Person personSelectedFromList; // Needed so that we can deactivate old listeners
	private RoleInterface roleSelectedFromList;
	private String jobSelectedFromComboBox;
	private BuildingInterface workplaceSelectedFromComboBox;
	
	private JScrollPane scrollPeople;
	private JList<Person> listPeople;
	private JPanel panelName;
	private JLabel labelName;
	private JTextField textName;
	private JPanel panelCash;
	private JLabel labelCash;
	private JFormattedTextField textCash;
	private JPanel panelControl;
	private JButton btnRevert;
	private JButton btnSave;
	private JPanel panelState;
	private JLabel labelState;
	private JLabel labelStateValue;
	private JPanel panelRoles;
	private JScrollPane scrollRoles;
	private JList<RoleInterface> listRoles;
	private JLabel labelRoles;
	private JPanel panelRoleState;
	private JLabel labelRoleState;
	private JLabel labelRoleStateValue;
	private JPanel panelRoleActive;
	private JLabel labelRoleActive;
	private JLabel labelRoleActiveValue;
	private JPanel panelRoleActivity;
	private JLabel labelRoleActivity;
	private JLabel labelRoleActivityValue;
	private JPanel panelToggles;
	private JButton btnHasCar;
	private JButton btnHouse;
	private JButton btnWork;
	// private JButton btnHasHouse; // TODO
	private JPanel panelShift1;
	private JLabel labelShift1;
	private JLabel labelStart1;
	private JLabel labelEnd1;
	private JSpinner spinnerStart1;
	private JSpinner spinnerEnd1;
	private JPanel panelSalary;
	private JLabel labelSalary;
	private JLabel labelSalaryValue;
	private JPanel panelRoleRevertSave;
	private JButton buttonRoleRevert;
	private JButton buttonRoleSave;
	private JButton btnHasJob;
	private JPanel panelJob;
	private JComboBox<String> comboBoxJob;
	private JLabel labelJob;
	private JPanel panelWorkplace;
	private JLabel labelWorkplace;
	private JComboBox<BuildingInterface> comboBoxWorkplace;
	private JPanel panelShift2;
	private JLabel labelShift2;
	private JLabel labelStart2;
	private JSpinner spinnerStart2;
	private JLabel labelEnd2;
	private JSpinner spinnerEnd2;
	private JPanel panelAdd;
	private JButton buttonAdd;
	private JPanel panelNewJob;
	private JPanel panelEditJob;
	private JLabel label;
	private JPanel panel;
	// private JButton btnSleep; // TODO
	private JButton btnTerminate;
	private JPanel panelHouseData;
	private JLabel lblLivesIn;
	private JLabel lblResidence;
	
	//============================================================================//
	// Constructor        
    //============================================================================//
		
	public EditPersonTab(MainFrame mf) {
		
		//--------------------------------------//
		// Start setup         
		//--------------------------------------//
		
		// Set up variables
		this.setVisible(true);
	    this.currencyFormat.setRoundingMode(RoundingMode.DOWN);
	    this.currencyFormat.setParseIntegerOnly(true);
	    this.mainFrame = mf;

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
		this.dataModel.getPropertyChangeSupport().addPropertyChangeListener(DataModel.PEOPLE, this);
		this.dataModel.getPropertyChangeSupport().addPropertyChangeListener(DataModel.BUILDINGS, this);
		
		// The ListModel is an object which stores what the JList displays
		this.peopleListModel = new DefaultListModel<Person>();
		this.roleListModel = new DefaultListModel<RoleInterface>();
		
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
		// Person list         
		//--------------------------------------//
        
		// Scroll for the list
		scrollPeople = new JScrollPane();
		scrollPeople.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPeople.setPreferredSize(new Dimension(300, 120));
		scrollPeople.setMinimumSize(new Dimension(300, 120));
		scrollPeople.setMaximumSize(new Dimension(300, 120));
		scrollPeople.setBorder(new LineBorder(SystemColor.menu, 3));
		this.add(scrollPeople);
		
        // Create the JList
		listPeople = new JList<Person>(peopleListModel);
		listPeople.addListSelectionListener(new ListSelectionListener() {
			/**
			 * When a Person is selected from the list, this is the code that kicks off the GUI update
			 * It also sets up a listener so that the GUI will update with changes for as long as that Person remains selected
			 */
			public void valueChanged(ListSelectionEvent arg0) {
				if (listPeople.getSelectedValue() != null) {
					if (personSelectedFromList != null) {
						// Remove the listener from the previous selection
						personSelectedFromList.getPropertyChangeSupport().removePropertyChangeListener(EditPersonTab.this);
						mainFrame.personTracePanel.toggleAlertsWithOString(Integer.toString(personSelectedFromList.hashCode()), false);
						goToPersonLocation(personSelectedFromList);
					}
					personSelectedFromList = listPeople.getSelectedValue();
					personSelectedFromList.getPropertyChangeSupport().addPropertyChangeListener(EditPersonTab.this);
					mainFrame.personTracePanel.toggleAlertsWithOString(Integer.toString(personSelectedFromList.hashCode()), true);
					updatePersonValues();
				} else {
					setEditPersonBlank();
				}
				toggleEditJob(false);
				toggleNewJob(false);
				setEditJobBlank();
				setNewJobBlank();
			}
		});
		listPeople.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPeople.setBackground(Color.WHITE);
		listPeople.setAlignmentY(Component.TOP_ALIGNMENT);
		listPeople.setAlignmentX(Component.LEFT_ALIGNMENT);
        listPeople.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -1088518111443365535L;
			
			/**
			 * Allows Person objects to be displayed as custom text (in this case, their names) in the JList
			 */
			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof Person) {
                    ((JLabel) renderer).setText(((Person) value).getName());
                }
                return renderer;
            }
        });
        scrollPeople.setViewportView(listPeople);
        
		//--------------------------------------//
		// Person info    
		//--------------------------------------//
        
        // Create the state area
        panelState = new JPanel();
        panelState.setPreferredSize(new Dimension(300, 20));
        panelState.setMinimumSize(new Dimension(300, 20));
        panelState.setMaximumSize(new Dimension(300, 20));
        panelState.setLayout(new BoxLayout(panelState, BoxLayout.X_AXIS));
        add(panelState);
        
        labelState = new JLabel("State");
        labelState.setBorder(new EmptyBorder(0, 10, 0, 10));
        labelState.setPreferredSize(new Dimension(70, 16));
        labelState.setMinimumSize(new Dimension(70, 16));
        labelState.setMaximumSize(new Dimension(70, 16));
        panelState.add(labelState);
        
        labelStateValue = new JLabel("null");
        labelStateValue.setFont(getFont().deriveFont(Font.BOLD));
        panelState.add(labelStateValue);
        
		//--------------------------------------//
		// Person form        
		//--------------------------------------//
        
        // Create the name control
        panelName = new JPanel();
        panelName.setMinimumSize(new Dimension(300, 30));
        panelName.setMaximumSize(new Dimension(300, 30));
        panelName.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panelName.setPreferredSize(new Dimension(300, 30));
        panelName.setLayout(new BoxLayout(panelName, BoxLayout.X_AXIS));
        add(panelName);
        
        labelName = new JLabel("Name");
        labelName.setPreferredSize(new Dimension(70, 14));
        labelName.setMinimumSize(new Dimension(70, 14));
        labelName.setMaximumSize(new Dimension(70, 14));
        labelName.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelName.add(labelName);
        
        textName = new JTextField();
        textName.setPreferredSize(new Dimension(200, 20));
        textName.setMinimumSize(new Dimension(200, 20));
        textName.setMaximumSize(new Dimension(200, 20));
        textName.setColumns(10);
        panelName.add(textName);
        
        // Create the cash control
        panelCash = new JPanel();
        panelCash.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panelCash.setPreferredSize(new Dimension(300, 30));
        panelCash.setMinimumSize(new Dimension(300, 30));
        panelCash.setMaximumSize(new Dimension(300, 30));
        panelCash.setLayout(new BoxLayout(panelCash, BoxLayout.X_AXIS));
        add(panelCash);
        
        labelCash = new JLabel("Cash");
        labelCash.setPreferredSize(new Dimension(70, 14));
        labelCash.setMinimumSize(new Dimension(70, 14));
        labelCash.setMaximumSize(new Dimension(70, 14));
        labelCash.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelCash.add(labelCash);
       
        textCash = new JFormattedTextField(currencyFormat);
        textCash.setPreferredSize(new Dimension(200, 20));
        textCash.setMinimumSize(new Dimension(200, 20));
        textCash.setMaximumSize(new Dimension(200, 20));
        textCash.setColumns(10);
        panelCash.add(textCash);
        
        panelHouseData = new JPanel();
        panelHouseData.setPreferredSize(new Dimension(300, 30));
        panelHouseData.setMinimumSize(new Dimension(300, 30));
        panelHouseData.setMaximumSize(new Dimension(300, 30));
        add(panelHouseData);
        panelHouseData.setLayout(new BoxLayout(panelHouseData, BoxLayout.X_AXIS));
        
        lblLivesIn = new JLabel("Lives in:");
        lblLivesIn.setPreferredSize(new Dimension(70, 14));
        lblLivesIn.setMinimumSize(new Dimension(70, 14));
        lblLivesIn.setMaximumSize(new Dimension(70, 14));
        lblLivesIn.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelHouseData.add(lblLivesIn);
        
        lblResidence = new JLabel("null");
        lblResidence.setMinimumSize(new Dimension(200, 14));
        lblResidence.setMaximumSize(new Dimension(200, 14));
        lblResidence.setPreferredSize(new Dimension(200, 14));
        lblResidence.setFont(getFont().deriveFont(Font.BOLD));
        panelHouseData.add(lblResidence);
        
		//--------------------------------------//
		// Person buttons 1        
		//--------------------------------------//
        
        panelControl = new JPanel();
        panelControl.setPreferredSize(new Dimension(300, 30));
        panelControl.setMinimumSize(new Dimension(300, 30));
        panelControl.setMaximumSize(new Dimension(300, 30));
        panelControl.setLayout(new BoxLayout(panelControl, BoxLayout.X_AXIS));
        add(panelControl);
        
        // Revert
        btnRevert = new JButton("Revert");
        btnRevert.setFocusable(false);
        btnRevert.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		updatePersonValues();
        	}
        });
        btnRevert.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panelControl.add(btnRevert);
        
        // Save
        btnSave = new JButton("Save");
        btnSave.setFocusable(false);
        btnSave.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		saveChangedPerson();
        	}
        });
        btnSave.setAlignmentX(Component.RIGHT_ALIGNMENT);
        panelControl.add(btnSave);
        
        // See Home
		btnHouse = new JButton("See Home");
		btnHouse.setFocusable(false);
		btnHouse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
        		Application.getMainFrame().CP.editBuildingsTab.displayBuilding(personSelectedFromList.getHome());
			}
		});
		btnHouse.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panelControl.add(btnHouse);
		
		// See Job
		btnWork = new JButton("See Job");
		btnWork.setFocusable(false);
		btnWork.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Application.getMainFrame().CP.editBuildingsTab.displayBuilding(personSelectedFromList.getOccupation().getWorkplace(BuildingInterface.class));
			}
		});
		btnWork.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panelControl.add(btnWork);
		
		//--------------------------------------//
		// Person buttons 2       
		//--------------------------------------//
        
        // Add car button
        panelToggles = new JPanel();
        panelToggles.setPreferredSize(new Dimension(300, 40));
        panelToggles.setMinimumSize(new Dimension(300, 40));
        panelToggles.setMaximumSize(new Dimension(300, 40));
        panelToggles.setLayout(new BoxLayout(panelToggles, BoxLayout.X_AXIS));
        add(panelToggles);
        
        // Car toggle
        btnHasCar = new JButton(nocar);
        btnHasCar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		togglePersonCar();
        	}
        });
        btnHasCar.setFocusable(false);
        btnHasCar.setMargin(new Insets(2, 2, 2, 2));
        btnHasCar.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnHasCar.setHorizontalAlignment(SwingConstants.LEFT);
        btnHasCar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHasCar.setPreferredSize(new Dimension(55, 40));
        btnHasCar.setMinimumSize(new Dimension(55, 40));
        btnHasCar.setMaximumSize(new Dimension(55, 40));
        panelToggles.add(btnHasCar);
      
// TODO
//        // House toggle
//        btnHasHouse = new JButton(house);
//        btnHasHouse.addActionListener(new ActionListener() {
//        	public void actionPerformed(ActionEvent arg0) {
//        		togglePersonHome();
//        	}
//        });
//        btnHasHouse.setFocusable(false);
//        btnHasHouse.setMargin(new Insets(2, 2, 2, 2));
//        btnHasHouse.setHorizontalTextPosition(SwingConstants.RIGHT);
//        btnHasHouse.setHorizontalAlignment(SwingConstants.LEFT);
//        btnHasHouse.setAlignmentX(Component.CENTER_ALIGNMENT);
//        btnHasHouse.setPreferredSize(new Dimension(55, 40));
//        btnHasHouse.setMinimumSize(new Dimension(55, 40));
//        btnHasHouse.setMaximumSize(new Dimension(55, 40));
//        panelToggles.add(btnHasHouse);
        
        // Job toggle
        btnHasJob = new JButton(job);
        btnHasJob.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		togglePersonJob();
        	}
        });
        btnHasJob.setFocusable(false);
        btnHasJob.setMargin(new Insets(2, 2, 2, 2));
        btnHasJob.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnHasJob.setHorizontalAlignment(SwingConstants.LEFT);
        btnHasJob.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnHasJob.setPreferredSize(new Dimension(55, 40));
        btnHasJob.setMinimumSize(new Dimension(55, 40));
        btnHasJob.setMaximumSize(new Dimension(55, 40));
        panelToggles.add(btnHasJob);
        
// TODO
//        // Sleep toggle
//        btnSleep = new JButton(sleep);
//        btnSleep.addActionListener(new ActionListener() {
//        	public void actionPerformed(ActionEvent e) {
//        		personSelectedFromList.forceSleep();
//        	}
//        });
//        btnSleep.setPreferredSize(new Dimension(55, 40));
//        btnSleep.setMinimumSize(new Dimension(55, 40));
//        btnSleep.setMaximumSize(new Dimension(55, 40));
//        btnSleep.setMargin(new Insets(2, 2, 2, 2));
//        btnSleep.setHorizontalTextPosition(SwingConstants.RIGHT);
//        btnSleep.setHorizontalAlignment(SwingConstants.LEFT);
//        btnSleep.setFocusable(false);
//        btnSleep.setEnabled(false);
//        btnSleep.setAlignmentX(0.5f);
//        panelToggles.add(btnSleep);
        
        // Terminate
        btnTerminate = new JButton(terminateWithExtremePrejudice);
        btnTerminate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		terminateWithExtremePrejudice();
        	}
        });
        btnTerminate.setPreferredSize(new Dimension(55, 40));
        btnTerminate.setMinimumSize(new Dimension(55, 40));
        btnTerminate.setMaximumSize(new Dimension(55, 40));
        btnTerminate.setMargin(new Insets(2, 2, 2, 2));
        btnTerminate.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnTerminate.setHorizontalAlignment(SwingConstants.LEFT);
        btnTerminate.setFocusable(false);
        btnTerminate.setEnabled(false);
        btnTerminate.setAlignmentX(0.5f);
        panelToggles.add(btnTerminate);
        
		//--------------------------------------//
		// Roles list      
		//--------------------------------------//
        
        // Add roles panel
        panelRoles = new JPanel();
        panelRoles.setBorder(new EmptyBorder(5, 0, 0, 0));
        panelRoles.setLayout(new BorderLayout(0, 0));
        panelRoles.setPreferredSize(new Dimension(300, 85));
        panelRoles.setMinimumSize(new Dimension(300, 85));
        panelRoles.setMaximumSize(new Dimension(300, 85));
        add(panelRoles);
        
        // Add roles label
        labelRoles = new JLabel("Person's Roles");
        labelRoles.setBorder(new EmptyBorder(0, 10, 0, 10));
        labelRoles.setPreferredSize(new Dimension(140, 20));
        labelRoles.setMinimumSize(new Dimension(140, 20));
        labelRoles.setMaximumSize(new Dimension(140, 20));
        labelRoles.setFont(getFont().deriveFont(Font.BOLD));
        panelRoles.add(labelRoles, BorderLayout.NORTH);
        
        // Add roles scroll pane
        scrollRoles = new JScrollPane();
        scrollRoles.setBorder(new LineBorder(SystemColor.menu, 3));
        scrollRoles.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollRoles.setPreferredSize(new Dimension(300, 60));
        scrollRoles.setMinimumSize(new Dimension(300, 60));
        scrollRoles.setMaximumSize(new Dimension(300, 60));
        panelRoles.add(scrollRoles, BorderLayout.CENTER);
       
        // Add roles list
        listRoles = new JList<RoleInterface>(roleListModel);
        listRoles.addListSelectionListener(new ListSelectionListener() {
			/**
			 * When a Role is selected from the list, this is the code that kicks off the GUI update
			 * It also sets up a listener so that the GUI will update with changes for as long as that Role remains selected
			 */
			public void valueChanged(ListSelectionEvent arg0) {
				if (listRoles.getSelectedValue() != null) {
					if (roleSelectedFromList != null) {
						// Remove the listener from the previous selection
						roleSelectedFromList.getPropertyChangeSupport().removePropertyChangeListener(EditPersonTab.this);
					}
					roleSelectedFromList = listRoles.getSelectedValue();
					roleSelectedFromList.getPropertyChangeSupport().addPropertyChangeListener(EditPersonTab.this);
					updateRoleValues();
				}  else {
					toggleEditJob(false);
					setEditJobBlank();
				}
				toggleNewJob(false);
				setNewJobBlank();
			}
		});
        listRoles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listRoles.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 3801676414916520494L;

			/**
			 * Allows RoleInterface objects to be displayed as custom text in the JList
			 */
			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof RoleInterface) {
                    ((JLabel) renderer).setText(((RoleInterface) value).getClass().getName());
                }
                return renderer;
            }
        });
        scrollRoles.setViewportView(listRoles);
        
		//--------------------------------------//
		// Role info    
		//--------------------------------------//
        
        // Create role state area
        panelRoleState = new JPanel();
        panelRoleState.setLayout(new BoxLayout(panelRoleState, BoxLayout.X_AXIS));
        panelRoleState.setPreferredSize(new Dimension(300, 20));
        panelRoleState.setMinimumSize(new Dimension(300, 20));
        panelRoleState.setMaximumSize(new Dimension(300, 20));
        add(panelRoleState);
        
        labelRoleState = new JLabel("State");
        labelRoleState.setPreferredSize(new Dimension(100, 16));
        labelRoleState.setMinimumSize(new Dimension(100, 16));
        labelRoleState.setMaximumSize(new Dimension(100, 16));
        labelRoleState.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelRoleState.add(labelRoleState);
        
        labelRoleStateValue = new JLabel("null");
        labelRoleStateValue.setFont(getFont().deriveFont(Font.BOLD));
        panelRoleState.add(labelRoleStateValue);
        
        // Create role active area
        panelRoleActive = new JPanel();
        panelRoleActive.setLayout(new BoxLayout(panelRoleActive, BoxLayout.X_AXIS));
        panelRoleActive.setPreferredSize(new Dimension(300, 20));
        panelRoleActive.setMinimumSize(new Dimension(300, 20));
        panelRoleActive.setMaximumSize(new Dimension(300, 20));
        add(panelRoleActive);
        
        labelRoleActive = new JLabel("Is Active");
        labelRoleActive.setPreferredSize(new Dimension(100, 16));
        labelRoleActive.setMinimumSize(new Dimension(100, 16));
        labelRoleActive.setMaximumSize(new Dimension(100, 16));
        labelRoleActive.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelRoleActive.add(labelRoleActive);
        
        labelRoleActiveValue = new JLabel("null");
        labelRoleActiveValue.setFont(getFont().deriveFont(Font.BOLD));
        panelRoleActive.add(labelRoleActiveValue);
        
        // Create role activity area
        panelRoleActivity = new JPanel();
        panelRoleActivity.setLayout(new BoxLayout(panelRoleActivity, BoxLayout.X_AXIS));
        panelRoleActivity.setPreferredSize(new Dimension(300, 20));
        panelRoleActivity.setMinimumSize(new Dimension(300, 20));
        panelRoleActivity.setMaximumSize(new Dimension(300, 20));
        add(panelRoleActivity);
        
        labelRoleActivity = new JLabel("Has Activity");
        labelRoleActivity.setPreferredSize(new Dimension(100, 16));
        labelRoleActivity.setMinimumSize(new Dimension(100, 16));
        labelRoleActivity.setMaximumSize(new Dimension(100, 16));
        labelRoleActivity.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelRoleActivity.add(labelRoleActivity);
        
        labelRoleActivityValue = new JLabel("null");
        labelRoleActivityValue.setFont(getFont().deriveFont(Font.BOLD));
        panelRoleActivity.add(labelRoleActivityValue);
        
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
		// Edit role 
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        
        panelEditJob = new JPanel();
        panelEditJob.setLayout(new BoxLayout(panelEditJob, BoxLayout.Y_AXIS));
        add(panelEditJob);

		//--------------------------------------//
		// Salary
		//--------------------------------------//

        panelSalary = new JPanel();
        panelSalary.setSize(new Dimension(300, 20));
        panelSalary.setMinimumSize(new Dimension(300, 20));
        panelSalary.setMaximumSize(new Dimension(300, 20));
        panelSalary.setLayout(new BoxLayout(panelSalary, BoxLayout.X_AXIS));
        panelEditJob.add(panelSalary);
        
        labelSalary = new JLabel("Salary");
        labelSalary.setPreferredSize(new Dimension(100, 16));
        labelSalary.setMinimumSize(new Dimension(100, 16));
        labelSalary.setMaximumSize(new Dimension(100, 16));
        labelSalary.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelSalary.add(labelSalary);
        
        labelSalaryValue = new JLabel("null");
        labelSalaryValue.setFont(getFont().deriveFont(Font.BOLD));
        panelSalary.add(labelSalaryValue);
        
		//--------------------------------------//
		// Shift
		//--------------------------------------//
        
        panelShift1 = new JPanel();
        panelEditJob.add(panelShift1);
        panelShift1.setPreferredSize(new Dimension(300, 30));
        panelShift1.setMinimumSize(new Dimension(300, 30));
        panelShift1.setMaximumSize(new Dimension(300, 30));
        panelShift1.setLayout(new BoxLayout(panelShift1, BoxLayout.X_AXIS));
        
        labelShift1 = new JLabel("Shift");
        labelShift1.setPreferredSize(new Dimension(100, 16));
        labelShift1.setMinimumSize(new Dimension(100, 16));
        labelShift1.setMaximumSize(new Dimension(100, 16));
        labelShift1.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelShift1.add(labelShift1);
        
        labelStart1 = new JLabel("Start");
        labelStart1.setBorder(new EmptyBorder(0, 0, 0, 5));
        panelShift1.add(labelStart1);
        
        spinnerStart1 = new JSpinner();
        spinnerStart1.setPreferredSize(new Dimension(40, 20));
        spinnerStart1.setMinimumSize(new Dimension(40, 20));
        spinnerStart1.setMaximumSize(new Dimension(40, 20));
        panelShift1.add(spinnerStart1);
        
        labelEnd1 = new JLabel("End");
        labelEnd1.setBorder(new EmptyBorder(0, 15, 0, 5));
        panelShift1.add(labelEnd1);
        
        spinnerEnd1 = new JSpinner();
        spinnerEnd1.setPreferredSize(new Dimension(40, 20));
        spinnerEnd1.setMinimumSize(new Dimension(40, 20));
        spinnerEnd1.setMaximumSize(new Dimension(40, 20));
        panelShift1.add(spinnerEnd1);
        
		//--------------------------------------//
		// Revert/Save
		//--------------------------------------//
        
        panelRoleRevertSave = new JPanel();
        panelRoleRevertSave.setPreferredSize(new Dimension(300, 30));
        panelRoleRevertSave.setMinimumSize(new Dimension(300, 30));
        panelRoleRevertSave.setMaximumSize(new Dimension(300, 30));
        panelRoleRevertSave.setLayout(new BoxLayout(panelRoleRevertSave, BoxLayout.X_AXIS));
        panelEditJob.add(panelRoleRevertSave);
        
        buttonRoleRevert = new JButton("Revert");
        buttonRoleRevert.setFocusable(false);
        buttonRoleRevert.setAlignmentX(Component.RIGHT_ALIGNMENT);
        buttonRoleRevert.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		updateRoleValues();
        	}
        });
        panelRoleRevertSave.add(buttonRoleRevert);
        
        buttonRoleSave = new JButton("Save");
        buttonRoleSave.setFocusable(false);
        buttonRoleSave.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		saveChangedJob((JobRole) roleSelectedFromList);
        	}
        });
        panelRoleRevertSave.add(buttonRoleSave);
        
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
		// New role 
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
        
        panelNewJob = new JPanel();
        panelNewJob.setPreferredSize(new Dimension(300, 145));
        panelNewJob.setMinimumSize(new Dimension(300, 145));
        panelNewJob.setMaximumSize(new Dimension(300, 145));
        panelNewJob.setLayout(new BoxLayout(panelNewJob, BoxLayout.Y_AXIS));
        add(panelNewJob);
        
        panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 0, 0, 0));
        panel.setPreferredSize(new Dimension(300, 25));
        panel.setMinimumSize(new Dimension(300, 25));
        panel.setMaximumSize(new Dimension(300, 25));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panelNewJob.add(panel);
        
        label = new JLabel("New Job");
        label.setPreferredSize(new Dimension(140, 20));
        label.setMinimumSize(new Dimension(140, 20));
        label.setMaximumSize(new Dimension(140, 20));
        label.setBorder(new EmptyBorder(0, 10, 0, 10));
        label.setFont(getFont().deriveFont(Font.BOLD));
        panel.add(label);
        
		//--------------------------------------//
		// Job
		//--------------------------------------//
        
        panelJob = new JPanel();
        panelJob.setPreferredSize(new Dimension(300, 30));
        panelJob.setMinimumSize(new Dimension(300, 30));
        panelJob.setMaximumSize(new Dimension(300, 30));
        panelJob.setLayout(new BoxLayout(panelJob, BoxLayout.X_AXIS));
        panelNewJob.add(panelJob);
        
        labelJob = new JLabel("Job");
        labelJob.setBorder(new EmptyBorder(0, 10, 0, 10));
        labelJob.setMaximumSize(new Dimension(100, 16));
        labelJob.setMinimumSize(new Dimension(100, 16));
        labelJob.setPreferredSize(new Dimension(100, 16));
        panelJob.add(labelJob);
        
        comboBoxJob = new JComboBox<String>(jobComboBoxModel);
        comboBoxJob.setFocusable(false);
        comboBoxJob.setPreferredSize(new Dimension(190, 20));
        comboBoxJob.setMinimumSize(new Dimension(190, 20));
        comboBoxJob.setMaximumSize(new Dimension(190, 20));
        comboBoxJob.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		jobSelectedFromComboBox = (String) comboBoxJob.getSelectedItem();
        		updateWorkplaceValues();
        	}
        });
        panelJob.add(comboBoxJob);
        
		//--------------------------------------//
		// Workplace
		//--------------------------------------//
        
        panelWorkplace = new JPanel();
        panelWorkplace.setPreferredSize(new Dimension(300, 30));
        panelWorkplace.setMaximumSize(new Dimension(300, 30));
        panelWorkplace.setMinimumSize(new Dimension(300, 30));
        panelWorkplace.setLayout(new BoxLayout(panelWorkplace, BoxLayout.X_AXIS));
        panelNewJob.add(panelWorkplace);
        
        labelWorkplace = new JLabel("Workplace");
        labelWorkplace.setBorder(new EmptyBorder(0, 10, 0, 10));
        labelWorkplace.setPreferredSize(new Dimension(100, 16));
        labelWorkplace.setMinimumSize(new Dimension(100, 16));
        labelWorkplace.setMaximumSize(new Dimension(100, 16));
        panelWorkplace.add(labelWorkplace);
        
        comboBoxWorkplace = new JComboBox<BuildingInterface>(workplaceComboBoxModel);
        comboBoxWorkplace.setPreferredSize(new Dimension(190, 20));
        comboBoxWorkplace.setMinimumSize(new Dimension(190, 20));
        comboBoxWorkplace.setMaximumSize(new Dimension(190, 20));
        comboBoxWorkplace.setFocusable(false);
        comboBoxWorkplace.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		workplaceSelectedFromComboBox = (BuildingInterface) comboBoxWorkplace.getSelectedItem();
        	}
        });
        comboBoxWorkplace.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 17900884575341448L;
			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (renderer instanceof JLabel && value instanceof BuildingInterface) {
                    ((JLabel) renderer).setText(((BuildingInterface) value).getName());
                }
                return renderer;
            }
        });
        panelWorkplace.add(comboBoxWorkplace);
        
		//--------------------------------------//
		// Shift
		//--------------------------------------//
        
        panelShift2 = new JPanel();
        panelShift2.setPreferredSize(new Dimension(300, 30));
        panelShift2.setMaximumSize(new Dimension(300, 30));
        panelShift2.setMinimumSize(new Dimension(300, 30));
        panelShift2.setLayout(new BoxLayout(panelShift2, BoxLayout.X_AXIS));
        panelNewJob.add(panelShift2);
        
        labelShift2 = new JLabel("Shift");
        labelShift2.setPreferredSize(new Dimension(100, 16));
        labelShift2.setMinimumSize(new Dimension(100, 16));
        labelShift2.setMaximumSize(new Dimension(100, 16));
        labelShift2.setBorder(new EmptyBorder(0, 10, 0, 10));
        panelShift2.add(labelShift2);
        
        labelStart2 = new JLabel("Start");
        labelStart2.setBorder(new EmptyBorder(0, 0, 0, 5));
        panelShift2.add(labelStart2);
        
        spinnerStart2 = new JSpinner();
        spinnerStart2.setPreferredSize(new Dimension(40, 20));
        spinnerStart2.setMinimumSize(new Dimension(40, 20));
        spinnerStart2.setMaximumSize(new Dimension(40, 20));
        panelShift2.add(spinnerStart2);
        
        labelEnd2 = new JLabel("End");
        labelEnd2.setBorder(new EmptyBorder(0, 15, 0, 5));
        panelShift2.add(labelEnd2);
        
        spinnerEnd2 = new JSpinner();
        spinnerEnd2.setPreferredSize(new Dimension(40, 20));
        spinnerEnd2.setMinimumSize(new Dimension(40, 20));
        spinnerEnd2.setMaximumSize(new Dimension(40, 20));
        spinnerEnd2.setValue(12);
        panelShift2.add(spinnerEnd2);
        
		//--------------------------------------//
		// Add
		//--------------------------------------//
        
        panelAdd = new JPanel();
        panelAdd.setPreferredSize(new Dimension(300, 30));
        panelAdd.setMinimumSize(new Dimension(300, 30));
        panelAdd.setMaximumSize(new Dimension(300, 30));
        panelAdd.setLayout(new BoxLayout(panelAdd, BoxLayout.X_AXIS));
        panelNewJob.add(panelAdd);
        
        buttonAdd = new JButton("Add");
        buttonAdd.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		saveNewJob();
        	}
        });
        buttonAdd.setFocusable(false);
        buttonAdd.setAlignmentX(1.0f);
        panelAdd.add(buttonAdd);
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
		// Finish setup
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
		
        // Make sure that there is a building shown (if one exists) for whatever role is selected
        jobSelectedFromComboBox = (String) comboBoxJob.getSelectedItem();
        // Disable the buttons until a Person is selected
        toggleButtons(false);
        // Disable the job editor until a JobRole is selected
        toggleEditJob(false);
        // Disable the new job form
        toggleNewJob(false);
	}
	
	//============================================================================//
	// Listeners     
    //============================================================================//
	
	/**
	 * This updates the list when people are added and removed
	 * This updates the GUI when the currently selected Person from the list is changed
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
        	// A person has been added or removed from the list
            if (DataModel.PEOPLE.equals(evt.getPropertyName())) {
                if (evt.getOldValue() != null && evt.getNewValue() == null) {
                    peopleListModel.removeElement(evt.getOldValue());
                } else if (evt.getOldValue() == null && evt.getNewValue() != null) {
                    peopleListModel.addElement((Person) evt.getNewValue());
                }
            }
            // A building has been added or removed from the combo box list
            if (DataModel.BUILDINGS.equals(evt.getPropertyName())) {
            	// Rather than updating the model directly, use this method.
            	// For some reason you can't iterate over the whole model. Since we need to show/hide
            	// buildings based on which role is selected, that is a problem.
            	updateWorkplaceValues();
            }
        }
        if (evt.getSource() == personSelectedFromList) {
        	if (Person.STATE.equals(evt.getPropertyName())) {
        		updatePersonValues();
        	}
        	if (Person.ROLES.equals(evt.getPropertyName())) {
                if (evt.getOldValue() != null && evt.getNewValue() == null) {
                    roleListModel.removeElement(evt.getOldValue());
                    if (roleSelectedFromList == evt.getOldValue()) {
                    	toggleEditJob(false);
                    	setEditJobBlank();
                    }
                } else if (evt.getOldValue() == null && evt.getNewValue() != null) {
                    roleListModel.addElement((RoleInterface) evt.getNewValue());
                }
        	}
        }
    }
    
	//============================================================================//
	// Messages   
    //============================================================================//
    
	public void displayPerson(Person p) {
		listPeople.setSelectedValue(p, true);
	}
    
	//============================================================================//
	// Actions   
    //============================================================================//
	
	//--------------------------------------//
	// Saves
	//--------------------------------------//
	
	/**
	 * When the save button is clicked, this code saves the changed data about the person
	 */
	private void saveChangedPerson() {
		personSelectedFromList.setName(textName.getText());
		try {
			textCash.commitEdit();
		} catch (ParseException e) {}
		personSelectedFromList.setCash(Integer.parseInt(textCash.getValue().toString()));
		listPeople.repaint();
	}
	
	private void saveChangedJob(JobRoleInterface r) {
		try {
			spinnerStart1.commitEdit();
			spinnerEnd1.commitEdit();
		} catch (ParseException e) {}
		r.setShift((Integer) spinnerStart1.getValue(), (Integer) spinnerEnd1.getValue());
	}
	
	private void saveNewJob() {
		if (jobSelectedFromComboBox != null && workplaceSelectedFromComboBox != null) {
			try {
				spinnerStart2.commitEdit();
				spinnerEnd2.commitEdit();
			} catch (ParseException e) {}
			try {
				Class<?> c0 = Class.forName(jobSelectedFromComboBox);
				Class<?> c1 = Class.forName(workplaceSelectedFromComboBox.getBuildingClassName());
				Constructor<?> r0 = c0.getConstructor(c1, Integer.TYPE, Integer.TYPE);
				JobRole j0 = (JobRole) r0.newInstance(workplaceSelectedFromComboBox, (Integer) spinnerStart2.getValue(), (Integer) spinnerEnd2.getValue());
				personSelectedFromList.setOccupation(j0);
			} catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			btnHasJob.setIcon(job);
			toggleNewJob(false);
			setNewJobBlank();
		}
	}
	
	private void terminateWithExtremePrejudice() {
		personSelectedFromList.terminateWithExtremePrejudice();
	}
	
	//--------------------------------------//
	// Toggle button handlers
	//--------------------------------------//
	
	private void togglePersonCar() {
		if (personSelectedFromList.getCar() == null) {
			CarAgent c = new CarAgent(personSelectedFromList.getHome(), personSelectedFromList);
			CarAnimation ca = new CarAnimation(c, personSelectedFromList.getHome());
			c.setAnimation(ca);
			Application.getMainFrame().cityView.addAnimation(ca);
			btnHasCar.setIcon(car);
		} else {
			personSelectedFromList.getCar().stopThread();
			CarAnimation ca = (CarAnimation) personSelectedFromList.getCar().getAnimation();
			Application.getMainFrame().cityView.removeAnimation(ca);
			personSelectedFromList.setCar(null);
			btnHasCar.setIcon(nocar);
		}
	}
	
// TODO
//	private void togglePersonHome() {
//		if (btnHasHouse.getIcon().equals(house)) {
//			btnHasHouse.setIcon(apartment);
//		} else {
//			btnHasHouse.setIcon(house);
//		}
//		//listPeopleSelection;
//		return;
//	}
	
	private void togglePersonJob() {
		Person p = personSelectedFromList;
		if (p.getOccupation() != null) {
			p.setOccupation(null);
			// Must check to see if it worked (can't remove while active)
			if (p.getOccupation() == null)
				btnHasJob.setIcon(nojob);
		} else {
			setNewJobBlank();
			toggleNewJob(true);
		}
		return;
	}
	
	//============================================================================//
	// Utilities    
    //============================================================================//
	
	//--------------------------------------//
	// Update forms
	//--------------------------------------//
    
	/**
	 * When a new Person is selected (or the selected person changes) this code updates the data
	 * When the revert button is clicked, this code loads the current data about the person
	 */
	private void updatePersonValues() {
		Person p = personSelectedFromList;
		labelStateValue.setText(p.getState().toString());
		textName.setText(p.getName());
		textCash.setValue(p.getCash());
		if (p.getCar() != null) {
			btnHasCar.setIcon(car);
		} else {
			btnHasCar.setIcon(nocar);
		}
		if (p.getHome() != null) {
			lblResidence.setText(p.getHome().getName());
		}
// TODO
//		if (p.getHome() != null) {
//			if (p.getHome() instanceof HouseBuilding) {
//				btnHasHouse.setIcon(house);
//			} else {
//				btnHasHouse.setIcon(apartment);
//			}
//		} else {
//			btnHasHouse.setIcon(house);
//			btnHasHouse.setEnabled(false);
//		}
		if (p.getOccupation() != null) {
			btnHasJob.setIcon(job);
		} else {
			btnHasJob.setIcon(nojob);
		}
		roleListModel.clear();
		for (RoleInterface r : p.getRoles()) {
			roleListModel.addElement(r);
		}
		toggleButtons(true);
	}
	
	private void updateRoleValues() {
		RoleInterface r = roleSelectedFromList;
		labelRoleStateValue.setText(r.getStateString());
		labelRoleActiveValue.setText(String.valueOf(r.getActive()));
		labelRoleActivityValue.setText(String.valueOf(r.getActivity()));
		if (r instanceof JobRoleInterface) {
			JobRoleInterface jr = (JobRole) r;
			toggleEditJob(true);
			labelSalaryValue.setText(currencyFormat.format(jr.getSalary()));
			try {
				spinnerStart1.commitEdit();
				spinnerEnd1.commitEdit();
			} catch (ParseException e) {}
			spinnerStart1.setValue(jr.getShiftStart());
			spinnerEnd1.setValue(jr.getShiftEnd());
		} else {
			toggleEditJob(false);
		}
	}
	
	private void updateWorkplaceValues() {
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
	// Blank forms
	//--------------------------------------//
	
	private void setEditPersonBlank() {
		labelStateValue.setText("null");
		textName.setText("");
		textCash.setText("");
		btnHasCar.setIcon(nocar);
		// btnHasHouse.setIcon(house); // TODO
		lblResidence.setText("null");
		btnHasJob.setIcon(nojob);
		roleListModel.clear();
		toggleButtons(false);
		setEditJobBlank();
	}
	
	private void setEditJobBlank() {
		labelRoleStateValue.setText("null");
		labelRoleActiveValue.setText("null");
		labelRoleActivityValue.setText("null");
		labelSalaryValue.setText("null");
		toggleEditJob(false);
	}
	
	private void setNewJobBlank() {
		comboBoxJob.setSelectedIndex(0);
	}
	
	//--------------------------------------//
	// Toggle views
	//--------------------------------------//
	
	private void toggleButtons(boolean b) {
		btnRevert.setEnabled(b);
		btnSave.setEnabled(b);
		btnHouse.setEnabled(false);
		btnWork.setEnabled(false);
		btnHasCar.setEnabled(b);
		// btnHasHouse.setEnabled(false); // TODO
		btnHasJob.setEnabled(false);
		// btnSleep.setEnabled(b); // TODO
		btnTerminate.setEnabled(b);
		
		if (b && personSelectedFromList != null) {
			if (personSelectedFromList.getHome() != null) {
				btnHouse.setEnabled(b);
				// btnHasHouse.setEnabled(b); // TODO
				btnHasJob.setEnabled(b);
			}
			if (personSelectedFromList.getOccupation() != null) {
				btnWork.setEnabled(b);
				if (personSelectedFromList.getOccupation().getActive()) {
					btnHasJob.setEnabled(false);
				} else {
					btnHasJob.setEnabled(true);
				}
			}
		}
	}
	
	private void toggleEditJob(boolean b) {
		panelEditJob.setVisible(b);
	}
	
	private void toggleNewJob(boolean b) {
		updateWorkplaceValues();
		panelNewJob.setVisible(b);
	}
	
	private void goToPersonLocation(Person p){
		if(p.getCurrentLocation() != null){
			Application.getMainFrame().buildingView.setView(p.getCurrentLocation().getCityViewBuilding().getID()); // visual go-to
			p.getCurrentLocation().getPanel().repaint(); // equivalent to ActionPerformed for building panels
		}
	}

}
