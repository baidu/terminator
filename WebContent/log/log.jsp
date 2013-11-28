<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/jquery-1.7.2.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/jquery-mustache.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/jquery.tmpl.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/json2.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/mustache.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/jquery.poshytip.js"></script>

<style type="text/css">

body {
    background: none repeat scroll 0 0 white;
    margin: 0;
    padding: 0;
}
body, table, form, input, td, th, p, textarea, select {
    font-family: Verdana,Helvetica,sans serif;
    font-size: 11px;
}
pre {
    margin: 0;
    white-space: pre-wrap;
    word-wrap: break-word;
}

</style>
</head>

<script type="text/html" id="log_template">
	{{logLine}}<br>
</script>

<script type="text/javascript">
	$.ajaxSetup({cache:false});
	baseUrl = '<%=request.getContextPath()%>';
	linkId = '<%=request.getParameter("linkId")%>';
	
	$(document).ready(function() {
		$.Mustache.addFromDom('log_template');
		queryLog(linkId, 0);
	});

	function queryLog(linkId, offset) {
		$('#spinner').slideDown();
		$.ajax({
			type : "GET",
			url : baseUrl + "/log/" + linkId + "/" + offset,
			contentType : "application/json",
			success : function(data, textStatus, jqXHR) {
				$('#logLocation').html(data.logLocation)
				for(var i in data.content){
					$('#log').mustache('log_template', {
						logLine : data.content[i]
					});
				};
				$('#spinner').slideUp();
				setTimeout(
		            function() {
		            	queryLog(linkId, data.offset);
		            },
		            3000
			    );
			},
			error: function (XMLHttpRequest, textStatus, errorThrown) {
	            alert(XMLHttpRequest.responseText);
	        }
		});
	}
	
	function clearLog(){
		var comfirm = confirm('Confirm to clear log related to this link?');
		if(comfirm){
			$.ajax({
				type : "DELETE",
				url : baseUrl + "/log/" + linkId,
				contentType : "application/json",
				success : function(data, textStatus, jqXHR) {
					$('#log').empty();
					alert("clear log success!");
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
		            alert(XMLHttpRequest.responseText);
		        }
			});
		}
	}
</script>

<body>
	<h1 style="width: 80%;margin: 0 auto; ">
		<img width="48" height="48" src="<%=request.getContextPath()%>/resources/images/green.png">
		后台链路通信数据日志,存放位置:<span id="logLocation"></span>
		<input type="button" value="清空日志" onclick="clearLog()">
	</h1>
	<div style="width: 80%;margin: 0 auto; ">
		<pre id="log"></pre>
	</div>
	<div id="spinner" style="width: 80%;margin: 0 auto;">
		<img width="16px" height="16px" src="<%=request.getContextPath()%>/resources/images/spinner.gif">
	</div>
</body>
</html>