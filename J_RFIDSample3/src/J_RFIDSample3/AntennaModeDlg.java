package J_RFIDSample3;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.ole.win32.COMObject;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import com.mot.rfid.api3.*;

public class AntennaModeDlg {

	private Shell sShell = null;
	private Label labelStatus = null;
	private Combo comboAntennaMode = null;
	private Button buttonApply = null;
	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		sShell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//sShell = new Shell();
		sShell.setText("Antenna Mode");
		sShell.setSize(new Point(240, 188));
		sShell.setLayout(null);
		labelStatus = new Label(sShell, SWT.NONE);
		labelStatus.setBounds(new Rectangle(14, 61, 79, 13));
		labelStatus.setText("Antenna Mode");
		createComboAntennaMode();
		buttonApply = new Button(sShell, SWT.NONE);
		buttonApply.setBounds(new Rectangle(124, 122, 67, 23));
		buttonApply.setText("OK");
		buttonApply.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
		        if (RFIDMainDlg.rfidBase.rm.isLoggedIn())
		        {
		            try {
		                if (comboAntennaMode.getSelectionIndex() == 0)
		                    RFIDMainDlg.rfidBase.rm.setAntennaMode(ANTENNA_MODE.ANTENNA_MODE_MONOSTATIC);
		                else
		                    RFIDMainDlg.rfidBase.rm.setAntennaMode(ANTENNA_MODE.ANTENNA_MODE_BISTATIC);

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
	 * This method initializes comboAntennaMode	
	 *
	 */
	private void createComboAntennaMode() {
		comboAntennaMode = new Combo(sShell, SWT.READ_ONLY);
		comboAntennaMode.setBounds(new Rectangle(99, 61, 92, 21));
		
		String items[] = new String[] { "MonoStatic", "BiStatic" };
		comboAntennaMode.setItems(items);
		
	}

	public AntennaModeDlg(Display display) {
		createSShell(display);
	
        if (RFIDMainDlg.rfidBase.rm.isLoggedIn())
        {
            try {
                if (RFIDMainDlg.rfidBase.rm.getAntennaMode() == ANTENNA_MODE.ANTENNA_MODE_MONOSTATIC)
                    comboAntennaMode.select(0);
                else
                    comboAntennaMode.select(1);

            } catch (InvalidUsageException ex) {
               // ex.printStackTrace();
            } catch (OperationFailureException ex) {
               // ex.printStackTrace();
            }

        }

		sShell.open();
	}

}
