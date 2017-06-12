package com.sva.common.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class GlobalConf
{
    public static String ip;

    public static String user;

    public static String password;
    
    /**
     * prru采集特征值线程池
     */
    private static Map<String,Thread> prruTaskPool = new HashMap<String,Thread>(10);

    /**
     * 对接SVA数据线程池
     */
    private static ArrayList<Thread> threadPool = new ArrayList<Thread>(10);
    
    /**
     * 对接IBMBluemixClient线程池
     */
    private static ArrayList<Thread> bluemixClientThreadPool = new ArrayList<Thread>(
            10);

    /**
     * 加入prru采集特征值线程池
     * 
     * @param e
     */
    public static synchronized void addPrruThreadPool(String id, Thread e)
    {
        prruTaskPool.put(id, e);
    }

    /**
     * 移除指定id对应的prru采集特征值线程
     * 
     * @param e
     */
    public static synchronized void removePrruThreadPool(String id)
    {
        prruTaskPool.remove(id);
    }
    
    /**
     * 根据id获取对于线程
     * 
     * @param i
     * @return
     */
    public static synchronized Thread getPrruThread(String id)
    {
        return prruTaskPool.get(id);
    }

    /**
     * 加入SVA数据线程池
     * 
     * @param e
     */
    public static synchronized void addThreadPool(Thread e)
    {
        threadPool.add(e);
    }

    /**
     * 移除SVA数据线程
     * 
     * @param e
     */
    public static synchronized void removeThreadPool(Thread e)
    {
        threadPool.remove(e);
    }

    /**
     * 获取SVA数据线程池长度
     * 
     * @return
     */
    public static synchronized int getThreadPoolSize()
    {
        return threadPool.size();
    }

    /**
     * 根据索引获取对于线程
     * 
     * @param i
     * @return
     */
    public static synchronized Thread getThreadPool(int i)
    {
        return threadPool.get(i);
    }

    /**
     * 添加对接IBMBluemixClient线程
     */
    public static synchronized void addBluemixClientThreadPool(Thread e)
    {
        bluemixClientThreadPool.add(e);
    }

    /**
     * 移除IBMBluemixClient线程
     */
    public static synchronized void removeBluemixClientThreadPool(Thread e)
    {
        bluemixClientThreadPool.remove(e);
    }

    /**
     * 获取IBMBluemixClient线程池长度
     */
    public static synchronized int getBluemixClientThreadPoolSize()
    {
        return bluemixClientThreadPool.size();
    }

    /**
     * 根据索引获取IBMBluemixClient线程
     */
    public static synchronized Thread getBluemixClientThreadPool(int i)
    {
        return bluemixClientThreadPool.get(i);
    }
}
