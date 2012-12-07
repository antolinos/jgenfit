/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * RunJDialog.java
 *
 * Created on Nov 26, 2011, 7:20:01 PM
 */
package jgenfit;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import jgenfit.bussines.GenfitController;
import jgenfit.bussines.experiment.GeneralSection;
import jgenfit.events.GenfitEvent;
import jgenfit.events.GenfitEventListener;
import jgenfit.events.GenfitEventType;
import utils.GenfitPropertiesReader;

/**
 *
 * @author alex
 */
public class ExploreResultsJDialog extends javax.swing.JDialog {

   
 
    
    private String outputFolder;
    private String gnuPlotFilePath;

    /** Creates new form RunJDialog */
    public ExploreResultsJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();


        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("settings.properties"));
          
            this.outputFolder = (prop.getProperty("outputfolder"));
            this.gnuPlotFilePath = (prop.getProperty("gnuplot"));
            this.populate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getOutputFolder() {

        return outputFolder;
    }
    

    
    
    public void populate() {
        this.jList1.removeAll();
        
        this.jList1.setEnabled(false);
        if (this.getOutputFolder() != null) {
            File outpuFolderFile = new File(this.getOutputFolder());
            File[] listOfFiles = outpuFolderFile.listFiles();
            
            Arrays.sort(listOfFiles, new Comparator<File>(){
                    public int compare(File f1, File f2)
                    {
                        return -1*Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                    } });
            
            DefaultListModel listModel = new DefaultListModel();
            this.jList1.setModel(listModel);
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {

                    if (!this.jTextFieldFilter.getText().isEmpty()) {
                        if (listOfFiles[i].getName().contains(this.jTextFieldFilter.getText())) {
                            listModel.addElement(listOfFiles[i].getName());
                        }
                    } else {
                        listModel.addElement(listOfFiles[i].getName());
                    }
                    //System.out.println("File " + listOfFiles[i].getName());
                } else if (listOfFiles[i].isDirectory()) {
                    //System.out.println("Directory " + listOfFiles[i].getName());
                }
            }

            this.jList1.setModel(listModel);
            this.jList1.setEnabled(true);
        }


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldFilter = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButtonSaveParameterFile2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(jgenfit.JGenfitApp.class).getContext().getResourceMap(ExploreResultsJDialog.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setName("Form"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jList1.setName("jList1"); // NOI18N
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jList1);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setFont(resourceMap.getFont("jTextArea1.font")); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText(resourceMap.getString("jTextArea1.text")); // NOI18N
        jTextArea1.setName("jTextArea1"); // NOI18N
        jScrollPane1.setViewportView(jTextArea1);

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jTextFieldFilter.setText(resourceMap.getString("jTextFieldFilter.text")); // NOI18N
        jTextFieldFilter.setName("jTextFieldFilter"); // NOI18N
        jTextFieldFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldFilterKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextFieldFilterKeyTyped(evt);
            }
        });

        jButton6.setText(resourceMap.getString("jButton6.text")); // NOI18N
        jButton6.setName("jButton6"); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButtonSaveParameterFile2.setText(resourceMap.getString("jButtonSaveParameterFile2.text")); // NOI18N
        jButtonSaveParameterFile2.setName("jButtonSaveParameterFile2"); // NOI18N
        jButtonSaveParameterFile2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveParameterFile2ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jTextFieldFilter, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                        .add(10, 10, 10))
                    .add(layout.createSequentialGroup()
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 172, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(10, 10, 10)
                        .add(jButtonSaveParameterFile2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 387, Short.MAX_VALUE)
                        .add(jButton6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 629, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel1)
                            .add(jTextFieldFilter, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane2))
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 393, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonSaveParameterFile2)
                    .add(jButton6))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

  
    private File getSelectedFile(){
             File[] files = new File(this.getOutputFolder()).listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().equals(this.jList1.getSelectedValue().toString())) {
                        return files[i];
                    }
                }
                return null;
    }
    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList1ValueChanged

        if (this.jList1.getSelectedValue() == null) {
            return;
        }
        
        File file = this.getSelectedFile();//new File(this.getOutputFolder() + "/" + this.jList1.getSelectedValue());

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        DataInputStream dis = null;
        StringBuilder br = new StringBuilder();
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            dis = new DataInputStream(bis);

            while (dis.available() != 0) {
                br.append(dis.readLine() + "\n");
            }

            this.jTextArea1.setText(br.toString());
            // dispose all the resources after using them.
            fis.close();
            bis.close();
            dis.close();


           

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jList1ValueChanged

    private void jTextFieldFilterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldFilterKeyTyped
    }//GEN-LAST:event_jTextFieldFilterKeyTyped

    private void jTextFieldFilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldFilterKeyReleased
        this.populate();
    }//GEN-LAST:event_jTextFieldFilterKeyReleased

    private void writeFile(String filename, String text) throws IOException {
        FileOutputStream fos = null;
        try {
           
            fos = new FileOutputStream(filename);
            fos.write(text.getBytes("UTF-8"));
        } catch (IOException e) {
            fos.close();
            throw e;
        }
    }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

private void jButtonSaveParameterFile2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveParameterFile2ActionPerformed
    System.out.println("Open with gnuPlot");
    
    MyRunnableGnuPlot myRunnable = new MyRunnableGnuPlot();
    myRunnable.gnuCommand = this.getSelectedFile().getAbsolutePath(); //this.jTextArea1.getText();
    myRunnable.gnuPlotFilePath = this.gnuPlotFilePath;
    myRunnable.folder = this.getSelectedFile().getParentFile().getAbsolutePath();
    Thread myThread = new Thread(myRunnable);
    myThread.setDaemon(true); // important, otherwise jvm does not exit at end of main()
    myThread.start();

    
    
   
}//GEN-LAST:event_jButtonSaveParameterFile2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ExploreResultsJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExploreResultsJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExploreResultsJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExploreResultsJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                ExploreResultsJDialog dialog = new ExploreResultsJDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButtonSaveParameterFile2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextFieldFilter;
    // End of variables declaration//GEN-END:variables

 
}

class MyRunnableGnuPlot implements Runnable { 
   public String gnuPlotFilePath;
   public String gnuCommand;
   public String folder;
   
   public void run() { 
//       String[] s = {this.gnuPlotFilePath,
//              "-e",
//              this.jTextArea1.getText()
//             };
         String[] s = {gnuPlotFilePath,
//              "-e",
              gnuCommand
             };
    try {
        
        System.out.println("GnuPlot Path: " + gnuPlotFilePath);
         System.out.println("folder: " + folder);
        System.out.println("GnuPlot command: " + gnuCommand);
         
        
        ProcessBuilder pb = new ProcessBuilder(gnuPlotFilePath, gnuCommand);
        pb.directory(new File(folder));        
      
        //Process process =   pb.start();// Runtime.getRuntime().exec(s);
       // Runtime rt = Runtime.getRuntime();
        Process proc = pb.start();
        InputStream stdin = proc.getErrorStream();
        InputStreamReader isr = new InputStreamReader(stdin);
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        while ((line = br.readLine()) != null)
            System.err.println("gnuplot:"+line);
        int exitVal = proc.waitFor();
        if (exitVal != 0)
            System.out.println("gnuplot Process exitValue: " + exitVal);
        proc.getInputStream().close();
        proc.getOutputStream().close();
        proc.getErrorStream().close();
    } catch (Exception e) {
        System.err.println("Fail: " + e);
    }
   } 
}