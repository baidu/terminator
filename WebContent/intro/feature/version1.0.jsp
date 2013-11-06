<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/common.jsp" flush="true" />
</head>

<body>
	<jsp:include page="/include/header.jsp" flush="true">
		<jsp:param name="index" value="4" />
	</jsp:include>

	<div class="main">
		<jsp:include page="/intro/feature/featureMenu.jsp" flush="true">
			<jsp:param name="index" value="1" />
		</jsp:include>

		<div class="us-content">
			<div class="cont">
				<h5>Terminator 1.1的主要功能见下图：</h5>
				<img alt="terminator1.1"
					src="<%=request.getContextPath()%>/resources/images/terminator1.1.bmp"
					width="760px">
			</div>

			<div class="cont">
				<h5>Terminator 1.0的主要功能见下图：</h5>
				<img alt="terminator1.0"
					src="<%=request.getContextPath()%>/resources/images/terminator1.0.bmp"
					width="760px">
			</div>
		</div>
		<!--end us-content-->
		<div class="clearfix"></div>
	</div>
	<!--end main-->

</body>
</html>