/*
 * 文件名：XmlParser.java
 * 版权：Copyright 2013-2013 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * 描述： XmlParser.java
 * 修改人：dWX182800
 * 修改时间：2013-11-4
 * 修改内容：新增
 */
package com.sva.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.sva.model.PrruModel;

/**
 * 解析xml
 * <p>
 * 详细描述
 * <p>
 * 示例代码
 * 
 * <pre>
 * </pre>
 * wwx283823
 */
public class XmlParser
{
    /**
     * 调测日志记录器。
     */
    private static Logger logger = Logger.getLogger(XmlParser.class);

    private Document xmldoc = null;

    public XmlParser()
    {
    }

    private Element root = null;

    public void init(File file)
    {
        SAXReader reader = new SAXReader();
        try
        {
            xmldoc = reader.read(file);
        }
        catch (Exception e)
        {
            logger.info(e.getMessage());
        }

        root = xmldoc.getRootElement();
    }

    /**
     * 获取属性集合
     * 
     * @param xPath
     * @param values
     * @return
     */
    public List<PrruModel> getAttrVal(String flooNo, int plceId, String xPath,
            String... values)
    {
        if (values == null || values.length == 0)
        {
            return null;
        }
        @SuppressWarnings("unchecked")
        List<Node> node = root.selectNodes(xPath);
        //读取比例尺
        @SuppressWarnings("unchecked")
        List<Node> scaleNode = root.selectNodes("//Project/Floors/Floor/DrawMap");
        double scale = 0;
        String eNodeBid;
        if (scaleNode==null) {
            logger.error( "Project/Floors/Floor/DrawMap hasn't node");
            return null;
        }else
        {
            scale = Double.valueOf((scaleNode.get(0).valueOf('@' + "scale")));
            eNodeBid = scaleNode.get(0).valueOf('@' + "eNodeBid").trim();
        }
        List<PrruModel> results = new ArrayList<PrruModel>(10);
        String result = null;
        PrruModel pm = null;
        int si = node.size();
        String id = "id";
        String name = "name";
        String x = "x";
        String y = "y";
        String necode = "neCode";
        for (int i = 0; i < si; i++)
        {
            pm = new PrruModel();
            pm.setPlaceId(plceId);
            pm.seteNodeBid(eNodeBid);
            for (String value : values)
            {
                if (node == null)
                {
                    logger.error(xPath + " hasn't node");
                    continue;
                }
                result = node.get(i).valueOf('@' + value);
                if (value.equals("cellId"))
                {
                    pm.setCellId(result);;
                }
                if (value.equals(id))
                {
                    pm.setNeId(result);
                }
                if (value.equals(necode))
                {
                    pm.setNeCode(eNodeBid + "_" + result);
                }
                if (value.equals(name))
                {
                    pm.setNeName(result);
                }
                if (value.equals(x))
                {
                    pm.setX(String.valueOf(new java.text.DecimalFormat("#.00").format(Integer.parseInt(result)/scale)));
                }
                if (value.equals(y))
                {
                    pm.setY(String.valueOf(new java.text.DecimalFormat("#.00").format(Integer.parseInt(result)/scale)));
                }
                if (result == null || "".equals(result))
                {
                    logger.error(" not find data:" + value);
                }

            }
            pm.setFloorNo(flooNo);
            results.add(pm);
        }
        return results;
    }

    public Node getNode(String xPath)
    {
        return root.selectSingleNode(xPath);
    }

    @SuppressWarnings("unchecked")
    public List<Node> getNodes(String xPath)
    {
        return root.selectNodes(xPath);
    }
}
