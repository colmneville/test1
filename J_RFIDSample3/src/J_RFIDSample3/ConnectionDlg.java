package J_RFIDSample3;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;

public class ConnectionDlg {

	Shell dialog;
	final Button buttonConnect;
	final Text textHostName;
	final Text textPort;

	ConnectionDlg(Display display) {

		dialog = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		dialog.setSize(240, 188);
		dialog.setText("Connect");
		final Label labelHostName = new Label(dialog, SWT.CENTER);
		labelHostName.setText("Host Name/IP:");
		labelHostName.setBounds(20, 20, 80, 20);
		final Label labelPort = new Label(dialog, SWT.CENTER);
		labelPort.setText("Portj:");
		labelPort.setBounds(65, 50, 35, 20);

		textHostName = new Text(dialog, SWT.SINGLE | SWT.BORDER);
		textHostName.setBounds(105, 20, 120, 20);
		textHostName.setText("4444");  /* jj added  */

		textPort = new Text(dialog, SWT.SINGLE | SWT.BORDER);
		textPort.setBounds(105, 50, 120, 20);
		/* textPort.setText("169.254.10.1");  */

		buttonConnect = new Button(dialog, SWT.PUSH);
		buttonConnect.setBounds(145, 80, 80, 20);
		buttonConnect.setText("Connect");

		buttonConnect.addSelectionListener(new ConnectionListener());
		textHostName.setText(RFIDMainDlg.rfidBase.hostName);
	textPort.setText(Integer.toString(RFIDMainDlg.rfidBase.port)); 
	textHostName.setText("169.254.10.1");  /* jj added  */
	

		if (RFIDMainDlg.rfidBase.getMyReader().isConnected() == true) {
			textHostName.setEnabled(false);
			textPort.setEnabled(false);
			buttonConnect.setText("Disconnect");
		}
		
		dialog.open();

	}

	public void widgetSelected(SelectionEvent e) {

		dialog.open();
	}

	class ConnectionListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			if (buttonConnect.getText().equals("Connect")) {
				int port = Integer.parseInt(textPort.getText());
				System.out.println(System.getProperty("java.library.path"));

				if (RFIDMainDlg.rfidBase.connectToReader(
						textHostName.getText(), port)) {
					buttonConnect.setText("Disconnect");
					// this.dispose();
				} else {
					// jTextField1.requestFocusInWindow();
				}
			} else {
				RFIDMainDlg.rfidBase.disconnectReader();
				buttonConnect.setText("Connect");
				textHostName.setEnabled(true);
				textPort.setEnabled(true);
				// jTextField1.requestFocusInWindow();

			}
			dialog.close();
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub

		}
	}

}
