/**   
 * @Title: PositionService.java 
 * @Package com.sva.service 
 * @Description: 根据手机号，查询手机号所属的省份 
 * @Company:ICS
 * @author label   
 * @date 2016年6月30日 上午10:14:25 
 * @version V1.0 
 */
package com.sva.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sva.model.core.HttpParams;
import com.sva.service.core.HttpService;

import net.sf.json.JSONObject;

/**
 * <p>Title:PositionService</p>
 * <p>Description:根据手机号，查询手机号所属的省份 </p>
 * <p>Company: ICS</p>
 * @author label
 * @date 2016年6月30日 上午10:14:25
 */
@Service("positionService")
public class PositionService extends HttpService {
	
	/**
	 * @Fields log 输出日志
	 */
	private static Logger log = Logger.getLogger(PositionService.class);
	
	/**
	 * @Fields apiUrl 淘宝api地址
	 */
	private String apiUrl = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=";

	/* (non-Javadoc)
	 * <p>Title: sendRequest</p> 
	 * <p>Description: 向淘宝api发送请求，获取所属省份</p> 
	 * @param phoneNumber 手机号
	 * @return 省份
	 * @see com.sva.service.core.HttpService#sendRequest(java.lang.Object) 
	 */
	@Override
	public String sendRequest(Object phoneNumber) {
		// 返回值
		JSONObject jsonObj = null;
		// 设置请求参数
		String url = apiUrl + phoneNumber.toString();
		HttpParams params = new HttpParams();
		params.setUrl(url);
		
		// 获取返回值
		String result = this.httpRequest(params);
		
		// 将返回值转换为JSON对象
		try{
			jsonObj = JSONObject.fromObject(result.replaceAll("^[__]\\w{14}+[_ = ]+", ""));
		}catch(Exception e){
			log.error(e.getMessage());
		}
        
		return jsonObj.getString("province");
	}
	
	// main方法，测试用
	public static void main(String[] args){
		PositionService p = new PositionService();
		
		String r = p.sendRequest("15680067296").toString();
		
		System.out.println(r);
	}

}
