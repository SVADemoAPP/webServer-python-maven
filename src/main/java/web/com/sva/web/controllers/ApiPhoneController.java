/**   
 * @Title: ApiPhoneController.java 
 * @Package com.sva.web.controllers 
 * @Description: app对接使用的Controller
 * @author labelCS   
 * @date 2016年9月29日 下午4:53:26 
 * @version V1.0   
 */
package com.sva.web.controllers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sva.common.conf.GlobalConf;
import com.sva.service.CollectThread;
import com.sva.service.PrruService;
import com.sva.web.models.PrruFeatureApiModel;

/** 
 * @ClassName: ApiPhoneController 
 * @Description: app对接使用的Controller
 * @author labelCS 
 * @date 2016年9月29日 下午4:53:26 
 *  
 */
@Controller
@RequestMapping(value = "/api/app")
public class ApiPhoneController {
    private static final Logger LOG = Logger.getLogger(ApiPhoneController.class);
    
    /** 
     * @Fields prruService : 处理prru特征定位的相关服务
     */ 
    @Autowired
    PrruService prruService;
    
    /** 
     * @Title: getStartCollectPrru 
     * @Description: 发起采集特征请求接口
     * @param requestModel
     * @return 
     */
    @RequestMapping(value = "/getStartCollectPrru", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getStartCollectPrru(@RequestBody PrruFeatureApiModel requestModel)
    {
        LOG.debug("Collect Prru Feature.ip:" + requestModel.getUserId());
        return prruService.collectFeatureValue(requestModel);
    }
    
    /** 
     * @Title: getCollectResult 
     * @Description: 查询采集结果请求接口 
     * @param requestModel
     * @return 
     */
    @RequestMapping(value = "/getCollectResult", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getCollectResult(@RequestBody PrruFeatureApiModel requestModel)
    {
        LOG.debug("Get result.ip:" + requestModel.getUserId());
        return prruService.checkIsFinished(requestModel);
    }
    
    /** 
     * @Title: stopCollectPrru 
     * @Description: 终止采集请求接口 
     * @param requestModel
     * @return 
     */
    @RequestMapping(value = "/stopCollectPrru", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> stopCollectPrru(@RequestBody PrruFeatureApiModel requestModel)
    {
        LOG.debug("Get result.ip:" + requestModel.getUserId());
        CollectThread t = (CollectThread) GlobalConf.getPrruThread(requestModel.getUserId());
        if(t != null){
            t.stopThread();
        }
        
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", true);
        return result;
    }
    
    /** 
     * @Title: getAllFeaturePoint 
     * @Description: 查询所有特征库的位置点的请求接口
     * @param floorNo
     * @return 
     */
    @RequestMapping(value = "/getAllFeaturePoint", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getAllFeaturePoint(String floorNo)
    {
        LOG.debug("floorNo:" + floorNo);
        return prruService.getAllFeaturePosition(floorNo);
    }
    
    /** 
     * @Title: removeFeatureValue 
     * @Description: 删除指定位置已采集的特征数据的请求接口
     * @param id
     * @return 
     */
    @RequestMapping(value = "/removeFeatureValue", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> removeFeatureValue(String id)
    {
        LOG.debug("ID:" + id);
        return prruService.deleteFeatureById(Integer.parseInt(id));
    }
    
    /** 
     * @Title: getData 
     * @Description: 特征库定位接口
     * @param userId
     * @param x
     * @param y
     * @return 
     */
    @RequestMapping(value = "/getData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getData(String userId, String x, String y, 
            @RequestParam(value="x1", required=false)String x1, 
            @RequestParam(value="y1", required=false)String y1,
            @RequestParam(value="floorNo", required=false)String floorNo)
    {
        x = new BigDecimal(x).divide(new BigDecimal(10)).toString();
        y = new BigDecimal(y).divide(new BigDecimal(10)).toString();
        if(x1 != null && y1 != null){
            x1 = new BigDecimal(x1).divide(new BigDecimal(10)).toString();
            y1 = new BigDecimal(y1).divide(new BigDecimal(10)).toString();
        }
        LOG.debug("ID:" + userId+",x:"+x+",y:"+y+",x1:"+x1+",y1:"+y1+",floorNo:"+floorNo);
        return prruService.getLocationPrru(userId,x,y,x1,y1,floorNo);
    }
    
    /** 
     * @Title: getCurrentPrruWithRsrp 
     * @Description: 获取当前的prru及对应的信号强度
     * @param userId
     * @return 
     */
    @RequestMapping(value = "/getCurrentPrruWithRsrp", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getCurrentPrruWithRsrp(String userId)
    {
        LOG.debug("ID:" + userId);
        return prruService.getCurrentPrruWithRsrp(userId);
    }
}
