package com.automic.global.util.mvc;

public class MvcCfg {

    public static final String rlt_message = "message";//回执消息的key
    /**
     * controller suffix
     */
    public static final String action_suffix = ".act";
    
    /**
     * controller prefix
     */
    public String action_prefix;
    
    
    public String getAction_suffix(){
        
        return action_suffix;
    }
    
    public String getAction_prefix(){
        
        return action_prefix;
    }
}
