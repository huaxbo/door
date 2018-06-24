package com.automic.global.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author Administrator
 * 时间格式化工具类
 */
public class DateUtil {
    	
    /**
     * 获取当前时间
     * @param fmt。
     * 年：yyyy
     * 月：MM
     * 日：dd
     * 时：HH
     * 分：mm
     * 秒：ss
     * 毫秒：SSS
     * @return
     */
    public static String getCurrTime(String fmt){
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        
        return sdf.format(new Date(System.currentTimeMillis()));
    }
    
    /**
     * 获取当前推迟后的时间
     * @param delay_tp:常量,Calendar中取值
     * @param delay：>0表示当前时间之后、<0表示当前时间之前
     * @param fmt
     * 年：yyyy
     * 月：MM
     * 日：dd
     * 时：HH
     * 分：mm
     * 秒：ss
     * 毫秒：SSS
     * @return
     */
    public static String getDelay(int delay_tp,int delay,String fmt){
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date(System.currentTimeMillis()));
    	cal.add(delay_tp, delay);
    	
    	return sdf.format(cal.getTime());
    }
    
    /**
     * string -> date
     * @param date
     * @return
     */
    public static Date str2date(String date){
    	Calendar cal = Calendar.getInstance();
    	if(date == null){
    		
    		return null;
    	}
    	String[] arr0 = date.split("-");
    	cal.set(Calendar.YEAR, Integer.parseInt(arr0[0]));
    	cal.set(Calendar.MONTH, Integer.parseInt(arr0[1]) - 1);
    	cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arr0[2].substring(0,2)));
    	if(arr0[2].length() > 3){
    		String str = arr0[2].substring(3);
    		String[] arr1 = str.split(":");
    		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(arr1[0]));
    		if(arr1.length > 1){
        		cal.set(Calendar.MINUTE, Integer.parseInt(arr1[1]));
    		}
    		if(arr1.length > 2){
        		cal.set(Calendar.SECOND, Integer.parseInt(arr1[2]));
    		}
    	}    	
    	
    	return cal.getTime();
    }
    
    /**
     * date -> string
     * @param date
     * @param fmt
     * @return
     */
    public static String date2str(Date date,String fmt){
    	SimpleDateFormat sdf = new SimpleDateFormat(fmt);
    	
    	return sdf.format(date);
    }
    
    /**
     * 日期所在月度周数
     * 从1开始
     * @param ymd
     * @return
     */
    public static Integer getWeekNum(String ymd){
    	if(ymd == null || ymd.equals("")){
    		
    		return null;
    	}
    	String[] arr = ymd.split("-");
    	int day = Integer.parseInt(arr[2]);
    	//获取月度周数
    	int d = 1;
    	Calendar cal = Calendar.getInstance();
    	cal.set(Integer.parseInt(arr[0]), 
    			Integer.parseInt(arr[1]) - 1, 1);
    	int dw = cal.get(Calendar.DAY_OF_WEEK) - 1; 
    	if(dw > Calendar.SUNDAY){
    		d = 7 - dw + 1;
    	}
    	for(int i = 1;i <= 6;i++){
    		if(day <= d + 7 * (i - 1)){
    			
    			return i;
    		}
    	}
    	
    	return null;
    }
    
    public static void main(String[] args){
    	/*System.out.println(getDelay(Calendar.HOUR_OF_DAY,1,"yyyy-MM-dd HH:mm:ss"));
    	System.out.println(getDelay(Calendar.HOUR_OF_DAY,-1,"yyyy-MM-dd HH:mm:ss"));
    	System.out.println(getDelay(Calendar.HOUR_OF_DAY,15,"yyyy-MM-dd HH:mm:ss"));
    	System.out.println(getDelay(Calendar.HOUR_OF_DAY,-10,"yyyy-MM-dd HH:00:00"));
    	
    	System.out.println(getDelay(Calendar.DAY_OF_MONTH,1,"yyyy-MM-dd"));
    	System.out.println(getDelay(Calendar.DAY_OF_MONTH,5,"yyyy-MM-dd"));
    	System.out.println(getDelay(Calendar.DAY_OF_MONTH,-28,"yyyy-MM-dd"));*/
    	
    	/*System.out.println(date2str(str2date("2017-07-25"),"yyyy-MM-dd"));
    	System.out.println(date2str(str2date("2017-07-25 01"),"yyyy-MM-dd HH"));
    	System.out.println(date2str(str2date("2017-07-25 01:12"),"yyyy-MM-dd HH:mm"));
    	System.out.println(date2str(str2date("2017-07-25 01:12:34"),"yyyy-MM-dd HH:mm:ss"));*/
    	
    	System.out.println(getWeekNum("2017-06-30"));
    }
}
