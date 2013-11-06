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
			<jsp:param name="index" value="0" />
		</jsp:include>

		<div class="us-content">
			<div class="cont">
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>问题</h4>
				</div>

				<p>
					在开发/测试一个复杂系统的时候我们经常遇到开发/测试中的模块依赖其它服务的情况。比如一个系统有两个模块A和B，A模块依赖于B模块提供的服务：<br> 
					1. B部分功能还未完成开发导致A模块开发被阻塞； <br>
					2. B模块有些数据不好构造，开发时无法自测到所有情况； <br>
					3. 对A模块进行集成测试时，写了一些自动化用例。但由于B模块不可控，B模块的数据经常变动导致返回给A模块的数据也变化了，这时候依赖B模块返回数据的断言将失败；<br>
					4. B模块不是自己团队维护，经常出现不稳定，导致开发环境中整个系统不稳定。<br>
				</p>

				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>解决方案</h4>
				</div>
				
				服务虚拟化指的就是虚拟出不稳定、不可用、未开发完全的服务。通常有两种方法：<br>
				1. 针对协议的通用桩，可以预先设置请求对应的返回值以及匹配条件，这样系统未开发完之前可以使用这个桩来代替真实的服务；<br>
				2. 录制回放方式，在第三方服务可用的时候将链路上的数据录制下来，当不稳定或者不可用时，回放当时录制的数据。<br>
				其中方案1主要针对问题一和二，方案2主要针对问题三和四。Terminator（寓意:明暗交界线）实现了以上两种方式，下图是Terminator的使用场景：<br>
				<img alt="function summary"
					src="<%=request.getContextPath()%>/resources/images/function-summary.jpg">
				<br> Terminator中每个链路可以看成是一个代理，运行在两个服务之间，现在支持四种工作模式：<br> 
				<b>TUNNEL：隧道模式，</b>链路服务负责接收和转发链路上的数据，但不做任何存储，相当于通透状态;<br>
				<b>RECORD：录制模式，</b>链路服务将链路上的请求和响应存储下来，并记录请求响应的对应关系;<br>
				<b>REPLAY：回放模式，</b>链路服务不会连接后端的依赖服务，当请求过来时当符合某些条件时直接返回当时录制的响应;<br>
				<b>STUB：通用桩模式，</b>链路服务能够预设返回结果与匹配规则，当请求过来时符合匹配规则即返回预设结果。<br>
			</div>
		</div>
		<!--end us-content-->
		<div class="clearfix"></div>
	</div>
	<!--end main-->

</body>
</html>