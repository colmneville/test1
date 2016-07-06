package J_RFIDSample3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;



public class FileUtil {

  
  public static void saveFile(String contents, String filename, boolean append) {
      try{
        // Create file 
        FileWriter fstream = new FileWriter(filename,append);
            BufferedWriter out = new BufferedWriter(fstream);
        out.write(contents);
        //Close the output stream
        out.close();
        }catch (Exception e){//Catch exception if any
          System.err.println("Error: " + e.getMessage());
        }
    }
  
  public static void deleteFile(String filename) {
  	new File(filename).delete();
  }
  
  public static void clearFileContents(String filename) {
	  PrintWriter writer;
	  try {
		  writer = new PrintWriter(filename);

		  writer.print("");
		  writer.close();
	  } catch (FileNotFoundException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  }
  }

	public static String getFileContents(String filename) {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));

			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
		} catch (FileNotFoundException fnf) {
			System.out.println("Error getting file : " + fnf);
		} catch (IOException ioe) {
			System.out.println("Error getting file data: " + ioe);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				System.out.println("Error closing reader: " + e);

			}
		}
		return sb.toString();
	}
}
