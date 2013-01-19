/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit.bussines;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import jgenfit.bussines.experiment.GeneralSection;
import jgenfit.bussines.experiment.GenfitModel;
import jgenfit.bussines.experiment.ModelList;
import jgenfit.bussines.experiment.SingleExperimentSection;
import jgenfit.bussines.experiment.ModelSingleExperiment;
import jgenfit.bussines.experiment.Parameter;
import jgenfit.bussines.experiment.SingleExperiment;
import jgenfit.utils.GenfitLogger;

/**
 *
 * @author alex
 */
public class GenfitController {
    private GenfitFile genfitFile;

    private String headerSection;
    private String modelList;
    private String experimentalSection;
    private Integer maxpdb;
    
    /**
     * @return the genfitFile
     */
    public GenfitFile getGenfitFile() {
        return genfitFile;
    }

    /**
     * @param genfitFile the genfitFile to set
     */
    public void setGenfitFile(GenfitFile genfitFile) {
        this.genfitFile = genfitFile;    
        this.headerSection = this.genfitFile.getHeaderSection();
        this.modelList = this.genfitFile.getModelListText();
        this.experimentalSection = this.genfitFile.getExperimentalSectionText();
        
        this.maxpdb = AdvancedCommonParametersFile.get_maxpdb();
        if (this.maxpdb != null){
            this.setMaxPDB(maxpdb);
        }

    }
    
    public void setMaxPDB(int maxpdb){
        this.maxpdb = maxpdb;
        ModelList models = new ModelList(this.modelList);
        if (maxpdb > 0){
           GenfitLogger.debug("maxPDB value: " + maxpdb); 
           for (int i = 1; i < models.getModelNames().size(); i++){ 
                GenfitModel model =  models.getModel(i, maxpdb);
                this.save(i, model);
           }
        }
        //GenfitLogger.debug(this.modelList);
        //this.modelList = this.getModel
    }
    
    
    public SingleExperimentSection getSingleExperimentSection() {   
        return new SingleExperimentSection(this.experimentalSection);
    }
    
    public ModelList getModelList(){
        return new ModelList(this.modelList);      
    }
    
     public GenfitModel getModel(int index){
       return this.getModelList().getModel(index, this.maxpdb);    
    }
     
    public GeneralSection getGeneralSection(){
        return new GeneralSection(this.headerSection);
    }
    
    public void removeExperimentModel(int singleExperiment, int model){
        SingleExperimentSection experiment = this.getSingleExperimentSection();
        SingleExperiment experimentModel = experiment.getExperiments().get(singleExperiment);
        String aux = experimentModel.getContent();
        experimentModel.removeModel(model);
        this.experimentalSection = this.experimentalSection.replace(aux, experimentModel.getContent());
    }
    
     public void addExperimentModel(int singleExperiment){
       SingleExperimentSection experiment = this.getSingleExperimentSection();
       SingleExperiment experimentModel = experiment.getExperiments().get(singleExperiment);
      // System.out.println(experimentModel.getContent());
       
    }
    
    public void save(int singleExperiment, int modelSingleExperimentIndex, ModelSingleExperiment model){
        SingleExperimentSection experiment = this.getSingleExperimentSection();
        SingleExperiment experimentModel = experiment.getExperiments().get(singleExperiment);
        String aux = experimentModel.getContent();
         
        experimentModel.saveModelSingleExperiment(modelSingleExperimentIndex, model);
        this.experimentalSection = this.experimentalSection.replace(aux, experimentModel.getContent());
    }
    
    public void save(int modelIndex, GenfitModel model){
        GenfitModel modelOld = this.getModelList().getModel(modelIndex);
        this.modelList = this.modelList.replace(modelOld.getContent(), model.getContent());
    }
    
    public void save(Parameter parameter, int modelIndex, int submodelIndex){
        GenfitModel model = this.getModelList().getModel(modelIndex, this.maxpdb);
        String modelContent = model.getContent().replace(model.getSubmodel(submodelIndex).toString(), parameter.toString());
        this.modelList = this.modelList.replace(model.toString(), modelContent);
    }
    
    public void save(GeneralSection generalSection){       
        this.headerSection = generalSection.getContent();        
    }
    
    public void save(SingleExperiment singleExperimentUpdated, int index){            
        //this.experimentalSection = this.getSingleExperimentSection().getContent().replace(this.getSingleExperimentSection().getExperiments().get(0).getContent(), singleExperiment.getContent());        
        //this.experimentalSection = this.getSingleExperimentSection().getContent().replace(oldExperiment, newExperiment);                               
        StringBuilder newExperimentalSection = new StringBuilder();
        newExperimentalSection.append(this.getSingleExperimentSection().getHeader());
        
        int aux = 0;
        for (SingleExperiment singleExperiment : this.getSingleExperimentSection().getExperiments()) {
            //System.out.append(singleExperiment1.getContent());
            if (aux == index){
                 newExperimentalSection.append(singleExperimentUpdated.getContent());
           }
            else{
                newExperimentalSection.append(singleExperiment.getContent());
            }
            aux ++;
        }
        this.experimentalSection = newExperimentalSection.toString();             
    }
    
    public void save(String filePath) throws Exception{
        File file = new File(filePath);
        StringBuilder br = new StringBuilder(); 
       
        SimpleDateFormat DATEFORMAT = new SimpleDateFormat("dd/M/yyyy hh:mm:ss");
        Calendar NOW = GregorianCalendar.getInstance();
                
        br.append("** File generated by JGenfit " + DATEFORMAT.format(NOW.getTime()) + "**\n\n");
        br.append(this.headerSection);
        br.append(this.experimentalSection);
        br.append(this.modelList);
        
        /** trim all the lines **/
        String content = br.toString();
        StringBuilder strimmedContent = new StringBuilder();
        for (String line : content.split("\n")) {
            strimmedContent.append(line.replaceAll("\\s+$", ""));
            strimmedContent.append("\n");
        }
        try{
            FileWriter fw = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fw);
            //out.write(br.toString());
            out.write(strimmedContent.toString());           
            out.close();
        }
        catch(Exception exp){
            System.err.println(exp.getMessage());
        }       
    }

    public void addNewCalculation() {
        SingleExperimentSection section  = this.getSingleExperimentSection();
        section.addNewExperiment();
        this.experimentalSection =  section.getContent();
    }

    public void removeCalculation(int experimentModelSelected) {                
        SingleExperimentSection single = this.getSingleExperimentSection();
        //System.out.println(this.experimentalSection);
        single.removeExperimentByIndex(experimentModelSelected);
        this.experimentalSection = single.getContent();
        
    }

    
}
