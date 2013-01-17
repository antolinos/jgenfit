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
            String sasPath = GenfitPropertiesReader.getGenfitFolder();

            File file = new File(sasPath);
            for (File child : file.listFiles()) {
                if (child.getName().equals(advancedFileName)){
                    return child;
                }
            }
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
    
    public static int getMaxPDB(){
        String content = AdvancedCommonParametersFile.getContent();
        List<String> lines = Arrays.asList(content.split("\n"));
        for (String line : lines) {
            if (line.contains("mxpdb")){
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
    
  }
