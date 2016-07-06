package J_RFIDSample3;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import com.mot.rfid.api3.*;

public class KillAccessDlg {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="15,11"
	private Label labelTagId = null;
	private Text textTagId = null;
	private Label labelPassWord = null;
	private Text textPassword = null;
	private Button buttonAccessFilter = null;
	private Button buttonKill = null;

	String tagID;
	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		sShell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//sShell = new Shell();
		sShell.setText("Kill Tag");
		sShell.setLayout(null);
		sShell.setSize(new Point(240, 188));
		labelTagId = new Label(sShell, SWT.WRAP);
		labelTagId.setText("Tag ID (Hex)");
		labelTagId.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelTagId.setBounds(new Rectangle(6, 2, 46, 23));
		textTagId = new Text(sShell, SWT.BORDER);
		textTagId.setBounds(new Rectangle(54, 1, 175, 21));
		textTagId.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
		        if (textTagId.getText().length() == 0)
		            buttonAccessFilter.setEnabled(true);
		        else
		            buttonAccessFilter.setEnabled(false);

			}
		});
		labelPassWord = new Label(sShell, SWT.WRAP);
		labelPassWord.setBounds(new Rectangle(5, 35, 47, 39));
		labelPassWord.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelPassWord.setText("Kill Password (Hex)");
		textPassword = new Text(sShell, SWT.BORDER);
		textPassword.setBounds(new Rectangle(54, 38, 175, 19));
		buttonAccessFilter = new Button(sShell, SWT.NONE);
		buttonAccessFilter.setBounds(new Rectangle(7, 146, 66, 16));
		buttonAccessFilter.setText("Access Filter");
		buttonKill = new Button(sShell, SWT.NONE);
		buttonKill.setBounds(new Rectangle(154, 146, 72, 16));
		buttonKill.setText("Kill");
		
		buttonKill.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {   
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
		        try {
		            TagAccess tagAccess = new TagAccess();
		            TagAccess.KillAccessParams killAccessParams = tagAccess.new KillAccessParams();

		            killAccessParams.setKillPassword(Long.parseLong(textPassword.getText(), 16));

		            if (textTagId.getText().length() > 0) {
		                RFIDMainDlg.rfidBase.getMyReader().Actions.TagAccess.killWait(tagID, killAccessParams,
		                        RFIDMainDlg.rfidBase.antennaInfo.getAntennaID() != null ? RFIDMainDlg.rfidBase.antennaInfo : null);
		            } else {
		                RFIDMainDlg.rfidBase.getMyReader().Actions.TagAccess.killEvent(killAccessParams, RFIDMainDlg.rfidBase.isAccessFilterSet ? RFIDMainDlg.rfidBase.accessFilter : null,
		                        RFIDMainDlg.rfidBase.antennaInfo.getAntennaID() != null ? RFIDMainDlg.rfidBase.antennaInfo : null);
		                
		            }



		            RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.API_SUCCESS, null);

		        } catch (InvalidUsageException ex) {
		            RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.PARAM_ERROR, ex.getVendorMessage());
		        } catch (OperationFailureException ex) {
		            RFIDMainDlg.rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
		        }
			}
		
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
	}

	public KillAccessDlg(Display display) {
		createSShell(display);
		
	     tagID = RFIDMainDlg.rfidBase.getSelectedTagID();

	        if (tagID == null) {
	            textTagId.setText("");
	            buttonAccessFilter.setEnabled(true);
	        } else {
	            textTagId.setText(tagID);
	            buttonAccessFilter.setEnabled(false);

	        }

	        // initialize the password, offset and length to 0
	        textPassword.setText("0");

		sShell.open();
	}
}
