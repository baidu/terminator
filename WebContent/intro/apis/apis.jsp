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
			<jsp:param name="index" value="0" />
		</jsp:include>
		
		<div class="us-content">
			<div class="cont">
				<p>
					Terminator以REST方式提供远程调用API，Http请求和放回都是以JSON方式传输。<br>
					REST相关知识可以参考：<a
						href="http://en.wikipedia.org/wiki/Representational_state_transfer">wikipedia</a><br>
					调试REST接口可以使用Firefox的扩展<a
						href="http://code.google.com/p/poster-extension">Poster</a>，这个工具可以方便发送GET/POST/PUT/DELETE类型的HTTP请求。<br>
					开发Java REST的客户端可以使用Spring中spring-web-3.X.X.jar中的RestTemplate类。
				</p>

				<h4>API主要涉及以下几个方面：</h4>

				<h5>链路相关</h5>
				<p>主要通过接口增删查改链路配置。</p>

				<h5>工作模式相关</h5>
				<p>主要通过接口启动/暂停Terminator，已经进行工作模式的切换。</p>

				<h5>通用桩</h5>
				<p>主要通过接口准备数据、设置匹配器以及查询桩的结果。</p>
			</div>
		</div>
		<!--end us-content-->
		<div class="clearfix"></div>
	</div>
	<!--end main-->

</body>
</html>