package com.automic.global.util.digests;

import java.security.MessageDigest;

import com.automic.global.util.ProtocolUtil;


/**
 * 摘要加密
 * @author huaxb
 *
 */
public class DigestManager {

    public static final String MD5 = "MD5";
    public static final String SHA_1 = "SHA-1";
    public static final String SHA_256 = "SHA-256";
    
    
    /**
     * 信息加密
     * @param src
     * @param algorithm ：MD5、SHA-1、SHA-256
     * @return
     */
    public static String digestMess(byte[] src,String algorithm,boolean space){
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(src);;
            
            return ProtocolUtil.byte2Hex(md.digest(),space);    
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    
    public static void main(String[] args){
        try {
            String src = "我";
            System.out.println(ProtocolUtil.byte2Hex(src.getBytes("UTF-8"), true));
            System.out.println(digestMess(src.getBytes("UTF-8"),MD5,true));
            System.out.println(digestMess(src.getBytes("UTF-8"),SHA_1,true));
            System.out.println(digestMess(src.getBytes("UTF-8"),SHA_256,true));
            
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }
}
