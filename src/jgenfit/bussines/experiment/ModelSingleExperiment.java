/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit.bussines.experiment;

import java.util.Arrays;

/**
 *
 * @author alex
 */
  public class ModelSingleExperiment{
        private String numberModelLine;
        private String header;
        private String weightLine;

        private int starting_start = 40;
        private int starting_end = 50;
        
        private int lower_start = 51;
        private int lower_end = 61;
        
        private int upper_start = 62;
        private int upper_end = 72;
        
        private int flag_start = 73;
        private int flag_end = 77;
        
        private int link_start = 78;
        //private int link_end = 378;
        private int link_end = 12000 + 78;
        
        public ModelSingleExperiment(String numberModelLine, String header, String weightLine) {
            this.numberModelLine = numberModelLine;
            this.header = header;
            this.weightLine = weightLine;  
            
            if (this.weightLine.length() < 400){
                for (int i = this.weightLine.length() - 1; i < 400; i++) {
                    this.weightLine = this.weightLine.concat(" ");
                }
            }
        }
        
        public String getModelNumber(){            
            return Arrays.asList(this.numberModelLine.split(":")).get(1);
        }
        
        
        public String getContent(){
            StringBuilder br = new StringBuilder();
            br.append(this.numberModelLine).append("\n");
            br.append(this.header).append("\n");
            br.append(this.weightLine).append("\n");
            return " " + br.toString().trim();
        
        }
        
        /** COLUMN POSITION WEIGHT PARAMETERS
         * 
         *  Starting : 41 - 51
         *  Lower    : 52 - 62
         *  Upper    : 63 - 73
         *  Flag     : 74 - 78
         *  Link     : 79 - 379
         * */
        public String getStarting(){            
            return this.getWeightParam(this.starting_start, this.starting_end).trim();                
        }
        
        public String getLower(){            
            return this.getWeightParam(this.lower_start, this.lower_end).trim();                
        }
                
        public String getUpper(){            
            return this.getWeightParam(this.upper_start, this.upper_end).trim();                
        }
        
        public String getFlag(){            
            return this.getWeightParam(this.flag_start, this.flag_end).trim();                
        }
        
        public String getLink(){            
            return this.getWeightParam(this.link_start, this.link_end).trim();           
        }
                
        private String getWeightParam(int start, int end){
            if (this.weightLine.length() > start){
                if(this.weightLine.length() < end){
                    return this.weightLine.substring(start, this.weightLine.length());
                }
                else{
                    return this.weightLine.substring(start, end);
                }
            }
            else{
                return new String();
            } 
        }
        
        public void setStarting(String value){           
            this.setParameters(starting_start, starting_end, value);
           
        }
        
        public void setLower(String value){
            this.setParameters(lower_start, lower_end, value);
            
        }
        
        public void setUpper(String value){
            this.setParameters(upper_start, upper_end, value);
            
        }
        
        public void setFlag(String value){
            this.setParameters(flag_start, flag_end, value);
           
        }
        
        public void setLink(String value){
            this.setParameters(link_start, link_end, value);
           
        }
        
     
        private void setParameters(int start, int end, String value){
          
            int length = end - start;
            if(value.length()< end){
                for(int i = value.length(); i < end; i++){
                    value = value.concat(" ");
                }                
            }
            
             if(this.weightLine.length()< end){
                for(int i = this.weightLine.length(); i < end; i++){
                    this.weightLine = this.weightLine.concat(" ");
                }                
            }
         System.out.println(start + " " + end + "  " +  + value.length() + " " + this.weightLine.length() );
         
            int count = 0;
            for (int i = start; i < end; i++) {
                this.weightLine = replaceCharAt(this.weightLine, i, value.charAt(count));
                count ++;
            }
        
        }

         private String replaceCharAt(String s, int pos, char c) {
            return s.substring(0, pos) + c + s.substring(pos + 1);
         }
        
    
    }
    