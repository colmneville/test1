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

public class LockAccessDlg {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="15,11"
	private Label labelTagId = null;
	private Text textTagId = null;
	private Label labelPassWord = null;
	private Text textPassword = null;
	private Label labelMemBank = null;
	private Combo comboLockField = null;
	private Label labelOffset = null;
	private Button buttonAccessFilter = null;
	private Button buttonLock = null;

	String tagID;
	private Combo comboLockPrivilege = null;
	
	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		sShell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//sShell = new Shell();
		sShell.setText("Lock Tag");
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
		labelPassWord.setBounds(new Rectangle(5, 26, 47, 26));
		labelPassWord.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelPassWord.setText("Password (Hex)");
		textPassword = new Text(sShell, SWT.BORDER);
		textPassword.setBounds(new Rectangle(54, 29, 175, 19));
		labelMemBank = new Label(sShell, SWT.WRAP);
		labelMemBank.setBounds(new Rectangle(5, 54, 38, 24));
		labelMemBank.setText("Lock Field");
		labelMemBank.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		createComboMemBank();
		labelOffset = new Label(sShell, SWT.WRAP);
		labelOffset.setBounds(new Rectangle(6, 82, 41, 24));
		labelOffset.setText("Lock Privilege");
		labelOffset.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		buttonAccessFilter = new Button(sShell, SWT.NONE);
		buttonAccessFilter.setBounds(new Rectangle(7, 146, 66, 16));
		buttonAccessFilter.setText("Access Filter");
		buttonLock = new Button(sShell, SWT.NONE);
		buttonLock.setBounds(new Rectangle(154, 146, 72, 16));
		buttonLock.setText("Lock");
		createComboLockPrivilege();
		
		buttonLock.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {   
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
		        try {
		            TagAccess tagAccess = new TagAccess();
		            TagAccess.LockAccessParams lockAccessParams = tagAccess.new LockAccessParams();

		            //LOCK_PRIVILEGE[] lockPrivilege = new LOCK_PRIVILEGE[5];
		            //lockPrivilege[comboLockField.getSelectionIndex()] = getPrivilege();
		            LOCK_PRIVILEGE lockPrivilege=getPrivilege();
		            LOCK_DATA_FIELD lockDataField=getLockDataField();

		            lockAccessParams.setLockPrivilege(lockDataField,lockPrivilege);
		            lockAccessParams.setAccessPassword(Long.parseLong(textPassword.getText(), 16));

		            if (textTagId.getText().length() > 0) {
		                RFIDMainDlg.rfidBase.getMyReader().Actions.TagAccess.lockWait(tagID, lockAccessParams,
		                        RFIDMainDlg.rfidBase.antennaInfo.getAntennaID() != null ? RFIDMainDlg.rfidBase.antennaInfo : null);
		            }
		            else
		            {
		                RFIDMainDlg.rfidBase.getMyReader().Actions.TagAccess.lockEvent(lockAccessParams, RFIDMainDlg.rfidBase.isAccessFilterSet ? RFIDMainDlg.rfidBase.accessFilter : null,
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

	/**
	 * This method initializes comboMemBank	
	 *
	 */
	private void createComboMemBank() {
		comboLockField = new Combo(sShell, SWT.READ_ONLY);
		comboLockField.setBounds(new Rectangle(54, 55, 92, 21));
		String items[] = new String[]{ "Kill Password", "Access Password", "EPC Memory", "TID Memory", "User Memory" };
		comboLockField.setItems(items);
		comboLockField.select(2);

		
	}

	public LockAccessDlg(Display display) {
		createSShell(display);
		
        tagID = RFIDMainDlg.rfidBase.getSelectedTagID();

        if (tagID == null)
        {
           textTagId.setText("");
           buttonAccessFilter.setEnabled(true);
        }
        else
        {
           textTagId.setText(tagID);
           buttonAccessFilter.setEnabled(false);

        }


        // initialize the password, offset and length to 0
        textPassword.setText("0");

		sShell.open();
	}

	/**
	 * This method initializes comboLockPrivilege	
	 *
	 */
	private void createComboLockPrivilege() {
		comboLockPrivilege = new Combo(sShell, SWT.READ_ONLY);
		comboLockPrivilege.setBounds(new Rectangle(54, 83, 92, 21));
		
		String items[] = new String[]{ "Read-Write", "Permanent Lock", "Permanent unlock", "Unlock" };
		comboLockPrivilege.setItems(items);
		comboLockPrivilege.select(0);

	}
	
    private LOCK_PRIVILEGE getPrivilege()
    {
        LOCK_PRIVILEGE lockPrivilege = LOCK_PRIVILEGE.LOCK_PRIVILEGE_NONE;
        switch (comboLockPrivilege.getSelectionIndex())
        {
            //Read-Write
            case 0:
                lockPrivilege = LOCK_PRIVILEGE.LOCK_PRIVILEGE_READ_WRITE;
                break;
            //Permanent Lock
            case 1:
                lockPrivilege = LOCK_PRIVILEGE.LOCK_PRIVILEGE_PERMA_LOCK;
                break;
            //Permanent unlock
            case 2:
                lockPrivilege = LOCK_PRIVILEGE.LOCK_PRIVILEGE_PERMA_UNLOCK;
                break;
            //Unlock
            case 3:
                lockPrivilege = LOCK_PRIVILEGE.LOCK_PRIVILEGE_UNLOCK;
                break;
        }

        return lockPrivilege;
    }
    private LOCK_DATA_FIELD getLockDataField()
    {
        LOCK_DATA_FIELD lockDataField =LOCK_DATA_FIELD.LOCK_EPC_MEMORY;
        switch (comboLockField.getSelectionIndex())
        {
            //Kill Password
            case 0:
            	lockDataField = LOCK_DATA_FIELD.LOCK_KILL_PASSWORD;
                break;
            //Access Password
            case 1:
            	lockDataField = LOCK_DATA_FIELD.LOCK_ACCESS_PASSWORD;
                break;
            //EPC Memory
            case 2:
            	lockDataField = LOCK_DATA_FIELD.LOCK_EPC_MEMORY;
                break;
            //TID Memory
            case 3:
            	lockDataField = LOCK_DATA_FIELD.LOCK_TID_MEMORY;
                break;  
            //User Memory
            case 4:
            	lockDataField = LOCK_DATA_FIELD.LOCK_USER_MEMORY;
                break;
        }

        return lockDataField;
    }
}
