var responeData = "";
var responeData2= "";
var responeData3 = "";

var floorNo = "";
var floorNo2 = "";
var floorNo3 = "";

var floorNo8 = "";
var radiusSel1 = "";
var radiusSel2 = "";
var radiusSel = "";

var maxValue = "";
var maxValue1 = "";
var maxValue2 = "";
var imgHeight, imgWidth, imgScale;
var imgHeight2, imgWidth2, imgScale2;
var imgHeight3, imgWidth3, imgScale3;

//var floorNo3sp = "";
var period  = "";
var timer;
var timer2;
var timer3;
var currentWidth = screen.width;
var coefficient;
var refreshHeatmapData = function() {
	clearTimeout(timer);
	$.post("/sva/heatmap/api/getData5?t="+Math.random(), {floorNo :floorNo,period:period}, function(data) {
		if (!data.error) {
			if (data.data && data.data.length > 0) {
				// var points = {max:1,data:dataFilter(data)};
				var points = dataFilter(data.data, origX, origY, scale,
						imgWidth, imgHeight, coordinate, imgScale);
				var dataObj = {
					max : maxValue,
					min : 1,
					data : points
				};
				heatmap.setData(dataObj);
			}else{
				var canvas=$("#heatmap canvas")[0];
				var ctx=canvas.getContext('2d');
				ctx.clearRect(0,0,bgImgWidth,bgImgHeight);
			}
			responeData = data;
			$("#number1").text(data.data.length*coefficient);
			//$("#numbern-data-item1").text(data.data.length);
		}
		timer = setTimeout("refreshHeatmapData();", 4000);
	});
};
var refreshHeatmapData2 = function() {
	clearTimeout(timer2);
	$.post("/sva/heatmap/api/getData5?t="+Math.random(), {floorNo :floorNo2,period:period}, function(data) {
		if (!data.error) {
			if (data.data && data.data.length > 0) {
				// var points = {max:1,data:dataFilter(data)};
				var points2= dataFilter2(data.data, origX2, origY2, scale2,
						imgWidth2, imgHeight2, coordinate2, imgScale2);
				var dataObj2 = {
					max : maxValue1,
					min : 1,
					data : points2
				};
				heatmap2.setData(dataObj2);
			}else{
				var canvas=$("#heatmap2 canvas")[0];
				var ctx=canvas.getContext('2d');
				ctx.clearRect(0,0,bgImgWidth2,bgImgHeight2);
			}
			responeData2 = data;
//			$("#number2").text(data.data.length*coefficient);
			$("#numbern-data-item4").text(data.data.length*coefficient);
			//$("#numbern-data-item1").text(data.data.length);
		}
		 timer2 = setTimeout("refreshHeatmapData2();", 4000);
	});
};

var refreshHeatmapData3 = function() {
	clearTimeout(timer3);
	$.post("/sva/heatmap/api/getData5?t="+Math.random(), {floorNo :floorNo3,period:period}, function(data) {
		if (!data.error) {
			if (data.data && data.data.length > 0) {
				// var points = {max:1,data:dataFilter(data)};
				var points3 = dataFilter3(data.data, origX3, origY3, scale3,
						imgWidth3, imgHeight3, coordinate3, imgScale3);
				var dataObj3 = {
					max : maxValue2,
					min : 1,
					data : points3
				};
				heatmap3.setData(dataObj3);
				$("#numbern-data-item7").text(data.data.length*coefficient);
			}else{
				var canvas=$("#heatmap3 canvas")[0];
				var ctx=canvas.getContext('2d');
				ctx.clearRect(0,0,bgImgWidth3,bgImgHeight3);
			}
			responeData3 = data;
//			$("#number3").text(data.data.length*coefficient);
			//$("#numbern-data-item1").text(data.data.length);
		}
		 timer3 = setTimeout("refreshHeatmapData3();", 4000);
	});
};



var dataFilter = function(data, xo, yo, scale, width, height, coordinate,
		imgScale) {
	var list = [];
	xo = parseFloat(xo);
	yo = parseFloat(yo);
	scale = parseFloat(scale);
	switch (coordinate){
	case "ul":
		for ( var i in data) {
			var point = {
				x : (data[i].x / 10 * scale + xo * scale)/imgScale+Math.random()/10,
				y : (data[i].y / 10 * scale + yo * scale)/imgScale +Math.random()/10,
				value : 1
			};
			list.push(point);
		}
		break;
	case "ll":
		for ( var i in data) {
			var point = {
				x : (data[i].x / 10 * scale + xo * scale)/imgScale +Math.random()/10,
				y : height - (data[i].y / 10 * scale + yo * scale)/imgScale +Math.random()/10,
				value : 1
			};
			list.push(point);
		}
		break;
	case "ur":
		for ( var i in data) {
			var point = {
				x : width - (data[i].x / 10 * scale + xo * scale)/imgScale +Math.random()/10,
				y : (data[i].y / 10 * scale + yo * scale)/imgScale + Math.random()/10,
				value : 1
			};
			list.push(point);
		}
		break;
	case "lr":
		for ( var i in data) {
			var point = {
				x : width - (data[i].x / 10 * scale + xo * scale)/imgScale +Math.random()/10,
				y : height - (data[i].y / 10 * scale + yo * scale)/imgScale +Math.random()/10,
				value : 1
			};
			list.push(point);
		}
		break;
	}

	return list;
};
var dataFilter2 = function(data, xo, yo, scale, width2, height2, coordinate,
		imgScale) {
	var list = [];
	xo2 = parseFloat(xo);
	yo2 = parseFloat(yo);
	scale2 = parseFloat(scale);
	switch (coordinate){
	case "ul":
		for ( var i in data) {
			var point = {
				x : (data[i].x / 10 * scale2 + xo2 * scale2)/imgScale +Math.random()/10,
				y : (data[i].y / 10 * scale2 + yo2 * scale2)/imgScale +Math.random()/10,
				value : 1
			};
			list.push(point);
		}
		break;
	case "ll":
		for ( var i in data) {
			var point = {
				x : (data[i].x / 10 * scale2 + xo2 * scale2)/imgScale +Math.random()/10,
				y : height2 - (data[i].y / 10 * scale2 + yo2 * scale2)/imgScale +Math.random()/10,
				value : 1
			};
			list.push(point);
		}
		break;
	case "ur":
		for ( var i in data) {
			var point = {
				x : width2 - (data[i].x / 10 * scale2 + xo2 * scale2)/imgScale +Math.random()/10,
				y : (data[i].y / 10 * scale2 + yo2 * scale2)/imgScale +Math.random()/10,
				value : 1
			};
			list.push(point);
		}
		break;
	case "lr":
		for ( var i in data) {
			var point = {
				x : width2 - (data[i].x / 10 * scale2 + xo2 * scale2)/imgScale +Math.random()/10,
				y : height2 - (data[i].y / 10 * scale2 + yo2 * scale2)/imgScale +Math.random()/10,
				value : 1
			};
			list.push(point);
		}
		break;
	}

	return list;
};

var dataFilter3 = function(data, xo, yo, scale, width3, height3, coordinate,
		imgScale) {
	var list = [];
	var xo3 = parseFloat(xo);
	var yo3 = parseFloat(yo);
	var scale3 = parseFloat(scale);
	switch (coordinate){
	case "ul":
		for ( var i in data) {
			var point = {
				x : (data[i].x / 10 * scale3 + xo3 * scale3)/imgScale +Math.random()/10,
				y : (data[i].y / 10 * scale3 + yo3 * scale3)/imgScale +Math.random()/10,
				value : 1
			};
			list.push(point);
		}
		break;
	case "ll":
		for ( var i in data) {
			var point = {
				x : (data[i].x / 10 * scale3 + xo3 * scale3)/imgScale +Math.random()/10,
				y : height3 - (data[i].y / 10 * scale3 + yo3 * scale3)/imgScale +Math.random()/10,
				value : 1
			};
			list.push(point);
		}
		break;
	case "ur":
		for ( var i in data) {
			var point = {
				x : width3 - (data[i].x / 10 * scale3 + xo3 * scale3)/imgScale +Math.random()/10,
				y : (data[i].y / 10 * scale3 + yo3 * scale3)/imgScale +Math.random()/10,
				value : 1
			};
			list.push(point);
		}
		break;
	case "lr":
		for ( var i in data) {
			var point = {
				x : width3 - (data[i].x / 10 * scale3 + xo3 * scale3)/imgScale +Math.random()/10,
				y : height3 - (data[i].y / 10 * scale3 + yo3 * scale3)/imgScale +Math.random()/10,
				value : 1
			};
			list.push(point);
		}
		break;
	}

	return list;
};




var calImgSize = function(width, height) {
	var newWidth, newHeight, imgScale;
	var divWidth = parseInt($("#wrapper").css("width").slice(0, -2));

	imgScale = width / divWidth;
	newWidth = divWidth;
	newHeight = height / imgScale;

	return [ imgScale, newWidth, newHeight ];
};

var calImgSize3 = function(width, height) {
	var newWidth, newHeight, imgScale;
	var divWidth = parseInt($("#imge3").css("width").slice(0, -2));
	
	imgScale = width / divWidth;
	newWidth = divWidth;
	newHeight = height / imgScale;
	return [ imgScale, newWidth, newHeight ];
};

var countTime;
var countTime2;
var countTime3;

function count(coefficient){
	clearTimeout(countTime);
	if(coefficient == 0){
		coefficient = coefficient + 1;
	}
	if(responeData == ""){
		countTime = setTimeout("count(coefficient)",1000);
	}else{
//		$("#user1").text(Math.ceil((responeData.data.length)*coefficient));
		countTime = setTimeout("count(coefficient)",60000);
	}
}
function count2(){
	clearTimeout(countTime2);
	if(responeData2 == ""){
		countTime2 = setTimeout("count2()",1000);
	}else{
//		$("#user2").text(responeData2.data.length);
		countTime2 = setTimeout("count2()",60000);
	}
}
function count3(){
	clearTimeout(countTime3);
	if(responeData3 == ""){
		countTime3 = setTimeout("count3()",1000);
	}else{
//		$("#user3").text(responeData3.data.length);
		countTime3 = setTimeout("count3()",60000);
	}
}


var Heatmap = function() {

	/**
	 * 将对应信息填充到对应的select @ param renderId [string] 标签id @ param data [array]
	 *                   列表数据
	 */
	var initHeatmap = function(floorNo,periodSel) {
		$("#heatmap-slide1-graphic").css("background-image","");
		$("#heatmap").empty();
		$.post("/sva/heatmap/api/getMapInfoByPosition", {
			floorNo : floorNo
		}, function(data) {
			if (!data.error) {
				if (data.bg) {
					// 全局变量赋值
					origX = data.xo;
					origY = data.yo;
					bgImg = data.bg;
					bgImgWidth = data.bgWidth;
					bgImgHeight = data.bgHeight;
					scale = data.scale;
					coordinate = data.coordinate;
					// 设置背景图片
					var bgImgStr = "url(../upload/" + bgImg + ")";
					var imgInfo = calImgSize(bgImgWidth, bgImgHeight);
					imgScale = imgInfo[0];
					imgWidth = imgInfo[1];
					imgHeight = imgInfo[2];
					$("#heatmap-slide1-graphic").css({
						"width" : imgWidth + "px",
						"height" : imgHeight + "px",
						"background-image" : bgImgStr,
						"background-size" : imgWidth + "px " + imgHeight + "px",
						"margin" : "0% auto"
					});
					
					configObj.onExtremaChange = function(data) {
						updateLegend(data);
					};
					
					heatmap = h337.create(configObj);
					$.post("/sva/heatmap/api/getData5?t="+Math.random(), {
						floorNo : floorNo,period:periodSel
					}, function(data) {
						if (!data.error) {
							if (data.data && data.data.length > 0) {
								// var points = {max:1,data:dataFilter(data)};
								var points = dataFilter(data.data, origX,
										origY, scale, imgWidth, imgHeight,
										coordinate, imgInfo[0]);
								var dataObj = {
									max : maxValue,
									min : 1,
									data : points
								};
								heatmap.setData(dataObj);
								$("#legend").show();
							}
							$(".countInfo").show();
							responeData = data;
						}
						$("#number1").text(data.data.length*coefficient);
					});
					clearTimeout(timer);
					timer = setTimeout("refreshHeatmapData();", 4000);
					// refreshHeatmapData();
				}
			}
		});
	};
	var initHeatmap2 = function(floorNo2,periodSel) {
		$("#heatmap-slide2-graphic").css("background-image","");
		$("#heatmap2").empty();
		$.post("/sva/heatmap/api/getMapInfoByPosition", {
			floorNo : floorNo2
		}, function(data) {
			if (!data.error) {
				if (data.bg) {
					// 全局变量赋值
					origX2 = data.xo;
					origY2 = data.yo;
					bgImg2 = data.bg;
					bgImgWidth2 = data.bgWidth;
					bgImgHeight2 = data.bgHeight;
					scale2 = data.scale;
					coordinate2 = data.coordinate;
					// 设置背景图片
					var bgImgStr2 = "url(../upload/" + bgImg2 + ")";
					var imgInfo = calImgSize(bgImgWidth2, bgImgHeight2);
					imgScale2= imgInfo[0];
					imgWidth2 = imgInfo[1];
					imgHeight2 = imgInfo[2];
					$("#heatmap-slide2-graphic").css({
						"width" : imgWidth2 + "px",
						"height" : imgHeight2 + "px",
						"background-image" : bgImgStr2,
						"background-size" : imgWidth2 + "px " + imgHeight2 + "px",
						"margin" : "0% auto"
					});
//					$(".slide-box .slide-box-tip.slide-box-tip-slide3").css({
//						"width" : bgImgWidth2 + "px",
//					    "margin-left": "-28%"
//					});
//					
					heatmap2 = h337.create(configObj2);
					$.post("/sva/heatmap/api/getData5?t="+Math.random(), {
						floorNo : floorNo2,period:periodSel
					}, function(data) {
						if (!data.error) {
							if (data.data && data.data.length > 0) {
								// var points = {max:1,data:dataFilter(data)};
								var points = dataFilter2(data.data, origX2,
										origY2, scale2, imgWidth2, imgHeight2,
										coordinate2, imgInfo[0]);
								var dataObj2 = {
										max : maxValue1,
										min : 1,
										data : points
								};
								heatmap2.setData(dataObj2);
							}
							responeData2 = data;
							$("#numbern-data-item4").text(data.data.length*coefficient);
//							$("#number2").text(data.data.length*coefficient);
						}
					});
					clearTimeout(timer2);
					timer2 = setTimeout("refreshHeatmapData2();", 4000);
					// refreshHeatmapData();
				}
			}
		});
	};

	var initHeatmap3 = function(floorNo3,periodSel) {
		$("#heatmap-slide3-graphic").css("background-image","");
		$("#heatmap3").empty();
		$.post("/sva/heatmap/api/getMapInfoByPosition", {
			floorNo : floorNo3
		}, function(data) {
			if (!data.error) {
				if (data.bg) {
					// 全局变量赋值
					origX3 = data.xo;
					origY3 = data.yo;
					bgImg3 = data.bg;
					bgImgWidth3 = data.bgWidth;
					bgImgHeight3 = data.bgHeight;
					scale3 = data.scale;
					coordinate3 = data.coordinate;
					// 设置背景图片
					var bgImgStr3 = "url(../upload/" + bgImg3 + ")";
					var imgInfo = calImgSize3(bgImgWidth3, bgImgHeight3);
					imgScale3 = imgInfo[0];
					imgWidth3 = imgInfo[1];
					imgHeight3 = imgInfo[2];
					$("#heatmap-slide3-graphic").css({
						"width" : imgWidth3 + "px",
						"height" : imgHeight3 + "px",
						"background-image" : bgImgStr3,
						"background-size" : imgWidth3 + "px " + imgHeight3 + "px",
						"margin" : "0% auto"
					});
					configObj3.onExtremaChange = function(data) {
						updateLegend(data);
					};
					
					heatmap3 = h337.create(configObj3);
					$.post("/sva/heatmap/api/getData5?t="+Math.random(), {
						floorNo : floorNo3,period:periodSel
					}, function(data) {
						if (!data.error) {
							if (data.data && data.data.length > 0) {
								// var points = {max:1,data:dataFilter(data)};
								var points = dataFilter3(data.data, origX3,
										origY3, scale3, imgWidth3, imgHeight3,
										coordinate3, imgInfo[0]);
								var dataObj3 = {
										max : maxValue2,
										min : 1,
										data : points
								};
								heatmap3.setData(dataObj3);
								$("#legend").show();
							}
							$(".countInfo").show();
							responeData3 = data;
//							$("#number3").text(data.data.length*coefficient);
							$("#numbern-data-item7").text(data.data.length*coefficient);
						}
					});
					clearTimeout(timer3);
					timer3 = setTimeout("refreshHeatmapData3();", 4000);
					// refreshHeatmapData();
				}
			}
		});
	};
	
	var updateLegend = function(data) {
	};
	
	
	return {
		// 初始化下拉列表
		initDropdown : function() {
			$.get("/sva/content/api/getData2?t="+Math.random(),function(data){
				if(!data.error){
					maxValue =  data.data[0].densitySel;
					maxValue1 =  data.data[0].densitySel1;
					maxValue2 =  data.data[0].densitySel2;
			
					radiusSel =  data.data[0].radiusSel;
					radiusSel1 =  data.data[0].radiusSel1;
					radiusSel2 =  data.data[0].radiusSel2;
					
					
					configObj.radius = parseInt(radiusSel); 
					configObj2.radius = parseInt(radiusSel1); 
					configObj3.radius = parseInt(radiusSel2); 
					
					floorNo = data.data[0].floorNo;
					floorNo2 = data.data[0].floorNo2;
					floorNo3 = data.data[0].floorNo3;
				
					period = data.data[0].periodSel;
					coefficient = data.data[0].coefficient;
					if (coefficient==0) {
						coefficient = 1;
					}
//					initHeatmap(floorNo, period);
					initHeatmap2(floorNo2, period);
					initHeatmap3(floorNo3, period);

					 
					 count();
					 count2();
					 count3();

				}
			});
			
		
		}

	};

}();