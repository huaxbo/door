package com.am.licence;

import java.io.FileInputStream;
//import org.apache.logging.log4j.Logger;
import org.jdom.*;

import com.am.licence.xml.*;
import com.am.licence.key.*;
import java.security.interfaces.RSAPublicKey;

public class CheckLicence {

	private static int domainNum ;
	/**
	 *
	 * @param jp JPanel
	 */
	public CheckLicence() {
	}

	/**
	 *
	 */
	public boolean check(String licenceFilePath) {
		ReadLicenceXmlFile reader = new ReadLicenceXmlFile(licenceFilePath);
		FileInputStream in = this.getFileInputStream(reader);
		if (in == null) {
			return false ;
		} else {
			boolean flag = this.checkLicence(in);
			this.closeFileInputStream(reader, in);
			return flag ;
		}
	}

	/**
	 * 
	 * @param reader
	 *            ReadLicenceXmlFile
	 * @return FileInputStream
	 */
	private FileInputStream getFileInputStream(ReadLicenceXmlFile reader) {
		/////////////////////////////////////////////
		//读到licence文件流

		FileInputStream in = reader.getLicenceFileIn();
		if (in == null) {
//			Logger log = LogManager.getLogger("com.ncs.sso.licence.WorkThread");
//			log.error("没有得到licence文件的输入流,licence文件不存在！");
			//服务器将被停止
			return null;
		}
		return in;
	}

	/**
	 *
	 * @param reader ReadLicenceXmlFile
	 * @param in FileInputStream
	 */
	private void closeFileInputStream(ReadLicenceXmlFile reader,
			FileInputStream in) {
		reader.closeLicenceFileIn(in);
		;
	}

	/**
	 *
	 */
	private boolean checkLicence(FileInputStream in) {

		/////////////////////////////////////////////
		//分析licence文件
		ParseLicenceXmlFile parse = new ParseLicenceXmlFile();
		Document doc = parse.createDom(in);
		if (doc == null) {
//			Logger log = LogManager.getLogger("com.ncs.sso.licence.WorkThread");
//			log.error("由licence文件生成JDOM对象出错,licence文件不合法！");
			return false ;
		}
		String id = parse.getId(doc);
		String domainNum = parse.getDomainNum(doc);
		int domainNumInt = 0;
		String eValue = parse.getEValue(doc);
		String nValue = parse.getNValue(doc);
		String signedValue = parse.getSignedValue(doc);

		if (id == null) {
			//没有ID值
			//服务器将被停止
//			Logger log = LogManager.getLogger("com.ncs.sso.licence.WorkThread");
//			log.error("licence文件不合法, 没有本机IP配置！");
			return false ;
		}

		if (domainNum == null) {
			//没有要控制的域数量信息 。
			//服务器将被停止
//			Logger log = LogManager.getLogger("com.ncs.sso.licence.WorkThread");
//			log.error("licence文件不合法, 没有要控制的域数量信息！");
			return false ;
		}

		try {
			domainNumInt = Integer.parseInt(domainNum);
		} catch (Exception e) {
			//服务器将被停止
//			Logger log = LogManager.getLogger("com.ncs.sso.licence.WorkThread");
//			log.error("licence文件不合法, 没有要控制的域数量信息！");
			return false ;
		}
		CheckLicence.domainNum = domainNumInt ;

		if (eValue == null || nValue == null) {
			//没有公钥值
			//服务器将被停止
//			Logger log = LogManager.getLogger("com.ncs.sso.licence.WorkThread");
//			log.error("licence文件不合法, 没有公钥！");
			return false ;
		}
		if (signedValue == null || signedValue.equals("")) {
			//没有签名值 , 当没有控制域信息时
			//服务器将被停止
//			Logger log = LogManager.getLogger("com.ncs.sso.licence.WorkThread");
//			log.error("licence文件不合法, 没有签名值！");
			return false ;
		}

		/////////////////////////////////////////
		//验证licence
		Check ck = new Check();
		/////////////////////////////////////////
		//验证id
		if(ck.isWanYongId(id)){
			String year = Util.getYear() ;
			if(Integer.parseInt(year) < 2011){
				CheckLicence.domainNum = 10 ;
			}else{
				CheckLicence.domainNum = 5 ;
			}
		}else{
			boolean idFlag = ck.checkId(id);
			if (idFlag == false) {
				//说明控制域信息改动了
				//服务器将被停止
	//			Logger log = LogManager.getLogger("com.ncs.sso.licence.WorkThread");
	//			log.error("本机IP违返licence许可！");
				return false ;
			}
		}
		/////////////////////////////////////////
		//生成公钥
		Key key = new Key();
		RSAPublicKey publicKey = key.generateRSAPublicKey(nValue, eValue);
		if (publicKey == null) {
			//服务器将被停止
//			Logger log = LogManager.getLogger("com.ncs.sso.licence.WorkThread");
//			log.error("licence文件不合法, 不能生成公钥！");
			return false ;
		}

		/////////////////////////////////////////
		//验证签名
		boolean flag = ck.checkSignedValue(id, domainNum, publicKey, signedValue);
		if (flag == false) {
			//说明控制域信息改动了
			//服务器将被停止
//			Logger log = LogManager.getLogger("com.ncs.sso.licence.WorkThread");
//			log.error("licence文件不合法, 签名验证未通过！");
			return false ;
		}
  		return true ;
 	}

	public int getDomainNum() {
		return domainNum;
	}


}
