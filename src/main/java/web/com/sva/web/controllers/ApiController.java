package com.sva.web.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.util.JSONPObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sva.common.ConvertUtil;
import com.sva.common.HttpUtil;
import com.sva.common.TCPDesktopServer;
import com.sva.common.Util;
import com.sva.common.conf.Params;
import com.sva.dao.AccuracyDao;
import com.sva.dao.AreaDao;
import com.sva.dao.BZPramesDao;
import com.sva.dao.CodeDao;
import com.sva.dao.CommonDao;
import com.sva.dao.DynamicAccuracyDao;
import com.sva.dao.ElectronicDao;
import com.sva.dao.EstimateDao;
import com.sva.dao.GeofencingDao;
import com.sva.dao.LocationDao;
import com.sva.dao.LocationDelayDao;
import com.sva.dao.MapsDao;
import com.sva.dao.MessageDao;
import com.sva.dao.MessagePushDao;
import com.sva.dao.ParamDao;
import com.sva.dao.PhoneDao;
import com.sva.dao.PrruDao;
import com.sva.dao.PushMsgDao;
import com.sva.dao.RegisterDao;
import com.sva.dao.SellerDao;
import com.sva.dao.StaticAccuracyDao;
import com.sva.dao.SvaDao;
import com.sva.dao.TicketDao;
import com.sva.model.AcquisitionPoint;
import com.sva.model.AreaModel;
import com.sva.model.CodeModel;
import com.sva.model.ElectronicModel;
import com.sva.model.IlsModel;
import com.sva.model.LocationDelay;
import com.sva.model.LocationModel;
import com.sva.model.MapsModel;
import com.sva.model.MessageModel;
import com.sva.model.MessagePush;
import com.sva.model.MyModel;
import com.sva.model.ParamModel;
import com.sva.model.PhoneModel;
import com.sva.model.PrruModel;
import com.sva.model.RegisterModel;
import com.sva.model.SellerModel;
import com.sva.model.SvaModel;
import com.sva.model.TicketModel;
import com.sva.web.models.AccuracyApiModel;
import com.sva.web.models.ApiRequestModel;
import com.sva.web.models.DynamicAccuracyApiModel;
import com.sva.web.models.GeofencingModel;
import com.sva.web.models.MapMngModel;
import com.sva.web.models.MsgMngModel;
import com.sva.web.models.StaticAccuracyApiModel;

@Controller
@RequestMapping(value = "/api")
public class ApiController
{

    private static Logger log = Logger.getLogger(ApiController.class);

    @Autowired
    private LocationDao dao;

    @Autowired
    private ElectronicDao electronicDao;
    
    @Autowired
    private TicketDao ticDao;

    @Autowired
    private MessagePushDao messagePushDao;

    @Autowired
    private LocationDelayDao delayDao;

    @Autowired
    private MapsDao daoMaps;

    @Autowired
    private MessageDao daoMsg;

    @Autowired
    private CodeDao daoCode;

    @Autowired
    private PhoneDao phoneDao;

    @Autowired
    private RegisterDao registerDao;

    @Autowired
    private AccuracyDao daoAccuracy;

    @Autowired
    private StaticAccuracyDao staticAccuracyDao;

    @Autowired
    private DynamicAccuracyDao dynamicAccuracyDao;

    @Autowired
    private SellerDao daoSeller;

    @Autowired
    private EstimateDao daoEstimate;

    @Autowired
    private SvaDao svaDao;

    @Autowired
    private PrruDao prruDao;

    @Autowired
    private CommonDao comDao;

    @Autowired
    private ParamDao daoParam;

    @Autowired
    private AreaDao daoArea;

    @Autowired
    private BZPramesDao bzDao;

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private GeofencingDao geofencingDao;
    
    @Autowired
    private PushMsgDao pushMsgDao;

    @RequestMapping(value = "/getData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getData(@RequestBody ApiRequestModel requestModel)
    {

        log.debug("api getData.ip:" + requestModel.getIp());

        if (StringUtils.isEmpty(requestModel.getIp()))
        {
            return null;
        }
        List<String> lists = null;
        List<String> allList = null;
        Collection<GeofencingModel> geofencingModels = null;
        GeofencingModel ge = null;
        List<GeofencingModel> list2 = new ArrayList<GeofencingModel>();
        List<LocationModel> list = new ArrayList<LocationModel>(10);
        String ip = ConvertUtil.convertMacOrIp(requestModel.getIp());
        Collection<LocationModel> ResultList = new ArrayList<LocationModel>(10);
        ResultList = dao.queryLocationByUseId(ip);
        log.debug("dao result size:"+ResultList.size());
        // 查询参数更新的时间
        long paramUpdate = 0;
        // 查询参数更新的时间
        Collection<ParamModel> paramUpdates = daoParam.doquery();
        List<String> ticketList = new ArrayList<String>();
        TreeSet<Integer> areaIdList = new TreeSet<Integer>();
        int len = 0;
        String tikectPath = null;
        for (ParamModel paramModel : paramUpdates)
        {
            paramUpdate = paramModel.getUpdateTime();
        }

        for (LocationModel l : ResultList)
        {
            list.add(l);
        }

        Map<String, Object> modelMap = new HashMap<String, Object>(3);
        // long time = System.currentTimeMillis() - 60 * 1000;
        if (ResultList.size() >= 1)
        {
            LocationModel loc = list.get(0);
            // get message info
            // Collection<MessageModel> msgList = new
            // ArrayList<MessageModel>(10);
            Collection<MessageModel> msgList1 = new ArrayList<MessageModel>(10);
            Collection<AreaModel> msgList2 = new ArrayList<AreaModel>(10);
            // Collection<ElectronicModel> msgList2 = new
            // ArrayList<ElectronicModel>(
            // 10);
            // msgList = daoMsg.queryByLocation(loc);
            msgList1 = daoMsg.queryByLocation1(loc);
            msgList2 = daoArea.geofencingByLocation(loc);
            // msgList2 = electronicDao.queryByLocation2(loc);
            // List<MsgMngModel> outList = new ArrayList<MsgMngModel>(10);
            List<MsgMngModel> outList1 = new ArrayList<MsgMngModel>(10);
            List<ElectronicModel> outList2 = new ArrayList<ElectronicModel>(10);
            MsgMngModel mmm = null;

            for (AreaModel a : msgList2)
            {
                int zoneid = a.getZoneId();
                geofencingModels = geofencingDao.getGeofencing(
                        String.valueOf(zoneid), ip);
                for (GeofencingModel g : geofencingModels)
                {
                    ge = new GeofencingModel();
                    ge.setEntre(g.getEntre());
                    ge.setId(g.getId());
                    ge.setIdType(g.getIdType());
                    ge.setMapid(g.getMapid());
                    ge.setZoneid(g.getZoneid());
                    ge.setTimestamp(g.getTimestamp());
                    ge.setUserid(g.getUserid());
                    list2.add(ge);
                }
            }
            // ElectronicModel eee = null;
            BigDecimal fina = new BigDecimal(2);
            // for (ElectronicModel l : msgList2)
            // {
            // BigDecimal x = new BigDecimal(0);
            // BigDecimal x1 = new BigDecimal(0);
            // BigDecimal y = new BigDecimal(0);
            // BigDecimal y1 = new BigDecimal(0);
            // BigDecimal tempx = new BigDecimal(0);
            // BigDecimal tempy = new BigDecimal(0);
            // x = l.getxSpot();
            // x1 = l.getX1Spot();
            // y = l.getySpot();
            // y1 = l.getY1Spot();
            // tempx = (x1.add(x)).divide(fina, 2);
            // tempy = (y1.add(y)).divide(fina, 2);
            // eee = new ElectronicModel();
            // eee.setAreaId(l.getAreaId());
            // eee.setElectronicName(l.getElectronicName());
            // eee.setFloor(l.getFloor());
            // eee.setFloorNo(l.getFloorNo());
            // // eee.setX1Spot(l.getX1Spot());
            // eee.setxSpot(tempx);
            // eee.setySpot(tempy);
            // // eee.setY1Spot(l.getY1Spot());
            // eee.setId(l.getId());
            // eee.setMessage(l.getMessage());
            // eee.setMoviePath(l.getMoviePath());
            // eee.setPictruePath(l.getPictruePath());
            // eee.setPlaceId(l.getPlaceId());
            // eee.setPlace(l.getPlace());
            // eee.setShopName(l.getShopName());
            // outList2.add(eee);
            //
            //
            // }
            for (MessageModel l : msgList1)
            {
                BigDecimal x = new BigDecimal(0);
                BigDecimal x1 = new BigDecimal(0);
                BigDecimal y = new BigDecimal(0);
                BigDecimal y1 = new BigDecimal(0);
                BigDecimal tempx = new BigDecimal(0);
                BigDecimal tempy = new BigDecimal(0);
                x = l.getxSpot();
                x1 = l.getX1Spot();
                y = l.getySpot();
                y1 = l.getY1Spot();
                tempx = (x1.add(x)).divide(fina, 2);
                tempy = (y1.add(y)).divide(fina, 2);
                mmm = new MsgMngModel();
                mmm.setxSpot(tempx);
                mmm.setySpot(tempy);
                mmm.setPlace(l.getPlace());
                mmm.setFloor(l.getFloor());
                mmm.setRangeSpot(l.getRangeSpot());
                mmm.setShopName(l.getShopName());
                mmm.setMessage(l.getMessage());
                mmm.setTimeInterval(l.getTimeInterval());
                mmm.setIsEnable(l.getIsEnable());
                mmm.setPictruePath(l.getPictruePath());
                mmm.setMoviePath(l.getMoviePath());
                mmm.setId(l.getId());
                mmm.setShopId(l.getShopId());
                if (l.getTicketPath() != null)
                {
                    if (l.getTicketPath().length() > 6)
                    {
                        ticketList.add(l.getTicketPath());
                    }
                }
//                areaIdList.add(l.getShopId());
                outList1.add(mmm);
            }
            len = ticketList.size();
            if (len > 0)
            {
                Random rand = new Random();
                int randNum = rand.nextInt(len);
                tikectPath = ticketList.get(randNum);
            }
            modelMap.put("tikectPath", tikectPath);
            modelMap.put("error", null);
            modelMap.put("data", loc);
            modelMap.put("message", outList1);
            modelMap.put("message1", outList2);
            modelMap.put("paramUpdateTime", paramUpdate);
            log.debug("return location:" + loc.getX() + "," + loc.getY());
        }
        modelMap.put("geofencing", list2);
        log.debug("return ok!");
        return modelMap;
    }

    @RequestMapping(value = "/getTikectData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getTikectData(
            @RequestBody ApiRequestModel requestModel)
    {

        if (StringUtils.isEmpty(requestModel.getIp()))
        {
            return null;
        }
        String userId = ConvertUtil.convertMacOrIp(requestModel.getIp());
        List<LocationModel> list = new ArrayList<LocationModel>(10);
        Collection<LocationModel> ResultList = new ArrayList<LocationModel>(10);
        ResultList = dao.queryLocationByUseId(userId);
        // 查询参数更新的时间
        List<String> ticketList = new ArrayList<String>();
        int len = 0;
        String tikectPath = null;
        for (LocationModel l : ResultList)
        {
            list.add(l);
        }

        Map<String, Object> modelMap = new HashMap<String, Object>(3);
        if (ResultList.size() >= 1)
        {
            LocationModel loc = list.get(0);
            Collection<MessageModel> msgList1 = new ArrayList<MessageModel>(10);
            msgList1 = daoMsg.queryByLocation3(loc);
            for (MessageModel l : msgList1)
            {
                if (l.getTicketPath().length() > 6)
                {
                    ticketList.add(l.getTicketPath());
                }
            }
            len = ticketList.size();
            if (len > 0)
            {
                Random rand = new Random();
                int randNum = rand.nextInt(len);
                tikectPath = ticketList.get(randNum);

            }
        }
        modelMap.put("tikectPath", tikectPath);
        return modelMap;

    }
 
    @RequestMapping(value = "/getNewTikectData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getNewTikectData(
            @RequestParam("messageId") String messageId
            )
    {

        List<Map<String, Object>> list = daoMsg.getTiketPathByMsgId(messageId);
        Map<String, Object> modelMap = new HashMap<String, Object>(3); 
        String tikePath = null;
        int size = list.size();
        int allSize = 0;
        int suiji = (int)(Math.random()*100+1);
        if (size>0)
        {
            for (int i = 0; i < list.size(); i++)
            {
                String ticke = list.get(i).get("ticketPath").toString();
                int chance = Integer.parseInt(list.get(i).get("chances").toString());
                if (i==0)
                {
                    allSize = allSize + chance;
                    if (suiji<=chance)
                    {
                        tikePath = ticke; 
                    }
                }else
                {
                    if (allSize<suiji&&suiji<=allSize+chance)
                    {
                        tikePath = ticke; 
                    }else
                    {
                        allSize = allSize + chance; 
                    }
                }
            }
            
        }
        modelMap.put("tikectPath",tikePath);
        return modelMap;

    }    
    

    @RequestMapping(value = "/getLocationData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getLocationData(@RequestParam("ip") String ip,
            @RequestParam("password") String password)
    {

        log.debug("api getData.ip:" + ip);

        if (StringUtils.isEmpty(ip))
        {
            return null;
        }
        List<LocationModel> list = new ArrayList<LocationModel>(10);
        Collection<LocationModel> ResultList = new ArrayList<LocationModel>(10);
        ResultList = dao.queryLocationByUseId(ip);
        for (LocationModel l : ResultList)
        {
            list.add(l);
        }

        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        if (ResultList.size() >= 1)
        {
            LocationModel loc = list.get(0);
            modelMap.put("error", null);
            modelMap.put("data", loc);

        }
        else
        {
            modelMap.put("data", null);
        }

        return modelMap;
    }

    @RequestMapping(value = "/subscription", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> subscription(
            @RequestParam("storeId") String storeId,
            @RequestParam("ip") String ip,
            @RequestParam("mapId") String mapIds)
    {
        Collection<SvaModel> svaList = svaDao.queryByStoreId(storeId);
        String token = null;
        HttpUtil capi = null;
        boolean result = true;
        String jsonStr = null;
        String charset = null;
        String content = null;
        String url = null;
        String mapId = mapIds;
        try
        {
            for (SvaModel sva : svaList)
            {

                url = "https://" + sva.getIp() + ':' + sva.getTokenProt()
                        + "/v3/auth/tokens";
                content = "{\"auth\":{\"identity\":{\"methods\":[\"password\"],\"password\": {\"user\": {\"domain\": \"Api\",\"name\": \""
                        + sva.getUsername()
                        + "\",\"password\": \""
                        + sva.getPassword() + "\"}}}}}";
                charset = "UTF-8";
                log.debug("from ip:" + ip + ",getToken url:" + url);
                capi = new HttpUtil();

                token = capi.httpsPost(url, content, charset);
                // subscribe url:
                // https://sva_server_ip:9001/enabler/catalog/locationstreamreg/json/v1.0
                // anoymous subscribe url:
                // https://sva_server_ip:9001/enabler/catalog/locationstreamanonymousreg/json/v1.0
                if (!"null".equals(mapId)) {
                    url = "https://" + sva.getIp() + ':' + sva.getTokenProt()
                    + "/enabler/catalog/maptransformer/json/v1.0";
                    content = "{\"APPID\":\"" + sva.getUsername()
                    + "\",\"idType\":\"" + sva.getIdType()
                    + "\",\"useridlist\":[\""
                    + ConvertUtil.convertMacOrIp(ip.trim()) 
                    + "\"],\"maplist\":[\""+mapId+"\"]}";
                }else
                {
                    url = "https://" + sva.getIp() + ':' + sva.getTokenProt()
                    + "/enabler/catalog/locationstreamreg/json/v1.0";
                    content = "{\"APPID\":\"" + sva.getUsername()
                    + "\",\"idType\":\"" + sva.getIdType()
                    + "\",\"useridlist\":[\""
                    + ConvertUtil.convertMacOrIp(ip.trim()) + "\"]}";
                }
                log.debug("from ip:" + ip + ",subscription url:" + url
                        + " content:" + content);
                jsonStr = capi.subscription(url, content, token, "POST");
                log.debug("subscription:" + jsonStr);
            }
        }
        catch (KeyManagementException e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("KeyManagementException.", e);
        }
        catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("NoSuchAlgorithmException.", e);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("IOException.", e);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("Exception.", e);
        }

        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", null);
        modelMap.put("data", result);
        return modelMap;
    }

    @RequestMapping(value = "/subscribePrru", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> subscribePrru(
            @RequestParam("storeId") String storeId,
            @RequestParam("ip") String ip)
    {
        Collection<SvaModel> svaList = svaDao.queryByStoreId(storeId);
        String token = null;
        HttpUtil capi = null;
        boolean result = true;
        String jsonStr = null;
        String charset = null;
        String url = null;
        String content = null;
        try
        {
            for (SvaModel sva : svaList)
            {

                url = "https://" + sva.getIp() + ':' + sva.getTokenProt()
                        + "/v3/auth/tokens";
                content = "{\"auth\":{\"identity\":{\"methods\":[\"password\"],\"password\": {\"user\": {\"domain\": \"Api\",\"name\": \""
                        + sva.getUsername()
                        + "\",\"password\": \""
                        + sva.getPassword() + "\"}}}}}";
                charset = "UTF-8";
                log.debug("from ip:" + ip + ",getToken url:" + url);
                capi = new HttpUtil();

                token = capi.httpsPost(url, content, charset);
                url = "https://" + sva.getIp() + ':' + sva.getTokenProt()
                        + "/enabler/catalog/networkinfo/json/v1.0";
                content = "{\"APPID\":\"" + sva.getUsername()
                        + "\",\"infotype\":\"ransignal\",\"useridlist\":[\""
                        + ConvertUtil.convertMacOrIp(ip) + "\"]}";
                log.debug("from ip:" + ip + ",subscription url:" + url
                        + " content:" + content);
                jsonStr = capi.subscription(url, content, token, "POST");
                log.debug("subscription:" + jsonStr);
            }
        }
        catch (KeyManagementException e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("KeyManagementException.", e);
        }
        catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("NoSuchAlgorithmException.", e);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("IOException.", e);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("Exception.", e);
        }

        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", null);
        modelMap.put("data", result);
        return modelMap;
    }
    
    @RequestMapping(value = "/unSubscribePrru", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> unSubscribePrru(
            @RequestParam("storeId") String storeId,
            @RequestParam("ip") String ip)
    {
        Collection<SvaModel> svaList = svaDao.queryByStoreId(storeId);
        String token = null;
        HttpUtil capi = null;
        boolean result = true;
        String jsonStr = null;
        String charset = null;
        String url = null;
        String content = null;
        try
        {
            for (SvaModel sva : svaList)
            {
                url = "https://" + sva.getIp() + ":"
                        + sva.getTokenProt() + "/v3/auth/tokens";
                content = "{\"auth\":{\"identity\":{\"methods\":[\"password\"],\"password\": {\"user\": {\"domain\": \"Api\",\"name\": \""
                        + sva.getUsername()
                        + "\",\"password\": \""
                        + sva.getPassword() + "\"}}}}}";
                charset = "UTF-8";
                log.debug("unSubscribPrru--from ip:" + ip + ",getToken url:" + url);
                capi = new HttpUtil();
                token = capi.httpsPost(url, content, charset);
                
                url = "https://" + sva.getIp() + ":" + sva.getTokenProt()
                        + "/enabler/catalog/networkinfounreg/json/v1.0";
                content = "{\"APPID\":\"" + sva.getUsername()
                + "\",\"infotype\":\"ransignal\",\"useridlist\":[\""
                + ConvertUtil.convertMacOrIp(ip) + "\"]}";
                log.debug("unSubscribPrru--subscription url:" + url
                        + " content:" + content);
                
                jsonStr = capi.subscription(url, content, token, "DELETE");
                log.debug("unsubscribePrru,jsonStr:" + jsonStr);
            }
        }
        catch (KeyManagementException e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("KeyManagementException.", e);
        }
        catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("NoSuchAlgorithmException.", e);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("IOException.", e);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("Exception.", e);
        }

        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", null);
        modelMap.put("data", result);
        return modelMap;
    }
    
    @RequestMapping(value = "/unSubscribePrruAll", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> unSubscribePrruAll(
            @RequestParam("storeId") String storeId)
    {
        Collection<SvaModel> svaList = svaDao.queryByStoreId(storeId);
        String token = null;
        HttpUtil capi = null;
        boolean result = true;
        String jsonStr = null;
        String charset = null;
        String url = null;
        String content = null;
        try
        {
            for (SvaModel sva : svaList)
            {
                url = "https://" + sva.getIp() + ":"
                        + sva.getTokenProt() + "/v3/auth/tokens";
                content = "{\"auth\":{\"identity\":{\"methods\":[\"password\"],\"password\": {\"user\": {\"domain\": \"Api\",\"name\": \""
                        + sva.getUsername()
                        + "\",\"password\": \""
                        + sva.getPassword() + "\"}}}}}";
                charset = "UTF-8";
                log.debug("unSubscribPrruAll--getToken url:" + url);
                capi = new HttpUtil();
                token = capi.httpsPost(url, content, charset);
                
                url = "https://" + sva.getIp() + ":" + sva.getTokenProt()
                        + "/enabler/catalog/networkinfounreg/json/v1.0";
                content = "{\"APPID\":\"" + sva.getUsername()
                + "\",\"infotype\":\"ransignal\",\"useridlist\":[]}";
                log.debug("unSubscribPrruAll--subscription url:" + url
                        + " content:" + content);
                
                jsonStr = capi.subscription(url, content, token, "DELETE");
                log.debug("unsubscribePrruAll,jsonStr:" + jsonStr);
            }
        }
        catch (KeyManagementException e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("KeyManagementException.", e);
        }
        catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("NoSuchAlgorithmException.", e);
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("IOException.", e);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            result = false;
            log.error("Exception.", e);
        }

        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", null);
        modelMap.put("data", result);
        return modelMap;
    }

    @RequestMapping(value = "/getMapDataByIp")
    @ResponseBody
    public Map<String, Object> getMapDataByIp(
            @RequestBody ApiRequestModel requestModel)
    {
        System.out.println("getMapDataByIp");
        log.debug("getMapDataByIp");
        Thread desktopServerThread = new Thread(new TCPDesktopServer());
        desktopServerThread.start();
        // String ip = requestModel.getIp();
        // String token = null;
        // Collection<SvaModel> svaList = svaDao.queryActive();
        // HttpUtil capi = null;
        // try
        // {
        // for (SvaModel sva : svaList)
        // {
        //
        // String url = "https://" + sva.getIp() +
        // ":"+sva.getTokenProt()+"/v3/auth/tokens";
        // String content =
        // "{\"auth\":{\"identity\":{\"methods\":[\"password\"],\"password\": {\"user\": {\"domain\": \"Api\",\"name\": \""
        // + sva.getUsername() + "\",\"password\": \"" + sva.getPassword() +
        // "\"}}}}}";
        // String charset = "UTF-8";
        // log.debug("from ip:" + ip + ",getToken url:" + url);
        // capi = new HttpUtil();
        //
        // token = capi.httpsPost(url, content, charset);
        // // subscribe url:
        // //
        // https://sva_server_ip:9001/enabler/catalog/locationstreamreg/json/v1.0
        // // anoymous subscribe url:
        // //
        // https://sva_server_ip:9001/enabler/catalog/locationstreamanonymousreg/json/v1.0
        // url = "https://" + sva.getIp() +
        // ":"+sva.getTokenProt()+"/enabler/catalog/locationstreamreg/json/v1.0";
        // content =
        // "{\"APPID\":\"" + sva.getUsername() + "\",\"useridlist\":[\"" +
        // ConvertUtil.convertMacOrIp(ip)
        // + "\"]}";
        // log.debug("from ip:" + ip + ",subscription url:" + url + " content:"
        // + content);
        // String jsonStr = capi.subscription(url, content, token);
        // log.debug("subscription:" + jsonStr);
        // }
        // }
        // catch (KeyManagementException e)
        // {
        // // TODO Auto-generated catch block
        // log.error("KeyManagementException.", e);
        // }
        // catch (NoSuchAlgorithmException e)
        // {
        // // TODO Auto-generated catch block
        // log.error("NoSuchAlgorithmException.", e);
        // }
        // catch (IOException e)
        // {
        // // TODO Auto-generated catch block
        // log.error("IOException.", e);
        // }
        // catch (Exception e)
        // {
        // // TODO Auto-generated catch block
        // log.error("Exception.", e);
        // }
        // MapMngModel mmm = null;
        // for (MapsModel l : ResultList)
        // {
        // mmm = new MapMngModel();
        // mmm.setFloor(l.getFloor());
        // mmm.setX(l.getXo());
        // mmm.setY(l.getYo());
        // mmm.setScale(l.getScale());
        // mmm.setPath(l.getPath());
        // mmm.setFloorNo(l.getFloorNo());
        // mmm.setPlace(l.getPlace());
        // mmm.setAngle(l.getAngle());
        // mmm.setCoordinate(l.getCoordinate());
        // mmm.setImgHeight(l.getImgHeight());
        // mmm.setImgWidth(l.getImgWidth());
        // list.add(mmm);
        // }

        Collection<MapsModel> ResultList = null;
        Collection<AreaModel> areaList = daoArea.doquery();
        ResultList = daoMaps.doquery();
        Map<String, Object> modelMap = new HashMap<String, Object>(2);

        modelMap.put("error", null);
        modelMap.put("data", ResultList);
        modelMap.put("areaData", areaList);

        return modelMap;
    }
    
    /** 
     * @Title: getMessages 
     * @Description: TODO 同通过店铺Id获取店铺范围的消息推送以及电子围栏消息
     * @param areaId 店铺Id
     * @return Map<String,Object>       
     * @throws 
     */
    @RequestMapping(value = "/getMessages", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getMessages(@RequestBody HashMap<String,List<String>> map)
    {
        Collection<MessageModel> messageList = new ArrayList<MessageModel>(10);
        Collection<GeofencingModel> GeofencingList = new ArrayList<GeofencingModel>(10);
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        List<String> msglists = new ArrayList<>();
        Collection<TicketModel> con = null;
        try
        {
            List<String> areaList = map.get("areaId");
            if (areaList.size()>0)
            {
                
                for (int i = 0; i < areaList.size(); i++)
                {
                    String areaId = areaList.get(i);
                    messageList.addAll(daoMsg.getAllMessageDataByAreaIdNew(areaId));
                    msglists.addAll(daoMsg.getMsgId(areaId));
                    String zoneId = daoArea.getZoneIdByAreaId(areaId);
                    log.debug("getMessage: areaId:"+areaId+" zoneId"+zoneId);
                    GeofencingList.addAll(geofencingDao.getGeofencingByZoneId(zoneId)); 
                }
            }
            
        }
        catch (Exception e)
        {
            log.error("getMessage error:"+e.getMessage());
        }
        modelMap.put("message", messageList);
        modelMap.put("geoFence", GeofencingList);
        return modelMap;
    }    

    @RequestMapping(value = "/getMapData", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getMapData(String model)
    {
        List<MapMngModel> list = new ArrayList<MapMngModel>(10);
        Collection<MapsModel> ResultList = new ArrayList<MapsModel>(10);
        MapMngModel mmm = null;
        for (MapsModel l : ResultList)
        {
            mmm = new MapMngModel();
            mmm.setFloor(l.getFloor());
            mmm.setX(l.getXo());
            mmm.setY(l.getYo());
            mmm.setScale(l.getScale());
            mmm.setPath(l.getPath());
            mmm.setSvg(l.getSvg());
            list.add(mmm);
        }
        Map<String, Object> modelMap = new HashMap<String, Object>(2);

        modelMap.put("error", null);
        modelMap.put("data", list);
        log.debug("getMapData end");
        return modelMap;
    }

    @RequestMapping(value = "/checkCode", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> checkCode(@RequestParam("name") String name,
            @RequestParam("password") String password)
    {
        int a = 0;
        Collection<CodeModel> model = daoCode.getData();
        boolean result = false;
        int flag = daoCode.checkIsValid(name, password);
        if (flag > a)
        {
            for (CodeModel m : model)
            {
                if (m.getName().equals(name)
                        && m.getPassword().equals(password))
                {
                    result = true;
                }
            }
        }

        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", null);
        modelMap.put("data", result);
        return modelMap;
    }

    @RequestMapping(value = "/saveTestData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> saveTestData(@RequestBody AccuracyApiModel aam)
    {

        log.debug("api saveTestData.offset:" + aam.getOffset());
        int result = 0;
        String err = null;
        try
        {
            result = daoAccuracy.saveTestInfo(aam);
        }
        catch (Exception e)
        {
            err = e.toString();
        }
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", err);
        modelMap.put("data", result);
        return modelMap;
    }

    // 静态精度测试接口
    @RequestMapping(value = "/staticSaveTestData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> staticSaveTestData(
            @RequestBody StaticAccuracyApiModel aam)
    {

        log.debug("api saveTestData.getAvgeOffset:" + aam.getAvgeOffset());
        int result = 0;
        String err = null;
        try
        {
            result = staticAccuracyDao.staticSaveTestInfo(aam);
        }
        catch (Exception e)
        {
            err = e.toString();
        }
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", err);
        modelMap.put("data", result);
        return modelMap;
    }

    // 动态精度测试接口
    @RequestMapping(value = "/dynamicSaveTestData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> dynamicSaveTestData(
            @RequestBody DynamicAccuracyApiModel aam)
    {

        log.debug("api saveTestData.getAvgeOffset:" + aam.getAvgeOffset());
        int result = 0;
        String err = null;
        try
        {
            result = dynamicAccuracyDao.dynamicSaveTestInfo(aam);
        }
        catch (Exception e)
        {
            err = e.toString();
        }
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", err);
        modelMap.put("data", result);
        return modelMap;
    }

    @RequestMapping(value = "/getSellerInfo", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getSellerInfo(
            @RequestParam("floorNo") String floorNo)
    {
        Collection<SellerModel> ResultList = new ArrayList<SellerModel>(10);
        ResultList = daoSeller.getInfoByFloorNo(floorNo);
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", null);
        modelMap.put("data", ResultList);
        return modelMap;
    }

    @RequestMapping(value = "/getVipSellerInfo", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getVipSellerInfo(
            @RequestParam("floorNo") String floorNo)
    {
        Collection<SellerModel> ResultList = new ArrayList<SellerModel>(10);
        ResultList = daoSeller.getVipByFloorNo(floorNo);
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", null);
        modelMap.put("data", ResultList);
        return modelMap;
    }

    @RequestMapping(value = "/pingSVA", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> pingSVA(@RequestParam("ip") String ip,
            @RequestParam("pingnumber") int pingnumber,
            @RequestParam("packtsize") int packtsize,
            @RequestParam("timeout") int timeout)
    {
        // 返回值
        Map<String, Object> result = new HashMap<String, Object>(2);
        // 获取sva的ip地址
        // InputStream in = getClass().getClassLoader().getResourceAsStream(
        // "sva.properties");
        // Properties properties = new Properties();
        // try
        // {
        // properties.load(in);
        // }
        // catch (IOException e)
        // {
        // // TODO Auto-generated catch block
        // log.error("load properties ERROR.", e);
        // result.put("error", true);
        // result.put("data", e.toString());
        // return result;
        // }
        // String ip = properties.getProperty("sva.ip");
        log.info("ip:" + ip);
        log.info("pingnumber" + pingnumber);
        log.info("packtsize:" + packtsize);
        log.info("timeout:" + timeout);

        result = Util.ping(ip, pingnumber, packtsize, timeout);

        return result;
    }

    @RequestMapping(value = "/getEstimate", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getEstimate(
            @RequestParam("floorNo") String floorNo)
    {
        log.info("floorNo:" + floorNo);
        // 返回值
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        BigDecimal result = daoEstimate.getEstimate(floorNo);
        modelMap.put("error", null);
        modelMap.put("data", result);
        return modelMap;
    }

    @RequestMapping(value = "/getPrruInfo", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getPrruInfo(
            @RequestParam("floorNo") String floorNo)
    {
        Collection<PrruModel> ResultList = new ArrayList<PrruModel>(10);
        ResultList = prruDao.getPrruInfoByflooNo(floorNo,"","");
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", null);
        modelMap.put("data", ResultList);
        return modelMap;
    }

    @RequestMapping(value = "/getPrruSignal")
    @ResponseBody
    public Map<String, Object> getPrruSignal( @RequestParam("ip") String ip)
    {
        List<Map<String, Object>> svaList = null;
        long time = System.currentTimeMillis()-60000;
        log.debug("getPrruSignal ip:"+ip);
        if (ip==null) {
            return null;
        }else
        {
           String userId = ConvertUtil.convertMacOrIp(ip);
           svaList = prruDao.getSignal(time,userId);
            
        }
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", null);
        modelMap.put("data", svaList);

        return modelMap;
    }

    @RequestMapping(value = "/getLineDataByHour", method = {RequestMethod.GET})
    @ResponseBody
    public JSONPObject getLineDataByHour(String callbackparam)
    {
        log.info("getLineDataByHour");
        // 返回值
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        List<Map<String, Object>> res = comDao.getDataToday();
        modelMap.put("error", null);
        modelMap.put("data", res);
        return new JSONPObject(callbackparam, modelMap);
    }

    @RequestMapping(value = "/getPieData", method = {RequestMethod.GET})
    @ResponseBody
    public JSONPObject getPieData(String callbackparam)
    {
        log.info("getPieData");
        // 返回值
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        List<Map<String, Object>> res = comDao.getStatisticTemp();
        modelMap.put("error", null);
        modelMap.put("data", res);
        return new JSONPObject(callbackparam, modelMap);
    }

    @RequestMapping(value = "/getDataParam", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getTableDataParam(String callbackparam)
    {
        log.info("ParamController:getTableData");
        Collection<ParamModel> ResultList = daoParam.doquery();
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", null);
        modelMap.put("data", ResultList);
        return modelMap;
    }

    @RequestMapping(value = "/savaMessageData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> savaMessageData(
            @RequestBody MessagePush messagePush)
    {

        log.debug("api savaMessageData:");
        int result = 0;
        String err = null;
        messagePush.setUpdateTime(new Date().getTime());
        try
        {
            result = messagePushDao.savaMessagePush(messagePush);
        }
        catch (Exception e)
        {
            err = e.toString();
        }
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", err);
        modelMap.put("data", result);
        return modelMap;
    }

    @RequestMapping(value = "/savaLocationDelay", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> savaLocationDelay(
            @RequestBody LocationDelay locationDelay)
    {

        log.debug("api savaLocationDelay:");
        int result = 0;
        String err = null;
        locationDelay.setUpdateTime(new Date().getTime());
        try
        {
            result = delayDao.savaMessagePush(locationDelay);
        }
        catch (Exception e)
        {
            err = e.toString();
        }
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", err);
        modelMap.put("data", result);
        return modelMap;
    }

    // 获取所有的消息
    @RequestMapping(value = "/getAllMessageData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getAllMessageData()
    {

        Map<String, Object> modelMap = new HashMap<String, Object>(3);
        Collection<MessageModel> ResultList = new ArrayList<MessageModel>(10);
        ResultList = daoMsg.getAllMessageData();
        modelMap.put("data", ResultList);

        return modelMap;
    }

    @RequestMapping(value = "/savePhone", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> savePhone(@RequestBody PhoneModel model)
    {

        log.debug("api savePhone:");
        long time = System.currentTimeMillis();
        model.setTimestamp(time);
        try
        {
            phoneDao.savePhone(model);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
        }
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", null);
        return modelMap;
    }

    // 注册
    @RequestMapping(value = "/saveRegister", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> savaRegister(@RequestBody RegisterModel model)
    {

        String phoneNumber = model.getPhoneNumber();
        List<RegisterModel> lis = registerDao.getDataByPhoneNumber(phoneNumber); 
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        if (lis.size() > 0 )
        {
            modelMap.put("error", "0");
            return modelMap;
        }
        log.debug("api savaRegister:");
        long time = System.currentTimeMillis();
        model.setStatus(0);
        model.setTimes(time);
        try
        {
        	model.setUserId(ConvertUtil.convertMacOrIp(model.getUserId()));
            registerDao.saveRegister(model);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
        }

        modelMap.put("error", null);
        return modelMap;
    }
    
    @RequestMapping(value = "/refreshRegister", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> refreshRegister(@RequestBody RegisterModel model)
    {

        log.debug("api refreshRegister:");
        long time = System.currentTimeMillis();
        model.setTimes(time);
        try
        {
            registerDao.refreshRegister(model);
        }
        catch (Exception e)
        {
            log.error(e.getMessage());
        }
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        modelMap.put("error", null);
        return modelMap;
    }

    // 登陆验证
    @RequestMapping(value = "/loginCheck", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> loginCheck(@RequestBody RegisterModel model)
    {

        String baseChart = "abcdefghijklmnopqrstuvwxyz0123456789";
        int baseSize = baseChart.length();
        StringBuffer strb = new StringBuffer();
        Random ron = new Random();
        for (int i = 0; i < 10; i++)
        {
            int val = ron.nextInt(baseSize);
            strb.append(baseChart.charAt(val));
        }
        log.debug("api loginCheck:");
        // int a = registerDao.checkLogin(userName, passWord);
        int b = registerDao.checkLogin1(model);
        log.debug("passWord:" + model.getPassWord() + " phoneNumber:"
                + model.getPhoneNumber() + " b:" + b +" token:"+strb);
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        if (b > 0)
        {
            registerDao.setLoginStatus(strb, model.getPhoneNumber());
            modelMap.put("loginStatus", strb);
            modelMap.put("error", "1");
            return modelMap;
        }
        else
        {
            modelMap.put("error", "0");
            return modelMap;
        }

    }

    // 请求找人
    @RequestMapping(value = "/seekPeople ", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> seekPeople(@RequestBody MyModel model)
    {
        String myPhone = model.getMyPhone();
        String otherPhone = model.getOtherPhone();
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        log.debug("api seekPeople:" + " myPhone:" + myPhone + " otherPhone:"
                + otherPhone);
        List<RegisterModel> lis = registerDao.getDataByPhoneNumber(otherPhone);
        // List<RegisterModel> lis3 = registerDao
        // .getDataByPhoneNumber3(otherPhone);
        List<RegisterModel> lis1 = null;
        List<RegisterModel> lis2 = null;
        List<RegisterModel> lis3 = null;
        List<RegisterModel> lis4 = null;
        List<RegisterModel> lis5 = null;
        lis3 = registerDao.getDataByStatus2(otherPhone);
        if (lis3.size() > 0)
        {
            // registerDao.updataStatus1(otherPhone);
            modelMap.put("error", "4");
            return modelMap;
        }
        if (lis.size() > 0)
        {
            log.debug("gai bian zhuang tai 1");
            // registerDao.updataStatus(myPhone, otherPhone);
            // 线程没两秒查询一次是否接受
            for (int i = 0; i < 15; i++)
            {
                try
                {
                    lis1 = registerDao.getDataByIsTrue(otherPhone);
                    lis2 = registerDao.getDataByIsTrue1(otherPhone);
                    // lis3 = registerDao.getDataByStatus2(otherPhone);
                    lis4 = registerDao.getDataByStatus2(otherPhone);
                    lis5 = registerDao.getDataByStatus3(otherPhone);
                    if (lis4.size() > 0)
                    {
                        // registerDao.updataStatus1(otherPhone);
                        modelMap.put("error", "4");
                        return modelMap;
                    }
                    if (lis1.size() > 0)
                    {
                        log.debug("error1");
                        // Thread.sleep(1000);
                        // registerDao.updateIsTrue1(otherPhone);
                        // registerDao.updateIsTrue1(myPhone);
                        // registerDao.updateIsTrue1(otherPhone);
                        // registerDao.updataStatus1(myPhone);
                        // registerDao.updataStatus1(otherPhone);
                        modelMap.put("error", "1");
                        return modelMap;
                    }
                    if (lis2.size() > 0)
                    {
                        log.debug("error3");
                        // Thread.sleep(1000);
                        // registerDao.updateIsTrue1(myPhone);
                        // registerDao.updateIsTrue1(otherPhone);
                        // registerDao.updateIsTrue1(otherPhone);
                        // registerDao.updataStatus1(myPhone);
                        // registerDao.updataStatus1(otherPhone);
                        modelMap.put("error", "3");
                        // System.out.println("erro1");
                        return modelMap;
                    }
                    if (lis5.size() > 0)
                    {
                        // registerDao.updataStatus1(otherPhone);
                        modelMap.put("error", "2");
                        return modelMap;
                    }
                    // if (lis3.size() > 0)
                    // {
                    // // registerDao.updataStatus1(myPhone);
                    // registerDao.updataStatus1(otherPhone);
                    // modelMap.put("error", "4");
                    // return modelMap;
                    // }
                    log.debug("seekPeople wait");
                    Thread.sleep(1000);

                }
                catch (Exception e)
                {
                    log.debug("seekPeople error");
                }
            }
            // registerDao.updateIsTrue1(myPhone);
            // registerDao.updateIsTrue1(otherPhone);
            // registerDao.updataStatus1(myPhone);
            // registerDao.updataStatus1(otherPhone);
            log.debug("seekPeople wei xiang ying!");
            modelMap.put("error", "2");
            return modelMap;
        }
        else
        {
            // registerDao.updateIsTrue1(myPhone);
            // registerDao.updateIsTrue1(otherPhone);
            // registerDao.updataStatus1(myPhone);
            // registerDao.updataStatus1(otherPhone);
            modelMap.put("error", "0");
            return modelMap;
        }

    }

    /** 
     * @Title: requestAnyTime 
     * @Description: 服务器推送，是否被找/是否有服务器推送
     * @param model
     * @return Map<String,Object>   
     * @throws 
     */
    @RequestMapping(value = "/requestAnyTime", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> requestAnyTime(@RequestBody MyModel model)
    {
        Map<String, Object> modelMap = new HashMap<String, Object>(2);

        // 服务器推送消息
    	List<String> msgList = new ArrayList<String>();
    	String userid = ConvertUtil.convertMacOrIp(model.getUserId());
        List<Map<String, Object>> messages = pushMsgDao.getMessageByUserId(userid);
        try{
	        for(Map<String, Object> m : messages){
	        	// 将取过的推送消息删除，避免二次推送
	        	pushMsgDao.deleteMessageById(m.get("id").toString());
	        	// 存在多个消息的情况做拼接处理
	        	msgList.add(m.get("content").toString());
	        }
        }catch(Exception e){
        	log.error(e.getMessage());
        }        
        modelMap.put("pushMsg", msgList);
        
        // 是否被别人找    
        String myPhone = model.getMyPhone();
        log.debug("api seekPeople:" + " myPhone:" + myPhone);
        
        List<RegisterModel> lis = registerDao.getDataByPhoneNumber(myPhone);
        if (lis.size()>0)
        {
        	RegisterModel userInfo = lis.get(0);
        	String loginStatus = userInfo.getLoginStatus();
        	int status = userInfo.getStatus();
        	switch(status){
        	case 1:		// 有人找，返回找人电话号码
        		modelMap.put("error", userInfo.getOtherPhone());
        		break;
        	case 2:		// 对方取消
        		modelMap.put("error", "1");
        		break;
        	default:
        		modelMap.put("error", "0");
        		break;
        	}
            modelMap.put("loginStatus",loginStatus);
        }else{
        	modelMap.put("error", "0");
        }
        
        return modelMap;

    }

    @RequestMapping(value = "/updateStatus", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> updateStatus(@RequestBody MyModel model)
    {
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        log.debug("updateStatus :");
        // String myPhone = model.getMyPhone();
        String otherPhone = model.getOtherPhone();
        // registerDao.updateIsTrue1(myPhone);
        registerDao.updateIsTrue1(otherPhone);
        // registerDao.updataStatus1(otherPhone);
        modelMap.put("error", "1");
        return modelMap;

    }

    @RequestMapping(value = "/updateStatus1", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> updateStatus1(@RequestBody MyModel model)
    {
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        log.debug("updateStatus1 :");
        // String myPhone = model.getMyPhone();
        String myPhone = model.getMyPhone();
        // registerDao.updateIsTrue1(myPhone);
        // registerDao.updateIsTrue1(otherPhone);
        registerDao.updataStatus1(myPhone);
        modelMap.put("error", "1");
        return modelMap;

    }
    
    @RequestMapping(value = "/updateStatus4", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> updateStatus4(@RequestBody MyModel model)
    {
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        log.debug("updateStatus1 :");
        // String myPhone = model.getMyPhone();
        String otherPhone = model.getOtherPhone();
        // registerDao.updateIsTrue1(myPhone);
        // registerDao.updateIsTrue1(otherPhone);
        registerDao.updataStatus1(otherPhone);
        modelMap.put("error", "1");
        return modelMap;

    }

    @RequestMapping(value = "/updateStatus2", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> updateStatus2(@RequestBody MyModel model)
    {
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        log.debug("updateStatus2 :");
        // String myPhone = model.getMyPhone();
        String myPhone = model.getMyPhone();
        // registerDao.updateIsTrue1(myPhone);
        // registerDao.updateIsTrue1(otherPhone);
        registerDao.updataStatus2(myPhone);
        modelMap.put("error", "1");
        return modelMap;

    }

    @RequestMapping(value = "/updateStatus3", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> updateStatus3(@RequestBody MyModel model)
    {
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        log.debug("updateStatus3 :");
        String myPhone = model.getMyPhone();
        String otherPhone = model.getOtherPhone();
        registerDao.updataStatus(myPhone, otherPhone);
        modelMap.put("error", "1");
        return modelMap;
        // registerDao.updateIsTrue1(myPhone);
        // registerDao.updateIsTrue1(otherPhone);
        // registerDao.updataStatus2(myPhone);

    }

    // 返回是否接受
    @RequestMapping(value = "/returnResult", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> returnResult(@RequestBody MyModel model)
    {
        String myPhone = model.getMyPhone();
        String result = model.getResult();
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        log.debug("api returnResult:" + " result:" + result + " myPhone:"
                + myPhone);
        List<String> lis = registerDao.getStatusByphoneNumber2(myPhone);
        if (lis.size() > 0)
        {
            modelMap.put("data", "2");
            registerDao.updataStatus1(myPhone);
            return modelMap;
        }
        if (result.equals("1"))
        {
            registerDao.updateIsTrue(myPhone);
            registerDao.updataStatus1(myPhone);
            modelMap.put("data", "1");
            return modelMap;
        }
        if (result.equals("0"))
        {
            registerDao.updateIsTrue2(myPhone);
            registerDao.updataStatus1(myPhone);
            // try
            // {
            // Thread.sleep(3000);
            // registerDao.updateIsTrue1(myPhone);
            // }
            // catch (InterruptedException e)
            // {
            // log.debug("sleep 3s");
            // }
            modelMap.put("data", "0");
            return modelMap;
        }
        modelMap.put("data", "0");
        return modelMap;

        // List<String> list = registerDao.getIpByUserName(userName);
        // String userID = null;
        // for (int i = 0; i < list.size(); i++)
        // {
        // userID = ConvertUtil.convertMacOrIp(list.get(i));
        // }
        // Collection<LocationModel> lis = locationDao.doquery1(userID,
        // floorNo);
        // LocationModel model = new LocationModel();
        // for (LocationModel l : lis)
        // {
        // model.setDataType(l.getDataType());
        // model.setIdType(l.getIdType());
        // model.setTimestamp(l.getTimestamp());
        // model.setX(l.getX());
        // model.setY(l.getY());
        // model.setZ(l.getZ());
        // model.setUserID(l.getUserID());
        //
        // }

    }

    // 找到人后返回结果
    @RequestMapping(value = "/cancalFind", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> cancalFind(@RequestBody MyModel model)
    {
        String otherPhone = model.getOtherPhone();
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        log.debug("api cancalFind:" + " otherPhone:" + otherPhone);
        List<String> lis = registerDao.getStatusByIsTrue2(otherPhone);
        List<String> lis1 = registerDao.getStatusByIsTrue1(otherPhone);
        if (lis.size() > 0)
        {
            modelMap.put("error", "0");
            registerDao.updataStatus1(otherPhone);
            registerDao.updateIsTrue1(otherPhone);
            return modelMap;
        }
        if (lis1.size() > 0)
        {
            modelMap.put("error", "1");
            registerDao.updataStatus1(otherPhone);
            registerDao.updateIsTrue1(otherPhone);
            return modelMap;
        }
        registerDao.updataStatusByCancel(otherPhone);
        // registerDao.updateIsTrue1(otherPhone);
        modelMap.put("error", null);
        return modelMap;
    }

    // 两个人实时坐标
    @RequestMapping(value = "/twoPeoPleData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> twoPeoPleData(@RequestBody MyModel model)
    {
        String floorNo = model.getFloorNo();
        String myPhone = model.getMyPhone();
        String otherPhone = model.getOtherPhone();
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        log.debug("api twoPeoPleData:" + " myPhone:" + myPhone + " otherPhone"
                + otherPhone);
        List<String> list = registerDao.getIpByUserName(myPhone);
        List<String> list1 = registerDao.getIpByUserName(otherPhone);
        String userID = null;
        String userID1 = null;
        for (int i = 0; i < list.size(); i++)
        {
            userID = list.get(i);
        }
        for (int i = 0; i < list1.size(); i++)
        {
            userID1 = list1.get(i);
        }
        log.debug("userID:" + userID + " userID1:" + userID1);
        Collection<LocationModel> lis = locationDao.doquery1(userID, floorNo);
        Collection<LocationModel> lis1 = locationDao.doquery1(userID1, floorNo);
        LocationModel model2 = new LocationModel();
        LocationModel model1 = new LocationModel();
        for (LocationModel l : lis)
        {
            model2.setDataType(l.getDataType());
            model2.setIdType(l.getIdType());
            model2.setTimestamp(l.getTimestamp());
            model2.setX(l.getX());
            model2.setY(l.getY());
            model2.setZ(l.getZ());
            model2.setUserID(l.getUserID());
            model2.setXo(l.getXo());
            model2.setYo(l.getYo());
            model2.setPath(l.getPath());
            model2.setUserID(l.getUserID());

        }
        for (LocationModel l : lis1)
        {
            model1.setDataType(l.getDataType());
            model1.setIdType(l.getIdType());
            model1.setTimestamp(l.getTimestamp());
            model1.setX(l.getX());
            model1.setY(l.getY());
            model1.setZ(l.getZ());
            model1.setUserID(l.getUserID());
            model1.setXo(l.getXo());
            model1.setYo(l.getYo());
            model1.setPath(l.getPath());
            model1.setUserID(l.getUserID());

        }

        // registerDao.updataStatus1(myPhone);
        // registerDao.updataStatus1(otherPhone);
        modelMap.put("myDate", model2);
        modelMap.put("otherDate", model1);
        return modelMap;

    }

    @RequestMapping(value = "/getShDate", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getShDate()
    {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        List<Map<String, Object>> bzData = bzDao.getAllFloorNo2("1");
        String floorNo1 = null;
        String floorNo2 = null;
        String floorNo3 = null;
        String periodSel = null;
        double coefficient = 0;
        long bztime = 0;
        String startTime = null;
        long time = 0;
        if (bzData.size() > 0)
        {
            floorNo1 = bzData.get(0).get("floorNo1").toString();
            floorNo2 = bzData.get(0).get("floorNo2").toString();
            floorNo3 = bzData.get(0).get("floorNo3").toString();
            startTime = bzData.get(0).get("startTime").toString();
            periodSel = bzData.get(0).get("periodSel").toString();
            coefficient = Double.parseDouble(bzData.get(0).get("coefficient")
                    .toString());
            startTime = startTime.split(" ")[1].substring(0, 8);
//            long maxTimestamp = daoArea.getMaxTimestamp();
//            if (maxTimestamp > 0)
//            {
//                time = maxTimestamp - Integer.parseInt(periodSel) * 60 * 1000;
//            }
//            else
//            {
//                time = System.currentTimeMillis() - Integer.parseInt(periodSel)
//                        * 60 * 1000;
//            }
        }
        if (coefficient == 0)
        {
            coefficient = 1.0;
        }

        List<AreaModel> ResultList1 = daoArea.selectAeareBaShow(floorNo1);
        List<AreaModel> ResultList2 = daoArea.selectAeareBaShow(floorNo2);
        List<AreaModel> ResultList3 = daoArea.selectAeareBaShow(floorNo3);
        long nowTime = System.currentTimeMillis()
                - (Integer.parseInt(periodSel)+1) * 60 * 1000;
        List<Object> areaData = new ArrayList<Object>();
        List<Object> areaData2 = new ArrayList<Object>();
        List<Object> areaData3 = new ArrayList<Object>();
        List<Object> areaData1 = null;
        Map<String, Object> map = null;
        Map<String, Object> allDataMap = new HashMap<String, Object>(2);

        String tableName = Params.LOCATION
                + ConvertUtil
                        .dateFormat(currentDate.getTime(), Params.YYYYMMDD);
        String visitDay = ConvertUtil.dateFormat(currentDate.getTime(),
                "yyyy-MM-dd");
        // 当前时间拼接
        if (startTime != null)
        {
            String startDate = visitDay + " " + startTime;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try
            {
                bztime = sdf.parse(startDate).getTime();
            }
            catch (Exception e)
            {
                log.debug("Time zhuanhuan error!");
            }
        }
        Map<String, Object> tquyu = null;
        double allTime1 = 0;
        double allTime2 = 0;
        double allTime3 = 0;
        long allTimes2 = 0; 
        long allTimes3 = 0; 
        for (int i = 0; i < ResultList1.size(); i++)
        {
            Map<String, Object> quyu1 = null;
            quyu1 = getAreaDate(areaData1, ResultList1.get(i).getId(),
                    ResultList1.get(i).getAreaName(), visitDay, tquyu, map,
                    nowTime, coefficient);
            allTime1 = allTime1 + Double.parseDouble((quyu1.get("average").toString()));
            if (quyu1.size() != 0)
            {
            	areaData.add(quyu1);
            }
        }
        for (int i = 0; i < ResultList2.size(); i++)
        {
            Map<String, Object> quyu2 = null;
            quyu2 = getAreaDate(areaData1, ResultList2.get(i).getId(),
                    ResultList2.get(i).getAreaName(), visitDay, tquyu, map,
                    nowTime, coefficient);
            allTime2 = allTime2 +  Double.parseDouble(quyu2.get("average").toString());
            allTimes2 = Long.parseLong(quyu2.get("allTime").toString())+allTimes2;
            if (quyu2.size() != 0)
            {
                areaData2.add(quyu2);
            }
        }
        for (int i = 0; i < ResultList3.size(); i++)
        {
            Map<String, Object> quyu3 = null;
            quyu3 = getAreaDate(areaData1, ResultList3.get(i).getId(),
                    ResultList3.get(i).getAreaName(), visitDay, tquyu, map,
                    nowTime, coefficient);
            allTime3 = allTime3 +  Double.parseDouble((quyu3.get("average").toString()));
            allTimes3 = Long.parseLong(quyu3.get("allTime").toString())+allTimes3;
            if (quyu3.size() != 0)
            {
                areaData3.add(quyu3);
            }
        }
    
        allDataMap.put("item", areaData);
        allDataMap.put("item1", areaData2);
        allDataMap.put("item2", areaData3);

        int allUsers1 = 0;
        int allUsers2 = 0;
        int allUsers3 = 0;
 
        int allLeiji1 = 0;
        int allLeiji2 = 0;
        int allLeiji3 = 0;
        
        double allUser2 = 0;
        double allUser3 = 0;
        
        
        
        allLeiji1 = dao.queryHeatmap6(floorNo1).size();
        allLeiji2 = dao.queryHeatmap6(floorNo2).size();
        allLeiji3 = dao.queryHeatmap6(floorNo3).size();
        
        allUser2 = Math.ceil(allLeiji2 * coefficient);
        allUser3 = Math.ceil(allLeiji3 * coefficient);
        
        allUsers1 = (dao.queryHeatmap5(floorNo1, Integer.parseInt(periodSel))).size();
        allUsers2 = (dao.queryHeatmap5(floorNo2, Integer.parseInt(periodSel))).size();
        allUsers3 = (dao.queryHeatmap5(floorNo3, Integer.parseInt(periodSel))).size();
//        allUsers2 = daoArea.getAllPeoples(floorNo2, tableName, bztime);
//        allUsers3 = daoArea.getAllPeoples(floorNo3, tableName, bztime);

        allDataMap.put("coefficient", coefficient);
        allDataMap.put("allTime1", allTime1);
        DecimalFormat    df   = new DecimalFormat("######0.00");   
        String avgAllTime2 = allUser2 == 0 ? "0.00" : df.format(allTimes2/60000.0/allUser2);
        String avgAllTime3 = allUser3 == 0 ? "0.00" : df.format(allTimes3/60000.0/allUser3);
        if (Double.parseDouble(avgAllTime2)>=120)
        {
            avgAllTime2 = "120.23";
        }
        if (Double.parseDouble(avgAllTime3)>=120)
        {
            avgAllTime2 = "120.10";
        }
        allDataMap.put("allTime2", avgAllTime2);
        allDataMap.put("allTime3", avgAllTime3);
        allDataMap.put("User1", Math.ceil(allUsers1 * coefficient));
        allDataMap.put("User2",Math.ceil(allUsers2 * coefficient) );
        allDataMap.put("User3", Math.ceil(allUsers3 * coefficient));
        allDataMap.put("allUser1",Math.ceil(allLeiji1 * coefficient) );
        allDataMap.put("allUser2",allUser2 );
        allDataMap.put("allUser3", allUser3);


        return allDataMap;
    }

    private String getMinute(long time, int size)
    {
        if (size == 0 || time == 0)
        {
            return "0";
        }
        else
        {

            float b = (float) (time / 1000) / 60;
            float averageTime = b / size;
            DecimalFormat df = new DecimalFormat("0.00");
            String min = df.format(averageTime);
            return min;
        }

    }

    private Map<String, Object> getAreaDate(List<Object> areaData,
            String areaId, String areaName, String visitDay,
            Map<String, Object> quyu, Map<String, Object> map, long nowTime,
            double coefficient)
    {
        areaData = new ArrayList<Object>();
        quyu = new HashMap<String, Object>();
        map = new HashMap<String, Object>();

        int allSize = 0;

        int size = 0;
        long allTimes = 0;
        // String areaId = ResultList1.get(i).getId();
        String times = null;
        quyu = daoArea.getAverageTimeByAreaId(areaId, visitDay);
        if (!quyu.isEmpty())
        {
            allSize = Integer.parseInt(quyu.get("count").toString());
            allTimes = Long.parseLong(quyu.get("timePeriod").toString());
            times = getMinute(
                    (Long.parseLong(quyu.get("timePeriod").toString())),
                    allSize);
        }
        if (Double.parseDouble(times)>120)
        {
            times = "120.21";
        }
        int allSize1 = daoArea.getAllArea(areaId);
        size = daoArea.getBaShowVisitUser(areaId, String.valueOf(nowTime));
        map = new HashMap<String, Object>();
        map.put("name", areaName);
        map.put("current", Math.ceil(size * coefficient));
        map.put("cumulative", Math.ceil(allSize1 * coefficient));
        map.put("average", times);
        map.put("allTime", allTimes);

        return map;
    }
    
	@RequestMapping(value = "/getBaShow", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> getBaShow() {
		Calendar currentDate = new GregorianCalendar();
		currentDate.set(Calendar.HOUR_OF_DAY, 0);
		currentDate.set(Calendar.MINUTE, 0);
		currentDate.set(Calendar.SECOND, 0);
		List<Map<String, Object>> bzData = bzDao.getBzAllData("1");
		String floorNo = null;
		String floorNo2 = null;
		String floorNo3 = null;
		String periodSel = null;
		double coefficient = 0;
		long bztime = 0;
		String startTime = null;
		long time = 0;
		if (bzData.size() > 0) {
			floorNo = bzData.get(0).get("floorNo").toString();
			floorNo2 = bzData.get(0).get("floorNo2").toString();
			floorNo3 = bzData.get(0).get("floorNo3").toString();
			startTime = bzData.get(0).get("startTime").toString();
			periodSel = bzData.get(0).get("periodSel").toString();
			coefficient = Double.parseDouble(bzData.get(0).get("coefficient")
					.toString());
			startTime = startTime.split(" ")[1].substring(0, 8);
			long maxTimestamp = daoArea.getMaxTimestamp();
			if(maxTimestamp > 0)
			{
				time = maxTimestamp - Integer.parseInt(periodSel)
						* 60 * 1000;
			}
			else
			{
				time = System.currentTimeMillis() - Integer.parseInt(periodSel)
					* 60 * 1000;
			}
		}
		if (coefficient == 0) {
			coefficient = 1.0;
		}

		List<AreaModel> ResultList = daoArea.selectAeareBaShow(floorNo);
		List<Object> areaData = new ArrayList<Object>();
		Map<String, Object> map = null;
		Map<String, Object> allDataMap = new HashMap<String, Object>(2);
		

		String tableName = Params.LOCATION
				+ ConvertUtil
						.dateFormat(currentDate.getTime(), Params.YYYYMMDD);
		String visitDay = ConvertUtil.dateFormat(currentDate.getTime(),
				"yyyy-MM-dd");
		//当前时间拼接
		if (startTime!=null) {
			String startDate = visitDay+" "+startTime;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				bztime = sdf.parse(startDate).getTime();
			} catch (Exception e) {
				log.debug("Time zhuanhuan error!");
			}
		}
		Map<String, Object> quyu = null;
		Map<String, Object> allQuyu = null;
		Map<String, Object> allQuyu2 = null;
		Map<String, Object> allQuyu3 = null;
		
		for (int i = 0; i < ResultList.size(); i++) {
			int allSize = 0;
			int size = 0;
			String areaId = ResultList.get(i).getId();
			String times = null;
			quyu = daoArea.getAverageTimeByAreaId1(areaId, bztime);
			if (!quyu.isEmpty()) {
				allSize =  Integer.parseInt(quyu.get("count").toString());
				times = getMinute(Long.parseLong( quyu.get("timePeriod").toString()), allSize);
			}
			size = daoArea.getBaShowVisitUser(areaId, String.valueOf(time));
			map = new HashMap<String, Object>();
			map.put("name", ResultList.get(i).getAreaName());
			map.put("current", Math.ceil(size * coefficient));
			map.put("cumulative",Math.ceil(allSize * coefficient));
			map.put("average", times);
			areaData.add(map);
		}
		allDataMap.put("item", areaData);

		String allAverageTime = null;
		int allUsers = 0;
		String areaTime = null;
		String allAverageTime2 = null;
		int allUsers2 = 0;
		String areaTime2 = null;
		String allAverageTime3 = null;
		int allUsers3 = 0;
		String areaTime3 = null;
		allQuyu = daoArea.getAllAverageTimeByAreaId(floorNo, bztime, tableName);
		allQuyu2 = daoArea.getAllAverageTimeByAreaId(floorNo2, bztime,
				tableName);
		allQuyu3 = daoArea.getAllAverageTimeByAreaId(floorNo3, bztime,
				tableName);
		if (!allQuyu.isEmpty()) {
			allAverageTime = allQuyu.get("timePeriod").toString();

			allUsers = Integer.parseInt( allQuyu.get("count").toString());
			areaTime = getMinute(Long.valueOf(allAverageTime), allUsers);
		}
		if (!allQuyu2.isEmpty()) {
			allAverageTime2 = allQuyu2.get("timePeriod").toString();

			allUsers2 =  Integer.parseInt(allQuyu2.get("count").toString());
			areaTime2 = getMinute(Long.valueOf(allAverageTime2), allUsers2);
		}
		if (!allQuyu3.isEmpty()) {
			allAverageTime3 = allQuyu3.get("timePeriod").toString();

			allUsers3 = Integer.parseInt( allQuyu3.get("count").toString());
			areaTime3 = getMinute(Long.valueOf(allAverageTime3), allUsers3);
		}
		// 用于隐藏热力图
		allDataMap.put("coefficient", coefficient);
		allDataMap.put("allTime", areaTime);
		allDataMap.put("allUser", Math.ceil(allUsers * coefficient));
		allDataMap.put("allTime2", areaTime2);
		allDataMap.put("allUser2", allUsers2);
		allDataMap.put("allTime3", areaTime3);
		allDataMap.put("allUser3", allUsers3);
		return allDataMap;
	}
	
	
    @RequestMapping(value = "/getBaShowData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getBaShowData()
    {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        // InputStream in = getClass().getClassLoader().getResourceAsStream(
        // "sva.properties");
        // Properties properties = new Properties();
        // try
        // {
        // properties.load(in);
        // } catch (IOException e)
        // {
        // log.error("load properties ERROR.", e);
        // }
        // // String time1 = properties.getProperty("bashow.time");
        // String floorNo = properties.getProperty("bashow.floorNo");
        List<Map<String, Object>> bzData = bzDao.getAllFloorNo("1");
        String floorNo1 = null;
        String floorNo2 = null;
        String floorNo3 = null;
        String floorNo4 = null;
        String floorNo5 = null;
        String floorNo6 = null;
        String floorNo7 = null;
        String floorNo8 = null;
        String periodSel = null;
        double coefficient = 0;
        long bztime = 0;
        String startTime = null;
        long time = 0;
        if (bzData.size() > 0)
        {
            floorNo1 = bzData.get(0).get("floorNo1").toString();
            floorNo2 = bzData.get(0).get("floorNo2").toString();
            floorNo3 = bzData.get(0).get("floorNo3").toString();
            floorNo4 = bzData.get(0).get("floorNo4").toString();
            floorNo5 = bzData.get(0).get("floorNo5").toString();
            floorNo6 = bzData.get(0).get("floorNo6").toString();
            floorNo7 = bzData.get(0).get("floorNo7").toString();
            floorNo8 = bzData.get(0).get("floorNo8").toString();
            startTime = bzData.get(0).get("startTime").toString();
            periodSel = bzData.get(0).get("periodSel").toString();
            coefficient = Double.parseDouble(bzData.get(0).get("coefficient")
                    .toString());
            startTime = startTime.split(" ")[1].substring(0, 8);
            long maxTimestamp = daoArea.getMaxTimestamp();
            if (maxTimestamp > 0)
            {
                time = maxTimestamp - Integer.parseInt(periodSel) * 60 * 1000;
            }
            else
            {
                time = System.currentTimeMillis() - Integer.parseInt(periodSel)
                        * 60 * 1000;
            }
        }
        if (coefficient == 0)
        {
            coefficient = 1.0;
        }

        List<AreaModel> ResultList1 = daoArea.selectAeareBaShow(floorNo1);
        List<AreaModel> ResultList2 = daoArea.selectAeareBaShow(floorNo2);
        List<AreaModel> ResultList3 = daoArea.selectAeareBaShow(floorNo3);
        List<AreaModel> ResultList4 = daoArea.selectAeareBaShow(floorNo4);
        List<AreaModel> ResultList5 = daoArea.selectAeareBaShow(floorNo5);
        List<AreaModel> ResultList6 = daoArea.selectAeareBaShow(floorNo6);
        List<AreaModel> ResultList7 = daoArea.selectAeareBaShow(floorNo7);
        List<AreaModel> ResultList8 = daoArea.selectAeareBaShow(floorNo8);
        long nowTime = System.currentTimeMillis()
                - (Integer.parseInt(periodSel) + 10) * 60 * 1000;
        List<Object> areaData = new ArrayList<Object>();
        List<Object> areaData1 = null;
        Map<String, Object> map = null;
        Map<String, Object> allDataMap = new HashMap<String, Object>(2);

        String tableName = Params.LOCATION
                + ConvertUtil
                        .dateFormat(currentDate.getTime(), Params.YYYYMMDD);
        String visitDay = ConvertUtil.dateFormat(currentDate.getTime(),
                "yyyy-MM-dd");
        // 当前时间拼接
        if (startTime != null)
        {
            String startDate = visitDay + " " + startTime;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try
            {
                bztime = sdf.parse(startDate).getTime();
            }
            catch (Exception e)
            {
                log.debug("Time zhuanhuan error!");
            }
        }
        Map<String, Object> tquyu = null;
        for (int i = 0; i < ResultList1.size(); i++)
        {
            Map<String, Object> quyu1 = null;
            quyu1 = getAreaDate(areaData1, ResultList1.get(i).getId(),
                    ResultList1.get(i).getAreaName(), visitDay, tquyu, map,
                    nowTime, coefficient);
            if (quyu1.size() != 0)
            {
                areaData.add(quyu1);
            }
        }
        for (int i = 0; i < ResultList2.size(); i++)
        {
            Map<String, Object> quyu2 = null;
            quyu2 = getAreaDate(areaData1, ResultList2.get(i).getId(),
                    ResultList2.get(i).getAreaName(), visitDay, tquyu, map,
                    nowTime, coefficient);
            if (quyu2.size() != 0)
            {
                areaData.add(quyu2);
            }
        }
        for (int i = 0; i < ResultList3.size(); i++)
        {
            Map<String, Object> quyu3 = null;
            quyu3 = getAreaDate(areaData1, ResultList3.get(i).getId(),
                    ResultList3.get(i).getAreaName(), visitDay, tquyu, map,
                    nowTime, coefficient);
            if (quyu3.size() != 0)
            {
                areaData.add(quyu3);
            }
        }
        for (int i = 0; i < ResultList4.size(); i++)
        {
            Map<String, Object> quyu4 = null;
            quyu4 = getAreaDate(areaData1, ResultList4.get(i).getId(),
                    ResultList4.get(i).getAreaName(), visitDay, tquyu, map,
                    nowTime, coefficient);
            if (quyu4.size() != 0)
            {
                areaData.add(quyu4);
            }
        }
        for (int i = 0; i < ResultList5.size(); i++)
        {
            Map<String, Object> quyu5 = null;
            quyu5 = getAreaDate(areaData1, ResultList5.get(i).getId(),
                    ResultList5.get(i).getAreaName(), visitDay, tquyu, map,
                    nowTime, coefficient);
            if (quyu5.size() != 0)
            {
                areaData.add(quyu5);
            }
        }
        for (int i = 0; i < ResultList6.size(); i++)
        {
            Map<String, Object> quyu6 = null;
            quyu6 = getAreaDate(areaData1, ResultList6.get(i).getId(),
                    ResultList6.get(i).getAreaName(), visitDay, tquyu, map,
                    nowTime, coefficient);
            if (quyu6.size() != 0)
            {
                areaData.add(quyu6);
            }
        }
        for (int i = 0; i < ResultList7.size(); i++)
        {
            Map<String, Object> quyu7 = null;
            quyu7 = getAreaDate(areaData1, ResultList7.get(i).getId(),
                    ResultList7.get(i).getAreaName(), visitDay, tquyu, map,
                    nowTime, coefficient);
            if (quyu7.size() != 0)
            {
                areaData.add(quyu7);
            }
        }
        for (int i = 0; i < ResultList8.size(); i++)
        {
            Map<String, Object> quyu8 = null;
            quyu8 = getAreaDate(areaData1, ResultList8.get(i).getId(),
                    ResultList8.get(i).getAreaName(), visitDay, tquyu, map,
                    nowTime, coefficient);
            if (quyu8.size() != 0)
            {
                areaData.add(quyu8);
            }
        }
        // Map<String, Object> quyu1 = getAreaDate(areaData1, ResultList2,
        // visitDay, tquyu, map, nowTime, coefficient);
        // Map<String, Object> quyu2 = getAreaDate(areaData1, ResultList3,
        // visitDay, tquyu, map, nowTime, coefficient);
        // Map<String, Object> quyu3 = getAreaDate(areaData1, ResultList4,
        // visitDay, tquyu, map, nowTime, coefficient);
        // Map<String, Object> quyu4 = getAreaDate(areaData1, ResultList5,
        // visitDay, tquyu, map, nowTime, coefficient);
        // Map<String, Object> quyu5 = getAreaDate(areaData1, ResultList6,
        // visitDay, tquyu, map, nowTime, coefficient);
        // Map<String, Object> quyu6 = getAreaDate(areaData1, ResultList7,
        // visitDay, tquyu, map, nowTime, coefficient);
        // Map<String, Object> quyu7 = getAreaDate(areaData1, ResultList8,
        // visitDay, tquyu, map, nowTime, coefficient);

        // if (quyu1.size() != 0)
        // {
        // areaData.add(quyu1);
        // }
        // if (quyu2.size() != 0)
        // {
        // areaData.add(quyu2);
        // }
        // if (quyu3.size() != 0)
        // {
        // areaData.add(quyu3);
        // }
        // if (quyu4.size() != 0)
        // {
        // areaData.add(quyu4);
        // }
        // if (quyu.size() != 0)
        // {
        // areaData.add(quyu5);
        // }
        // if (quyu6.size() != 0)
        // {
        // areaData.add(quyu6);
        // }
        // if (quyu7.size() != 0)
        // {
        // areaData.add(quyu7);
        // }

        allDataMap.put("item", areaData);

        int allUsers1 = 0;
        int allUsers2 = 0;
        int allUsers3 = 0;
        int allUsers4 = 0;
        int allUsers5 = 0;
        int allUsers6 = 0;
        int allUsers7 = 0;
        int allUsers8 = 0;

        allUsers1 = daoArea.getAllPeoples(floorNo1, tableName, bztime);
        allUsers2 = daoArea.getAllPeoples(floorNo2, tableName, bztime);
        allUsers3 = daoArea.getAllPeoples(floorNo3, tableName, bztime);
        allUsers4 = daoArea.getAllPeoples(floorNo4, tableName, bztime);
        allUsers5 = daoArea.getAllPeoples(floorNo5, tableName, bztime);
        allUsers6 = daoArea.getAllPeoples(floorNo6, tableName, bztime);
        allUsers7 = daoArea.getAllPeoples(floorNo7, tableName, bztime);
        allUsers8 = daoArea.getAllPeoples(floorNo8, tableName, bztime);

        // 用于隐藏热力图
        allDataMap.put("coefficient", coefficient);
        allDataMap.put("allUser1", Math.ceil(allUsers1 * coefficient));
        allDataMap.put("allUser2", Math.ceil(allUsers2 * coefficient));
        allDataMap.put("allUser3", Math.ceil(allUsers3 * coefficient));
        allDataMap.put("allUser4", Math.ceil(allUsers4 * coefficient));
        allDataMap.put("allUser5", Math.ceil(allUsers5 * coefficient));
        allDataMap.put("allUser6", Math.ceil(allUsers6 * coefficient));
        allDataMap.put("allUser7", Math.ceil(allUsers7 * coefficient));
        allDataMap.put("allUser8", Math.ceil(allUsers8 * coefficient));

        return allDataMap;
    }
    
    
    /** 
     * @Title: getShDateJing 
     * @Description: 菁蓉整数据获取
     * @return Map<String,Object>       
     * @throws 
     */
    @RequestMapping(value = "/getShDateJing", method = {RequestMethod.POST})
@ResponseBody
public Map<String, Object> getShDateJing()
{
    Calendar currentDate = new GregorianCalendar();
    currentDate.set(Calendar.HOUR_OF_DAY, 0);
    currentDate.set(Calendar.MINUTE, 0);
    currentDate.set(Calendar.SECOND, 0);
    List<Map<String, Object>> bzData = bzDao.getAllFloorNoJing("1");
    String floorNo1 = null;
    String floorNo2 = null;
    String floorNo3 = null;
    String floorNo4 = null;
    String periodSel = null;
    double coefficient = 0;
    long bztime = 0;
    String startTime = null;
    long time = 0;
    if (bzData.size() > 0)
    {
        floorNo1 = bzData.get(0).get("floorNo1").toString();
        floorNo2 = bzData.get(0).get("floorNo2").toString();
        floorNo3 = bzData.get(0).get("floorNo3").toString();
        floorNo4= bzData.get(0).get("floorNo4").toString();
        startTime = bzData.get(0).get("startTime").toString();
        periodSel = bzData.get(0).get("periodSel").toString();
        coefficient = Double.parseDouble(bzData.get(0).get("coefficient")
                .toString());
        startTime = startTime.split(" ")[1].substring(0, 8);
//        long maxTimestamp = daoArea.getMaxTimestamp();
//        if (maxTimestamp > 0)
//        {
//            time = maxTimestamp - Integer.parseInt(periodSel) * 60 * 1000;
//        }
//        else
//        {
//            time = System.currentTimeMillis() - Integer.parseInt(periodSel)
//                    * 60 * 1000;
//        }
    }
    if (coefficient == 0)
    {
        coefficient = 1.0;
    }

    List<AreaModel> ResultList1 = daoArea.selectAeareBaShow(floorNo1);
    List<AreaModel> ResultList2 = daoArea.selectAeareBaShow(floorNo2);
    List<AreaModel> ResultList3 = daoArea.selectAeareBaShow(floorNo3);
    List<AreaModel> ResultList4 = daoArea.selectAeareBaShow(floorNo4);
    long nowTime = System.currentTimeMillis()
            - (Integer.parseInt(periodSel)+1) * 60 * 1000;
    List<Object> areaData = new ArrayList<Object>();
    List<Object> areaData2 = new ArrayList<Object>();
    List<Object> areaData3 = new ArrayList<Object>();
    List<Object> areaData4 = new ArrayList<Object>();
    List<Object> areaData1 = new ArrayList<Object>();
    Map<String, Object> map = null;
    Map<String, Object> allDataMap = new HashMap<String, Object>(2);

    String tableName = Params.LOCATION
            + ConvertUtil
                    .dateFormat(currentDate.getTime(), Params.YYYYMMDD);
    String visitDay = ConvertUtil.dateFormat(currentDate.getTime(),
            "yyyy-MM-dd");
    // 当前时间拼接
    if (startTime != null)
    {
        String startDate = visitDay + " " + startTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            bztime = sdf.parse(startDate).getTime();
        }
        catch (Exception e)
        {
            log.debug("Time zhuanhuan error!");
        }
    }
    Map<String, Object> tquyu = null;
    double allTime1 = 0;
    double allTime2 = 0;
    double allTime3 = 0;
    double allTime4 = 0;
    long allTimes1 = 0; 
    long allTimes2 = 0; 
    long allTimes3 = 0; 
    long allTimes4 = 0; 
    for (int i = 0; i < ResultList1.size(); i++)
    {
        Map<String, Object> quyu2 = null;
        quyu2 = getAreaDate(areaData1, ResultList1.get(i).getId(),
                ResultList1.get(i).getAreaName(), visitDay, tquyu, map,
                nowTime, coefficient);
        allTime1 = allTime1 +  Double.parseDouble(quyu2.get("average").toString());
        allTimes1 = Long.parseLong(quyu2.get("allTime").toString())+allTimes1;
        if (quyu2.size() != 0)
        {
            areaData.add(quyu2);
        }
    }
    for (int i = 0; i < ResultList2.size(); i++)
    {
        Map<String, Object> quyu2 = null;
        quyu2 = getAreaDate(areaData1, ResultList2.get(i).getId(),
                ResultList2.get(i).getAreaName(), visitDay, tquyu, map,
                nowTime, coefficient);
        allTime2 = allTime2 +  Double.parseDouble(quyu2.get("average").toString());
        allTimes2 = Long.parseLong(quyu2.get("allTime").toString())+allTimes2;
        if (quyu2.size() != 0)
        {
            areaData2.add(quyu2);
        }
    }
    for (int i = 0; i < ResultList3.size(); i++)
    {
        Map<String, Object> quyu3 = null;
        quyu3 = getAreaDate(areaData1, ResultList3.get(i).getId(),
                ResultList3.get(i).getAreaName(), visitDay, tquyu, map,
                nowTime, coefficient);
        allTime3 = allTime3 +  Double.parseDouble((quyu3.get("average").toString()));
        allTimes3 = Long.parseLong(quyu3.get("allTime").toString())+allTimes3;
        if (quyu3.size() != 0)
        {
            areaData3.add(quyu3);
        }
    }
    for (int i = 0; i < ResultList4.size(); i++)
    {
        Map<String, Object> quyu3 = null;
        quyu3 = getAreaDate(areaData4, ResultList4.get(i).getId(),
            ResultList4.get(i).getAreaName(), visitDay, tquyu, map,
            nowTime, coefficient);
        allTime4 = allTime4 +  Double.parseDouble((quyu3.get("average").toString()));
        allTimes4 = Long.parseLong(quyu3.get("allTime").toString())+allTimes4;
        if (quyu3.size() != 0)
        {
            areaData4.add(quyu3);
        }
    }

    allDataMap.put("item", areaData);
    allDataMap.put("item1", areaData2);
    allDataMap.put("item2", areaData3);
    allDataMap.put("item4", areaData4);

    int allUsers1 = 0;
    int allUsers2 = 0;
    int allUsers3 = 0;
    int allUsers4 = 0;

    int allLeiji1 = 0;
    int allLeiji2 = 0;
    int allLeiji3 = 0;
    int allLeiji4 = 0;
    
    double allUser1 = 0;
    double allUser2 = 0;
    double allUser3 = 0;
    double allUser4 = 0;
    
    
    
    allLeiji1 = dao.queryHeatmap6(floorNo1).size();
    allLeiji2 = dao.queryHeatmap6(floorNo2).size();
    allLeiji3 = dao.queryHeatmap6(floorNo3).size();
    allLeiji4 = dao.queryHeatmap6(floorNo4).size();
    
    allUser1 = Math.ceil(allLeiji1 * coefficient);
    allUser2 = Math.ceil(allLeiji2 * coefficient);
    allUser3 = Math.ceil(allLeiji3 * coefficient);
    allUser4 = Math.ceil(allLeiji4 * coefficient);
    
    allUsers1 = (dao.queryHeatmap5(floorNo1, Integer.parseInt(periodSel))).size();
    allUsers2 = (dao.queryHeatmap5(floorNo2, Integer.parseInt(periodSel))).size();
    allUsers3 = (dao.queryHeatmap5(floorNo3, Integer.parseInt(periodSel))).size();
    allUsers4 = (dao.queryHeatmap5(floorNo4, Integer.parseInt(periodSel))).size();
//    allUsers2 = daoArea.getAllPeoples(floorNo2, tableName, bztime);
//    allUsers3 = daoArea.getAllPeoples(floorNo3, tableName, bztime);

    allDataMap.put("coefficient", coefficient);
    allDataMap.put("allTime1", allTime1);
    DecimalFormat    df   = new DecimalFormat("######0.00");   
    String avgAllTime1 = allUser1 == 0 ? "0.00" : df.format(allTimes1/60000.0/allUser1);
    String avgAllTime2 = allUser2 == 0 ? "0.00" : df.format(allTimes2/60000.0/allUser2);
    String avgAllTime3 = allUser3 == 0 ? "0.00" : df.format(allTimes3/60000.0/allUser3);
    String avgAllTime4 = allUser4 == 0 ? "0.00" : df.format(allTimes4/60000.0/allUser4);
    if (Double.parseDouble(avgAllTime2)>=120)
    {
        avgAllTime2 = "120.23";
    }
    if (Double.parseDouble(avgAllTime3)>=120)
    {
        avgAllTime2 = "120.10";
    }
    allDataMap.put("allTime1", avgAllTime1);
    allDataMap.put("allTime2", avgAllTime2);
    allDataMap.put("allTime3", avgAllTime3);
    allDataMap.put("allTime4", avgAllTime4);
    allDataMap.put("User1", Math.ceil(allUsers1 * coefficient));
    allDataMap.put("User2",Math.ceil(allUsers2 * coefficient) );
    allDataMap.put("User3", Math.ceil(allUsers3 * coefficient));
    allDataMap.put("User4", Math.ceil(allUsers4 * coefficient));
    allDataMap.put("allUser1",allUser1);
    allDataMap.put("allUser2",allUser2 );
    allDataMap.put("allUser3", allUser3);
    allDataMap.put("allUser4", allUser4);


    return allDataMap;
}

    /** 
     * @Title: getLocationData 
     * @Description: 获取Ils定位信息接口 
     * @param model
     * @return 
     */
    @RequestMapping(value = "/app/getLocationInfo", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getLocationData(@RequestBody IlsModel model)
    {
        //获取ip
        String ip = model.getInternalIP();
        //结果原因
        String reason = "NA";
        //结果
        String status = "faild";
        //坐标点
        BigDecimal x = new BigDecimal(0);
        BigDecimal y = new BigDecimal(0);
        //楼层
        BigDecimal z = null;
        //userID
        String userID = null;
        //id类型
        String idType = null;
        //时间戳
        BigDecimal timestamp = null;
        //数据类型
        String dataType = null;
        //Map结果
        Map<String, Object> result = new HashMap<String,Object>(2);
        //坐标点map
        InputStream in = getClass().getClassLoader().getResourceAsStream("sva.properties");
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            log.error("load properties ERROR.", e);
        }
        //睡眠时间
        int time = Integer.parseInt((properties.getProperty("ILS.time")));
        
        Map<String, Object> locationData = new HashMap<String,Object>(2);
        Map<String, Object> GxyData = new HashMap<String,Object>(2);
        Collection<LocationModel> resultList= new ArrayList<LocationModel>(10);
        int svaId = dao.getSvaId(model.getCgi());
        log.debug("Ils cgi:"+model.getCgi());
        //根据cgi查询数据库svaId是否存在，返回【本小区不支持室内定位】
        if (svaId!=0) {
            //判断参数ip是否为空
            if (StringUtils.isEmpty(ip)) {
                reason = "IP cannot be empty!";
            }else
            {
                //根据userId查询数据结果，无数据向sva发起订阅请求
                String userId =  ConvertUtil.convertMacOrIp(ip);
                resultList= dao.getIlsData(userId);
               if (resultList.size()>0) {
                    for (LocationModel re:resultList) {
                         x = re.getX();
                         y = re.getY();
                         z = re.getZ();
                         userID = re.getUserID();
                         idType = re.getIdType();
                         timestamp = re.getTimestamp();
                         dataType = re.getDataType();
                    }
                    status = "Ok";
                }else
                {
                    log.debug("Ils svaId:"+svaId);
                    //根据svaid查看是否有指定用户配置的sva，存在则进行sva订阅
                    Collection<SvaModel> svaList = svaDao.getSvaData(svaId);
                    if (svaList.size()>0) {
                        for (SvaModel sva : svaList)
                        {
                            String token = null;
                            HttpUtil capi = null;
                            String jsonStr = null;
                            String charset = null;
                            String content = null;
                            String url = null;
                            try
                            {
        
                                    url = "https://" + sva.getIp() + ':' + sva.getTokenProt()
                                            + "/v3/auth/tokens";
                                    content = "{\"auth\":{\"identity\":{\"methods\":[\"password\"],\"password\": {\"user\": {\"domain\": \"Api\",\"name\": \""
                                            + sva.getUsername()
                                            + "\",\"password\": \""
                                            + sva.getPassword() + "\"}}}}}";
                                    charset = "UTF-8";
                                    log.debug("Ils from ip:" + ip + ",getToken url:" + url);
                                    capi = new HttpUtil();
        
                                    token = capi.httpsPost(url, content, charset);
                                    url = "https://" + sva.getIp() + ':' + sva.getTokenProt()
                                            + "/enabler/catalog/locationstreamreg/json/v1.0";
                                    content = "{\"APPID\":\"" + sva.getUsername()
                                            + "\",\"useridlist\":[\""
                                            + ConvertUtil.convertMacOrIp(ip) + "\"]}";
                                    log.debug("Ils from ip:" + ip + ",subscription url:" + url
                                            + " content:" + content);
                                    jsonStr = capi.subscription(url, content, token, "POST");
                                    log.debug("Ils subscription:" + jsonStr);
                                    reason = "Ils subscription result:"+jsonStr;
                                    //订阅后等待数秒再去取数据
                                    Thread.sleep(time);
                                    resultList= dao.getIlsData(userId);
                                    for (LocationModel re:resultList) {
                                        x = re.getX();
                                        y = re.getY();
                                        z = re.getZ();
                                        userID = re.getUserID();
                                        idType = re.getIdType();
                                        timestamp = re.getTimestamp();
                                        dataType = re.getDataType();
                                        status = "Ok";
                                   }
                            }
                            catch (KeyManagementException e)
                            {
                                status = "faild";
                                reason = "KeyManagementException:"+e;
                                log.error("Ils KeyManagementException.", e);
                            }
                            catch (NoSuchAlgorithmException e)
                            {
                                status = "faild";
                                reason = "NoSuchAlgorithmException:"+e;
                                log.error("Ils NoSuchAlgorithmException.", e);
                            }
                            catch (IOException e)
                            {
                                status = "faild";
                                reason = "IOException:"+e;
                                log.error("Ils IOException.", e);
                            }
                            catch (Exception e)
                            {
                                status = "faild";
                                reason = "Exception:"+e;
                                log.error("Ils Exception.", e);
                            }
        
                            
                        }
                    }else
                    {
                        Collection<LocationModel> noConfigResult= dao.getIlsData(userId);
                        if (noConfigResult.size()>0) {
                            log.debug("Ils：Did not specify a user subscription, but there are data!");
                            reason = "Did not specify a user subscription, but there are data!";
                        }else
                        {
                            log.debug("Ils：Specified user configuration was not found!");
                            reason = "Specified user configuration was not found!";
                        }
                    }
 
                    }
            }
        }else
        {
            status = "faild";
            reason = "Please check the info_cgi_mapper configuration!";
        }
        BigDecimal gx = new BigDecimal(0);
        BigDecimal gy = new BigDecimal(0);        
        if (z!=null) {
            
            Collection<AcquisitionPoint> point = comDao.getLatitudeCoordinates(z);
            GxyData = Util.getLatitudeCoordinates(point,x,y);
            gx = new BigDecimal(GxyData.get("gx").toString());
            gy = new BigDecimal(GxyData.get("gy").toString());
        }
        //封装数据结果
        locationData.put("Latitude", gx);
        locationData.put("Longitude", gy);
        locationData.put("z", z);
        locationData.put("userID", userID);
        locationData.put("idType", idType);
        locationData.put("timestamp", timestamp);
        locationData.put("dataType", dataType);
        result.put("type", "1");
        result.put("Status", status);
        result.put("Reason", reason);
        result.put("Location",locationData);
        return result;
        
    }
    
    @RequestMapping(value = "/getDataByFloorNo", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getDataByFloorNo(@RequestParam("floorNo") String floorNo,@RequestParam(value = "time", required = false) String times)
    {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        String periodSel = null;
        if (times==null) {
            periodSel = "5";
        }else
        {
            periodSel = times;
        }
        String floorNo1 = floorNo;

        double coefficient = 1;
        long bztime = 0;
        String startTime = null;
        long time = 0;


        List<AreaModel> ResultList1 = daoArea.selectAeareBaShow(floorNo1);

        long nowTime = System.currentTimeMillis()
                - (Integer.parseInt(periodSel)+1) * 60 * 1000;
        List<Object> areaData = new ArrayList<Object>();

        List<Object> areaData1 = new ArrayList<Object>();
        Map<String, Object> map = null;
        Map<String, Object> allDataMap = new HashMap<String, Object>(2);

        String visitDay = ConvertUtil.dateFormat(currentDate.getTime(),
                "yyyy-MM-dd");
        // 当前时间拼接
            String startDate = visitDay + " " + "00:00:00";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try
            {
                bztime = sdf.parse(startDate).getTime();
            }
            catch (Exception e)
            {
                log.debug("Time zhuanhuan error!");
            }
        Map<String, Object> tquyu = null;
        double allTime1 = 0;

        long allTimes1 = 0; 

        for (int i = 0; i < ResultList1.size(); i++)
        {
            Map<String, Object> quyu2 = null;
            quyu2 = getAreaDate(areaData1, ResultList1.get(i).getId(),
                    ResultList1.get(i).getAreaName(), visitDay, tquyu, map,
                    nowTime, coefficient);
            allTime1 = allTime1 +  Double.parseDouble(quyu2.get("average").toString());
            allTimes1 = Long.parseLong(quyu2.get("allTime").toString())+allTimes1;
            if (quyu2.size() != 0)
            {
                areaData.add(quyu2);
            }
        }


        allDataMap.put("item", areaData);


        int allUsers1 = 0;


        int allLeiji1 = 0;

        
        double allUser1 = 0;

        
        
        allLeiji1 = dao.queryHeatmap6(floorNo1).size();

        
        allUser1 = Math.ceil(allLeiji1 * coefficient);

        
        allUsers1 = (dao.queryHeatmap5(floorNo1, Integer.parseInt(periodSel))).size();

        allDataMap.put("allTime1", allTime1);
        DecimalFormat    df   = new DecimalFormat("######0.00");   
        String avgAllTime1 = allUser1 == 0 ? "0.00" : df.format(allTimes1/60000.0/allUser1);


        allDataMap.put("average", avgAllTime1);

        allDataMap.put("user", Math.ceil(allUsers1 * coefficient));

        allDataMap.put("allUser",allUser1);

        return allDataMap;
    }
        //获取所有的地图信息
        @RequestMapping(value = "/getAllFloorNo", method = {RequestMethod.GET})
        @ResponseBody
        public Map<String, Object> getAllFloorNo()
        {
            Map<String, Object> map = new HashMap<String, Object>(2);
            Collection<MapsModel> model= null;
            model = daoMaps.doquery();
            map.put("data", model);
            map.put("error", null);
            return map;
            
        }
}
