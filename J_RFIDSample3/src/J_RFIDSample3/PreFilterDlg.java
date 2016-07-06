package J_RFIDSample3;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import com.mot.rfid.api3.*;

public class PreFilterDlg {

    PreFilters.PreFilter preFilter1;
    PreFilters.PreFilter preFilter2;

	private Shell sShell = null;
	private TabFolder tabFolder = null;
	private Composite tabPanel1 = null;
	private Composite panelPreFilter1 = null;
	private Label labelAntennaID1 = null;
	private Combo comboAntennaID1 = null;
	private Button checkBoxUseFilter1 = null;
	private Label labelMemBank1 = null;
	private Combo comboMemBank1 = null;
	private Label labelBitOffset1 = null;
	private Text textBitOffset1 = null;
	private Text textTagPattern1 = null;
	private Label labelTagPattern1 = null;
	private Label labelFilterAction1 = null;
	private Combo comboFilterAction1 = null;
	private Label labelAction1 = null;
	private Combo comboAction1 = null;
	private Label labelTarget1 = null;
	private Combo comboTarget1 = null;
	private Button buttonApply = null;
	private Composite tabPanel2 = null;
	private Composite panelPreFilter2 = null;
	private Label labelAntennaID2 = null;
	private Combo comboAntennaID2 = null;
	private Button checkBoxUseFilter2 = null;
	private Label labelMemBank2 = null;
	private Combo comboMemBank2 = null;
	private Label labelBitOffset2 = null;
	private Text textBitOffset2 = null;
	private Text textTagPattern2 = null;
	private Label labelTagPattern2 = null;
	private Label labelFilterAction2 = null;
	private Combo comboFilterAction2 = null;
	private Label labelAction2 = null;
	private Combo comboAction2 = null;
	private Label labelTarget2 = null;
	private Combo comboTarget2 = null;
	
    String[] stateAwareAction = new String[] {
            "Inv A Not Inv B",
            "Asrt SL Not Dsrt SL",
            "Inv A",
            "Asrt SL",
            "Not Inv B",
            "Not Dsrt SL",
            "Inv A2BB2A Not Inv B",
            "Neg SL Not Asrt SL",
            "Inv B Not Inv A",
            "Dsrt SL Not Asrt SL",
            "Inv B",
            "Dsrt SL",
            "Not Inv A",
            "Not Asrt SL",
            "Not Inv A2BB2A",
            "Not Neg SL"

            };

    String[] stateUnawareAction = new String[] {
            "Select Not Unselect",
            "Select",
            "Not Unselect",
            "Unselect",
            "Unselect Not Select",
            "Not Select"

            };



	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		sShell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//sShell = new Shell();
		sShell.setText("Pre-Filters");
		sShell.setLayout(null);
		createTabFolder();
		sShell.setSize(new Point(240, 188));
		buttonApply = new Button(sShell, SWT.NONE);
		buttonApply.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		buttonApply.setSize(new Point(45, 17));
		buttonApply.setLocation(new Point(187, 145));
		buttonApply.setText("Apply");
	}
	
	public PreFilterDlg(Display display) {
		createSShell(display);
        preFilter1 = RFIDMainDlg.rfidBase.preFilter1;
        preFilter2= RFIDMainDlg.rfidBase.preFilter2;
	    
        // initialize Pre-Filter from the global setting
        initializePreFilter();

        // Add Selection Event Handler for Filter Action 1
        comboFilterAction1.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				updateFilterAction1();
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

        // Add Selection Event Handler for Filter Action 2
        comboFilterAction2.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				updateFilterAction2();
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

        // Apply Button Handler
        buttonApply.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				applyPreFilters();
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		sShell.open();
	}

	private void initializePreFilter()
	{
        short[] antennaList = RFIDMainDlg.rfidBase.getMyReader().Config.Antennas.getAvailableAntennas();

        // Add antennas
        for (int index = 0; index < antennaList.length; index++)
            comboAntennaID1.add(String.valueOf(antennaList[index]));

        for (int index = 0; index < antennaList.length; index++)
        	comboAntennaID2.add(String.valueOf(antennaList[index]));

       // set the antenna selection
        int antennaIDsel1 = preFilter1.getAntennaID();
        if (antennaIDsel1 > 0) antennaIDsel1--;
        comboAntennaID1.select(antennaIDsel1);

        int antennaIDsel2 = preFilter2.getAntennaID();
        if (antennaIDsel2 > 0) antennaIDsel2--;
        comboAntennaID2.select(antennaIDsel2);

        // Set the Offset
        textBitOffset1.setText(String.valueOf(preFilter1.getBitOffset()));
        textBitOffset2.setText(String.valueOf(preFilter2.getBitOffset()));

        // Memory Bank
        int memoryBank1 = RFIDMainDlg.rfidBase.preFilter1.getMemoryBank().getValue();
        if (memoryBank1 > 0) memoryBank1 -= 1;
        comboMemBank1.select(memoryBank1);    
            
        int memoryBank2 = RFIDMainDlg.rfidBase.preFilter2.getMemoryBank().getValue();
        if (memoryBank2 > 0)memoryBank2 -= 1;
        comboMemBank2.select(memoryBank2);

        
        // Set the Tag Pattern
        if (RFIDMainDlg.rfidBase.preFilterTagPattern1 == null)
            textTagPattern1.setText("");
        else
        	textTagPattern1.setText(RFIDMainDlg.rfidBase.preFilterTagPattern1);

        if (RFIDMainDlg.rfidBase.preFilterTagPattern2 == null)
        	textTagPattern2.setText("");
        else
        	textTagPattern2.setText(RFIDMainDlg.rfidBase.preFilterTagPattern2);

        // Use filter check box
        checkBoxUseFilter1.setSelection(RFIDMainDlg.rfidBase.isPreFilterSet1);
        checkBoxUseFilter2.setSelection(RFIDMainDlg.rfidBase.isPreFilterSet2);

        // Filter Action
        comboFilterAction1.select(preFilter1.getFilterAction().getValue());
        updateFilterAction1();
        
        comboFilterAction2.select(preFilter2.getFilterAction().getValue());
        updateFilterAction2();

	}
	/**
	 * This method initializes tabFolder	
	 *
	 */
	private void createTabFolder() {
		tabFolder = new TabFolder(sShell, SWT.BOTTOM);
		tabFolder.setLocation(new Point(0, 0));
		tabFolder.setSize(new Point(234, 143));
		createTabPanel1();
		createTabPanel2();
		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("PreFilter 1");
		tabItem.setControl(tabPanel1);
		TabItem tabItem2 = new TabItem(tabFolder, SWT.NONE);
		tabItem2.setText("PreFilter 2");
		tabItem2.setControl(tabPanel2);   
	}

	/**
	 * This method initializes tabPanel1	
	 *
	 */
	private void createTabPanel1() {
		tabPanel1 = new Composite(tabFolder, SWT.NONE);
		tabPanel1.setLayout(null);
		createPanelPreFilter1();
	}

	/**
	 * This method initializes panelPreFilter1	
	 *
	 */
	private void createPanelPreFilter1() {
		panelPreFilter1 = new Composite(tabPanel1, SWT.NONE);
		panelPreFilter1.setLayout(null);
		panelPreFilter1.setBounds(new Rectangle(0, -3, 223, 119));
		labelAntennaID1 = new Label(panelPreFilter1, SWT.NONE);
		labelAntennaID1.setBounds(new Rectangle(5, 9, 52, 13));
		labelAntennaID1.setText("AntennaID");
		labelAntennaID1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		createComboAntennaID1();
		checkBoxUseFilter1 = new Button(panelPreFilter1, SWT.CHECK);
		checkBoxUseFilter1.setBounds(new Rectangle(138, 7, 83, 16));
		checkBoxUseFilter1.setText("Use Filter 1");
		checkBoxUseFilter1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelMemBank1 = new Label(panelPreFilter1, SWT.NONE);
		labelMemBank1.setLocation(new Point(5, 35));
		labelMemBank1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelMemBank1.setText("Mem Bank");
		labelMemBank1.setSize(new Point(52, 13));
		createComboMemBank1();
		labelBitOffset1 = new Label(panelPreFilter1, SWT.NONE);
		labelBitOffset1.setLocation(new Point(134, 35));
		labelBitOffset1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelBitOffset1.setText("Bit Offset");
		labelBitOffset1.setSize(new Point(48, 13));
		textBitOffset1 = new Text(panelPreFilter1, SWT.BORDER);
		textBitOffset1.setLocation(new Point(184, 31));
		textBitOffset1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		textBitOffset1.setSize(new Point(39, 19));
		textTagPattern1 = new Text(panelPreFilter1, SWT.BORDER);
		textTagPattern1.setLocation(new Point(62, 54));
		textTagPattern1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		textTagPattern1.setSize(new Point(160, 19));
		labelTagPattern1 = new Label(panelPreFilter1, SWT.WRAP);
		labelTagPattern1.setLocation(new Point(5, 53));
		labelTagPattern1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelTagPattern1.setText("Tag Pattern (Hex)");
		labelTagPattern1.setSize(new Point(51, 25));
		labelFilterAction1 = new Label(panelPreFilter1, SWT.NONE);
		labelFilterAction1.setLocation(new Point(5, 80));
		labelFilterAction1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelFilterAction1.setText("Filter Action");
		labelFilterAction1.setSize(new Point(53, 19));
		createComboFilterAction1();
		labelAction1 = new Label(panelPreFilter1, SWT.NONE);
		labelAction1.setLocation(new Point(6, 102));
		labelAction1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelAction1.setText("Action");
		labelAction1.setSize(new Point(34, 13));
		createComboAction1();
		labelTarget1 = new Label(panelPreFilter1, SWT.NONE);
		labelTarget1.setLocation(new Point(140, 101));
		labelTarget1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelTarget1.setText("Target");
		labelTarget1.setSize(new Point(34, 13));
		createComboTarget1();
	}

	/**
	 * This method initializes comboAntennaID1	
	 *
	 */
	private void createComboAntennaID1() {
		comboAntennaID1 = new Combo(panelPreFilter1, SWT.READ_ONLY);
		comboAntennaID1.setLocation(new Point(62, 5));
		comboAntennaID1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		comboAntennaID1.setSize(new Point(69, 21));
	}

	/**
	 * This method initializes comboMemBank1	
	 *
	 */
	private void createComboMemBank1() {
		comboMemBank1 = new Combo(panelPreFilter1, SWT.READ_ONLY);
		comboMemBank1.setLocation(new Point(62, 30));
		comboMemBank1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		comboMemBank1.setSize(new Point(69, 21));
		comboMemBank1.setItems(new String[] { "EPC", "TID", "USER" });
	}

	/**
	 * This method initializes comboFilterAction1	
	 *
	 */
	private void createComboFilterAction1() {
		comboFilterAction1 = new Combo(panelPreFilter1, SWT.READ_ONLY);
		comboFilterAction1.setLocation(new Point(62, 76));
		comboFilterAction1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		comboFilterAction1.setSize(new Point(105, 21));
		comboFilterAction1.setItems(new String[] { "Default", "State Aware", "State Unaware" });
	}

	/**
	 * This method initializes comboAction1	
	 *
	 */
	private void createComboAction1() {
		comboAction1 = new Combo(panelPreFilter1, SWT.READ_ONLY);
		comboAction1.setLocation(new Point(43, 98));
		comboAction1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		comboAction1.setSize(new Point(92, 21));
	}

	/**
	 * This method initializes comboTarget1	
	 *
	 */
	private void createComboTarget1() {
		comboTarget1 = new Combo(panelPreFilter1, SWT.READ_ONLY);
		comboTarget1.setLocation(new Point(176, 98));
		comboTarget1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		comboTarget1.setSize(new Point(45, 21));
		comboTarget1.setItems(new String[] { "SL", "S0", "S1", "S2", "S3" });
	}

	/**
	 * This method initializes tabPanel2	
	 *
	 */
	private void createTabPanel2() {
		tabPanel2 = new Composite(tabFolder, SWT.NONE);
		tabPanel2.setLayout(null);
		createPanelPreFilter2();
	}

	/**
	 * This method initializes panelPreFilter2	
	 *
	 */
	private void createPanelPreFilter2() {
		panelPreFilter2 = new Composite(tabPanel2, SWT.NONE);
		panelPreFilter2.setLayout(null);
		panelPreFilter2.setBounds(new Rectangle(0, -3, 223, 119));
		labelAntennaID2 = new Label(panelPreFilter2, SWT.NONE);
		labelAntennaID2.setBounds(new Rectangle(5, 9, 52, 13));
		labelAntennaID2.setText("AntennaID");
		labelAntennaID2.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		createComboAntennaID2();
		checkBoxUseFilter2 = new Button(panelPreFilter2, SWT.CHECK);
		checkBoxUseFilter2.setBounds(new Rectangle(138, 7, 83, 16));
		checkBoxUseFilter2.setText("Use Filter 2");
		checkBoxUseFilter2.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelMemBank2 = new Label(panelPreFilter2, SWT.NONE);
		labelMemBank2.setLocation(new Point(5, 35));
		labelMemBank2.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelMemBank2.setText("Mem Bank");
		labelMemBank2.setSize(new Point(52, 13));
		createComboMemBank2();
		labelBitOffset2 = new Label(panelPreFilter2, SWT.NONE);
		labelBitOffset2.setLocation(new Point(134, 35));
		labelBitOffset2.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelBitOffset2.setText("Bit Offset");
		labelBitOffset2.setSize(new Point(48, 13));
		textBitOffset2 = new Text(panelPreFilter2, SWT.BORDER);
		textBitOffset2.setLocation(new Point(184, 31));
		textBitOffset2.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		textBitOffset2.setSize(new Point(39, 19));
		textTagPattern2 = new Text(panelPreFilter2, SWT.BORDER);
		textTagPattern2.setLocation(new Point(62, 54));
		textTagPattern2.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		textTagPattern2.setSize(new Point(160, 19));
		labelTagPattern2 = new Label(panelPreFilter2, SWT.WRAP);
		labelTagPattern2.setLocation(new Point(5, 53));
		labelTagPattern2.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelTagPattern2.setText("Tag Pattern (Hex)");
		labelTagPattern2.setSize(new Point(51, 25));
		labelFilterAction2 = new Label(panelPreFilter2, SWT.NONE);
		labelFilterAction2.setLocation(new Point(5, 80));
		labelFilterAction2.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelFilterAction2.setText("Filter Action");
		labelFilterAction2.setSize(new Point(53, 19));
		createComboFilterAction2();
		labelAction2 = new Label(panelPreFilter2, SWT.NONE);
		labelAction2.setLocation(new Point(6, 102));
		labelAction2.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelAction2.setText("Action");
		labelAction2.setSize(new Point(34, 13));
		createComboAction2();
		labelTarget2 = new Label(panelPreFilter2, SWT.NONE);
		labelTarget2.setLocation(new Point(140, 101));
		labelTarget2.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelTarget2.setText("Target");
		labelTarget2.setSize(new Point(34, 13));
		createComboTarget2();
	}

	/**
	 * This method initializes comboAntennaID2	
	 *
	 */
	private void createComboAntennaID2() {
		comboAntennaID2 = new Combo(panelPreFilter2, SWT.READ_ONLY);
		comboAntennaID2.setLocation(new Point(62, 5));
		comboAntennaID2.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		comboAntennaID2.setSize(new Point(69, 21));
	}

	/**
	 * This method initializes comboMemBank2	
	 *
	 */
	private void createComboMemBank2() {
		comboMemBank2 = new Combo(panelPreFilter2, SWT.READ_ONLY);
		comboMemBank2.setLocation(new Point(62, 30));
		comboMemBank2.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		comboMemBank2.setSize(new Point(69, 21));
		comboMemBank2.setItems(new String[] { "EPC", "TID", "USER" });

	}

	/**
	 * This method initializes comboFilterAction2	
	 *
	 */
	private void createComboFilterAction2() {
		comboFilterAction2 = new Combo(panelPreFilter2, SWT.READ_ONLY);
		comboFilterAction2.setLocation(new Point(62, 76));
		comboFilterAction2.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		comboFilterAction2.setSize(new Point(105, 21));
		comboFilterAction2.setItems(new String[] { "Default", "State Aware", "State Unaware" });
	}

	/**
	 * This method initializes comboAction2	
	 *
	 */
	private void createComboAction2() {
		comboAction2 = new Combo(panelPreFilter2, SWT.READ_ONLY);
		comboAction2.setLocation(new Point(43, 98));
		comboAction2.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		comboAction2.setSize(new Point(92, 21));
	}

	/**
	 * This method initializes comboTarget2	
	 *
	 */
	private void createComboTarget2() {
		comboTarget2 = new Combo(panelPreFilter2, SWT.READ_ONLY);
		comboTarget2.setLocation(new Point(176, 98));
		comboTarget2.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		comboTarget2.setSize(new Point(45, 21));
		comboTarget2.setItems(new String[] { "SL", "S0", "S1", "S2", "S3" });
	}
	
    void setStateAwareParams(int filterAction)
    {
        switch (filterAction)
        {
            // Default
            case 0:
                break;
            // State Aware
            case 1:
                {
                     preFilter1.StateAwareAction.setStateAwareAction(getStateAwareAction(comboAction1.getSelectionIndex()));
                     preFilter2.StateAwareAction.setStateAwareAction(getStateAwareAction(comboAction2.getSelectionIndex()));
                     
                     preFilter1.StateAwareAction.setTarget(getStateAwareTarget(comboTarget1.getSelectionIndex()));
                     preFilter2.StateAwareAction.setTarget(getStateAwareTarget(comboTarget2.getSelectionIndex()));
                }
                break;
            // State UnAware
            case 2:
                {
                     preFilter1.StateUnawareAction.setStateUnawareAction(getStateUnawareAction(comboAction1.getSelectionIndex()));
                     preFilter2.StateUnawareAction.setStateUnawareAction(getStateUnawareAction(comboAction2.getSelectionIndex()));

                }
                break;
        }
    }
    
    STATE_AWARE_ACTION getStateAwareAction(int actionIndex)
    {
        STATE_AWARE_ACTION stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_ASRT_SL_NOT_DSRT_SL;
        switch (actionIndex)
        {
            case 0:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_A_NOT_INV_B;
                break;
            case 1:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_ASRT_SL_NOT_DSRT_SL;
                break;
            case 2:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_A;
                break;
            case 3:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_ASRT_SL;
                break;
            case 4:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_NOT_INV_B;           
                break;
            case 5:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_NOT_DSRT_SL;            
                break;
            case 6:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_A2BB2A;            
                break;
            case 7:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_NEG_SL;          
                break;
            case 8:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B_NOT_INV_A;           
                break;
            case 9:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL_NOT_ASRT_SL;           
                break;
            case 10:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_INV_B;
                break;
            case 11:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_DSRT_SL;           
                break;
            case 12:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_NOT_INV_A;           
                break;
            case 13:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_NOT_ASRT_SL;           
                break;
            case 14:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_NOT_INV_A2BB2A;           
                break;
            case 15:
                stateAwareAction = STATE_AWARE_ACTION.STATE_AWARE_ACTION_NOT_NEG_SL;           
                break;
        }
        return stateAwareAction;
    }

    STATE_UNAWARE_ACTION getStateUnawareAction(int index)
    {
        STATE_UNAWARE_ACTION action = STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_NOT_SELECT;
        switch (index)
        {
        case 0:
            action = STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_SELECT_NOT_UNSELECT;
            break;
        case 1:
            action = STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_SELECT;
            break;
        case 2:
            action = STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_NOT_UNSELECT;
        case 3:
            action = STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_UNSELECT;
        case 4:
            action = STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_UNSELECT_NOT_SELECT;
        case 5:
            action = STATE_UNAWARE_ACTION.STATE_UNAWARE_ACTION_NOT_SELECT;
        }
        return action;
    }

    TARGET getStateAwareTarget(int index)
    {
        TARGET target = TARGET.TARGET_SL;
        switch (index)
        {
            case 0:
                target = TARGET.TARGET_SL;
                break;
            case 1:
                target = TARGET.TARGET_INVENTORIED_STATE_S0;
                break;
            case 2:
                target = TARGET.TARGET_INVENTORIED_STATE_S1;;
                break;
            case 3:
                target = TARGET.TARGET_INVENTORIED_STATE_S2;
                break;
            case 4:
                target = TARGET.TARGET_INVENTORIED_STATE_S3;
                break;

        }
        return target;
    }

    FILTER_ACTION getFilterAction(int index)
    {
        FILTER_ACTION filterAction = FILTER_ACTION.FILTER_ACTION_DEFAULT;
        switch (index) {
            case 0:
                filterAction = FILTER_ACTION.FILTER_ACTION_DEFAULT;
                break;
            case 1:
                filterAction = FILTER_ACTION.FILTER_ACTION_STATE_AWARE;
                break;
            case 2:
                filterAction = FILTER_ACTION.FILTER_ACTION_STATE_UNAWARE;
                break;
        }
        return filterAction;
    }

    void applyPreFilters()
    {
        // Create New PreFilter
        preFilter1 = RFIDMainDlg.rfidBase.preFilters.new PreFilter();
        preFilter2 = RFIDMainDlg.rfidBase.preFilters.new PreFilter();
        RFIDMainDlg.rfidBase.preFilter1 = preFilter1;
        RFIDMainDlg.rfidBase.preFilter2 = preFilter2;

        // Pre-Filter1
        short antennaID = (short)comboAntennaID1.getSelectionIndex();
        antennaID++;
        byte[] tagPattern = null;
        preFilter1.setAntennaID(antennaID);
        preFilter1.setMemoryBank(RFIDMainDlg.rfidBase.getMemoryBankEnum(comboMemBank1.getSelectionIndex() + 1));
        tagPattern = RFIDBase.hexStringToByteArray(textTagPattern1.getText());
        preFilter1.setTagPattern(tagPattern);
        preFilter1.setBitOffset(Integer.parseInt(textBitOffset1.getText()));
        preFilter1.setTagPatternBitCount(tagPattern.length * 8);
        RFIDMainDlg.rfidBase.preFilterTagPattern1 = textTagPattern1.getText();
        RFIDMainDlg.rfidBase.preFilterActionIndex1 = comboAction1.getSelectionIndex();

        // Pre-Filter2
        short antennaID1 = (short)comboAntennaID2.getSelectionIndex();
        antennaID1++;
        tagPattern = null;
        preFilter2.setAntennaID(antennaID1);
        preFilter2.setMemoryBank(RFIDMainDlg.rfidBase.getMemoryBankEnum(comboMemBank2.getSelectionIndex() + 1));
        tagPattern = RFIDBase.hexStringToByteArray(textTagPattern2.getText());
        preFilter2.setTagPattern(tagPattern);
        preFilter2.setBitOffset(Integer.parseInt(textBitOffset2.getText()));
        preFilter2.setTagPatternBitCount(tagPattern.length * 8);
        RFIDMainDlg.rfidBase.preFilterTagPattern2 = textTagPattern2.getText();
        RFIDMainDlg.rfidBase.preFilterActionIndex2 = comboAction2.getSelectionIndex();

        // Set the filter Action
        preFilter1.setFilterAction(getFilterAction(comboFilterAction1.getSelectionIndex()));
        preFilter2.setFilterAction(getFilterAction(comboFilterAction2.getSelectionIndex()));
        
        // Set the state Aware Params
        setStateAwareParams(comboFilterAction1.getSelectionIndex());
        setStateAwareParams(comboFilterAction2.getSelectionIndex());

        RFIDMainDlg.rfidBase.isPreFilterSet1 = checkBoxUseFilter1.getSelection();
        RFIDMainDlg.rfidBase.isPreFilterSet2 = checkBoxUseFilter2.getSelection();


        try {
            // check pre-filter is set
            RFIDMainDlg.rfidBase.getMyReader().Actions.PreFilters.deleteAll();

            // Pre Filter 1
            if (checkBoxUseFilter1.getSelection())
                RFIDMainDlg.rfidBase.getMyReader().Actions.PreFilters.add(preFilter1);

            // Pre Filter 2
            if (checkBoxUseFilter2.getSelection())
                RFIDMainDlg.rfidBase.getMyReader().Actions.PreFilters.add(preFilter2);


       } catch (InvalidUsageException ex) {
            RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.PARAM_ERROR, ex.getVendorMessage());
        } catch (OperationFailureException ex) {
            RFIDMainDlg.rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
        }
    }
    
    void updateFilterAction1()
    {
        // Default Filter Action
        if (comboFilterAction1.getSelectionIndex() == 0)
        {
            comboAction1.setEnabled(false);
            comboTarget1.setEnabled(false);
        }


        // State Aware Filter Action
        if (comboFilterAction1.getSelectionIndex() == 1)
        {
            loadActionCombo1(true);
            comboAction1.setEnabled(true);
            comboTarget1.setEnabled(true);
            comboAction1.select(RFIDMainDlg.rfidBase.preFilterActionIndex1);
            comboTarget1.select(RFIDMainDlg.rfidBase.preFilter1.StateAwareAction.getTarget().getValue());

        }
        // State UnAware Filter Action
        if (comboFilterAction1.getSelectionIndex() == 2)
        {
            loadActionCombo1(false);
            comboAction1.setEnabled(true);
            comboTarget1.setEnabled(false);


            // Set selected index
            comboAction1.select(RFIDMainDlg.rfidBase.preFilter1.StateUnawareAction.getStateUnawareAction().getValue());

        }
    }

    void updateFilterAction2()
    {
        // Default Filter Action
        if (comboFilterAction2.getSelectionIndex() == 0)
        {
            comboAction2.setEnabled(false);
            comboTarget2.setEnabled(false);
        }

        // State Aware Filter Action
        if(comboFilterAction2.getSelectionIndex() == 1)
        {
            loadActionCombo2(true);
            comboAction2.setEnabled(true);
            comboTarget2.setEnabled(true);
            // Set selected index  - Action
            comboAction2.select(RFIDMainDlg.rfidBase.preFilterActionIndex2);

            // Set selected index  - Target
            comboTarget2.select(RFIDMainDlg.rfidBase.preFilter2.StateAwareAction.getTarget().getValue());
        }

        // State UnAware Filter Action
        if(comboFilterAction2.getSelectionIndex() == 2)
        {
            loadActionCombo2(false);
            comboAction2.setEnabled(true);
            comboTarget2.setEnabled(false);

            // Set selected index
            comboAction2.select(RFIDMainDlg.rfidBase.preFilter2.StateUnawareAction.getStateUnawareAction().getValue());

        }
    }
    
    void loadActionCombo1(boolean isStateAware)
    {
        if (isStateAware)
            comboAction1.setItems(stateAwareAction);
        else
            comboAction1.setItems(stateUnawareAction);
    }

    void loadActionCombo2(boolean isStateAware)
    {
        if (isStateAware)
            comboAction2.setItems(stateAwareAction);
        else
            comboAction2.setItems(stateUnawareAction);
    }

}
