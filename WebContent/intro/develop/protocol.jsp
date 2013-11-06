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
			<jsp:param name="index" value="1" />
		</jsp:include>

		<div class="us-content">
			<div class="cont">
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>简介</h4>
				</div>

				<p>
					Terminator的各种工作方式都是基于通信协议的，因此Terminator从设计上将协议扩充作为重要的组成部分。协议的开发主要分为四个部分：<br>
					1. 编解码器开发：主要将数据流转化为协议对象或者将协议对象转化为传输数据流；<br>
					2. 工作模式处理器：主要实现在不同工作模式（Record、Replay、Stub）下协议的具体工作方式；<br>
					3. 默认签名器：针对此协议的默认请求签名器,主要针对录制回放模式使用；<br>
					4. 默认抽取器：针对此协议的默认请求抽取器,主要针对通用桩模式使用。
				</p>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>编解码器开发</h4>
				</div>
				
				<p>
					Terminator网络通信采用netty框架，因而也复用了它的<a target="_blank" href="http://netty.io/3.6/guide/#architecture.8.3">编码-解码框架</a>,新增一种协议是非常方便的。<br>
					参见com.baidu.terminator.server包中提供的各种协议的实现细节。一个基本的实现过程如下：<br>
					1. 定义与协议相关的对象，方便后续进行业务操作（对于Terminator而言主要是编写签名类）。比如针对HTTP协议可以定义HttpRequest、HttpResponse对象；<br>
					2. 定义编码-解码器，即如何将请求的数据流组装成上面定义的请求对象或者如何将上面定义的响应对象转化为数据流。编码-解码器将会配置在下面介绍的“协议Factory”中。<br>
					现阶段netty自带SSL/TLS、HTTP、WebSockets、Google Protocol Buffer的支持。
				</p>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>工作模式处理器</h4>
				</div>
				
				<p>
					工作模式处理器主要实现在录制（Record）、回放（Replay）、桩（Stub）模式下的行为细节，首先需要建立一个协议Factory，它负责制造出不同的处理器，这个Factory形式如下：<br>
					<img src="<%=request.getContextPath()%>/resources/images/intro/factoryCode.jpg"/><br>
					类上需要有@PipelineFactory(protocol = "ProtocolName")，ProtocolName是协议名称，Terminator在启动时会扫描这个注解以及@DefaultRequestSigner（见下面默认签名类介绍）和@DefaultRequestExtractor（见下面默认抽取器介绍），当某个协议同时存在这三个标注时才能正常工作，新增（修改）链路设置时才会出现在协议类型列表里。
					另外构造函数需要能够传入ServerContext对象。可以看到getPipeline()中不同工作模式指定了不同的处理器（Handler），下面着重介绍他们的实现：<br>
					1. 录制处理器，推荐继承com.baidu.terminator.server.common.record.BaseRecordServerHandler，这个类默认会将链路请求和返回存储下来，用户只需实现两个方法：<br>
					• messageReceived(ChannelHandlerContext ctx, MessageEvent e)，即如何接收数据；<br>
					• getRelayChannelPipelineFactory(RelayListener relayListener)，即如何创建中继服务器将接收到的数据发送到后端真实服务；<br>
					2. 回放处理器，推荐继承com.baidu.terminator.server.common.replay.BaseReplayServerHandler，这个类默认将链路请求进行签名，如果存储中存在就返回之前录制的数据，不存在就返回默认返回。用户只需实现一个方法：<br>
					• responseWhenNotHit()，当请求没有被录制时回调这个方法，这个方法可以给出一个默认返回；<br>
					3. 桩处理器，推荐继承com.baidu.terminator.server.common.stub.BaseStubServerHandler,这个类将请求交给抽取器，抽取后的结果与匹配条件进行匹配。用户需要实现三个方法：<br>
					• responseWhenHit(String content)，当请求与匹配条件匹配上，这个方法将用户在的预期返回组装成协议对象；<br>
					• responseWhenNotHit() ，当用户使用的是无真实后端的模式，当请求与匹配条件没有匹配上，这个方法可以给出一个默认返回；<br>
					• getRelayChannelPipelineFactory(RelayListener relayListener)，当用户使用的是有真实后端的模式，当请求与匹配条件没有匹配上，创建中继服务器将接收到的数据发送到后端真实服务。<br>
					所有处理器的实现也可以不继承以上类，完全采用自定义开发，从netty的SimpleChannelUpstreamHandler继承开始编写。Terminator的获取工作模式处理器的入口是协议Factory。
				</p>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>默认签名类</h4>
				</div>
				<p>
					一种协议只能有一个默认签名类，但能有多个自定义签名类，当用户新建（修改）链路配置时，如果不填写签名类，Terminator将启动默认签名类。默认签名类形式如下：<br>
					<img src="<%=request.getContextPath()%>/resources/images/intro/signCode.jpg"/><br>
					@DefaultRequestSigner(protocol = "ProtocolName", message = "how to sign request")，message可以介绍签名的简略过程，这个信息会展现在<a href="<%=request.getContextPath()%>/intro/develop/signer.jsp">签名类开发</a>页面。签名类需要有一个构造器传入Link对象。另外，尽量在sign(Object req)中打印原始的请求内容以及纳入签名的内容，在logResponse(RequestResponseBundle bundle)打印返回的类容，这样更方便于调试。
				</p>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>默认抽取类</h4>
				</div>
				<p>
					一种协议只能有一个默认抽取类，但能有多个自定义抽取类，当用户新建（修改）链路配置时，如果不填写抽取类，Terminator将启动默认抽取类。默认抽取类形式如下：<br>
					<img src="<%=request.getContextPath()%>/resources/images/intro/extractCode.jpg"/><br>
					@DefaultRequestExtractor(protocol = "ProtocolName", message = "how to extract request")，message可以介绍抽取的简略过程，这个信息会展现在<a href="<%=request.getContextPath()%>/intro/develop/extractor.jsp">抽取类开发</a>页面。抽取类需要有一个构造器传入Link对象。
				</p>
			</div>
		</div>
		<!--end us-content-->
		<div class="clearfix"></div>
	</div>
	<!--end main-->

</body>
</html>