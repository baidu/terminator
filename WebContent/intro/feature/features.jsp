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
			<jsp:param name="index" value="0" />
		</jsp:include>

		<div class="us-content">
			<div class="cont">
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>
						Terminator是一个服务虚拟化平台，以两种形式提供虚拟化服务：通用桩和录制回放。
					</h4>
				</div>
				

				<h5>工作模式</h5>
				<p>支持TUNNEL、RECORD、REPLAY、STUB四种工作模式以及互相的动态切换</p>

				<h5>协议支持</h5>
				<p>现阶段支持HTTP、Mcpack+JSON-RPC over HTTP（百度内部协议）、Nshead+Mcpack over TCP（百度内部协议）<br>
				        另外由于netty自带SSL/TLS、WebSockets、Google Protocol Buffer的支持，所以扩充这几个协议也很简单
				</p>

				<h5>存储方式</h5>
				<p>支持CACHE、FILE</p>

			</div>
		</div>
		<!--end us-content-->
		<div class="clearfix"></div>
	</div>
	<!--end main-->

</body>
</html>