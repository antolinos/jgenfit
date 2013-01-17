/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit.bussines.experiment;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import jgenfit.utils.GenfitLogger;

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
        
        //GenfitLogger.debug("Number of models detected " + linesNumberOfModels.size());
        
    }
    
     private String removePDBLines(StringBuilder result, int linesToRemove, int totalPDB) {
        //GenfitLogger.debug("Remove:" + linesToRemove);
        List<String> lines = Arrays.asList(result.toString().split("\n"));
        result = new StringBuilder();
        int pdblinesfound = 0;
         for (String line : lines) {
             if (line.contains(" PDB File ")){
                 pdblinesfound = pdblinesfound + 1;
                 if (pdblinesfound <= (totalPDB - linesToRemove)){
                     result.append(line);
                     result.append("\n");
                 }
             }
             else{
                result.append(line);
                result.append("\n");
             }
         }    
         return result.toString();
    }
    
    private String addPDBLines(int linesToAdd, int maxpdb){
        //GenfitLogger.debug("Add:" + linesToAdd);
        String result = "";
         for (int i = maxpdb - linesToAdd + 1; i <= maxpdb; i++) {
             
              result = result + (getNewPDBLine(i));
         }
         return result;
    }
    
     private String getNewPDBLine( int i){
         if (i < 10){
            return " PDB File (without .pdb) #" + i + "............:\n";
         }
          if ((i > 9)&&(i < 100)){
            return " PDB File (without .pdb) #" + i + "...........:\n";
          }
          return " PDB File (without .pdb) #" + i + "..........:\n";
     }
     
    private String adjustByMaxPDBParameter( List<String> lines, int maxpdb){
        /**Adjusting maxpdb **/
         StringBuilder result = new StringBuilder();
         
         int pdblinesfound = 0;
         for (String line : lines) {             
             if (line.contains(" PDB File ")){
                 pdblinesfound = pdblinesfound + 1;
             }
              if (!line.contains(" PDB File ") && pdblinesfound > 0){
                   //GenfitLogger.debug(" PDB found " + pdblinesfound);
                         if (pdblinesfound > 0){
                             if (pdblinesfound > maxpdb){
                                  //GenfitLogger.debug("Remove last:" + (pdblinesfound - maxpdb));                                
                                  String pdbCut =(this.removePDBLines(result, (pdblinesfound - maxpdb), pdblinesfound));
                                  result = new StringBuilder();
                                  result.append(pdbCut);
                             }
                             else{
                                 int toAdd = maxpdb - pdblinesfound;
                                 //GenfitLogger.debug("PDB lines to add:" + toAdd);
                                 result.append(this.addPDBLines(toAdd, maxpdb));
                               /*  for (int i = 22 - toAdd + 1; i <= 22; i++) {
                                      System.out.println("PDB File (without .pdb) #" + i + "...........:");
                                 }*/
                             }
                         }
                         /** We init again the counter to know that the pdb lines has been updated **/
                         pdblinesfound = 0;
              }
              
             result.append(line);     
             result.append("\n");
         }
        
         //GenfitLogger.debug(result.toString());
         return result.toString();
    }
    
     /** modelIndex starting from 1
     * maxpdb will be adjusted
     **/
    public String getModelText(int modelIndex, int maxpdb){
        //List<String> lines = Arrays.asList(this.content.split("\n"));
        
        
//        StringBuilder modelText = new StringBuilder();
//        
//         System.out.println("--++ " + modelIndex + " -- " + linesNumberOfModels.size());
//         
//         if (modelIndex != this.modelNames.size()){
//             /** not Last model **/
//                for (int i = this.linesNumberOfModels.get(modelIndex); i < this.linesNumberOfModels.get(modelIndex + 1) - 1; i++) {
//                    modelText.append(lines.get(i));
//                    modelText.append("\n");
//
//                }
//         }
//         else{
//             /** Last model **/
//              for (int i = this.linesNumberOfModels.get(modelIndex); i < Arrays.asList(this.content.split("\n")).size() - 1; i++) {
//                    modelText.append(lines.get(i));
//                    modelText.append("\n");
//
//                }
//         
//         }
        String contentModel = this.getModelText(modelIndex);
        return this.adjustByMaxPDBParameter( Arrays.asList(contentModel.split("\n")), maxpdb);
    }
    
      /** modelIndex starting from 1 **/
    public String getModelText(int modelIndex){
        List<String> lines = Arrays.asList(this.content.split("\n"));
     
            StringBuilder modelText = new StringBuilder();
         if (modelIndex != this.modelNames.size()){
             /** not Last model **/
                for (int i = this.linesNumberOfModels.get(modelIndex); i < this.linesNumberOfModels.get(modelIndex + 1) - 1; i++) {
                    modelText.append(lines.get(i));
                    modelText.append("\n");

                }
         }
         else{
             /** Last model **/
              for (int i = this.linesNumberOfModels.get(modelIndex); i < Arrays.asList(this.content.split("\n")).size() - 1; i++) {
                    modelText.append(lines.get(i));
                    modelText.append("\n");

                }
         
         }
            return modelText.toString();
      
    }
    
    public GenfitModel getModel(int modelIndex, int maxpdb){
        return new GenfitModel(this.getModelText(modelIndex, maxpdb));
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
