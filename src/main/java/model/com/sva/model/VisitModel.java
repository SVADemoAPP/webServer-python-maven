package com.sva.model;

public class VisitModel
{
  private int visitCount;
  private String ip;
  private long firstVisitTime;
  private long lastVisitTime;
  private String userName;
public int getVisitCount()
{
    return visitCount;
}
public void setVisitCount(int visitCount)
{
    this.visitCount = visitCount;
}
public String getIp()
{
    return ip;
}
public void setIp(String ip)
{
    this.ip = ip;
}
public long getFirstVisitTime()
{
    return firstVisitTime;
}
public void setFirstVisitTime(long firstVisitTime)
{
    this.firstVisitTime = firstVisitTime;
}
public long getLastVisitTime()
{
    return lastVisitTime;
}
public void setLastVisitTime(long lastVisitTime)
{
    this.lastVisitTime = lastVisitTime;
}
public String getUserName()
{
    return userName;
}
public void setUserName(String userName)
{
    this.userName = userName;
}
  
}
