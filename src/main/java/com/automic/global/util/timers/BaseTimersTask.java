package com.automic.global.util.timers;

import java.util.TimerTask;

public abstract class BaseTimersTask extends TimerTask {

    public BaseTimersTask(){}
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        execute();
    }
    
    public abstract void execute();
}
