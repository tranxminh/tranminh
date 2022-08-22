

import java.awt.Container;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.UnsupportedLookAndFeelException;


public class ShowFile extends JFrame{ 
	
	
	public void ShowFile() {
			
		 try {
			  
			 for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
	              if ("Nimbus".equals(info.getName())) {
	                  javax.swing.UIManager.setLookAndFeel(info.getClassName());
	                  File myObj = new File(info.getClassName());
	                  Scanner myReader = new Scanner(myObj);  
	                  while (myReader.hasNextLine()) {
	                    String data = myReader.nextLine();
	                    System.out.println(data);
	                  }
	                  myReader.close();
	                  break;
	              }
	          }
	                 
	             
	          }
	  catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	            
	}
  public static void main(String[] args) throws FileNotFoundException { 
	  ShowFile frame = new ShowFile();
	 
	 
     
    }
  
  }  
 