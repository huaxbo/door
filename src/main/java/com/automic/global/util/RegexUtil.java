package com.automic.global.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式校验
 * @author huaxb
 *
 */
public class RegexUtil {

    /**
     * @param str
     * @param regex
     * @return
     */
    public static boolean regexes(String str,String regex){
        if(str == null){
            
            return false;
        }
        Pattern p = Pattern.compile(regex,Pattern.DOTALL);
        Matcher m = p.matcher(str);
        
        return m.matches();        
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        try {
            System.out.println(071);//8进制
            System.out.println(regexes(new String(new byte[]{(byte)0xFF,(byte)0xAA,(byte)056},"iso-8859-1"),"\\xFF.\\056"));
            System.out.println(regexes("-2s","-?\\d+"));
            
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
