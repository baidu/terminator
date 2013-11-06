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
			<jsp:param name="index" value="2" />
		</jsp:include>

		<div class="us-content">
			<div class="cont">
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>新建链路配置</h4>
				</div>
				
				<p>
				首先点击首页的“新建链路”进行配置设置。假设开发测试中的模块是A，需要做桩的是它所依赖的模块B。以下以此为例进行详细解释：<br> 
				• 链路名称：链路的名称标识；<br>
				• 链路端口：链路服务监听的端口，服务A本来需要连接服务B的IP和端口，现在是连接到Terminator部署机器所在的ip（当新增完毕后，首页列表将会显示这个链路IP）和这个配置端口，用户可以任意填写一个1024到65535的端口号（服务会进行端口是否被占用的检测）；<br>
				• 远程地址：服务B的IP地址（既然已经有桩服务了，为什么还要配置真实服务？因为当符合预设条件时可以返回预设结果，不符合时可以发到真实的server，对于维护开发很有作用）；<br> 
				• 远程端口：服务B的端口；<br> 
				• 工作模式：选择STUB模式；<br> 
				• 协议类型：服务A和服务B之间通信的协议；<br>
				• 存储类型：存储类型，如果选择CACHE，则在链路服务停掉后预设数据将消失，因为在内存中保存；如果选择FILE，则是持久化保存；<br>
				• 抽取类：可选，不填将使用协议默认的抽取类，点击查看<a href="<%=request.getContextPath()%>/intro/develop/extractor.jsp" target="_blank">现有的抽取类</a>。<br> 填写完后点击确定，链路配置建立成功。
				</p>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>启动链路服务</h4>
				</div>
				
				<p>
				链接配置建立成功后服务并没有启动，需要在首页找到刚才的链路配置，点击操作里面的“启动”，当看到链路状态变为“running”表明链路服务启动正常。
				</p>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>预设数据</h4>
				</div>
				
				<p>
					选择首页操作中的STUB可以进行桩数据预设。<br>
					匹配条件：现在只支持与操作，即如果添加的多个条件都满足才返回预设结果。这里都是键值对的形式，每个请求在发送到Terminator都会被抽取类抽取成键值对的形式，这里是字符串完全匹配。<br>
					预设返回：当满足匹配条件时将会返回这里面填写的内容。<br>
					填写完成后，点击提交便能在桩列表中看到刚添加的桩数据。<br>
					此时将待开发或者待测试模块连向链路IP和端口，并发送请求后，只要满足匹配条件便会返回预设的结果。<br>
				</p>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>抽取类</h4>
				</div>
				
				<p>
					抽取类是通用桩模式中一个重要的概念。先解释一下通用桩的整个过程：<br>
					1. 先在桩数据预设页面设置好匹配条件和预期返回；<br>
					2. 向通用桩发送一个请求，首先会通过抽取类，它将请求分解为很多被称之为RequestElement的对象，这个对象实际就是一个键值对；<br>
					3. 接着分解成的这些请求元素会经过匹配器（现在暂时只实现了与匹配,即所有条件都满足时才认为匹配上了），它负责与预设条件的键值进行匹配；<br>
					4. 如果请求元素与匹配条件匹配上了就返回预期结果，如果没有匹配上就根据用户选用的模式来返回：默认模式将返回默认的返回（适合全新系统开发），后端模式将直接将请求转接到真实的服务（适合系统升级开发）。<br>
					例如：链路协议选择的是HTTP协议，没有填写抽取器（系统使用默认抽取器）。在Stub设置页面可以看到一句这样的提示："METHOD"代表HTTP method，"URI"代表HTTP访问地址，"BODY"代表HTTP内容。这代表抽取器将如何将请求转化为键值对形式。<br>
					点击增加匹配条件，键填写METHOD，值填写GET，再增加一个匹配条件，键填写URI，只填写/hello；在预设返回中填入：world。这时候在浏览器访问http://链路IP:链路端口/hello就能看到world的返回了。
				</p>
			</div>
		</div>
		<!--end us-content-->
		<div class="clearfix"></div>
	</div>
	<!--end main-->

</body>
</html>