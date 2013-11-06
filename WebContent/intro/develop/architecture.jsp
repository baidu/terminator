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
			<jsp:param name="index" value="0" />
		</jsp:include>

		<div class="us-content">
			<div class="cont">
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>整体架构</h4>
				</div>

				<p>
					Terminator的整体架构如下,基本分为四层：<br>
					<img alt="architecture"
						src="<%=request.getContextPath()%>/resources/images/intro/architecture.png" height="300px"><br>
					1. 网络通信：主要在TCP层建立Socket收发链路上的通信数据，这里采用的是netty框架；<br>
					2. 协议编解码器：主要将二进制数据包解析为协议数据或者反过来将协议数据转化为二进制数据，netty本身提供了HTTP、SSL/TLS、WebSockets、Google Protocol Buffer的编解码器，如果需要扩展可以自己定义协议编解码器；<br>
					3. 工作模式处理器：本系统的核心，现在提供的录制回放、通用桩都是这里实现的。这里提供了较多的扩展接口，可以基于定制化需求实现新的模式，比如当后端服务down掉的情况下启动之前的录制数据。另外对于录制回放模式，签名类是一个核心组件，它的作用是如何标识一个请求，对于不同系统可能有不一样的实现；对于通用桩模式，抽取类是一个核心组件，他的作用是如何提取一个请求，涉及到如何设置匹配条件，对于不同系统（特别是协议）也可能有不一样的实现。所以这些都是系统提供的可扩展接口。<br>
					4. APIs:为了使用上的方便（比如持续集成），系统基本所有的功能都通过REST API提供。
				</p>
			</div>
		</div>
		<!--end us-content-->
		<div class="clearfix"></div>
	</div>
	<!--end main-->

</body>
</html>