/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit.bussines.experiment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author alex
 */
public class GeneralSection {
    
    String HEADER_SEPARATOR = "GENERAL SECTION";
    String FINAL_HEADER_SEPARATOR = "SINGLE EXPERIMENT SECTION";
    
    private String original_content;
    private String content;
    
    public GeneralSection(String content){
        this.original_content = content;
        this.content = content;
    }

    
    private String getLineByKey(String key){
        List<String> lines = Arrays.asList(this.getContent().split("\n"));
        for (String line : lines) {
            if (line.toLowerCase().replace(" ", "").contains(key.toLowerCase().replace(" ", ""))){
                return line;
            }
        }
        return null;
    }
    
    public String getValue(String key){                
        String line = this.getLineByKey(key);
         
        if (line != null){
            if (line.split(":").length > 1){
                return line.split(":")[1];
            }
            else{
                return new String();
            }
        }
         
        return "Not found";
    }

    public void setValue(String key, String text) {
        String line = this.getLineByKey(key);
       
        
        if (line != null){
            List<String> splitted = Arrays.asList(line.split(":"));
            this.setContent(this.getContent().replace(line, splitted.get(0) + ":" + text));
        }
        
        
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the original_content
     */
    public String getOriginal_content() {
        return original_content;
    }

    /**
     * @param original_content the original_content to set
     */
    public void setOriginal_content(String original_content) {
        this.original_content = original_content;
    }
}
