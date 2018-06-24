package com.automic.global.util.digests;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Hashtable;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;

import com.automic.global.util.ProtocolUtil;


/**
 * 对称加密
 * @author huaxb
 *
 */
public class DigestSymmetry {
    //加密算法
    public static enum meths{DES,DESede,AES}
    private static String password = "12hel+_)((*&";
    private SecretKey skey;
    private Cipher cp;
    
    private static Hashtable<String,Integer> keysize = new Hashtable<String,Integer>(0);    
    static{
        keysize.put(meths.DES.toString(), 56);
        keysize.put(meths.DESede.toString(), 112);
        keysize.put(meths.AES.toString(), 128);
    }
    /**
     * @param meth 加密算法
     */
    public DigestSymmetry(String meth){
        try {            
            KeyGenerator kgener = KeyGenerator.getInstance(meth);
            kgener.init(keysize.get(meth), 
                    new SecureRandom(password.getBytes()));
            skey = kgener.generateKey();            
            cp = Cipher.getInstance(meth);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * 加密
     * @param src
     * @return
     */
    public byte[] encryptor(byte[] src){
        if(src == null){
            
            return null;
        }
        try {
            cp.init(Cipher.ENCRYPT_MODE, skey);
            
            return cp.doFinal(src);
            
        } catch (InvalidKeyException | IllegalBlockSizeException 
                | BadPaddingException  e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * 解密
     * @param dst
     * @return
     */
    public byte[] decryptor(byte[] dst){
        if(dst == null){
            
            return null;
        }
        try {
            cp.init(Cipher.DECRYPT_MODE, skey);
            
            return cp.doFinal(dst);
            
        } catch (InvalidKeyException | IllegalBlockSizeException 
                | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
    
     /**  
     * BASE64解密  
     *   
     * @param key  
     * @return  
     * @throws Exception  
     */  
    public static byte[] decryptBASE64(String key) throws Exception {
    	
    	return Base64.decodeBase64(key);
    }   

    /**  
     * BASE64加密  
     *   
     * @param key  
     * @return  
     * @throws Exception  
     */  
    public static String encryptBASE64(byte[] key) throws Exception {   
    	
    	return Base64.encodeBase64String(key);
    } 
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        String mess = "12345678";
        try{
            DigestSymmetry ds = new DigestSymmetry(meths.DES.toString());
            System.out.println(meths.DES);
            System.out.println("原文:" + ProtocolUtil.byte2Hex(mess.getBytes(), true));
            byte[] enbts = ds.encryptor(mess.getBytes());
            System.out.println("密文:" + ProtocolUtil.byte2Hex(enbts, true));
            System.out.println("解密:" + ProtocolUtil.byte2Hex(ds.decryptor(enbts), true));
            
            
            mess = "123456785";        
            ds = new DigestSymmetry(meths.AES.toString());
            System.out.println(meths.AES);
            System.out.println("原文:" + ProtocolUtil.byte2Hex(mess.getBytes(), true));
            enbts = ds.encryptor(mess.getBytes());
            System.out.println("密文:" + ProtocolUtil.byte2Hex(enbts, true));
            System.out.println("解密:" + ProtocolUtil.byte2Hex(ds.decryptor(enbts), true));
            
            mess = "123456";
            String bmt = encryptBASE64(mess.getBytes());
            System.out.println(bmt);
            System.out.println(new String(decryptBASE64(bmt)));
            
        }catch(Exception e){e.printStackTrace();}
        
    }

}
