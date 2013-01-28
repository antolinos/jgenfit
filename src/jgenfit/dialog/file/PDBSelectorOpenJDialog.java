/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit.dialog.file;

import java.io.File;
import javax.swing.JTable;
import jgenfit.EditModelDialog;

/**
 *
 * @author DEMARIAA
 */
public class PDBSelectorOpenJDialog extends OpenJDialog {
    /** this table is the visual table component where the parameters are showed **/
    private final JTable table;
    private final int selectedRow;
    private EditModelDialog editModelDialog;
     public PDBSelectorOpenJDialog(java.awt.Frame parent, boolean modal, JTable table, int selectedRow) {
        super(parent, modal);
        this.table = table;
        this.selectedRow = selectedRow;
       
    }
      public void setEditModelDialog(EditModelDialog editModelDialog){
          this.editModelDialog = editModelDialog;
          
     }
      
     public void onFileSelected(File file){
          this.table.getModel().setValueAt(file.getAbsolutePath(), this.selectedRow, 1 );
          if (this.editModelDialog != null){
              this.editModelDialog.lastOpenefile = file.getAbsolutePath();
          }
     }
}
