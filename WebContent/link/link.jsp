<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String function = request.getParameter("function");
%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/common.jsp" flush="true" />
</head>

<body>

	<jsp:include page="/include/header.jsp" flush="true">
		<jsp:param name="index" value="0" />
	</jsp:include>

	<div class="main">
		<div class="subnav">
			<ul>
				<% if (function.equals("update")) { %>
				<li class="first current1">编辑链路</li>
				<% } else { %>
				<li class="first current1">新增链路</li>
				<% } %>
			</ul>
		</div>
	
		<form>
			<table class="infotbl">
				<% if (function.equals("update")) { %>
				<tr>
					<td>链路ID</td>
					<td>
						<%=request.getParameter("linkId")%>
					</td>
				</tr>
				<% } %>
				
				<tr>
					<td>链路名称<span style="color: red; margin-left: 10px">*</span></td>
					<td>
						<input id="name" type="text" class="textcss1" title="链路名称"/>
						<span id="namePrompt" style="display:none;color:red"></span>
					</td>
				</tr>
				<tr>
					<td>链路监听端口<span style="color: red; margin-left: 10px">*</span></td>
					<td>
						<input id="localPort" onblur="checkPortAvailable()" type="text" class="textcss1" title="client连接terminator服务的端口"/>
						<span id="localPortPrompt" style="display:none;color:red"></span>
					</td>
				</tr>
				<tr>
					<td>目标服务地址<span style="color: red; margin-left: 10px">*</span></td>
					<td>
						<input id="remoteAddress" type="text" class="textcss1" title="terminator连接目标服务的ip地址"/>
						<span id="remoteAddressPrompt" style="display:none;color:red"></span>
					</td>
				</tr>
				<tr>
					<td>目标服务端口<span style="color: red; margin-left: 10px">*</span></td>
					<td>
						<input id="remotePort" type="text" class="textcss1" title="terminator连接目标服务的端口"/>
						<span id="remotePortPrompt" style="display:none;color:red"></span>
					</td>
				</tr>
				<tr>
					<td>工作模式<span style="color: red; margin-left: 10px">*</span></td>
					<td>
						<select id="workMode" class="selectcss3">
								<option value="TUNNEL">TUNNEL</option>
								<option value="RECORD">RECORD</option>
								<option value="REPLAY">REPLAY</option>
								<option value="STUB">STUB</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>协议类型<span style="color: red; margin-left: 10px">*</span></td>
					<td>
						<select id="protocolType" class="selectcss3">
						</select>
					</td>
				</tr>
				<tr>
					<td>存储类型<span style="color: red; margin-left: 10px">*</span></td>
					<td>
						<select id="storageType" class="selectcss3">
								<option value="FILE">FILE</option>
								<option value="CACHE">CACHE</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>签名类</td>
					<td>
						<input id="signClass" type="text" style="width: 520px" title="自定义签名类，根据签名值来取当时录制时对应的响应，不填采用默认匹配类，见现有签名类"/>
						<a href="<%=request.getContextPath()%>/intro/develop/signer.jsp" target="_blank">现有签名类</a>
					</td>
				</tr>
				<tr>
					<td>抽取类</td>
					<td>
						<input id="extractClass" type="text" style="width: 520px" title="自定义抽取类，将请求转化为可供匹配的元素，不填采用默认抽取类，见现有抽取类"/>
						<a href="<%=request.getContextPath()%>/intro/develop/extractor.jsp" target="_blank">现有抽取类</a>
					</td>
				</tr>
				<tr>
					<td><input id="submitLink" type="button" value="提交"
						class="btn" onclick="saveOrUpdateLink()"/>
					</td>
				</tr>
			</table>
		</form>
	</div>
	
	<script type="text/javascript">
		$.ajaxSetup({cache:false});
		baseUrl = '<%=request.getContextPath()%>'
		
		$(document).ready(function() {
			setTips();
			listProtocols();
		});
		
		function setTips(){
			$('#name').poshytip();
			$('#localPort').poshytip();
			$('#remoteAddress').poshytip();
			$('#remotePort').poshytip();
			$('#signClass').poshytip();
			$('#extractClass').poshytip();
		}
		
		function listProtocols() {
			$.getJSON(baseUrl + '/link/protocols', function(data) {
				$('#protocolType').empty();
				for (var i=0 ; i<data.length; i++){
					$('#protocolType').append("<option value='"+ data[i] + "'>" + data[i] +  "</option>"); 
				}
				
				<% if (function.equals("update")) { %>
				queryLink(<%=request.getParameter("linkId")%>);
				<% } %>
			});
		}
		
		function queryLink(linkId){
			$.getJSON(baseUrl + '/link/' + linkId, function(data) {
				$("#name").val(data['name']);
				$("#localPort").val(data['localPort']);
				$("#remoteAddress").val(data['remoteAddress']);
				$("#remotePort").val(data['remotePort']);
				$("#signClass").val(data['signClass']);
				$("#extractClass").val(data['extractClass']);
				
				$("#workMode").val(data['workMode']);
				$("#protocolType").val(data['protocolType']);
				$("#storageType").val(data['storageType']);
			})
		}
		
		function checkPortAvailable(){
			var port = $('#localPort').val();
			$.getJSON(baseUrl + '/link/portChecker/' + port, function(data) {
				if(false == data){
					$("#localPortPrompt").html('端口已被其他程序占用!');
					$("#localPortPrompt").show();
				}
				else{
					$("#localPortPrompt").hide();
				}
			});
		}
		
		function saveOrUpdateLink() {
			var portReg = /(^[0-9_]+$$)/;
			var ipReg = /((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))/;
			
			var name = $('#name').val();
			if(name == ""){
				$("#namePrompt").html('请填写链路名称!');
				$("#namePrompt").show();
				return false;
			}
			$("#namePrompt").hide();
			
			var localPort = $('#localPort').val();
			if(localPort == ""){
				$("#localPortPrompt").html('请填写本地监听端口!');
				$("#localPortPrompt").show();
				return false;
			}
			else{
				if (!portReg.test(localPort)) {
					$("#localPortPrompt").html("端口只能是数字!");
					$("#localPortPrompt").show();
					return false;
				}
			}
			$("#localPortPrompt").hide();
			
			var remoteAddress = $('#remoteAddress').val();
			if(remoteAddress == ""){
				$("#remoteAddressPrompt").html('请填写远程地址!');
				$("#remoteAddressPrompt").show();
				return false;
			}
			else{
				if(!ipReg.test(remoteAddress)){
					$("#remoteAddressPrompt").html("远程地址只能是IP!");
					$("#remoteAddressPrompt").show();
					return false;
				}
			}
			$("#remoteAddressPrompt").hide();
			
			var remotePort = $('#remotePort').val();
			if(remotePort == ""){
				$("#remotePortPrompt").html('请填写远程端口!');
				$("#remotePortPrompt").show();
				return false;
			}
			else{
				if (!portReg.test(remotePort)) {
					$("#remotePortPrompt").html("端口只能是数字!");
					$("#remotePortPrompt").show();
					return false;
				}
			}
			$("#remotePortPrompt").hide();
			
			var method = "POST";
			var uri = baseUrl + "/link/";
			<% if (function.equals("update")) { %>
				method = "PUT";
				uri = uri + '<%=request.getParameter("linkId")%>'
			<% } %>
			$.ajax({
				type : method,
				url : uri,
				contentType : "application/json",
				data : JSON.stringify({
					"name" : $('#name').val(),
					"localPort" : $('#localPort').val(),
					"remoteAddress" : $('#remoteAddress').val(),
					"remotePort" : $('#remotePort').val(),
					"workMode" : $('#workMode').val(),
					"protocolType" : $('#protocolType').val(),
					"storageType" : $('#storageType').val(),
					"signClass" : $('#signClass').val(),
					"extractClass" : $('#extractClass').val(),
				}),
				statusCode : {
					500 : function() {
						alert("服务器繁忙，请稍后再试！");
					}
				},
				success : function(data, textStatus, jqXHR) {
					window.location.href = baseUrl + '/index.jsp';
				}
			});
		}
	</script>

</body>
</html>