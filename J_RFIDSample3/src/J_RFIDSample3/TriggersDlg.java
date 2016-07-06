package J_RFIDSample3;

import java.awt.Checkbox;


import org.eclipse.swt.graphics.Point;

import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.swt.custom.CTabFolder;

import org.eclipse.swt.custom.CTabItem;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import com.mot.rfid.api3.*;

public class TriggersDlg {

	private Shell sShell = null;
	private TabFolder TabFolderTriggerType = null;
	private Composite compositeStartTrigger = null;
	private Composite compositeStopTrigger = null;
	private Composite compositeReportTrigger = null;
	private Label labelTagReportTrigger = null;
	private Text textTagReportTrigger = null;
	private Button buttonApply = null;
	private Label labelStartTriggerType = null;
	private Combo comboStartTriggerType = null;
	private Label labelStartDate = null;
	private Label labelStartTime = null;
	private DateTime date =null;
	private DateTime time =null;
	private Label labelPeriod =null;
	private Text textPeriod =null;
	private Label labelGPIStartTriggerEvent =null;
	private Combo comboGPIPortStart =null;
	private Button checkboxStartTriggerHighToLow =null;
	private Button checkboxStartTriggerLowToHigh =null;
	private Label labelHandheldEvent =null;
	private Button checkboxStartTriggerReleased =null;
	private Button checkboxStartTriggerPressed =null;
	private Label labelNewTag =null;
	private Label labelTagInvisible =null;
	private Label labelTagbacktoVisibility =null;
	private Combo ComboNewTag =null;
	private Combo ComboTagInvisible =null;
	private Combo ComboTagbacktoVisibility =null;	
	private Text textNewTag =null;
	private Text textTagInvisible =null;
	private Text textTagbacktoVisibility =null;
	  
	private Label labelStopTriggerType = null;
	private Combo comboStopTriggerType = null;
	
	private Label labelDuration =null;
	private Text textDuration =null;
	
	private Label labelGPIPort=null;
	private Combo comboGPIPortStop=null;
	private Label labelGPITimeOut=null;
	private Text textGPITimeOut=null;
	private Label labelGPIStopTriggerEvent=null;
	private Button checkboxStopTriggerHighToLow =null;
	private Button checkboxStopTriggerLowToHigh =null;
	
	private Label labelTagObservation =null;
	private Text textTagObservation =null;	
	private Label labelTime =null;
	private Text textTime =null;
	
	private Label labelNoOfAttempts =null;
	private Text textNoOfAttempts =null;	
	private Label labelNAttemptsTimeOut =null;
	private Text textNAttemptsTimeOut =null;
	

	private Label labelHandheldTriggerTimeOut =null;
	private Text textHandheldTriggerTimeOut =null;

	private Label labelHandheldTriggerEvent =null;
	private Button checkboxStopTriggerReleased =null;
	private Button checkboxStopTriggerPressed =null;
	
	
    private TriggerInfo triggerInfo;
    private int numOfGPIS;

	/**
	 * This method initializes sShell
	 */
	private void createSShell(Display display) {
		sShell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		//sShell = new Shell();
		sShell.setText("Trigger");
		sShell.setLayout(null);
		createCTabFolderTriggerType();
		sShell.setSize(new Point(240, 188));
		labelTagReportTrigger = new Label(sShell, SWT.NONE);
		labelTagReportTrigger.setBounds(new Rectangle(5, 147, 110, 15));
		labelTagReportTrigger.setText("Tag Report Trigger");
		textTagReportTrigger = new Text(sShell, SWT.BORDER);
		textTagReportTrigger.setBounds(new Rectangle(120, 147, 40, 15));
		textTagReportTrigger.setText("0");
		buttonApply = new Button(sShell, SWT.NONE);
		buttonApply.setBounds(new Rectangle(165, 147, 60, 15));
		buttonApply.setText("Apply");
		
		buttonApply.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				applyButtonHandler();
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		sShell.open();
	}
	
	public TriggersDlg(Display display) {
		createSShell(display);
        triggerInfo = RFIDMainDlg.rfidBase.triggerInfo;

        // Trigger Dialog
        initializeTriggerDlg();

		
		sShell.open();
	}
	/**
	 * This method initializes cTabFolderTriggerType	
	 *
	 */
	private void createCTabFolderTriggerType() {
		TabFolderTriggerType = new TabFolder(sShell, SWT.NONE);
		createCompositeStartTrigger();
		createCompositeStopTrigger();
		createCompositeReportTrigger();
		TabFolderTriggerType.setBounds(new Rectangle(0, 0, 230, 142));
		TabItem TabItemStartTrigger = new TabItem(TabFolderTriggerType, SWT.NULL);
		TabItemStartTrigger.setControl(compositeStartTrigger);
		TabItemStartTrigger.setText("Start Trigger");
		TabItem TabItemStopTrigger = new TabItem(TabFolderTriggerType, SWT.NULL);
		TabItemStopTrigger.setControl(compositeStopTrigger);
		TabItemStopTrigger.setText("Stop Trigger");
		TabItem TabItemReportTrigger = new TabItem(TabFolderTriggerType, SWT.NULL);
		TabItemReportTrigger.setControl(compositeReportTrigger);
		TabItemReportTrigger.setText("Report Trigger");
		labelStartTriggerType = new Label(compositeStartTrigger, SWT.LEFT);
		labelStartTriggerType.setBounds(10,12,70,15);
		labelStartTriggerType.setText("Trigger Type");
		comboStartTriggerType=new Combo( compositeStartTrigger,SWT.READ_ONLY);
		comboStartTriggerType.setBounds(90,10,80,15);
		String startTriggerItems[]= {"Immediate","Periodic","GPI","Handheld Trigger"};
		comboStartTriggerType.setItems(startTriggerItems);
		comboStartTriggerType.addSelectionListener (new SelectionAdapter () {
		    public void widgetSelected (SelectionEvent e) {
		    	updateStartTriggerPanel();
		    	}
			  });
		
		labelStartDate=new Label(compositeStartTrigger, SWT.LEFT);
		labelStartDate.setBounds(10,40,60,15);
		labelStartDate.setText("Start Date");
		labelStartDate.setVisible(false);
		
		labelStartTime=new Label(compositeStartTrigger, SWT.LEFT);
		labelStartTime.setBounds(10,68,60,30);
		labelStartTime.setText("Start Time");
		labelStartTime.setVisible(false);
		date = new DateTime (compositeStartTrigger, SWT.DATE|SWT.BORDER);
		date.setBounds(90,38,90,18);
		date.addSelectionListener (new SelectionAdapter () {
	    public void widgetSelected (SelectionEvent e) {
	      //System.out.println ("calendar date changed");
	    	}
		});
		date.setVisible(false);
	
		time = new DateTime (compositeStartTrigger, SWT.TIME|SWT.BORDER);
		time.setBounds(90,66,90,18);
		time.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
	     // System.out.println ("time changed");
			}
		});
		time.setVisible(false);
		
		labelPeriod=new Label(compositeStartTrigger, SWT.LEFT);
		labelPeriod.setBounds(10,96,70,15);
		labelPeriod.setText("Period (ms)");
		labelPeriod.setVisible(false);
		
		textPeriod=new Text(compositeStartTrigger, SWT.BORDER);
		textPeriod.setBounds(90,96,60,18);
		textPeriod.setText("1");
		textPeriod.setVisible(false);
		
		
		labelGPIStartTriggerEvent=new Label(compositeStartTrigger, SWT.LEFT);
		labelGPIStartTriggerEvent.setBounds(10,42,60,15);
		labelGPIStartTriggerEvent.setText("Event");
		labelGPIStartTriggerEvent.setVisible(false);
		
		comboGPIPortStart=new Combo(compositeStartTrigger, SWT.READ_ONLY);
		comboGPIPortStart.setBounds(90,40,80,15);
		
		comboGPIPortStart.setVisible(false);
		
		checkboxStartTriggerHighToLow=new Button(compositeStartTrigger, SWT.CHECK);
		checkboxStartTriggerHighToLow.setBounds(90,68,90,15);
		checkboxStartTriggerHighToLow.setText("High To Low");
		checkboxStartTriggerHighToLow.setVisible(false);
		checkboxStartTriggerHighToLow.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				checkboxStartTriggerLowToHigh.setSelection(false);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		checkboxStartTriggerLowToHigh=new Button(compositeStartTrigger, SWT.CHECK);
		checkboxStartTriggerLowToHigh.setBounds(90,86,90,15);
		checkboxStartTriggerLowToHigh.setText("Low To High");
		checkboxStartTriggerLowToHigh.setVisible(false);
		checkboxStartTriggerLowToHigh.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				checkboxStartTriggerHighToLow.setSelection(false);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		

		labelHandheldEvent=new Label(compositeStartTrigger, SWT.LEFT);
		labelHandheldEvent.setBounds(10,60,60,15);
		labelHandheldEvent.setText("Event");
		labelHandheldEvent.setVisible(false);
		
		checkboxStartTriggerReleased=new Button(compositeStartTrigger, SWT.CHECK);
		checkboxStartTriggerReleased.setBounds(90,60,120,15);
		checkboxStartTriggerReleased.setText("Trigger Released");
		checkboxStartTriggerReleased.setVisible(false);
		checkboxStartTriggerReleased.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				checkboxStartTriggerPressed.setSelection(false);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		checkboxStartTriggerPressed=new Button(compositeStartTrigger, SWT.CHECK);
		checkboxStartTriggerPressed.setBounds(90,80,120,15);
		checkboxStartTriggerPressed.setText("Trigger Pressed");
		checkboxStartTriggerPressed.setVisible(false);
		checkboxStartTriggerPressed.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				checkboxStartTriggerReleased.setSelection(false);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		
		labelNewTag =new Label(compositeReportTrigger, SWT.LEFT);
		labelNewTag.setBounds(10,12,60,15);
		labelNewTag.setText("New Tag");
			
		labelTagInvisible =new Label(compositeReportTrigger, SWT.LEFT);
		labelTagInvisible.setBounds(10,45,60,15);
		labelTagInvisible.setText("Tag Invisible");
		
		labelTagbacktoVisibility =new Label(compositeReportTrigger, SWT.LEFT);
		labelTagbacktoVisibility.setBounds(10,78,60,30);
		labelTagbacktoVisibility.setText("Tag back to \nvisibility");
		
		ComboNewTag =new Combo(compositeReportTrigger, SWT.READ_ONLY);
		ComboNewTag.setBounds(80,10,60,15);
		String ReportTriggerItems[]= {"Never","Immediate","Moderated"};
		ComboNewTag.setItems(ReportTriggerItems);
		
		ComboTagInvisible =new Combo(compositeReportTrigger, SWT.READ_ONLY);
		ComboTagInvisible.setBounds(80,43,60,15);
		ComboTagInvisible.setItems(ReportTriggerItems);
		
		ComboTagbacktoVisibility =new Combo(compositeReportTrigger, SWT.READ_ONLY);
		ComboTagbacktoVisibility.setBounds(80,76,60,15);
		ComboTagbacktoVisibility.setItems(ReportTriggerItems);	
		
		textNewTag =new Text(compositeReportTrigger, SWT.BORDER);
		textNewTag.setBounds(150,10,60,20);
		textNewTag.setText("500");
		
		textTagInvisible =new Text(compositeReportTrigger, SWT.BORDER);
		textTagInvisible.setBounds(150,43,60,20);
		textTagInvisible.setText("500");
		
		textTagbacktoVisibility =new Text(compositeReportTrigger, SWT.BORDER);
		textTagbacktoVisibility.setBounds(150,76,60,20);
		textTagbacktoVisibility.setText("500");
		
		
		labelStopTriggerType = new Label(compositeStopTrigger, SWT.LEFT);
		labelStopTriggerType.setBounds(10,12,70,15);
		labelStopTriggerType.setText("Trigger Type");
		comboStopTriggerType=new Combo( compositeStopTrigger,SWT.READ_ONLY);
		comboStopTriggerType.setBounds(90,10,80,15);
		String stopTriggerItems[]= {"Immediate","Duration","GPI with TimeOut","Tag Observation","N Attempts","Handheld Trigger"};
		comboStopTriggerType.setItems(stopTriggerItems);
		comboStopTriggerType.addSelectionListener (new SelectionAdapter () {
		    public void widgetSelected (SelectionEvent e) {
		    	updateStopTriggerPanel();
		    }
			  
		});		
		
		labelDuration= new Label(compositeStopTrigger, SWT.LEFT);
		labelDuration.setBounds(10,52,70,15);
		labelDuration.setText("Duration(ms)");
		labelDuration.setVisible(false);
		
		
		textDuration= new Text(compositeStopTrigger, SWT.BORDER);
		textDuration.setBounds(90,50,80,20);
		textDuration.setVisible(false);
		
		labelGPIPort=new Label(compositeStopTrigger, SWT.LEFT);
		labelGPIPort.setBounds(10,37,60,15);
		labelGPIPort.setText("Port");
		labelGPIPort.setVisible(false);
		
		
		comboGPIPortStop=new Combo(compositeStopTrigger, SWT.READ_ONLY);
		comboGPIPortStop.setBounds(90,35,80,15);
		
		comboGPIPortStop.setVisible(false);
		
		labelGPITimeOut=new Label(compositeStopTrigger, SWT.LEFT);
		labelGPITimeOut.setBounds(10,62,60,15);
		labelGPITimeOut.setText("Time Out");
		labelGPITimeOut.setVisible(false);
		
		textGPITimeOut=new Text(compositeStopTrigger, SWT.BORDER);
		textGPITimeOut.setBounds(90,60,80,15);
		textGPITimeOut.setVisible(false);
		
		labelGPIStopTriggerEvent=new Label(compositeStopTrigger, SWT.LEFT);
		labelGPIStopTriggerEvent.setBounds(10,82,60,15);
		labelGPIStopTriggerEvent.setText("Event");
		labelGPIStopTriggerEvent.setVisible(false);
		
		checkboxStopTriggerHighToLow =new Button(compositeStopTrigger, SWT.CHECK);
		checkboxStopTriggerHighToLow.setBounds(90,82,90,15);
		checkboxStopTriggerHighToLow.setText("High To Low");
		checkboxStopTriggerHighToLow.setVisible(false);
		checkboxStopTriggerHighToLow.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				checkboxStopTriggerLowToHigh.setSelection(false);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		checkboxStopTriggerLowToHigh =new Button(compositeStopTrigger, SWT.CHECK);
		checkboxStopTriggerLowToHigh.setBounds(90,100,90,15);
		checkboxStopTriggerLowToHigh.setText("Low To High");
		checkboxStopTriggerLowToHigh.setVisible(false);
		checkboxStopTriggerLowToHigh.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				checkboxStopTriggerHighToLow.setSelection(false);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		
		labelTagObservation =new Label(compositeStopTrigger, SWT.LEFT);
		labelTagObservation.setBounds(10,47,70,30);
		labelTagObservation.setText("Tag \nObservation");
		labelTagObservation.setVisible(false);
		textTagObservation =new Text(compositeStopTrigger, SWT.BORDER);
		textTagObservation.setBounds(90,45,80,20);
		textTagObservation.setVisible(false);	
		
		labelTime =new Label(compositeStopTrigger, SWT.LEFT);
		labelTime.setBounds(10,82,60,20);
		labelTime.setText("Time");;
		labelTime.setVisible(false);
		textTime =new Text(compositeStopTrigger, SWT.BORDER);
		textTime.setBounds(90,80,80,20);
		textTime.setVisible(false);	
		
		labelNoOfAttempts =new Label(compositeStopTrigger, SWT.LEFT);
		labelNoOfAttempts.setBounds(10,47,60,30);
		labelNoOfAttempts.setText("No. of \nAttempts");
		labelNoOfAttempts.setVisible(false);
		textNoOfAttempts =new Text(compositeStopTrigger, SWT.BORDER);
		textNoOfAttempts.setBounds(90,45,80,20);
		textNoOfAttempts.setVisible(false);	
		
		labelNAttemptsTimeOut =new Label(compositeStopTrigger, SWT.LEFT);
		labelNAttemptsTimeOut.setBounds(10,82,60,20);
		labelNAttemptsTimeOut.setText("Time Out");
		labelNAttemptsTimeOut.setVisible(false);
		textNAttemptsTimeOut =new Text(compositeStopTrigger, SWT.BORDER);
		textNAttemptsTimeOut.setBounds(90,80,80,20);
		textNAttemptsTimeOut.setVisible(false);	
		
		labelHandheldTriggerTimeOut =new Label(compositeStopTrigger, SWT.LEFT);
		labelHandheldTriggerTimeOut.setBounds(10,42,60,25);
		labelHandheldTriggerTimeOut.setText("Time Out");
		labelHandheldTriggerTimeOut.setVisible(false);
		textHandheldTriggerTimeOut =new Text(compositeStopTrigger, SWT.BORDER);
		textHandheldTriggerTimeOut.setBounds(90,40,80,20);
		textHandheldTriggerTimeOut.setText("0");
		textHandheldTriggerTimeOut.setVisible(false);	

		labelHandheldTriggerEvent =new Label(compositeStopTrigger, SWT.LEFT);
		labelHandheldTriggerEvent.setBounds(10,72,60,20);
		labelHandheldTriggerEvent.setText("Event");
		labelHandheldTriggerEvent.setVisible(false);

		checkboxStopTriggerReleased=new Button(compositeStopTrigger, SWT.CHECK);
		checkboxStopTriggerReleased.setBounds(90,70,120,15);
		checkboxStopTriggerReleased.setText("Trigger Released");
		checkboxStopTriggerReleased.setVisible(false);
		checkboxStopTriggerReleased.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				checkboxStopTriggerPressed.setSelection(false);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		checkboxStopTriggerPressed=new Button(compositeStopTrigger, SWT.CHECK);
		checkboxStopTriggerPressed.setBounds(90,90,120,15);
		checkboxStopTriggerPressed.setText("Trigger Pressed");
		checkboxStopTriggerPressed.setVisible(false);
		checkboxStopTriggerPressed.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				checkboxStopTriggerReleased.setSelection(false);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		//textDuration.setText("Duration(ms)");
		}
//	/**
//	 * This method initializes compositeStartTrigger	
//	 *
//	 */
	private void createCompositeStartTrigger() {
		compositeStartTrigger = new Composite(TabFolderTriggerType, SWT.BORDER);
		//compositeStartTrigger.setLayout(new GridLayout());
	}
	/**
	 * This method initializes compositeStopTriggger	
	 *
	 */
	private void createCompositeStopTrigger() {
		compositeStopTrigger = new Composite(TabFolderTriggerType, SWT.BORDER);
		//compositeStopTriggger.setLayout(new GridLayout());
	}
	/**
	 * This method initializes compositeReportTrigger	
	 *
	 */
	private void createCompositeReportTrigger() {
		compositeReportTrigger = new Composite(TabFolderTriggerType, SWT.BORDER);
		//compositeReportTrigger.setLayout(new GridLayout());
	}

    START_TRIGGER_TYPE getStartTriggerType(int index)
    {
        START_TRIGGER_TYPE startTriggerType = START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE;

            switch(comboStartTriggerType.getSelectionIndex())
            {
                // Immediate
                case 0:
                    startTriggerType = START_TRIGGER_TYPE.START_TRIGGER_TYPE_IMMEDIATE;
                   break;
                // Periodic
                case 1:
                    startTriggerType = START_TRIGGER_TYPE.START_TRIGGER_TYPE_PERIODIC;
                    break;
                // GPI
                case 2:
                    startTriggerType = START_TRIGGER_TYPE.START_TRIGGER_TYPE_GPI;
                    break;
                // Handheld Trigger
                case 3:
                    startTriggerType = START_TRIGGER_TYPE.START_TRIGGER_TYPE_HANDHELD;
                    break;
            }
            return startTriggerType;
    }

    STOP_TRIGGER_TYPE getStopTriggerType(int index)
    {
        STOP_TRIGGER_TYPE stopTriggerType = STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE;

        switch(comboStopTriggerType.getSelectionIndex())
        {
            // Immediate
            case 0:
               stopTriggerType = STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_IMMEDIATE;
               break;
            // Duration
            case 1:
                stopTriggerType = STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_DURATION;
                break;
            // GPI with time out
            case 2:
                stopTriggerType = STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_GPI_WITH_TIMEOUT;
                break;
            // Tag Observation
            case 3:
                stopTriggerType = STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_TAG_OBSERVATION_WITH_TIMEOUT;
               break;
           // N Attempts
            case 4:
                stopTriggerType = STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_N_ATTEMPTS_WITH_TIMEOUT;
                break;
            // Handheld Trigger
            case 5:
                stopTriggerType = STOP_TRIGGER_TYPE.STOP_TRIGGER_TYPE_HANDHELD_WITH_TIMEOUT;
                break;

        }
        return stopTriggerType;
    }

    TAG_EVENT_REPORT_TRIGGER getReportTriggerEvent(int index)
    { 
        TAG_EVENT_REPORT_TRIGGER tagReportTrigger = TAG_EVENT_REPORT_TRIGGER.IMMEDIATE;
        switch(index)
        {
            // Never
            case 0:
                tagReportTrigger = TAG_EVENT_REPORT_TRIGGER.NEVER;
                break;
            case 1:
                tagReportTrigger = TAG_EVENT_REPORT_TRIGGER.IMMEDIATE;
                break;
            case 2:
                tagReportTrigger = TAG_EVENT_REPORT_TRIGGER.MODERATED;
                break;
        }
        return tagReportTrigger;
    }

    private void updateStartTriggerPanel()
    {
	      if(comboStartTriggerType.getText().equals("Immediate"))
	    	{
		    	  labelStartDate.setVisible(false);
		    	  labelStartTime.setVisible(false);
		    	  date.setVisible(false);
		    	  time.setVisible(false);
		    	  labelPeriod.setVisible(false);
		    	  textPeriod.setVisible(false);
		    	  labelGPIStartTriggerEvent.setVisible(false);
		    	  comboGPIPortStart.setVisible(false);
		    	  checkboxStartTriggerHighToLow.setVisible(false);
		    	  checkboxStartTriggerLowToHigh.setVisible(false);
		    	  labelHandheldEvent.setVisible(false);
		    	  checkboxStartTriggerReleased.setVisible(false);
		    	  checkboxStartTriggerPressed.setVisible(false);
	    	  
	    	}
	      else if(comboStartTriggerType.getText().equals("Periodic"))
	      {
	    	  labelStartDate.setVisible(true);
	    	  labelStartTime.setVisible(true);
	    	  date.setVisible(true);
	    	  time.setVisible(true);
	    	  labelPeriod.setVisible(true);
	    	  textPeriod.setVisible(true);
	    	  labelGPIStartTriggerEvent.setVisible(false);
	    	  comboGPIPortStart.setVisible(false);
	    	  checkboxStartTriggerHighToLow.setVisible(false);
	    	  checkboxStartTriggerLowToHigh.setVisible(false);
	    	  labelHandheldEvent.setVisible(false);
	    	  checkboxStartTriggerReleased.setVisible(false);
	    	  checkboxStartTriggerPressed.setVisible(false);
	    	  
	    	  
	      }
	      else if(comboStartTriggerType.getText().equals("GPI"))
	      {
	    	  labelStartDate.setVisible(false);
	    	  labelStartTime.setVisible(false);
	    	  date.setVisible(false);
	    	  time.setVisible(false);
	    	  labelPeriod.setVisible(false);
	    	  textPeriod.setVisible(false);
	    	  labelGPIStartTriggerEvent.setVisible(true);
	    	  comboGPIPortStart.setVisible(true);
	    	  checkboxStartTriggerHighToLow.setVisible(true);
	    	  checkboxStartTriggerLowToHigh.setVisible(true);
	    	  labelHandheldEvent.setVisible(false);
	    	  checkboxStartTriggerReleased.setVisible(false);
	    	  checkboxStartTriggerPressed.setVisible(false);			    	  
	      }
	      else if(comboStartTriggerType.getText().equals("Handheld Trigger"))
	      {
	    	  labelStartDate.setVisible(false);
	    	  labelStartTime.setVisible(false);
	    	  date.setVisible(false);
	    	  time.setVisible(false);
	    	  labelPeriod.setVisible(false);
	    	  textPeriod.setVisible(false);
	    	  labelGPIStartTriggerEvent.setVisible(false);
	    	  comboGPIPortStart.setVisible(false);
	    	  checkboxStartTriggerHighToLow.setVisible(false);
	    	  checkboxStartTriggerLowToHigh.setVisible(false);
	    	  labelHandheldEvent.setVisible(true);
	    	  checkboxStartTriggerReleased.setVisible(true);
	    	  checkboxStartTriggerPressed.setVisible(true);			    	  
	      
	      }
    }
    
    private void updateStopTriggerPanel()
    {
		if(comboStopTriggerType.getText().equals("Immediate"))
		{
			labelDuration.setVisible(false);
			textDuration.setVisible(false);
			labelGPIPort.setVisible(false);
			comboGPIPortStop.setVisible(false);
			labelGPITimeOut.setVisible(false);
			textGPITimeOut.setVisible(false);
			labelGPIStopTriggerEvent.setVisible(false);
			checkboxStopTriggerHighToLow.setVisible(false);
			checkboxStopTriggerLowToHigh.setVisible(false);	
			labelTagObservation.setVisible(false);
			textTagObservation.setVisible(false);
			labelTime.setVisible(false);
			textTime.setVisible(false);
			labelNoOfAttempts.setVisible(false);
			textNoOfAttempts.setVisible(false);	
			labelNAttemptsTimeOut.setVisible(false);
			textNAttemptsTimeOut.setVisible(false);
			labelHandheldTriggerTimeOut.setVisible(false);
			textHandheldTriggerTimeOut.setVisible(false);	
			labelHandheldTriggerEvent.setVisible(false);
			checkboxStopTriggerReleased.setVisible(false);
			checkboxStopTriggerPressed.setVisible(false);	  
	    }
		else if(comboStopTriggerType.getText().equals("Duration"))
		{
			labelDuration.setVisible(true);
			textDuration.setVisible(true);
			labelGPIPort.setVisible(false);
			comboGPIPortStop.setVisible(false);
			labelGPITimeOut.setVisible(false);
			textGPITimeOut.setVisible(false);
			labelGPIStopTriggerEvent.setVisible(false);
			checkboxStopTriggerHighToLow.setVisible(false);
			checkboxStopTriggerLowToHigh.setVisible(false);	
			labelTagObservation.setVisible(false);
			textTagObservation.setVisible(false);
			labelTime.setVisible(false);
			textTime.setVisible(false);
			labelNoOfAttempts.setVisible(false);
			textNoOfAttempts.setVisible(false);	
			labelNAttemptsTimeOut.setVisible(false);
			textNAttemptsTimeOut.setVisible(false);
			labelHandheldTriggerTimeOut.setVisible(false);
			textHandheldTriggerTimeOut.setVisible(false);	
			labelHandheldTriggerEvent.setVisible(false);
			checkboxStopTriggerReleased.setVisible(false);
			checkboxStopTriggerPressed.setVisible(false);
		}
		else if(comboStopTriggerType.getText().equals("GPI with TimeOut"))
		{
			labelDuration.setVisible(false);
			textDuration.setVisible(false);
			labelGPIPort.setVisible(true);
			comboGPIPortStop.setVisible(true);
			labelGPITimeOut.setVisible(true);
			textGPITimeOut.setVisible(true);
			labelGPIStopTriggerEvent.setVisible(true);
			checkboxStopTriggerHighToLow.setVisible(true);
			checkboxStopTriggerLowToHigh.setVisible(true);	
			labelTagObservation.setVisible(false);
			textTagObservation.setVisible(false);
			labelTime.setVisible(false);
			textTime.setVisible(false);
			labelNoOfAttempts.setVisible(false);
			textNoOfAttempts.setVisible(false);	
			labelNAttemptsTimeOut.setVisible(false);
			textNAttemptsTimeOut.setVisible(false);
			labelHandheldTriggerTimeOut.setVisible(false);
			textHandheldTriggerTimeOut.setVisible(false);	
			labelHandheldTriggerEvent.setVisible(false);
			checkboxStopTriggerReleased.setVisible(false);
			checkboxStopTriggerPressed.setVisible(false);
		}
		else if(comboStopTriggerType.getText().equals("Tag Observation"))
		{
			labelDuration.setVisible(false);
			textDuration.setVisible(false);
			labelGPIPort.setVisible(false);
			comboGPIPortStop.setVisible(false);
			labelGPITimeOut.setVisible(false);
			textGPITimeOut.setVisible(false);
			labelGPIStopTriggerEvent.setVisible(false);
			checkboxStopTriggerHighToLow.setVisible(false);
			checkboxStopTriggerLowToHigh.setVisible(false);	
			labelTagObservation.setVisible(true);
			textTagObservation.setVisible(true);
			labelTime.setVisible(true);
			textTime.setVisible(true);
			labelNoOfAttempts.setVisible(false);
			textNoOfAttempts.setVisible(false);	
			labelNAttemptsTimeOut.setVisible(false);
			textNAttemptsTimeOut.setVisible(false);
			labelHandheldTriggerTimeOut.setVisible(false);
			textHandheldTriggerTimeOut.setVisible(false);	
			labelHandheldTriggerEvent.setVisible(false);
			checkboxStopTriggerReleased.setVisible(false);
			checkboxStopTriggerPressed.setVisible(false);
		}			
		else if(comboStopTriggerType.getText().equals("N Attempts"))
		{
			labelDuration.setVisible(false);
			textDuration.setVisible(false);
			labelGPIPort.setVisible(false);
			comboGPIPortStop.setVisible(false);
			labelGPITimeOut.setVisible(false);
			textGPITimeOut.setVisible(false);
			labelGPIStopTriggerEvent.setVisible(false);
			checkboxStopTriggerHighToLow.setVisible(false);
			checkboxStopTriggerLowToHigh.setVisible(false);	
			labelTagObservation.setVisible(false);
			textTagObservation.setVisible(false);
			labelTime.setVisible(false);
			textTime.setVisible(false);
			labelNoOfAttempts.setVisible(true);
			textNoOfAttempts.setVisible(true);	
			labelNAttemptsTimeOut.setVisible(true);
			textNAttemptsTimeOut.setVisible(true);
			labelHandheldTriggerTimeOut.setVisible(false);
			textHandheldTriggerTimeOut.setVisible(false);	
			labelHandheldTriggerEvent.setVisible(false);
			checkboxStopTriggerReleased.setVisible(false);
			checkboxStopTriggerPressed.setVisible(false);		    	  
		}				
		else if(comboStopTriggerType.getText().equals("Handheld Trigger"))
		{
			labelDuration.setVisible(false);
			textDuration.setVisible(false);
			labelGPIPort.setVisible(false);
			comboGPIPortStop.setVisible(false);
			labelGPITimeOut.setVisible(false);
			textGPITimeOut.setVisible(false);
			labelGPIStopTriggerEvent.setVisible(false);
			checkboxStopTriggerHighToLow.setVisible(false);
			checkboxStopTriggerLowToHigh.setVisible(false);	
			labelTagObservation.setVisible(false);
			textTagObservation.setVisible(false);
			labelTime.setVisible(false);
			textTime.setVisible(false);
			labelNoOfAttempts.setVisible(false);
			textNoOfAttempts.setVisible(false);	
			labelNAttemptsTimeOut.setVisible(false);
			textNAttemptsTimeOut.setVisible(false);
			labelHandheldTriggerTimeOut.setVisible(true);
			textHandheldTriggerTimeOut.setVisible(true);	
			labelHandheldTriggerEvent.setVisible(true);
			checkboxStopTriggerReleased.setVisible(true);
			checkboxStopTriggerPressed.setVisible(true);
		}
    	
    }

    private void initializeTriggerDlg()
    {

        //numOfGPIS = RFIDMainDlg.rfidBase.getMyReader().ReaderCapabilities.getNumGPIPorts();
        numOfGPIS = 4;

        // Start Trigger Settings
        comboStartTriggerType.select(triggerInfo.StartTrigger.getTriggerType().getValue());
        updateStartTriggerPanel();

        //  GPI Port
        for (int index = 1; index <= numOfGPIS; index ++)
            comboGPIPortStart.add(String.valueOf(index));

        // Periodic
        textPeriod.setText(String.valueOf(triggerInfo.StartTrigger.Periodic.getPeriod()));
        //triggerInfo.StartTrigger.Periodic.
//        jFormattedTextFieldStartTriggerStartDate.setValue(new Date());
//        jFormattedTextFieldStartTriggerStartTime.setValue(new Date());


        // GPI
        if (triggerInfo.StartTrigger.GPI.isGPIEvent())
            checkboxStartTriggerLowToHigh.setSelection(true);
        else
            checkboxStartTriggerHighToLow.setSelection(true);

        int startPortIndex = triggerInfo.StartTrigger.GPI.getPortNumber();
        if (startPortIndex > 0) startPortIndex -= 1;
        comboGPIPortStart.select(startPortIndex);


        // Handheld
        if (triggerInfo.StartTrigger.Handheld.getHandheldTriggerEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED)
            checkboxStartTriggerPressed.setSelection(true);
        else
            checkboxStartTriggerReleased.setSelection(true);


        // Stop Trigger Settings

        //  GPI Port
        for (int index = 1; index <= numOfGPIS; index ++)
           comboGPIPortStop.add(String.valueOf(index));


        comboStopTriggerType.select(triggerInfo.StopTrigger.getTriggerType().getValue());
        updateStopTriggerPanel();

        // Duration Stop Trigger
        textDuration.setText(String.valueOf(triggerInfo.StopTrigger.getDurationMilliSeconds()));

        // N Attempts Stop Trigger
        textNoOfAttempts.setText(String.valueOf(triggerInfo.StopTrigger.NumAttempts.getN()));
        textNAttemptsTimeOut.setText(String.valueOf(triggerInfo.StopTrigger.NumAttempts.getTimeout()));

        // Tag Observation Stop Trigger
        textTagObservation.setText(String.valueOf(triggerInfo.StopTrigger.TagObservation.getN()));
        textTime.setText(String.valueOf(triggerInfo.StopTrigger.TagObservation.getTimeout()));

        // GPI Stop Trigger
        int stopPortIndex = triggerInfo.StopTrigger.GPI.getPortNumber();
        if (stopPortIndex > 0) stopPortIndex -= 1;
        comboGPIPortStop.select(stopPortIndex);
        textGPITimeOut.setText(String.valueOf(triggerInfo.StopTrigger.GPI.getTimeout()));
        if (triggerInfo.StopTrigger.GPI.isGPIEvent())
            checkboxStopTriggerLowToHigh.setSelection(true);  
        else
            checkboxStopTriggerHighToLow.setSelection(true);

        // Handheld Stop Trigger
        textHandheldTriggerTimeOut.setText(String.valueOf(triggerInfo.StopTrigger.Handheld.getHandheldTriggerTimeout()));
        if (triggerInfo.StopTrigger.Handheld.getHandheldTriggerEvent() == HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED )
            checkboxStopTriggerPressed.setSelection(true);
        else
            checkboxStopTriggerReleased.setSelection(true);

        // Tag Event Reporting Settings
        ComboNewTag.select(triggerInfo.TagEventReportInfo.getReportNewTagEvent().getValue());
        textNewTag.setText(String.valueOf(triggerInfo.TagEventReportInfo.getNewTagEventModeratedTimeoutMilliseconds()));

        ComboTagInvisible.select(triggerInfo.TagEventReportInfo.getReportTagInvisibleEvent().getValue());
        textTagInvisible.setText(String.valueOf(triggerInfo.TagEventReportInfo.getTagInvisibleEventModeratedTimeoutMilliseconds()));

        ComboTagbacktoVisibility.select(triggerInfo.TagEventReportInfo.getReportTagBackToVisibilityEvent().getValue());
        textTagbacktoVisibility.setText(String.valueOf(triggerInfo.TagEventReportInfo.getTagBackToVisibilityModeratedTimeoutMilliseconds()));

        textTagReportTrigger.setText(String.valueOf(triggerInfo.getTagReportTrigger()));
    }

    void applyButtonHandler()
    {
        // Start Trigger settings
        triggerInfo.StartTrigger.setTriggerType(getStartTriggerType(comboStartTriggerType.getSelectionIndex()));

        // GPI start trigger
        if (checkboxStartTriggerLowToHigh.getSelection())
            triggerInfo.StartTrigger.GPI.setGPIEvent(true);
        else
            triggerInfo.StartTrigger.GPI.setGPIEvent(false);
        triggerInfo.StartTrigger.GPI.setPortNumber(comboGPIPortStart.getSelectionIndex() + 1);

        // Handheld trigger
        if (checkboxStartTriggerPressed.getSelection())
            triggerInfo.StartTrigger.Handheld.setHandheldTriggerEvent(HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED);
        else
            triggerInfo.StartTrigger.Handheld.setHandheldTriggerEvent(HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED);

        // Periodic
        SYSTEMTIME startTime=new SYSTEMTIME();
        startTime.Year=(short) date.getYear();
        startTime.Month=(short) (date.getMonth()+1);      
        startTime.Day=(short) date.getDay();
        startTime.Hour=(short) time.getHours();      
        startTime.Minute=(short) time.getMinutes();       
        startTime.Second=(short) time.getSeconds();   
        startTime.Milliseconds=0;
        
        
        triggerInfo.StartTrigger.Periodic.StartTime=startTime;
        triggerInfo.StartTrigger.Periodic.setPeriod(Integer.parseInt(textPeriod.getText()));
        
        // Stop Trigger settings
        triggerInfo.StopTrigger.setTriggerType(getStopTriggerType(comboStopTriggerType.getSelectionIndex()));

        // Duration
        triggerInfo.StopTrigger.setDurationMilliSeconds(Integer.parseInt(textDuration.getText()));

        // N-Attempts
        triggerInfo.StopTrigger.NumAttempts.setN(Short.parseShort(textNoOfAttempts.getText()));
        triggerInfo.StopTrigger.NumAttempts.setTimeout(Integer.parseInt(textNAttemptsTimeOut.getText()));

        // Tag Observation
        triggerInfo.StopTrigger.TagObservation.setN(Short.parseShort(textTagObservation.getText()));
        triggerInfo.StopTrigger.TagObservation.setTimeout(Integer.parseInt(textTime.getText()));

        // GPI with Timeout
        if (checkboxStopTriggerLowToHigh.getSelection())
            triggerInfo.StopTrigger.GPI.setGPIEvent(true);
        else
            triggerInfo.StopTrigger.GPI.setGPIEvent(false);

        triggerInfo.StopTrigger.GPI.setPortNumber(comboGPIPortStop.getSelectionIndex() + 1);
        triggerInfo.StopTrigger.GPI.setTimeout(Integer.parseInt(textGPITimeOut.getText()));

        // Handheld Trigger
        triggerInfo.StopTrigger.Handheld.setHandheldTriggerTimeout(Integer.parseInt(textHandheldTriggerTimeOut.getText()));

        if (checkboxStopTriggerPressed.getSelection())
            triggerInfo.StopTrigger.Handheld.setHandheldTriggerEvent(HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_PRESSED);
        else
            triggerInfo.StopTrigger.Handheld.setHandheldTriggerEvent(HANDHELD_TRIGGER_EVENT_TYPE.HANDHELD_TRIGGER_RELEASED);
        
        // Report Trigger settings
        triggerInfo.TagEventReportInfo.setReportNewTagEvent(getReportTriggerEvent(ComboNewTag.getSelectionIndex()));
        triggerInfo.TagEventReportInfo.setNewTagEventModeratedTimeoutMilliseconds(Short.parseShort(textNewTag.getText()));

        triggerInfo.TagEventReportInfo.setReportTagInvisibleEvent(getReportTriggerEvent(ComboTagInvisible.getSelectionIndex()));
        triggerInfo.TagEventReportInfo.setTagInvisibleEventModeratedTimeoutMilliseconds(Short.parseShort(textTagInvisible.getText()));

        triggerInfo.TagEventReportInfo.setReportTagBackToVisibilityEvent(getReportTriggerEvent(ComboTagbacktoVisibility.getSelectionIndex()));
        triggerInfo.TagEventReportInfo.setTagBackToVisibilityModeratedTimeoutMilliseconds(Short.parseShort(textTagbacktoVisibility.getText()));

        // set report trigger
        triggerInfo.setTagReportTrigger(Integer.parseInt(textTagReportTrigger.getText()));

    }
}
