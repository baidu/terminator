<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
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
				<li class="first current1">链路详情</li>
			</ul>
		</div>
	
		<form>
			<table class="infotbl">
				<tr>
					<td style="width: 120px">链路ID</td>
					<td>
						<%=request.getParameter("linkId")%>
					</td>
				</tr>
				<tr>
					<td>链路名称</td>
					<td>
						<div id="name"></div>
					</td>
				</tr>
				<tr>
					<td>链路IP</td>
					<td>
						<div id="localIp"></div>
					</td>
				</tr>
				<tr>
					<td>链路监听端口</td>
					<td>
						<div id="localPort"></div>
					</td>
				</tr>
				<tr>
					<td>目标服务地址</td>
					<td>
						<div id="remoteAddress"></div>
					</td>
				</tr>
				<tr>
					<td>目标服务端口</td>
					<td>
						<div id="remotePort"></div>
					</td>
				</tr>
				<tr>
					<td>工作模式</td>
					<td>
						<div id="workMode"></div>
					</td>
				</tr>
				<tr>
					<td>协议类型</td>
					<td>
						<div id="protocolType"></div>
					</td>
				</tr>
				<tr>
					<td>存储类型</td>
					<td>
						<div id="storageType"></div>
					</td>
				</tr>
				<tr>
					<td>签名类</td>
					<td>
						<div id="signClass">无</div>
					</td>
				</tr>
				<tr>
					<td>抽取类</td>
					<td>
						<div id="extractClass">无</div>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<script type="text/javascript">
		$.ajaxSetup({cache:false});
		baseUrl = '<%=request.getContextPath()%>'
		linkId = '<%=request.getParameter("linkId")%>'
		
		$(document).ready(function() {
			queryLink();
		});
		
		function queryLink(){
			$.getJSON(baseUrl + '/link/' + linkId, function(data) {
				$("#name").html(data['name']);
				$("#localIp").html(data['localIp']);
				$("#localPort").html(data['localPort']);
				$("#remoteAddress").html(data['remoteAddress']);
				$("#remotePort").html(data['remotePort']);
				$("#signClass").html(data['signClass']);
				$("#workMode").html(data['workMode']);
				$("#protocolType").html(data['protocolType']);
				$("#storageType").html(data['storageType']);
				$("#extractClass").html(data['extractClass']);
			})
		}
	</script>

</body>
</html>