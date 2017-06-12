<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>
<%@ include file="../shared/taglib.jsp"%>

<html>

<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en" class="no-js">
<!--<![endif]-->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SVA APP demo</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />
<meta name="MobileOptimized" content="320">

<%@ include file="../shared/importCss.jsp"%>
<!-- BEGIN PAGE LEVEL PLUGIN STYLES -->
<link
	href="<c:url value='/plugins/data-tables/media/css/demo_table.css'/>"
	rel="stylesheet" type="text/css" />
<style type="text/css">
.popuptext{
	margin-top:50px;
}
.editData{
	-webkit-user-select:text !important;
}
</style>
<!-- END PAGE LEVEL PLUGIN STYLES -->

<link rel="shortcut icon" href="favicon.ico" />
</head>
<!-- END HEAD -->

<!-- BEGIN BODY -->
<body class="page-header-fixed">

	<%@ include file="../shared/pageHeader.jsp"%>

	<div class="clearfix"></div>
	<!-- BEGIN CONTAINER -->
	<div class="page-container">

		<%@ include file="../shared/sidebarMenu.jsp"%>

		<!-- BEGIN PAGE -->
		<div class="page-content">

			<!-- BEGIN PAGE HEADER-->
			<div>
				<div class="col-md-12">
					<!-- BEGIN PAGE TITLE & BREADCRUMB
					<h3 class="page-title"><spring:message code="test_title" /></h3>-->
					<ul class="page-breadcrumb breadcrumb">
						<li><i class="icon-home" style="background-image:none"></i>
							<spring:message code="ceshi_tool" /> <i
							class="icon-angle-right"></i></li>
						<li><spring:message code="menu_location_result" /></li>
					</ul>
					<!-- END PAGE TITLE & BREADCRUMB-->
				</div>
			</div>
			<!-- END PAGE HEADER-->
			<form class="demoform">
                <div class="span3" style="margin-left: 0px">
                    <select id="marketSel" name = "marketSelName"  datatype="*"  nullmsg='<spring:message code="map_store_name" />'
                        data-placeholder="<spring:message code="heatmap_place" />"
                        style="width: 100%;height:26px;padding:2px 0 2px 0;">
                        <option value=""></option>
                    </select>
                </div>
                <div class="span2">
                    <input type="button" class="btn btn-primary" value="<spring:message code="common_confirm" />" id="confirm" style="vertical-align: top;">
                </div>
                <div class="span1" id="msgdemo2"></div>             
                <div class = "span6" style="text-align: right;">
                    <div class="btn-group">
                        <button class="btn btn-primary btn-lg dropdown-toggle" type="button" data-toggle="dropdown">
                            <spring:message code="sva_daochu" /><span class="caret"></span>
                        </button>
                        <ul class="dropdown-menu" role="menu">
                            <li><a href="#" id="exportExcel">Excel</a></li>
                            <li><a href="#" id="exportTxt">Txt</a></li>
                        </ul>
                    </div>
                </div>              
            </form>

			<div class="clearfix"></div>
			<div class="tableBox">
				<table id="table" class="table table-bordered">
					<thead>
						<tr>
							<th>ID</th>
							<th>MapID</th>
							<th>X</th>
							<th>Y</th>
							<th>Floor ID</th>
							<th>Check Value</th>
							<th><spring:message code="test_featureBase_featureRadius" /></th>
							<th>user ID</th>
							<th><spring:message code="test_featureBase_gpp" /></th>
							<th><spring:message code="test_featureBase_featureValue" /></th>
							<th><spring:message code="test_featureBase_time" /></th>
						</tr>
					</thead>
				</table>
			</div>

		</div>
		<!-- END PAGE -->
	</div>
	
	<!-- END CONTAINER -->
	<!-- BEGIN FOOTER -->
	<%@ include file="../shared/pageFooter.jsp"%>
	<!-- END FOOTER -->

	<%@ include file="../shared/importJs.jsp"%>
	<!-- BEGIN PAGE LEVEL SCRIPTS -->
	<script
		src="<c:url value='/plugins/data-tables/media/js/jquery.dataTables.js'/>"
		type="text/javascript"></script>
	<script src="<c:url value='/js/app.js'/>" type="text/javascript"></script>
	<script src="<c:url value='/js/featureBase.js'/>" type="text/javascript"></script>
	<!-- END PAGE LEVEL SCRIPTS -->

	<script type="text/javascript">
		var oTable;
		var basePath = "<%=basePath%>";
		var i18n_detail ='<spring:message code="test_table_title_detail" />',
			i18n_chart_title = '<spring:message code="test_popup_chart_title" />',
			i18n_chart_tip = '<spring:message code="test_popup_chart_tip" />',
			i18n_minus = '<spring:message code="test_popup_chart_minus" />',
			i18n_plus = '<spring:message code="test_popup_chart_plus" />',
			i18n_offset = '<spring:message code="test_table_title_offset" />',
			i18n_var ='<spring:message code="test_table_title_variance" />',
			i18n_dataview ='<spring:message code="common_echart_dataview" />',
			i18n_saveimg ='<spring:message code="common_echart_saveimg" />',
			i18n_close ='<spring:message code="common_close" />',
			i18n_Invalid ='<spring:message code="test_table_title_failed" />',
			i18n_Valid ='<spring:message code="test_table_title_success" />',
			i18n_common_info ='<spring:message code="common_echart_info" />',
			i18n_accuracy_static ='<spring:message code="test_table_static" />',
	         i18n_daochu ='<spring:message code="sva_daochu" />',
			i18n_accuracy_dynamic ='<spring:message code="test_table_dynamic" />';
		$(document).ready(function() {
			App.init();
            featureBase.initDropdown();
			featureBase.bindEvent();
		});
	</script>
	<!-- END JAVASCRIPTS -->
</body>
</html>