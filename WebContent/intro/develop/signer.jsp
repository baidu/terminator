<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/common.jsp" flush="true" />
</head>

<script type="text/html" id="defaultSigners_template">
	<table class="listtbl2">
		<thead>
		<tr>
			<th>名称</th>
			<th>适用协议</th>
			<th>说明</th>
		</tr>
		</thead>
		{{#signers}}
		<tr>
			<td>{{name}}</td>
			<td class="name3">{{protocol}}</td>
			<td class="name3">{{message}}</td>
		</tr>
		{{/signers}}
	</table>
</script>

<script type="text/html" id="customizedSigners_template">
	<table class="listtbl2">
		<thead>
		<tr>
			<th>名称</th>
			<th>适用协议</th>
			<th>说明</th>
		</tr>
		</thead>
		{{#signers}}
		<tr>
			<td>{{name}}</td>
			<td class="name3">{{protocol}}</td>
			<td class="name3">{{message}}</td>
		</tr>
		{{/signers}}
	</table>
</script>

<body>

	<jsp:include page="/include/header.jsp" flush="true">
		<jsp:param name="index" value="3" />
	</jsp:include>

	<div class="main">
		<jsp:include page="/intro/develop/devMenu.jsp" flush="true">
			<jsp:param name="index" value="2" />
		</jsp:include>

		<div class="us-content">
			<div class="cont">
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>现有签名类</h4>
				</div>
				
				<h5>默认签名类</h5>
				<div id="defaultSigners"></div>
				
				<h5>自定义签名类</h5>
				<div id="customizedSigners"></div>
				<br>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>签名类开发</h4>
				</div>
				
				<h5>1.默认签名类示例</h5>
				<p>
					在How To Start中的<a
						href="<%=request.getContextPath()%>/intro/start/record.jsp#signclass">录制回放模式</a>
					提到了签名类的作用，这里以HTTP协议为例介绍默认签名类，例如以下Http请求如下：<br><br> 
					
					GET /s?wd=%E7%99%BE%E5%BA%A6 HTTP/1.1<br> Host:
					www.baidu.com<br> User-Agent: Mozilla/5.0 (Windows NT 6.1;
					WOW64; rv:22.0) Gecko/20100101 Firefox/22.0<br> Accept:
					text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8<br>
					Accept-Language: zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3<br>
					Accept-Encoding: gzip, deflate<br> Cookie:
					BAIDUID=FD5F8A39EC43ED671CF42D50005B861B:FG=1;
					BAIDU_WISE_UID=wapp_1374997101327_887;<br> Connection:
					keep-alive<br> <br>

					会选出Version：HTTP/1.1，URI：/s?wd=%E7%99%BE%E5%BA%A6，method：GET，Body：空（因为是GET请求）拼接起来计算MD5值。<br>
				</p>
				
				<h5 id="whyCustomized">2.什么时候需要自定义签名类</h5>

				<p>
					当触发同一动作时请求内容不同的时候需要根据业务场景重写签名类。例如以下情况，当A模块调用B模块的一个API：查询现在时间点之前已完成的订单，
					A模块每次需要将现在的时间发送给B模块，这样在录制时即便录制到了数据，但在回放时肯定找不到对应的应答，因为两个请求的内容不一样。这时候就需要自定义签名类：当请求的是这个API时，过滤时间字段，即不把时间作为签名的内容。<br>
					签名类本质上是做一件事：将请求中不变的并且可以基本唯一（有些查询请求很难做到唯一）标识一个请求的特征值提取出来计算一个固定值。
				</p>

				<h5>3.自定义签名类</h5>
				<p>
					自定义签名类只需要实现com.baidu.terminator.signer.Signer接口，同时提供一个传入Link对象的构造器。接口方法如下:<br>
					<img alt="request signer"
						src="<%=request.getContextPath()%>/resources/images/intro/signer.jpg">
					<br>另外需要在实现类上加上@CustomizedSigner(protocol = "ProtocolName", message = "how to sign request")。
					实现完以后放到com.baidu.terminator.plugin.signer包下随terminator一同编译部署。<br>
					最后，在链路配置的签名类中填上自定义签名类的全限定名（包名+类名）或者直接复制"现有签名类"表格中的名称。
				</p>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		$.ajaxSetup({cache:false});
		baseUrl = '<%=request.getContextPath()%>'
		
		$(document).ready(function() {
			$.Mustache.addFromDom('defaultSigners_template');
			$.Mustache.addFromDom('customizedSigners_template');
			listDefaultSigners();
			listCustomizedSigners();
		});
		
		function listDefaultSigners(){
			$.getJSON(baseUrl + '/link/defaultSigners', function(data) {
				$('#defaultSigners').empty().mustache('defaultSigners_template', {
					signers : data
				});
			});
		}
		
		function listCustomizedSigners(){
			$.getJSON(baseUrl + '/link/customizedSigners', function(data) {
				$('#customizedSigners').empty().mustache('customizedSigners_template', {
					signers : data
				});
			});
		}
		
	</script>

</body>
</html>