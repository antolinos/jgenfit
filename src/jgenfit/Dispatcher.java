/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit;

import java.io.*;
import javax.swing.JTextArea;
/**
 *
 * @author alex
 */
public class Dispatcher {
    
    public static void Execute(String command){
    
    try {
      String line;
      Process p = Runtime.getRuntime().exec(command);
      BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
      BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
      while ((line = bri.readLine()) != null) {
        System.out.println(line);
      }
      bri.close();
      while ((line = bre.readLine()) != null) {
        System.out.println(line);
      }
      bre.close();
      p.waitFor();
      System.out.println("Done.");
    }
    catch (Exception err) {
      err.printStackTrace();
    }
    
    
    }
    
    
    public static void Execute(String command, JTextArea jtextArea){
    
    try {
      String line;
      Process p = Runtime.getRuntime().exec(command);
      BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
      BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
      while ((line = bri.readLine()) != null) {
        System.out.println(line);
        jtextArea.setText(jtextArea.getText() + "\n" + line);
      }
      bri.close();
      while ((line = bre.readLine()) != null) {
        System.out.println(line);
        jtextArea.setText(jtextArea.getText() + "\n" + line);
      }
      bre.close();
      p.waitFor();
      System.out.println("Done.");
      
    }
    catch (Exception err) {
      err.printStackTrace();
    }
    
    
    }
}
