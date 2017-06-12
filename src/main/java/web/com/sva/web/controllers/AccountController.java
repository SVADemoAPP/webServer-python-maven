	package com.sva.web.controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sva.common.Util;
import com.sva.dao.AccountDao;
import com.sva.dao.VisitDao;
import com.sva.model.AccountModel;
import com.sva.model.VisitModel;

@Controller
@RequestMapping(value = "/account")
public class AccountController
{
    @Autowired
    private AccountDao dao;
    
    @Autowired
    private VisitDao visDao;   

    private static Logger log = Logger.getLogger(AccountController.class);

    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    public String login()
    {
        return "account/login";
    }
    
    @RequestMapping(value = "/main", method = {RequestMethod.GET})
    public String main(){
    	return "account/main";
    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public String login(HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            @RequestParam("username") String username,
            @RequestParam("password") String password)

    {
        List<String> selMenuKey = new ArrayList<String>(10);
        List<String> storeides = new ArrayList<String>(10);
        if (username == "" || password == "")
        {
            redirectAttributes.addFlashAttribute("info", "null");
            return "redirect:/account/login";
        }
        Collection<AccountModel> result = dao.findUser(username, password);
        if (result.size() != 0)
        {
        	List<String> menuListy = new ArrayList<String>();
        	menuListy.add("all");
        	request.getSession().setAttribute("username", username);
        	request.getSession().setAttribute("auth", "root");
            if ("admin".equals(username))
            {
                request.getSession().setAttribute("menu", menuListy);
            }
            else
            {
                try
                {
                    selMenuKey = dao.selMenuKey(username);
                    storeides = dao.selectStore(username);
                    request.getSession().setAttribute("storeides", storeides);
                    request.getSession().setAttribute("menu", selMenuKey);
                }
                catch (SQLException e)
                {
                    log.info(e.getMessage());
                }
            }
        }
        else
        {
            redirectAttributes.addFlashAttribute("info", "error");
            return "redirect:/account/login";
        }
        String ip = Util.getIpAddr(request);
        Collection<VisitModel> visitResult = visDao.getIpExistence(ip);
        int reSize = visitResult.size();
        String userName =  (String)request.getSession().getAttribute("username");
        VisitModel visi = new VisitModel();
        visi.setIp(ip);
        visi.setUserName(userName);
        int count =0;
        long time = System.currentTimeMillis();
        if (reSize>0)
        {
            for (VisitModel vi:visitResult)
            {
                count = vi.getVisitCount(); 
            } 
            count = count+1;
            visi.setVisitCount(count);
            visi.setLastVisitTime(time);
            visDao.updateData(visi);
            
        }
        else
        {
          count = 1;
          visi.setVisitCount(count);
          visi.setFirstVisitTime(time);
          visi.setLastVisitTime(time);
          visDao.saveData(visi);
        }
        return "redirect:/home/showOverView";
    }

    // 退出登陆
    @RequestMapping(value = "/logout", method = {RequestMethod.GET})
    public String login(HttpServletRequest request)
    {
        request.getSession().setAttribute("username", null);
        return "redirect:/account/login";
    }

    @RequestMapping(value = "/api/getData", method = {RequestMethod.GET})
    @ResponseBody
    public Map<String, Object> getTableData(Model model)
    {
        log.info("AccountController:getTableData");

        Collection<AccountModel> ResultList = dao.doquery();
        Map<String, Object> modelMap = new HashMap<String, Object>(2);

        modelMap.put("error", null);
        modelMap.put("data", ResultList);

        return modelMap;
    }

    @RequestMapping(value = "/api/saveData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> saveData(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam("userName") String userName,
            @RequestParam("password") String password,
            @RequestParam("type") String type)
    {

        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        Date current = new Date();
        AccountModel sm = new AccountModel();
        sm.setUsername(userName);
        sm.setPassword(password);
        sm.setRoleId(Integer.parseInt(type));
        sm.setUpdateTime(current);
        // 保存
        try
        {
            if ("".equals(id))
            {
                dao.saveInfo(sm);
            }
            else
            {
                sm.setId(Integer.parseInt(id));
                dao.updateInfo(sm);
            }
        }
        catch (DuplicateKeyException e)
        {
            log.info(e.getStackTrace());
            modelMap.put("error", "username can not be the same");
        }
        catch (Exception e)
        {
            log.info(e.getStackTrace());
            modelMap.put("error", e.getStackTrace());
        }

        modelMap.put("data", null);
        return modelMap;
    }

    @RequestMapping(value = "/api/deleteData", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> deleteData(@RequestParam("id") String id)
    {
        log.info("AccountController:deleteData::" + id);
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        try
        {
            dao.deleteById(id);
            // dao.deleteMapByStore(id);
        }
        catch (Exception e)
        {
            log.info(e.getStackTrace());
            modelMap.put("error", e.getStackTrace());
        }

        return modelMap;
    }
    
    @RequestMapping(value = "/getAllCount", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String, Object> getAllCount()
    {
        Map<String, Object> modelMap = new HashMap<String, Object>(2);
        List<String> allCountList = visDao.getAllCount();
        int allCount = 0;
        for (int i = 0; i < allCountList.size(); i++)
        {
            allCount = allCount + Integer.parseInt(allCountList.get(i));
        }
        modelMap.put("allCount", allCount);
        return modelMap;
    }
}