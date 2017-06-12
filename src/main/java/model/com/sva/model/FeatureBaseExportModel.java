/**   
 * @Title: FeatureBaseExportModel.java 
 * @Package com.sva.model 
 * @Description: FeatureBaseExportModel实体类
 * @author labelCS   
 * @date 2016年9月27日 上午10:41:10 
 * @version V1.0   
 */
package com.sva.model;

import java.math.BigDecimal;

import com.sva.common.ConvertUtil;

/** 
 * @ClassName: FeatureBaseExportModel 
 * @Description: FeatureBaseExportModel实体类
 * @author labelCS 
 * @date 2016年9月27日 上午10:41:10 
 *  
 */
public class FeatureBaseExportModel {
    
    private int id;
    private int mapId;
    private String x;
    private String y;
    private String floorid;
    private BigDecimal checkValue;
    private BigDecimal featureRadius;
    private String userId;
    private String gpp;
    private BigDecimal featureValue;
    private long timestamp;
    private String formatDate;
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return the x
     */
    public String getX() {
        return String.valueOf((new BigDecimal(x).multiply(new BigDecimal(10)).intValue()));
    }
    /**
     * @param x the x to set
     */
    public void setX(String x) {
        this.x = x;
    }
    /**
     * @return the y
     */
    public String getY() {
        return String.valueOf((new BigDecimal(y).multiply(new BigDecimal(10)).intValue()));
    }
    /**
     * @param y the y to set
     */
    public void setY(String y) {
        this.y = y;
    }
    /**
     * @return the floorNo
     */
    public String getFloorid() {
        return String.valueOf(new BigDecimal(floorid).intValue());
    }
    /**
     * @param floorNo the floorNo to set
     */
    public void setFloorid(String floorid) {
        this.floorid = floorid;
    }
    /**
     * @return the checkValue
     */
    public BigDecimal getCheckValue() {
        return checkValue;
    }
    /**
     * @param checkValue the checkValue to set
     */
    public void setCheckValue(BigDecimal checkValue) {
        this.checkValue = checkValue;
    }
    /**
     * @return the featureRadius
     */
    public BigDecimal getFeatureRadius() {
        return featureRadius;
    }
    /**
     * @param featureRadius the featureRadius to set
     */
    public void setFeatureRadius(BigDecimal featureRadius) {
        this.featureRadius = featureRadius;
    }
    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }
    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }
    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * @return the mapId
     */
    public int getMapId() {
        return mapId;
    }
    /**
     * @param mapId the mapId to set
     */
    public void setMapId(int mapId) {
        this.mapId = mapId;
    }
    /**
     * @return the formatDate
     */
    public String getFormatDate() {
        return ConvertUtil.dateFormat(timestamp, "yyyy-MM-dd HH:mm:ss");
    }
    /**
     * @param formatDate the formatDate to set
     */
    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
    }
    /**
     * @return the gpp
     */
    public String getGpp() {
        return gpp;
    }
    /**
     * @param gpp the gpp to set
     */
    public void setGpp(String gpp) {
        this.gpp = gpp;
    }
    /**
     * @return the featureValue
     */
    public BigDecimal getFeatureValue() {
        return featureValue;
    }
    /**
     * @param featureValue the featureValue to set
     */
    public void setFeatureValue(BigDecimal featureValue) {
        this.featureValue = featureValue;
    }
    
    /* (非 Javadoc) 
     * <p>Title: toString</p> 
     * <p>Description: 特征库导出txt文件时使用</p> 
     * @return 
     * @see java.lang.Object#toString() 
     */
    public String toString(){
        String[] enbidList = this.gpp.split("__");
        String enbid = enbidList[0];
        String gppString = enbidList[1];
        String[] gppList = gppString.split("_");
        return "id=" + this.id + ", coordinateX=" + this.getX() + ", coordinateY=" + this.getY() 
                + ", eNodeBId=" + enbid + ", radioNodeCabinetNum=" + gppList[0] + ", radioNodeSubrackNum=" + gppList[1] 
                + ", radioNodeSlotNum=" + gppList[2] + ", featureValue=" + this.featureValue.intValue()
                + ", mapId=" + this.mapId +";";
    }
}
