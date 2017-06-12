package com.sva.model;

public class TicketModel
{   
    //奖券路径
    private String ticketPath;
    //概率
    private String chances;
    //消息Id
    private String msgId;
    
    private String name;
    
    private String ticketId;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTicketPath()
    {
        return ticketPath;
    }

    public void setTicketPath(String ticketPath)
    {
        this.ticketPath = ticketPath;
    }

    public String getChances()
    {
        return chances;
    }

    public void setChances(String chances)
    {
        this.chances = chances;
    }

    public String getMsgId()
    {
        return msgId;
    }

    public void setMsgId(String msgId)
    {
        this.msgId = msgId;
    }

    public String getTicketId()
    {
        return ticketId;
    }

    public void setTicketId(String ticketId)
    {
        this.ticketId = ticketId;
    }

}   
 