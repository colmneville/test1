package J_RFIDSample3;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import com.mot.rfid.api3.*;

public class ReadPointDlg {

	private Shell sShell = null;
	private Label labelAntennaID = null;
	private Combo comboAntennaID = null;
	private Label labelStatus = null;
	private Combo comboStatus = null;
	private Button buttonApply = null;
	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		sShell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//sShell = new Shell();
		sShell.setText("Read Point");
		sShell.setSize(new Point(240, 188));
		sShell.setLayout(null);
		labelAntennaID = new Label(sShell, SWT.NONE);
		labelAntennaID.setBounds(new Rectangle(32, 32, 66, 13));
		labelAntennaID.setText("Antenna ID");
		createComboAntennaID();
		labelStatus = new Label(sShell, SWT.NONE);
		labelStatus.setBounds(new Rectangle(32, 68, 37, 13));
		labelStatus.setText("Status");
		createComboStatus();
		buttonApply = new Button(sShell, SWT.NONE);
		buttonApply.setBounds(new Rectangle(124, 122, 67, 23));
		buttonApply.setText("Apply");
		buttonApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
		         if (RFIDMainDlg.rfidBase.rm.isLoggedIn())
		            {
		                short antennaID = (short)(comboAntennaID.getSelectionIndex() + 1);
		                try {
		                    if (comboStatus.getSelectionIndex() == 0)
		                        RFIDMainDlg.rfidBase.rm.ReadPoint.setReadPointStatus(antennaID, READPOINT_STATUS.ENABLE);
		                    else
		                        RFIDMainDlg.rfidBase.rm.ReadPoint.setReadPointStatus(antennaID, READPOINT_STATUS.DISABLE);

		                    RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.API_SUCCESS, null);

		                } catch (InvalidUsageException ex) {
		                    RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.PARAM_ERROR, ex.getVendorMessage());
		                } catch (OperationFailureException ex) {
		                    RFIDMainDlg.rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
		                }
		            }
			}
		});
		
	}
	/**
	 * This method initializes comboAntennaID	
	 *
	 */
	private void createComboAntennaID() {
		comboAntennaID = new Combo(sShell, SWT.READ_ONLY);
		comboAntennaID.setBounds(new Rectangle(99, 26, 92, 21));
		String items[] = new String[]{ "1", "2", "3", "4", "5", "6", "7", "8" };
		comboAntennaID.setItems(items);
		comboAntennaID.select(0);
		
		comboAntennaID
				.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
					public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
						
			            if (RFIDMainDlg.rfidBase.rm.isLoggedIn())
			            {
			                short antennaID = (short)(comboAntennaID.getSelectionIndex() + 1);
			                try {
			                    READPOINT_STATUS readPointStatus = RFIDMainDlg.rfidBase.rm.ReadPoint.getReadPointStatus(antennaID);
			                    
			                    if (readPointStatus == READPOINT_STATUS.ENABLE)
			                        comboStatus.select(0);
			                    else
			                        comboStatus.select(1);

			                    RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.API_SUCCESS, null);
			                    
			                } catch (InvalidUsageException ex) {
			                    //ex.printStackTrace();
			                } catch (OperationFailureException ex) {
			                    RFIDMainDlg.rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
			                }

			            }

					}
					public void widgetDefaultSelected(
							org.eclipse.swt.events.SelectionEvent e) {
					}
				});
	}
	/**
	 * This method initializes comboStatus	
	 *
	 */
	private void createComboStatus() {
		comboStatus = new Combo(sShell, SWT.READ_ONLY);
		comboStatus.setBounds(new Rectangle(99, 68, 92, 21));
		
		String items[] = new String[] { "Enable", "Disable" };
		comboStatus.setItems(items);
		
	}

	public ReadPointDlg(Display display) {
		createSShell(display);
	
        try {
            READPOINT_STATUS readPointStatus = RFIDMainDlg.rfidBase.rm.ReadPoint.getReadPointStatus((short) 1);
            if (readPointStatus == READPOINT_STATUS.ENABLE)
                comboStatus.select(0);
            else
                comboStatus.select(1);

        } catch (InvalidUsageException ex) {
            //ex.printStackTrace();
        } catch (OperationFailureException ex) {
            //ex.printStackTrace();
        }

		sShell.open();
	}

}
