/**   
 * @Title: PrruSignalModel.java 
 * @Package com.sva.model 
 * @Description: PrruSignalModel实体类
 * @author labelCS   
 * @date 2016年9月27日 上午10:37:59 
 * @version V1.0   
 */
package com.sva.model;

import java.math.BigDecimal;

/**
 * @ClassName: PrruSignalModel
 * @Description: PrruSignalModel实体类
 * @author labelCS
 * @date 2016年9月27日 上午10:37:59
 * 
 */
public class PrruSignalModel {

    private int id;

    private String enbid;

    private String userId;

    private String gpp;

    private BigDecimal rsrp;

    private long timestamp;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the enbid
     */
    public String getEnbid() {
        return enbid;
    }

    /**
     * @param enbid
     *            the enbid to set
     */
    public void setEnbid(String enbid) {
        this.enbid = enbid;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the gpp
     */
    public String getGpp() {
        return gpp;
    }

    /**
     * @param gpp
     *            the gpp to set
     */
    public void setGpp(String gpp) {
        this.gpp = gpp;
    }

    /**
     * @return the rsrp
     */
    public BigDecimal getRsrp() {
        return rsrp;
    }

    /**
     * @param rsrp
     *            the rsrp to set
     */
    public void setRsrp(BigDecimal rsrp) {
        this.rsrp = rsrp;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp
     *            the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "id:" + id + ",enbid:" + enbid + ",userId:" + userId + ",gpp:" + gpp + ",rsrp:" + rsrp + ",timestamp:"
                + timestamp;
    }
}
