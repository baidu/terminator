<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/common.jsp" flush="true" />
</head>

<body>
	<jsp:include page="/include/header.jsp" flush="true">
		<jsp:param name="index" value="3" />
	</jsp:include>

	<div class="main">
		<jsp:include page="/intro/develop/devMenu.jsp" flush="true">
			<jsp:param name="index" value="4" />
		</jsp:include>

		<div class="us-content">
			<div class="cont">
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>日志打印</h4>
				</div>

				<p>
					为了使调试和使用更加方便，Terminator的日志是分链路的，即日志中只显示当条链路相关的信息。因此在开发协议、签名类时，如果需要将重要信息打印出来，获得日志类请使用LinkLogger.getLogger(ClassName.class,link.getId())，它所返回的对象对将日志打印到对应的日志文件中。
				</p>
			</div>
		</div>
		<!--end us-content-->
		<div class="clearfix"></div>
	</div>
	<!--end main-->

</body>
</html>