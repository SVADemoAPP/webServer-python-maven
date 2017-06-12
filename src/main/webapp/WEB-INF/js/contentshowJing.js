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
					updateList("placeSel3",data.data);
				}
			});
			$.get("/sva/store/api/getData?t="+Math.random(),function(data){
				if(!data.error){
					updateList("placeSel4",data.data);
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
    		$("#placeSel3").on("change", function(){
    			var placeIdSp = $("#placeSel3").val();
    			$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeIdSp}, function(data){
    				if(!data.error){
    					updateFloorList1("zSel3",data.data);
    				}
    			});
    		});
    		$("#placeSel4").on("change", function(){
    			var placeIdSp = $("#placeSel4").val();
    			$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeIdSp}, function(data){
    				if(!data.error){
    					updateFloorList1("zSel4",data.data);
    				}
    			});
    		});
 
   		
    		//  确认按钮点击  触发热力图刷新    		
    		$('#confirm').click(function(e){
    			var check = validForm.check(false);
              	if (!check) {
					return false;
				}
    			var floorSel = $("#zSel").val();
    			var floorSel2 = $("#zSel2").val();
    			var floorSel3 = $("#zSel3").val();
    			var floorSel4 = $("#zSel4").val();
    			var periodSel = $("#periodSel").val();
    			var	coefficient = $("#coefficient").val();
    			
    			startTime = $("#select_time_begin_tab1").val();
    			var densitySel = $("#densitySel").val();
    			var radiusSel = $("#radiusSel").val();
    			var densitySel1 = $("#densitySel1").val();
    			var radiusSel1 = $("#radiusSel1").val();
    			var densitySel2 = $("#densitySel2").val();
    			var radiusSel2 = $("#radiusSel2").val();
    			var densitySel4 = $("#densitySel4").val();
    			var radiusSel4 = $("#radiusSel4").val();



    			//修改参数更新数据库
    			var param ={
    					densitySel:densitySel,
    					radiusSel:radiusSel,
    					densitySel1:densitySel1,
    					radiusSel1:radiusSel1,
    					densitySel2:densitySel2,
    					radiusSel2:radiusSel2,
    					densitySel4:densitySel4,
    					radiusSel4:radiusSel4,
    					floorNo : floorSel,
    					floorNo2 :floorSel2,
    					floorNo3 : floorSel3,   				
    					floorNo4 : floorSel4,   				
    					periodSel : periodSel,
    					coefficient : coefficient,
    					startTime : $("#select_time_begin_tab1").val()
    					//startTime :"2016-00-00 00:00:00"
                	};
    			$.ajax({
		              "dataType": 'json', 
		              "type": "POST", 
		              "url": "/sva/content/api/saveData3", 
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
	 $("#densitySel4").val("");

	 $("#radiusSel").val("");
	 $("#radiusSel1").val("");
	 $("#radiusSel2").val("");
	 $("#radiusSel4").val("");

	$("#placeSel").val("");
	$("#placeSel2").val("");
	$("#placeSel3").val("");
	$("#placeSel4").val("");
	$("#zSel").val("");	
	$("#zSel2").val("");	
	$("#zSel3").val("");		
	$("#zSel4").val("");		
	$("#periodSel").val("");	
	$("#coefficient").val("");	
	//$("#select_time_begin_tab1").val("");	
	$("#idid").val("");	

}
function paramValue(){
	$.get("/sva/content/api/getData3?t="+Math.random(),function(data){
		if(!data.error){
			var a = data.data;
			$("#placeSel").val(Math.floor(a[0].floorNo/10000));
    		var placeId = $("#placeSel").val();
			$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeId}, function(data){
				if(!data.error){
					contentshow.updateFloorList1("zSel",data.data);
					$("#zSel").val(a[0].floorNo);	
					}
			});
			$("#placeSel2").val(Math.floor(a[0].floorNo2/10000));
			var placeId2 = $("#placeSel2").val();
			$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeId2}, function(data){
				if(!data.error){
					contentshow.updateFloorList1("zSel2",data.data);
					$("#zSel2").val(a[0].floorNo2);	
				}
			});
			$("#placeSel3").val(Math.floor(a[0].floorNo3/10000));
			var placeId3 = $("#placeSel3").val();
			$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeId3}, function(data){
				if(!data.error){
					contentshow.updateFloorList1("zSel3",data.data);
					$("#zSel3").val(a[0].floorNo3);	
				}
			});
			$("#placeSel4").val(Math.floor(a[0].floorNo4/10000));
			var placeId4 = $("#placeSel4").val();
			$.post("/sva/heatmap/api/getFloorsByMarket",{placeId:placeId3}, function(data){
				if(!data.error){
					contentshow.updateFloorList1("zSel4",data.data);
					$("#zSel4").val(a[0].floorNo4);	
				}
			});
			$("#radiusSel").val(a[0].radiusSel);
			$("#radiusSel1").val(a[0].radiusSel1);
			$("#radiusSel2").val(a[0].radiusSel2);
			$("#radiusSel4").val(a[0].radiusSel4);

			$("#densitySel").val(a[0].densitySel);
			$("#densitySel1").val(a[0].densitySel1);
			$("#densitySel2").val(a[0].densitySel2);
			$("#densitySel4").val(a[0].densitySel4);


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