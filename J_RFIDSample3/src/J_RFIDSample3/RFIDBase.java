package J_RFIDSample3;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableItem;

import com.mot.rfid.api3.*;

import java.util.Hashtable;


public class RFIDBase {
	RFIDReader myReader = null;
	RFIDMainDlg mainApp = null;
	public Hashtable tagStore = null;

	public static final String API_SUCCESS = "Function Succeeded";
	public static final String PARAM_ERROR = "Parameter Error";
	final String APP_NAME = "J_RFIDSample3";
	public boolean isConnected;
	
	public String hostName = "";
	

	public int port = 5084;

	public boolean isAccessSequenceRunning = false;
	String[] memoryBank = new String[] { "Reserved", "EPC", "TID", "USER" };

	String[] tagState = new String[] { "New", "Gone", "Back", "None" };
	// To display tag read count
	public long uniqueTags = 0;
	public long totalTags = 0;
	
	// Access Filter
	public AccessFilter accessFilter = null;
	public boolean isAccessFilterSet = false;

	// Post Filter
	public PostFilter postFilter = null;
	public boolean isPostFilterSet = false;

	// Antenna Info
	public AntennaInfo antennaInfo = null;

	// Pre Filter
	public PreFilters preFilters = null;

	public PreFilters.PreFilter preFilter1 = null;
	public PreFilters.PreFilter preFilter2 = null;

	public String preFilterTagPattern1 = null;
	public String preFilterTagPattern2 = null;

	public boolean isPreFilterSet1 = false;
	public boolean isPreFilterSet2 = false;
    public int preFilterActionIndex1 = 0;
    public int preFilterActionIndex2 = 0;

	public TriggerInfo triggerInfo = null;
	ReaderManagement rm = null;
	public boolean isRmConnected = false;
	public LoginInfo loginInfo;
	public int readerTypeIndex = 1;

	public int rowId = 0;

	TagData[] myTags = null;
	
	public RFIDReader getMyReader() {
		return myReader;
	}

	public RFIDBase(RFIDMainDlg rfidSample) {
			
		// Create Reader Object
		myReader = new RFIDReader();
		mainApp = rfidSample;

		// Hash table to hold the tag data
		tagStore = new Hashtable();
		isAccessSequenceRunning = false;

		// Create the Access Filter
		accessFilter = new AccessFilter();

		// create the post filter
		postFilter = new PostFilter();

		// Create Antenna Info
		antennaInfo = new AntennaInfo();

		// Create Pre-Filter
		PreFilters preFilter = new PreFilters();

		preFilter1 = preFilters.new PreFilter();
		preFilter2 = preFilters.new PreFilter();

		triggerInfo = new TriggerInfo();

		triggerInfo.StartTrigger
				.setTriggerType(START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE);
		triggerInfo.StopTrigger
				.setTriggerType(STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE);

		triggerInfo.TagEventReportInfo
				.setReportNewTagEvent(TAG_EVENT_REPORT_TRIGGER.MODERATED);
		triggerInfo.TagEventReportInfo
				.setNewTagEventModeratedTimeoutMilliseconds((short) 500);

		triggerInfo.TagEventReportInfo
				.setReportTagInvisibleEvent(TAG_EVENT_REPORT_TRIGGER.MODERATED);
		triggerInfo.TagEventReportInfo
				.setTagInvisibleEventModeratedTimeoutMilliseconds((short) 500);

		triggerInfo.TagEventReportInfo
				.setReportTagBackToVisibilityEvent(TAG_EVENT_REPORT_TRIGGER.MODERATED);
		triggerInfo.TagEventReportInfo
				.setTagBackToVisibilityModeratedTimeoutMilliseconds((short) 500);

		triggerInfo.setTagReportTrigger(1);

		/* Reader Management Interface */
		rm = new ReaderManagement();
		loginInfo = new LoginInfo();
		loginInfo.setUserName("admin");
		
		
		if (System.getProperty("os.name").equals("Windows CE"))
		{
			hostName = "127.0.0.1";
			// On Device, connect automatically to the reader
			connectToReader(hostName, 5084);
			
		}
	}

	public boolean connectToReader(String readerHostName, int readerPort) {
		boolean retVal = false;
		hostName = readerHostName;
		port = readerPort;
		myReader.setHostName(hostName);
		loginInfo.setHostName(hostName);
		myReader.setPort(port);
		//mainApp.setTitle("J_RFIDHostSample");
		// mainApp.setCursor(Cursor.WAIT_CURSOR);

		try {
			myReader.connect();



			myReader.Events.setInventoryStartEvent(true);
			myReader.Events.setInventoryStopEvent(true);
			myReader.Events.setAccessStartEvent(true);
			myReader.Events.setAccessStopEvent(true);
			myReader.Events.setAntennaEvent(true);
			myReader.Events.setGPIEvent(true);
			myReader.Events.setBufferFullEvent(true);
			myReader.Events.setBufferFullWarningEvent(true);
			myReader.Events.setReaderDisconnectEvent(true);
			myReader.Events.setReaderExceptionEvent(true);
			myReader.Events.setTagReadEvent(true);
			myReader.Events.setAttachTagDataWithReadEvent(false);

			myReader.Events.addEventsListener(new EventsHandler(mainApp));
			retVal = true;
			isConnected = true;
			// mainApp.setTitle("Connected to " + hostName);
			postStatusNotification(API_SUCCESS, null);
			// init GPI images on the main page
			// mainApp.initGPIportImages();

			// update Menu Items
			mainApp.updateMenuItems(true);

		} catch (InvalidUsageException ex) {
			postStatusNotification(PARAM_ERROR, ex.getVendorMessage());
		} catch (OperationFailureException ex) {
			postStatusNotification(ex.getStatusDescription(),
					ex.getVendorMessage());
		}
		// mainApp.setCursor(Cursor.DEFAULT_CURSOR);
		mainApp.updateUI();
		return retVal;
	}

	public void disconnectReader() {
		try {
			myReader.disconnect();
			isConnected = false;

			// update Menu Items
			mainApp.updateMenuItems(false);

			postStatusNotification(API_SUCCESS, null);
			mainApp.updateUI();

		} catch (InvalidUsageException ex) {
			postStatusNotification(PARAM_ERROR, ex.getVendorMessage());

		} catch (OperationFailureException ex) {
			postStatusNotification(ex.getStatusDescription(),
					ex.getVendorMessage());

		}
	}

	public void startRead() {
		PostFilter myPostFilter = null;
		AntennaInfo myAntennInfo = null;
		AccessFilter myAccessFilter = null;

		// Set the Antenna Info
		if (antennaInfo.getAntennaID() != null)
			myAntennInfo = antennaInfo;

		// set the post filter
		if (isPostFilterSet)
			myPostFilter = postFilter;

		// set the access filter
		if (isAccessFilterSet)
			myAccessFilter = accessFilter;

		try {

			// Access Sequence Operation - Read the specified Memory Bank
		
			int memoryBankSelected = mainApp.comboMemoryBank.getSelectionIndex();
			
			if (memoryBankSelected > 0) {
				TagAccess tagaccess = new TagAccess();
				MEMORY_BANK memoryBank = MEMORY_BANK.MEMORY_BANK_EPC;
				TagAccess.Sequence opSequence = tagaccess.new Sequence(
						tagaccess);
				TagAccess.Sequence.Operation op1 = opSequence.new Operation();
				op1.setAccessOperationCode(ACCESS_OPERATION_CODE.ACCESS_OPERATION_READ);

				switch (--memoryBankSelected) {
				case 0:
					memoryBank = MEMORY_BANK.MEMORY_BANK_RESERVED;
					break;
				case 1:
					memoryBank = MEMORY_BANK.MEMORY_BANK_EPC;
					break;
				case 2:
					memoryBank = MEMORY_BANK.MEMORY_BANK_TID;
					break;
				case 3:
					memoryBank = MEMORY_BANK.MEMORY_BANK_USER;
					break;
				}
				op1.ReadAccessParams.setMemoryBank(memoryBank);
				op1.ReadAccessParams.setByteCount(0);
				op1.ReadAccessParams.setByteOffset(0);
				op1.ReadAccessParams.setAccessPassword(0);
				myReader.Actions.TagAccess.OperationSequence.add(op1);
				myReader.Actions.TagAccess.OperationSequence.performSequence(
						myAccessFilter, triggerInfo, myAntennInfo);

				isAccessSequenceRunning = true;

			} else // Simple Inventory
			{
				myReader.Actions.Inventory.perform(myPostFilter, triggerInfo,
						myAntennInfo);
			}
			postStatusNotification(API_SUCCESS, null);

		} catch (InvalidUsageException ex) {
			postStatusNotification(PARAM_ERROR, ex.getVendorMessage());
		} catch (OperationFailureException ex) {
			postStatusNotification(ex.getStatusDescription(),
					ex.getVendorMessage());
		}

	}

	public void stopRead() {
		try {
			if (isAccessSequenceRunning) {
				myReader.Actions.TagAccess.OperationSequence.stopSequence();
				myReader.Actions.TagAccess.OperationSequence.deleteAll();
				isAccessSequenceRunning = false;
			} else {
				myReader.Actions.Inventory.stop();
			}
			postStatusNotification(API_SUCCESS, null);

		} catch (InvalidUsageException ex) {
			postStatusNotification(PARAM_ERROR, ex.getVendorMessage());
		} catch (OperationFailureException ex) {
			postStatusNotification(ex.getStatusDescription(),
					ex.getVendorMessage());
		}

	}

	public boolean logIn(READER_TYPE readerType) {
		boolean retVal = false;
		//mainApp.setCursor(Cursor.WAIT_CURSOR);

		try {
			rm.login(loginInfo, readerType);
			retVal = true;
			isRmConnected = true;
			postStatusNotification(API_SUCCESS, null);

			// update Menu Items
			//mainApp.updateRMMenuItems(true);

		} catch (InvalidUsageException ex) {
			postStatusNotification(PARAM_ERROR, ex.getVendorMessage());
		} catch (OperationFailureException ex) {
			postStatusNotification(ex.getStatusDescription(),
					ex.getVendorMessage());
		}
		//mainApp.setCursor(Cursor.DEFAULT_CURSOR);
		return retVal;
	}

	public void logOut() {
		try {
			rm.logout();
			isRmConnected = false;

			// update Menu Items
			//mainApp.updateRMMenuItems(false);
			postStatusNotification(API_SUCCESS, null);

		} catch (InvalidUsageException ex) {
			postStatusNotification(PARAM_ERROR, ex.getVendorMessage());

		} catch (OperationFailureException ex) {
			postStatusNotification(ex.getStatusDescription(),
					ex.getVendorMessage());

		}
	}

	public void postStatusNotification(String statusMsg, String vendorMsg) {
		String status;
		if (statusMsg == API_SUCCESS)
			return;
		
		if (vendorMsg != null && vendorMsg.length()>0)
			status = statusMsg + "[" + vendorMsg + "]";
		else
			status = statusMsg;

		mainApp.showStatusNotification(status);
		
	}

	public String getSelectedTagID() {
		String tagID = null;
		
		int selectedRow = mainApp.InventoryTable.getSelectionIndex();
		
		if (selectedRow >= 0)
		{
			TableItem item = mainApp.InventoryTable.getItem(selectedRow);
			tagID = item.getText(0);
		}
		return tagID;

	}
	public String getSelectedTagAntenna() {
		String tagID = null;
		
		int selectedRow = mainApp.InventoryTable.getSelectionIndex();
		
		if (selectedRow >= 0)
		{
			TableItem item = mainApp.InventoryTable.getItem(selectedRow);
			tagID = item.getText(2);
		}
		return tagID;

	}
	
	public String getSelectedTagDate() {
		String tagID = null;
		
		int selectedRow = mainApp.InventoryTable.getSelectionIndex();
		
		if (selectedRow >= 0)
		{
			TableItem item = mainApp.InventoryTable.getItem(selectedRow);
			tagID = item.getText(9);
		}
		return tagID;

	}
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

	public MEMORY_BANK getMemoryBankEnum(int index) {
		MEMORY_BANK memBank = MEMORY_BANK.MEMORY_BANK_EPC;
		switch (index) {
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
		return memBank;
	}

	public class EventsHandler implements RfidEventsListener {
		RFIDMainDlg inventoryUI = null;

		public EventsHandler(RFIDMainDlg rfidSample) {
			inventoryUI = rfidSample;

		}

		public void eventReadNotify(RfidReadEvents rre) {
			//displayTags displayTagsThread = new displayTags();
			//displayTagsThread.start();
			//System.out.println("Tag Read Notification");
			updateTags();
		//	mainApp.prepareDataAndUpload();
		//	System.out.println("j Tag Read Notification");
		}

		
		
		void updateTags() {
			myTags = myReader.Actions.getReadTags(10);
			if (myTags != null) {

				inventoryUI.display.syncExec(new Runnable() {
					public void run() {
						//Color color = new Color(null, 127, count, count);
						
						//  mainApp.prepareDataAndUpload();

						for (int index = 0; index < myTags.length; index++) {

							TagData tag = myTags[index];
							//String key = tag.getTagID();
							String key = tag.getTagID() +"_"+String.valueOf(tag.getAntennaID());
							String memBank = new String("");

							if (tag.getOpCode() != ACCESS_OPERATION_CODE.ACCESS_OPERATION_NONE) {
								key += tag.getMemoryBank().toString();
								if (tag.getOpStatus() != ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)
									continue;
							}

							if (tagStore.containsKey(key)) {
								Integer rowIndex = (Integer) tagStore
										.get(key);
								TableItem item = inventoryUI.InventoryTable
										.getItem(rowIndex.intValue());
								String seenCount = item.getText(3);

								// Update Seen Count
								Integer seenCountTemp = Integer
										.valueOf(seenCount);

								long seenCountNew = (long) ((int) tag
										.getTagSeenCount() + seenCountTemp
										.intValue());
								//Long seenCountNew = Long
										//.valueOf(seenCountTmp);

								// Update Tag State
								item.setText(1, tagState[tag.getTagEvent().getValue() - 1]);
								
								// Update Antenna ID
								item.setText(2, String.valueOf(tag.getAntennaID()));

								// Update Seen Count
								item.setText(3, String.valueOf(seenCountNew));

								 // Update Peak RSSI
								item.setText(4, String.valueOf(tag.getPeakRSSI()));
								
								 // Update PC bits
								item.setText(5, Integer.toHexString(tag.getPC()));
								 
								 // Update Memory Bank Data
								item.setText(6, tag.getMemoryBankData());
								
								 // Update offset
								item.setText(8, String.valueOf(tag.getMemoryBankDataOffset()));
								item.setText(9, String.valueOf(tag.SeenTime.getUTCTime().getLastSeenTimeStamp().ConvertTimetoString()));

							} else {
								if (tag.getOpCode() != ACCESS_OPERATION_CODE.ACCESS_OPERATION_NONE)
									memBank = memoryBank[tag
											.getMemoryBank().getValue()];

								TableItem item = new TableItem(
										inventoryUI.InventoryTable,
										SWT.NONE);
								//item.setBackground(color);
								item.setText(new String[] {
										tag.getTagID(),
										tagState[tag.getTagEvent().getValue() - 1],
										String.valueOf(tag.getAntennaID()),
										String.valueOf(tag.getTagSeenCount()),
										String.valueOf(tag.getPeakRSSI()),
										Integer.toHexString(tag.getPC()),
										tag.getMemoryBankData(),
										memBank,
										String.valueOf(tag.getMemoryBankDataOffset()),
										String.valueOf(tag.SeenTime.getUTCTime().getLastSeenTimeStamp().ConvertTimetoString())});

								tagStore.put(key, new Integer(rowId));
								rowId++;
								uniqueTags++;
							}
							totalTags++;

							inventoryUI.labelTotalTagsCount.setText(String
									.valueOf(uniqueTags)
									+ "("
									+ String.valueOf(totalTags) + ")");
						}
					}
				});

			}
		
		}
		
		class displayTags extends Thread {
			int count;
			TagData[] myTags;

			displayTags() {
				myTags = myReader.Actions.getReadTags(100);

			}

			public void run() {
				if (myTags != null) {

					inventoryUI.display.syncExec(new Runnable() {
						public void run() {
							Color color = new Color(null, 127, count, count);

							for (int index = 0; index < myTags.length; index++) {

								TagData tag = myTags[index];
								String key = tag.getTagID();
								String memBank = new String("");

								if (tag.getOpCode() != ACCESS_OPERATION_CODE.ACCESS_OPERATION_NONE) {
									key += tag.getMemoryBank().toString();
									if (tag.getOpStatus() != ACCESS_OPERATION_STATUS.ACCESS_SUCCESS)
										continue;
								}

								if (tagStore.containsKey(key)) {
									Integer rowIndex = (Integer) tagStore
											.get(key);
									TableItem item = inventoryUI.InventoryTable
											.getItem(rowIndex.intValue());
									String seenCount = item.getText(3);

									// Update Seen Count
									Short seenCountTemp = Short
											.valueOf(seenCount);

									long seenCountTmp = (long) ((int) tag
											.getTagSeenCount() + (int) seenCountTemp
											.shortValue());
									Long seenCountNew = new Long(seenCountTmp);

									// Update Tag State
									item.setText(1, tagState[tag.getTagEvent().getValue() - 1]);
									
									// Update Antenna ID
									item.setText(2, String.valueOf(tag.getAntennaID()));

									
									item.setText(9, String.valueOf(tag.SeenTime.getUTCTime().getLastSeenTimeStamp().ConvertTimetoString()));
									

									// Update Seen Count
									item.setText(3, String.valueOf(seenCountNew));

									 // Update Peak RSSI
									item.setText(4, String.valueOf(tag.getPeakRSSI()));
									
									 // Update PC bits
									item.setText(5, Integer.toHexString(tag.getPC()));
									 
									// // Update Memory Bank Data
									// ((DefaultTableModel)tableModel).setValueAt(tag.getMemoryBankData(),
									// rowIndex.intValue(), 6);
									//
									// // Update offset
									// ((DefaultTableModel)tableModel).setValueAt(Integer.valueOf(tag.getMemoryBankDataOffset()),
									// rowIndex.intValue(), 8);

								} else {
									System.out.println("++ " +tag.SeenTime + " " + tag.getAntennaID());

									if (tag.getOpCode() != ACCESS_OPERATION_CODE.ACCESS_OPERATION_NONE)
										memBank = memoryBank[tag
												.getMemoryBank().getValue()];

									TableItem item = new TableItem(
											inventoryUI.InventoryTable,
											SWT.NONE);
									//item.setBackground(color);
									item.setText(new String[] {
											tag.getTagID(),
											tagState[tag.getTagEvent().getValue() - 1],
											String.valueOf(tag.getAntennaID()),
											String.valueOf(tag.getTagSeenCount()),
											String.valueOf(tag.getPeakRSSI()),
											Integer.toHexString(tag.getPC()),
											memBank,String.valueOf(tag.SeenTime.getUTCTime().getLastSeenTimeStamp().ConvertTimetoString()) });

									tagStore.put(key, new Integer(rowId));
									rowId++;
									uniqueTags++;
								}
								totalTags++;

								inventoryUI.labelTotalTagsCount.setText(String
										.valueOf(uniqueTags)
										+ "("
										+ String.valueOf(totalTags) + ")");
							}
						}
					});

				}
			}

		}

		public void eventStatusNotify(RfidStatusEvents rse) {
			STATUS_EVENT_TYPE statusType = rse.StatusEventData
					.getStatusEventType();

			if (statusType == STATUS_EVENT_TYPE.INVENTORY_START_EVENT) {
				mainApp.postNotification(statusType.toString(), "");
			} else if (statusType == STATUS_EVENT_TYPE.INVENTORY_STOP_EVENT) {
				mainApp.postNotification(statusType.toString(), "");
			} else if (statusType == STATUS_EVENT_TYPE.ACCESS_START_EVENT) {
				mainApp.postNotification(statusType.toString(), "");
			} else if (statusType == STATUS_EVENT_TYPE.ACCESS_STOP_EVENT) {
				mainApp.postNotification(statusType.toString(), "");
			} else if (statusType == STATUS_EVENT_TYPE.ANTENNA_EVENT) {
				int antennaID = rse.StatusEventData.AntennaEventData
						.getAntennaID();

				String antennaEventValue = "";
				if (rse.StatusEventData.AntennaEventData.getAntennaEvent() == ANTENNA_EVENT_TYPE.ANTENNA_CONNECTED)
					antennaEventValue = String.valueOf(antennaID)
							+ " Connected";
				else if (rse.StatusEventData.AntennaEventData.getAntennaEvent() == ANTENNA_EVENT_TYPE.ANTENNA_DISCONNECTED)
					antennaEventValue = String.valueOf(antennaID)
							+ " Disconnected";

				mainApp.postNotification(statusType.toString(),
						antennaEventValue);

			} else if (statusType == STATUS_EVENT_TYPE.READER_EXCEPTION_EVENT) {
				mainApp.postNotification(statusType.toString(),
						rse.StatusEventData.ReaderExceptionEventData
								.getReaderExceptionEventInfo());
			} else if (statusType == STATUS_EVENT_TYPE.BUFFER_FULL_WARNING_EVENT) {
				mainApp.postNotification(statusType.toString(), "");
			} else if (statusType == STATUS_EVENT_TYPE.BUFFER_FULL_EVENT) {
				mainApp.postNotification(statusType.toString(), "");
			} else if (statusType == STATUS_EVENT_TYPE.DISCONNECTION_EVENT) {
				String disconnectionEventData = "";
				/*
				CN Comment
				if (rse.StatusEventData.DisconnectionEventData
						.getDisconnectionEvent().eventInfo == DISCONNECTION_EVENT_TYPE.READER_INITIATED_DISCONNECTION)
					disconnectionEventData = "Reader Initiated Disconnection";
				else if ((rse.StatusEventData.DisconnectionEventData
						.getDisconnectionEvent().eventInfo == DISCONNECTION_EVENT_TYPE.CONNECTION_LOST))
					disconnectionEventData = "Connection Lost";
				else if ((rse.StatusEventData.DisconnectionEventData
						.getDisconnectionEvent().eventInfo == DISCONNECTION_EVENT_TYPE.READER_EXCEPTION))
					disconnectionEventData = "Reader Exception";
				end cn comment
				*/
                		mainApp.postNotification(statusType.toString(), disconnectionEventData);
                		BackgroundDisconnectThread disconnetThread = new BackgroundDisconnectThread(mainApp.rfidBase);
                		disconnetThread.start();
			} else if (statusType == STATUS_EVENT_TYPE.GPI_EVENT) {
				mainApp.postNotification(statusType.toString(), "");
				// mainApp.setGPIOnScreen(rse.StatusEventData.GPIEventData.getGPIPort(),
				// true, rse.StatusEventData.GPIEventData.getGPIEventState() ?
				// true : false);
			}

        }
    }
}
class BackgroundDisconnectThread extends Thread
{
    RFIDBase rfidBase;
    public BackgroundDisconnectThread(RFIDBase _rfidBase) {
        rfidBase = _rfidBase;
    }


    public void run()
    {
        // invoke Disconnect reader
        rfidBase.disconnectReader();
    }

}