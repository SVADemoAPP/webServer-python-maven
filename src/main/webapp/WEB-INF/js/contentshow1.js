var contentshow = function() {
	/**
	 * 将对应信息填充到对应的select @ param renderId [string] 标签id @ param data [array]
	 *                   列表数据
	 */
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
	var updateFloorList1 = function(renderId,data,selectTxt,callback){
		$("#floorSel").find("option").remove(); 
	    var sortData = data.sort(function(a,b){return a.floor - b.floor;});
	    var len = sortData.length;
	    var options = '';
	    for(var i=0;i<len;i++){
	    //	var info = sortData[i];
	        if(selectTxt == sortData[i].floor){
	    		options += '<option class="addoption" selected=true value="'+sortData[i].floorNo+'">' + sortData[i].floor +'</option>';
	    	}else{
	    		options += '<option class="addoption"  value="'+sortData[i].floorNo+'">' + sortData[i].floor +'</option>';
	    	}
	    }
	    removeOption(renderId);
	    $('#' + renderId).append(options);
	    return;
	};

	var removeOption = function(renderId) {
		$('#' + renderId + ' .addoption').remove().trigger("liszt:updated");
	};
	
	return {
		updateFloorList1: function(renderId,data,selectTxt,callback){
			updateFloorList1(renderId,data,selectTxt,callback);
		},
		initDropdown : function() {
			$("#editBox").show();
			$("#add").hide();
			$.get("/sva/store/api/getData?t="+Math.random(),function(data){
				if(!data.error){
					updateList("placeSel",data.data);
				}
			});
			$.get("/sva/store/api/getData?t="+Math.random(),function(data){
				if(!data.error){
					updateList("placeSel2",data.data);
				}
			});
			$.get("/sva/store/api/getData?t="+Math.random(),function(data){
				if(!data.error){
					updateList("placeSel2Sp",data.data);
				}
			});
			$.get("/sva/store/api/getData?t="+Math.random(),function(data){
				if(!data.error){
					updateList("placeSel3",data.data);
				}
			});
			$.get("/sva/store/api/getData?t="+Math.random(),function(data){
				if(!data.error){
					updateList("placeSel3Sp",data.data);
				}
			});
			var options = '';
    		for ( var i = 1; i <= 60; i++) {
    			if(i==5){
        			options += '<option class="addoption" selected value="' + i + '">' + i + '</option>';    				
    			}else{
        			options += '<option class="addoption" value="' + i + '">' + i + '</option>';    				
    			}
    		}
    		$('#periodSel').append(options);
    		$('#periodSel').trigger("liszt:updated");
    		paramValue();
		},
		
		
		showDate: function(id){
			WdatePicker({
				el : document.getElementById(id),
				lang : i18n_language,
				isShowClear : false,
				isShowToday:false,
				readOnly : true,
				dateFmt : 'HH:00:00',
				maxDate : '23:00:00',
				skin : "twoer"
			});
		},
		
		bindClickEvent: function(){
			// 地点下拉列表修改 触发楼层下拉列表变化
    		$("#placeSel").on("change", function(){
    			var placeId = $("#placeSel").val();
				$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeId}, function(data){
					if(!data.error){
						updateFloorList1("zSel",data.data);
						
					}
				});
	    		$('a[href="#myModal1"]').attr("disabled","disabled");
	    		$("#search").hide();
			});
    		$("#placeSel2").on("change", function(){
    			var placeId = $("#placeSel2").val();
    			$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeId}, function(data){
    				if(!data.error){
    					updateFloorList1("zSel2",data.data);
    				}
    			});
    			$('a[href="#myModal1"]').attr("disabled","disabled");
    			$("#search").hide();
    		});
    		$("#placeSel2Sp").on("change", function(){
    			var placeIdSp = $("#placeSel2Sp").val();
    			$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeIdSp}, function(data){
    				if(!data.error){
    					updateFloorList1("zSel2Sp",data.data);
    				}
    			});
    		});
    		$("#placeSel3").on("change", function(){
    			var placeId = $("#placeSel3").val();
    			$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeId}, function(data){
    				if(!data.error){
    					updateFloorList1("zSel3",data.data);
    				}
    			});
    			$('a[href="#myModal1"]').attr("disabled","disabled");
    			$("#search").hide();
    		});
    		$("#placeSel3Sp").on("change", function(){
    			var placeId = $("#placeSel3Sp").val();
    			$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeId}, function(data){
    				if(!data.error){
    					updateFloorList1("zSel3Sp",data.data);
    				}
    			});
    		});
    		
    		//  确认按钮点击  触发热力图刷新    		
    		$('#confirm').click(function(e){
    			var check = validForm.check(false);
              	if (!check) {
					return false;
				}
    			var placeId = $("#placeSel").val();
    			var placeId2 = $("#placeSel2").val();
    			var placeId2Sp = $("#placeSel2Sp").val();
    			var placeId3 = $("#placeSel3").val();
    			var placeId3Sp = $("#placeSel3Sp").val();
    			var floorSel = $("#zSel").val();
    			var floorSel2 = $("#zSel2").val();
    			var floorSel2Sp = $("#zSel2Sp").val();
    			var floorSel3 = $("#zSel3").val();
    			var floorSel3Sp = $("#zSel3Sp").val();
    			var periodSel = $("#periodSel").val();
    			var	coefficient = $("#coefficient").val();
    			
    			startTime = $("#select_time_begin_tab1").val();
    			var densitySel = $("#densitySel").val();
    			var radiusSel = $("#radiusSel").val();
    			var densitySel1 = $("#densitySel1").val();
    			var radiusSel1 = $("#radiusSel1").val();
    			var densitySel2 = $("#densitySel2").val();
    			var radiusSel2 = $("#radiusSel2").val();
    			//floor全部空为不提交
    			if (floorSel==" " && floorSel2==" " && floorSel2Sp==" " && floorSel3==" " && floorSel3Sp==" ")
    			{
    				return false;
    			}
    			//空值默认转0
    			if (placeId==" ")	//1
    			{
    				placeId=0;
    			}
    			if (placeId2==" ")	//2
    			{
    				placeId2=0;
    			}
    			if (placeId2Sp==" ")	//3
    			{
    				placeId2Sp=0;
    			}
    			if (placeId3==" ")	//4
    			{
    				placeId3=0;
    			}
    			if (placeId3Sp==" ")	//5
    			{
    				placeId3Sp=0;
    			}
    			if (floorSel==" ")	//6
    			{
    				floorSel=0;
    			}
    			if (floorSel2==" ")	//7
    			{
    				floorSel2=0;
    			}
    			if (floorSel2Sp==" "||floorSel2Sp==null)//8
    			{
    				floorSel2Sp=0;
    			}
    			if (floorSel3==" ")	//9
    			{
    				floorSel3=0;
    			}
    			if (floorSel3Sp==" ")//10
    			{
    				floorSel3Sp=0;
    			}
    			//修改参数更新数据库
    			var param ={
    					densitySel:densitySel,
    					radiusSel:radiusSel,
    					densitySel1:densitySel1,
    					radiusSel1:radiusSel1,
    					densitySel2:densitySel2,
    					radiusSel2:radiusSel2,
    					placeId : placeId,
    					placeId2 : placeId2,
    					placeId2Sp : placeId2Sp,
    					placeId3 : placeId3,
    					placeId3Sp : placeId3Sp,
    					floorNo : floorSel,
    					floorNo2 :floorSel2,
    					floorNo2Sp :floorSel2Sp,
    					floorNo3 : floorSel3,
    					floorNo3Sp : floorSel3Sp,
    					periodSel : periodSel,
    					coefficient : coefficient,
    					startTime : $("#select_time_begin_tab1").val()
    					//startTime :"2016-00-00 00:00:00"
                	};
    			$.ajax({
		              "dataType": 'json', 
		              "type": "POST", 
		              "url": "/sva/content/api/saveData1", 
		              "data": param, 
		              "success": function(data){
		            	  if(data.error){
		            		  
		            		 // $("#alertBox").show();
		            	  }else{
//		            		  $("#editBox").hide();
		            		  alert("success!");
//		            		  clearinfo();
//		            		  refreshTable();
		            	  }
		              }
		        });
    		});

    		$("#hour").click(function(e){
    			placeId = $("#marketSel").val();
    			startTime = $("#select_time_begin_tab1").val();
    	//		endTime = $("#select_time_end_tab1").val();
    			
				showChart(placeId, startTime, 1);
    		});
    		
    		$("#day").click(function(e){
    			placeId = $("#marketSel").val();
    			startTime = $("#select_time_begin_tab1").val();
    			//endTime = $("#select_time_end_tab1").val();
    			
				showChart(placeId, startTime,2);
    		});
    		$("#hour1").click(function(e){
    			var param = {
    					id:1,
    					placeId :placeId,
    					timeBegin :startTime 
    				
    			};
    			getAreaName(param);
    		});
    		
    		$("#day1").click(function(e){
    			var param = {
    					id:2,
    					placeId :placeId,
    					timeBegin :startTime 
    					//timeEnd :endTime 
    			};
    			
    			getAreaName(param);
    		});
    		
		}
	};

}();
$("#add").click(function(e){
	
	$(".demoform").Validform().resetForm();
	$(".sameInfo").removeClass("Validform_wrong");
	$(".sameInfo").text("");
	clearinfo();
	$("#editBox").show();	
});
function hideAdd()
{
	$(".demoform").Validform().resetForm();
	clearinfo();
	$("#editBox").show();
}
function clearinfo()
{
	 $("#densitySel").val("");
	 $("#densitySel1").val("");
	 $("#densitySel2").val("");
	 $("#radiusSel").val("");
	 $("#radiusSel1").val("");
	 $("#radiusSel2").val("");
	$("#placeSel").val("");
	$("#placeSel2").val("");
	$("#placeSel2Sp").val("");
	$("#placeSel3").val("");
	$("#placeSel3Sp").val("");
	$("#zSel").val("");	
	$("#zSel2").val("");	
	$("#zSel2Sp").val("");	
	$("#zSel3").val("");	
	$("#zSel3Sp").val("");	
	$("#periodSel").val("");	
	$("#coefficient").val("");	
	//$("#select_time_begin_tab1").val("");	
	$("#idid").val("");	

}
function paramValue(){
	$.get("/sva/content/api/getData1?t="+Math.random(),function(data){
		if(!data.error){
			var a = data.data;
			$("#placeSel").val(a[0].placeId);
    		var placeId = $("#placeSel").val();
			$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeId}, function(data){
				if(!data.error){
					contentshow.updateFloorList1("zSel",data.data);
					$("#zSel").val(a[0].floorNo);	
					}
			});
			$("#placeSel2").val(a[0].placeId2);
			var placeId2 = $("#placeSel2").val();
			$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeId2}, function(data){
				if(!data.error){
					contentshow.updateFloorList1("zSel2",data.data);
					$("#zSel2").val(a[0].floorNo2);	
				}
			});
			$("#placeSel2Sp").val(a[0].placeId2sp);
			var placeId2sp = $("#placeSel2Sp").val();
			$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeId2sp}, function(data){
				if(!data.error){
					contentshow.updateFloorList1("zSel2Sp",data.data);
					$("#zSel2Sp").val(a[0].floorNo2sp);	
				}
			});
			$("#placeSel3").val(a[0].placeId3);
			var placeSel3 = $("#placeSel3").val();
			$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeSel3}, function(data){
				if(!data.error){
					contentshow.updateFloorList1("zSel3",data.data);
					$("#zSel3").val(a[0].floorNo3);		
				}
			});
			$("#placeSel3Sp").val(a[0].placeId3sp);
			var placeSel3Sp = $("#placeSel3Sp").val();
			$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeSel3Sp}, function(data){
				if(!data.error){
					contentshow.updateFloorList1("zSel3Sp",data.data);
					$("#zSel3Sp").val(a[0].floorNo3sp);		
				}
			});	
			$("#radiusSel").val(a[0].radiusSel);
			$("#radiusSel1").val(a[0].radiusSel1);
			$("#radiusSel2").val(a[0].radiusSel2);
			$("#densitySel").val(a[0].densitySel);
			$("#densitySel1").val(a[0].densitySel1);
			$("#densitySel2").val(a[0].densitySel2);
			$("#periodSel").val(a[0].periodSel);
			$("#coefficient").val(a[0].coefficient);
			$("#select_time_begin_tab1").val(a[0].startTimeEmp);
			
		}
	});
}

function closeModel()
{
	$("#myModal").modal('hide');
	//$("#movie").empty();
}