<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/common.jsp" flush="true" />
</head>

<body>
	<jsp:include page="/include/header.jsp" flush="true">
		<jsp:param name="index" value="1" />
	</jsp:include>

	<div class="main">
		<jsp:include page="/intro/start/startMenu.jsp" flush="true">
			<jsp:param name="index" value="3" />
		</jsp:include>

		<div class="us-content">
			<div class="cont">
				<h4>1. Q：为什么我明明录制了某个请求动作，但回放时根本就没有返回当时录制时对应的响应？</h4>
				<p>
					A： 因为录制时的请求和回放时的请求看上去是一样的，但实际上可能有差别，需要自定义签名类。请参考<a
						href="<%=request.getContextPath()%>/intro/develop/signer.jsp#whyCustomized">为什么要自定义签名类</a>。
				</p>
				
				<h4>2. Q：我怎么才能看到到链路录制和回放了什么数据？怎么调试？</h4>
				<p>
					A： 首页每个链路配置的右边有个日志链接，点开就可以查看到此链路上的所有通信数据了。对于自定义签名器的，需要自己在public String sign(Object request)方法中打印原始请求和纳入签名的内容，在public void logResponse(RequestResponseBundle bundle)打印返回内容。为什么没有设置统一的方法打印通信数据呢？因为有些模块间的交互对数据进行了加密和打包，terminator在不知道解密或者解包方式的情况下打印出来的东西也是不可读的。
				</p>
			</div>
		</div>
		<!--end us-content-->
		<div class="clearfix"></div>
	</div>
	<!--end main-->

</body>
</html>