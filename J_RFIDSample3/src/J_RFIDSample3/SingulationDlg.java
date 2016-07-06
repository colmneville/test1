package J_RFIDSample3;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import com.mot.rfid.api3.*;
import com.mot.rfid.api3.Antennas.SingulationControl;

public class SingulationDlg {

	private Shell sShell = null;
	private Label labelAntenna = null;
	private Combo comboAntennaID = null;
	private Label labelSession = null;
	private Combo comboSession = null;
	private Label labelTagTransitTime = null;
	private Text textTagTransitTime = null;
	private Label labelTagPopulation = null;
	private Text textTagPopulation = null;
	private Label labeInventoryState = null;
	private Label labeSLFlag = null;
	private Combo comboInventoryState = null;
	private Combo comboSLFlag = null;
	private Group groupStateAware = null;
	private Button checkBoxStateAware = null;
	private Button buttonApply = null;
	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		sShell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//sShell = new Shell();
		sShell.setText("Singulation Settings");
		sShell.setLayout(null);
		sShell.setSize(new Point(240, 188));
		labelAntenna = new Label(sShell, SWT.NONE);
		labelAntenna.setBounds(new Rectangle(5, 9, 44, 13));
		labelAntenna.setText("Antenna");
		createComboAntennaID();
		labelSession = new Label(sShell, SWT.NONE);
		labelSession.setBounds(new Rectangle(115, 9, 41, 13));
		labelSession.setText("Session");
		createComboSession();
		labelTagTransitTime = new Label(sShell, SWT.WRAP);
		labelTagTransitTime.setBounds(new Rectangle(7, 24, 35, 33));
		labelTagTransitTime.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelTagTransitTime.setText("Tag Transit Time");
		textTagTransitTime = new Text(sShell, SWT.BORDER);
		textTagTransitTime.setBounds(new Rectangle(51, 36, 58, 19));
		labelTagPopulation = new Label(sShell, SWT.WRAP);
		labelTagPopulation.setBounds(new Rectangle(116, 33, 46, 27));
		labelTagPopulation.setText("Tag Population");
		labelTagPopulation.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		textTagPopulation = new Text(sShell, SWT.BORDER);
		textTagPopulation.setBounds(new Rectangle(167, 36, 58, 19));
		labeInventoryState = new Label(sShell, SWT.NONE);
		labeInventoryState.setBounds(new Rectangle(18, 78, 80, 13));
		labeInventoryState.setText("Inventory State");
		labeSLFlag = new Label(sShell, SWT.NONE);
		labeSLFlag.setBounds(new Rectangle(17, 106, 77, 13));
		labeSLFlag.setText("SL Flag");
		createComboInventoryState();
		createComboSLFlag();
		createGroupStateAware();
		buttonApply = new Button(sShell, SWT.NONE);
		buttonApply.setBounds(new Rectangle(166, 141, 59, 17));
		buttonApply.setText("Apply");
		buttonApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			      int antennaID = comboAntennaID.getSelectionIndex();
			        antennaID++;
			        try {
			            SingulationControl singulationControl = RFIDMainDlg.rfidBase.getMyReader().Config.Antennas.getSingulationControl(antennaID);
			            SESSION session = singulationControl.getSession();
			            switch (comboSession.getSelectionIndex())
			            {
			                case 0:
			                    session = SESSION.SESSION_S0;
			                    break;
			                case 1:
			                    session = SESSION.SESSION_S1;
			                    break;
			                case 2:
			                    session = SESSION.SESSION_S2;
			                    break;
			                case 3:
			                    session = SESSION.SESSION_S3;
			                    break;
			            }
			            singulationControl.setSession(session);
			            singulationControl.setTagPopulation(Short.parseShort(textTagPopulation.getText()));
			            singulationControl.setTagTransitTime(Short.parseShort(textTagTransitTime.getText()));

			 
			            
			            // Get State Aware Singulation State
			            if (checkBoxStateAware.getSelection())
			            {
			                singulationControl.Action.setPerformStateAwareSingulationAction(true);

			                INVENTORY_STATE inventoryState = singulationControl.Action.getInventoryState();

			                // Get Inventory State
			                switch (comboInventoryState.getSelectionIndex())
			                {
			                    case 0:
			                        inventoryState = INVENTORY_STATE.INVENTORY_STATE_A;
			                        break;
			                    case 1:
			                        inventoryState = INVENTORY_STATE.INVENTORY_STATE_B;
			                        break;
			                }
			                singulationControl.Action.setInventoryState(inventoryState);

			                SL_FLAG slFlag = singulationControl.Action.getSLFlag();

			                // Get Inventory State
			                switch (comboSLFlag.getSelectionIndex())
			                {
			                    case 0:
			                        slFlag = SL_FLAG.SL_FLAG_ASSERTED;
			                        break;
			                    case 1:
			                        slFlag = SL_FLAG.SL_FLAG_DEASSERTED;
			                        break;
			                }
			                singulationControl.Action.setSLFlag(slFlag);

			            }
			            else
			                singulationControl.Action.setPerformStateAwareSingulationAction(false);
			            
			            // Set singulation control
			            RFIDMainDlg.rfidBase.getMyReader().Config.Antennas.setSingulationControl(antennaID, singulationControl);
			            RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.API_SUCCESS, null);

			        } catch (InvalidUsageException ex) {
			            RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.PARAM_ERROR, ex.getVendorMessage());
			        } catch (OperationFailureException ex) {
			            RFIDMainDlg.rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
			        }
			}
		});
	}
	
	public SingulationDlg(Display display) {
		createSShell(display);
		
       if (RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.isTagInventoryStateAwareSingulationSupported() == true)
    	   checkBoxStateAware.setEnabled(true);
        else
            checkBoxStateAware.setEnabled(false);
 
         short[] antennaList = RFIDMainDlg.rfidBase.getMyReader().Config.Antennas.getAvailableAntennas();
         // Add antennas
         for (int index = 0; index < antennaList.length; index++)
            comboAntennaID.add(String.valueOf(antennaList[index]));
         
         // select antenna
         comboAntennaID.select(0);
         updateAntennaSelection();
         
		 sShell.open();
	}

	/**
	 * This method initializes comboAntennaID	
	 *
	 */
	private void createComboAntennaID() {
		comboAntennaID = new Combo(sShell, SWT.READ_ONLY);
		comboAntennaID.setBounds(new Rectangle(51, 7, 58, 21));
		
		comboAntennaID
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				         updateAntennaSelection();
					}
					public void widgetDefaultSelected(
							org.eclipse.swt.events.SelectionEvent e) {
					}
				});
	}

	/**
	 * This method initializes comboSession	
	 *
	 */
	private void createComboSession() {
		comboSession = new Combo(sShell, SWT.READ_ONLY);
		comboSession.setBounds(new Rectangle(167, 7, 58, 21));
		String[] items = new String[] { "S0", "S1", "S2", "S3" };
		comboSession.setItems(items);
		comboSession.select(-1);
		
	}

	/**
	 * This method initializes comboInventoryState	
	 *
	 */
	private void createComboInventoryState() {
		comboInventoryState = new Combo(sShell, SWT.READ_ONLY);
		comboInventoryState.setBounds(new Rectangle(106, 77, 92, 21));
		
		String[] items = new String[] { "A", "B" };;
		comboInventoryState.setItems(items);
		comboInventoryState.select(-1);
		
		
	}

	/**
	 * This method initializes comboSLFlag	
	 *
	 */
	private void createComboSLFlag() {
		comboSLFlag = new Combo(sShell, SWT.READ_ONLY);
		comboSLFlag.setBounds(new Rectangle(106, 103, 92, 21));
		String[] items = new String[] { "ASSERTED", "DEASSERTED" };
		comboSLFlag.setItems(items);
		comboSLFlag.select(-1);
		
	}

	/**
	 * This method initializes groupStateAware	
	 *
	 */
	private void createGroupStateAware() {
		groupStateAware = new Group(sShell, SWT.NONE);
		groupStateAware.setLayout(null);
		groupStateAware.setBounds(new Rectangle(3, 59, 225, 74));
		checkBoxStateAware = new Button(groupStateAware, SWT.CHECK);
		checkBoxStateAware.setBounds(new Rectangle(8, -1, 152, 16));
		checkBoxStateAware.setText("State Aware Singulation");
		checkBoxStateAware
				.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						   if (checkBoxStateAware.getSelection())
					        {
					            comboInventoryState.setEnabled(true);
					            comboSLFlag.setEnabled(true);
					        }
					        else
					        {
					        	comboInventoryState.setEnabled(false);
					        	comboSLFlag.setEnabled(false);
					        }
					}
				});
	}

	private void updateAntennaSelection() {
		int antennaID = comboAntennaID.getSelectionIndex();
            antennaID++;
            try {
                SingulationControl singulationControl = RFIDMainDlg.rfidBase.getMyReader().Config.Antennas.getSingulationControl(antennaID);

                comboSession.select(singulationControl.getSession().getValue());
                textTagPopulation.setText(String.valueOf(singulationControl.getTagPopulation()));
                textTagTransitTime.setText(String.valueOf(singulationControl.getTagTransitTime()));

                // Get State Aware Singulation State
                if (singulationControl.Action.isPerformStateAwareSingulationActionSet())
                {
                    checkBoxStateAware.setSelection(true);
                    comboInventoryState.select(singulationControl.Action.getInventoryState().getValue());
                    comboSLFlag.select(singulationControl.Action.getSLFlag().getValue());
                    comboInventoryState.setEnabled(true);
                    comboSLFlag.setEnabled(true);
                }
                else
                {
                    checkBoxStateAware.setSelection(false);
                    comboInventoryState.setEnabled(false);
                    comboSLFlag.setEnabled(false);
                }

            } catch (InvalidUsageException ex) {
                RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.PARAM_ERROR, ex.getVendorMessage());
            } catch (OperationFailureException ex) {
                RFIDMainDlg.rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
            }
	}

}
