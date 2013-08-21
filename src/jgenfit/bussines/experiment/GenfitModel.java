/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit.bussines.experiment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import jgenfit.utils.GenfitLogger;

/**
 *
 * @author alex
 */
  public class GenfitModel{
        private String content;
        
        private final String PARAMETERS_HEADER  = "|-----Link-Function";        
        private List<String> subModelsName = new ArrayList<String>();
        private List<Parameter> parameters = new ArrayList<Parameter>();        
        private List<String> modelFields = new ArrayList<String>();
        
        private String title = new String();
        
        public GenfitModel(String content){
            this.content = content;
            this.parse(content);
        }             
        
        public List<String> getSubmodelsText(){
            return this.subModelsName;
        }
                  
        public String getContent(){
            return this.content;
        }
        
        public Parameter getSubmodel(int index){
            return this.parameters.get(index);
        }
        
        public String toString(){
            return this.getContent();
        }
        
        
        public void changeParameterByIndex(int index, String value){            
            String toReplace = this.modelFields.get(index);            
            String newValue = toReplace.substring(0, 40) + value;
          
            this.content = this.content.replace(toReplace, newValue);
        
        }
        
/*
                                                                             |-----Link-Function (up to 300 characters ...
                                       [-Starting-|---Lower--|---Upper--|Flag|Kind|---Grid---|Lower Int-|Upper Int-|Lower Par1|Upper Par1|Lower Par1|Upper Par1|Lower Par2|Upper Par2|Lower Par3|Upper Par3|Lower Par4|Upper Par4|Lower Par5|Upper Par5|Lower Par6|Upper Par6|Lower Par7|Upper Par7|Lower Par8|Upper Par8|Lower Par9|Upper Par9|Lower Pa10|Upper Pa10|Link-Func. Par. 1-(up to 300 characters) ...
 Outer Radius (Angs.) .................: 500.          0.           600.  5    if (mm.eq.1) then; r1+d1 ;else if (mm.eq.2) then; r2+d2 ; endif        */
        public void parse(String content){
          //GenfitLogger.debug("content:" + content);
          List<String> lines = Arrays.asList(this.content.split("\n"));
          
          this.setTitle(lines.get(0));
          int parameter_header_found = -1;
          for (int i = 0; i < lines.size(); i++) {
              if (lines.get(i).contains(this.PARAMETERS_HEADER)){
                  parameter_header_found = i;
              }              
          }
          
          for (int i = 2; i < parameter_header_found ; i++) {
              this.getModelFields().add(lines.get(i));
          }

            for (int i = parameter_header_found + 2; i < lines.size(); i++) { 
                    //GenfitLogger.debug("Parameter:" + lines.get(i));
                    parameters.add(new Parameter(lines.get(i), this)); 
                    this.subModelsName.add(lines.get(i).substring(0, 40));
            } 
        }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the modelFields
     */
    public List<String> getModelFields() {
        return modelFields;
    }

    /**
     * @param modelFields the modelFields to set
     */
    public void setModelFields(List<String> modelFields) {
        this.modelFields = modelFields;
    }

    }