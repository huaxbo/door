package com.am.cs12.commu.core.log;

import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.am.cs12.config.AmLogVO;
import com.am.util.DateTime;
import com.am.cs12.config.ConfigCenter;

public class AmLog {

	private static Logger log = LogManager.getLogger(AmLog.class.getName()) ;
	private static AmLogVO amLogVO ;

	/**
	 * 记录终端日志
	 * @param rtuId
	 * @param data
	 */
	public static void log(String rtuId , String conent){
		if(amLogVO == null){
			amLogVO = ConfigCenter.instance().getAmLogVO() ;
			if(amLogVO == null){
				log.error("严重错误，终端日志相关配置参数未得到！") ;
			}
			return ;
		}
		if(rtuId == null || rtuId.trim().equals("")){
			log.error("严重错误，写入终端日志时，rtuId未提供！") ;
			return ;
		}
		if(conent == null || conent.equals("")){
			log.error("严重错误，写入终端日志时，日志内容未提供！") ;
			return ;
		}
		File f = getLogFile(amLogVO.logDir , rtuId.trim()) ;
		if(f != null){
			writeLog(f , DateTime.yyyy_MM_dd_HH_mm_ss() + "  " + conent) ;
		}else{
			log.error("不能得到rtuId:" + rtuId + "的终端日志文件！") ;
		}
	}
	
	/**
	 * 得到日志文件
	 * @param rtuId
	 * @return
	 */
	private static File getLogFile(String path , String rtuId) {
		File f = new File(path + rtuId + ".log");
		FileInputStream in = null;
		try {
			if (f.exists()) {
				in = new FileInputStream(f);
				if(in != null){
					if(in.available() >= amLogVO.fileMaxSize){
						in.close() ;//必须加上此语句
						File oldestLog = new File(path + rtuId + ".log." + (amLogVO.fileMaxNum - 1)) ;
						if(oldestLog.exists()){
							oldestLog.delete() ;
						}
						for(int i = (amLogVO.fileMaxNum - 2) ; i > 0  ; i--){
							File oldLog = new File(path + rtuId + ".log." + i) ;
							if(oldLog.exists()){
								oldLog.renameTo(new File(path + rtuId + ".log." + (i + 1))) ;
								oldLog.delete() ;
							}
						}
						f.renameTo(new File(path + rtuId + ".log." + 1)) ;
						f.delete() ;
						
						f = new File(path + rtuId + ".log");
						if(!f.exists()){
							f.createNewFile() ;
							return f ;
						}
					}else{
						return f ;
					}
				}else{
					return null ;
				}
			}else{
				f.createNewFile() ;
				return f ;
			}
		} catch (Exception e) {
			log.error("得到终端(" + rtuId + ")日志文件时出错" , e) ;
//			e.printStackTrace();
		}finally{
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				log.error("关闭终端日志文件(" + f.getName() + ")读入流时出错" , e) ;
//				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * 向文件中写入日志
	 * @param f
	 * @param data
	 */
	private static void writeLog(File f, String conent) {
		if (f.exists()) {
			BufferedWriter out = null;
			try {
				out = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(f, true))); // 参数为true，表示追加内容
				out.write(conent + "\n");
			} catch (Exception e) {
				log.error("写入终端日志文件(" + f.getName() + ")时出错" , e) ;
//				e.printStackTrace();
			} finally {
				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					log.error("关闭终端日志文件(" + f.getName() + ")写出流时出错" , e) ;
//					e.printStackTrace();
				}
			}
		}
	}
}
