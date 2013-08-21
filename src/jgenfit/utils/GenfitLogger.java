/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit.utils;

/**
 *
 * @author DEMARIAA
 */
public class GenfitLogger {
    public static void info(String message){
        System.out.println("[INFO] " + message);
    }
    
     public static void warn(String message){
        System.out.println("[WARN] " + message);
    }
     
    public static void debug(String message){
        //System.out.println("[DEBUG] " + message);
    }
      
    public static void error(String message){
        System.out.println("[ERROR] " + message);
    }
}
