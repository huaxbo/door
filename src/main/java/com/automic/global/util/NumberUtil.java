package com.automic.global.util;

import java.math.BigDecimal;

/**
 * @author Administrator
 * 数字处理工具类
 */
public class NumberUtil {

    /**
     * 16进制->单精度浮点数
     * @param bts
     * @return
     */
    public static float hex2float(byte[] bts){
                
        return Float.intBitsToFloat(hex2int(bts));
    }
    
    /**
     * 单精度浮点数->16进制
     * @param f
     * @return
     */
    public static byte[] float2hex(float f){
        
        return int2Hex(Float.floatToIntBits(f));
    }
    
    /**
     * byte[] -> int
     * @param bts
     * @return
     */
    public static int hex2int(byte[] bts){
        
        return Integer.parseInt(hex2str(bts), 16);
    }
    
    /**
     * int -> byte[]
     * @param fint
     * @return
     */
    public static byte[] int2Hex(int fint){
        String str = Integer.toHexString(fint);
        if(str.length()%2 > 0){
            str = "0" + str;
        }
        byte[] bts = new byte[str.length()/2];
        for(int i=0;i<str.length();i+=2){
            bts[i/2] = (byte)Integer.parseInt(str.substring(i,i+2),16);
        }
        
        return bts;
    }    
    
    /**
     * hex->str
     * @param bts
     * @return
     */
    public static String hex2str(byte[] bts){
        StringBuilder str = new StringBuilder();
        for(int i=0;i<bts.length;i++){
            if((bts[i]&0xFF) < 16){
                str.append("0");
            }
            str.append(Integer.toHexString(bts[i]&0xFF));
        }
        
        return str.toString();
    }
    
    /**
     * hex string -> byte[]
     * @param hex
     * @return
     */
    public static byte[] str2hex(String hex){
        if(hex.length()%2 > 0){
            hex = "0" + hex;
        }
        byte[] bts = new byte[hex.length()/2];
        int idx = 0;
        for(int i=0;i<bts.length;i++){
            bts[i] = (byte)Integer.parseInt(hex.substring(idx,idx+2), 16);
            idx+=2;
        }
        
        return bts;
    }
    /**
     * 反转义 fd ed->fd,fd ee->fe
     * @param bts
     * @return
     */
    public static byte[] unescape_sixin(byte[] bts){
        String str = hex2str(bts);
        str = str.replaceAll("fdee", "fe");
        str = str.replaceAll("fded", "fd");
        
        return str2hex(str);
    }
    
    /**
     * 转义 fd->fd ed,fe ->fd ee
     * @param bts
     * @return
     */
    public static byte[] escape_sixin(byte[] bts){
        String str = hex2str(bts);
        str = str.replaceAll("fd", "fded");
        str = str.replaceAll("fe", "fdee");
        
        return str2hex(str);
    }
    
    /**
     * 加法
     * @param bd1
     * @param bd2
     * @return
     */
    public static BigDecimal add(BigDecimal bd1,BigDecimal bd2){
    	if(bd1 == null){
    		bd1 = new BigDecimal(0);
    	}
    	if(bd2 == null){
    		bd2 = new BigDecimal(0);
    	}
    	
    	return bd1.add(bd2);
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub        
        byte[] bts = new byte[]{(byte)0x01,(byte)0x0C,0x19,(byte)0x9A};
        float f = hex2float(bts);
        System.out.println(f);
        
        byte[] bts2 = float2hex(f);
        System.out.println(hex2str(bts2));
        
        byte[] bts3 = str2hex("01f2");
        System.out.println(hex2str(bts3));
        
        byte[] bts4 = new byte[]{0x1,(byte)0xfd,(byte)0xee,(byte)0xfe,(byte)0xdd};
        byte[] bts4_1 = escape_sixin(bts4);
        System.out.println(hex2str(bts4_1));
        byte[] bts4_2 = unescape_sixin(bts4_1);
        System.out.println(hex2str(bts4_2));
    }

}
