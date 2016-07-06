package J_RFIDSample3;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import com.mot.rfid.api3.*;
import com.mot.rfid.api3.Antennas.RFMode;

public class RFModeDlg {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="-4,-51"
	private Label labelAntennaID = null;
	private Combo comboAntennaID = null;
	private Label label = null;
	private Text textTari = null;
	private Label label1 = null;
	private Combo comboRFmodeIndex = null;
	private Table table = null;
	private Button buttonApply = null;

	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		sShell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//sShell = new Shell();
		sShell.setText("RF Mode");
		sShell.setLayout(null);
		sShell.setSize(new Point(240, 188));
		labelAntennaID = new Label(sShell, SWT.NONE);
		labelAntennaID.setBounds(new Rectangle(8, 9, 60, 13));
		labelAntennaID.setText("Antenna ID");
		createComboAntennaID();
		label = new Label(sShell, SWT.NONE);
		label.setBounds(new Rectangle(127, 38, 25, 13));
		label.setText("Tari");
		textTari = new Text(sShell, SWT.BORDER);
		textTari.setBounds(new Rectangle(156, 35, 67, 19));
		label1 = new Label(sShell, SWT.WRAP);
		label1.setBounds(new Rectangle(7, 29, 56, 31));
		label1.setText("RF mode table index");
		createComboRFmodeIndex();
		table = new Table(sShell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setBounds(new Rectangle(6, 62, 218, 72));
		
		TableColumn tc1 = new TableColumn(table, SWT.CENTER);
		TableColumn tc2 = new TableColumn(table, SWT.CENTER);
		
		tc1.setText("Parameter");
		tc2.setText("Value");
	
		tc1.setWidth(100);
		tc2.setWidth(90);
		
		buttonApply = new Button(sShell, SWT.NONE);
		buttonApply.setBounds(new Rectangle(172, 141, 52, 17));
		buttonApply.setText("Apply");
	}
	
	public RFModeDlg(Display display) {
		createSShell(display);

		// initialize RF Mode 
		initializeRFModeDlg();
		
		// RF Mode Table Index combo handler
		comboRFmodeIndex.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
	            int rfModeTableIndex = comboRFmodeIndex.getSelectionIndex();
	            if (rfModeTableIndex > 0) rfModeTableIndex --;
	                updateRFModeTable(rfModeTableIndex);

			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		// Antenna ID Selection handler
		comboAntennaID.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				updateRFModeValues();
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		buttonApply.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
		       try {
		            int antennaID = comboAntennaID.getSelectionIndex();
		            antennaID += 1;

		            RFMode rFMode = RFIDMainDlg.rfidBase.getMyReader().Config.Antennas.getRFMode(antennaID);
		            int rfModeTableIndex = 0;

		            // set the RF mode table index
		            rfModeTableIndex = comboRFmodeIndex.getSelectionIndex();
		            rFMode.setTableIndex(rfModeTableIndex);

		            // set the Tari
		            rFMode.setTari(Integer.parseInt(textTari.getText()));

		            RFIDMainDlg.rfidBase.getMyReader().Config.Antennas.setRFMode(antennaID, rFMode);
		            RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.API_SUCCESS, null);

		        } catch (InvalidUsageException ex) {
		            RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.PARAM_ERROR, ex.getVendorMessage());
		        } catch (OperationFailureException ex) {
		            RFIDMainDlg.rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
		        }

			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		sShell.open();
	}

	void initializeRFModeDlg()
	{
		// Add RF Mode table index
		for (int index = 0; index < RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.RFModes
				.getRFModeTableInfo(0).length(); index++)
			comboRFmodeIndex.add(String.valueOf(index));
		
		// Add antennas
		short[] antennaList = RFIDMainDlg.rfidBase.getMyReader().Config.Antennas
				.getAvailableAntennas();
		for (int index = 0; index < antennaList.length; index++)
			comboAntennaID.add(String.valueOf(antennaList[index]));

		// select the first antenna
		comboAntennaID.select(0);
		updateRFModeValues();
		updateRFModeTable(comboRFmodeIndex.getSelectionIndex());
		
	}
	
	/**
	 * This method initializes comboAntennaID	
	 *
	 */
	private void createComboAntennaID() {
		comboAntennaID = new Combo(sShell, SWT.READ_ONLY);
		comboAntennaID.setLocation(new Point(70, 6));
		comboAntennaID.setSize(new Point(53, 21));
	}

	/**
	 * This method initializes comboRFmodeIndex	
	 *
	 */
	private void createComboRFmodeIndex() {
		comboRFmodeIndex = new Combo(sShell, SWT.READ_ONLY);
		comboRFmodeIndex.setLocation(new Point(70, 35));
		comboRFmodeIndex.setSize(new Point(53, 21));
	}
    private void updateRFModeTable(int rfModeIndex)
    {
        int rowId = 0;
        // Get Reader Capabilities
        ReaderCapabilities readerCaps = RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities;

        // clear the rows
        table.removeAll();
        
        insertRow("Mode Identifier", String.valueOf(readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getModeIdentifer()));
        insertRow("M", readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getModulation().toString());
        insertRow("DR", readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getDivideRatio().toString());
        String flModulation = "";

         if(readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getForwardLinkModulationType() == FORWARD_LINK_MODULATION.FORWARD_LINK_MODULATION_DSB_ASK)
            flModulation = "DSB_ASK";
         else if(readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getForwardLinkModulationType() == FORWARD_LINK_MODULATION.FORWARD_LINK_MODULATION_PR_ASK)
            flModulation = "PR_ASK";
         else if(readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getForwardLinkModulationType() == FORWARD_LINK_MODULATION.FORWARD_LINK_MODULATION_SSB_ASK)
            flModulation = "SSB_ASK";
         else
            flModulation = readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getForwardLinkModulationType().toString();

	     insertRow("Forward Link Modulation", flModulation);
	     insertRow("PIE", String.valueOf(readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getPieValue()));
	     insertRow("Min Tari", String.valueOf(readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getMinTariValue()));
	     insertRow("Max Tari", String.valueOf(readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getMaxTariValue()));
	     insertRow("Step Tari", String.valueOf(readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getStepTariValue()));
	     insertRow("EPC HAG T&C conformance", readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).isEpcHAGTCConformance() ? "yes" : "no");

		String smIndicator = "";

        if (readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getSpectralMaskIndicator() == SPECTRAL_MASK_INDICATOR.SMI_DI)
            smIndicator = "DI";
        else if((readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getSpectralMaskIndicator() == SPECTRAL_MASK_INDICATOR.SMI_MI))
            smIndicator = "MI";
        else if((readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getSpectralMaskIndicator() == SPECTRAL_MASK_INDICATOR.SMI_SI))
            smIndicator = "SI";
        else if((readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getSpectralMaskIndicator() == SPECTRAL_MASK_INDICATOR.SMI_UNKNOWN))
            smIndicator = "Unknown";
        else
            smIndicator = readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getSpectralMaskIndicator().toString();
        
	     insertRow("Spectral Mask Indicator", smIndicator);
	     insertRow("BDR", String.valueOf(readerCaps.RFModes.getRFModeTableInfo(0).getRFModeTableEntryInfo(rfModeIndex).getBdrValue()));

    }

	  void insertRow(String propertyName, String value)
	  {
		  TableItem item = new TableItem(table, SWT.NONE);
		  item.setText(new String[] {propertyName, value});
	  }
	  
	  void updateRFModeValues()
	  {
          int antennaID = comboAntennaID.getSelectionIndex();
          int rfModeIndex = 0;
          antennaID += 1;
           try {
               RFMode rFMode = RFIDMainDlg.rfidBase.getMyReader().Config.Antennas.getRFMode(antennaID);
               textTari.setText(String.valueOf(rFMode.getTari()));
               rfModeIndex = rFMode.getTableIndex();

               comboRFmodeIndex.select(rfModeIndex);
             
           } catch (InvalidUsageException ex) {
               RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.PARAM_ERROR, ex.getVendorMessage());
           } catch (OperationFailureException ex) {
               RFIDMainDlg.rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
           }
	  }
}