/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit.dialog.file;

import jgenfit.bussines.AdvancedCommonParametersFile;
import jgenfit.bussines.GenfitController;

/**
 *
 * @author DEMARIAA
 */
public class AdvancedPropertiesEditFileJDialog extends EditFileJDialog {
    private final GenfitController genfitController;
     /** Creates new form EditFileJDialog */
    public AdvancedPropertiesEditFileJDialog(java.awt.Frame parent, boolean modal, GenfitController controller) {
        super(parent, modal);
       this.genfitController = controller;
    }
    
   
    public void onAfterSaved(){
        System.out.println("After saved");
        this.genfitController.setMaxPDB(AdvancedCommonParametersFile.get_maxpdb());
    }
}
