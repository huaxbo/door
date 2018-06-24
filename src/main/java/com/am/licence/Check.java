package com.am.licence;

import com.am.licence.key.Key;
import java.net.InetAddress;
import java.util.*;
import java.io.* ;
import java.net.NetworkInterface;
import java.security.interfaces.RSAPublicKey;



public class Check{

  public Check() {
  }

  public boolean isWanYongId(String idConfig){
	  if(idConfig != null && idConfig.equals("0-0-0-0")){
		  //是万用Id
		  return true ;
	  }else{
		  return false ;
	  }
  }
  /**
   * 检测ip，看配置ID与实际本机网卡ID是否一致
   * @param ip String
   * @return boolean
   */
  public boolean checkId(String idConfig){
    String operator = System.getProperty("os.name") ;
    boolean flag = false ;
    if(operator.startsWith("Windows")){
      //在windows操作系统
    	ArrayList<String> list = this.getMacOnWindow() ;
    	if(list == null || list.size() == 0){
    		System.out.println("出错，没有得到本机的网卡序列号，从而不能进行licence验证！") ;
    		return false ;
    	}else{
    		for(int i = 0 ; i < list.size() ; i++){
    			if(idConfig.equals(list.get(i))){
    	    		return true ;
    			}
    		}
    	}
      return flag ;
    }else{
      //在linux操作系统
		System.out.println("出错，不支持linux系统，从而不能进行licence验证！") ;
        return flag ;
    }
 }
  /**
   * 
   * @return
   */
  private ArrayList<String> getMacOnWindow() {
      ArrayList<String> list = new ArrayList<String>() ;;
      try {
              String s1 = "ipconfig /all";
              Process process = Runtime.getRuntime().exec(s1);
              BufferedReader bufferedreader = new BufferedReader(
                              new InputStreamReader(process.getInputStream()));
              String nextLine;
              for (String line = bufferedreader.readLine(); line != null; line = nextLine) {
                      nextLine = bufferedreader.readLine();
                      int i = 0;
                	  if(line.indexOf("Physical Address") > 0){
                		  i = line.indexOf("Physical Address") + 36;//“Physical Address”-windowsxp及windows server 2003使用
                	  }
                	  if(line.indexOf("物理地址") > 0){
                		  i = line.indexOf("物理地址") + 32;//"物理地址"-window7系统使用
                	  }
                	  if(i == 0){
                		  continue;
                	  }
                	  String s = line.substring(i);//获取当前服务器物理地址
                      list.add(s.trim());
              }
              bufferedreader.close();
              process.waitFor();
      } catch (Exception exception) {
              list = null ;
      }
      return list ;
}
 /**
   * 
   * @param idConfig
   * @return
   */
  public boolean isWanYongIp(String ipConfig){
	  if(ipConfig != null && ipConfig.equals("1.1.1.119")){
		  //是万用IP
		  return true ;
	  }else{
		  return false ;
	  }
  }

  /**
   * 检测ip，看配置IP与实际本机IP是否一致
   * @param ip String
   * @return boolean
   */
  @SuppressWarnings("unchecked")
public boolean checkIp(String ipConfig){
    String operator = System.getProperty("os.name") ;
    String ip = "" ;
    boolean flag = true ;
    if(operator.startsWith("Windows")){
      //在windows操作系统
      try {
        InetAddress inet = InetAddress.getLocalHost();
        ip = inet.getHostAddress();

        if(ip.equals(ipConfig.trim())){
          flag = true ;
        }else{
          flag = false ;
        }
      }
      catch (Exception e) {
        flag = false ;
        e.printStackTrace();
      }
      return flag ;
    }else{
      //在linux操作系统
      try{


        Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress inetAddress = null;


        boolean banner = false ;
        while (netInterfaces.hasMoreElements()) {
          NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
          Enumeration inetAddresses = ni.getInetAddresses();
          while (inetAddresses.hasMoreElements()) {
            inetAddress = (InetAddress) inetAddresses.nextElement() ;
            if(inetAddress.getHostAddress().equals(ipConfig)){
              ip = inetAddress.getHostAddress() ;
              banner = true ;
              break ;
            }
          }
          if(banner){
            break ;
          }
        }


        if(banner){
          flag = true ;
        }else{
          flag = false ;
        }

      }catch(Exception ee){
        flag = false ;
        ee.printStackTrace();
      }
       return flag ;
    }
 }


  /**
   *
   * @return boolean
   */
  public boolean checkSignedValue(String ip ,
                                    String domainNum ,
                                    RSAPublicKey publicKey ,
                                    String signedValue) {
    Key k = new Key() ;
    String message = this.createMessage(ip , domainNum) ;

    boolean flag = k.verify(signedValue,
                            message,
                            publicKey);
    return flag;
  }

 
  /**
   *
   * @param ip String
   * @param domainNum String
   * @return String
   */
  private String createMessage(String ip , String domainNum){
	    String message = "" ;
	    message += ip ;
	    message += "." ;
	    message += domainNum ;
	    int count = 0 ;
	    String message1 = "" ;
	    String message2 = "" ;
	    for(int i = 0 ; i < message.length() ; i++){
	      if(message.charAt(i) == '.'){
	        message1 = message.substring(0 , i) ;
	        message2 = message.substring(i+1 , message.length()) ;
	        if(count == 0){
	          count++ ;
	          message = message1 + "Automic" + message2 ;
	        }
	        if(count == 1){
	          count++ ;
	          message = message1 + "Beijing" + message2 ;
	        }
	        if(count == 2){
	          count++ ;
	          message = message1 + "China" + message2 ;
	        }
	        if(count == 3){
	          count++ ;
	          message = message1 + "CommuServer" + message2 ;
	        }
	      }
	    }
	    message += "lrywhq-02D235-cn.com.automic" ;
	    return message ;
  }
}
