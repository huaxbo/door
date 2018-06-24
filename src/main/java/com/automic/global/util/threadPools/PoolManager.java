package com.automic.global.util.threadPools;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PoolManager {

    private static int pool_min = 1;
    private static int pool_max = 5;
    
    /**
     * 
     */
    private PoolManager(){}
    
    private ThreadPoolExecutor pool = new ThreadPoolExecutor(pool_min, pool_max, 
            500L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(pool_min));
    
    /**
     * @param task
     */
    public synchronized void execute(Runnable task) throws Exception{        
        try{
            if(pool.getPoolSize() < pool_max){
                pool.execute(task);                    
            }else{
                Thread.sleep(1500);                
            }
        }catch(Exception e){
            throw e;
        }finally{}
        
    }
    
    /**
     * @param args
     */
    public static void main(String[] args){
        PoolManager pm = new PoolManager();
       final AtomicInteger ai = new AtomicInteger();
        for(int i=0;i<100;i++){
            try{
                pm.execute(new BaseThreadTask(i+1) {
                    public void execute() {
                        // TODO Auto-generated method stub
                        System.out.println(ai.incrementAndGet());
                        System.out.println("ThreadTask-" + idx + "::" + System.currentTimeMillis());
                        
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }finally{}
                    }
                });
            }catch(Exception e){
                i--;
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }        
    }
    
}
