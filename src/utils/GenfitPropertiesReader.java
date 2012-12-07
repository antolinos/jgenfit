/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DEMARIAA
 */
public class GenfitPropertiesReader {
    
    public static String getFourierFile() throws FileNotFoundException, IOException{
            return  GenfitPropertiesReader.readParameter("fourier_file");
    }
    
    public static void setFourierFile(String value) throws FileNotFoundException, IOException{
             GenfitPropertiesReader.writeParameter("fourier_file", value);
    }
    
    public static String getFourierFlag() throws FileNotFoundException, IOException{
            return  GenfitPropertiesReader.readParameter("fourier_flag");
    }
    
    public static void setFourierFlag(String value) throws FileNotFoundException, IOException{
             GenfitPropertiesReader.writeParameter("fourier_flag", value);
    }
     
    public static String getParameterFlag() throws FileNotFoundException, IOException{
            return  GenfitPropertiesReader.readParameter("parameter_flag");
    }
    
      public static void setParameterFlag(String value) throws FileNotFoundException, IOException{
             GenfitPropertiesReader.writeParameter("parameter_flag", value);
    }
      
    public static String getParameterFile() throws FileNotFoundException, IOException{
            return  GenfitPropertiesReader.readParameter("parameter_file");
    }
    
    public static void setParameterFile(String value) throws FileNotFoundException, IOException{
             GenfitPropertiesReader.writeParameter("parameter_file", value);
    }
       
    public static String readLastOpenedFile() throws FileNotFoundException, IOException{
            return  GenfitPropertiesReader.readParameter("last_file");
    }
    
    
    public static void setOutputFolder(String value) throws FileNotFoundException, IOException {
           GenfitPropertiesReader.writeParameter("outputfolder", value);
    }
     
     public static String getOutputFolder() throws FileNotFoundException, IOException{
            return  GenfitPropertiesReader.readParameter("outputfolder");
    }
    
    public static String readAdvancedSettingsfile() throws FileNotFoundException, IOException {
         return  GenfitPropertiesReader.readParameter("advaced_settings_file");
    }
    
    public static void setGenfitFolder(String value) throws FileNotFoundException, IOException {
           GenfitPropertiesReader.writeParameter("genfitfolder", value);
    }
    
    public static String getGenfitFolder() throws FileNotFoundException, IOException {
         return  GenfitPropertiesReader.readParameter("genfitfolder");
    }
    
    
    public static void setInputCode(String value) throws FileNotFoundException, IOException {
           GenfitPropertiesReader.writeParameter("inputcode", value);
    }
    
    public static String getInputCode() throws FileNotFoundException, IOException {
         return  GenfitPropertiesReader.readParameter("inputcode");
    }
    
    
    
    public static String getGNUPlot() throws FileNotFoundException, IOException {
         return  GenfitPropertiesReader.readParameter("gnuplot");
    }
     
    public static String getWindowsCompilerCommand() throws FileNotFoundException, IOException {
         return  GenfitPropertiesReader.readParameter("windows_compiler");
    }
    
    public static String getMacCompilerCommand() throws FileNotFoundException, IOException {
         return  GenfitPropertiesReader.readParameter("mac_compiler");
    }
    
    private static String readParameter(String key) throws FileNotFoundException, IOException{
            Properties prop = new Properties();
            prop.load(new FileInputStream("settings.properties"));
            return  prop.getProperty(key);
    }

    
    private static void writeParameter(String key, String value){
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("settings.properties"));
            prop.setProperty(key, value);                
            prop.store(new FileOutputStream("settings.properties"), null);
        }
        catch(FileNotFoundException exf){
             Logger.getLogger(GenfitPropertiesReader.class.getName()).log(Level.SEVERE, "Not file settings.properties found", exf);
        }
        catch (IOException ex) {
            Logger.getLogger(GenfitPropertiesReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String readAdvancedCommonsSettingsfile() throws FileNotFoundException, IOException {
        return  GenfitPropertiesReader.readParameter("advaced_common_settings_file");
    }

   
    
   
}
