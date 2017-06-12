var swiper;
var timerFunctionTimer;
var BarJsonData;
function timerFunction()
{	
	clearTimeout(timerFunctionTimer);
	
	$.post("/sva/api/getBaShow", function(item){
		if(!item.error){
			//隐藏热力图
			if(item.floorflag==0)
				$("#huawei").hide();
			if(item.floorflag2==0)
				$("#vdf").hide();
			
			if(item.floorflag3==0)
				$("#other").hide();
			BarJsonData = item.item;
			if(item.allUser == null){
				
				$("#numbern-data-item2").text(0);
			}else{
				$("#numbern-data-item2").text(item.allUser);
			}
			if(item.allTime == null){
				$("#numbern-data-item3").text(0);
			}else{
				
				$("#numbern-data-item3").text(item.allTime);
			}
			if(item.allUser2 == null ){
				$("#numbern-data-item2-slide2").text(0);
			}
			else{
				$("#numbern-data-item2-slide2").text(item.allUser2);
			}
			if(item.allTime2 ==null){
				
				$("#numbern-data-item3-slide2").text(0);
			}else{
				
				$("#numbern-data-item3-slide2").text(item.allTime2);
			}
			if(item.allUser3 == null){
				
				$("#numbern-data-item2-slide3").text(0);
			}else{
				$("#numbern-data-item2-slide3").text(item.allUser3);
			}
			if(item.allTime3 == null){
				$("#numbern-data-item3-slide3").text(0);
			}else{
				$("#numbern-data-item3-slide3").text(item.allTime3);
			}
			
			var arr = new Array();
			arr[0] = "#028be9";
			arr[1] = "#01aaed";
			arr[2] = "#02c8eb";
			arr[3] = "#196ecd";
			arr[4] = "#06dfd2";
			currentArr = _.pluck(BarJsonData, 'current');
			currentMax = _.max(currentArr,function(d){return parseInt(d);});
			var intcurrentMax = parseInt(currentMax)*1.5;
			
			maxcumulativeArr = _.pluck(BarJsonData, 'cumulative');
			cumulativeMax = _.max(maxcumulativeArr,function(d){return parseInt(d);});
			var maxcumulativeMax = parseInt(cumulativeMax)*1.5;
			if(BarJsonData){
				$("#bar-slide1-list").html("");
				$.each(BarJsonData, function(i, item) {
					var _current = 0;
					if(intcurrentMax > 0){
						_current = item.current/intcurrentMax * 100;
					}
					var _cumulative = 0;
					if(maxcumulativeMax > 0){
						_cumulative = item.cumulative/maxcumulativeMax * 100;
					}
		            $("#bar-slide1-list").append(
					       '<div class="bar-slide1-listItem"><ul><li>' + item.name + '</li>' + 
						   '<li><span class="slide1-bar"style="background-color:#d38a23;width:' + _current + '%"><span>'+ item.current +'</span></span></li>'+
		                 '<li><span class="slide1-bar"style="background-color:#e9643b;width:' + _cumulative + '%"><span>'+ item.cumulative +'</span></span></li>'+
						  '<li>'+item.average +'</li></ul></div>'
						  );
		        });
			}
		}
		timerFunctionTimer = setTimeout("timerFunction();", 60000);
	});
	
}
$(function(){
	
	swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        paginationClickable: true,
		keyboardControl: true,
        autoHeight: true//enable auto height
    });
	timerFunction();
});