package com.am.cs12.config.conf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.am.cs12.config.ConfigCenter;
import com.am.cs12.config.RTUProtocolVO;
import com.am.cs12.config.ResetServerVO;
import com.am.cs12.util.Config;
import com.am.util.NumberUtil;

public class ResetServerConfig  extends Config{
	
	private ResetServerVO vo  ;
	
	public ResetServerConfig(){
		this.vo = new ResetServerVO() ;
	}

	/**
	 * 分析服务器运行参数
	 * 
	 * @throws ACException
	 */
	@SuppressWarnings("unchecked")
	public ResetServerVO parseOptions(String filePath) throws IllegalArgumentException {
		Element root = this.doc.getRootElement();
		if (root == null) {
			throw new IllegalArgumentException(filePath + "语法有错误，不能得到根元素!");
		}

		List list = root.getChildren();
		Iterator it = list.iterator();
		Element e = null;
		String name = null ;
		String value = null ;
		while (it.hasNext()) {
			e = (Element) it.next();
			name = e.getName() ;
			value = e.getText() ;
			if(value != null){
				value = value.trim() ;
			}
			if(name.equals(ResetServerConstant.id_key )){
				if(value == null || value.equals("")){
					throw new IllegalArgumentException("出错！" + filePath + "配置中，" + ResetServerConstant.id_key + "必须配置！");
				}
				vo.id = value ;
			}
			if(name.equals(ResetServerConstant.needResetProtocols_key)){
				if(value == null ){
					throw new IllegalArgumentException(filePath + "中元素"+ ResetServerConstant.needResetProtocols_key + "必须配置！");
				}
				value = value.replaceAll("，", ",") ;
				value = value.replaceAll("；", ";") ;
				String[] ss = value.split(";") ;
				vo.protocol_code = new String[ss.length][2] ;
				for(int i = 0 ; i < ss.length ; i++){
					String[] sss = ss[i].split(",") ;
					if(!this.isExistRtuProtocol(sss[0].trim())){
						throw new IllegalArgumentException(filePath + "中元素"+ ResetServerConstant.needResetProtocols_key + "配置协议" + ss[i].trim() + "在协议配置中不存在！");
					}
					vo.protocol_code[i][0] = sss[0].trim() ;
					vo.protocol_code[i][1] = sss[1].trim() ;
				}
			}
			
			if(name.equals(ResetServerConstant.startHour_key)){
				if(value == null ){
					throw new IllegalArgumentException(filePath + "中元素"+ ResetServerConstant.startHour_key + "必须配置！");
				}
				if(value == null || value.trim().equals("")){
					throw new IllegalArgumentException(filePath + "配置有错误，元素" + ResetServerConstant.startHour_key + "属性必须配置0-23间的整数！");
				}else{
					if(!NumberUtil.isPlusIntNumber(value.trim())){
						throw new IllegalArgumentException(filePath + "配置有错误，元素" + ResetServerConstant.startHour_key + "属性必须配置0-23间的整数！");
					}
					Integer temp = Integer.parseInt(value.trim()) ;
					if(temp < 0 || temp > 23){
						throw new IllegalArgumentException(filePath + "配置有错误，元素" + ResetServerConstant.startHour_key + "属性必须配置0-23间的整数！");
					}
					vo.startHour = temp ;
				}
			}
			if(name.equals(ResetServerConstant.startMinute_key)){
				if(value == null ){
					throw new IllegalArgumentException(filePath + "中元素"+ ResetServerConstant.startMinute_key + "必须配置！");
				}
				if(value == null || value.trim().equals("")){
					throw new IllegalArgumentException(filePath + "配置有错误，元素" + ResetServerConstant.startMinute_key + "属性必须配置0-59间的整数！");
				}else{
					if(!NumberUtil.isPlusIntNumber(value.trim())){
						throw new IllegalArgumentException(filePath + "配置有错误，元素" + ResetServerConstant.startMinute_key + "属性必须配置0-59间的整数！");
					}
					Integer temp = Integer.parseInt(value.trim()) ;
					if(temp < 0 || temp > 59){
						throw new IllegalArgumentException(filePath + "配置有错误，元素" + ResetServerConstant.startMinute_key + "属性必须配置0-59间的整数！");
					}
					vo.startMinute = temp ;
				}
			}
			
		}
		return vo ;
	}
	
	/**
	 * 是否存在RTU协议
	 * @param p
	 * @return
	 */
	private boolean isExistRtuProtocol(String p){
		HashMap<String, RTUProtocolVO> map = ConfigCenter.instance().getRtuProtocolMap() ;
		if(map != null){
			if(map.containsKey(p)){
				return true ;
			}
		}
		return false ;
	}

}

