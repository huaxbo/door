package com.automic.global.util.signatures;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import com.automic.global.util.ProtocolUtil;


public class SignatureVerify {
    private Signature sg;
    
    /**
     * 
     */
    public SignatureVerify(){
        try {            
            sg = Signature.getInstance("SHA1WithRSA");            
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * @param mess
     * @param modulus
     * @param exponent
     * @param sign
     * @return
     */
    public boolean verifyMess(String mess,String modulus,String exponent,String sign){
        try {
        	PublicKey ksi = KeyFactory.getInstance("RSA").generatePublic(
        				new RSAPublicKeySpec(new BigInteger(modulus,16),
                    new BigInteger(exponent, 16)));
        	
            sg.initVerify(ksi);
            sg.update(mess.getBytes());
            
            return sg.verify(ProtocolUtil.hex2Bytes(sign));
            
        } catch (InvalidKeyException | SignatureException 
        		| InvalidKeySpecException | NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return false;
    }
            
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        SignatureVerify smg = new SignatureVerify();
        String mess = "helloslllllefefe";        
        String sign = "42e2a9b14c051a6e38d0979490bfbf527775dfea574d15fb18779476e7f8d4417dfeb5c600b8566fd7bf2ca86d28aa5d56317fa4a1ec731760915bf2081cbb4c02b577da11f5ae94299647d0fed5862f10d5af2516c86e027e1c306e989a7cd7a84ae1aae5efe75bbd1fc4e1120ab9c7f7051abd3660d32369b0e8141f89e111";
        String modulus = "d509ae664a2869a6a5968693ffccdad8037264b02fc8e633e43311b5ecd3df55bd39c599012d745d39207d673d930066b75bf08c9586c405843c8186a2481f3fc173ebec54507295dc503af5dd8a3b3ae0af4b5bdd5f2133616bafe20adc13e4f2700a27ea57784f3a1c71b34458ef4be2159fd04964be010f90c644d69710af";
        String exponent = "10001";            
        
        System.out.println("verify：" + smg.verifyMess(mess, modulus, exponent, sign));
    }

}
