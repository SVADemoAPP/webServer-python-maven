var featureBase = function () {
	// 更新商场
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
	
	// excel导出按钮点击事件
	var bindExcelClickEvent = function(){
		// 导出excel点击
		$("#exportExcel").click(function(e){
			var placeId = $("#marketSel").val();
			if(!placeId){
				return false;
			}
			window.location.href = '/sva/featureBase/api/ExportExcel?placeId='+placeId;
		});
		
		// 导出txt点击
		$("#exportTxt").click(function(e){
			var placeId = $("#marketSel").val();
			if(!placeId){
				return false;
			}
			window.location.href = '/sva/featureBase/api/ExportTxt?placeId='+placeId;
		});
	};
	
	var bindConfirmClickEvent = function(){
		$("#confirm").click(function(e){
			var placeId = $("#marketSel").val();
			if(!placeId){
				return false;
			}
			
			var param = {
				placeId :placeId
			};

			$.post("/sva/featureBase/api/getTableData",param,function(data){
	    		if(!data.error){
	    			if(oTable){oTable.fnDestroy();};
	    			oTable = $('#table').dataTable({
	    				"bProcessing": true,
	    				"sDom": 'rt<"toolbar"lp<"clearer">>',
	    				"sPaginationType": "full_numbers",
	    				"aaData":data.data,
	    				"bStateSave": true,
	    				"aoColumnDefs": 
	    				[
							{ 
								"aTargets": [0],
								"mData": "id" 
							},
							{ 
								"aTargets": [1],
								"mData": "mapId",
								
							},
							{ 
								"aTargets": [2],
								"mData": "x"
							},
							{ 
								"aTargets": [3],
								"mData": "y"
							},
							{ 
								"aTargets": [4],
								"mData": "floorid"
							},
							{ 
								"aTargets": [5],
								"mData": "checkValue"
							},
							{ 
								"aTargets": [6],
								"mData": "featureRadius"
							},
							{ 
								"aTargets": [7],
								"mData": "userId"
							},
							{ 
								"aTargets": [8],
								"mData": "gpp"
							},
							{ 
								"aTargets": [9],
								"mData": "featureValue"
							},
							{ 
								"aTargets": [10],
								"mData": "formatDate"
							}
						]
	    			});
	    		}  
				
			});
		});
	};
	
	return {
        
		initDropdown : function() {
			$.get("/sva/store/api/getData?t="+Math.random(),function(data){
				if(!data.error){
					updateList("marketSel",data.data);
				}
			});
		},
		
		bindEvent : function(){
			bindExcelClickEvent();
			bindConfirmClickEvent();
		}
    
    };

}();