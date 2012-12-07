/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jgenfit.events;

import java.util.EventObject;

/**
 *
 * @author alex
 */
public class GenfitEvent extends EventObject{
    private GenfitEventListener listener;
    private GenfitEventType type;
    public String filePath;
    
    public GenfitEvent(Object source, GenfitEventType type){
        super(source);
        this.type = type;
    }
    
    
    public void addListener(GenfitEventListener listener){
        this.listener = listener;
    }
    
    public void fire(){
        this.listener.handleGenfitEvent(this);
    }
    
    public GenfitEventType getType(){
        return this.type;
    }
}

