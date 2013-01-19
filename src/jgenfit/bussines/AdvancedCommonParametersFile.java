/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit.bussines;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jgenfit.utils.FileUtils;
import jgenfit.utils.GenfitLogger;
import utils.GenfitPropertiesReader;

/**
 *
 * @author DEMARIAA
 */
public class AdvancedCommonParametersFile{
    private static File getFile() throws FileNotFoundException, IOException{
            String advancedFileName = GenfitPropertiesReader.readAdvancedCommonsSettingsfile();
            String sasPath = GenfitPropertiesReader.getGenfitFolderAbsolutePath();

            File file = new File(sasPath);
            if (file.exists()){
                for (File child : file.listFiles()) {
                    if (child.getName().equals(advancedFileName)){
                        return child;
                    }
                }
            }
            else{
                GenfitLogger.error(sasPath + " doesn't exist or not set yet");
            }
            GenfitLogger.error(advancedFileName + " not found. Go to Settings/Options and set a SAS home path");
            return null;
    }
    
    public static String getContent(){
          File file = null;
        try {
            file = AdvancedCommonParametersFile.getFile();
            if (file != null){
                         
                String content = FileUtils.readFile(file);                    
                return content;
            }            
        } catch (FileNotFoundException ex) {
            GenfitLogger.error("File not found: " + file.getAbsolutePath());
            Logger.getLogger(AdvancedCommonParametersFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            GenfitLogger.error("AdvancedCommonParametersFile: " + ex.getMessage());
            Logger.getLogger(AdvancedCommonParametersFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
 
    
    private static int getParameter(String key){
      String content = AdvancedCommonParametersFile.getContent();
        List<String> lines = Arrays.asList(content.split("\n"));
        for (String line : lines) {
            if (line.contains(key)){
                try{
                    return Integer.parseInt(line.subSequence(30, line.length()).toString());
                }
                catch(Exception e){
                    GenfitLogger.error("Reading maxPDB");
                }
            }            
        }        
        return -1;
    
    }
    
    

    public static int get_mxparexperiment() {
         return getParameter("mxparexp");
    }
    
       public static int get_maxpdb(){
        return getParameter("mxpdb");
    }
    
  }
