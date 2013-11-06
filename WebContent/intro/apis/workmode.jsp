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
			<jsp:param name="index" value="2" />
		</jsp:include>

		<div class="us-content">
			<div class="cont">
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>开启链路服务</h4>
				</div>

				<h5>请求:</h5>
				<p>
					接口地址:http://ip:port/terminator/start/{linkId}<br> Http方法:PUT<br>
				</p>

				<h5>返回:</h5>
				<p>
					状态码:204（No Content）<br> Http Body:空
				</p>

				<h5>例子:</h5>
				<p>
					Http请求:向接口地址发送PUT请求<br> Http响应:空
				</p>

				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>关闭链路服务</h4>
				</div>

				<h5>请求:</h5>
				<p>
					接口地址:http://ip:port/terminator/stop/{linkId}<br> Http方法:PUT<br>
				</p>

				<h5>返回:</h5>
				<p>
					状态码:204（No Content）<br> Http Body:空
				</p>

				<h5>例子:</h5>
				<p>
					Http请求:向接口地址发送PUT请求<br> Http响应:空
				</p>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>变更某个链路的工作模式</h4>
				</div>

				<h5>请求:</h5>
				<p>
					接口地址:http://ip:port/terminator/link/{workMode}/{linkId}<br>
					Http方法:PUT<br> workMode有4个取值，注意全部大写:TUNNEL,RECORD,REPLAY,STUB
				</p>

				<h5>返回:</h5>
				<p>
					状态码:204（No Content）<br> Http Body:空
				</p>

				<h5>例子:</h5>
				<p>
					Http请求:向接口地址发送PUT请求<br> Http响应:空
				</p>
			</div>
		</div>
		<!--end us-content-->
		<div class="clearfix"></div>
	</div>
	<!--end main-->

</body>
</html>