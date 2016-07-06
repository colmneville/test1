package J_RFIDSample3;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import com.mot.rfid.api3.*;

//import java.lang.Thread

public class RFIDMainDlg {
	static RFIDBase rfidBase = null;
	Display display;
	Shell shell;
	boolean autoStart = true;
	boolean currentlyUploading = false;  
	Thread uploadThread = null;
	boolean timetoUpload = false;      /* may not use this */
	public final static String DATA_FILENAME = "C:\\i2sRfid\\data.txt";
	// Main Menus
	Menu menuBar, connectionMenu, readerMenu, configSubMenu;

	// Menu Header
	MenuItem connectionMenuHeader, readerMenuHeader;

	// Sub Menu Header
	MenuItem configMenuSubHeader;

	MenuItem tagStorageSettingsConfigMenuItem;

	MenuItem connectionMenuItem, OperationMenuHeader, ManagementMenuHeader;

	MenuItem exitReaderMenu, ConnectionItem, CapabilitiesItem, helpGetHelpItem,
			ReadAccessItem, WriteAccessItem, LockAccessItem, KillAccessItem,
			AccessFilterItem;

	Label label, labelTotalTags, labelTotalTagsCount;
	Button startReadButton, ClearTagButton,UploadButton,ClearSelectedButton;

	Table InventoryTable;
	List InventoryList;

	final public Combo comboMemoryBank;
	// final Label LabelMemoryBank;
	final Button autonomousModeCheckBox;

	public RFIDMainDlg() {

		rfidBase = new RFIDBase(this);

		display = new Display();
		shell = new Shell(display, SWT.CLOSE);

		shell.setText("J_RFIDSample3");
		shell.setSize(550, 250);

		// Start/Stop Read Button
		startReadButton = new Button(shell, SWT.PUSH);
		startReadButton.setText("Start Read");
		startReadButton.setBounds(2, 3, 70, 20);

		// Autonomous Mode
		autonomousModeCheckBox = new Button(shell, SWT.CHECK);
		autonomousModeCheckBox.setText("Autonomous");
		autonomousModeCheckBox.setBounds(75, 3, 90, 20);

		// Memory Bank Label
		// LabelMemoryBank = new Label(shell, SWT.LEFT);
		// LabelMemoryBank.setText("Mem");
		// LabelMemoryBank.setBounds(155, 5, 25, 20);

		comboMemoryBank = new Combo(shell, SWT.READ_ONLY);
		comboMemoryBank.setBounds(170, 3, 65, 20);
		String items[] = { "None", "Reserved", "EPC", "TID", "User","TEST jAMES" };
		comboMemoryBank.setItems(items);
		comboMemoryBank.select(0);

		// Total Tag Label
		labelTotalTags = new Label(shell, SWT.NULL);
		labelTotalTags.setText("Total Tags: ");
		labelTotalTags.setBounds(0, 170, 60, 20);

		// Total Tags
		labelTotalTagsCount = new Label(shell, SWT.NULL);
		labelTotalTagsCount.setText("0(0)");
		labelTotalTagsCount.setBounds(66, 170, 80, 20);

		// Clear Button
		ClearTagButton = new Button(shell, SWT.PUSH);
		ClearTagButton.setBounds(195, 167, 40, 18);
		ClearTagButton.setText("Clear");
		
		
		UploadButton = new Button(shell, SWT.PUSH);
		UploadButton.setBounds(145, 167, 45, 18);
		UploadButton.setText("Upload");

		
		ClearSelectedButton = new Button(shell, SWT.PUSH);
		ClearSelectedButton.setBounds(245, 167, 95, 18);
		ClearSelectedButton.setText("ClearSelected");


		// Menu Bar
		menuBar = new Menu(shell, SWT.BAR);

		// Connection Menu
		connectionMenuHeader = new MenuItem(menuBar, SWT.PUSH);
		connectionMenuHeader.setText("&Connection");

		// Reader Menu
		readerMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		readerMenuHeader.setText("&Reader");

		readerMenu = new Menu(shell, SWT.DROP_DOWN);
		readerMenuHeader.setMenu(readerMenu);

		// Get Reader Capabilities
		CapabilitiesItem = new MenuItem(readerMenu, SWT.PUSH);

		readerMenuHeader.setText("&Reader");
		CapabilitiesItem.setText("&Capabilities");

		// Config Menu
		configMenuSubHeader = new MenuItem(readerMenu, SWT.CASCADE);
		configMenuSubHeader.setText("C&onfig");

		configSubMenu = new Menu(shell, SWT.DROP_DOWN);
		configMenuSubHeader.setMenu(configSubMenu);

		tagStorageSettingsConfigMenuItem = new MenuItem(configSubMenu, SWT.PUSH);
		tagStorageSettingsConfigMenuItem.setText("&Tag Storage Settings...");
		tagStorageSettingsConfigMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				TagStorageSettingsDlg tagStorage = new TagStorageSettingsDlg(display);
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		// Add separator
		new MenuItem(configSubMenu, SWT.SEPARATOR);

		// Antenna configuration
		MenuItem antennaConfigMenuItem = new MenuItem(configSubMenu, SWT.PUSH);
		antennaConfigMenuItem.setText("&Antenna...");
		antennaConfigMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				AntennaConfigDlg antennaConfigDlg = new AntennaConfigDlg(display);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// RF Mode
		MenuItem rfModeConfigMenuItem = new MenuItem(configSubMenu, SWT.PUSH);
		rfModeConfigMenuItem.setText("&RF Modes...");
		rfModeConfigMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				RFModeDlg rfModeDlg = new RFModeDlg(display);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		// GPIO
		MenuItem gpioConfigMenuItem = new MenuItem(configSubMenu, SWT.PUSH);
		gpioConfigMenuItem.setText("&GPIO...");
		gpioConfigMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				GPIOConfigDlg gpioConfigDlg = new GPIOConfigDlg(display);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		// Singulation
		MenuItem singulationConfigMenuItem = new MenuItem(configSubMenu,
				SWT.PUSH);
		singulationConfigMenuItem.setText("&Singulation...");
		singulationConfigMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				SingulationDlg singulation = new SingulationDlg(display);
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// Power On/Off Radio
		final MenuItem powerOnOffConfigMenuItem = new MenuItem(configSubMenu,
				SWT.PUSH);
		powerOnOffConfigMenuItem.setText("&Power Off Radio");
		
		powerOnOffConfigMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
			      try {

			            if (rfidBase.getMyReader().Config.getRadioPowerState() == RADIO_POWER_STATE.OFF)
			            {
			                rfidBase.getMyReader().Config.setRadioPowerState(RADIO_POWER_STATE.ON);
			                powerOnOffConfigMenuItem.setText("Power Off Radio");
			            }
			            else
			            {
			                rfidBase.getMyReader().Config.setRadioPowerState(RADIO_POWER_STATE.OFF);
			                powerOnOffConfigMenuItem.setText("Power On Radio");
			            }

			        } catch (InvalidUsageException ex) {
			            rfidBase.postStatusNotification(RFIDBase.PARAM_ERROR, ex.getVendorMessage());
			        } catch (OperationFailureException ex) {
			            rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
			        }
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		// Add separator
		new MenuItem(configSubMenu, SWT.SEPARATOR);

		// Reset to Factory Defaults
		final MenuItem resetFactoryConfigMenuItem = new MenuItem(configSubMenu,
				SWT.PUSH);
		resetFactoryConfigMenuItem.setText("R&eset to Factory Defaults");
		
		resetFactoryConfigMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
			      try {
			            rfidBase.getMyReader().Config.resetFactoryDefaults();
			        } catch (InvalidUsageException ex) {
			            rfidBase.postStatusNotification(RFIDBase.PARAM_ERROR, ex.getVendorMessage());
			        } catch (OperationFailureException ex) {
			            rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
			        }
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// Operations Menu
		MenuItem operationsMenuSubHeader = new MenuItem(readerMenu, SWT.CASCADE);
		operationsMenuSubHeader.setText("O&perations");

		Menu operationsSubMenu = new Menu(shell, SWT.DROP_DOWN);
		operationsMenuSubHeader.setMenu(operationsSubMenu);
		
		MenuItem antennaInfoMenuItem = new MenuItem(operationsSubMenu, SWT.PUSH);
		antennaInfoMenuItem.setText("&Antenna Info...");
		antennaInfoMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				AntennaSelectionDlg antennaSelDlg = new AntennaSelectionDlg(display);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		MenuItem filterMenuItem = new MenuItem(operationsSubMenu, SWT.CASCADE);
		filterMenuItem.setText("&Filter");

		Menu filterSubMenu = new Menu(shell, SWT.DROP_DOWN);
		filterMenuItem.setMenu(filterSubMenu);

		MenuItem preFilterMenuItem = new MenuItem(filterSubMenu, SWT.PUSH);
		preFilterMenuItem.setText("&Pre-Filter...");
		preFilterMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				PreFilterDlg preFilterDlg = new PreFilterDlg(display);
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		MenuItem postFilterMenuItem = new MenuItem(filterSubMenu, SWT.PUSH);
		postFilterMenuItem.setText("P&ost-Filter...");
		postFilterMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				PostFilterDlg postFilterDlg = new PostFilterDlg(display);
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		MenuItem accessFilterMenuItem = new MenuItem(filterSubMenu, SWT.PUSH);
		accessFilterMenuItem.setText("&Access-Filter...");
		accessFilterMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				AccessFilterDlg accessFilterDlg = new AccessFilterDlg(display);
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		MenuItem accessMenuItem = new MenuItem(operationsSubMenu, SWT.CASCADE);
		accessMenuItem.setText("A&ccess");

		Menu accessSubMenu = new Menu(shell, SWT.DROP_DOWN);
		accessMenuItem.setMenu(accessSubMenu);

		MenuItem readAccesMenuItem = new MenuItem(accessSubMenu, SWT.PUSH);
		readAccesMenuItem.setText("&Read...");
		readAccesMenuItem.addSelectionListener(new ReadAccessItemListener());
		

		MenuItem writeAccesMenuItem = new MenuItem(accessSubMenu, SWT.PUSH);
		writeAccesMenuItem.setText("&Write...");
		writeAccesMenuItem.addSelectionListener(new WriteAccessItemListener());
		
		MenuItem lockAccesMenuItem = new MenuItem(accessSubMenu, SWT.PUSH);
		lockAccesMenuItem.setText("&Lock...");
		lockAccesMenuItem.addSelectionListener(new LockAccessItemListener());

		MenuItem killAccesMenuItem = new MenuItem(accessSubMenu, SWT.PUSH);
		killAccesMenuItem.setText("&Kill...");
		killAccesMenuItem.addSelectionListener(new KillAccessItemListener());

		MenuItem blkWriteAccesMenuItem = new MenuItem(accessSubMenu, SWT.PUSH);
		blkWriteAccesMenuItem.setText("&Block Write...");
		blkWriteAccesMenuItem.addSelectionListener(new BlockWriteAccessItemListener());
		
		MenuItem blkEraseAccesMenuItem = new MenuItem(accessSubMenu, SWT.PUSH);
		blkEraseAccesMenuItem.setText("Bl&ock Erase...");
		blkEraseAccesMenuItem.addSelectionListener(new BlockEraseAccessItemListener());
		
		MenuItem triggerMenuItem = new MenuItem(operationsSubMenu, SWT.PUSH);
		triggerMenuItem.setText("&Triggers...");

		triggerMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				TriggersDlg triggerDlg = new TriggersDlg(display);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// Management Menu
		MenuItem mgmtMenuSubHeader = new MenuItem(readerMenu, SWT.CASCADE);
		mgmtMenuSubHeader.setText("&Mgmt");

		Menu mgmtSubMenu = new Menu(shell, SWT.DROP_DOWN);
		mgmtMenuSubHeader.setMenu(mgmtSubMenu);
		
		MenuItem loginMenuItem = new MenuItem(mgmtSubMenu, SWT.PUSH);
		loginMenuItem.setText("&Login/Logout...");
		
		loginMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				LoginDlg loginDlg = new LoginDlg(display);
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		MenuItem antennaModeMenuItem = new MenuItem(mgmtSubMenu, SWT.PUSH);
		antennaModeMenuItem.setText("&Antenna Mode...");

		antennaModeMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				AntennaModeDlg antennaModeDlg = new AntennaModeDlg(display);
				
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		MenuItem readPointMenuItem = new MenuItem(mgmtSubMenu, SWT.PUSH);
		readPointMenuItem.setText("&Read Point...");
		readPointMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				ReadPointDlg readPointDlg = new ReadPointDlg(display);	
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		MenuItem softwareUpdateMenuItem = new MenuItem(mgmtSubMenu, SWT.PUSH);
		softwareUpdateMenuItem.setText("&Software Update...");
		softwareUpdateMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				SoftwareUpdateDlg softwareUpdateDlg = new SoftwareUpdateDlg(display);
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		MenuItem rebootMenuItem = new MenuItem(mgmtSubMenu, SWT.PUSH);
		rebootMenuItem.setText("R&eboot");

		rebootMenuItem.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
			     try {
			            rfidBase.rm.restart();
			        } catch (InvalidUsageException ex) {
			            rfidBase.postStatusNotification(RFIDBase.PARAM_ERROR, ex.getVendorMessage());
			        } catch (OperationFailureException ex) {
			            rfidBase.postStatusNotification(ex.getStatusDescription(), ex.getVendorMessage());
			        }
				
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// Help Menu
		MenuItem helpMenuSubHeader = new MenuItem(readerMenu, SWT.CASCADE);
		helpMenuSubHeader.setText("&Help");

		Menu helpSubMenu = new Menu(shell, SWT.DROP_DOWN);
		helpMenuSubHeader.setMenu(helpSubMenu);

		MenuItem aboutMenuItem = new MenuItem(helpSubMenu, SWT.PUSH);
		aboutMenuItem.setText("&About...");
		
		// Add separator
		new MenuItem(readerMenu, SWT.SEPARATOR);

		// Exit
		exitReaderMenu = new MenuItem(readerMenu, SWT.PUSH);
		exitReaderMenu.setText("E&xit");

		exitReaderMenu.addSelectionListener(new fileExitItemListener());
		connectionMenuHeader.addSelectionListener(new ConnectionItemListener());
		CapabilitiesItem.addSelectionListener(new CapabilitiesItemListener());

		aboutMenuItem.addSelectionListener(new aboutMenuItemListener() );
		startReadButton.addSelectionListener(new StartReadButtonListener());

		// Listener for Clear Tags Button
		ClearTagButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				synchronized (this) {
					InventoryTable.removeAll();
					rfidBase.tagStore.clear();

					rfidBase.totalTags = 0;
					rfidBase.uniqueTags = 0;
					rfidBase.rowId = 0;

					labelTotalTagsCount.setText("0(0)");
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		UploadButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				synchronized (this) {

					prepareDataAndUpload();
					/*
					InventoryTable.removeAll();
					rfidBase.tagStore.clear();

					rfidBase.totalTags = 0;
					rfidBase.uniqueTags = 0;
					rfidBase.rowId = 0;

					labelTotalTagsCount.setText("0(0)");
					*/
				}
			}

			
			
			
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
		
		
		
		ClearSelectedButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				synchronized (this) {
				/*	InventoryTable.clear(InventoryTable.getSelection()); */
				/*	 InventoryTable.selectAll(); */
				/*	 InventoryTable.remove(0); */
					 
					rfidBase.tagStore.clear();
					String tag =  rfidBase.getSelectedTagID();
					String antenna = rfidBase.getSelectedTagAntenna() ;
					String date = rfidBase.getSelectedTagDate() ;
					
					System.out.println("Tag Id: " + tag);
					System.out.println("Antenna Id: " + antenna);
					System.out.println("Date: " + date);
					System.out.println("cleared one : " + date);
					String tagData = readTable();
					connectToServer(tagData);
					/*
					InventoryTable.removeAll();
					rfidBase.tagStore.clear();

					rfidBase.totalTags = 0;
					rfidBase.uniqueTags = 0;
					rfidBase.rowId = 0;

					labelTotalTagsCount.setText("0(0)");
					*/
				}
			}

			
			
			
			
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
				
		
		
		// Listener for Autonomous Mode
		autonomousModeCheckBox.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (autonomousModeCheckBox.getSelection())
					rfidBase.triggerInfo.setEnableTagEventReport(true);
				else
					rfidBase.triggerInfo.setEnableTagEventReport(false);
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		InventoryTable = new Table(shell, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION);
		InventoryTable.setHeaderVisible(true);
		InventoryTable.setLinesVisible(true);

		InventoryTable.setBounds(2, 25, 540, 140);

		TableColumn tc1 = new TableColumn(InventoryTable, SWT.CENTER);
		TableColumn tc2 = new TableColumn(InventoryTable, SWT.CENTER);
		TableColumn tc3 = new TableColumn(InventoryTable, SWT.CENTER);
		TableColumn tc4 = new TableColumn(InventoryTable, SWT.CENTER);
		TableColumn tc5 = new TableColumn(InventoryTable, SWT.CENTER);
		TableColumn tc6 = new TableColumn(InventoryTable, SWT.CENTER);
		TableColumn tc7 = new TableColumn(InventoryTable, SWT.CENTER);
		TableColumn tc8 = new TableColumn(InventoryTable, SWT.CENTER);
		TableColumn tc9 = new TableColumn(InventoryTable, SWT.CENTER);
		TableColumn tc10 = new TableColumn(InventoryTable, SWT.CENTER);


		tc1.setText("EPC ID");
		tc2.setText("State");
		tc3.setText("Antenna ID");
		tc4.setText("Seen Count");
		tc5.setText("RSSI");
		tc6.setText("PC Bits");
		tc7.setText("Memory Bank");
		tc8.setText("MB");
		tc9.setText("Offset");
		tc10.setText("Date");

		tc1.setWidth(100);
		tc2.setWidth(40);
		tc3.setWidth(50);
		tc4.setWidth(40);
		tc5.setWidth(50);
		tc6.setWidth(40);
		tc7.setWidth(60);
		tc8.setWidth(40);
		tc9.setWidth(20);
		tc10.setWidth(80);

		Menu popupMenu = new Menu(InventoryTable);
		// Menu popupMenu = new Menu(InventoryList);

		// Read Context Menu
		MenuItem readAccess = new MenuItem(popupMenu, SWT.CASCADE);
		readAccess.setText("Read");
		readAccess.addSelectionListener(new ReadAccessItemListener());
		
		// Write Context Menu
		MenuItem writeAccess = new MenuItem(popupMenu, SWT.CASCADE);
		writeAccess.setText("Write");
		writeAccess.addSelectionListener(new WriteAccessItemListener());
		
		// Lock Context Menu
		MenuItem lockAccess = new MenuItem(popupMenu, SWT.CASCADE);
		lockAccess.setText("Lock");
		lockAccess.addSelectionListener(new LockAccessItemListener());
		
		// Kill Context Menu
		MenuItem killAccess = new MenuItem(popupMenu, SWT.CASCADE);
		killAccess.setText("Kill");
		killAccess.addSelectionListener(new KillAccessItemListener());
		
		// Block Write Context Menu
		MenuItem blockWriteAccess = new MenuItem(popupMenu, SWT.CASCADE);
		blockWriteAccess.setText("Block Write");
		blockWriteAccess.addSelectionListener(new BlockWriteAccessItemListener());
		
		
		// Block Erase Context Menu
		MenuItem blockEraseAccess = new MenuItem(popupMenu, SWT.CASCADE);
		blockEraseAccess.setText("Block Erase");
		blockEraseAccess.addSelectionListener(new BlockEraseAccessItemListener());

		InventoryTable.setMenu(popupMenu);

		shell.setMenuBar(menuBar);
		shell.open();
		autoConnectAndStart();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		if(uploadThread != null && uploadThread.isAlive() ) {
			uploadThread.interrupt();
		}
		display.dispose();
		
	}

	class fileExitItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			shell.close();
			display.dispose();
		}

		public void widgetDefaultSelected(SelectionEvent event) {
			shell.close();
			display.dispose();
		}
	}

	class ConnectionItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			ConnectionDlg connection = new ConnectionDlg(display);
		}

		public void widgetDefaultSelected(SelectionEvent event) {
			
		}
	}

	class CapabilitiesItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			CapabilitiesDlg connection = new CapabilitiesDlg(display);
		}

		public void widgetDefaultSelected(SelectionEvent event) {
			
		}
	}

	class ReadAccessItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			ReadAccessDlg ReadAccess = new ReadAccessDlg(display);
		}

		public void widgetDefaultSelected(SelectionEvent event) {
			
		}
	}

	class WriteAccessItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			WriteAccessDlg WriteAccess = new WriteAccessDlg(display, false);
		}

		public void widgetDefaultSelected(SelectionEvent event) {
			
		}
	}

	class BlockWriteAccessItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			WriteAccessDlg blkWriteAccessDlg = new WriteAccessDlg(display, true);
		}

		public void widgetDefaultSelected(SelectionEvent event) {
			
		}
	}

	class BlockEraseAccessItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			BlockEraseAccessDlg writeAccessDlg = new BlockEraseAccessDlg(display);
		}

		public void widgetDefaultSelected(SelectionEvent event) {
			
		}
	}

	class LockAccessItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			LockAccessDlg LockAccess = new LockAccessDlg(display);
		}

		public void widgetDefaultSelected(SelectionEvent event) {
			label.setText("Saved");
		}
	}

	class KillAccessItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			KillAccessDlg KillAccess = new KillAccessDlg(display);
		}

		public void widgetDefaultSelected(SelectionEvent event) {
			label.setText("Saved");
		}
	}

	class AccessFilterItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			AccessFilterDlg AccessFilter = new AccessFilterDlg(display);
		}

		public void widgetDefaultSelected(SelectionEvent event) {
			label.setText("Saved");
		}
	}

	class StartReadButtonListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			if (startReadButton.getText().equals("Start Read")) {
				rfidBase.startRead();
				startReadButton.setText("Stop Read");
			} else {
				rfidBase.stopRead();
				startReadButton.setText("Start Read");
			}
		}

		public void widgetDefaultSelected(SelectionEvent event) {
			// label.setText("Saved");
		}
	}

	class aboutMenuItemListener implements SelectionListener {
		public void widgetSelected(SelectionEvent event) {
			AboutDlg aboutDlg = new AboutDlg();

		}

		public void widgetDefaultSelected(SelectionEvent event) {
			
		}
	}

	public void postNotification(String eventName, String eventValue) {
		// labelEventName.setText(eventName);
		// labelEventValue.setText(eventValue);
	}

	public void showStatusNotification(String status) {
		MessageBox infoBox = new MessageBox(shell, SWT.ICON_INFORMATION
				| SWT.OK);
		infoBox.setText("Info");
		infoBox.setMessage(status);
		infoBox.open();
	}

	public void updateMenuItems(boolean isConnected) {

	}

	public void updateUI() {

	}   
	
	
	public String  readTable() {
		StringBuffer tagData = new StringBuffer();
		System.out.println(InventoryTable);
		TableItem[] rows = InventoryTable.getItems();
		
		for(int i=0; i<rows.length;i++){
			TableItem item = rows[i];
			tagData.append(item.getText(0));
			tagData.append(",");
			tagData.append(item.getText(2));
			tagData.append(",");
			tagData.append(convertDate(item.getText(9)));
			tagData.append("|");
			
		}
		String encodedData = tagData.toString();
		try {
			encodedData = java.net.URLEncoder.encode(tagData.toString(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Error encoding string: " + e);
			
		}
		return encodedData;
	}   
		

	private String convertDate(String date) {
		String convertedDate = "";
		SimpleDateFormat SCAN_SRC_RFID_READER = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss/SSS", new DateFormatSymbols(new Locale("EN", "IE") ));
		try {
			java.util.Date d = SCAN_SRC_RFID_READER.parse(date);
			convertedDate = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss:SSS", new DateFormatSymbols(new Locale("EN", "IE") )).format(d);
		} catch (ParseException e) {
			System.out.println("Unable to convert date ["+date+"]: " + e);
		}		
		return convertedDate;
	}
	

/*  http://46.163.66.167:8099/pps/gts/rfid?data=12345,1,31-OCT-2014%2010:23|12345,1,31-OCT-2014%2010:23|12345,1,31-OCT-2014%2010:23    */
	private void connectToServer(String rfidData) {
	/*	String rfidUrl = "http://192.168.1.4:8099/pps/jsp/testRfid.jsp?tags="+rfidTag +
				"&antenna="+antenna+
				"&date="+date;  
*/
	/*	String rfidUrl = "http://46.163.66.167:8099/pps/gts/rfid?data=+rfidTag,1,31-OCT-2014%2010:23|12345,1,31-OCT-2014%2010:23";
		*/
		/*String rfidUrl = "http://192.168.1.7:8099/pps/gts/rfid?data="+rfidData; */

		String rfidUrl = "http://46.163.66.167:8099/pps/gts/rfid?data="+rfidData;
		
		System.out.println("RfidUrl: " + rfidData);
		
		StringBuffer dataReturned = new StringBuffer();  /* easier string type to work ith efficiently */
		 try {
		      URL url = new URL(rfidUrl);
		      URLConnection yc = url.openConnection();
		      BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		      String inputLine;  /* bufferd reader converts bytes to strings */
		      while ((inputLine = in.readLine()) != null) { /* read a line at a  time*/
		        dataReturned.append(inputLine);
		      }
		      in.close();
		      System.out.println("Data returned: " + dataReturned.toString());
		      
	    	  cleanUp(dataReturned.toString());

		      
		   } catch (MalformedURLException mue) {
			   System.out.println("MalformedURLException error " + mue);
		   } catch(IOException ioe) {
			   System.out.println("IOException error " + ioe);
		   }
	}
	
	public static void main(String[] args) {
		System.out.println("In Main");
		RFIDMainDlg menuExample = new RFIDMainDlg();
		
		
	}
	
	private void autoConnectAndStart() {
		if(autoStart){
			System.out.println("Trying to autoStart");
			// Deal with the connect here
			
			ConnectionDlg connection = new ConnectionDlg(display);
			if (connection.buttonConnect.getText().equals("Connect")) {
				int port = Integer.parseInt(connection.textPort.getText());
				System.out.println(System.getProperty("java.library.path"));

				if (rfidBase.connectToReader(
						connection.textHostName.getText(), port)) {
					connection.buttonConnect.setText("Disconnect");
					// this.dispose();
					
				
					
					
					
				} else {
					// jTextField1.requestFocusInWindow();
				}
			} else {
				rfidBase.disconnectReader();
				connection.buttonConnect.setText("Connect");
				connection.textHostName.setEnabled(true);
				connection.textPort.setEnabled(true);
				// jTextField1.requestFocusInWindow();

			}
			connection.dialog.dispose();
			// Deal with the start read here
			if (startReadButton.getText().equals("Start Read")) {
				rfidBase.startRead();
				startReadButton.setText("Stop Read");
					
				
			} else {
				rfidBase.stopRead();
				startReadButton.setText("Start Read");
			}
			
			
			
	//	jj	 uploadThread = new Thread(new UploadThread());
	// jj		 uploadThread.start();
		     /*
		     int i = 0;
		     //while (t.isAlive()) {
			     while(true) {
			    	 i++;
			    	 if((i%50000000)==0) {
			    	 System.out.println("Main thread...."+i);
			     		}
			    	 if(i > 1850000000) {
			    		 t.interrupt();
			    	 }
			     }
			*/
			
			
			/*
			while(true){  // improve on this  should be in a seperate thread 
				if (!currentlyUploading){
					if(InventoryTable.getItemCount() >  2  ){
						currentlyUploading = true;
						prepareDataAndUpload();  // this is what the upload button does when we push it manually 
						currentlyUploading = false;
						// delay 5 seconds 



					}
				}
			}
		*/
			
		}
	}
	
	/**
	 * The method is called when the upload button is pressed.  
	 * This will handle clearing the table, writing the file and uploading the data
	 */
	public void prepareDataAndUpload() {
		// Read data from the table	and write to a file
		writeTableDataToFile();
		// Clear the table - for uploading we will be using the file contents
		clearInventoryTable();
		// Connect to the server passing in the data from the file 
		String data = FileUtil.getFileContents(DATA_FILENAME);
		// Connect to the server and upload
		// When the upload has completed the method #cleanUp will be called.
		// Handle the cleanup of files etc. here
		if(data != null && data.length() > 0) {
		connectToServer(data);
		} else {
			System.out.println("No data...not sending!");
		}
		
	}
	
	/**
	 * This method is called when the server has responded
	 * It should handle both good and bad resonses,timeouts etc.
	 * @param serverResponse
	 */
	private void cleanUp(String serverResponse) {
		System.out.println("Cleaning up after send....server response: " + serverResponse);
		if(serverResponse.indexOf("OK") > -1) {
			// Server response is ok
			// Delete the contents of the text file
			FileUtil.clearFileContents(DATA_FILENAME);
		} else {
			// Bad response received append to text file
		}
	}
	
	private void clearInventoryTable() {
		InventoryTable.removeAll();
		rfidBase.tagStore.clear();

		rfidBase.totalTags = 0;
		rfidBase.uniqueTags = 0;
		rfidBase.rowId = 0;

		labelTotalTagsCount.setText("0(0)");	}
	
	private void writeTableDataToFile() {
		String tableContents = readTable();
		FileUtil.saveFile(tableContents, DATA_FILENAME, true);
	}
	

	 private class UploadThread implements Runnable {
	   public void run() {
	  	 try {
	  		 while(true) {
		  		 System.out.println("Doing something....");
		  		//prepareDataAndUpload();
	         Thread.sleep(10000);
	  		 }
	     } catch (InterruptedException e) {	    	 
	    	 System.out.println("Interrupted: " + e);
	    	 return;
	     }
	   }
	 }


	
	
}

