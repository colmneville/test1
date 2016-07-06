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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import com.mot.rfid.api3.*;
import com.mot.rfid.api3.Antennas.Config;

public class AntennaConfigDlg {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="14,-24"
	private Label labelAntennaID = null;
	private Combo comboAntennaID = null;
	private Label labelReceiveSensitivity = null;
	private Combo comboReceiveSensitivity = null;
	private Label labelTransmitPower = null;
	private Combo comboTxPower = null;
	private Label labelTransmitFrequency = null;
	private Combo comboTxFrequency = null;
	private Label labelAntennaID1 = null;
	private Text textArea = null;
	private Button buttonApply = null;

	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		sShell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//sShell = new Shell();
		sShell.setText("Antenna Config");
		sShell.setLayout(null);
		sShell.setSize(new Point(240, 188));
		labelAntennaID = new Label(sShell, SWT.NONE);
		labelAntennaID.setText("Antenna ID");
		labelAntennaID.setLocation(new Point(6, 16));
		labelAntennaID.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelAntennaID.setSize(new Point(49, 13));
		createComboAntennaID();
		labelReceiveSensitivity = new Label(sShell, SWT.WRAP);
		labelReceiveSensitivity.setText("Receive Sensitivity (dB)");
		labelReceiveSensitivity.setSize(new Point(47, 33));
		labelReceiveSensitivity.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelReceiveSensitivity.setLocation(new Point(121, 5));
		createComboReceiveSensitivity();
		labelTransmitPower = new Label(sShell, SWT.WRAP);
		labelTransmitPower.setText("Transmit Power (dBm)");
		labelTransmitPower.setLocation(new Point(6, 40));
		labelTransmitPower.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelTransmitPower.setSize(new Point(56, 24));
		createComboTxPower();
		labelTransmitFrequency = new Label(sShell, SWT.WRAP);
		labelTransmitFrequency.setText("Transmit Frequency");
		labelTransmitFrequency.setLocation(new Point(118, 42));
		labelTransmitFrequency.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		labelTransmitFrequency.setSize(new Point(50, 28));
		createComboTxFrequency();
		labelAntennaID1 = new Label(sShell, SWT.NONE);
		labelAntennaID1.setBounds(new Rectangle(7, 68, 49, 11));
		labelAntennaID1.setText("Frequencies");
		labelAntennaID1.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		textArea = new Text(sShell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		textArea.setBounds(new Rectangle(7, 82, 216, 60));
		buttonApply = new Button(sShell, SWT.NONE);
		buttonApply.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		buttonApply.setSize(new Point(45, 14));
		buttonApply.setLocation(new Point(178, 146));
		buttonApply.setText("Apply");
	}
	
	public AntennaConfigDlg(Display display) {
		createSShell(display);
		
		// initialize the antenna config
	    initializeAntennaConfigDlg();
	    
	    // Apply button handler
		buttonApply.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
		          try {
		              int antennaID = comboAntennaID.getSelectionIndex();
		              antennaID += 1;
		              Config antennaConfig = RFIDMainDlg.rfidBase.getMyReader().Config.Antennas.getAntennaConfig(antennaID);
		              antennaConfig.setReceiveSensitivityIndex((short)comboReceiveSensitivity.getSelectionIndex());
		              short txFreqIndex = (short)comboTxFrequency.getSelectionIndex();
		              txFreqIndex++;
		              antennaConfig.setTransmitFrequencyIndex(txFreqIndex);
		              antennaConfig.setTransmitPowerIndex((short)comboTxPower.getSelectionIndex());
		              RFIDMainDlg.rfidBase.getMyReader().Config.Antennas.setAntennaConfig(antennaID, antennaConfig);
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
		
		// Antenna ID Selection handler
		comboAntennaID.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				updateAntennaConfigValues();
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		sShell.open();
	}

	// initialize antenna config dialog
	private void initializeAntennaConfigDlg()
	{
        short[] antennaList = RFIDMainDlg.rfidBase.getMyReader().Config.Antennas.getAvailableAntennas();
        
        // Add Receive sensitivity
        int[] receiveSensitivityValues = RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.getReceiveSensitivityValues();
        for (int index = 0; index < receiveSensitivityValues.length; index++)
            comboReceiveSensitivity.add(String.valueOf(receiveSensitivityValues[index]));

        // Add Transmit Power
        int[] transmitPowerLevelValues = RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.getTransmitPowerLevelValues();
        for (int index = 0; index < transmitPowerLevelValues.length; index++)
            comboTxPower.add(String.valueOf(transmitPowerLevelValues[index]));

        // if frequency hopping enabled, show hop table else transmit frequency table
        if (RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.isHoppingEnabled())
        {
            labelTransmitFrequency.setText("Hop table ID");
            for (int index = 0; index < RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.FrequencyHopInfo.Length(); index++)
                comboTxFrequency.add(String.valueOf(RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.FrequencyHopInfo.getFrepuencyHopTablesInfo(index).getHopTableID()));

            FrequencyHopTable frequencyHopTablesInfo = RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.FrequencyHopInfo.getFrepuencyHopTablesInfo(0);
            int[] frequencyHopValues = frequencyHopTablesInfo.getFrequencyHopValues();
            
            String frequencies = "";
            for (int index = 0; index < frequencyHopValues.length; index++)
                frequencies += String.valueOf(frequencyHopValues[index]) + ", ";

            frequencies = frequencies.substring(0, frequencies.length() - 2);
            textArea.setText(frequencies);

        }
        else
        {
            int[] fixedFreqValues = RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.getFixedFreqValues();
            for(int index = 0; index < fixedFreqValues.length; index++)
                comboTxFrequency.add(String.valueOf(fixedFreqValues[index]));
        }

        // Add antennas
        for (int index = 0; index < antennaList.length; index++)
            comboAntennaID.add(String.valueOf(antennaList[index]));
        
        comboAntennaID.select(0);
        
        // update Antenna Config
        updateAntennaConfigValues();
	}
	
	/**
	 * This method initializes comboAntennaID	
	 *
	 */
	private void createComboAntennaID() {
		comboAntennaID = new Combo(sShell, SWT.READ_ONLY);
		comboAntennaID.setLocation(new Point(63, 11));
		comboAntennaID.setSize(new Point(51, 21));
	}

	/**
	 * This method initializes comboReceiveSensitivity	
	 *
	 */
	private void createComboReceiveSensitivity() {
		comboReceiveSensitivity = new Combo(sShell, SWT.READ_ONLY);
		comboReceiveSensitivity.setLocation(new Point(170, 9));
		comboReceiveSensitivity.setSize(new Point(51, 21));
	}

	/**
	 * This method initializes comboTxPower	
	 *
	 */
	private void createComboTxPower() {
		comboTxPower = new Combo(sShell, SWT.READ_ONLY);
		comboTxPower.setLocation(new Point(63, 42));
		comboTxPower.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		comboTxPower.setSize(new Point(51, 21));
	}

	/**
	 * This method initializes comboTxFrequency	
	 *
	 */
	private void createComboTxFrequency() {
		comboTxFrequency = new Combo(sShell, SWT.READ_ONLY);
		comboTxFrequency.setLocation(new Point(170, 45));
		comboTxFrequency.setFont(new Font(Display.getDefault(), "Tahoma", 7, SWT.NORMAL));
		comboTxFrequency.setSize(new Point(51, 21));
	}
	
	private void updateAntennaConfigValues()
	{
        try {
            int antennaID = comboAntennaID.getSelectionIndex();
            int receiveSensitivityIndex = 0;
            int txPowerIndex = 0;
            int txFreqIndex = 0;

            antennaID++;
            Config antennaConfig = RFIDMainDlg.rfidBase.getMyReader().Config.Antennas.getAntennaConfig(antennaID);
            antennaID--;


            receiveSensitivityIndex = antennaConfig.getReceiveSensitivityIndex();
            txPowerIndex = antennaConfig.getTransmitPowerIndex();
            txFreqIndex = antennaConfig.getTransmitFrequencyIndex();

            // 1 based index so reduce 1
            if (txFreqIndex > 0)
                txFreqIndex--;

            comboReceiveSensitivity.select(receiveSensitivityIndex);
            comboTxPower.select(txPowerIndex);
            comboTxFrequency.select(txFreqIndex);

            // if frequency hopping enabled, show hop table else transmit frequency table
            if (RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.isHoppingEnabled())
            {
                FrequencyHopTable frequencyHopTablesInfo = RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.FrequencyHopInfo.getFrepuencyHopTablesInfo(txFreqIndex);
                int[] frequencyHopValues = frequencyHopTablesInfo.getFrequencyHopValues();

                String frequencies = "";
                for (int index = 0; index < frequencyHopValues.length; index++)
                    frequencies += String.valueOf(frequencyHopValues[index]) + ", ";

                frequencies = frequencies.substring(0, frequencies.length() - 2);
                textArea.setText(frequencies);
            }
            else
            {
                int[] fixedFreqValues = RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.getFixedFreqValues();
                for(int index = 0; index < fixedFreqValues.length; index++)
                    comboTxFrequency.add(String.valueOf(fixedFreqValues[index]));
            }



        } catch (InvalidUsageException ex) {
            RFIDMainDlg.rfidBase.postStatusNotification(RFIDBase.PARAM_ERROR, ex.getVendorMessage());
        } catch (OperationFailureException ex) {
            RFIDMainDlg.rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
        }
	
	}
}
