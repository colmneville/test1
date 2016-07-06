
package J_RFIDSample3;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.internal.ole.win32.COMObject;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.omg.CORBA.UserException;

import com.mot.rfid.api3.*;

public class PostFilterDlg {
	  Shell shell;

	  PostFilterDlg(Display display) {
	    
		  shell = new Shell(display, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		  //shell = new Shell(display);
		  shell.setSize(240, 188);
		  shell.setText("Post Filter");
		  final TabFolder TagPattern = new TabFolder (shell, SWT.NONE);
		  TagPattern.setBounds(5,0,230,120);
		  TabItem ItemTagPatternA = new TabItem (TagPattern, SWT.NULL);
		  ItemTagPatternA.setText ("Tag Pattern A");		  
		  TabItem ItemTagPatternB = new TabItem (TagPattern, SWT.NULL);
		  ItemTagPatternB.setText ("Tag Pattern B");	
		  Composite CompositeTagPatternA= new Composite(TagPattern, SWT.BORDER);
		  
		  final Label labelMemoryBank1=new  Label(CompositeTagPatternA, SWT.LEFT);
		  labelMemoryBank1.setText("Memory Bank");
		  labelMemoryBank1.setBounds(10,0,80,15);
		  
		  final Label labelBitOffset1=new  Label(CompositeTagPatternA, SWT.LEFT);
		  labelBitOffset1.setText("Bit Offset");
		  labelBitOffset1.setBounds(10,25,80,15);		  
		    
		  final Combo ComboMemoryBank1 = new Combo(CompositeTagPatternA, SWT.READ_ONLY);
		  ComboMemoryBank1.setBounds(95, 0, 80, 15);
		    String items[] = { "Reserved", "EPC", "TID", "User"};
		    ComboMemoryBank1.setItems(items);;
		  
		  final Text textBitOffset1 = new Text(CompositeTagPatternA, SWT.SINGLE);
		  textBitOffset1.setBounds(95, 25, 80, 15);
		  
		  final Label labelTagPattern1=new  Label(CompositeTagPatternA, SWT.LEFT);
		  labelTagPattern1.setText("Tag Pattern(Hex)");
		  labelTagPattern1.setBounds(10,50,80,15);		  
		  
		  final Text textTagPattern1 = new Text(CompositeTagPatternA, SWT.SINGLE);
		  textTagPattern1.setBounds(95, 50, 80, 15);

		  final Label labelTagMask1=new  Label(CompositeTagPatternA, SWT.LEFT);
		  labelTagMask1.setText("Tag Mask(Hex)");
		  labelTagMask1.setBounds(10,75,80,15);		  
		    
		  final Text textTagMask1 = new Text(CompositeTagPatternA, SWT.SINGLE);
		  textTagMask1.setBounds(95, 75, 80, 15);
		  ItemTagPatternA.setControl(CompositeTagPatternA);

		  Composite CompositeTagPatternB= new Composite(TagPattern, SWT.BORDER);
		  
		  final Label labelMemoryBank2=new  Label(CompositeTagPatternB, SWT.LEFT);
		  labelMemoryBank2.setText("Memory Bank");
		  labelMemoryBank2.setBounds(10,0,80,15);
		  
		  final Label labelBitOffset2=new  Label(CompositeTagPatternB, SWT.LEFT);
		  labelBitOffset2.setText("Bit Offset");
		  labelBitOffset2.setBounds(10,25,80,15);		  
		    
		  final Combo ComboMemoryBank2 = new Combo(CompositeTagPatternB, SWT.READ_ONLY);
		  ComboMemoryBank2.setBounds(95, 0, 80, 15);
		    ComboMemoryBank2.setItems(items);;
	
		  final Text textBitOffset2 = new Text(CompositeTagPatternB, SWT.SINGLE);
		  textBitOffset2.setBounds(95, 25, 80, 15);
		  
		  final Label labelTagPattern2=new  Label(CompositeTagPatternB, SWT.LEFT);
		  labelTagPattern2.setText("Tag Pattern(Hex)");
		  labelTagPattern2.setBounds(10,50,80,15);		  
		    
		  final Text textTagPattern2 = new Text(CompositeTagPatternB, SWT.SINGLE);
		  textTagPattern2.setBounds(95, 50, 80, 15);

		  final Label labelTagMask2=new  Label(CompositeTagPatternB, SWT.LEFT);
		  labelTagMask2.setText("Tag Mask(Hex)");
		  labelTagMask2.setBounds(10,75,80,15);		  
		    
		  final Text textTagMask2 = new Text(CompositeTagPatternB, SWT.SINGLE);
		  textTagMask2.setBounds(95, 75, 80, 15);
		  ItemTagPatternB.setControl(CompositeTagPatternB);
		  
		  final Label labelMatch=new  Label(shell, SWT.LEFT);
		  labelMatch.setText("Match");
		  labelMatch.setBounds(25,122,40,15);
		  
		  final Combo ComboMatch = new Combo(shell, SWT.READ_ONLY);
		  ComboMatch.setBounds(105,122,100,15);
		    String MatchItems[] = { "A_AND_B", "NOTA_AND_B", "NOTA_AND_NOTB", "A_AND_NOTB", "A" };
		    ComboMatch.setItems(MatchItems);

		  final Button buttonUseFilter = new Button(shell, SWT.CHECK);
		  buttonUseFilter.setBounds(25, 145, 100, 15);
		  buttonUseFilter.setText("Use Filter");	
		  
		  final Button buttonApply = new Button(shell, SWT.PUSH);
		  buttonApply.setBounds(165, 145, 60, 15);
		  buttonApply.setText("Apply");
			  

	        PostFilter postFilter = RFIDMainDlg.rfidBase.postFilter;

	        // Set the Memory Bank to EPC
	        ComboMemoryBank1.select(postFilter.TagPatternA.getMemoryBank().getValue());
	        ComboMemoryBank2.select(postFilter.TagPatternB.getMemoryBank().getValue());

	        // Bit offset
	        textBitOffset1.setText(String.valueOf(postFilter.TagPatternA.getBitOffset()));
	        textBitOffset2.setText(String.valueOf(postFilter.TagPatternB.getBitOffset()));

	        // Tag Pattern
	        if (postFilter.TagPatternA.getTagPattern() != null)
	        {
	            String tagPatternA = RFIDBase.byteArrayToHexString(postFilter.TagPatternA.getTagPattern());
	            textTagPattern1.setText(tagPatternA);
	        }
	        if (postFilter.TagPatternB.getTagPattern() != null)
	        {
	            String tagPatternB = RFIDBase.byteArrayToHexString(postFilter.TagPatternB.getTagPattern());
	            textTagPattern2.setText(tagPatternB);
	        }
	   
	        // Tag Mask
	        if (postFilter.TagPatternA.getTagMask() != null)
	        {
	            String tagMaskA = RFIDBase.byteArrayToHexString(postFilter.TagPatternA.getTagMask());
	            textTagMask1.setText(tagMaskA);
	        }

	        if (postFilter.TagPatternB.getTagMask() != null)
	        {
	            String tagMaskB = RFIDBase.byteArrayToHexString(postFilter.TagPatternB.getTagMask());
	            textTagMask2.setText(tagMaskB);
	        }

	        ComboMatch.select(postFilter.getPostFilterMatchPattern().getValue());

	        buttonUseFilter.setSelection(RFIDMainDlg.rfidBase.isPostFilterSet);

	        buttonApply.addSelectionListener(new SelectionListener() {
				
				public void widgetSelected(SelectionEvent e) {
			        PostFilter postFilter = RFIDMainDlg.rfidBase.postFilter;
			        postFilter.TagPatternA.setMemoryBank(RFIDMainDlg.rfidBase.getMemoryBankEnum(ComboMemoryBank1.getSelectionIndex()));
			        postFilter.TagPatternB.setMemoryBank(RFIDMainDlg.rfidBase.getMemoryBankEnum(ComboMemoryBank2.getSelectionIndex()));

			        postFilter.TagPatternA.setBitOffset(Integer.parseInt(textBitOffset1.getText()));
			        postFilter.TagPatternB.setBitOffset(Integer.parseInt(textBitOffset2.getText()));

			        postFilter.TagPatternA.setTagPattern(RFIDBase.hexStringToByteArray(textTagPattern1.getText()));
			        postFilter.TagPatternB.setTagPattern(RFIDBase.hexStringToByteArray(textTagPattern2.getText()));

			        postFilter.TagPatternA.setTagMask(RFIDBase.hexStringToByteArray(textTagMask1.getText()));
			        postFilter.TagPatternB.setTagMask(RFIDBase.hexStringToByteArray(textTagMask2.getText()));

			        postFilter.TagPatternA.setTagPatternBitCount(RFIDBase.hexStringToByteArray(textTagPattern1.getText()).length * 8);
			        postFilter.TagPatternB.setTagPatternBitCount(RFIDBase.hexStringToByteArray(textTagPattern2.getText()).length * 8);

			        postFilter.TagPatternA.setTagMaskBitCount(RFIDBase.hexStringToByteArray(textTagMask1.getText()).length * 8);
			        postFilter.TagPatternB.setTagMaskBitCount(RFIDBase.hexStringToByteArray(textTagMask2.getText()).length * 8);

			        FILTER_MATCH_PATTERN patternMatch = FILTER_MATCH_PATTERN.A;
			        switch (ComboMatch.getSelectionIndex())
			        {
			            case 0:
			                patternMatch = FILTER_MATCH_PATTERN.A_AND_B;
			                break;
			            case 1:
			                patternMatch = FILTER_MATCH_PATTERN.NOTA_AND_B;
			                break;
			            case 2:
			                patternMatch = FILTER_MATCH_PATTERN.NOTA_AND_NOTB;
			                break;
			            case 3:
			                patternMatch = FILTER_MATCH_PATTERN.A_AND_NOTB;
			                break;
			            case 4:
			                patternMatch = FILTER_MATCH_PATTERN.A;
			                break;

			        }

			        postFilter.setPostFilterMatchPattern(patternMatch);

			        if (buttonUseFilter.getSelection() == true)
			            RFIDMainDlg.rfidBase.isPostFilterSet = true;
			        else
			            RFIDMainDlg.rfidBase.isPostFilterSet = false;
				}
				
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
		  shell.open();
		  }
}