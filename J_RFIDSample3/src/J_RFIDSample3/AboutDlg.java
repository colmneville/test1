package J_RFIDSample3;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Label;
import com.mot.rfid.api3.*;

public class AboutDlg {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="7,4"
	private Button buttonOK = null;
	private Label labelAppName = null;
	private Label labelVersion = null;
	private Label labelVersionInfo = null;
	
	AboutDlg()
	{
		createSShell();
		sShell.open();
	}
	/**
	 * This method initializes sShell
	 */
	private void createSShell() {
		sShell = new Shell(SWT.CLOSE);
		sShell.setText("About");
		sShell.setLayout(null);
		sShell.setSize(240, 188);//new Point(238, 147));
		buttonOK = new Button(sShell, SWT.NONE);
		buttonOK.setText("OK");
		buttonOK.setBounds(new Rectangle(160, 92, 57, 23));
		buttonOK.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				sShell.close();
			}
		});
		labelAppName = new Label(sShell, SWT.NONE);
		labelAppName.setBounds(new Rectangle(16, 18, 164, 19));
		//labelAppName.setText("J_RFIDSample3");
		labelVersion = new Label(sShell, SWT.NONE);
		labelVersion.setBounds(new Rectangle(16, 49, 100, 13));
		labelVersion.setText("RFID C DLL Version");
		labelVersionInfo = new Label(sShell, SWT.NONE);
		labelVersionInfo.setBounds(new Rectangle(122, 49, 106, 13));
		labelVersionInfo.setText("Label");
		
		labelAppName.setText(RFIDMainDlg.rfidBase.APP_NAME);
		
	    String versionInfo = "Unknown";
        try {
        	versionInfo = RFIDMainDlg.rfidBase.getMyReader().versionInfo().getVersion();
   
        } catch (InvalidUsageException ex) {
        } catch (OperationFailureException ex) {
        }
        labelVersionInfo.setText(versionInfo);
;	}

}
