package J_RFIDSample3;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Event;

import com.mot.rfid.api3.ReaderCapabilities;
import java.awt.*;

public class CapabilitiesDlg {
	 Display d;

	  Shell dialog;
	  Table t;
	  int width = 240;
	  int height = 250;
		  
	  CapabilitiesDlg(Display display) {
	    
	    dialog = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
 
	    if (System.getProperty("os.name") == "Windows CE")
	    {
	        width = Toolkit.getDefaultToolkit().getScreenSize().width;
	        height = Toolkit.getDefaultToolkit().getScreenSize().height;
	    }
	    
	    dialog.setText("Reader Capabilities");
	    dialog.setLayout(new FillLayout());

	    t = new Table(dialog, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
	    t.setHeaderVisible(true);
	    
	    // Get Reader Capabilities
        ReaderCapabilities readerCaps = RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities;

	    TableColumn Capability = new TableColumn(t, SWT.CENTER);
	    TableColumn Value = new TableColumn(t, SWT.CENTER);
	    Capability.setText("Capability");
	    Value.setText("Value");
	    Value.setWidth(120);
	    Capability.setWidth(125);
	    dialog.setSize(240, 188);
	    t.setBounds(0, 0, 240, 188);
	    
	    insertRow("ReaderID", readerCaps.ReaderID.getID());                                                              
	    insertRow("Firmware Version", readerCaps.getFirwareVersion());                                                  
	    insertRow("Model Name", readerCaps.getModelName());                                                             
	    insertRow("Number of Antennas", String.valueOf(readerCaps.getNumAntennaSupported()));                            
	    insertRow("Number of GPI", String.valueOf(readerCaps.getNumGPIPorts()));                                         
	    insertRow("Number of GPO", String.valueOf(readerCaps.getNumGPOPorts()));                                         
	    insertRow("Max Ops in Access Sequence", String.valueOf(readerCaps.getMaxNumOperationsInAccessSequence()));       
	    insertRow("Max No. of Pre-Filters", String.valueOf(readerCaps.getMaxNumPreFilters()));                           
	    insertRow("Country Code", String.valueOf(readerCaps.getCountryCode()));                                          
	    insertRow("Communication Standard", readerCaps.getCommunicationStandard().toString());                           
	    insertRow("UTC Clock", readerCaps.isUTCClockSupported() ? "Yes" : "No");                                         
	    insertRow("Block Erase", readerCaps.isBlockEraseSupported() ? "Yes" : "No");                                     
	    insertRow("Block Write", readerCaps.isBlockWriteSupported() ? "Yes" : "No");                                     
	    insertRow("Block Permalock", readerCaps.isBlockPermalockSupported() ? "Yes" : "No");                             
	    insertRow("Recommission", readerCaps.isRecommisionSupported() ? "Yes" : "No");                                   
	    insertRow("Write UMI", readerCaps.isWriteUMISupported() ? "Yes" : "No");                                         
	    insertRow("State-aware Singulation", readerCaps.isTagInventoryStateAwareSingulationSupported() ? "Yes" : "No");  
	    insertRow("Tag Event Reporting Supported", readerCaps.isTagEventReportingSupported() ? "Yes" : "No");            
	    insertRow("RSSI Filter Supported", readerCaps.isRSSIFilterSupported() ? "Yes" : "No");                           

	    t.setFocus();

	    dialog.open();

	     while (!dialog.isDisposed()) {
	          if (!display.readAndDispatch())
	            display.sleep();
	        }
	}
	  void insertRow(String propertyName, String value)
	  {
		  TableItem item = new TableItem(t, SWT.NONE);
		  item.setText(new String[] {propertyName, value});
	  }
}
