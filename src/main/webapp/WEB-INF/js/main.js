$("#xiazai1").live("click",function(e){
	var path = "../upload/QRcode2.jpg";
	$("#myModal").modal("show");
	$("#pictureid").attr("src",path);
	var param = {"type":"adr"};
	$.post("/sva/paramconfig/api/saveApkCount",param,function(data){
		
	});
 });
$("#xiazai2").live("click",function(e){
	$("#myModal").modal("show");
	var path = "../upload/QRcode3.jpg";
	$("#pictureid").attr("src",path);
	var param = {"type":"adr"};
	$.post("/sva/paramconfig/api/saveApkCount",param,function(data){
		
	});
});
$("#xiazai3").live("click",function(e){
	$("#myModal").modal("show");
	var path = "../upload/QRcode4.jpg";
	$("#pictureid").attr("src",path);
	var param = {"type":"adr"};
	$.post("/sva/paramconfig/api/saveApkCount",param,function(data){
		
	});
});
$("#web2").live("click",function(e){
	var param = {"type":"web"};
	$.post("/sva/paramconfig/api/saveApkCount",param,function(data){
		
	});
});
$("#web3").live("click",function(e){
	var param = {"type":"web"};
	$.post("/sva/paramconfig/api/saveApkCount",param,function(data){
		
	});
});
$("#IOSxiazai1").live("click",function(e){
	var path = "../upload/IOSQRcode2.jpg";
	$("#myModal").modal("show");
	$("#pictureid").attr("src",path);
});
$("#IOSxiazai2").live("click",function(e){
	$("#myModal").modal("show");
	var path = "../upload/IOSQRcode2.png";
	$("#pictureid").attr("src",path);
	var param = {"type":"ios"};
	$.post("/sva/paramconfig/api/saveApkCount",param,function(data){
		
	});
});
$("#IOSxiazai3").live("click",function(e){
	$("#myModal").modal("show");
	var path = "../upload/IOSQRcode4.png";
	$("#pictureid").attr("src",path);
});

$("#imgClose").hover(
	    function(){
		    $(this).attr("src","../images/close1.png");
	    },
	function(){
		    $(this).attr("src","../images/close.png");
	        }
    );
function closeModel()
{
	$("#myModal").modal('hide');
	//$("#movie").empty();
}