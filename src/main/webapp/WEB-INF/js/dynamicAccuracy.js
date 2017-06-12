$("#exportButton").click(function(e){
	
	window.location.href = basePath +'dynamicAccuracy/api/dynamicExportCodeTemplate';
	})
	
var dynamicAccuracy = function () {
	return {
		initTable:function(){
			placeId = $("#marketSel").val();
			startTime = $("#select_time_begin_tab1").val();
			endTime = $("#select_time_end_tab1").val();
			if (startTime=="") {
				startTime = "1016-06-23 14:30:12";
				endTime = "3016-06-23 14:30:12";
			}
			var param = {
					placeId :placeId,
					startTime :startTime ,
					endTime :endTime 
			};
			$.post("/sva/dynamicAccuracy/api/getDynamicTableDataNew",param,function(data){
        		if(!data.error){
        			if(oTable){oTable.fnDestroy();};
        			oTable = $('#table').dataTable({
        				"bProcessing": true,
        				"sDom": 'rt<"toolbar"lp<"clearer">>',
        				"sPaginationType": "full_numbers",
        				"aaData":data.data,
        				"bStateSave": true,
        				"aoColumnDefs": [
        									{ 
        										"aTargets": [0],
        										"bVisible": false,
        										"mData": "id" 
        									},
        									{ 
        										"aTargets": [1],
        										"mData": "place",
        										
        									},
        									{ 
        										"aTargets": [2],
        										"mData": "floor"
        									},
        									{ 
        										"aTargets": [3],
        										"mData": "origin"
        									},
        									{ 
        										"aTargets": [4],
        										"mData": "destination",
        										"mRender": function ( data, type, full ) {
        											if(!data){
        												return "-";
        											}else{
        												return data;
        											}
        										}
        									},
        									{ 
        										"aTargets": [5],
        										"mData": "startdate",
        										"mRender": function ( data, type, full ) {
        											var date = new Date();
        											date.setTime(data);
        											return dateFormat(date,"yyyy/MM/dd HH:mm:ss");
        										}
        									},
        									{ 
        										"aTargets": [6],
        										"mData": "enddate",
        										"mRender": function ( data, type, full ) {
        											var date = new Date();
        											date.setTime(data);
        											return dateFormat(date,"yyyy/MM/dd HH:mm:ss");
        										}
        									},
        									{ 
        										"aTargets": [7],
        										"mData": "triggerIp"
        									},
        									{ 
        										"aTargets": [8],
        										"mData": "avgeOffset"
        									},
        									{ 
        										"aTargets": [9],
        										"mData": "maxOffset",
        									},
        									{ 
        										"aTargets": [10],
        										"mData": "offset"
        									},
        									
        									{
        					                    "aTargets": [11],
        					                    "bSortable": false,
        					                    "bFilter": false,
        					                    "mData": function(source, type, val) {
        					                        return "";
        					                    },
        					                    "mRender": function ( data, type, full ) {
        					                    	console.log(full);
        					                        return '<a data-type="detail" data-toggle="modal" href="#modal" data-id="'+full.id+'">'+i18n_detail+'</a>';
        					                      }
        					                }
        								],
        				"fnCookieCallback": function (sName, oData, sExpires, sPath) {      
        					// Customise oData or sName or whatever else here     
        					var newObj = {iLength:oData.iLength};
        					return sName + "="+JSON.stringify(newObj)+"; expires=" + sExpires +"; path=" + sPath;    
        				}
        			});
        			
    				var myVisitChart = echarts.init(document.getElementById("chart3"));
    				var option = {
//    				    title : {
//    				        text: i18n_title
//    				    },
    				    legend: {
    				        data:data.CDFName
    				    },
    				    tooltip : {
    				        trigger: 'axis'
    				    },
    				    toolbox: {
    				        show : true,
    				        feature : {
    				            dataView : {
    				            	show: true, 
    				            	title : i18n_dataview,
    				            	readOnly: true,
    				                lang: [i18n_dataview, i18n_close, i18n_refresh]
    				            },
    				            saveAsImage : {
    				            	show: true,
    				            	title : i18n_saveimg
    				            },
    				            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
    				            restore : {show: true}
    				        }
    				    },
    				    calculable : false,
    				    xAxis : [
    				        {
    				            type : 'category',
    				            boundaryGap : false,
    				            data : data.xValue,
    				            axisLabel : {
    				                formatter: '{value} '+i18n_visitTitle
    				            }
    				        }
    				    ],
    				    yAxis : [
    				        {
    				            type : 'value',
    				            axisLabel : {
    				                formatter: '{value} '+i18n_cishu
    				            }
    				        }
    				    ],
    				    series : data.CDFdata
    				};                  
    				
    				myVisitChart.setOption(option);	
        		}  
				
			});
        },
		initDropdown : function() {
			$.get("/sva/store/api/getData?t="+Math.random(),function(data){
				if(!data.error){
					updateList("marketSel",data.data);
				}
				$('#myTab a:last').tab('show');
				$('#myTab a:first').tab('show');
			});
		}, 
		showDate: function(id){
			WdatePicker({
				el : document.getElementById(id),
				lang : i18n_language,
				isShowClear : false,
				isShowToday:false,
				readOnly : true,
				dateFmt : 'yyyy-MM-dd HH:00:00',
				maxDate : '%y-%M-%d 23:00:00',
				skin : "twoer"
			});
		},
        initPopupText: function(data){
        	if(data){
				var val = '';
				if (data.offset<=data.deviation) {
					 val = i18n_Valid;
				}else{
					val = i18n_Invalid;
				}
	        	$("#place").text(data.place);
	        	$("#floor").text(data.floor);
	        	$("#x").text(data.origin);
	        	$("#y").text(data.destination);
		        	var startTime = data.startdate;
		        	var date = new Date();
		        	date.setTime(startTime);
		        	startTime =  dateFormat(date,"yyyy/MM/dd HH:mm:ss");
		        $("#startTime").text(startTime);
			        var endTime = data.enddate;
		        	var date = new Date();
		        	date.setTime(endTime);
		        	endTime =  dateFormat(date,"yyyy/MM/dd HH:mm:ss");
	        	$("#endTime").text(endTime);
	        	$("#trigger").text(data.triggerIp);
	        	$("#avgeOffset").text(data.avgeOffset);
				$("#maxOffset").text(data.maxOffset);
				$("#offset").text(data.offset);
				
        	}
        },
        
		clickComfirm : function()
		{
			placeId = $("#marketSel").val();
			startTime = $("#select_time_begin_tab1").val();
			endTime = $("#select_time_end_tab1").val();
			if(startTime>=endTime)
			{
				$("#msgdemo2").removeClass("Validform_right");
				$("#msgdemo2").addClass("Validform_wrong");
				$("#msgdemo2").text(i18n_time);
				return false;
			}
			$("#msgdemo2").removeClass("Validform_wrong");
			$("#msgdemo2").addClass("Validform_right");
			$("#msgdemo2").text(pa);
			
			var param = {
					placeId :placeId,
					startTime :startTime ,
					endTime :endTime 
			};
			$.post("/sva/dynamicAccuracy/api/getDynamicTableDataNew",param,function(data){
        		if(!data.error){
        			if(oTable){oTable.fnDestroy();};
        			oTable = $('#table').dataTable({
        				"bProcessing": true,
        				"sDom": 'rt<"toolbar"lp<"clearer">>',
        				"sPaginationType": "full_numbers",
        				"aaData":data.data,
        				"bStateSave": true,
        				"aoColumnDefs": [
        									{ 
        										"aTargets": [0],
        										"bVisible": false,
        										"mData": "id" 
        									},
        									{ 
        										"aTargets": [1],
        										"mData": "place",
        										
        									},
        									{ 
        										"aTargets": [2],
        										"mData": "floor"
        									},
        									{ 
        										"aTargets": [3],
        										"mData": "origin"
        									},
        									{ 
        										"aTargets": [4],
        										"mData": "destination",
        										"mRender": function ( data, type, full ) {
        											if(!data){
        												return "-";
        											}else{
        												return data;
        											}
        										}
        									},
        									{ 
        										"aTargets": [5],
        										"mData": "startdate",
        										"mRender": function ( data, type, full ) {
        											var date = new Date();
        											date.setTime(data);
        											return dateFormat(date,"yyyy/MM/dd HH:mm:ss");
        										}
        									},
        									{ 
        										"aTargets": [6],
        										"mData": "enddate",
        										"mRender": function ( data, type, full ) {
        											var date = new Date();
        											date.setTime(data);
        											return dateFormat(date,"yyyy/MM/dd HH:mm:ss");
        										}
        									},
        									{ 
        										"aTargets": [7],
        										"mData": "triggerIp"
        									},
        									{ 
        										"aTargets": [8],
        										"mData": "avgeOffset"
        									},
        									{ 
        										"aTargets": [9],
        										"mData": "maxOffset",
        									},
        									{ 
        										"aTargets": [10],
        										"mData": "offset"
        									},
        									
        									{
        					                    "aTargets": [11],
        					                    "bSortable": false,
        					                    "bFilter": false,
        					                    "mData": function(source, type, val) {
        					                        return "";
        					                    },
        					                    "mRender": function ( data, type, full ) {
        					                    	console.log(full);
        					                        return '<a data-type="detail" data-toggle="modal" href="#modal" data-id="'+full.id+'">'+i18n_detail+'</a>';
        					                      }
        					                }
        								],
        				"fnCookieCallback": function (sName, oData, sExpires, sPath) {      
        					// Customise oData or sName or whatever else here     
        					var newObj = {iLength:oData.iLength};
        					return sName + "="+JSON.stringify(newObj)+"; expires=" + sExpires +"; path=" + sPath;    
        				}
        			});
        			
    				var myVisitChart = echarts.init(document.getElementById("chart3"));
    				var option = {
//    				    title : {
//    				        text: i18n_title
//    				    },
    				    legend: {
    				        data:data.CDFName
    				    },
    				    tooltip : {
    				        trigger: 'axis'
    				    },
    				    toolbox: {
    				        show : true,
    				        feature : {
    				            dataView : {
    				            	show: true, 
    				            	title : i18n_dataview,
    				            	readOnly: true,
    				                lang: [i18n_dataview, i18n_close, i18n_refresh]
    				            },
    				            saveAsImage : {
    				            	show: true,
    				            	title : i18n_saveimg
    				            },
    				            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
    				            restore : {show: true}
    				        }
    				    },
    				    calculable : false,
    				    xAxis : [
    				        {
    				            type : 'category',
    				            boundaryGap : false,
    				            data : data.xValue,
    				            axisLabel : {
    				                formatter: '{value} '+i18n_visitTitle
    				            }
    				        }
    				    ],
    				    yAxis : [
    				        {
    				            type : 'value',
    				            axisLabel : {
    				                formatter: '{value} '+i18n_cishu
    				            }
    				        }
    				    ],
    				    series : data.CDFdata
    				};                  
    				
    				myVisitChart.setOption(option);	
        		}  
				
			});
			
		},
        initPopupChart: function(data){
        	var dataObj = [{
	        		name:"3m "+i18n_minus,value:data.count3
	        	},{
	        		name:"3m-5m",value:data.count5
	        	},{
	        		name:"5m-10m",value:data.count10
	        	},{
	        		name:"10m "+i18n_plus,value:data.count10p
	        	}
        	];
        	
        	var myChart = echarts.init(document.getElementById("chart")); 
        	var val = '';
        	if (data.offset<data.variance+1) {
				 val = i18n_Valid;
			}else
				{
				val = i18n_Invalid;
				}
        	var option = {
        		customData:data,
    		    title : {
    		        text: i18n_chart_title,
    		        x:'center'
    		    },
    		    tooltip : {
    		        trigger: 'item',
    		        formatter: "{a} <br/>{b} : {c} ({d}%)"
    		    },
    		    legend: {
    		        orient : 'vertical',
    		        x : 'left',
    		        data:['3m '+i18n_minus,'3m-5m','5m-10m','10m '+i18n_plus]
    		    },
    		    toolbox: {
    		        show : true,
    		        feature : {
    		            dataView : {
    		            	show: true, 
    		            	title:i18n_dataview,
    		            	readOnly: true,
    		            	lang:[i18n_dataview,i18n_close],
    		            	optionToContent: function(opt) {
    		            		var dataStr = opt.customData.detail;
    		            	//	var xo = opt.customData.x;
    		            	//	var yo = opt.customData.y;
    		            		var dataObj = JSON.parse(dataStr);
    		            		var table = '<table style="width:100%;text-align:center"><tbody><tr>'
                                    + '<td>'+i18n_common_info+'</td>'
                                    + '</tr>';
    		            		for (var i = 0, l = dataObj.length; i < l; i++) {
    		            			//var offset = Math.sqrt(Math.pow(Math.abs(dataObj[i].x-xo),2)+Math.pow(Math.abs(dataObj[i].y-yo),2));
    		            			table += '<tr><td>'
		                                  + i + '; '
		                                  + dataObj[i].x + '; '
		                                  + dataObj[i].y + '; '
		                                  + dataObj[i].timestamp + '; '
		                                 // + offset.toFixed(2)
		                                  + '</td></tr>';
    		            		}
    		            		table += '</tbody></table>';
                       
    		            		return table;
    		            	}
    		            },
    		            magicType : {
    		                show: false, 
    		                type: ['pie', 'funnel'],
    		                option: {
    		                    funnel: {
    		                        x: '25%',
    		                        width: '50%',
    		                        funnelAlign: 'left',
    		                        max: 1548
    		                    }
    		                }
    		            },
    		            restore : {show: false},
    		            saveAsImage : {show: true,title:i18n_saveimg}
    		        }
    		    },
    		    series : [
    		        {
    		            name:i18n_chart_tip,
    		            type:'pie',
    		            radius : '55%',
    		            center: ['50%', '60%'],
    		            data:dataObj
    		        }
    		    ]
    		};
        	
        	myChart.setOption(option);
        		                    
        }
    
    };

}();
var updateList = function(renderId, data, callback) {
	var sortData = data.sort(function(a,b){return a.name - b.name;});
    var len = sortData.length;
    var options = '';
    for(var i=0;i<len;i++){
    	var info = sortData[i];
		options += '<option class="addoption" value="'+info.id+'">' + HtmlDecode3(info.name) +'</option>';
    }
    removeOption(renderId);
    $('#' + renderId).append(options);
    if(callback){
        callback();
    }
    return;
};
var removeOption = function(renderId) {
	$('#' + renderId + ' .addoption').remove().trigger("liszt:updated");
};