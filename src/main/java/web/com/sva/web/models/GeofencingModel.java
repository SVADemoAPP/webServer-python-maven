package com.sva.web.models;

public class GeofencingModel
{

    private int id;

    private String IdType;

    private String userid;

    private long mapid;

    private long zoneid;

    private String entre;

    private long timestamp;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getIdType()
    {
        return IdType;
    }

    public void setIdType(String idType)
    {
        IdType = idType;
    }

    public String getUserid()
    {
        return userid;
    }

    public void setUserid(String userid)
    {
        this.userid = userid;
    }

    public long getMapid()
    {
        return mapid;
    }

    public void setMapid(long mapid)
    {
        this.mapid = mapid;
    }

    public long getZoneid()
    {
        return zoneid;
    }

    public void setZoneid(long zoneid)
    {
        this.zoneid = zoneid;
    }

    public String getEntre()
    {
        return entre;
    }

    public void setEntre(String entre)
    {
        this.entre = entre;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

}
