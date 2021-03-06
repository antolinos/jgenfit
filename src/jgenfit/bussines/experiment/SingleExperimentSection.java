/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit.bussines.experiment;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jgenfit.utils.GenfitLogger;
/**
 *
 * @author alex
 */
public class SingleExperimentSection {
    private static String EXPERIMENT_SEPARATOR = " For each experiment copy and paste starting from the next line";
    private static String SINGLE_EXPERIMENT_SEPARATOR = "                                       [---File Name ---------------------------------------------------------------------------------------|Par. Symb.|--Value--|Par. Symb.|--Value--|Par. Symb.|--Value--|Par. Symb.|--Value--|Par. Symb.|--Value--|Par. Symb.|--Value--|Par. Symb.|--Value--|Par. Symb.|--Value--|Par. Symb.|--Value--|Par. Symb.|--Value--|Par. Symb.|--Value--|Par. Symb.|--Value--|Par. Symb.|--Value--|Par. Symb.|--Value--|Par. Symb.|--Value--|";
    private String END_OF_SECTION = "[End of section]";
    private String content;
    int experimentsCount;
    
    List<SingleExperiment> experiments = new ArrayList<SingleExperiment>();
    private String header;
  
    
    
     private String readResourceExperimentTemplate(){
            StringBuilder sb = new StringBuilder();
            String resourcesPath = "/jgenfit/resources/templates/experiment.txt";
            try {
                BufferedReader objBin = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(resourcesPath)));
                if (objBin != null) {
                    String strLine = "";
                    while ((strLine = objBin.readLine()) != null) {
                        sb.append(strLine + "\n" );
                    }
                    objBin.close();
                } else {
                    GenfitLogger.error("Unable to retrieve InputStream");
                }
            } catch (Exception ex) {
                GenfitLogger.error("Unable to retrieve InputStream");            
            }            
            return sb.toString();        
    }
    
    public void addNewExperiment(){
        this.content = this.content.replace(this.END_OF_SECTION, this.readResourceExperimentTemplate());    
    }
    
    public SingleExperimentSection(String content){
        this.content = content;
        this.parse();
    }
    
    public List<SingleExperiment> getExperiments(){
        return this.experiments;
    }
    
    public String getHeader(){
        return this.header;
    }
    
    public int getSingleExperimentCount(){
        return this.experimentsCount;
    }
    
    private void parse(){
       GenfitLogger.debug("Parsing Single Experiment Section....");
        
       this.experimentsCount = 0;
       List<String> singleExperimentText = Arrays.asList(this.getContent().split(EXPERIMENT_SEPARATOR + "\n"));
       this.header = singleExperimentText.get(0) + EXPERIMENT_SEPARATOR +  "\n";
      // this.header = singleExperimentText.get(0);
      
           /** Getting experiments **/
           if (singleExperimentText.size() > 0){
               String singleExperimentSectionText = singleExperimentText.get(1);

               /** Go round lines **/
               List<String> lines = Arrays.asList(singleExperimentSectionText.split("\n"));
               StringBuilder singleSectionExperiment = new StringBuilder();
               for (String line : lines) {
                   if (line.contains(SINGLE_EXPERIMENT_SEPARATOR)){
                    if (singleSectionExperiment.length() != 0){                   
                      //  System.out.println("================ ADDING");
                        this.experiments.add(new SingleExperiment(singleSectionExperiment.toString()));  
                        singleSectionExperiment = new StringBuilder();
                    }
                   }
                   
                   if (line.contains(END_OF_SECTION)){
                        //System.out.println("=============== last");
                        singleSectionExperiment.append(line + "\n");  
                        this.experiments.add(new SingleExperiment(singleSectionExperiment.toString()));  
                        singleSectionExperiment = new StringBuilder();
                   }
                   singleSectionExperiment.append(line + "\n");             
               }              
               
               this.experimentsCount = this.experiments.size();
               /** Just for logging **/
               GenfitLogger.debug("Done: " + this.experiments.size()  + " experiments found");
              
                
               /* List<String> experimentsSingle = Arrays.asList(this.getContent().split(SINGLE_EXPERIMENT_SEPARATOR));
               for (String experiment : experimentsSingle) {
                    System.out.println("xxxxxxxxxxxxxxxxxxxxx");
                   System.out.println(experiment);
               }*/
               
          // }
           
       }
    }

    
    
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    public void removeExperimentByIndex(int experimentModelSelected) {
        StringBuilder sb = new StringBuilder();
        List<String> lines = Arrays.asList(this.content.split("\n"));
        
        int calculationCount = -1;
        for (String line : lines) {
            if (line.contains(SINGLE_EXPERIMENT_SEPARATOR)){
                calculationCount ++;
            }
            if (line.contains(END_OF_SECTION)){
                calculationCount ++;
                sb.append(line + "\n\n");
                
                this.content = sb.toString();
                return;
            }
            if (calculationCount != experimentModelSelected){
                sb.append(line + "\n");
            }
        }
        
        
    }
    
    
    
    
    
    

     
    
   
   
  
}
