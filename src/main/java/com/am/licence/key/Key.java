package com.am.licence.key;

import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.math.BigInteger;
import java.security.spec.InvalidKeySpecException;

public class Key {
	public Key() {
	}

	/**
	 * 
	 * @param str
	 *            String
	 * @return RSAPublicKey
	 */
	// public RSAPublicKey recoverRSAPublicKey(String str){
	// byte[] b = Util.hex2Byte(str) ;
	// try{
	// ByteArrayInputStream bin = new ByteArrayInputStream(b);
	// ObjectInputStream oin = new ObjectInputStream(bin);
	// Object o = oin.readObject() ;
	// MyPublicKey mykey = (MyPublicKey)o ;
	// RSAPublicKey rsa = mykey.getPublicKey() ;
	// return rsa ;
	// }catch(Exception e){
	// e.printStackTrace();
	// }
	// return null ;
	// }

	/**
	 * 生成公钥
	 * 
	 * @param modulus
	 * @param publicExponent
	 * @return RSAPublicKey
	 * @throws EncryptException
	 */
	public RSAPublicKey generateRSAPublicKey(byte[] modulus,
			byte[] publicExponent) throws Exception {
		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance("RSA",
					new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} catch (NoSuchAlgorithmException ex) {
			throw new Exception(ex.getMessage());
		}

		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(
				modulus), new BigInteger(publicExponent));
		try {
			return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
		} catch (InvalidKeySpecException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	/**
	 * 生成公钥
	 * 
	 * @param nHex
	 * @param publicExponent
	 * @return eHex
	 * @throws EncryptException
	 */
	public RSAPublicKey generateRSAPublicKey(String nHex, String eHex) {
		byte[] e = Util.hex2Byte(eHex);
		byte[] n = Util.hex2Byte(nHex);

		RSAPublicKey publicKey = null;
		try {
			publicKey = this.generateRSAPublicKey(n, e);
		} catch (Exception ee) {
			ee.printStackTrace();
			return null;
		}
		return publicKey;
	}

	/**
	 * 生成私钥
	 * 
	 * @param modulus
	 * @param privateExponent
	 * @return RSAPrivateKey
	 * @throws EncryptException
	 */
	// public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus,
	// byte[] privateExponent) throws Exception {
	// KeyFactory keyFac = null;
	// try {
	// keyFac = KeyFactory.getInstance("RSA",
	// new org.bouncycastle.jce.provider.BouncyCastleProvider());
	// }
	// catch (NoSuchAlgorithmException ex) {
	// throw new Exception(ex.getMessage());
	// }
	//
	// RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new
	// BigInteger(modulus),
	// new BigInteger(privateExponent));
	// try {
	// return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
	// }
	// catch (InvalidKeySpecException ex) {
	// throw new Exception(ex.getMessage());
	// }
	// }
	//
	//

	/**
	 * 
	 * @param hexSingedMessage
	 *            String
	 * @param message
	 *            String
	 * @param key
	 *            KeyPair
	 * @return boolean
	 */
	public boolean verify(String hexSingedMessage, String message,
			RSAPublicKey publicKey) {
		try {
			byte[] b = Util.hex2Byte(hexSingedMessage);
			Signature signcheck = Signature.getInstance("MD5WithRSA");
			signcheck.initVerify(publicKey);
			signcheck.update(message.getBytes());
			boolean flag = signcheck.verify(b);
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
