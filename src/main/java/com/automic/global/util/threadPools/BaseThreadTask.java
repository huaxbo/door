package com.automic.global.util.threadPools;


public abstract class BaseThreadTask implements Runnable {

    protected int idx = 0;
    /**
     * @param idx
     */
    public BaseThreadTask(int idx){
        
        this.idx = idx;
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        execute();        
    }

    public abstract void execute();
    
}
