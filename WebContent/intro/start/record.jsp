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
			<jsp:param name="index" value="1" />
		</jsp:include>

		<div class="us-content">
			<div class="cont">
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>新建链路配置</h4>
				</div>
				
				<p>
				首先点击首页的“新建链路”进行配置设置。假设开发测试中的模块是A，它依赖于服务B。以下以此为例进行详细解释：<br> 
				• 链路名称：链路的名称标识；<br>
				• 链路端口：链路服务监听的端口，服务A本来需要连接服务B的IP和端口，现在是连接到Terminator部署机器所在的ip（当新增完毕后，首页列表将会显示这个链路IP）和这个配置端口，用户可以任意填写一个1024到65535的端口号（服务会进行端口是否被占用的检测）；<br>
				• 远程地址：服务B的IP地址；<br> 
				• 远程端口：服务B的端口；<br> 
				• 工作模式：见整体介绍中的工作模式。工作模式后续在链路服务启动后可以随时修改；<br> 
				• 协议类型：服务A和服务B之间通信的协议；<br>
				• 存储类型：存储类型，如果选择CACHE，则在链路服务停掉后录制数据将消失，因为在内存中保存；如果选择FILE，则是持久化保存；<br>
				• 签名类：可选，不填将使用协议默认的签名类，点击查看<a href="<%=request.getContextPath()%>/intro/develop/signer.jsp" target="_blank">现有的签名类</a>。<br> 填写完后点击确定，链路配置建立成功。
				</p>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>工作模式选择</h4>
				</div>
				
				<p>
				链接配置建立成功后服务并没有启动，需要在首页找到刚才的链路配置，点击操作里面的“启动”，当看到链路状态变为“running”表明链路服务启动正常。这时候就可以在工作模式下拉框中选择相应的模式。
				</p>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>签名类</h4>
				</div>
				
				<p>
				签名类是录制回放模式中一个重要的概念。先解释一下录制回放的整个过程：<br>
				1. 当启动录制模式时，Terminator会将A模块发过来的请求直接发送给B模块，得到响应后将A的请求和B的响应存储起来，同时对A请求进行签名计算，最后将B模块的响应原样转回给A模块。签名的过程实际就是抽取A模块发送请求中的特征值进行计算得到一个字符串。<br>
				比如录制协议为HTTP时，当不填写签名类时使用的是默认的签名类（DefaultHttpSigner），它做的就是对Http
				Header中version、uri、method以及整个Http Body计算一个MD5值。要开发自定义的签名类见<a
					href="<%=request.getContextPath()%>/intro/develop/signer.jsp">开发文档</a>。<br>
				2. 当启动回放模式时，Terminator会将A模块发过来的请求用签名类进行签名，当计算的值与录制时计算值一致时就直接返回录制时对应的响应，当不一致时就直接返回一个默认的响应。比如回放协议为HTTP时，当请求没有被录制的时候将返回一个Http Status为501，Http Body为“You see this
				message because the response to this request is not recorded!”的响应。<br>
				每一次改变链路配置中的签名类时，当时录制的签名值将会自动重新计算。
				</p>
			</div>
		</div>
		<!--end us-content-->
		<div class="clearfix"></div>
	</div>
	<!--end main-->

</body>
</html>