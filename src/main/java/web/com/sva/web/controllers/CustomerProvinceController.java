/**   
 * @Title: CustomerProvinceController.java 
 * @Package com.sva.web.controllers 
 * @Description: 顾客归属地分布图 
 * @Company:ICS
 * @author label  
 * @date 2016年6月30日 下午3:45:53 
 * @version V1.0 
 */
package com.sva.web.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sva.common.ConvertUtil;
import com.sva.common.conf.Params;
import com.sva.dao.LocationDao;
import com.sva.service.PositionService;

/**
 * <p>Title:CustomerProvinceController</p>
 * <p>Description:顾客归属地分布图</p>
 * <p>Company: ICS</p>
 * @author label
 * @date 2016年6月30日 下午3:45:53
 */
@Controller
@RequestMapping("/position")
public class CustomerProvinceController {
	
	/**
	 * @Fields log 输出日志
	 */
	private static Logger log = Logger.getLogger(CustomerProvinceController.class);
	
	/**
	 * @Fields locationDao 操作locationxxx表
	 */
	@Autowired
    private LocationDao locationDao;
	
	/**
	 * @Fields positionService 获取归属地服务
	 */
	@Resource(name="positionService")
	private PositionService positionService;
	
	/** 
	 * @Title: getProvinceData 
	 * @Description: 获取顾客归属地数据
	 * @param floorNo 楼层号
	 * @param time 时间
	 * @return Map<String,Object>   
	 * @throws 
	 */
	@RequestMapping("/getProvinceData")
	@ResponseBody
	public Map<String,Object> getProvinceData(String floorNo, String beginTime, String endTime){
		log.info("[CustomerProvinceController.getProvinceData]用户查询location数据，参数为："+beginTime+"-"+endTime+"-"+floorNo);
		// 返回值
		Map<String, Object> modelMap = new HashMap<String, Object>(1);
		// 将时间段转换为日期数组
		Date start = ConvertUtil.dateStringFormat(beginTime,Params.YYYYMMDD);
		Date end = ConvertUtil.dateStringFormat(endTime,Params.YYYYMMDD);
		List<String> time = ConvertUtil.getRangeDay(start, end, Params.YYYYMMDD);
		// 获取指定楼层、指定时间内的所有用户的手机信息
		List<Map<String,Object>> result = locationDao.queryLocationForPosition(floorNo,time);
		// 存储各省份的用户数
		Map<String,Integer> data = new HashMap<String,Integer>();
		
		// 临时变量
		String phoneNo = "";
		String province = "";
		
		try
		{
			// 遍历用户，获取其电话所属的省份
			for(Map<String,Object> r : result)
			{
				phoneNo = r.get("userID").toString();
				// 如果该用户没有电话信息，则不作处理
				if("".equals(phoneNo)){
					continue;
				}
				// 调用获取省份信息服务
				province = positionService.sendRequest(phoneNo);
				
				// 如果该省份已存在，则计数加1；否则添加该省份，初始值为1
				if(data.containsKey(province)){
					data.put(province, data.get(province)+1);
				}else{
					data.put(province, 1);
				}
			}
		}
		catch(Exception e)
		{
			log.error(e.getMessage());
			modelMap.put("error", e.getMessage());
		}
		
		modelMap.put("data", data);
		
		return modelMap;
	}
	
}
