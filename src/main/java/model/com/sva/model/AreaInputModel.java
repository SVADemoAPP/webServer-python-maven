package com.sva.model;

import java.math.BigDecimal;

public class AreaInputModel {
	private String idType;

	private long timestamp;
	private long bigentime;
	private long localtime;

	private String dataType;

	private String userID;

	private int areaId;

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getBigentime() {
		return bigentime;
	}

	public void setBigentime(long bigentime) {
		this.bigentime = bigentime;
	}

	public long getLocaltime() {
		return localtime;
	}

	public void setLocaltime(long localtime) {
		this.localtime = localtime;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}

}
