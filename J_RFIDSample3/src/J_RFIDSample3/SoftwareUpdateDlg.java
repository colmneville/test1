package J_RFIDSample3;

import java.util.Timer;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import com.mot.rfid.api3.*;
import org.eclipse.swt.widgets.ProgressBar;

public class SoftwareUpdateDlg {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="15,11"
	private Label labelTagId = null;
	private Text textUserName = null;
	private Label labelPassWord = null;
	private Text textPassword = null;
	private Text textUpdateStatus = null;
	private Button buttonUpdate = null;

	SoftwareUpdateInfo softwareUpdateInfo;
    final static int interval = 1000;
    int progressBarValue;
    final Runnable timer;    
    final Display myDisplay;    		

	private Label labelLocation = null;
	private Text textLocation = null;
	private ProgressBar progressBarStatus = null;
	
	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		sShell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//sShell = new Shell();
		sShell.setText("Software Update");
		sShell.setLayout(null);
		sShell.setSize(new Point(240, 188));
		labelTagId = new Label(sShell, SWT.WRAP);
		labelTagId.setText("FTP User Name");
		labelTagId.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelTagId.setBounds(new Rectangle(6, 2, 46, 23));
		textUserName = new Text(sShell, SWT.BORDER);
		textUserName.setBounds(new Rectangle(54, 1, 175, 21));
		
		labelPassWord = new Label(sShell, SWT.WRAP);
		labelPassWord.setBounds(new Rectangle(5, 31, 47, 23));
		labelPassWord.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelPassWord.setText("FTP Password");
		textPassword = new Text(sShell, SWT.BORDER);
		textPassword.setBounds(new Rectangle(54, 29, 175, 21));
		textUpdateStatus = new Text(sShell, SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		textUpdateStatus.setBounds(new Rectangle(6, 118, 219, 27));
		buttonUpdate = new Button(sShell, SWT.NONE);
		buttonUpdate.setBounds(new Rectangle(154, 146, 72, 16));
		buttonUpdate.setText("Update");
		labelLocation = new Label(sShell, SWT.WRAP);
		labelLocation.setBounds(new Rectangle(5, 66, 40, 13));
		labelLocation.setText("Location");
		labelLocation.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		textLocation = new Text(sShell, SWT.BORDER);
		textLocation.setBounds(new Rectangle(55, 63, 172, 19));
		progressBarStatus = new ProgressBar(sShell, SWT.SMOOTH);
		progressBarStatus.setBounds(new Rectangle(6, 92, 219, 18));
		
		buttonUpdate.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {   
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
		        softwareUpdateInfo = new SoftwareUpdateInfo();
		        softwareUpdateInfo.setHostName(textLocation.getText());
		        softwareUpdateInfo.setUserName(textUserName.getText());
		        softwareUpdateInfo.setPassword(textPassword.getText());
		        try {
		            RFIDMainDlg.rfidBase.rm.getSoftwareUpdate().Update(softwareUpdateInfo);

		            buttonUpdate.setEnabled(false);
		            progressBarValue = 0;
		            String str =  "Software update is in process.......";
		            textUpdateStatus.setText(str);
		            myDisplay.timerExec(interval, timer);
		            
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

	public SoftwareUpdateDlg(Display display) {
		createSShell(display);
		myDisplay = display;
		timer = new Runnable() {
		      public void run() {
		        if (textUpdateStatus.isDisposed())
		          return;

                if (progressBarValue == 100) {
                    //Toolkit.getDefaultToolkit().beep();
                	myDisplay.timerExec(-1, timer);
                    buttonUpdate.setEnabled(true);
                    progressBarStatus.setSelection(0);
                    
                    String str =  "Software update completed.";
                    textUpdateStatus.setText(str);
                    return;
                }
                
                try {

                    UpdateStatus updateStatus = RFIDMainDlg.rfidBase.rm.getSoftwareUpdate().getUpdateStatus();
                    progressBarValue = updateStatus.getPercentage();
                    progressBarStatus.setSelection(progressBarValue);
                    String str =  updateStatus.getUpdateInfo().toString();
                    textUpdateStatus.setText(str);

                } catch (InvalidUsageException ex) {
                    //ex.printStackTrace();
                } catch (OperationFailureException ex) {
                    //ex.printStackTrace();
                    String str =  "Software update is in process.......";
                    textUpdateStatus.setText(str);
                }
		      }
		    };
        
		sShell.open();
	}
}
