package J_RFIDSample3;

import java.awt.Checkbox;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.FormLayout;
import com.mot.rfid.api3.*;

public class AntennaSelectionDlg {

	private Shell sShell = null;  //  @jve:decl-index=0:visual-constraint="1,9"
	private Button checkBoxSelectAll = null;
	private Composite composite = null;
	private Button checkBoxAntenna1 = null;
	private Button checkBoxAntenna2 = null;
	private Button checkBoxAntenna3 = null;
	private Button checkBoxAntenna4 = null;
	private Button checkBoxAntenna5 = null;
	private Button checkBoxAntenna6 = null;
	private Button checkBoxAntenna7 = null;
	private Button checkBoxAntenna8 = null;
	private Button buttonApply = null;
	private int numOfAntennas = 0;
	private AntennaInfo antennaInfo;

	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		sShell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//sShell = new Shell();
		sShell.setText("Select Antenna");
		sShell.setLayout(null);
		sShell.setSize(new Point(240, 188));
		checkBoxSelectAll = new Button(sShell, SWT.CHECK);
		checkBoxSelectAll.setBounds(new Rectangle(14, 6, 114, 16));
		checkBoxSelectAll.setText("Select/unselect All");
		createComposite();
		buttonApply = new Button(sShell, SWT.NONE);
		buttonApply.setBounds(new Rectangle(153, 102, 51, 23));
		buttonApply.setText("Apply");
	}
	
	public AntennaSelectionDlg(Display display) {
		createSShell(display);
		// initialize antenna selection dialog
	    initializeAntennaSelDlg();    		
	
	    // Apply button handler
	    buttonApply.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
		        Button chkBox;
		        ArrayList selectedAntennas = new ArrayList();
				Control[] myControls = composite.getChildren();

		        for (int index = 0; index < myControls.length; index++)
		        {
		            chkBox = (Button)myControls[index]; // since it is zero based index
		            
		            if (chkBox.getSelection())
		            {
		                selectedAntennas.add(String.valueOf(index + 1));
		            }
		        }

		        if (selectedAntennas.size() > 0)
		        {
		            short[] myAntennas = new short[selectedAntennas.size()];

		            for (int index = 0; index < selectedAntennas.size(); index++)
		            {
		                myAntennas[index] = Short.parseShort((String)selectedAntennas.get(index));;
		            }
		            antennaInfo.setAntennaID(myAntennas);
		        }
		        else
		            antennaInfo.setAntennaID(null);


			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	    
	    // Select All check box handler
	    checkBoxSelectAll.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
		        if (checkBoxSelectAll.getSelection())
		        {
		            for (int index = 1; index <= numOfAntennas; index++)
		                setAntennaPortState(index, true, true);
		        }
		        else
		        {
		            for (int index = 1; index <= numOfAntennas; index++)
		                setAntennaPortState(index, true, false);
		        }
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	    
		sShell.open();
	}
	
	private void initializeAntennaSelDlg()
	{
        numOfAntennas = RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.getNumAntennaSupported();
        antennaInfo = RFIDMainDlg.rfidBase.antennaInfo;

        // Set antennas visible to false
        for (int index = 1; index <= 8; index++)
            setAntennaPortState(index, false, false);

        // Show only available antennas
        for (int index = 1; index <= numOfAntennas; index++)
            setAntennaPortState(index, true, false);
        

        // Set the available antennas only
        short[] antennaSelected = antennaInfo.getAntennaID();
        if (antennaSelected != null)
        {
            for (int index = 0; index < antennaSelected.length; index++)
               setAntennaPortState(antennaSelected[index], true, true);
        }
        else
        {
            // Show only available antennas
            for (int index = 1; index <= numOfAntennas; index++)
                    setAntennaPortState(index, true, true);

            checkBoxSelectAll.setSelection(true);
        }

	}
	
	/**
	 * This method initializes composite	
	 *
	 */
	private void createComposite() {
		composite = new Composite(sShell, SWT.BORDER);
		composite.setLayout(null);
		composite.setBounds(new Rectangle(18, 37, 188, 47));
		checkBoxAntenna1 = new Button(composite, SWT.CHECK);
		checkBoxAntenna1.setText("1    ");
		checkBoxAntenna1.setBounds(new Rectangle(6, 3, 39, 16));
		checkBoxAntenna2 = new Button(composite, SWT.CHECK);
		checkBoxAntenna2.setText("2    ");
		checkBoxAntenna2.setBounds(new Rectangle(51, 3, 39, 16));
		checkBoxAntenna3 = new Button(composite, SWT.CHECK);
		checkBoxAntenna3.setText("3    ");
		checkBoxAntenna3.setBounds(new Rectangle(96, 3, 39, 16));
		checkBoxAntenna4 = new Button(composite, SWT.CHECK);
		checkBoxAntenna4.setText("4    ");
		checkBoxAntenna4.setBounds(new Rectangle(141, 3, 39, 16));
		checkBoxAntenna5 = new Button(composite, SWT.CHECK);
		checkBoxAntenna5.setText("5    ");
		checkBoxAntenna5.setBounds(new Rectangle(6, 22, 39, 16));
		checkBoxAntenna6 = new Button(composite, SWT.CHECK);
		checkBoxAntenna6.setText("6    ");
		checkBoxAntenna6.setBounds(new Rectangle(51, 22, 39, 16));
		checkBoxAntenna7 = new Button(composite, SWT.CHECK);
		checkBoxAntenna7.setText("7    ");
		checkBoxAntenna7.setBounds(new Rectangle(96, 22, 39, 16));
		checkBoxAntenna8 = new Button(composite, SWT.CHECK);
		checkBoxAntenna8.setText("8    ");
		checkBoxAntenna8.setBounds(new Rectangle(141, 22, 39, 16));
	}
	
	private void setAntennaPortState(int port, boolean isVisible, boolean isSelected)
    {
		Control[] myControls = composite.getChildren();
		Button chkBox = (Button)myControls[port - 1]; // since it is zero based index
		chkBox.setSelection(isSelected);
		chkBox.setVisible(isVisible);
    }

}
