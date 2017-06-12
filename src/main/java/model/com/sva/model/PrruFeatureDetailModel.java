/**   
 * @Title: PrruFeatureDetailModel.java 
 * @Package com.sva.model 
 * @Description: PrruFeatureDetailModel实体类  
 * @author labelCS   
 * @date 2016年9月27日 上午10:44:54 
 * @version V1.0   
 */
package com.sva.model;

import java.math.BigDecimal;

/**
 * @ClassName: PrruFeatureDetailModel
 * @Description: PrruFeatureDetailModel实体类
 * @author labelCS
 * @date 2016年9月27日 上午10:44:54
 * 
 */
public class PrruFeatureDetailModel {
	private int id;
	private int featureid;
	private String gpp;
	private BigDecimal featureValue;

	// prru对应特征点（featureid）距离
	private double distance;

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
	 * @return the featureid
	 */
	public int getFeatureid() {
		return featureid;
	}

	/**
	 * @param featureid
	 *            the featureid to set
	 */
	public void setFeatureid(int featureid) {
		this.featureid = featureid;
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
	 * @return the featureValue
	 */
	public BigDecimal getFeatureValue() {
		return featureValue;
	}

	/**
	 * @param featureValue
	 *            the featureValue to set
	 */
	public void setFeatureValue(BigDecimal featureValue) {
		this.featureValue = featureValue;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

}
