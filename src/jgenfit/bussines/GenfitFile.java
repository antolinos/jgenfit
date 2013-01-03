/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit.bussines;

import java.io.BufferedReader;
import java.io.*;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alex
 */
public class GenfitFile extends File {
    private final String content;
    
    
    public GenfitFile(String filePath) {
        super(filePath);
        
        this.content = this.getContent();
    }
    
    public String getContent(){
        try {
            StringBuilder content = new StringBuilder();
            String strLine = new String();
           // Open the file that is the first 
            // command line parameter
            FileInputStream fstream = new FileInputStream(this.getAbsolutePath());
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                content.append(strLine + "\n");
                
            }
            return content.toString();
        } catch (IOException ex) {
            Logger.getLogger(GenfitFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return null;
    }
  
    
    
    /** Read the file and retuns the string corresponding with the single experimental section 
     * from "SINGLE EXPERIMENT SECTION" to "MODEL LIST #############"
     **/
    public String getExperimentalSectionText(){
        try{
            StringBuilder experimentalSectionText = new StringBuilder();
            boolean insideTargetRegion = false;            
            List<String> lines = Arrays.asList(this.content.split("\n"));
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).contains("SINGLE EXPERIMENT SECTION")){
                    insideTargetRegion = true;
                    
                }
                
                if (lines.get(i).contains("MODEL LIST #############")){
                    insideTargetRegion = false;
                    return experimentalSectionText.toString();
                }
                
                if (insideTargetRegion){
                    experimentalSectionText.append(lines.get(i) + "\n");
                }
            }            
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }        
        return content.toString();
    }
    
    
    public String getModelListText(){        
        return this.getContent().substring(this.getContent().indexOf("MODEL LIST #############"));    
    }
    
    
    public String getHeaderSection(){
        return this.getContent().substring(this.getContent().indexOf(" GENERAL SECTION"), this.getContent().indexOf(" SINGLE EXPERIMENT SECTION"));    
    }

    
    
    
}
