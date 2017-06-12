package com.sva.model;

public class IlsModel {
    // SDK计算的CGI
    private int cgi;
    // 网络类型
    private String networkType;
    // DK计算的PLMN
    private String plmn;
    
    private int lac;
    
    private int eNodeBID;
    
    private int localCellID;
    //手机ip
    private String InternalIP;
    
    private int svaId;

    public int getCgi() {
        return cgi;
    }

    public void setCgi(int cgi) {
        this.cgi = cgi;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getPlmn() {
        return plmn;
    }

    public void setPlmn(String plmn) {
        this.plmn = plmn;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int geteNodeBID() {
        return eNodeBID;
    }

    public void seteNodeBID(int eNodeBID) {
        this.eNodeBID = eNodeBID;
    }

    public int getLocalCellID() {
        return localCellID;
    }

    public void setLocalCellID(int localCellID) {
        this.localCellID = localCellID;
    }

    public String getInternalIP() {
        return InternalIP;
    }

    public void setInternalIP(String internalIP) {
        InternalIP = internalIP;
    }

    public int getSvaId() {
        return svaId;
    }

    public void setSvaId(int svaId) {
        this.svaId = svaId;
    }
    
}
