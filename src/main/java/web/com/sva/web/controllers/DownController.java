package com.sva.web.controllers;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.sva.model.VersionModel;

@Controller
@RequestMapping(value = "/down")
public class DownController
{

    private static Logger log = Logger.getLogger(AccountController.class);

    @RequestMapping(value = "/api/getData", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getTableData(Model model,
            HttpServletRequest request)
    {
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        List<VersionModel> list = new ArrayList<VersionModel>(10);
        String path = request.getSession().getServletContext()
                .getRealPath("/WEB-INF/version/WAR");
        VersionModel v = null;
        long l = 0;
        String pa = null;
        String p = null;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        File file = new File(path);
        if (file.exists())
        {
            File[] files = file.listFiles();
            if (files.length == 0)
            {
                modelMap.put("error", null);
                modelMap.put("data", list);
                return modelMap;
            }
            else
            {
                for (File file2 : files)
                {
                    if (file2.isDirectory())
                    {
                        modelMap.put("error", null);
                        modelMap.put("data", list);
                        return modelMap;
                    }
                    else
                    {
                        v = new VersionModel();
                        l = file2.lastModified();
                        v.setCreatTime(f.format(new Date(l)));
                        pa = file2.getAbsolutePath();
                        v.setName(file2.getName());
                        p = pa.substring(pa.lastIndexOf("\\"));
                        v.setPath(p);
                        list.add(v);
                    }
                }
                modelMap.put("error", null);
                modelMap.put("data", list);
            }
        }
        else
        {
            modelMap.put("error", null);
            modelMap.put("data", list);
        }
        return modelMap;
    }

    /*
     * 生成二维码APK图片
     */

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/api/generrateQrcode", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> generrateQrcode(HttpServletRequest request,
            Model model)
    {

        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        String apkName = null;
        String content = null;
        try
        {

            // 二维码内容
            String path = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/version/APK");
            String QRcodePath = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/upload");
            // int port = request.getLocalPort();

            // InetAddress ia=null;
            // http://localhost:8081/sva/home/uploadQRcode
            String url = request.getRequestURL().toString();
            // http://10.169.77.131:8081/sva/down/api/generrateQrcode

            String a = url.split("//")[1];
            String ips = null;
            String p = null;
            String ports = null;
            if (a.split(":").length>1) {
                ips = a.split(":")[0];
                p = a.split(":")[1];
                ports =  p.split("/")[0];
            }else
            {
                ips = a.split("/")[0];
                ports = "80";
            }
            // String content =
            // "http://10.169.77.131:8082/sva/version/APK/QRcode.jpg";

            // String ip = request.getLocalAddr();
            // String localip = "";
            // try
            // {
            // ia=ia.getLocalHost();
            // localip=ia.getHostAddress();
            //
            //
            // }
            // catch (Exception e)
            // {
            // e.printStackTrace();
            // }

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            File newPath = new File(path);
            // 列出该目录下所有文件和文件夹
            File[] files = newPath.listFiles();
            if (files.length != 0)
            {
                // 按照文件最后修改日期倒序排序
                Arrays.sort(files, new Comparator<File>()
                {
                    @Override
                    public int compare(File file1, File file2)
                    {
                        return (int) (file2.lastModified() - file1
                                .lastModified());
                    }
                });
                // 取出第一个(即最新修改的)文件，打印文件名
                apkName = files[0].getName();
                String prefix = apkName.substring(apkName.lastIndexOf(".") + 1)
                        .toUpperCase();
                if (prefix.equals("APK"))
                {
                    content = "http://" + ips + ':' + ports
                            + "/sva/version/downloadAPK.html";
                }
                else
                {
                    content = "The latest file is not apk package";
                }
            }
            else
            {
                content = "No File";
            }

            @SuppressWarnings("rawtypes")
            Map hints = new HashMap(2);

            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = multiFormatWriter.encode(content,
                    BarcodeFormat.QR_CODE, 200, 200, hints);

            File file1 = new File(QRcodePath, "QRcode.jpg");

            MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file1);

        }
        catch (Exception e)
        {

            log.info(e.getMessage());

        }
        modelMap.put("error", null);
        modelMap.put("data", apkName);
        return modelMap;
    }

    /*
     * 生成二维码IOS图片
     */

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/api/iosQrcode", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> iosQrcode(HttpServletRequest request, Model model)
    {

        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        String apkName = null;
        String content = null;
        try
        {

            // 二维码内容
            String path = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/version/IOS");
            String QRcodePath = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/upload");
            // int port = request.getLocalPort();

            // InetAddress ia=null;
            // http://localhost:8081/sva/home/uploadQRcode
            String url = request.getRequestURL().toString();
            // http://10.169.77.131:8081/sva/down/api/generrateQrcode

            String a = url.split("//")[1];
            String ips = null;
            String p = null;
            String ports = null;
            if (a.split(":").length>1) {
                ips = a.split(":")[0];
                p = a.split(":")[1];
                ports =  p.split("/")[0];
            }else
            {
                ips = a.split("/")[0];
                ports = "80";
            }
            // String content =
            // "http://10.169.77.131:8082/sva/version/APK/QRcode.jpg";

            // String ip = request.getLocalAddr();
            // String localip = "";
            // try
            // {
            // ia=ia.getLocalHost();
            // localip=ia.getHostAddress();
            //
            //
            // }
            // catch (Exception e)
            // {
            // e.printStackTrace();
            // }

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            File newPath = new File(path);
            // 列出该目录下所有文件和文件夹
            File[] files = newPath.listFiles();
            if (files.length != 0 && files != null)
            {
                // 按照文件最后修改日期倒序排序
                Arrays.sort(files, new Comparator<File>()
                {
                    @Override
                    public int compare(File file1, File file2)
                    {
                        return (int) (file2.lastModified() - file1
                                .lastModified());
                    }
                });
                // 取出第一个(即最新修改的)文件，打印文件名
                apkName = files[0].getName();
                String prefix = apkName.substring(apkName.lastIndexOf('.') + 1)
                        .toUpperCase();
                if ("IPA".equals(prefix))
                {
                    content = "http://" + ips + ':' + ports
                            + "/sva/version/downloadIOS.html";
                }
                else
                {
                    content = "The latest file is not IPA package";
                }
            }
            else
            {
                content = "No File";
            }

            @SuppressWarnings("rawtypes")
            Map hints = new HashMap(2);

            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = multiFormatWriter.encode(content,
                    BarcodeFormat.QR_CODE, 200, 200, hints);

            File file1 = new File(QRcodePath, "IOSQRcode.jpg");

            MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file1);

        }
        catch (Exception e)
        {

            log.info(e.getMessage());

        }
        modelMap.put("error", null);
        modelMap.put("data", apkName);
        return modelMap;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/api/allqrcode", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> allqrcode(HttpServletRequest request, Model model)
    {

        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        String apkName = null;
        String content = null;
        String apkName3 = null;
        String content3 = null;
        String apkName4 = null;
        String content4 = null;
        try
        {

            // 二维码内容
            String path = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/version/APK/1.2T");
            String path3 = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/version/APK/1.3T");
            String path4 = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/version/APK/1.4T");

            String QRcodePath = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/upload");
            String QRcodePath3 = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/upload");
            String QRcodePath4 = request.getSession().getServletContext()
                    .getRealPath("/WEB-INF/upload");
            log.debug("allqrcode path:"+QRcodePath);
            // int port = request.getLocalPort();

            // InetAddress ia=null;
            // http://localhost:8081/sva/home/uploadQRcode
            String url = request.getRequestURL().toString();
            // http://10.169.77.131:8081/sva/down/api/generrateQrcode

            String a = url.split("//")[1];
            String ips = null;
            String p = null;
            String ports = null;
            if (a.split(":").length>1) {
                ips = a.split(":")[0];
                p = a.split(":")[1];
                ports =  p.split("/")[0];
            }else
            {
                ips = a.split("/")[0];
                ports = "80";
            }

            // String content =
            // "http://10.169.77.131:8082/sva/version/APK/QRcode.jpg";

            // String ip = request.getLocalAddr();
            // String localip = "";
            // try
            // {
            // ia=ia.getLocalHost();
            // localip=ia.getHostAddress();
            //
            //
            // }
            // catch (Exception e)
            // {
            // e.printStackTrace();
            // }

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            File newPath = new File(path);
            // 列出该目录下所有文件和文件夹
            File[] files = newPath.listFiles();
            if (files.length != 0)
            {
                // 按照文件最后修改日期倒序排序
                Arrays.sort(files, new Comparator<File>()
                {
                    @Override
                    public int compare(File file1, File file2)
                    {
                        return (int) (file2.lastModified() - file1
                                .lastModified());
                    }
                });
                // 取出第一个(即最新修改的)文件，打印文件名
                apkName = files[0].getName();
                String prefix = apkName.substring(apkName.lastIndexOf(".") + 1)
                        .toUpperCase();
                if (prefix.equals("APK"))
                {
                    content = "http://" + ips + ':' + ports
                            + "/sva/version/downloadAPK.html";
                }
                else
                {
                    content = "The latest file is not apk package";
                }
            }
            else
            {
                content = "No File";
            }
            MultiFormatWriter multiFormatWriter3 = new MultiFormatWriter();
            File newPath3 = new File(path3);
            // 列出该目录下所有文件和文件夹
            File[] files3 = newPath3.listFiles();
            if (files3.length != 0)
            {
                // 按照文件最后修改日期倒序排序
                Arrays.sort(files3, new Comparator<File>()
                {
                    @Override
                    public int compare(File file1, File file2)
                    {
                        return (int) (file2.lastModified() - file1
                                .lastModified());
                    }
                });
                // 取出第一个(即最新修改的)文件，打印文件名
                apkName3 = files3[0].getName();
                String prefix3 = apkName3.substring(
                        apkName3.lastIndexOf(".") + 1).toUpperCase();
                if (prefix3.equals("APK"))
                {
                    content3 = "http://" + ips + ':' + ports
                            + "/sva/version/downloadAPKV1.3T.html";
                }
                else
                {
                    content3 = "The latest file is not apk package 3";
                }
            }
            else
            {
                content3 = "No File3";
            }
            MultiFormatWriter multiFormatWriter4 = new MultiFormatWriter();
            File newPath4 = new File(path4);
            // 列出该目录下所有文件和文件夹
            File[] files4 = newPath4.listFiles();
            if (files4.length != 0)
            {
                // 按照文件最后修改日期倒序排序
                Arrays.sort(files4, new Comparator<File>()
                {
                    @Override
                    public int compare(File file1, File file2)
                    {
                        return (int) (file2.lastModified() - file1
                                .lastModified());
                    }
                });
                // 取出第一个(即最新修改的)文件，打印文件名
                apkName4 = files4[0].getName();
                String prefix4 = apkName4.substring(
                        apkName4.lastIndexOf(".") + 1).toUpperCase();
                if (prefix4.equals("APK"))
                {
                    content4 = "http://" + ips + ':' + ports
                            + "/sva/version/downloadAPKV1.4T.html";
                }
                else
                {
                    content4 = "The latest file is not apk package 3";
                }
            }
            else
            {
                content4 = "No File3";
            }
            log.debug("allqrcode content:"+content +" "+content3+" "+content4);
            @SuppressWarnings("rawtypes")
            Map hints = new HashMap(2);
            @SuppressWarnings("rawtypes")
            Map hints3 = new HashMap(2);
            @SuppressWarnings("rawtypes")
            Map hints4 = new HashMap(2);

            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints3.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints4.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = multiFormatWriter.encode(content,
                    BarcodeFormat.QR_CODE, 200, 200, hints);
            BitMatrix bitMatrix3 = multiFormatWriter3.encode(content3,
                    BarcodeFormat.QR_CODE, 200, 200, hints3);
            BitMatrix bitMatrix4 = multiFormatWriter4.encode(content4,
                    BarcodeFormat.QR_CODE, 200, 200, hints4);
            File file1 = new File(QRcodePath, "QRcode2.jpg");
            MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file1);

            File file31 = new File(QRcodePath3, "QRcode3.jpg");
            MatrixToImageWriter.writeToFile(bitMatrix3, "jpg", file31);

            File file41 = new File(QRcodePath4, "QRcode4.jpg");
            MatrixToImageWriter.writeToFile(bitMatrix4, "jpg", file41);

        }
        catch (Exception e)
        {

            log.info(e.getMessage());

        }
        modelMap.put("error", null);
        modelMap.put("data2", apkName);
        modelMap.put("data3", apkName3);
        modelMap.put("data4", apkName4);
        return modelMap;
    }
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/api/allIosQrcode", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> allIosQrcode(HttpServletRequest request, Model model)
    {
        
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        String apkName = null;
        String content = null;
        String apkName3 = null;
        String content3 = null;
        String apkName4 = null;
        String content4 = null;
        try
        {
            
            // 二维码内容
            String path = request.getSession().getServletContext()
            .getRealPath("/WEB-INF/version/IOS/1.2T");
            String path3 = request.getSession().getServletContext()
            .getRealPath("/WEB-INF/version/IOS/1.3T");
            String path4 = request.getSession().getServletContext()
            .getRealPath("/WEB-INF/version/IOS/1.4T");
            
            String QRcodePath = request.getSession().getServletContext()
            .getRealPath("/WEB-INF/upload");
            String QRcodePath3 = request.getSession().getServletContext()
            .getRealPath("/WEB-INF/upload");
            String QRcodePath4 = request.getSession().getServletContext()
            .getRealPath("/WEB-INF/upload");
            // int port = request.getLocalPort();
            
            // InetAddress ia=null;
            // http://localhost:8081/sva/home/uploadQRcode
            String url = request.getRequestURL().toString();
            // http://10.169.77.131:8081/sva/down/api/generrateQrcode
            
            String a = url.split("//")[1];
            String ips = null;
            String p = null;
            String ports = null;
            if (a.split(":").length>1) {
                ips = a.split(":")[0];
                p = a.split(":")[1];
                ports =  p.split("/")[0];
            }else
            {
                ips = a.split("/")[0];
                ports = "80";
            }
            // String content =
            // "http://10.169.77.131:8082/sva/version/APK/QRcode.jpg";
            
            // String ip = request.getLocalAddr();
            // String localip = "";
            // try
            // {
            // ia=ia.getLocalHost();
            // localip=ia.getHostAddress();
            //
            //
            // }
            // catch (Exception e)
            // {
            // e.printStackTrace();
            // }
            
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            File newPath = new File(path);
            // 列出该目录下所有文件和文件夹
            File[] files = newPath.listFiles();
            if (files.length != 0)
            {
                // 按照文件最后修改日期倒序排序
                Arrays.sort(files, new Comparator<File>()
                        {
                    @Override
                    public int compare(File file1, File file2)
                    {
                        return (int) (file2.lastModified() - file1
                                .lastModified());
                    }
                        });
                // 取出第一个(即最新修改的)文件，打印文件名
                apkName = files[0].getName();
                String prefix = apkName.substring(apkName.lastIndexOf(".") + 1)
                .toUpperCase();
                if (prefix.equals("APK"))
                {
                    content = "http://" + ips + ':' + ports
                    + "/sva/version/downloadIOS.html";
                }
                else
                {
                    content = "The latest file is not apk package";
                }
            }
            else
            {
                content = "No File";
            }
            MultiFormatWriter multiFormatWriter3 = new MultiFormatWriter();
            File newPath3 = new File(path3);
            // 列出该目录下所有文件和文件夹
            File[] files3 = newPath3.listFiles();
            if (files3.length != 0)
            {
                // 按照文件最后修改日期倒序排序
                Arrays.sort(files3, new Comparator<File>()
                        {
                    @Override
                    public int compare(File file1, File file2)
                    {
                        return (int) (file2.lastModified() - file1
                                .lastModified());
                    }
                        });
                // 取出第一个(即最新修改的)文件，打印文件名
                apkName3 = files3[0].getName();
                String prefix3 = apkName3.substring(
                        apkName3.lastIndexOf(".") + 1).toUpperCase();
                if (prefix3.equals("APK"))
                {
                    content3 = "http://" + ips + ':' + ports
                    + "/sva/version/downloadIOSV1.3T.html";
                }
                else
                {
                    content3 = "The latest file is not apk package 3";
                }
            }
            else
            {
                content3 = "No File3";
            }
            MultiFormatWriter multiFormatWriter4 = new MultiFormatWriter();
            File newPath4 = new File(path4);
            // 列出该目录下所有文件和文件夹
            File[] files4 = newPath4.listFiles();
            if (files4.length != 0)
            {
                // 按照文件最后修改日期倒序排序
                Arrays.sort(files4, new Comparator<File>()
                        {
                    @Override
                    public int compare(File file1, File file2)
                    {
                        return (int) (file2.lastModified() - file1
                                .lastModified());
                    }
                        });
                // 取出第一个(即最新修改的)文件，打印文件名
                apkName4 = files4[0].getName();
                String prefix4 = apkName4.substring(
                        apkName4.lastIndexOf(".") + 1).toUpperCase();
                if (prefix4.equals("APK"))
                {
                    content4 = "http://" + ips + ':' + ports
                    + "/sva/version/downloadIOSV1.4T.html";
                }
                else
                {
                    content4 = "The latest file is not apk package 3";
                }
            }
            else
            {
                content4 = "No File3";
            }
            
            @SuppressWarnings("rawtypes")
            Map hints = new HashMap(2);
            @SuppressWarnings("rawtypes")
            Map hints3 = new HashMap(2);
            @SuppressWarnings("rawtypes")
            Map hints4 = new HashMap(2);
            
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints3.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints4.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            
            BitMatrix bitMatrix = multiFormatWriter.encode(content,
                    BarcodeFormat.QR_CODE, 200, 200, hints);
            BitMatrix bitMatrix3 = multiFormatWriter3.encode(content3,
                    BarcodeFormat.QR_CODE, 200, 200, hints3);
            BitMatrix bitMatrix4 = multiFormatWriter4.encode(content4,
                    BarcodeFormat.QR_CODE, 200, 200, hints4);
            
            File file1 = new File(QRcodePath, "IOSQRcode2.jpg");
            MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file1);
            
            File file31 = new File(QRcodePath3, "IOSQRcode3.jpg");
            MatrixToImageWriter.writeToFile(bitMatrix3, "jpg", file31);
            
            File file41 = new File(QRcodePath4, "IOSQRcode4.jpg");
            MatrixToImageWriter.writeToFile(bitMatrix4, "jpg", file41);
            
        }
        catch (Exception e)
        {
            
            log.info(e.getMessage());
            
        }
        modelMap.put("error", null);
        modelMap.put("data2", apkName);
        modelMap.put("data3", apkName3);
        modelMap.put("data4", apkName4);
        return modelMap;
    }

    @RequestMapping(value = "/contentShow", method = {RequestMethod.GET})
    // public String contentShow(HttpServletRequest
    // request,@RequestParam("stores") String stores,
    // @RequestParam("floor") String floor,
    // @RequestParam("startTime") String startTime,
    // @RequestParam("stores") String endTime) throws SQLException
    public String contentShow(HttpServletRequest request) throws SQLException
    {
        // request.getSession().setAttribute("show", null);
        request.getSession().removeAttribute("show");
        return "/home/contentShowindex";
    }
}
