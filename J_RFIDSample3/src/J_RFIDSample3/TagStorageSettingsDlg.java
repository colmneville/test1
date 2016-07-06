package J_RFIDSample3;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import com.mot.rfid.api3.*;

public class TagStorageSettingsDlg {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="11,1"
	private Label labelMaxTagCount = null;
	private Text textMaxTagCount = null;
	private Label labelMaxTagIdLength = null;
	private Text textMaxTagIdLength = null;
	private Label labelMaxMemBankSize = null;
	private Text textMaxMemBankSize = null;
	private Button buttonOK = null;
	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		sShell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
	//	sShell = new Shell();
		sShell.setText("Tag Storage Settings");
		sShell.setLayout(null);
		sShell.setSize(new Point(240, 188));
		labelMaxTagCount = new Label(sShell, SWT.NONE);
		labelMaxTagCount.setBounds(new Rectangle(12, 16, 85, 13));
		labelMaxTagCount.setText("Max Tag Count");
		textMaxTagCount = new Text(sShell, SWT.BORDER);
		textMaxTagCount.setBounds(new Rectangle(102, 15, 101, 19));
		labelMaxTagIdLength = new Label(sShell, SWT.WRAP);
		labelMaxTagIdLength.setBounds(new Rectangle(13, 46, 79, 27));
		labelMaxTagIdLength.setText("Max Tag ID length (bytes)");
		textMaxTagIdLength = new Text(sShell, SWT.BORDER);
		textMaxTagIdLength.setBounds(new Rectangle(102, 51, 100, 19));
		labelMaxMemBankSize = new Label(sShell, SWT.WRAP);
		labelMaxMemBankSize.setBounds(new Rectangle(14, 83, 78, 52));
		labelMaxMemBankSize.setText("Max Size of Memory Bank (bytes)");
		textMaxMemBankSize = new Text(sShell, SWT.BORDER);
		textMaxMemBankSize.setSize(new Point(100, 19));
		textMaxMemBankSize.setLocation(new Point(102, 90));
		buttonOK = new Button(sShell, SWT.NONE);
		buttonOK.setBounds(new Rectangle(139, 135, 63, 23));
		buttonOK.setText("OK");
		buttonOK.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
		        try {
		            TagStorageSettings tagStorageSettings = RFIDMainDlg.rfidBase.getMyReader().Config.getTagStorageSettings();
		            tagStorageSettings.setMaxTagCount(Integer.parseInt(textMaxTagCount.getText()));
		            tagStorageSettings.setMaxTagIDLength(Integer.parseInt(textMaxTagIdLength.getText()));
		            tagStorageSettings.setMaxMemoryBankByteCount(Integer.parseInt(textMaxMemBankSize.getText()));

		            RFIDMainDlg.rfidBase.getMyReader().Config.setTagStorageSettings(tagStorageSettings);
		            
		            RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.API_SUCCESS, null);
		        } catch (InvalidUsageException ex) {
		            RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.PARAM_ERROR, ex.getVendorMessage());
		        } catch (OperationFailureException ex) {
		            RFIDMainDlg.rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
		        }
		        // End the dialog
		        sShell.close();

			}
		});
		
	}

	public TagStorageSettingsDlg(Display display) {
		createSShell(display);
		
        try {
            TagStorageSettings tagStorageSettings = RFIDMainDlg.rfidBase.getMyReader().Config.getTagStorageSettings();
            textMaxTagCount.setText(String.valueOf(tagStorageSettings.getMaxTagCount()));
            textMaxTagIdLength.setText(String.valueOf(tagStorageSettings.getMaxTagIDLength()));
            textMaxMemBankSize.setText(String.valueOf(tagStorageSettings.getMaxMemoryBankByteCount()));

        } catch (InvalidUsageException ex) {
            RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.PARAM_ERROR, ex.getVendorMessage());
        } catch (OperationFailureException ex) {
            RFIDMainDlg.rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
        }
		
		sShell.open();
	}
}
