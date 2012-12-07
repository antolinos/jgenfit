/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit.utils;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import jgenfit.MessageJDialog;

public class OutputDisplayer extends Thread implements Runnable, ThreadCompleteListener {

protected final JTextArea textArea_;
protected Reader reader_ = null;

public OutputDisplayer(JTextArea textArea) {
    textArea_ = textArea;
}

public void commence(Process proc) {
    InputStream in = proc.getInputStream();
    reader_ = new InputStreamReader(in);
    Thread thread = new Thread(this);
    thread.start();
    
    
    
}

public void run() {
    StringBuilder buf = new StringBuilder();
    try {
        
        StringBuffer buffer = new StringBuffer();
        BufferedReader br = new BufferedReader(reader_);
        String line = null;
        while ((line = br.readLine()) != null){
            System.out.println(line);
            buffer.append(line + "\n");            
            setText(buffer.toString());
        }
           MessageJDialog m = new MessageJDialog(null, true);
           m.setMessage("Execution has been finished successfully");
           m.setVisible(true);
       /* String message = "Execution has been finished successfully";
        String header = "";
        JFrame frame = new JFrame();
        frame.setTitle("Notification Message");
        frame.setSize(300,100);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0f;
        constraints.weighty = 1.0f;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        JLabel headingLabel = new JLabel(header);
        //headingLabel .setIcon(headingIcon); // --- use image icon you want to be as heading image.
        headingLabel.setOpaque(false);
        frame.add(headingLabel, constraints);
        constraints.gridx++;
        constraints.weightx = 0f;
        constraints.weighty = 0f;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.NORTH;
       
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.weightx = 1.0f;
        constraints.weighty = 1.0f;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Tahoma", 0, 15));
        frame.add(messageLabel, constraints);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);*/
        System.out.println("FINISHED");
       
         
    } catch ( IOException ioe ) {
        buf.append("\n\nERROR:\n"+ioe.toString());
        setText( buf.toString() );
    } finally {
        try {
            reader_.close();
        } catch ( IOException ioe ) {
            ioe.printStackTrace();
        }
    }
}


private void setText(final String text) {
    EventQueue.invokeLater(new Runnable() {
        public void run() {
           // textArea_.setText("");
           // System.err.println(text.length());
           
            textArea_.setText(text + "\n");
            //textArea_.setText(textArea_.getText() +  "\r");
               
        }
    });
}

    public void notifyOfThreadComplete(Thread thread) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}   