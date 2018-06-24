package com.automic.global.util.hibernate;

import java.util.HashMap;

import org.hibernate.Session;

public abstract class BaseHibernateCall {
    
    protected HashMap<String,Object> param;
    
    /**
     * 
     */
    public BaseHibernateCall(){}
    
    /**
     * @param params
     */
    public BaseHibernateCall(HashMap<String,Object> params){
        this.param = params;
    }
    
    /**
     * @param se
     * @return
     */
    protected abstract Object execute(Session se);
}
