package com.sva.web.controllers;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sva.common.ConvertUtil;
import com.sva.common.Util;
import com.sva.dao.BZPramesDao;
import com.sva.model.BZPramesModel;
import com.sva.model.BZPramesModel1;

@Controller
@RequestMapping(value = "/content")
public class BZPramesController
{

    @Autowired
    private BZPramesDao bzPramesDao;

    private static Logger log = Logger.getLogger(BZPramesController.class);

    @RequestMapping(value = "/api/getData", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getTableData(Model model)
    {
        log.info("ParamController:getTableData");

        Collection<BZPramesModel> ResultList = bzPramesDao.doquery();
        Map<String, Object> modelMap = new HashMap<String, Object>(2);

        modelMap.put("error", null);
        modelMap.put("data", ResultList);

        return modelMap;
    }

    
    @RequestMapping(value = "/api/getData2", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getTableData2(Model model)
    {
        log.info("ParamController:getTableData");

        Collection<BZPramesModel> ResultList = bzPramesDao.doquery3();
        Map<String, Object> modelMap = new HashMap<String, Object>(2);

        modelMap.put("error", null);
        modelMap.put("data", ResultList);

        return modelMap;
    }
    
    /** 
     * @Title: getTableData3 
     * @Description: 菁蓉镇数据列表
     * @param model
     * @return Map<String,Object>       
     * @throws 
     */
    @RequestMapping(value = "/api/getData3", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getTableData3(Model model)
    {
        log.info("ParamController:getTableData");

        Collection<BZPramesModel> ResultList = bzPramesDao.doquery4();
        Map<String, Object> modelMap = new HashMap<String, Object>(2);

        modelMap.put("error", null);
        modelMap.put("data", ResultList);

        return modelMap;
    }

    
    @RequestMapping(value = "/api/getData1", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getTableData1(Model model)
    {
        log.info("ParamController:getTableData");

        Collection<BZPramesModel1> ResultList = bzPramesDao.doquery2();
        Map<String, Object> modelMap = new HashMap<String, Object>(2);

        modelMap.put("error", null);
        modelMap.put("data", ResultList);

        return modelMap;
    }
    @RequestMapping(value = "/api/saveData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> saveData(HttpServletRequest request,
            @RequestParam("densitySel") int densitySel,
            @RequestParam("radiusSel") int radiusSel,
            @RequestParam("densitySel1") int densitySel1,
            @RequestParam("radiusSel1") int radiusSel1,
            @RequestParam("densitySel2") int densitySel2,
            @RequestParam("radiusSel2") int radiusSel2,
            @RequestParam("densitySel3") int densitySel3,
            @RequestParam("radiusSel3") int radiusSel3,
            @RequestParam("densitySel4") int densitySel4,
            @RequestParam("radiusSel4") int radiusSel4,
            @RequestParam("densitySel5") int densitySel5,
            @RequestParam("radiusSel5") int radiusSel5,
            @RequestParam("densitySel6") int densitySel6,
            @RequestParam("radiusSel6") int radiusSel6,
            @RequestParam("densitySel7") int densitySel7,
            @RequestParam("radiusSel7") int radiusSel7,
            @RequestParam("floorNo") String floorNo,
            @RequestParam("floorNo2") String floorNo2,
            @RequestParam("floorNo3") String floorNo3,
            @RequestParam("floorNo4") String floorNo4,
            @RequestParam("floorNo5") String floorNo5,
            @RequestParam("floorNo6") String floorNo6,
            @RequestParam("floorNo7") String floorNo7,
            @RequestParam("floorNo8") String floorNo8,
            @RequestParam("periodSel") int periodSel,
            @RequestParam("coefficient") Double coefficient,
            @RequestParam("startTime") String startTime)
    {
        String ip = Util.getIpAddr(request);
        log.info(ip+"insert hangzhoue");
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        BigDecimal bd = new BigDecimal(floorNo);
        BigDecimal bd2 = new BigDecimal(floorNo2);
        BigDecimal bd3 = new BigDecimal(floorNo3);
        BigDecimal bd4 = new BigDecimal(floorNo4);
        BigDecimal bd5 = new BigDecimal(floorNo5);
        BigDecimal bd6 = new BigDecimal(floorNo6);
        BigDecimal bd7 = new BigDecimal(floorNo7);
        BigDecimal bd8 = new BigDecimal(floorNo8);
        BZPramesModel sm = new BZPramesModel();
        sm.setDensitySel(densitySel);
        sm.setRadiusSel(radiusSel);
        sm.setDensitySel1(densitySel1);
        sm.setRadiusSel1(radiusSel1);
        sm.setDensitySel2(densitySel2);
        sm.setRadiusSel2(radiusSel2);
        sm.setDensitySel3(densitySel3);
        sm.setRadiusSel3(radiusSel3);
        sm.setDensitySel4(densitySel4);
        sm.setRadiusSel4(radiusSel4);
        sm.setDensitySel5(densitySel5);
        sm.setRadiusSel5(radiusSel5);
        sm.setDensitySel6(densitySel6);
        sm.setRadiusSel6(radiusSel6);
        sm.setDensitySel7(densitySel7);
        sm.setRadiusSel7(radiusSel7);
        sm.setFloorNo(bd);
        sm.setFloorNo2(bd2);
        sm.setFloorNo3(bd3);
        sm.setFloorNo4(bd4);
        sm.setFloorNo5(bd5);
        sm.setFloorNo6(bd6);
        sm.setFloorNo7(bd7);
        sm.setFloorNo8(bd8);
        sm.setPeriodSel(periodSel);
        sm.setCoefficient(coefficient);
        startTime = "2016-02-15 " + startTime;
        sm.setStartTime(ConvertUtil.dateStringFormat(startTime,
                "yyyy-MM-dd HH:mm:ss"));
        sm.setId(1);
        try
        {
            bzPramesDao.updateBZInfo(sm);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        modelMap.put("data", null);
        return modelMap;
    }

    
    @RequestMapping(value = "/api/saveData2", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> saveData2(
            @RequestParam("densitySel") int densitySel,
            @RequestParam("radiusSel") int radiusSel,
            @RequestParam("densitySel1") int densitySel1,
            @RequestParam("radiusSel1") int radiusSel1,
            @RequestParam("densitySel2") int densitySel2,
            @RequestParam("radiusSel2") int radiusSel2,
            @RequestParam("floorNo") String floorNo,
            @RequestParam("floorNo2") String floorNo2,
            @RequestParam("floorNo3") String floorNo3,
            @RequestParam("periodSel") int periodSel,
            @RequestParam("coefficient") Double coefficient,
            @RequestParam("startTime") String startTime)
    {
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        BigDecimal bd = new BigDecimal(floorNo);
        BigDecimal bd2 = new BigDecimal(floorNo2);
        BigDecimal bd3 = new BigDecimal(floorNo3);
        BZPramesModel sm = new BZPramesModel();
        sm.setDensitySel(densitySel);
        sm.setRadiusSel(radiusSel);
        sm.setDensitySel1(densitySel1);
        sm.setRadiusSel1(radiusSel1);
        sm.setDensitySel2(densitySel2);
        sm.setRadiusSel2(radiusSel2);
        sm.setFloorNo(bd);
        sm.setFloorNo2(bd2);
        sm.setFloorNo3(bd3);
        sm.setPeriodSel(periodSel);
        sm.setCoefficient(coefficient);
        startTime = "2016-02-15 " + startTime;
        sm.setStartTime(ConvertUtil.dateStringFormat(startTime,
                "yyyy-MM-dd HH:mm:ss"));
        sm.setId(1);
        try
        {
            bzPramesDao.updateSHInfo(sm);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        modelMap.put("data", null);
        return modelMap;
    }

    
    @RequestMapping(value = "/api/saveData1", method =
		{ RequestMethod.POST})
		@ResponseBody
		public Map<String, Object> saveData1(
				@RequestParam("densitySel") int densitySel,
				@RequestParam("radiusSel") int radiusSel,
				@RequestParam("densitySel1") int densitySel1,
				@RequestParam("radiusSel1") int radiusSel1,
				@RequestParam("densitySel2") int densitySel2,
				@RequestParam("radiusSel2") int radiusSel2,
				@RequestParam("placeId") int placeId,
				@RequestParam("placeId2") int placeId2,
				@RequestParam("placeId2Sp") int placeId2Sp,
				@RequestParam("placeId3") int placeId3,
				@RequestParam("placeId3Sp") int placeId3Sp,
				@RequestParam("floorNo") String  floorNo,
				@RequestParam("floorNo2") String  floorNo2,
				@RequestParam("floorNo2Sp") String  floorNo2Sp,
				@RequestParam("floorNo3") String  floorNo3,
				@RequestParam("floorNo3Sp") String  floorNo3Sp,
				@RequestParam("periodSel") int  periodSel,
				@RequestParam("coefficient") Double coefficient,
				@RequestParam("startTime") String startTime)
		{
			log.info("ParamController:saveData:: "+ placeId + ','
					+ floorNo + ',' + startTime );
			Map<String, Object> modelMap = new HashMap<String, Object>(2);
			BigDecimal bd=new BigDecimal(floorNo);   
			BigDecimal bd2=new BigDecimal(floorNo2);   
			BigDecimal bd2sp=new BigDecimal(floorNo2Sp);   
			BigDecimal bd3=new BigDecimal(floorNo3);   
			BigDecimal bd3sp=new BigDecimal(floorNo3Sp);   
			BZPramesModel1 sm = new BZPramesModel1();
			sm.setDensitySel(densitySel);
			sm.setRadiusSel(radiusSel);
			sm.setDensitySel1(densitySel1);
			sm.setRadiusSel1(radiusSel1);
			sm.setDensitySel2(densitySel2);
			sm.setRadiusSel2(radiusSel2);
			sm.setPlaceId(placeId);
			sm.setPlaceId2(placeId2);
			sm.setPlaceId2sp(placeId2Sp);
			sm.setPlaceId3(placeId3);
			sm.setPlaceId3sp(placeId3Sp);
			sm.setFloorNo(bd);
			sm.setFloorNo2(bd2);
			sm.setFloorNo2sp(bd2sp);
			sm.setFloorNo3(bd3);
			sm.setFloorNo3sp(bd3sp);
			sm.setPeriodSel(periodSel);
			sm.setCoefficient(coefficient);
			startTime = "2016-02-15 " + startTime;
			sm.setStartTime(ConvertUtil.dateStringFormat(startTime,"yyyy-MM-dd HH:mm:ss"));
			sm.setId(1);
			try
			{
				bzPramesDao.updateBZInfo1(sm);
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
			modelMap.put("data", null);
			return modelMap;
		}
    
    /** 
     * @Title: saveData2 
     * @Description: 菁蓉镇参数配置
     * @param densitySel
     * @param radiusSel
     * @param densitySel1
     * @param radiusSel1
     * @param densitySel2
     * @param radiusSel2
     * @param densitySel4
     * @param radiusSel4
     * @param floorNo
     * @param floorNo2
     * @param floorNo3
     * @param floorNo4
     * @param periodSel
     * @param coefficient
     * @param startTime
     * @return Map<String,Object>       
     * @throws 
     */
    @RequestMapping(value = "/api/saveData3", method = {RequestMethod.POST})
   @ResponseBody
   public Map<String, Object> saveData3(
           @RequestParam("densitySel") int densitySel,
           @RequestParam("radiusSel") int radiusSel,
           @RequestParam("densitySel1") int densitySel1,
           @RequestParam("radiusSel1") int radiusSel1,
           @RequestParam("densitySel2") int densitySel2,
           @RequestParam("radiusSel2") int radiusSel2,
           @RequestParam("densitySel4") int densitySel4,
           @RequestParam("radiusSel4") int radiusSel4,
           @RequestParam("floorNo") String floorNo,
           @RequestParam("floorNo2") String floorNo2,
           @RequestParam("floorNo3") String floorNo3,
           @RequestParam("floorNo4") String floorNo4,
           @RequestParam("periodSel") int periodSel,
           @RequestParam("coefficient") Double coefficient,
           @RequestParam("startTime") String startTime)
   {
       Map<String, Object> modelMap = new HashMap<String, Object>(2);
       BigDecimal bd = new BigDecimal(floorNo);
       BigDecimal bd2 = new BigDecimal(floorNo2);
       BigDecimal bd3 = new BigDecimal(floorNo3);
       BigDecimal bd4 = new BigDecimal(floorNo4);
       BZPramesModel sm = new BZPramesModel();
       sm.setDensitySel(densitySel);
       sm.setRadiusSel(radiusSel);
       sm.setDensitySel1(densitySel1);
       sm.setRadiusSel1(radiusSel1);
       sm.setDensitySel2(densitySel2);
       sm.setRadiusSel2(radiusSel2);
       sm.setDensitySel4(densitySel4);
       sm.setRadiusSel4(radiusSel4);
       sm.setFloorNo(bd);
       sm.setFloorNo2(bd2);
       sm.setFloorNo3(bd3);
       sm.setFloorNo4(bd4);
       sm.setPeriodSel(periodSel);
       sm.setCoefficient(coefficient);
       startTime = "2016-02-15 " + startTime;
       sm.setStartTime(ConvertUtil.dateStringFormat(startTime,
               "yyyy-MM-dd HH:mm:ss"));
       sm.setId(1);
       try
       {
           bzPramesDao.updateSHInfoJing(sm);
       }
       catch (SQLException e)
       {
           e.printStackTrace();
       }
       modelMap.put("data", null);
       return modelMap;
   }
}
