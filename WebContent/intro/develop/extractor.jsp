<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/common.jsp" flush="true" />
</head>

<script type="text/html" id="defaultExtractors_template">
	<table class="listtbl2">
		<thead>
		<tr>
			<th>名称</th>
			<th>适用协议</th>
			<th>说明</th>
		</tr>
		</thead>
		{{#extractors}}
		<tr>
			<td>{{name}}</td>
			<td class="name3">{{protocol}}</td>
			<td class="name3">{{message}}</td>
		</tr>
		{{/extractors}}
	</table>
</script>

<script type="text/html" id="customizedExtractors_template">
	<table class="listtbl2">
		<thead>
		<tr>
			<th>名称</th>
			<th>适用协议</th>
			<th>说明</th>
		</tr>
		</thead>
		{{#extractors}}
		<tr>
			<td>{{name}}</td>
			<td class="name3">{{protocol}}</td>
			<td class="name3">{{message}}</td>
		</tr>
		{{/extractors}}
	</table>
</script>

<body>

	<jsp:include page="/include/header.jsp" flush="true">
		<jsp:param name="index" value="3" />
	</jsp:include>

	<div class="main">
		<jsp:include page="/intro/develop/devMenu.jsp" flush="true">
			<jsp:param name="index" value="3" />
		</jsp:include>

		<div class="us-content">
			<div class="cont">
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>现有抽取类</h4>
				</div>
				
				<h5>默认抽取类</h5>
				<div id="defaultExtractors"></div>
				
				<h5>自定义抽取类</h5>
				<div id="customizedExtractors"></div>
				<br>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>抽取类开发</h4>
				</div>
				
				<h5>1.默认抽取类示例</h5>
				<p>
					在How To Start中的<a
						href="<%=request.getContextPath()%>/intro/start/stub.jsp#extractClass">通用桩模式</a>
					提到了抽取类的作用，这里以HTTP协议为例介绍默认抽取类，例如以下Http请求如下：<br><br> 
					
					GET /search HTTP/1.1<br> 
					Host:www.baidu.com<br> 
					User-Agent: Mozilla/5.0 (Windows NT 6.1;
					WOW64; rv:22.0) Gecko/20100101 Firefox/22.0<br> 
					Accept:text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8<br>
					Accept-Language: zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3<br>
					Accept-Encoding: gzip, deflate<br> 
					Cookie:BAIDUID=FD5F8A39EC43ED671CF42D50005B861B:FG=1;
					BAIDU_WISE_UID=wapp_1374997101327_887;<br> 
					Connection:keep-alive<br>
				</p>
				这里会抽取为以下几个键值对：<br>
				<table class="listtbl2">
					<thead>
						<tr>
							<th>键</th>
							<th>值</th>
						</tr>
					</thead>
					<tr>
						<td>METHOD</td>
						<td>GET</td>
					</tr>
					<tr>
						<td>URI</td>
						<td>/search</td>
					</tr>
					<tr>
						<td>Host</td>
						<td>www.baidu.com</td>
					</tr>
					<tr>
						<td>User-Agent</td>
						<td>Mozilla/5.0 (Windows NT 6.1; WOW64; rv:22.0) Gecko/20100101 Firefox/22.0</td>
					</tr>
					<tr>
						<td>Accept</td>
						<td>text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8</td>
					</tr>
					<tr>
						<td>Accept-Language</td>
						<td>zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3</td>
					</tr>
					<tr>
						<td>Accept-Encoding</td>
						<td>gzip, deflate</td>
					</tr>
					<tr>
						<td>Cookie</td>
						<td>BAIDUID=FD5F8A39EC43ED671CF42D50005B861B:FG=1; BAIDU_WISE_UID=wapp_1374997101327_887;</td>
					</tr>
					<tr>
						<td>Connection</td>
						<td>keep-alive</td>
					</tr>
				</table>
				<br>
				
				<h5 id="whyCustomized">2.什么时候需要自定义抽取类</h5>
				<p>
					系统提供了默认抽取类以及一些开箱即用的抽取类，但不能包含所有的情况，特别是通过一些自定义格式进行数据传输。当需要的匹配的条件在请求元素（即抽取后的键值对）中找到不到时，就需要自己书写抽取类将请求抽取为自己可以匹配的粒度。
				</p>

				<h5>3.自定义抽取类</h5>
				<p>
					自定义抽取类只需要实现com.baidu.terminator.plugin.extractor.Extractor接口，同时提供一个传入Link对象的构造器。接口方法如下:<br>
					<img alt="request extractor"
						src="<%=request.getContextPath()%>/resources/images/intro/extractor.jpg">
					<br>另外需要在实现类上加上@CustomizedExtractor(protocol = "ProtocolName", message = "how to extract request")。
					实现完以后放到com.baidu.terminator.plugin.extractor包下随terminator一同编译部署。<br>
					最后，在链路配置的抽取类中填上自定义抽取类的全限定名（包名+类名）或者直接复制"现有抽取类"表格中的名称。
				</p>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		$.ajaxSetup({cache:false});
		baseUrl = '<%=request.getContextPath()%>'
		
		$(document).ready(function() {
			$.Mustache.addFromDom('defaultExtractors_template');
			$.Mustache.addFromDom('customizedExtractors_template');
			listDefaultSigners();
			listCustomizedSigners();
		});
		
		function listDefaultSigners(){
			$.getJSON(baseUrl + '/link/defaultExtractors', function(data) {
				$('#defaultExtractors').empty().mustache('defaultExtractors_template', {
					extractors : data
				});
			});
		}
		
		function listCustomizedSigners(){
			$.getJSON(baseUrl + '/link/customizedExtractors', function(data) {
				$('#customizedExtractors').empty().mustache('customizedExtractors_template', {
					extractors : data
				});
			});
		}
		
	</script>

</body>
</html>