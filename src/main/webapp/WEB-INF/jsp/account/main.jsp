<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!doctype html>
<%@ include file="../shared/taglib.jsp"%>

<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
    <head>
        <%@ include file="../shared/importCss.jsp"%>
        <meta charset="utf-8">
        <title>SVA APP demo</title>
        <style>
            body{ background-color:#e9e9e9 !important;}
            body{font-size:12px; font-family:"Microsoft YaHei";}
            .tab{width:100%; height:100%;}
            .tdtextcenter{text-align: center}
            .tab ul{width:100%; height:44px; background:none; margin:0; padding:0;}
            .tab ul li{list-style:none; width:82px; height:42px; line-height:42px; border-top:2px solid #fff; border-right:4px solid #fff; border-bottom:0; float:left; text-align:center; font-size:14px; font-weight:bold; color:#fff;}
            .tab .tabContent{clear:left;}
            .tab .tabContent div{width:100%; height:354px; border:0; display:none; padding:25px 30px; box-sizing:border-box; -moz-box-sizing:border-box;}
            .tab .tabContent div.active{display:block;}
            .top{ overflow:hidden; margin-top:15px}
            .top a img{ float:left}
            .top p{ float:right; line-height:0px}
            .toptext{ font-size:48px; color:#274f70; margin:1em 0;}
            .topline { margin-left:25px; margin-right:25px;}
            .tdtextcenter1 td{border-left: 1px solid #FFFFFF;border-bottom: 1px solid #FFFFFF;border-right: 1px solid #FFFFFF}
            .shiyong th{border-left: 1px solid #FFFFFF;border-bottom: 1px solid #FFFFFF;border-right: 1px solid #FFFFFF;border-top: 1px solid #FFFFFF}
            .midbox{margin:0 auto;  overflow:hidden; position:relative; margin-top:20px}
            .banner{margin-top:10px;}
            .banner img { height:338px;}
        </style>
    </head>


    <body>
        <div class="container">
            <div class="row-fluid top">
                <a><img src="<c:url value='/images/login/huawei.png'/>"></a>
                <img class="topline" src="<c:url value='/images/login/line.png'/>">
                <img src="<c:url value='/images/login/SVA APP demo.png'/>">
                <a href="/sva/home/changeLocal?local=zh" style="float:right; font-size:14px;">中文</a> | 
                <a href="/sva/home/changeLocal?local=en" style="float:right; font-size:14px; margin-right: 13px;">English</a>
            </div>
            <div class="row-fluid midbox" style="height: 50%;background-color:#FFF;">
                <div class="bannerbox">
                    <div id="myCarousel" class="carousel slide banner">
                        <ol class="carousel-indicators">
                            <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
                            <li data-target="#myCarousel" data-slide-to="1"></li>
                            <li data-target="#myCarousel" data-slide-to="2"></li>
                            <li data-target="#myCarousel" data-slide-to="3"></li>
                        </ol>
                        <!-- Carousel items -->
                        <div class="carousel-inner">
                            <div class="active item">
                                <img src="<spring:message code="sva_login_1" />">
                            </div>
                            <div class="item">
                                <img src="<spring:message code="sva_login_2" />">
                            </div>
                            <div class="item">
                                <img src="<spring:message code="sva_login_3" />">
                            </div>
                            <div class="item">
                                <img src="<spring:message code="sva_login_4" />">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row-fluid midbox">
                <div class="tab">
                    <ul>
                        <li>URL</li>
                        <li>Web</li>
                        <li>Android</li>
                        <li>Ios</li>
                        <li>Help</li>
                    </ul>
                    <div class="tabContent">
                        <div class="active">
                            <table  class="table table-bordered"">
                                <thead>
                                    <tr>
                                        <th colspan="2"><spring:message code="port" /></th>
                                        <th><spring:message code="url" /></th>
                                        <th><spring:message code="version_no" /></th>
                                    </tr>
                                </thead>
                                <tbody class="tdtextcenter">
                                    <tr>
                                        <td rowspan="4">演示样板</td>
                                        <td>成研(U6,U1)</td>
                                        <td>
                                        <a href="http://223.202.102.66:8084/sva/account/login">http://223.202.102.66:8084/sva/account/login
                                        </a>
                                        </td>
                                        <td>SVADemo V1.4T</td>
                                    </tr>
                                    <tr>
                                        <td>王府井</td>
                                        <td>
                                        <a href="http://223.202.102.66:8085/sva/account/login">http://223.202.102.66:8085/sva/account/login
                                        </a>
                                        </td>
                                        <td>SVADemo V1.4T</td>
                                    </tr>
                                    <tr>
                                        <td>上海演示</td>
                                        <td>
                                        <a href="http://223.202.102.66:8090/sva/home/content3">http://223.202.102.66:8090/sva/home/content3
                                        </a>
                                        </td>
                                        <td>SVADemo V1.4T</td>
                                    </tr>
                                    <tr>
                                        <td>成研所演示</td>
                                        <td>
                                        <a href="http://223.202.102.66:8089/sva/home/contentJingShow">http://223.202.102.66:8089/sva/home/contentJingShow
                                        </a>
                                        </td>
                                        <td>SVADemo V1.4T</td>
                                    </tr>
                                    <tr>
                                        <td rowspan="2">内部测试</td>
                                        <td>成研（u9-wlan,u9-Lampsite,海岸城）</td>
                                        <td>
                                        <a href="http://223.202.102.66:8086/sva/account/login">http://223.202.102.66:8086/sva/account/login
                                        </a>
                                        </td>
                                        <td>SVADemo V1.4T</td>
                                    </tr>
                                    <tr>
                                        <td>成研U6测试</td>
                                        <td>
                                        <a href="http://223.202.102.66:7074/sva/account/login">http://223.202.102.66:7074/sva/account/login
                                        </a>
                                        </td>
                                        <td>SVADemo V1.4T</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div>
                            <table  class="table table-bordered"">
                                <thead>
                                    <tr>
                                        <th><spring:message code="version_name" /></th>
                                        <th><spring:message code="test_report" /></th>
                                        <th><spring:message code="explain_for_use" /></th>
                                        <th><spring:message code="download" /></th>
                                    </tr>
                                </thead>
                                <tbody class="tdtextcenter">
                                     <tr>
                                        <td>SVADemo V1.2T</td>
                                        <td>
                                        <a href="../testReport/SVADemoV1.2T.docx">SVADemoV1.2T.docx
                                        </a>
                                        </td>
                                        <td> 
                                        <a href="../readme/readme_1.2T.txt">readme_1.2T.txt
                                        </a></td>
                                        <td><a href="../version/WAR/SVADemoV1.2T.zip">
                                        <spring:message code="download" /></a></td>
                                    </tr>
                                    <tr>
                                        <td>SVADemo V1.3T</td>
                                        <td>
                                        <a href="../testReport/SVADemoV1.3T.docx">SVADemoV1.3T.docx
                                        </a>
                                        </td>
                                        <td> 
                                        <a href="../readme/readme_1.3T.txt">readme_1.3T.txt
                                        </a></td>
                                        <td><a href="../version/WAR/SVADemoV1.3T.zip">
                                        <spring:message code="download" /></a></td>
                                    </tr>
                                    <tr>
                                        <td>V100R005C10B01</td>
                                        <td>
                                        <a href="../testReport/SVADemoV1.3T.docx">SVADemoV1.3T.docx
                                        </a>
                                        </td>
                                        <td> 
                                        <a href="../readme/readme_1.3T.txt">readme_1.3T.txt
                                        </a></td>
                                        <td><a href="../version/WAR/V100R005C10B01.zip">
                                        <spring:message code="download" /></a></td>
                                    </tr>                                           
                                </tbody>
                            </table>
                        </div>
                        <div>
                            <table  class="table table-bordered"">
                                <thead>
                                    <tr>
                                        <th><spring:message code="version_name" /></th>
                                        <th><spring:message code="test_report" /></th>
                                        <th><spring:message code="explain_for_use" /></th>
                                        <th><spring:message code="two_dimension_code" /></th>
                                    </tr>
                                </thead>
                                <tbody class="tdtextcenter">
                                     <tr>
                                        <td>SVADemo V1.2T</td>
                                        <td>
                                        <a href="../testReport/SVADemoV1.2T.docx">SVADemoV1.2T.docx
                                        </a>
                                        </td>
                                        <td> 
                                        <a href="../readme/readme_1.2T.txt">readme_1.2T.txt
                                        </a></td>
                                        <td >
                                        <a id = "xiazai1"><spring:message code="download" /></a></td>
                                    </tr>                                   
                                     <tr>
                                        <td>SVADemo V1.3T</td>
                                        <td>
                                        <a href="../testReport/SVADemoV1.3T.docx">SVADemoV1.3T.docx
                                        </a>
                                        </td>
                                        <td> 
                                        <a href="../readme/readme_1.3T.txt">readme_1.3T.txt
                                        </a></td>
                                        <td >
                                        <a id = "xiazai2"><spring:message code="download" /></a></td>
                                    </tr>                                   
                                   <!--  <tr>
                                        <td>SVADemo V1.4T</td>
                                        <td>
                                        <a href="../testReport/SVADemoV1.4T.docx">SVADemoV1.4T.docx
                                        </a>
                                        </td>
                                        <td> 
                                        <a href="../readme/readme_1.4T.txt">readme_1.4T.txt
                                        </a></td>
                                        <td >
                                        <a id = "xiazai3"><spring:message code="download" /></a></td>
                                    </tr>       -->                         
                                </tbody>
                            </table>
                        </div>
                        <div>
                            <table  class="table table-bordered"">
                                <thead>
                                    <tr>
                                        <th><spring:message code="version_name" /></th>
                                        <th><spring:message code="test_report" /></th>
                                        <th><spring:message code="explain_for_use" /></th>
                                        <th><spring:message code="two_dimension_code" /></th>
                                    </tr>
                                </thead>
                                <tbody class="tdtextcenter">
                                   <!--  <tr>
                                        <td>SVADemo V1.2T</td>
                                        <td>
                                        <a href="../testReport/SVADemoV1.2T.docx">SVADemoV1.2T.docx
                                        </a>
                                        </td>
                                        <td> 
                                        <a href="../readme/readme_1.2T.txt">readme_1.2T.txt
                                        </a></td>
                                        <td >
                                        <a id = "IOSxiazai1"><spring:message code="download" /></a></td>
                                    </tr>                                   
                                     <tr>
                                        <td>SVADemo V1.3T</td>
                                        <td>
                                        <a href="../testReport/SVADemoV1.3T.docx">SVADemoV1.3T.docx
                                        </a>
                                        </td>
                                        <td> 
                                        <a href="../readme/readme_1.3T.txt">readme_1.3T.txt
                                        </a></td>
                                        <td >
                                        <a id = "IOSxiazai2"><spring:message code="download" /></a></td>
                                    </tr> -->                                 
                                     <tr>
                                        <td>SVADemo V1.3T</td>
                                        <td>
                                        <a href="../testReport/SVADemoV1.3T.docx">SVADemoV1.3T.docx
                                        </a>
                                        </td>
                                        <td> 
                                        <a href="../readme/readme_1.3T.txt">readme_1.3T.txt
                                        </a></td>
                                        <td >
                                        <a id = "IOSxiazai3"><spring:message code="download" /></a></td>
                                    </tr>                                   
                                </tbody>
                            </table>
                        </div>
                        <div>
                            <table  class="table table-bordered"" style="border-top:1px solid #FFFFFF ">
                                <thead class="shiyong">
                                    <tr>
                                        <th style="border-left: 1px solid #FFFFFF;border-bottom: 1px solid #FFFFFF"><spring:message code="version_name" /></th>
                                        <th><spring:message code="download" /></th>
                                    </tr>
                                </thead>
                                <tbody class="tdtextcenter">                             
                                     <tr class = "tdtextcenter1">
                                        <td>SVADemo V1.2T</td>
                                        <td>
                                        <a href="../user/SVADemoV1.2T.doc"><spring:message code="download" />
                                        </a>
                                        </td>
                                    </tr>
                                     <tr class = "tdtextcenter1">
                                        <td>SVADemo V1.3T</td>
                                        <td>
                                        <a href="../user/SVADemoV1.3T.doc"><spring:message code="download" />
                                        </a>
                                        </td>
                                    </tr>  
                                     <tr class = "tdtextcenter1">
                                        <td>svaTools</td>
                                        <td>
                                        <a href="../user/sva.zip"><spring:message code="download" />
                                        </a>
                                        </td>
                                    </tr>                                   
                                </tbody>
                            </table>
                        </div>                      
                    </div>
                </div>
            </div>
        <div class="modal hide fade" id="myModal" style="top: 30%;width: 40%;margin-left: 20%">
            <div class="modal-body">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><img id ="imgClose"src="../images/close.png" ></button>
                <div style="text-align: center;margin-top: 8%" class="bs-docs-example">
                    <img  id ="pictureid"src="">
                </div>
            </div>
            <div class="modal-footer">
                <input type="button" id= "closeId" onclick="closeModel()" value=" <spring:message code="common_close" />">
            </div>

        </div>          
            <div class="row-fluid">
                <div class="span12">
                    <div class="bottom">
                        <%@ include file="../shared/loginFooter.jsp"%>
                    </div>
                </div>
            </div>
        </div>
  
        <%@ include file="../shared/importJs.jsp"%>
         <script src="<c:url value='/js/main.js'/>" type="text/javascript"></script>
        <script type="text/javascript">
        var path2;
        var path3;
        var path4;
        
          $(document).ready(function() { 
              $.post("/sva/down/api/allqrcode",function(data){
                    path2 = data.data2;
                    path3 = data.data3;
                    path4 = data.data4;
              });
       <!--       $.post("/sva/down/api/allIosQrcode",function(data){
          <!--          path2 = data.data2;
          <!--          path3 = data.data3;
         <!--           path4 = data.data4;
          <!--       });
            });
            $(function() {
                //启动轮播
                $('.carousel').carousel({
                    interval: 3000
                });
                
                $(".tab ul li:eq(0), .tab .tabContent div:eq(0)").css("background","#F68B6D");
                $(".tab ul li:eq(1), .tab .tabContent div:eq(1)").css("background","#FFB863");
                $(".tab ul li:eq(2), .tab .tabContent div:eq(2)").css("background","#96C2E2");
                $(".tab ul li:eq(3), .tab .tabContent div:eq(3)").css("background","#68CB4E");
                $(".tab ul li:eq(4), .tab .tabContent div:eq(4)").css("background","#f0e3cd");
                $(".tab ul li").click(function(){
                    var _index = $(this).index();
                    $(".tab .tabContent div").eq(_index).show().siblings().hide();
                });
            });

        </script>    
    </body>
</html>
