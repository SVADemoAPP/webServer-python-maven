/**   
 * @Title: FeatureBaseExportController.java 
 * @Package com.sva.web.controllers 
 * @Description: 特征库导出控制器
 * @author labelCS   
 * @date 2016年11月24日 上午10:00:24 
 * @version V1.0   
 */
package com.sva.web.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sva.common.ConvertUtil;
import com.sva.dao.PrruSignalDao;
import com.sva.model.FeatureBaseExportModel;
import com.sva.service.core.ExcelService;

/** 
 * @ClassName: FeatureBaseExportController 
 * @Description: 特征库导出控制器
 * @author labelCS 
 * @date 2016年11月24日 上午10:00:24 
 *  
 */
@Controller
@RequestMapping(value = "/featureBase")
public class FeatureBaseExportController {
    /**
     * @Fields log 输出日志
     */
    private static final Logger LOG = Logger.getLogger(FeatureBaseExportController.class);
    
    /** 
     * @Fields prruSignalDao : prruSignal数据库操作DAO
     */ 
    @Autowired
    private PrruSignalDao dao;
    
    /** 
     * @Title: exportExcel 
     * @Description: excel导出
     * @param request
     * @param response 
     */
    @RequestMapping(value = "/api/ExportExcel")
    @ResponseBody
    public void exportExcel(HttpServletResponse response, @RequestParam("placeId") String placeId){
        LOG.info("download excel");
        ExcelService<FeatureBaseExportModel> ex = new ExcelService<FeatureBaseExportModel>();
        // Excel标题
        String[] headers = {
                "id", 
                "mapId", 
                "x", 
                "y", 
                "floorid", 
                "checkValue", 
                "featureRadius", 
                "userId", 
                "gpp", 
                "featureValue", 
                "timestamp", 
                "formatDate" 
        };
        // 特征库数据
        List<FeatureBaseExportModel> dataset = dao.getFeatureBaseData(placeId);
        LOG.debug("data count"+dataset.size());
        
        try  
        {
            // 设置输出流
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            // 设定输出文件头
            response.setHeader("Content-disposition", "attachment; filename=featureBase"
                    + ConvertUtil.dateFormat(new Date(), "yyyyMMddHHmmss")
                    + ".xls");
            // 导出excel
            ex.exportExcel("featureBase", headers, dataset, response.getOutputStream(), null);
            
        } catch (FileNotFoundException e) {  
            LOG.error(e);
        } catch (IOException e) {  
            LOG.error(e);
        } catch (Exception e){
            LOG.error(e);
        }
    }
    
    /** 
     * @Title: exportTxt 
     * @Description: txt文件导出
     * @param request
     * @param response 
     */
    @RequestMapping(value = "/api/ExportTxt")
    @ResponseBody
    public void exportTxt(HttpServletResponse response, @RequestParam("placeId") String placeId){
        LOG.info("download txt");
        // 特征库数据
        List<FeatureBaseExportModel> dataset = dao.getFeatureBaseData(placeId); 
        
        // 设置输出流
        response.reset();
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        // 设定输出文件头
        response.setHeader("Content-disposition", "attachment; filename=featureBase"
                + ConvertUtil.dateFormat(new Date(), "yyyyMMddHHmmss")
                + ".txt");
        // 输出
        PrintWriter output;
        try {
            output = response.getWriter();
            for(int i = 0; i < dataset.size(); i ++){
                output.println("ADD FEATUREPOINT: " + dataset.get(i).toString());
            }
            output.close();
        } catch (IOException e) {
            LOG.error(e);
        }
    }
    
    /** 
     * @Title: getData 
     * @Description: 获取表格数据
     * @param request
     * @param response
     * @return 
     */
    @RequestMapping(value = "/api/getTableData")
    @ResponseBody
    public Map<String, Object> getData(@RequestParam("placeId") String placeId){
        LOG.debug("placeId:" + placeId);
        List<FeatureBaseExportModel> dataset = dao.getFeatureBaseData(placeId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", dataset);
        return result;
    }
}
