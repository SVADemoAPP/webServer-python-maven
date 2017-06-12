package com.sva.web.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sva.common.Util;
import com.sva.dao.MapsDao;
import com.sva.model.MapsModel;
import com.sva.web.models.MapMngModel;

@Controller
@RequestMapping(value = "/map")
public class MapController
{

    @Autowired
    private MapsDao dao;
    
    private static Logger log = Logger.getLogger(MapController.class);

    @RequestMapping(value = "/api/getTableData", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getTableData(HttpServletRequest request,
            Model model)
    {
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        Collection<MapsModel> ResultList = new ArrayList<MapsModel>(10);
        Collection<MapsModel> store = new ArrayList<MapsModel>(10);
        Object userName = request.getSession().getAttribute("username");
        @SuppressWarnings("unchecked")
        List<String> storeides = (List<String>) request.getSession()
                .getAttribute("storeides");
        if ("admin".equals(userName))
        {

            ResultList = dao.doquery();
        }
        else
        {
            if (storeides.size() > 0)
            {
                String storeid = storeides.get(0);
                String[] stores = storeid.split(",");
                for (int i = 0; i < stores.length; i++)
                {
                    store = dao.doqueryByStoreid(Integer.parseInt(stores[i]));
                    if (store != null)
                    {
                        if (ResultList == null)
                        {
                            ResultList = store;
                        }
                        else if (!store.isEmpty())
                        {
                            ResultList.addAll(store);
                        }
                    }
                }
            }
        }

        modelMap.put("error", null);
        modelMap.put("data", ResultList);

        return modelMap;
    }

    @RequestMapping(value = "/api/deleteByFloor", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteTableData(Model model,
            HttpServletRequest request, @RequestParam("floorNo") String floorNo)
    {
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        int result = 0;
        int result1 = 0;
        int result2 = 0;
        try
        {
            // 删除图片
            String name = dao.getMapName(floorNo);
            // 文件路径
            String filedir = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/upload");
            File file = new File(filedir, name);
            if (!file.exists())
            {
                // 文件不存在;
                modelMap.put("error", true);
            }
            else
            {
                if (file.isFile())
                {
                    file.delete();
                }
                else
                {
                    modelMap.put("error", true);
                }
            }

            // 删除数据
            result = dao.deleteMapByFloor(floorNo);
            String ip = Util.getIpAddr(request);
            log.info(ip+"delete mapData:floorNo="+floorNo);
        }
        catch (Exception e)
        {
            Logger.getLogger(MapController.class).info(e.getMessage());
        }
        if (result > 0 || result1 > 0 || result2 > 0)
        {
            modelMap.put("error", null);
        }
        else
        {
            modelMap.put("error", true);
        }

        return modelMap;
    }

    @RequestMapping(value = "/api/upload")
    public String upload(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "svgfile", required = false) MultipartFile svg,
            @RequestParam(value = "pathroutefile", required = false) MultipartFile pathFile,
            @RequestParam(value = "pathroutefile4", required = false) MultipartFile zMap,
            @RequestParam(value = "pathroutefile5", required = false) MultipartFile iosMap,
            @RequestParam(value = "pathroutefile6", required = false) MultipartFile zPathFile,
            @RequestParam(value = "pathroutefile7", required = false) MultipartFile fMap,
            @RequestParam(value = "routefile", required = false) MultipartFile route,
            HttpServletRequest request, ModelMap model, MapMngModel mapMngModel)
            throws Exception
    {
        // mapMngModel.setPlace(svaname + "_" + mapMngModel.getPlace());
        // Collection<SvaModel> svam = svaDao.selectID(svaname);
        String ip = Util.getIpAddr(request);
        long updateTime = System.currentTimeMillis();
        mapMngModel.setUpdateTime(String.valueOf(updateTime));
        BigDecimal id = null;
        String nu = "";
        BigDecimal sid = (BigDecimal) mapMngModel.getFloorid();
        // 通过placeid查询出需要的商场的值
        String placeName = dao.placeByPlaceId(mapMngModel.getPlaceId());
        mapMngModel.setPlace(placeName);
        int valueid = sid.intValue();
        // for (SvaModel s : svam)
        // {
        id = new BigDecimal(10000 * mapMngModel.getPlaceId());
        if (valueid > 0)
        {
            mapMngModel.setFloorNo(id.add(mapMngModel.getFloorid()));
        }
        else
        {
            mapMngModel.setFloorNo(id.add(
                    mapMngModel.getFloorid().multiply(new BigDecimal(-1))).add(
                    new BigDecimal(5000)));
        }
        // }

        if (!mapMngModel.getId().equals(nu))
        {
            log.info(ip+"updata mapData:floorNo="+mapMngModel.getFloorNo());
            String fileName = file.getOriginalFilename();
            String svgName = svg.getOriginalFilename();
            String routeName = route.getOriginalFilename();
            String pathfileName = pathFile.getOriginalFilename();
            String fMapName = fMap.getOriginalFilename();
            String zMapName = zMap.getOriginalFilename();
            String zPathFileName = zPathFile.getOriginalFilename();
            String  iosMapName = iosMap.getOriginalFilename();
            if (!fileName.equals(nu))
            {
                String path = request.getSession().getServletContext()
                        .getRealPath("/WEB-INF/upload");
                String _ext = fileName.substring(fileName.lastIndexOf('.'));
                fileName = mapMngModel.getFloorNo() + "_"
                        + mapMngModel.getFloor() + _ext;
                mapMngModel.setPath(fileName);
                Logger.getLogger(MapController.class).debug(path);
                File targetFile = new File(path, fileName);
                if (!targetFile.exists())
                {
                    targetFile.mkdirs();
                }
                // 修改
                try
                {
                    BufferedImage sourceImg = javax.imageio.ImageIO.read(file
                            .getInputStream());
                    file.transferTo(targetFile);

                    mapMngModel.setImgWidth(sourceImg.getWidth());
                    mapMngModel.setImgHeight(sourceImg.getHeight());
                }
                catch (Exception e)
                {
                    Logger.getLogger(MapController.class).info(e.getMessage());
                }
            }

            if (!svgName.equals(nu))
            {
                String path = request.getSession().getServletContext()
                        .getRealPath("/WEB-INF/upload");
                String _ext = svgName.substring(svgName.lastIndexOf('.'));
                svgName = mapMngModel.getFloorNo() + "_"
                        + mapMngModel.getFloor() + _ext;
                mapMngModel.setSvg(svgName);
                Logger.getLogger(MapController.class).debug(svgName);
                File targetFile = new File(path, svgName);
                if (!targetFile.exists())
                {
                    targetFile.mkdirs();
                }
                // 修改
                try
                {
                    svg.transferTo(targetFile);
                }
                catch (Exception e)
                {
                    Logger.getLogger(MapController.class).info(e.getMessage());
                }
            }
            if (!routeName.equals(nu))
            {
                String path = request.getSession().getServletContext()
                        .getRealPath("/WEB-INF/upload");
                String _ext = routeName.substring(routeName.lastIndexOf('.'));
                routeName = mapMngModel.getFloorNo() + "_"
                        + mapMngModel.getFloor() + _ext;
                mapMngModel.setRoute(routeName);
                Logger.getLogger(MapController.class).debug(svgName);
                File targetFile = new File(path, routeName);
                if (!targetFile.exists())
                {
                    targetFile.mkdirs();
                }
                // 修改
                try
                {
                    route.transferTo(targetFile);
                }
                catch (Exception e)
                {
                    Logger.getLogger(MapController.class).info(e.getMessage());
                }
            }
            
            if (!pathfileName.equals(nu))
            {
                String path = request.getSession().getServletContext()
                        .getRealPath("/WEB-INF/upload");
                String _ext = pathfileName.substring(pathfileName.lastIndexOf('.'));
                pathfileName = mapMngModel.getUpdateTime() + "_"
                        + mapMngModel.getFloor() + _ext;
                mapMngModel.setPathFile(pathfileName);
                Logger.getLogger(MapController.class).debug(pathfileName);
                File targetFile = new File(path, pathfileName);
                if (!targetFile.exists())
                {
                    targetFile.mkdirs();
                }
                // 修改
                try
                {
                    pathFile.transferTo(targetFile);
                }
                catch (Exception e)
                {
                    Logger.getLogger(MapController.class).info(e.getMessage());
                }
            }
            if (!zMapName.equals(nu))
            {
                String path = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/upload");
                String _ext = zMapName.substring(zMapName.lastIndexOf('.'));
                zMapName =System.currentTimeMillis() + "_"
                    + mapMngModel.getFloor() + _ext;
                mapMngModel.setzMap(zMapName);
                Logger.getLogger(MapController.class).debug(zMapName);
                File targetFile = new File(path, zMapName);
                if (!targetFile.exists())
                {
                    targetFile.mkdirs();
                }
                // 修改
                try
                {
                    zMap.transferTo(targetFile);
                }
                catch (Exception e)
                {
                    Logger.getLogger(MapController.class).info(e.getMessage());
                }
            }
            if (!iosMapName.equals(nu))
            {
                String path = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/upload");
                String _ext = iosMapName.substring(iosMapName.lastIndexOf('.'));
                iosMapName =System.currentTimeMillis() + "_"
                    + mapMngModel.getFloor() + _ext;
                mapMngModel.setzIosMap(iosMapName);
                Logger.getLogger(MapController.class).debug(iosMapName);
                File targetFile = new File(path, iosMapName);
                if (!targetFile.exists())
                {
                    targetFile.mkdirs();
                }
                // 修改
                try
                {
                    iosMap.transferTo(targetFile);
                }
                catch (Exception e)
                {
                    Logger.getLogger(MapController.class).info(e.getMessage());
                }
            }
            if (!zPathFileName.equals(nu))
            {
                String path = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/upload");
                String _ext = zPathFileName.substring(zPathFileName.lastIndexOf('.'));
                zPathFileName = System.currentTimeMillis() + "_"
                    + mapMngModel.getFloor() + _ext;
                mapMngModel.setzMapPathfile(zPathFileName);
                Logger.getLogger(MapController.class).debug(zPathFileName);
                File targetFile = new File(path, zPathFileName);
                if (!targetFile.exists())
                {
                    targetFile.mkdirs();
                }
                // 修改
                try
                {
                    zPathFile.transferTo(targetFile);
                }
                catch (Exception e)
                {
                    Logger.getLogger(MapController.class).info(e.getMessage());
                }
            }
            if (!fMapName.equals(nu))
            {
                String path = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/upload");
                String _ext = fMapName.substring(fMapName.lastIndexOf('.'));
                fMapName = System.currentTimeMillis() + "_"
                    + mapMngModel.getFloor() + _ext;
                mapMngModel.setfMap(fMapName);
                Logger.getLogger(MapController.class).debug(fMapName);
                File targetFile = new File(path, fMapName);
                if (!targetFile.exists())
                {
                    targetFile.mkdirs();
                }
                // 修改
                try
                {
                    fMap.transferTo(targetFile);
                }
                catch (Exception e)
                {
                    Logger.getLogger(MapController.class).info(e.getMessage());
                }
            }

            if (!fileName.equals(nu))
            {
                dao.updateMapData(mapMngModel);
            }else
            {
                dao.updateMapDataNoPath(mapMngModel); 
            }
            return "redirect:/home/showMapMng";
        }
        else
        {
            Logger.getLogger(MapController.class).debug(mapMngModel.getFloor());
            String path = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/upload");
            saveFile(mapMngModel, file, svg, path, nu, route,pathFile,zMap,fMap,zPathFile,iosMap);
            log.info(ip+"insert mapData:floorNo="+mapMngModel.getFloorNo());
            return "redirect:/home/showMapMng";
        }

    }

    private void getFile(File targetFile1)
    {
        if (!targetFile1.exists())
        {
            targetFile1.mkdirs();
        }
    }

    private void saveFile(MapMngModel mapMngModel, MultipartFile file,
            MultipartFile svg, String path, String nu, MultipartFile route,MultipartFile pathFile,
            MultipartFile zMap,MultipartFile fMap,MultipartFile zPathFile,MultipartFile iosMap)
    {
        String fileName = file.getOriginalFilename();
        String svgName = svg.getOriginalFilename();
        String routeName = route.getOriginalFilename();
        String pathfileName = pathFile.getOriginalFilename();
        String fMapName = fMap.getOriginalFilename();
        String zMapName = zMap.getOriginalFilename();
        String zPathFileName = zPathFile.getOriginalFilename();
        String iosMapName = iosMap.getOriginalFilename();
        String _ext = null;
        String _ext1 = null;
        String _ext2 = null;
        String _ext3 = null;
        String _ext4 = null;
        String _ext5 = null;
        String _ext6 = null;
        String _ext7 = null;
        // 保存
        try
        {
           
        if (fileName != nu)
        {
            _ext = fileName.substring(fileName.lastIndexOf('.'));
            fileName = mapMngModel.getFloorNo() + "_" + mapMngModel.getFloor()
            + _ext;
            mapMngModel.setPath(fileName);
            File targetFile = new File(path, fileName);
            getFile(targetFile);
            BufferedImage sourceImg = javax.imageio.ImageIO.read(file
                .getInputStream());
            mapMngModel.setImgWidth(sourceImg.getWidth());
            mapMngModel.setImgHeight(sourceImg.getHeight());
            file.transferTo(targetFile);

        }
        if (routeName != nu)
        {
            _ext2 = routeName.substring(routeName.lastIndexOf('.'));
            routeName = mapMngModel.getFloorNo() + "_" + mapMngModel.getFloor()
            + _ext2;
            mapMngModel.setRoute(routeName);
            File targetFile2 = new File(path, routeName);
            getFile(targetFile2);
            route.transferTo(targetFile2);

        }
        if (svgName != nu)
        {
            _ext1 = svgName.substring(svgName.lastIndexOf('.'));
            svgName = mapMngModel.getFloorNo() + "_" + mapMngModel.getFloor()
            + _ext1;
            mapMngModel.setSvg(svgName);
            File targetFile1 = new File(path, svgName);
            getFile(targetFile1);
            svg.transferTo(targetFile1);

        }
        if (pathfileName != nu)
        {
            _ext3 = pathfileName.substring(pathfileName.lastIndexOf('.'));
            pathfileName = mapMngModel.getUpdateTime() + "_" + mapMngModel.getFloor()
            + _ext3;
            mapMngModel.setPathFile(pathfileName);
            File targetFile3 = new File(path, pathfileName);
            getFile(targetFile3);
            pathFile.transferTo(targetFile3);
            
        }
        if (zMapName != nu)
        {
            _ext4 = zMapName.substring(zMapName.lastIndexOf('.'));
            zMapName = System.currentTimeMillis() + "_" + mapMngModel.getFloor()
            + _ext4;
            mapMngModel.setzMap(zMapName);
            File targetFile3 = new File(path, zMapName);
            getFile(targetFile3);
            zMap.transferTo(targetFile3);
            
        }
        if (fMapName != nu)
        {
            _ext5 = fMapName.substring(fMapName.lastIndexOf('.'));
            fMapName = System.currentTimeMillis() + "_" + mapMngModel.getFloor()
            + _ext5;
            mapMngModel.setfMap(fMapName);
            File targetFile3 = new File(path, fMapName);
            getFile(targetFile3);
            fMap.transferTo(targetFile3);
            
        }
        if (zPathFileName != nu)
        {
            _ext6 = zPathFileName.substring(zPathFileName.lastIndexOf('.'));
            zPathFileName = System.currentTimeMillis() + "_" + mapMngModel.getFloor()
            + _ext6;
            mapMngModel.setzMapPathfile(zPathFileName);
            File targetFile3 = new File(path, zPathFileName);
            getFile(targetFile3);
            zPathFile.transferTo(targetFile3);
            
        }
        if (iosMapName != nu)
        {
            _ext7 = iosMapName.substring(iosMapName.lastIndexOf('.'));
            iosMapName = System.currentTimeMillis() + "_" + mapMngModel.getFloor()
            + _ext7;
            mapMngModel.setzIosMap(iosMapName);
            File targetFile3 = new File(path, iosMapName);
            getFile(targetFile3);
            iosMap.transferTo(targetFile3);
            
        }

            // svg.transferTo(targetFile1);
            dao.saveMapInfo(mapMngModel);

        }
        catch (Exception e)
        {
            Logger.getLogger(MapController.class).info(e.getMessage());
        }

    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex)
    {
        String info = "";
        if (ex instanceof MaxUploadSizeExceededException)
        {
            info = "Max";
        }
        else
        {
            info = "未知错误: " + ex.getMessage();
        }
        ModelAndView model = new ModelAndView("redirect:/home/showMapMng");
        model.addObject("info", info);
        return model;

    }

    @RequestMapping(value = "/api/check", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> check(Model model,
            @RequestParam("id") String id, @RequestParam("place") String place,
            @RequestParam("floor") String floor,
            @RequestParam("floorNo") String floorId)
    {
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        String nu = "";
        int floorid = Integer.parseInt(floorId);
        int floorNo = 0;
        int placeId = Integer.parseInt(place);
        if (floorid > 0)
        {
            floorNo = 10000 * placeId + floorid;
        }
        else
        {
            floorNo = 10000 * placeId + 5000 - floorid;
        }

        if (id == nu)
        {
            return check1(place, floor, floorNo, modelMap);
        }
        else
        {
            return check2(id, place, floorNo, floor, modelMap);
        }
    }

    private Map<String, Object> check2(String id, String place, int floorNo,
            String floor, Map<String, Object> modelMap)
    {
        int i = 0;
        int j = 0;
        i = dao.chekByFloorNo1(floorNo, id);
        if (i > 0)
        {
            modelMap.put("data", false);
            modelMap.put("same", "0");
            return modelMap;
        }
        j = dao.checkByPlace1(place, floor, id);
        if (j > 0)
        {
            modelMap.put("data", false);
            modelMap.put("same", "1");
            return modelMap;
        }
        else
        {
            modelMap.put("data", true);
            return modelMap;
        }
    }

    private Map<String, Object> check1(String place, String floor, int floorNo,
            Map<String, Object> modelMap)
    {
        int i = 0;
        int j = 0;
        i = dao.chekByFloorNo(floorNo);
        if (i > 0)
        {
            modelMap.put("data", false);
            modelMap.put("same", "0");
            return modelMap;
        }
        j = dao.checkByPlace(place, floor);
        if (j > 0)
        {
            modelMap.put("data", false);
            modelMap.put("same", "1");
            return modelMap;
        }
        else
        {
            modelMap.put("data", true);
            return modelMap;
        }
    }

}
