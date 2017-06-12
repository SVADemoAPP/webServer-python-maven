/**   
 * @Title: CollectThread.java 
 * @Package com.sva.service 
 * @Description: 采集特征值线程 
 * @author labelCS   
 * @date 2016年9月27日 下午2:56:58 
 * @version V1.0   
 */
package com.sva.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import com.sva.common.conf.GlobalConf;
import com.sva.dao.PrruSignalDao;
import com.sva.model.PrruFeatureDetailModel;
import com.sva.model.PrruFeatureModel;
import com.sva.model.PrruSignalModel;
import com.sva.web.models.PrruFeatureApiModel;

/** 
 * @ClassName: CollectThread 
 * @Description: 采集特征值线程 
 * @author labelCS 
 * @date 2016年9月27日 下午2:56:58 
 *  
 */
public class CollectThread extends Thread {
    /** 
     * @Fields log : 日志处理类 
     */ 
    private static final Logger LOG = Logger.getLogger(CollectThread.class);
    
    /** 
     * @Fields params : 客户端传入的参数bean
     */ 
    private PrruFeatureApiModel params;
    
    /** 
     * @Fields prruSignalDao : prru相关数据表操作句柄
     */ 
    private PrruSignalDao prruSignalDao;
    
    /** 
     * @Fields isStop : 判断线程是否被终止
     */ 
    private boolean isStop = false;
    
    /** 
     * @Fields DEFAULT_VALUE : 计算特征值时的默认值
     */ 
    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(-1400);
    /** 
     * @Fields THRESHOLD : 特征值的阈值
     */ 
    private static final BigDecimal THRESHOLD = new BigDecimal(-1300);
    
    public CollectThread(PrruFeatureApiModel bean, PrruSignalDao dao){
        this.params = bean;
        this.prruSignalDao = dao;
    }
    
    /** 
     * @Title: stopThread 
     * @Description: 中断线程
     */
    public void stopThread(){
        // 中断阻塞线程
        this.interrupt();
        // 中断正在运行的线程
        this.isStop = true;
    }
    
    @Override
    public void run(){
        // 开始之前将任务线程加入线程池
        GlobalConf.addPrruThreadPool(params.getUserId(), this);
        
        // 取得当前时间戳，作为任务起始时间
        long beginTimestamp = System.currentTimeMillis();
        // 计数器
        int counter = 0;
        // 遍历时间戳
        long lastTimestamp = 0L;
        try{
            // 任务挂起一段时间，等待prruSignal信息采集入库
            sleep(2*params.getLength()*1000);
            // 从数据库取出已收集到的prru信号信息
            List<PrruSignalModel> prruSignals = prruSignalDao.getSignalByUserIdTime(params.getUserId(), beginTimestamp);
            // 修改gpp的值,添加enbId
            editGpp(prruSignals);
            // 如果未找到prru信号数据，说明对接AE出现问题
            if(prruSignals.isEmpty()){
                LOG.error("未收到prru信号数据，请确定sva通道是否正常!");
                return;
            }
            // 进行格式转换
            List<Map<String,Object>> formatedList = formatPrruSignals(prruSignals,beginTimestamp);
            // 遍历数据，计算checkValue进行修正
            Iterator<Map<String,Object>> it = formatedList.iterator();
            while(it.hasNext())  
            {  
                Map<String, Object> entity = it.next();
                long timestampTmp = (long)entity.get("time");
                @SuppressWarnings("unchecked")
                Map<String, BigDecimal> tempData = (Map<String, BigDecimal>) entity.get("data");
                // 修正rsrp，获取Data
                editRsrp(tempData);
                counter++;
                lastTimestamp = timestampTmp;
            } 
            
            // 如果数据条数不够length，且运算时间未达到上限，继续获取信号数据，直至满足2个条件的任意一个
            while(counter<params.getLength() && 
                    (System.currentTimeMillis()-beginTimestamp)<params.getTimeInSeconds()*1000 && !isStop){
                // 每取一条，等待2s，等待信号数据入库
                sleep(2*1000);
                List<PrruSignalModel> onePrruSignal = 
                        prruSignalDao.getOneSignalByUserIdTime(params.getUserId(), lastTimestamp);
                // 修改gpp的值,添加enbId
                editGpp(onePrruSignal);
                // 如果未取到数据，说明信号数据未入库，再等待2s
                if(!onePrruSignal.isEmpty()){
                    // 进行格式转换
                    List<Map<String, Object>> formatedMapOne = formatPrruSignals(onePrruSignal,beginTimestamp);
                    Map<String, Object> tempMap = formatedMapOne.get(0);
                    long timestampTmp = (long)tempMap.get("time");
                    @SuppressWarnings("unchecked")
                    Map<String, BigDecimal> tempData = (Map<String, BigDecimal>) tempMap.get("data");
                    // 修正rsrp，获取Data
                    editRsrp(tempData);
                    // 加入计算list
                    formatedList.add(tempMap);
                    counter++;
                    lastTimestamp = timestampTmp;
                }
            }
            
            // 如果线程未被终止，执行特征值解析入库操作
            if(!isStop){
                saveFeautreValue(formatedList, beginTimestamp);
            }
        }catch(InterruptedException e){
            // 线程中断
            LOG.info(e);            
        }catch(Exception e){
            LOG.error(e);
        }finally{
            // 任务线程最终要从线程池中移除
            GlobalConf.removePrruThreadPool(params.getUserId());
        }
    }
    
    public void runBak(){
        // 开始之前将任务线程加入线程池
        GlobalConf.addPrruThreadPool(params.getUserId(), this);
        
        // 取得当前时间戳，作为任务起始时间
        long beginTimestamp = System.currentTimeMillis();
        // 计数器
        int counter = 0;
        // 遍历时间戳
        long lastTimestamp = 0L;
        try{
            // 任务挂起一段时间，等待prruSignal信息采集入库
            sleep(2*params.getLength()*1000);
            // 从数据库取出已收集到的prru信号信息
            List<PrruSignalModel> prruSignals = prruSignalDao.getSignalByUserIdTime(params.getUserId(), beginTimestamp);
            // 如果未找到prru信号数据，说明对接AE出现问题
            if(prruSignals.isEmpty()){
                LOG.error("未收到prru信号数据，请确定sva通道是否正常!");
                return;
            }
            // 进行格式转换
            List<Map<String,Object>> formatedList = formatPrruSignals(prruSignals,beginTimestamp);
            // 遍历数据，计算dataCheckValue
            Iterator<Map<String,Object>> it = formatedList.iterator();
            while(it.hasNext())  
            {  
                Map<String, Object> entity = it.next();
                long timestampTmp = (long)entity.get("time");
                @SuppressWarnings("unchecked")
                Map<String, BigDecimal> tempData = (Map<String, BigDecimal>) entity.get("data");
                // 如果信号数据有效，计算dataCheckValue，并写入计算list，计数器+1，更新最后一次时间戳
                if(checkSignalIsValid(tempData)){
                    BigDecimal dataCheckValue = calcDataCheckValue(tempData);
                    entity.put("dataCheckValue", dataCheckValue);
                    counter++;
                }
                // 否则，删除该元素
                else
                {
                    it.remove();
                }
                lastTimestamp = timestampTmp;
            } 
            
            // 如果数据条数不够length，且运算时间未达到上限，继续获取信号数据，直至满足2个条件的任意一个
            while(counter<params.getLength() && 
                    (System.currentTimeMillis()-beginTimestamp)<params.getTimeInSeconds()*1000 && !isStop){
                // 每取一条，等待2s，等待信号数据入库
                sleep(2*1000);
                List<PrruSignalModel> onePrruSignal = 
                        prruSignalDao.getOneSignalByUserIdTime(params.getUserId(), lastTimestamp);
                // 如果未取到数据，说明信号数据未入库，再等待2s
                if(!onePrruSignal.isEmpty()){
                    // 进行格式转换
                    List<Map<String, Object>> formatedMapOne = formatPrruSignals(onePrruSignal,beginTimestamp);
                    Map<String, Object> tempMap = formatedMapOne.get(0);
                    long timestampTmp = (long)tempMap.get("time");
                    @SuppressWarnings("unchecked")
                    Map<String, BigDecimal> tempData = (Map<String, BigDecimal>) tempMap.get("data");
                    if(checkSignalIsValid(tempData)){
                        BigDecimal dataCheckValue = calcDataCheckValue(tempData);
                        tempMap.put("dataCheckValue", dataCheckValue);
                        // 加入计算list
                        formatedList.add(tempMap);
                        counter++;
                    }
                    lastTimestamp = timestampTmp;
                }
            }
            
            // 如果线程未被终止，执行特征值解析入库操作
            if(!isStop){
                saveFeautreValue(formatedList, beginTimestamp);
            }
        }catch(InterruptedException e){
            // 线程中断
            LOG.info(e);            
        }catch(Exception e){
            LOG.error(e);
        }finally{
            // 任务线程最终要从线程池中移除
            GlobalConf.removePrruThreadPool(params.getUserId());
        }
    }
    
    /** 
     * @Title: formatPrruSignals 
     * @Description: 格式转换，便于接下来计算
     * @param datas 源数据格式
     * @return 
     */
    private List<Map<String, Object>> formatPrruSignals(List<PrruSignalModel> datas,long beginTimestamp){
        // 结果
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        // 过度数据格式
        Map<Long,Map<String,BigDecimal>> tempFormat = new LinkedHashMap<Long,Map<String,BigDecimal>>();
        // 遍历数据
        for(PrruSignalModel p: datas){
            LOG.fatal("pruu data:"+p.toString()+",beginTimestamp:"+beginTimestamp);
            // 柜框槽号
            String gpp = p.getGpp();
            // 信号值
            BigDecimal rsrp = p.getRsrp();
            // 时间戳
            long timestamp = p.getTimestamp();
            // 如果结果map里已有该时间戳的数据，则将信号数据添加仅map里
            if(tempFormat.containsKey(timestamp)){
                Map<String,BigDecimal> temp = tempFormat.get(timestamp);
                temp.put(gpp, rsrp);
            }
            // 否则，将时间戳作为key，信号map作为value，加入结果map里
            else
            {
                Map<String,BigDecimal> temp = new HashMap<String,BigDecimal>();
                temp.put(gpp, rsrp);
                tempFormat.put(timestamp, temp);
            }
        }
        
        // 遍历map，转换为list
        Iterator<Entry<Long, Map<String, BigDecimal>>> it = tempFormat.entrySet().iterator();  
        while(it.hasNext())  
        {
            Map.Entry<Long, Map<String, BigDecimal>> entity = (Entry<Long, Map<String, BigDecimal>>) it.next();
            long timestampLoop = entity.getKey();
            Map<String, BigDecimal> dataLoop = entity.getValue();
            Map<String, Object> tempLoop = new HashMap<String, Object>();
            tempLoop.put("time", timestampLoop);
            tempLoop.put("data", dataLoop);
            result.add(tempLoop);
        }
        
        return result;
    }
    
    /** 
     * @Title: calcDataCheckValue 
     * @Description: 计算一条信号数据的dataCheckValue 
     * @param datas 计算的对象
     * @return 
     */
    private BigDecimal calcDataCheckValue(Map<String, BigDecimal> datas){
        // 结果
        BigDecimal result = new BigDecimal(0);
        // 计数器
        int count = 0;
        // 对于datas的每一个数据
        Iterator<Entry<String, BigDecimal>> it = datas.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry<String, BigDecimal> entity = (Entry<String, BigDecimal>) it.next();
            BigDecimal item = entity.getValue();
            // 如果大于阈值，求和，计数器加1
            if(item.compareTo(THRESHOLD) > 0){
                result = result.add(item);
                count++;
            }
        }
        // 如果有效值的个数不为0
        if(count > 0){
            result = result.divide(new BigDecimal(count),2);
        }
        
        return result;
    }
    
    /** 
     * @Title: checkSignalIsValid 
     * @Description: 判断信号数据是否有效，如果全部<-1300，则无效 
     * @param datas 判断对象
     * @return 
     */
    private boolean checkSignalIsValid(Map<String, BigDecimal> datas){
        // 结果
        boolean result = false;
        
        // 对于datas的每一个数据
        Iterator<Entry<String, BigDecimal>> it = datas.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry<String, BigDecimal> entity = (Entry<String, BigDecimal>) it.next();
            BigDecimal item = entity.getValue();
            // 如果大于默认值，该数据有效，推出判断
            if(item.compareTo(THRESHOLD) > 0){
                result = true;
                break;
            }
        }
        
        return result;
    }
    
    /** 
     * @Title: saveFeautreValue 
     * @Description: 计算特征值并入库
     * @param datas 
     * @param timestamp
     */
    private void saveFeautreValue(List<Map<String, Object>> datas, long timestamp){
        // 如果数据list为空，说明所有数据均无效，需要重测
        if(datas.isEmpty()){
            LOG.error("未找到有效的prru信号数据");
            return;
        }
        
        // 计算特征值
        Map<String, BigDecimal> featureValueMap = calcFeautreValue(datas);
        // 计算特征半径
        BigDecimal featureRadius = calcFeatureRadius(datas,featureValueMap);
        // 数据入库
        PrruFeatureModel featureModel = new PrruFeatureModel();
        featureModel.setFeatureRadius(featureRadius);
        featureModel.setX(params.getX().toString());
        featureModel.setY(params.getY().toString());
        featureModel.setFloorNo(params.getFloorNo().toString());
        featureModel.setTimestamp(timestamp);
        featureModel.setUserId(params.getUserId());
        int id = prruSignalDao.savePrruFeature(featureModel);
        // 生成特征值bean，然后入库
        List<PrruFeatureDetailModel> detailList = generatePrruFeatureDetail(featureValueMap,id);
        prruSignalDao.savePrruFeatureDetail(detailList);
        
    }
    
    /** 
     * @Title: saveFeautreValue 
     * @Description: 计算特征值并入库
     * @param datas 
     * @param timestamp
     */
    private void saveFeautreValueBak(List<Map<String, Object>> datas, long timestamp){
        // 如果数据list为空，说明所有数据均无效，需要重测
        if(datas.isEmpty()){
            LOG.error("未找到有效的prru信号数据");
            return;
        }
        
        // 计算特征值
        Map<String, BigDecimal> featureValueMap = calcFeautreValue(datas);
        // 计算校验值
        BigDecimal checkValue = calcCheckValue(featureValueMap);
        // 计算特征半径
        BigDecimal featureRadius = calcFeatureRadiusBak(datas,featureValueMap,checkValue);
        // 数据入库
        PrruFeatureModel featureModel = new PrruFeatureModel();
        featureModel.setCheckValue(checkValue);
        featureModel.setFeatureRadius(featureRadius);
        featureModel.setX(params.getX().toString());
        featureModel.setY(params.getY().toString());
        featureModel.setFloorNo(params.getFloorNo().toString());
        featureModel.setTimestamp(timestamp);
        featureModel.setUserId(params.getUserId());
        int id = prruSignalDao.savePrruFeature(featureModel);
        // 生成特征值bean，然后入库
        List<PrruFeatureDetailModel> detailList = generatePrruFeatureDetail(featureValueMap,id);
        prruSignalDao.savePrruFeatureDetail(detailList);
        
    }
    
    /** 
     * @Title: generatePrruFeatureDetail 
     * @Description: 生成用于插入数据库的bean 
     * @param featureValues
     * @param featureId
     * @return 
     */
    private List<PrruFeatureDetailModel> generatePrruFeatureDetail(Map<String, BigDecimal> featureValues, 
            int featureId){
        // 结果
        List<PrruFeatureDetailModel> list = new ArrayList<PrruFeatureDetailModel>();
        // 遍历特征值，生成bean
        Iterator<Entry<String, BigDecimal>> it = featureValues.entrySet().iterator();  
        while(it.hasNext()){
            Entry<String,BigDecimal> entity = it.next();
            String gpp = entity.getKey();
            BigDecimal featureValue = entity.getValue();
            PrruFeatureDetailModel temp = new PrruFeatureDetailModel();
            temp.setFeatureid(featureId);
            temp.setFeatureValue(featureValue);
            temp.setGpp(gpp);
            list.add(temp);
        }
        
        return list;
    }
    
    /** 
     * @Title: calcFeautreValue 
     * @Description: 计算特征值
     * @param datas 
     */
    private Map<String, BigDecimal> calcFeautreValue(List<Map<String, Object>> datas){
        // 存放各个prru对应的特征值
        Map<String, BigDecimal> featureValueMap = new HashMap<String, BigDecimal>();
        // 存放各个prru对应的有效rsrp的个数
        Map<String, Integer> featureCount = new HashMap<String, Integer>();
        // 遍历数据，统计特征值
        for(Map<String,Object> item : datas){
            @SuppressWarnings("unchecked")
            Map<String, BigDecimal> rsrps = (Map<String, BigDecimal>) item.get("data");
            Iterator<Entry<String, BigDecimal>> it = rsrps.entrySet().iterator();  
            while(it.hasNext()){
                Entry<String,BigDecimal> entity = it.next();
                String gpp = entity.getKey();
                BigDecimal rsrp = entity.getValue();
                // 已有该gpp的数据则执行相加
                if(featureValueMap.get(gpp)!=null ){
                    featureValueMap.put(gpp, featureValueMap.get(gpp).add(rsrp));
                    featureCount.put(gpp, featureCount.get(gpp)+1);
                }
                // 否则初始化该gpp的数据
                else{
                    featureValueMap.put(gpp, rsrp);
                    featureCount.put(gpp, 1);
                }
            }
        }
        
        // 取平均，生成特征值
        Iterator<Entry<String, BigDecimal>> itFeature = featureValueMap.entrySet().iterator();  
        while(itFeature.hasNext()){
            Entry<String,BigDecimal> entity = itFeature.next();
            String gpp = entity.getKey();
            BigDecimal rsrp = entity.getValue();
            int count = featureCount.get(gpp);
            BigDecimal featureValue = rsrp.divide(new BigDecimal(count),2);
            featureValueMap.put(gpp,featureValue);
        }
        
        return featureValueMap;
    }
    
    /** 
     * @Title: calcFeautreValue 
     * @Description: 计算特征值
     * @param datas 
     */
    private Map<String, BigDecimal> calcFeautreValueBak(List<Map<String, Object>> datas){
        // 存放各个prru对应的特征值
        Map<String, BigDecimal> featureValueMap = new HashMap<String, BigDecimal>();
        // 存放各个prru对应的有效rsrp的个数
        Map<String, Integer> featureCount = new HashMap<String, Integer>();
        // 遍历数据，统计特征值
        for(Map<String,Object> item : datas){
            @SuppressWarnings("unchecked")
            Map<String, BigDecimal> rsrps = (Map<String, BigDecimal>) item.get("data");
            Iterator<Entry<String, BigDecimal>> it = rsrps.entrySet().iterator();  
            while(it.hasNext()){
                Entry<String,BigDecimal> entity = it.next();
                String gpp = entity.getKey();
                BigDecimal rsrp = entity.getValue();
                // 略去无效的数据
                if(rsrp.compareTo(DEFAULT_VALUE)<=0){
                    continue;
                }
                // 已有该gpp的数据则执行相加
                if(featureValueMap.get(gpp)!=null ){
                    featureValueMap.put(gpp, featureValueMap.get(gpp).add(rsrp));
                    featureCount.put(gpp, featureCount.get(gpp)+1);
                }
                // 否则初始化该gpp的数据
                else{
                    featureValueMap.put(gpp, rsrp);
                    featureCount.put(gpp, 1);
                }
            }
        }
        
        // 取平均，生成特征值
        Iterator<Entry<String, BigDecimal>> itFeature = featureValueMap.entrySet().iterator();  
        while(itFeature.hasNext()){
            Entry<String,BigDecimal> entity = itFeature.next();
            String gpp = entity.getKey();
            BigDecimal rsrp = entity.getValue();
            int count = featureCount.get(gpp);
            BigDecimal featureValue = rsrp.divide(new BigDecimal(count),2);
            featureValueMap.put(gpp,featureValue);
        }
        
        return featureValueMap;
    }
    
    /** 
     * @Title: calcCheckValue 
     * @Description: 计算checkValue
     * @param featureValues：特征值
     * @return 
     */
    private BigDecimal calcCheckValue(Map<String, BigDecimal> featureValues){
     // 取平均，生成特征值，并统计checkValue
        BigDecimal checkValue = new BigDecimal(0);
        int checkCount = 0;
        Iterator<Entry<String, BigDecimal>> it = featureValues.entrySet().iterator();  
        while(it.hasNext()){
            Entry<String,BigDecimal> entity = it.next();
            BigDecimal featureValue = entity.getValue();
            // 判断对于计算checkValue是否有效
            if(featureValue.compareTo(THRESHOLD)>0){
                checkValue = checkValue.add(featureValue);
                checkCount++;
            }
        }
        
        // 计算checkValue
        if(checkCount != 0){
            checkValue = checkValue.divide(new BigDecimal(checkCount),2);
        }
        
        return checkValue;
    }
    
    /** 
     * @Title: calcFeatureRadius 
     * @Description: 计算特征半径
     * @param datas rsrp：原始数据
     * @param featureValues：特征值
     * @param checkValue：校验值
     * @return 
     */
    private BigDecimal calcFeatureRadius(List<Map<String, Object>> datas, 
            Map<String, BigDecimal> featureValues){
        // rsrp距离
        BigDecimal rsrpDistance = new BigDecimal(0);
        // 遍历原始数据，套用公式
        for(Map<String, Object> item : datas){
            @SuppressWarnings("unchecked")
            Map<String, BigDecimal> rsrps = (Map<String, BigDecimal>) item.get("data");
            Iterator<Entry<String, BigDecimal>> it = rsrps.entrySet().iterator();  
            while(it.hasNext()){
                Entry<String,BigDecimal> entity = it.next();
                String gpp = entity.getKey();
                BigDecimal rsrp = entity.getValue();
                BigDecimal featureValue = featureValues.get(gpp);
                // (data-featureValue)^2
                BigDecimal temp = (rsrp.subtract(featureValue)).pow(2);
                rsrpDistance = rsrpDistance.add(temp);
            }
        }
        // 返回特征半径
        return rsrpDistance.divide(new BigDecimal(datas.size()),2);
        
    }
    
    /** 
     * @Title: calcFeatureRadius 
     * @Description: 计算特征半径
     * @param datas rsrp：原始数据
     * @param featureValues：特征值
     * @param checkValue：校验值
     * @return 
     */
    private BigDecimal calcFeatureRadiusBak(List<Map<String, Object>> datas, 
            Map<String, BigDecimal> featureValues, 
            BigDecimal checkValue){
        // rsrp距离
        BigDecimal rsrpDistance = new BigDecimal(0);
        // 遍历原始数据，套用公式
        for(Map<String, Object> item : datas){
            @SuppressWarnings("unchecked")
            Map<String, BigDecimal> rsrps = (Map<String, BigDecimal>) item.get("data");
            BigDecimal dataCheckValue = (BigDecimal) item.get("dataCheckValue");
            Iterator<Entry<String, BigDecimal>> it = rsrps.entrySet().iterator();  
            while(it.hasNext()){
                Entry<String,BigDecimal> entity = it.next();
                String gpp = entity.getKey();
                BigDecimal rsrp = entity.getValue();
                BigDecimal featureValue = featureValues.get(gpp);
                // 略去无效的数据
                if(rsrp.compareTo(DEFAULT_VALUE)<=0){
                    continue;
                }
                BigDecimal temp = rsrp.subtract(dataCheckValue).subtract(featureValue.subtract(checkValue)).pow(2);
                rsrpDistance = rsrpDistance.add(temp);
            }
        }
        // 返回特征半径
        return rsrpDistance.divide(new BigDecimal(datas.size()),2);
        
    }
    
    /** 
     * @Title: editRsrp 
     * @Description: 对rsrp进行修正 
     * @param datas 
     */
    private void editRsrp(Map<String, BigDecimal> datas){
        // 如果是空数据，直接返回
        if(datas.isEmpty()){
            return;
        }
        // 单条rsrp信息的总和
        BigDecimal total = new BigDecimal(0);
        // 平均值
        BigDecimal average = new BigDecimal(0);
        // 循环，求所有rsrp总和
        Iterator<Entry<String, BigDecimal>> it = datas.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry<String, BigDecimal> entity = (Entry<String, BigDecimal>) it.next();
            // 相加
            total = total.add(entity.getValue());
        }
        // 取平均
        average = total.divide(new BigDecimal(datas.size()),2);
        
        // 循环，修正rsrp，所有的rsrp减去平均值
        it = datas.entrySet().iterator();
        while(it.hasNext())
        {
            Map.Entry<String, BigDecimal> entity = (Entry<String, BigDecimal>) it.next();
            entity.setValue(entity.getValue().subtract(average));
        }
    }
    
    /** 
     * @Title: editGpp 
     * @Description: gpp添加enbId标识 
     * @param data 
     */
    private void editGpp(List<PrruSignalModel> data){
        for(PrruSignalModel p:data){
            p.setGpp(p.getEnbid() + "__" + p.getGpp());
        }
    }
}
