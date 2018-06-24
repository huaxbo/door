package com.am.licence.xml;


import java.io.FileInputStream;

import java.io.* ;
import java.net.MalformedURLException;
import java.net.URL;

import com.am.util.ResUtil;


public class ReadLicenceXmlFile {

	private String licenceFilePath ;

  public ReadLicenceXmlFile(String licenceFilePath) {
	  this.licenceFilePath = licenceFilePath ;
  }

  /**
   * 得到licence文件的输入流
   * @return FileInputStream
   */
  public FileInputStream getLicenceFileIn(){
	URL path = ReadLicenceXmlFile.class.getResource(licenceFilePath);
	try {
		if(path == null){
			path = new URL(ResUtil.getAppPath(ReadLicenceXmlFile.class,licenceFilePath));
		}
	} catch (MalformedURLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}finally{}
//    String operator = System.getProperty("os.name") ;
//    String licenceFileName = null ;
//    if(operator.equals("Windows")){
//      licenceFileName = path + "licence\\licence.xml";
//    }else{
//      licenceFileName = path + "licence/licence.xml";
//    }
//    File f = new File(licenceFileName) ;
	 
	if(path == null){
		return null ;
	}
    File f = new File(path.getFile()) ; 
    if(f == null || !(f.exists() && f.isFile())){
      return null ;
    }else{
      try{
        FileInputStream in = new FileInputStream(f) ;
        return in ;
      }catch(Exception e){
        return null ;
      }
    }

  }

  /**
   * 关闭licence输入流
   */
  public void closeLicenceFileIn(FileInputStream in ){
    if(in != null){
      try{
        in.close();
      }catch(Exception e){
        ;
      }
    }
  }
}
