<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/common.jsp" flush="true" />
</head>

<script type="text/html" id="links_template">
	<table class="listtbl1">
	<thead>
	<tr>
		<th>ID</th>
		<th>名称</th>
		<th>链路IP</th>
		<th>链路端口</th>
		<th>目标服务地址</th>
		<th>目标服务端口</th>
		<th>状态</th>
		<th>工作模式</th>
		<th>操作</th>
	</tr>
	</thead>
	{{#links}}
	<tr>
		<td>{{id}}</td>
		<td><a href="<%=request.getContextPath()%>/link/linkDetail.jsp?linkId={{id}}">{{name}}</a></td>
		<td>{{localIp}}</td>
		<td>{{localPort}}</td>
		<td>{{remoteAddress}}</td>
		<td>{{remotePort}}</td>
		<td name="status" value="{{id}}">
			<img style="padding-top: 8px" src="<%=request.getContextPath()%>/resources/images/{{status}}.png" title="{{status}}"/>
			<input id="status{{id}}" type="hidden" value="{{status}}"/>
		</td>
		<td>
			 <select id="workMode{{id}}" name="workMode" selectValue="{{workMode}}" onchange="changeWorkMode({{id}})">
                   	<option value="TUNNEL">TUNNEL</option>
				   	<option value="RECORD">RECORD</option>
					<option value="REPLAY">REPLAY</option>
					<option value="STUB">STUB</option>
             </select>
		</td>
		<td>
			<a id="switcher{{id}}" onclick="stopServer({{id}})"></a>
			<a onclick="editLink({{id}})">编辑</a>
			<a onclick="deleteLink({{id}})">删除</a>
			<a target="_blank" href="<%=request.getContextPath()%>/log/log.jsp?linkId={{id}}">日志</a>
			<a target="_blank" href="<%=request.getContextPath()%>/stub/stub.jsp?linkId={{id}}">Stub</a>
		</td>
	</tr>
	{{/links}}
	</table>
</script>


<body>

<jsp:include page="/include/header.jsp" flush="true">
	<jsp:param name="index" value="0" />
</jsp:include>

<div class="mainIndex">

	<div style="padding-bottom: 40px">
		<div style="float: left">
			<input type="button" onclick="window.location.href = baseUrl + '/link/link.jsp?function=add';" class="btn" value="新增链路">
		</div>
		
		<div style="float: right;">
			<input id="searchBox" type="text" style="height: 20px;float: left;" onkeydown="doKeyDown()">
			<input id="searchButton" type="button" class="searchButton" onclick="listLinks()" >
		</div>
	</div>

	<div id="linkDiv" style="clear:both;"></div>
</div>

<script type="text/javascript">
		$.ajaxSetup({cache:false});
		baseUrl = '<%=request.getContextPath()%>';

		$(document).ready(function() {
			$.Mustache.addFromDom('links_template');
			listLinks();
			
			document.onkeydown = function(e) {
			    e = e ? e : window.event;
			    var keyCode = e.which ? e.which : e.keyCode;
			    if (keyCode==13){
					listLinks();
				}
			}
		});
		
		function toStub(linkId,protocolType){
			$.ajax({
				type : "GET",
				url : baseUrl + "/link/status/" + linkId,
				contentType : "application/json",
				statusCode : {
					405 : function(data){
						alert("链路服务没有启动，请先启动服务!");
					}
				},
				success : function(data, textStatus, jqXHR) {
					if(protocolType=="HTTP"){
						window.location.href = baseUrl + '/stub/stubSet.jsp?Stubid='+linkId+'&protocolType=HTTP';
					}else{
						window.location.href = baseUrl + '/stub/stubSet.jsp?Stubid='+linkId+'&protocolType=MCPACK';					
						}
						
				}
			});
		}
		
		function listLinks() {
			var name = $('#searchBox').val();
			if ($.trim(name) == ""){
				$.getJSON(baseUrl + '/link', function(data) {
					callback(data);
				});
			}
			else{
				$.ajax({
					type : "POST",
					url : baseUrl + "/link/name",
					contentType : "application/json",
					data : name,
					success : function(data, textStatus, jqXHR) {
						callback(data);
					}
				});
			}
			
			function callback(data){
				$('#linkDiv').empty().mustache('links_template', {
					links : data
				});
				
				$("select[name='workMode']").each(function() {
					var value = $(this).attr("selectValue");
					$(this).val(value);
				});
				
				$("td[name='status']").each(function() {
					id = $(this).attr("value");
					status = $('#status' + id).val();
					if ("stopped" == status) {
						$('#switcher' + id).html("启动");
						$('#switcher' + id).attr("onclick",
								'startServer(' + id + ')');
						$('#workMode' + id).attr("disabled",true);
					} else {
						$('#switcher' + id).html("停止");
						$('#switcher' + id).attr("onclick",
								'stopServer(' + id + ')');
						$('#workMode' + id).attr("disabled",false);
					}
				});
			}
		}

		function stopServer(linkId) {
			var operationType = arguments[1] ? arguments[1] : 'stop';
			$.ajax({
				type : "PUT",
				url : baseUrl + "/control/stop/" + linkId,
				contentType : "application/json",
				statusCode : {
					500 : function() {
						alert("服务器繁忙，请稍后再试");
					}
				},
				success : function(data, textStatus, jqXHR) {
					if(operationType == 'delete'){
						$.ajax({
							type : "DELETE",
							url : baseUrl + "/link/" + linkId,
							contentType : "application/json",
							statusCode : {
								500 : function() {
									alert("服务器繁忙，请稍后再试");
								}
							},
							success : function(data, textStatus, jqXHR) {
								listLinks();
							}
						});
					}
					else if(operationType == 'edit'){
						window.location.href = baseUrl + '/link/link.jsp?function=update&linkId=' + linkId;
					}
					else{
						listLinks();
					}
				}
			});
		}

		function startServer(linkId) {
			$.ajax({
				type : "PUT",
				url : baseUrl + "/control/start/" + linkId,
				contentType : "application/json",
				success : function(data, textStatus, jqXHR) {
					listLinks();
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
		            alert(XMLHttpRequest.responseText);
		        }
			});
		}
		
		function changeWorkMode(linkId){
			var workMode = $('#workMode'+ linkId).val();
			$.ajax({
				type : "PUT",
				url : baseUrl + "/control/" + workMode + "/" + linkId,
				contentType : "application/json",
				statusCode : {
					500 : function() {
						alert("服务器繁忙，请稍后再试");
					},
					405 : function(data){
						alert("链路服务没有启动，请先启动服务!");
					}
				},
				success : function(data, textStatus, jqXHR) {
					alert("链路"+ linkId+ "已经转向工作模式:" + workMode);
				}
			});
		}

		function deleteLink(linkId) {
			var comfirm = confirm('您确定要删除该链路吗？');
			if (comfirm) {
				stopServer(linkId,'delete');
			}
		}
		
		function editLink(linkId){
			var status = $('#status' + linkId).val();
			if("running" == status){
				var comfirm = confirm('编辑链路配置会先停止链路服务,你确认要编辑吗？');
				if(comfirm){
					stopServer(linkId,'edit')
				}
			}
			else{
				stopServer(linkId,'edit')
			}
		}
		
		function deleteRecord(linkId) {
			var comfirm = confirm('您确定要删除之前的录制数据吗？');
			if (comfirm) {
				$.ajax({
					type : "DELETE",
					url : baseUrl + "/record/" + linkId + "/1",
					contentType : "application/json",
					statusCode : {
						500 : function() {
							alert("服务器繁忙，请稍后再试");
						},
						405 : function(data){
							alert("链路服务没有启动，请先启动服务!");
						}
					},
					success : function(data, textStatus, jqXHR) {
						alert("链路"+ linkId+ "的历史录制数据已删除！");
					}
				});
			}
		}
	</script>

</body>
</html>