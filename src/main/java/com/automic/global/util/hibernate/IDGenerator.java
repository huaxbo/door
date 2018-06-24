package com.automic.global.util.hibernate;


import java.io.Serializable;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentityGenerator;
import org.hibernate.HibernateException ;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IDGenerator extends IdentityGenerator {

    private static String curr ;
    private static int add = 0 ;
    private static String dtStr = "yyMMddHHmmssSSS" ;
    static {
        curr = new SimpleDateFormat(dtStr).format(new Date(System
                .currentTimeMillis()));
        new ResetTimerThread();
    }
   
    /**
     * 
     */
    public Serializable generate(SessionImplementor parm1,
            Object parm2) throws HibernateException {
        return curr + getAdd();    
    }
    /**
     * 
     */
    private synchronized String getAdd(){
        add ++ ;
        if(add < 10){
            return "0" + add  ;
        }else{
        	if(add < 99){
                return "" + add ;
            }else{
                String s = "99" ;
                try{
                    Thread.sleep(1) ;
                }catch(Exception e){
                    ;
                }finally{
                    add = 0 ;
                }
                return s ;
            }
        }
    }
    private static void update() {
        curr = new SimpleDateFormat(dtStr).format(new Date(System
                .currentTimeMillis()));
    }

    /**
     * 
     */
    private static class ResetTimerThread extends Thread {
        public ResetTimerThread() {
            super();
            this.setDaemon(true);
            start();
        }

        public void run() {
            while (true) {
                update();
                try {
                    Thread.sleep(1000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public static void main(String args[]){
        IDGenerator o = new IDGenerator() ;
        for(int i = 0 ; i < 1000 ; i++){        	
            System.out.println((String)(o.generate(null , null))) ;
            try{
              Thread.sleep(200) ;
            }catch(Exception e){
                e.printStackTrace() ;
            }
        }
    }
    
}
