/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author DEMARIAA
 */
public class MyRunnable implements Runnable {
  
    public  String[] command;

  
   
   public void run() { 
//       String[] s = {this.gnuPlotFilePath,
//              "-e",
//              this.jTextArea1.getText()
//             };
         
    try {
        Process process = Runtime.getRuntime().exec(this.command);
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(this.command);
        InputStream stdin = proc.getErrorStream();
        InputStreamReader isr = new InputStreamReader(stdin);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        while ((line = br.readLine()) != null)
            System.err.println("compiling error:"+ this.command.toString());
        int exitVal = proc.waitFor();
        if (exitVal != 0)
            System.out.println("compiling Process exitValue: " + exitVal);
        proc.getInputStream().close();
        proc.getOutputStream().close();
        proc.getErrorStream().close();
    } catch (Exception e) {
        System.err.println("Fail: " + e);
    }
   } 
}
