package com.sva.web.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AuthInterceptor extends HandlerInterceptorAdapter
{
	private HashMap<String,String> menuMap = new HashMap<String,String>();
	
	public AuthInterceptor(){
		menuMap.put("showStoreMng", "key_storeManage");
		menuMap.put("showCategoryMng", "key_areaCategory");
		menuMap.put("showSvaMng", "key_svaManage");
		menuMap.put("showMapMng", "key_mapManage");
		menuMap.put("showMsgMng", "key_messagePush");
		menuMap.put("showInputMng", "key_areaInfo");
		menuMap.put("showSellerMng", "key_sellerInfo");
		menuMap.put("showPing", "key_ping");
		menuMap.put("showHeatmap5", "key_customerPeriodHeamap");
		menuMap.put("showScattermap", "key_customerScattermap");
		menuMap.put("showLinemap", "key_CustomerFlowLinemap");
		menuMap.put("showCodeMng", "key_code");
		menuMap.put("showEstimate", "key_estimateDeviation");
		menuMap.put("showBluemix", "key_bluemixConnection");
		menuMap.put("showpRRU", "key_pRRUConfig");
		menuMap.put("showDown", "key_versionDownload");
	}

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception
    {

        if (handler.getClass().isAssignableFrom(HandlerMethod.class))
        {
            AuthPassport authPassport = ((HandlerMethod) handler)
                    .getMethodAnnotation(AuthPassport.class);

            // 没有声明需要权限,或者声明不验证权限
            if (authPassport == null || !authPassport.validate())
            {
                return true;
            }
            else
            {
                // 在这里实现自己的权限验证逻辑
                String username = (String) (request.getSession()
                        .getAttribute("username"));
                if (username != null && (!"".equals(username)))
                {
                    @SuppressWarnings("unchecked")
                    List<String> menuList = (List<String>) request.getSession().getAttribute("menu");
                    String url = request.getRequestURI();
                    String menuKey = menuMap.get(url.substring(url.lastIndexOf("/") + 1));
                    boolean isTrue = false;
                    if (menuKey!=null) {
                         isTrue = menuList.get(0).contains(menuKey);
                    }
                	if(StringUtils.isEmpty(menuKey) || menuList.contains("all") || menuList.contains(menuKey)|| isTrue){
                		return true;
                    }else {
                    	//TODO
                    	//增加权限限制提示界面，暂时用跳转到登陆界面控制
                    	response.sendRedirect("/sva/account/login");
                    	return false;
                    }
                }
                else
                {
                    // 返回到登录界面
                    response.sendRedirect("/sva/account/login");
                    return false;
                }
            }
        }
        else
        {
            return true;
        }
    }
    
}
