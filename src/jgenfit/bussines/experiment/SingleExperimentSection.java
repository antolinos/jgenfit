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
public class SingleExperimentSection {
    private static String SINGLE_EXPERIMENT_SEPARATOR = "For each experiment copy and paste starting from the next line";
    
    
    private String content;
    int experimentsCount;
    
    List<SingleExperiment> experiments = new ArrayList<SingleExperiment>();
    private String header;
    
    public SingleExperimentSection(String content){
        this.content = content;
        this.parse();
    }
    
    public List<SingleExperiment> getExperiments(){
        return this.experiments;
    }
    
    public int getSingleExperimentCount(){
        return this.experimentsCount;
    }
    
    private void parse(){
       this.experimentsCount = 0;
            
       List<String> singleExperimentText = Arrays.asList(this.getContent().split(SINGLE_EXPERIMENT_SEPARATOR));
       this.header = singleExperimentText.get(0);
       this.experimentsCount =  singleExperimentText.size() - 1;
       
       if (this.experimentsCount > 0){
           /** Remove header **/
           for (int i = 1; i < singleExperimentText.size(); i++) {
               this.experiments.add(new SingleExperiment(singleExperimentText.get(i)));  
           }  
       }
    }

    
    
    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }
    
    
    
    
    
    

     
    
   
   
  
}
