/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author DEMARIAA
 */
public class FileUtils {
    public static String readFile(File file){
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

            
            // dispose all the resources after using them.
            fis.close();
            bis.close();
            dis.close();
            return br.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String();
    }
    
    public static void writeFile(File file, String text) throws IOException{
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file.getAbsolutePath());
            fos.write(text.getBytes("UTF-8"));
        } catch (IOException e) {
            fos.close();
            throw e;
        }
    }
}
