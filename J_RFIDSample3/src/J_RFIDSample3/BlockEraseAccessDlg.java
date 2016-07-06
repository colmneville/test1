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

public class BlockEraseAccessDlg {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="15,11"
	private Label labelTagId = null;
	private Text textTagId = null;
	private Label labelPassWord = null;
	private Text textPassword = null;
	private Label labelMemBank = null;
	private Combo comboMemBank = null;
	private Label labelOffset = null;
	private Text textOffSet = null;
	private Label labelLength = null;
	private Text textLength = null;
	private Button buttonAccessFilter = null;
	private Button buttonErase = null;

	String tagID;
	
	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		sShell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//sShell = new Shell();
		sShell.setText("Block Erase");
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
		labelMemBank.setText("Memory Bank");
		labelMemBank.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		createComboMemBank();
		labelOffset = new Label(sShell, SWT.WRAP);
		labelOffset.setBounds(new Rectangle(6, 82, 35, 24));
		labelOffset.setText("Offset (bytes)");
		labelOffset.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		textOffSet = new Text(sShell, SWT.BORDER);
		textOffSet.setBounds(new Rectangle(54, 82, 61, 19));
		labelLength = new Label(sShell, SWT.WRAP);
		labelLength.setBounds(new Rectangle(124, 82, 40, 23));
		labelLength.setText("Length (bytes)");
		labelLength.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		textLength = new Text(sShell, SWT.BORDER);
		textLength.setBounds(new Rectangle(169, 82, 57, 19));
		buttonAccessFilter = new Button(sShell, SWT.NONE);
		buttonAccessFilter.setBounds(new Rectangle(7, 146, 66, 16));
		buttonAccessFilter.setText("Access Filter");
		buttonErase = new Button(sShell, SWT.NONE);
		buttonErase.setBounds(new Rectangle(154, 146, 72, 16));
		buttonErase.setText("Erase");
		
		buttonErase.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {   
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
		        try {
		            TagAccess tagAccess = new TagAccess();
		            TagAccess.BlockEraseAccessParams blkEraseAccessParams = tagAccess.new BlockEraseAccessParams();
		            MEMORY_BANK memBank = MEMORY_BANK.MEMORY_BANK_EPC;
		            switch (comboMemBank.getSelectionIndex())
		            {
		                case 0:
		                    memBank = MEMORY_BANK.MEMORY_BANK_RESERVED;
		                    break;
		                case 1:
		                    memBank = MEMORY_BANK.MEMORY_BANK_EPC;
		                    break;
		                case 2:
		                    memBank = MEMORY_BANK.MEMORY_BANK_TID;
		                    break;
		                case 3:
		                    memBank = MEMORY_BANK.MEMORY_BANK_USER;
		                    break;
		            }
		            blkEraseAccessParams.setMemoryBank(memBank);
		            blkEraseAccessParams.setByteCount(Integer.parseInt(textLength.getText()));
		            blkEraseAccessParams.setByteOffset(Integer.parseInt(textOffSet.getText()));
		            blkEraseAccessParams.setAccessPassword(Long.parseLong(textPassword.getText(), 16));

		            if (textTagId.getText().length() > 0) {
		                RFIDMainDlg.rfidBase.getMyReader().Actions.TagAccess.blockEraseWait(tagID, blkEraseAccessParams,
		                        RFIDMainDlg.rfidBase.antennaInfo.getAntennaID() != null ? RFIDMainDlg.rfidBase.antennaInfo : null);
		            }
		            else
		            {
		                RFIDMainDlg.rfidBase.getMyReader().Actions.TagAccess.blockEraseEvent(blkEraseAccessParams, RFIDMainDlg.rfidBase.isAccessFilterSet ? RFIDMainDlg.rfidBase.accessFilter : null,
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
		comboMemBank = new Combo(sShell, SWT.READ_ONLY);
		comboMemBank.setBounds(new Rectangle(54, 55, 92, 21));
		String items[] = new String[] { "Reserved", "EPC", "TID", "User" };
		comboMemBank.setItems(items);
		comboMemBank.select(1);

		
	}

	public BlockEraseAccessDlg(Display display) {
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

        // select the EPC as default
        comboMemBank.select(1);

        // initialize the password, offset and length to 0
        textPassword.setText("0");
        textOffSet.setText("0");
        textLength.setText("0");
		
		sShell.open();
	}
}
