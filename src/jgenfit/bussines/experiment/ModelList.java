/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit.bussines.experiment;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author alex
 */
public class ModelList {

    private ArrayList<String> modelNames = new ArrayList<String>();
    private HashMap<Integer, Integer> linesNumberOfModels;
    private final String content;
    
    public ModelList(String content){
        this.content = content;
        this.parseContent(content);
    }
    
    private void parseContent(String content){
        List<String> lines = Arrays.asList(content.split("\n"));        
        this.linesNumberOfModels = new HashMap<Integer, Integer>();
        int modelsCount = 0;
        
        int indexLinePreviousModelFound = 0;
        for (int i = 5; i < lines.size(); i++) {
       
            /** first model **/
            if ((lines.get(i - 1 ).trim().contains("9-polydisperse"))){
                   //System.out.println("----- " + lines.get(i));
                   indexLinePreviousModelFound = i;
                   getModelNames().add(lines.get(i));
                   modelsCount++;
                   
                   linesNumberOfModels.put(modelsCount, i); 
                   continue;
            }
             
            /** model with parameter or PDB **/
            if ((lines.get(i - 2 ).trim().isEmpty())&&(lines.get(i).trim().isEmpty())&&!(lines.get(i - 1).trim().isEmpty())){
                   //System.out.println("-----x " + lines.get(i - 1));
                   indexLinePreviousModelFound = i - 1;
                   getModelNames().add(lines.get(i - 1));
                   modelsCount++;
                   linesNumberOfModels.put(modelsCount, i - 1); 
                   continue;
            }
             
            /** default **/
            if ((!lines.get(i - 1 ).contains("..."))&&(lines.get(i - 2 ).trim().isEmpty())&&(!lines.get(i).trim().isEmpty())&&(i - indexLinePreviousModelFound > 3) &&!(lines.get(i - 1).trim().isEmpty())){
                   //System.out.println("----- " + lines.get(i - 1));
                   indexLinePreviousModelFound = i - 1;
                   getModelNames().add(lines.get(i - 1));
                   modelsCount++;
                   linesNumberOfModels.put(modelsCount, i - 1); 
                   continue;
            }
        }
    }
    
    
     /** modelIndex starting from 1 **/
    public String getModelText(int modelIndex){
        List<String> lines = Arrays.asList(this.content.split("\n"));
        
        StringBuilder modelText = new StringBuilder();
        for (int i = this.linesNumberOfModels.get(modelIndex); i < this.linesNumberOfModels.get(modelIndex + 1) - 1; i++) {
            modelText.append(lines.get(i) + "\n");
        }
        return modelText.toString();
    }
    
    public GenfitModel getModel(int modelIndex){
        return new GenfitModel(this.getModelText(modelIndex));
    }
    
    
    /** modelIndex starting from 1 **/
    public String getModelName(int modelIndex){
        return this.getModelNames().get(modelIndex - 1);
    }
    
    public List<String> getModelName(List<Integer> modelIndex){
         List<String> modelsNames = new ArrayList<String>();
         for (int index : modelIndex) {
             modelsNames.add(this.getModelName(index));
         }
        return modelsNames;
    }

    /**
     * @return the modelNames
     */
    public ArrayList<String> getModelNames() {
        return modelNames;
    }
    
    
    
  
    
   
}
