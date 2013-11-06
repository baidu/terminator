<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/common.jsp" flush="true" />
</head>

<body>
	<jsp:include page="/include/header.jsp" flush="true">
		<jsp:param name="index" value="2" />
	</jsp:include>

	<div class="main">
		<jsp:include page="/intro/apis/apiMenu.jsp" flush="true">
			<jsp:param name="index" value="3" />
		</jsp:include>

		<div class="us-content">
			<div class="cont">
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>设置链路信息</h4>
				</div>
			
				<h5>请求：</h5>
				接口地址：<br> http://ip:port/terminator/stub/<br> http方法：POST<br>
				<table>
					<tr>
						<td>参数列表</td>
						<td>描述</td>
					</tr>
					<tr>
						<td>linkid(必填)</td>
						<td>链路id</td>
					</tr>
					<tr>
						<td>matcher(必填)</td>
						<td>匹配条件</td>
					</tr>
					<tr>
						<td>response(必填)</td>
						<td>预期返回</td>
					</tr>
				</table>
				<br>
				
				<h5>返回：</h5>
				<p>
					状态码：405（METHOD_NOT_ALLOWED） 表示因为链路没有没启动而不能预设数据<br> httpbody：空<br>
				<p>
				
				<h5>例子：</h5>
				<p>
					HTTP请求：<br>
					{"matcher":"{\"query\":[\"跑步\"],\"is_business_tag\":1}","response":"{\"status\":0,\"query_trade_items\":[{\"trade_result\":[],\"business_res\":[\"休闲娱乐;体育器械;跑步类;跑步;跑步;;名词\"],\"query\":\"跑步\"}]}","stubid":"24"}<br>
					HTTP返回：空<br>
				</p>
			</div>
		</div>
		<!--end us-content-->
		<div class="clearfix"></div>
	</div>
	<!--end main-->

</body>
</html>