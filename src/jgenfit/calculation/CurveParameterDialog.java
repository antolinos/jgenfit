/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EditModelDialog.java
 *
 * Created on Feb 26, 2012, 1:46:17 PM
 */
package jgenfit.calculation;

import java.util.ArrayList;
import jgenfit.bussines.experiment.SingleExperiment;
import jgenfit.dialog.file.OpenJDialog;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import jgenfit.bussines.AdvancedCommonParametersFile;
import jgenfit.bussines.experiment.GenfitModel;
import jgenfit.dialog.file.PDBSelectorOpenJDialog;
import jgenfit.events.GenfitEvent;
import jgenfit.events.GenfitEventType;
import jgenfit.utils.GenfitLogger;

/**
 *
 * @author alex
 */
public class CurveParameterDialog extends javax.swing.JDialog {
    private GenfitModel model;
    public GenfitEvent genfitEvent;
    private SingleExperiment singleExperiment;

    /** Creates new form EditModelDialog */
    public CurveParameterDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.genfitEvent = new GenfitEvent(modal, GenfitEventType.SCATTERING_PARAMETERS_SAVE);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(jgenfit.JGenfitApp.class).getContext().getResourceMap(CurveParameterDialog.class);
        jButton1.setLabel(resourceMap.getString("jButton1.label")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setLabel(resourceMap.getString("jButton2.label")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "index", "key", "value"
            }
        ));
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setMinWidth(50);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(50);
        jTable1.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTable1.columnModel.title2")); // NOI18N
        jTable1.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTable1.columnModel.title0")); // NOI18N
        jTable1.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTable1.columnModel.title1")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(324, 324, 324)
                        .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 78, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 493, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 218, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .add(jButton1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
           this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       this.genfitEvent.fire();
       this.setVisible(false);
        
    }//GEN-LAST:event_jButton1ActionPerformed

  
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
            java.util.logging.Logger.getLogger(CurveParameterDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CurveParameterDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CurveParameterDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CurveParameterDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                CurveParameterDialog dialog = new CurveParameterDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

     private void cleanTable(){
        DefaultTableModel tableSubModel = (DefaultTableModel) this.jTable1.getModel();
        while (tableSubModel.getRowCount() != 0) {
            tableSubModel.removeRow(0);
        }
    }
     
    public void setExperiment(SingleExperiment singleExperiment) {  
        this.singleExperiment = singleExperiment;
        this.cleanTable();        
        DefaultTableModel tableSubModel = (DefaultTableModel) this.jTable1.getModel();     
        for (int i = 0; i < AdvancedCommonParametersFile.get_mxparexperiment(); i++){            
                    tableSubModel.addRow(new Object[]{(i + 1), singleExperiment.getScatteringParametersKey(i).trim(), singleExperiment.getScatteringParametersValue(i).trim()});                                                      
        } 
        
         this.setTitle("Curve Parameters (mxparexp = " + AdvancedCommonParametersFile.get_mxparexperiment() + ")");
    }

    public SingleExperiment getExperiment() {
        ArrayList<String> parameters = new ArrayList<String>();
       

        if(jTable1.isEditing()){
            jTable1.getCellEditor().stopCellEditing();
        }   
         
          DefaultTableModel tableSubModel = (DefaultTableModel) this.jTable1.getModel();    
        for (int i = 0; i < tableSubModel.getRowCount(); i++){    
            String key = this.jTable1.getModel().getValueAt(i, 1).toString(); 
            String value = this.jTable1.getModel().getValueAt(i, 2).toString();  
            GenfitLogger.debug(key + " = " + value);
            parameters.add(key + " = " + value);            
        }        
        GenfitLogger.debug("sss " +parameters.toString());
        this.singleExperiment.setScatteringCurveParameters(parameters);    
        return this.singleExperiment;
    }
}