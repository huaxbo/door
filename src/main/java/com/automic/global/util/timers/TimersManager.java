package com.automic.global.util.timers;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class TimersManager{

    /**
     * @param timerNm：定时器名称
     * @param startMinu：任务启动的分钟
     * @param intv,任务轮训间隔，单位：s
     * @param tk：执行任务
     */
    public static void startTimer(String timerNm,int startMinu,long intv,TimerTask tk){        
        Timer t = new Timer(timerNm,false);
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                startMinu,0);
        t.schedule(tk, cal.getTime(), intv * 1000);
    }
    
    
    /**
     * @param args
     */
    public static void main(String[] args){        
        TimersManager.startTimer("timer1",11,60,new BaseTimersTask() {
            
            @Override
            public void execute() {
                // TODO Auto-generated method stub
                System.out.println("handler execute!");
            }
        });
    }
    
}
