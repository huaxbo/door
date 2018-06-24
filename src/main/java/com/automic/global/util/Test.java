package com.automic.global.util;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args){
        ColorEnum b = ColorEnum.BLUE;
        
         System.out.println(b);
         System.out.println(b.getValue());
         System.out.println(b.compareTo(ColorEnum.YELLOW));
         
         String str = "a,b,2,,";
         String[] strs = str.split(",");
         System.out.println(strs.length);
         
         ArrayList<String> lt = new ArrayList<String>(0);
         lt.add("1");
         lt.add("2");
         lt.add("3");
         lt.add("4");
         
         List<String> sub = lt.subList(2, 4);
         System.out.println(sub.get(1));
         sub.add("5");
         
         System.out.println(lt.get(4));
         
         lt.remove(4);
         sub = lt.subList(2, 4);
         System.out.println(sub.get(1));
         
         for(String ve : lt){
             if("2".equals(ve)){
                 lt.remove(ve);
             }
             System.out.println(ve);
         }
         
     }
}
