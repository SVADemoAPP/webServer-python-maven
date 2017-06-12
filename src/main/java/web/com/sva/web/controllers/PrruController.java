package com.sva.web.controllers;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sva.common.XmlParser;
import com.sva.dao.MapsDao;
import com.sva.dao.PrruDao;
import com.sva.dao.PrruSignalDao;
import com.sva.model.MapsModel;
import com.sva.model.PrruFeatureDetailModel;
import com.sva.model.PrruFeatureModel;
import com.sva.model.PrruModel;

@Controller
@RequestMapping(value = "/pRRU")
public class PrruController {
	private static Logger logger = Logger.getLogger(PrruController.class);

	private static final double MINRSRP = -1300;

	private static final double FISTRSRP = -1000;

	private static final String SIMULATE = "simulate";
	
	private static final BigDecimal ZERO = new BigDecimal(0);
	@Autowired
	private PrruDao dao;

	@Autowired
	private MapsDao mapDao;

	@Autowired
	private PrruSignalDao prruDao;

	@RequestMapping(value = "/api/getData", method = { RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> getTableData(HttpServletRequest request, Model model) {
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		Object userName = request.getSession().getAttribute("username");
		Collection<MapsModel> store = new ArrayList<MapsModel>(10);
		@SuppressWarnings("unchecked")
		List<String> storeides = (List<String>) request.getSession().getAttribute("storeides");
		Collection<MapsModel> c = new ArrayList<MapsModel>(10);
		if ("admin".equals(userName)) {
			c = mapDao.getMapDataFromPrru();
		} else {
			// storeides = accountDao.selectStore(userName);
			if (storeides != null && storeides.size() > 0) {
				String storeid = storeides.get(0);
				String[] stores = storeid.split(",");
				for (int i = 0; i < stores.length; i++) {
					store = mapDao.getMapDataFromPrruByStoreid(Integer.parseInt(stores[i]));
					if (store != null) {
						if (c == null) {
							c = store;
						} else if (!store.isEmpty()) {
							c.addAll(store);
						}
					}
				}
			}
		}

		modelMap.put("error", null);
		modelMap.put("data", c);
		return modelMap;
	}

	@RequestMapping(value = "/api/getPrruInfo", method = { RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> getPrruInfo(Model model, @RequestParam("floorNo") String floorNo) {
		Collection<PrruModel> ResultList = dao.getPrruInfoByflooNo(floorNo,"","");
		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		modelMap.put("error", null);
		modelMap.put("data", ResultList);
		return modelMap;
	}

	/**
	 * 2016年10月31日，新增模拟采集点计算逻辑 fwx219758
	 * 
	 * @param file
	 * @param request
	 * @param msgMngModel
	 * @param model
	 * @param floorNo
	 * @param defaultDistance
	 *            2016年10月31日新增参数 默认采集点间隔距离
	 * @param kValue
	 *            2016年10月31日新增参数，K值
	 * @return
	 */
	@RequestMapping(value = "/api/saveData")
	public String saveData(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request, PrruModel msgMngModel, ModelMap model,
			@RequestParam("floor") String floorNo, @RequestParam("defaultDistance") String defaultDistance,
			@RequestParam("kValue") String kValue, @RequestParam("height") String height,
			@RequestParam("infiltration") String infiltration) {
	    logger.debug("in");
		String fileName = file.getOriginalFilename();
		String path = request.getSession().getServletContext().getRealPath("/WEB-INF/upload");
		File targetFile = new File(path, fileName);
		List<PrruModel> str = null;
		File f = null;
		String eNodeBid = "";
		logger.debug("fileName"+fileName);
		if (!fileName.isEmpty()) {
		    logger.debug("if2");
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			try {
				file.transferTo(targetFile);

			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			f = new File(path + '/' + fileName);
			try {
			    logger.debug("try parse");
				XmlParser xml = new XmlParser();
				xml.init(f);
				str = xml.getAttrVal(floorNo, msgMngModel.getPlaceId(), "//Project/Floors/Floor/NEs/NE", "id",
						"name", "x", "y", "neCode", "cellId");
				eNodeBid = str.get(0).geteNodeBid();
				int st = str.size();
				if(st > 0){
					dao.deleteInfo(msgMngModel.getFloorNo(),eNodeBid);
					logger.debug("delete OK");
				}
				for (int i = 0; i < st; i++) {
					dao.saveInfo(str.get(i));
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

		} else {
		    logger.debug("else2");
			try {
				dao.updateInfo(msgMngModel.getFloorNo(), floorNo);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}

		// 根据楼层号从数据库查询地图信息
		Collection<MapsModel> maps = mapDao.getMapDetail(floorNo);
		if (maps.isEmpty() || eNodeBid.isEmpty()) {
			logger.error("没有查到地图数据或没有加载prru文件");
			return "redirect:/home/showpRRU";
		}
		List<PrruModel> prruInfo = dao.getPrruInfo(floorNo,eNodeBid);
		if (prruInfo.isEmpty()) {
			logger.error("没有查到Prru信息");
			return "redirect:/home/showpRRU";
		}
	
		// 删除掉该楼层之前模拟SRS计算的结果，保证数据唯一性
		prruDao.delSimulateDataByFloorNo(floorNo,eNodeBid);
		// 根据地图长、宽、原点及模拟点间隔等信息遍历获取模拟点
		List<PrruFeatureModel> prruFeatureModelList = getPointList(maps.iterator().next(), defaultDistance, eNodeBid);
		for(PrruModel prruModel : prruInfo){
			calculation(prruFeatureModelList,floorNo,prruModel.geteNodeBid(),prruModel.getCellId(),defaultDistance,kValue,height,infiltration);
		}
		return "redirect:/home/showpRRU";
	}
	
	private void calculation(final List<PrruFeatureModel> prruFeatureModelList,String floorNo, String eNodeBid, String cellId,final String defaultDistance, 
			final String kValue, final String height, final String infiltration){
		final List<PrruModel> prruModelList = (List<PrruModel>) dao.getPrruInfoByflooNo(floorNo,eNodeBid,cellId);

		if(prruModelList.isEmpty()){
			return;
		}

		// 计算过程较长，用线程处理，提升用户体验
		Thread calDistanceThread = new Thread(new Runnable() {

			@Override
			public void run() {
				// 计算并存储结果
				calDistance(prruFeatureModelList, prruModelList, kValue, height, infiltration);

			}
		});

		calDistanceThread.start();
	}

	/**
	 * 根据prru坐标计算与每个模拟点的距离
	 * 
	 * @param prruFeatureModelList
	 * @param prruList
	 */
	private void calDistance(List<PrruFeatureModel> prruFeatureModelList, List<PrruModel> prruList,
			String kValue, String height, String infiltration) {
		BigDecimal x;
		BigDecimal y;
		BigDecimal z = new BigDecimal(height);
		BigDecimal i = new BigDecimal(infiltration);
		BigDecimal d;
		boolean flag = false;
		double distance;
		double distanceRange = Math.sqrt(z.multiply(z).add(i.multiply(i)).doubleValue());
		List<PrruFeatureDetailModel> prruFeatureList = new ArrayList<PrruFeatureDetailModel>();
		PrruFeatureDetailModel prruFeatureDetail;
		for (PrruFeatureModel prruFeatureModel : prruFeatureModelList) {
			for (PrruModel prruModel : prruList) {
				// 计算距离x² + y² + z²= d²
				x = new BigDecimal(prruFeatureModel.getX()).subtract(new BigDecimal(prruModel.getX()));
				y = new BigDecimal(prruFeatureModel.getY()).subtract(new BigDecimal(prruModel.getY()));
				d = (x.multiply(x)).add(y.multiply(y)).add(z.multiply(z));
				distance = Math.sqrt(d.doubleValue());
				prruFeatureDetail = new PrruFeatureDetailModel();
				prruFeatureDetail.setGpp(prruModel.getNeCode());
				prruFeatureDetail.setDistance(distance);
				prruFeatureList.add(prruFeatureDetail);
				if(distance < distanceRange){
					flag = true;
				}
				
			}
			// 计算模拟RSRP
			if (!prruFeatureList.isEmpty() && flag) {
				calRSRP(prruFeatureList, kValue, prruFeatureModel);
			}
			prruFeatureList.clear();
			flag = false;

		}
		System.out.println("模拟点计算完成");
	}

	/**
	 * 根据距离排序后计算rsrp
	 * 
	 * @param prruFeatureList
	 * @return
	 */
	private void calRSRP(List<PrruFeatureDetailModel> prruFeatureList, String kValue,
			PrruFeatureModel prruFeatureModel) {
		// 按照prru与特征点距离进行升序排序
		Collections.sort(prruFeatureList, new Comparator<PrruFeatureDetailModel>() {
			@Override
			public int compare(PrruFeatureDetailModel o1, PrruFeatureDetailModel o2) {
				if(o1.getDistance() > o2.getDistance() ){
					return 1;
				}else if(o1.getDistance() < o2.getDistance() ){
					return -1;
				}else{
					return 0;
				}
			}
		});
		
		if(prruFeatureList.size() > 10){
			prruFeatureList = prruFeatureList.subList(0, 10);
		}

		double minDistance = prruFeatureList.get(0).getDistance();
		double rsrp;
		double totalRsrp = 0.0;
		double checkValue = 0.0;
		int count = 0;
		// RSRP算法 srs1 = srs0 - K * lg(d1/d0) 三维空间，不考虑d0为零的情况
		for (PrruFeatureDetailModel featureDetail : prruFeatureList) {
			rsrp = FISTRSRP - Integer.parseInt(kValue) * Math.log10(featureDetail.getDistance() / minDistance);

			// 将计算出的Rsrp值小于-1400的赋值-1400 按照之后的逻辑，此处逻辑没有意义
			// rsrp = rsrp > DEFALUTRSRP ? rsrp : DEFALUTRSRP;

			if (rsrp > MINRSRP) {
				totalRsrp += rsrp;
				count++;
			}
			featureDetail.setFeatureValue(new BigDecimal(rsrp));
		}

		// count是大于1的，不作异常判断
		checkValue = totalRsrp / count;
		prruFeatureModel.setCheckValue(new BigDecimal(checkValue));
		prruFeatureModel.setId(prruDao.savePrruFeature(prruFeatureModel));

		// 循环添加关联表ID
		for (PrruFeatureDetailModel featureDetail : prruFeatureList) {
			featureDetail.setFeatureid(prruFeatureModel.getId());
			
			//2017年1月4日新增逻辑
			featureDetail.setFeatureValue(featureDetail.getFeatureValue().subtract(new BigDecimal(checkValue)));
			
			logger.debug("prruFeatureModel:" + prruFeatureModel.getId() + "gpp:" + featureDetail.getGpp() + ";Rsrp:" + featureDetail.getFeatureValue() + ";Distance:"
					+ featureDetail.getDistance());
		}

		prruDao.savePrruFeatureDetail(prruFeatureList);
	}

	/**
	 * 根据地图的高、宽等属性遍历出所有模拟点
	 * 
	 * @param map
	 * @return
	 */
	private List<PrruFeatureModel> getPointList(MapsModel map, String defaultDistance, String eNodeBid) {
		List<PrruFeatureModel> prruFeatureModelList = new ArrayList<PrruFeatureModel>();
		double defalutValue = Double.parseDouble(defaultDistance);
		PrruFeatureModel prruModel;
		String floorNo = map.getFloorNo().toString();
		double width = map.getImgWidth() / Double.parseDouble(map.getScale());
		double hight = map.getImgHeight() / Double.parseDouble(map.getScale());
		for (Double x = 0.0; x <= width; x += defalutValue) {
			for (Double y = 0.0; y <= hight; y += defalutValue) {
				prruModel = new PrruFeatureModel();
				
				prruModel.setX((x - Double.parseDouble(map.getXo())) + "");
				prruModel.setY((y - Double.parseDouble(map.getYo())) + "");
				
				prruModel.setFloorNo(floorNo);
				prruModel.setUserId(SIMULATE);
				prruModel.setTimestamp(System.currentTimeMillis());
				prruModel.setFeatureRadius(ZERO);
				prruModel.seteNodeBid(eNodeBid);
				prruFeatureModelList.add(prruModel);
			}
		}

		return prruFeatureModelList;
	}

	@RequestMapping(value = "/api/deleteData", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> deleteMsgData(Model model, @RequestParam("floorNo") String floorNo) {

		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		try {
			dao.deleteInfo(floorNo,"");

			// 删除掉该楼层之前模拟SRS计算的结果，保证数据唯一性
			prruDao.delSimulateDataByFloorNo(floorNo, "");
		} catch (Exception e) {
			Logger.getLogger(MessageController.class).info(e.getStackTrace());
		}
		return modelMap;
	}

	@RequestMapping(value = "/api/getSignal", method = { RequestMethod.GET })
	@ResponseBody
	public Map<String, Object> getSignal() {

		Map<String, Object> modelMap = new HashMap<String, Object>(2);

//		List<Map<String, Object>> res = dao.getSignal();
//
//		modelMap.put("data", res);

		return modelMap;
	}

	@RequestMapping(value = "/api/checkStore", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> checkStore(Model model, @RequestParam("id") String id,
			@RequestParam("floor") String floor) {

		Map<String, Object> modelMap = new HashMap<String, Object>(2);
		String nu = "";
		int i = 0;
		if (id == nu) {
			i = dao.getPrruInfoByflooNo(floor,"","").size();
			if (i > 0) {
				modelMap.put("data", false);
				return modelMap;
			} else {
				modelMap.put("data", true);
				return modelMap;
			}
		} else {
			i = dao.checkByFloorNo(floor, id);
			if (i > 0) {
				modelMap.put("data", false);
				return modelMap;
			} else {
				modelMap.put("data", true);
				return modelMap;
			}
		}
	}
}
