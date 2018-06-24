package com.automic.global.util.signatures;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;

import com.automic.global.util.ProtocolUtil;


public class SignatureSign {
    
    private KeyPair kp;
    private Signature sg;
    
    /**
     * 
     */
    public SignatureSign(){
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024,new SecureRandom());
            kp = kpg.generateKeyPair();
            sg = Signature.getInstance("MD5WithRSA");//("SHA1WithRSA");
            
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * 签名
     * @param mess
     * @return
     */
    public String signatureMess(String mess){
        if(mess == null){
            
            return null;
        }
        try {
            sg.initSign(kp.getPrivate());
            sg.update(mess.getBytes());
            
            return ProtocolUtil.byte2Hex(sg.sign(),false);
            
        } catch (InvalidKeyException | SignatureException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * 获取publicKey modulus
     * @return
     */
    public String getPublicModulus(){
        
        RSAPublicKey pk = (RSAPublicKey)kp.getPublic();
        
        return pk.getModulus().toString(16);
    }
    
    /**
     * 获取publicKey exponent
     * @return
     */
    public String getPublicExponent(){
        
        RSAPublicKey pk = (RSAPublicKey)kp.getPublic();
        
        return pk.getPublicExponent().toString(16);
    }    
            
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        SignatureSign smg = new SignatureSign();
        String mess = "helloslllllefefe";        
        System.out.println("mess：" + mess);
        String sign = smg.signatureMess(mess);
        System.out.println("signature：" + sign);        
        String modulus = smg.getPublicModulus();
        String exponent = smg.getPublicExponent();            
        
        System.out.println("public modulus：" + modulus);
        System.out.println("public exponent：" + exponent);
    }
      
}
