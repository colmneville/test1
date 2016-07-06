package J_RFIDSample3;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.RowLayout;
import com.mot.rfid.api3.*;

public class GPIOConfigDlg {

	private Shell sShell = null;
	private Group groupGPO = null;
	private Button checkBoxGPO1 = null;
	private Button checkBoxGPO2 = null;
	private Button checkBoxGPO3 = null;
	private Button checkBoxGPO4 = null;
	private Button checkBoxGPO5 = null;
	private Button checkBoxGPO6 = null;
	private Button checkBoxGPO7 = null;
	private Button checkBoxGPO8 = null;
	private Group groupGPI = null;
	private Button checkBoxGPI1 = null;
	private Button checkBoxGPI2 = null;
	private Button checkBoxGPI3 = null;
	private Button checkBoxGPI4 = null;
	private Button checkBoxGPI5 = null;
	private Button checkBoxGPI6 = null;
	private Button checkBoxGPI7 = null;
	private Button checkBoxGPI8 = null;
	private Button buttonApply = null;
    private AntennaInfo antennaInfo;
    private int numOfGPIS;
    private int numOfGPOS;


	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		sShell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//sShell = new Shell();
		sShell.setText("GPIO config");
		sShell.setLayout(null);
		createGroupGPO();
		createGroupGPI();
		sShell.setSize(new Point(240, 188));
		buttonApply = new Button(sShell, SWT.NONE);
		buttonApply.setText("Apply");
		buttonApply.setSize(new Point(59, 19));
		buttonApply.setLocation(new Point(148, 141));
	}
	
	public GPIOConfigDlg(Display display) {
		createSShell(display);
	        		
		// initialize GPO
		initGPIState();
		
		// initialize GPI
		initGPOState();
		
		// Apply Button Handler
		buttonApply.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				
		        Control[] myGPOs = groupGPO.getChildren();

		        // Set the GPO State
		        for (int index = 0; index < numOfGPOS; index++)
		        {
		            Button checkBoxGPOState;
		            checkBoxGPOState = (Button)myGPOs[index];

		            try {
		                if (checkBoxGPOState.getSelection())
		                    RFIDMainDlg.rfidBase.getMyReader().Config.GPO.setPortState(index + 1, GPO_PORT_STATE.TRUE);
		                else
		                    RFIDMainDlg.rfidBase.getMyReader().Config.GPO.setPortState(index + 1, GPO_PORT_STATE.FALSE);

		                RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.API_SUCCESS, null);

		            } catch (InvalidUsageException ex) {
		                RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.PARAM_ERROR, ex.getVendorMessage());
		            } catch (OperationFailureException ex) {
		                RFIDMainDlg.rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
		            }
		        }

		        Control[] myGPIs = groupGPI.getChildren();

		        // Set the GPI Port Enable/Disable
		        for (int index = 0; index < numOfGPIS; index++)
		        {
		            Button checkBoxGPIState;
		            checkBoxGPIState = (Button)myGPIs[index];

		            try {
		                if (checkBoxGPIState.getSelection())
		                    RFIDMainDlg.rfidBase.getMyReader().Config.GPI.enablePort(index + 1, true);
		                else
		                    RFIDMainDlg.rfidBase.getMyReader().Config.GPI.enablePort(index + 1, false);

		                RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.API_SUCCESS, null);

		            } catch (InvalidUsageException ex) {
		                RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.PARAM_ERROR, ex.getVendorMessage());
		            } catch (OperationFailureException ex) {
		                RFIDMainDlg.rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
		            }
		        }

				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});	
			
		
		sShell.open();
	}

	/**
	 * This method initializes groupGPO	
	 *
	 */
	private void createGroupGPO() {
		groupGPO = new Group(sShell, SWT.NONE);
		groupGPO.setText("GPO state");
		groupGPO.setLocation(new Point(25, 2));
		groupGPO.setSize(new Point(183, 65));
		groupGPO.setLayout(null);
		checkBoxGPO1 = new Button(groupGPO, SWT.CHECK);
		checkBoxGPO1.setText("1    ");
		checkBoxGPO1.setBounds(new Rectangle(9, 20, 39, 16));
		checkBoxGPO2 = new Button(groupGPO, SWT.CHECK);
		checkBoxGPO2.setText("2    ");
		checkBoxGPO2.setBounds(new Rectangle(51, 20, 39, 16));
		checkBoxGPO3 = new Button(groupGPO, SWT.CHECK);
		checkBoxGPO3.setText("3    ");
		checkBoxGPO3.setBounds(new Rectangle(93, 20, 39, 16));
		checkBoxGPO4 = new Button(groupGPO, SWT.CHECK);
		checkBoxGPO4.setText("4    ");
		checkBoxGPO4.setBounds(new Rectangle(135, 20, 39, 16));
		checkBoxGPO5 = new Button(groupGPO, SWT.CHECK);
		checkBoxGPO5.setText("5    ");
		checkBoxGPO5.setBounds(new Rectangle(9, 39, 39, 16));
		checkBoxGPO6 = new Button(groupGPO, SWT.CHECK);
		checkBoxGPO6.setText("6    ");
		checkBoxGPO6.setBounds(new Rectangle(51, 39, 39, 16));
		checkBoxGPO7 = new Button(groupGPO, SWT.CHECK);
		checkBoxGPO7.setText("7    ");
		checkBoxGPO7.setBounds(new Rectangle(93, 39, 39, 16));
		checkBoxGPO8 = new Button(groupGPO, SWT.CHECK);
		checkBoxGPO8.setText("8    ");
		checkBoxGPO8.setBounds(new Rectangle(135, 39, 39, 16));
	}

	/**
	 * This method initializes groupGPI	
	 *
	 */
	private void createGroupGPI() {
		groupGPI = new Group(sShell, SWT.NONE);
		groupGPI.setText("GPI Enable");
		groupGPI.setLocation(new Point(25, 73));
		groupGPI.setSize(new Point(183, 65));
		groupGPI.setLayout(null);
		checkBoxGPI1 = new Button(groupGPI, SWT.CHECK);
		checkBoxGPI1.setText("1    ");
		checkBoxGPI1.setBounds(new Rectangle(9, 20, 39, 16));
		checkBoxGPI2 = new Button(groupGPI, SWT.CHECK);
		checkBoxGPI2.setText("2    ");
		checkBoxGPI2.setBounds(new Rectangle(51, 20, 39, 16));
		checkBoxGPI3 = new Button(groupGPI, SWT.CHECK);
		checkBoxGPI3.setText("3    ");
		checkBoxGPI3.setBounds(new Rectangle(93, 20, 39, 16));
		checkBoxGPI4 = new Button(groupGPI, SWT.CHECK);
		checkBoxGPI4.setText("4    ");
		checkBoxGPI4.setBounds(new Rectangle(135, 20, 39, 16));
		checkBoxGPI5 = new Button(groupGPI, SWT.CHECK);
		checkBoxGPI5.setText("5    ");
		checkBoxGPI5.setBounds(new Rectangle(9, 39, 39, 16));
		checkBoxGPI6 = new Button(groupGPI, SWT.CHECK);
		checkBoxGPI6.setText("6    ");
		checkBoxGPI6.setBounds(new Rectangle(51, 39, 39, 16));
		checkBoxGPI7 = new Button(groupGPI, SWT.CHECK);
		checkBoxGPI7.setText("7    ");
		checkBoxGPI7.setBounds(new Rectangle(93, 39, 39, 16));
		checkBoxGPI8 = new Button(groupGPI, SWT.CHECK);
		checkBoxGPI8.setText("8    ");
		checkBoxGPI8.setBounds(new Rectangle(135, 39, 39, 16));
	}
	
    void initGPIState()
    {
        numOfGPIS = RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.getNumGPIPorts();

        Control[] myGPIs = groupGPI.getChildren();
        
        // Hide all the GPI ports
        for (int index = 0; index < 8; index++)
        {
            Button checkBoxGPIState;
            checkBoxGPIState = (Button)myGPIs[index];
            checkBoxGPIState.setVisible(false);
        }

        
        // Show only available GPIs
        for (int index = 0; index < numOfGPIS; index++)
        {
            Button checkBoxGPIState;
            checkBoxGPIState = (Button)myGPIs[index];
            checkBoxGPIState.setVisible(true);
        }

        // Set the check if GPI port is enabled in the reader
        for (int index = 0; index < numOfGPIS; index++)
        {
            Button checkBoxGPIState;
            checkBoxGPIState = (Button)myGPIs[index];

            try {
                if (RFIDMainDlg.rfidBase.getMyReader().Config.GPI.isPortEnabled(index + 1)) {
                    checkBoxGPIState.setSelection(true);
                } else {
                    checkBoxGPIState.setSelection(false);
                }
            } catch (InvalidUsageException ex) {
                //ex.printStackTrace();
            } catch (OperationFailureException ex) {
                //ex.printStackTrace();
            }
        }

    }

    void initGPOState()
    {
        numOfGPOS = RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.getNumGPOPorts();

        Control[] myGPOs = groupGPO.getChildren();
        
        // Hide all the GPO ports
        for (int index = 0; index < 8; index++)
        {
            Button checkBoxGPOState;
            checkBoxGPOState = (Button)myGPOs[index];
            checkBoxGPOState.setVisible(false);
        }

        // Show only available GPO
        for (int index = 0; index < numOfGPOS; index++)
        {
            Button checkBoxGPOState;
            checkBoxGPOState = (Button)myGPOs[index];
            checkBoxGPOState.setVisible(true);
        }

        // Set the check if it is enabled in the reader
        for (int index = 0; index < numOfGPOS; index++)
        {
            Button checkBoxGPOState;
            checkBoxGPOState = (Button)myGPOs[index];

            try {
                if (RFIDMainDlg.rfidBase.getMyReader().Config.GPO.getPortState(index + 1) == GPO_PORT_STATE.TRUE) {
                    checkBoxGPOState.setSelection(true);
                } else {
                    checkBoxGPOState.setSelection(false);
                }
            } catch (InvalidUsageException ex) {
                //ex.printStackTrace();
            } catch (OperationFailureException ex) {
                //ex.printStackTrace();
            }
        }

    }

}
