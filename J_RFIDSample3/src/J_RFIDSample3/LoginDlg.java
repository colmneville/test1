package J_RFIDSample3;

import javax.swing.plaf.TextUI;

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

public class LoginDlg {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="15,11"
	private Combo comboReaderType = null;
	private Label labelhostName = null;
	private Text textHost = null;
	private Label labelUserName = null;
	private Text textUserName = null;
	private Text textPassword = null;
	private Label labelReaderType = null;
	private Button buttonLogin = null;

	LoginInfo loginInfo = null;
	private Label labelPassword = null;
	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		sShell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//sShell = new Shell();
		sShell.setText("Login");
		sShell.setLayout(null);
		sShell.setSize(new Point(240, 188));
		labelhostName = new Label(sShell, SWT.WRAP);
		labelhostName.setText("Host Name/IP");
		labelhostName.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelhostName.setBounds(new Rectangle(5, 35, 63, 16));
		textHost = new Text(sShell, SWT.BORDER);
		textHost.setBounds(new Rectangle(70, 34, 145, 21));
		labelUserName = new Label(sShell, SWT.WRAP);
		labelUserName.setBounds(new Rectangle(5, 63, 47, 17));
		labelUserName.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelUserName.setText("User Name");
		textUserName = new Text(sShell, SWT.BORDER);
		textUserName.setBounds(new Rectangle(70, 62, 145, 19));
		labelReaderType = new Label(sShell, SWT.WRAP);
		labelReaderType.setBounds(new Rectangle(4, 3, 61, 16));
		labelReaderType.setText("Reader Type");
		labelReaderType.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		createComboReaderType();
		buttonLogin = new Button(sShell, SWT.NONE);
		buttonLogin.setBounds(new Rectangle(141, 130, 72, 16));
		buttonLogin.setText("Login");
		labelPassword = new Label(sShell, SWT.WRAP);
		labelPassword.setBounds(new Rectangle(5, 92, 47, 11));
		labelPassword.setText("Password");
		labelPassword.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		textPassword = new Text(sShell, SWT.BORDER | SWT.PASSWORD);
		textPassword.setBounds(new Rectangle(70, 92, 145, 19));
		
		buttonLogin.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {   
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {    
		        int readerTypeIndex = 0;
		        if (buttonLogin.getText().equals("Login"))
		        {
		            loginInfo.setHostName(textHost.getText());
		            loginInfo.setUserName(textUserName.getText());
		            loginInfo.setPassword(textPassword.getText());
		            loginInfo.setSecureMode(SECURE_MODE.HTTP);
		            loginInfo.setForceLogin(true);
		            
		            readerTypeIndex = comboReaderType.getSelectionIndex();

		            if (RFIDMainDlg.rfidBase.logIn(getReaderType(readerTypeIndex)))
		            {
		                RFIDMainDlg.rfidBase.readerTypeIndex = readerTypeIndex;
		                buttonLogin.setText("Logout");
		                sShell.close();
		            }
		            else
		            {
		                textHost.setFocus();
		            }
		        }
		        else
		        {
		            RFIDMainDlg.rfidBase.logOut();
		            buttonLogin.setText("Login");

		            comboReaderType.setEnabled(true);
		            textHost.setEnabled(true);
		            textUserName.setEnabled(true);
		            textPassword.setEnabled(true);

		            textHost.setFocus();
		        }
			}
		
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
	}

	/**
	 * This method initializes comboReaderType	
	 *
	 */
	private void createComboReaderType() {
		comboReaderType = new Combo(sShell, SWT.READ_ONLY);
		
		comboReaderType.setBounds(new Rectangle(70, 2, 92, 21));
		String items[] = new String[] {"XR", "FX", "MC" };
		comboReaderType.setItems(items);
		comboReaderType.select(2);

		
	}

	public LoginDlg(Display display) {
		createSShell(display);
		
        loginInfo = RFIDMainDlg.rfidBase.loginInfo;

        if (RFIDMainDlg.rfidBase.isRmConnected == true)
        {
             comboReaderType.select(RFIDMainDlg.rfidBase.readerTypeIndex);
             textHost.setText(loginInfo.getHostName());
             textUserName.setText(loginInfo.getUserName());
             textPassword.setText(loginInfo.getPassword());
             comboReaderType.setEnabled(false);
             textHost.setEnabled(false);
             textUserName.setEnabled(false);
             textPassword.setEnabled(false);
             buttonLogin.setText("Logout");
        }
        else
        {
             comboReaderType.select(RFIDMainDlg.rfidBase.readerTypeIndex);
             textHost.setText(loginInfo.getHostName());
             textUserName.setText(loginInfo.getUserName());

        }

		
		sShell.open();
	}
	  
	READER_TYPE getReaderType(int index) {
		READER_TYPE readerType = READER_TYPE.FX;

		switch (index) {
		case 0:
			readerType = READER_TYPE.XR;
			break;
		case 1:
			readerType = READER_TYPE.FX;
			break;
		case 2:
			readerType = READER_TYPE.MC;
			break;
		}
		return readerType;
	}
	  
}
