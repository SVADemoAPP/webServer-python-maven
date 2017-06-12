package com.sva.web.controllers;

import com.sva.dao.ParamDao;
import com.sva.model.ParamModel;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

@Controller
@RequestMapping(value = "/paramconfig")
public class ParamController
{

    @Autowired
    private ParamDao dao;

    private static Logger log = Logger.getLogger(ParamController.class);

    @RequestMapping(value = "/api/getData", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getTableData(Model model)
    {
        log.info("ParamController:getTableData");

        Collection<ParamModel> ResultList = dao.doquery();
        Map<String, Object> modelMap = new HashMap<String, Object>(2);

        modelMap.put("error", null);
        modelMap.put("data", ResultList);

        return modelMap;
    }

    @RequestMapping(value = "/api/saveData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> saveData(
            @RequestParam(value = "id", required = false) int id,
            @RequestParam("banThreshold") Double banThreshold,
            @RequestParam("filterLength") Double filterLength,
            @RequestParam("horizontalWeight") Double horizontalWeight,
            @RequestParam("ongitudinalWeight") Double ongitudinalWeight,
            @RequestParam("maxDeviation") Double maxDeviation,
            @RequestParam("exceedMaxDeviation") Double exceedMaxDeviation,
            @RequestParam("correctMapDirection") Double correctMapDirection,
            @RequestParam("restTimes") Double restTimes,
            @RequestParam("filterPeakError") Double filterPeakError,
            @RequestParam("peakWidth") Double peakWidth,
            @RequestParam("step") Double step

    )
    {
        log.info("ParamController:saveData:: " + id + ',' + banThreshold + ','
                + filterLength + ',' + horizontalWeight + ',' + ongitudinalWeight + ','
                + maxDeviation + ',' + exceedMaxDeviation);
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        Date current = new Date();
        ParamModel sm = new ParamModel();
        sm.setBanThreshold(banThreshold);
        sm.setFilterLength(filterLength);
        sm.setHorizontalWeight(horizontalWeight);
        sm.setOngitudinalWeight(ongitudinalWeight);
        sm.setMaxDeviation(maxDeviation);
        sm.setExceedMaxDeviation(exceedMaxDeviation);
        sm.setUpdateTime(current.getTime());
        sm.setId(id);
        sm.setCorrectMapDirection(correctMapDirection);
        sm.setRestTimes(restTimes);
        sm.setFilterPeakError(filterPeakError);
        sm.setPeakWidth(peakWidth);
        sm.setStep(step);

        try
        {
            dao.saveData(sm);
        }
        catch (SQLException e)
        {
            log.info(e.getMessage());
        }
        modelMap.put("data", null);
        return modelMap;
    }
    
    @RequestMapping(value={"/api/saveApkCount"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public void saveApkCount(Model model, HttpServletRequest request, @RequestParam("type") String type)
    {
      String ip = getIpAddrs(request);
      List<Map<String, Object>> list = this.dao.findIp(ip);
      int count = 0;
      long time = System.currentTimeMillis();
      if (type.equals("adr"))
      {
        if (list.size() > 0)
        {
          count = Integer.parseInt((list.get(0)).get("androidCount").toString()) + 1;
          this.dao.updateAdrCount(ip, count);
        }
        else
        {
          count = 1;
          this.dao.saveAdrCount(ip, count, time);
        }
      }
      if (type.equals("web"))
      {
        if (list.size() > 0)
        {
          count = Integer.parseInt((list.get(0)).get("webCount").toString()) + 1;
          this.dao.updateWebCount(ip, count);
        }
        else
        {
          count = 1;
          this.dao.saveWebCount(ip, count, time);
        }
      }
      if (type.equals("ios"))
      {
        if (list.size() > 0)
        {
          count = Integer.parseInt((list.get(0)).get("iosCount").toString()) + 1;
          this.dao.updateIosCount(ip, count);
        }
        else
        {
          count = 1;
          this.dao.saveIosCount(ip, count, time);
        }
      }
      log.info("saveApkCount ip:" + ip);
    }

    @RequestMapping(value={"/api/saveIosCount"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> saveIosCount(Model model, HttpServletRequest request)
    {
      Collection<ParamModel> ResultList = this.dao.doquery();
      Map<String, Object> modelMap = new HashMap<>();

      modelMap.put("error", null);
      modelMap.put("data", ResultList);

      return modelMap;
    }

    @RequestMapping(value={"/api/getAllCount"}, method={org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getAllCount() {
      List<Map<String, Object>> ResultList = this.dao.getAllData();
      int webCount = 0;
      int adrCount = 0;
      int iosCount = 0;
      for (int i = 0; i < ResultList.size(); i++)
      {
        webCount += Integer.parseInt((ResultList.get(i)).get("webCount").toString());
        adrCount += Integer.parseInt((ResultList.get(i)).get("androidCount").toString());
        iosCount += Integer.parseInt((ResultList.get(i)).get("iosCount").toString());
      }
      Map<String, Object> modelMap = new HashMap<>();
      modelMap.put("web", Integer.valueOf(webCount));
      modelMap.put("adr", Integer.valueOf(adrCount));
      modelMap.put("ios", Integer.valueOf(iosCount));
      return modelMap;
    }

    public static String getIpAddrs(HttpServletRequest request) {
      String ip = request.getHeader("x-forwarded-for");
      if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
        ip = request.getHeader("Proxy-Client-IP");
      }
      if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
        ip = request.getHeader("WL-Proxy-Client-IP");
      }
      if ((ip == null) || (ip.length() == 0) || ("unknown".equalsIgnoreCase(ip))) {
        ip = request.getRemoteAddr();
      }
      return ip;
    }

}
